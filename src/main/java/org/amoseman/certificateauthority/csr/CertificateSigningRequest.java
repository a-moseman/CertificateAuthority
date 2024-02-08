package org.amoseman.certificateauthority.csr;

public class CertificateSigningRequest {
    private String name;
    private String publicKey;

    public CertificateSigningRequest(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
