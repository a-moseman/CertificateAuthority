package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.csr.CertificateSigningRequest;

public interface CertificateDAO {
    boolean exists(CertificateSigningRequest csr);
    void create(CertificateSigningRequest csr);
}
