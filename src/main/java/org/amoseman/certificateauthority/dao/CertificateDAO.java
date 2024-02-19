package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;

import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateDAO {
    /**
     * Check if a certificate already exists for the given certificate signing requests.
     * @param csr the certificate signing request.
     * @return true if the name or public key has already been certified.
     */
    boolean exists(CertificateSigningRequest csr);

    /**
     * Create an X.509 certificate based on the certificate signing request.
     * This also should add the certificate to some sort of data store.
     * @param csr the certificate signing request.
     * @param password the password to access the signing certificate's private key.
     * @return the new X.509 certificate.
     */
    X509Certificate create(CertificateSigningRequest csr, String password);

    /**
     * Get a list of all X.509 certificates granted by the Certificate Authority.
     * This list will also include revoked certificates, which are solely defined by having their serial number in the certificate revocation list.
     * @return the list of certificates.
     */
    List<X509Certificate> list();
}
