package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.csr.CertificateSigningRequest;
import org.amoseman.certificateauthority.dao.CertificateDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TransientSigningService implements SigningService {
    private long requestTimeoutMinutes;
    private CertificateDAO certificateDAO;
    private ConcurrentLinkedQueue<CertificateSigningRequest> requests;
    private ScheduledExecutorService executorService;

    public TransientSigningService(long requestTimeoutMinutes, CertificateDAO certificateDAO) {
        this.requestTimeoutMinutes = requestTimeoutMinutes;
        this.certificateDAO = certificateDAO;
        this.requests = new ConcurrentLinkedQueue<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            CertificateSigningRequest top = requests.peek();
            if (top == null) {
                return;
            }
            long ageMilliseconds = System.currentTimeMillis() - top.getCreated();
            double ageMinutes = (double) ageMilliseconds / 60_000;
            if (ageMinutes < requestTimeoutMinutes) {
                return;
            }
            requests.poll();
        }, 1, TimeUnit.MINUTES);
    }

    private boolean exists(CertificateSigningRequest csr) {
        for (CertificateSigningRequest request : requests) {
            if (csr.getName().equals(request.getName())) {
                return true;
            }
        }
        return certificateDAO.exists(csr);
    }

    @Override
    public boolean request(CertificateSigningRequest csr) {
        if (exists(csr)) {
            return false;
        }
        requests.add(csr);
        return false;
    }

    @Override
    public boolean accept(String name) {
        CertificateSigningRequest target = null;
        for (CertificateSigningRequest csr : requests) {
            if (name.equals(csr.getName())) {
                target = csr;
                requests.remove(csr);
                break;
            }
        }
        if (null == target) {
            return false;
        }
        certificateDAO.create(target);
        return true;
    }

    @Override
    public boolean reject(String name) {
        for (CertificateSigningRequest csr : requests) {
            if (name.equals(csr.getName())) {
                requests.remove(csr);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CertificateSigningRequest> list() {
        return new ArrayList<>(requests);
    }
}
