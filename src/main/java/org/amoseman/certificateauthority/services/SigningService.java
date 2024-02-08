package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.csr.CertificateSigningRequest;

import java.util.List;

public interface SigningService {
    boolean request(CertificateSigningRequest csr);
    void accept(String name);
    void reject(String name);
    List<CertificateSigningRequest> list();
}
