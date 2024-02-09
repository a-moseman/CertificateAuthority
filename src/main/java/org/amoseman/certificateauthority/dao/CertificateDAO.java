package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;

import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateDAO {
    boolean exists(CertificateSigningRequest csr);
    X509Certificate create(CertificateSigningRequest csr);
    List<X509Certificate> list();
}
