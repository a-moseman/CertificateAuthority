package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.dao.CertificateDAO;

import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class TransientSigningService implements SigningService {
    private long requestTimeoutMinutes;
    private CertificateDAO certificateDAO;
    private ConcurrentHashMap<String, CertificateSigningRequest> requests;
    private ScheduledExecutorService executorService;
    private SecureRandom random;
    private ConcurrentHashMap<String, X509Certificate> acceptedRequests;

    public TransientSigningService(long requestTimeoutMinutes, CertificateDAO certificateDAO) {
        this.requestTimeoutMinutes = requestTimeoutMinutes;
        this.certificateDAO = certificateDAO;
        this.requests = new ConcurrentHashMap<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            for (String code : requests.keySet()) {
                CertificateSigningRequest csr = requests.get(code);
                long ageMilliseconds = System.currentTimeMillis() - csr.getCreated();
                double ageMinutes = (double) ageMilliseconds / 60_000;
                if (ageMinutes < requestTimeoutMinutes) {
                    return;
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
        return certificateDAO.exists(csr);
    }

    @Override
    public Optional<String> request(CertificateSigningRequest csr) {
        if (exists(csr)) {
            return Optional.empty();
        }
        String code = Base64.getEncoder().encodeToString(random.generateSeed(32));
        csr.setTemporaryCode(code);
        requests.put(code, csr);
        return Optional.of(code);
    }

    @Override
    public boolean accept(String name) {
        CertificateSigningRequest target = null;
        for (String code : requests.keySet()) {
            CertificateSigningRequest csr = requests.get(code);
            if (name.equals(csr.getSelfSignedCertificate().getSubjectX500Principal().getName())) {
                target = csr;
                requests.remove(code);
                break;
            }
        }
        if (null == target) {
            return false;
        }
        X509Certificate certificate = certificateDAO.create(target);
        acceptedRequests.put(target.getTemporaryCode(), certificate);
        return true;
    }

    @Override
    public boolean reject(String name) {
        for (String code : requests.keySet()) {
            CertificateSigningRequest csr = requests.get(code);
            if (name.equals(csr.getSelfSignedCertificate().getSubjectX500Principal().getName())) {
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
        return requests.get(code) != null;
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
