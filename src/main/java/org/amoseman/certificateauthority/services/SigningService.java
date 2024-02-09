package org.amoseman.certificateauthority.services;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;

import java.util.List;
import java.util.Optional;

public interface SigningService {
    /**
     * Request for the Certificate Authority to provide an X.509 certificate using the provided certificate signing request.
     * @param csr the certificate signing request.
     * @return a temporary code for the pending certificate signing request.
     */
    Optional<String> request(CertificateSigningRequest csr);

    /**
     * Accept a pending certificate signing request.
     * @param name the name of the certificate signing request.
     * @return false if no pending certificate signing request matching the provided name was found.
     */
    boolean accept(String name);

    /**
     * Reject a pending certificate signing request.
     * @param name the name of the certificate signing request.
     * @return false if no pending certificate signing request matching the provided name was found.
     */
    boolean reject(String name);

    /**
     * Get a list of the pending certificate signing requests.
     * @return the list of pending certificate signing requests.
     */
    List<CertificateSigningRequest> list();

    boolean isPending(String code);
    Optional<byte[]> getAcceptedRequestCertificate(String code);
}
