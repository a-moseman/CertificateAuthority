package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.csr.CertificateSigningRequest;

import java.util.List;

public interface SigningService {
    boolean request(CertificateSigningRequest csr);
    boolean accept(String name);
    boolean reject(String name);
    List<CertificateSigningRequest> list();
}
