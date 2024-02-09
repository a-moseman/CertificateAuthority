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

    /**
     * Determine if there is a pending certificate signing request corresponding to the provided temporary code.
     * @param code the temporary code.
     * @return if there is such a request.
     */
    boolean isPending(String code);

    /**
     * Get the X.509 certificate of a presumably accepted certificate signing request corresponding to the provided temporary code.
     * @param code the temporary code.
     * @return an Optional containing the encoded bytes of the X.509 certificate, which will be empty if there was no accepted request.
     */
    Optional<byte[]> getAcceptedRequestCertificate(String code);
}
