package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;

import java.security.cert.X509Certificate;

public interface CertificateDAO {
    boolean exists(CertificateSigningRequest csr);
    X509Certificate create(CertificateSigningRequest csr);
}
