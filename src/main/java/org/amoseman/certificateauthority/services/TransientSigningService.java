package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.dao.KeyStoreCertificateDAO;
import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Component
@Qualifier("transientSigningService")
public class TransientSigningService implements SigningService {
    @Autowired
    @Qualifier("requestTimeoutMinutes")
    private long requestTimeoutMinutes;
    @Autowired
    @Qualifier("keyStoreCertificateDAO")
    private KeyStoreCertificateDAO keyStoreCertificateDAO;
    private ConcurrentHashMap<String, CertificateSigningRequest> requests;
    private ScheduledExecutorService executorService;
    private SecureRandom random;
    private ConcurrentHashMap<String, X509Certificate> acceptedRequests;

    public TransientSigningService() {
        this.requests = new ConcurrentHashMap<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            for (String code : requests.keySet()) {
                CertificateSigningRequest csr = requests.get(code);
                long ageMilliseconds = System.currentTimeMillis() - csr.getCreated();
                //double ageMinutes = (double) ageMilliseconds / 60_000;
                if (ageMilliseconds < requestTimeoutMinutes * 60_000) {
                    continue;
                }
                requests.remove(code);
            }
        }, 5, TimeUnit.SECONDS);
        this.random = new SecureRandom();
        this.acceptedRequests = new ConcurrentHashMap<>();
    }

    private boolean exists(CertificateSigningRequest csr) {
        for (CertificateSigningRequest request : requests.values()) {
            if (csr.equals(request)) {
                return true;
            }
        }
        return keyStoreCertificateDAO.exists(csr);
    }

    private String generateCode(int n) {
        /*
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
         */
        return Base64.getEncoder().encodeToString(random.generateSeed(n));

    }

    @Override
    public Optional<String> request(CertificateSigningRequest csr) {
        if (exists(csr)) {
            return Optional.empty();
        }
        String code = generateCode(18);
        csr.setTemporaryCode(code);
        requests.put(code, csr);
        return Optional.of(code);
    }

    @Override
    public boolean accept(String name, String password) {
        CertificateSigningRequest target = null;
        for (String code : requests.keySet()) {
            CertificateSigningRequest csr = requests.get(code);
            if (name.equals(csr.getName())) {
                target = csr;
                requests.remove(code);
                break;
            }
        }
        if (null == target) {
            return false;
        }
        X509Certificate certificate = keyStoreCertificateDAO.create(target, password);
        acceptedRequests.put(target.getTemporaryCode(), certificate);
        return true;
    }

    @Override
    public boolean reject(String name) {
        for (String code : requests.keySet()) {
            CertificateSigningRequest csr = requests.get(code);
            if (name.equals(csr.getName())) {
                requests.remove(code);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CertificateSigningRequest> list() {
        return new ArrayList<>(requests.values());
    }

    @Override
    public boolean isPending(String code) {
        return requests.containsKey(code);
    }

    @Override
    public Optional<byte[]> getAcceptedRequestCertificate(String code) {
        X509Certificate certificate = acceptedRequests.remove(code);
        if (certificate == null) {
            return Optional.empty();
        }
        try {
            byte[] bytes = certificate.getEncoded();
            return Optional.of(bytes);
        } catch (CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
