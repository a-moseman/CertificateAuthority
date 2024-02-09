package org.amoseman.certificateauthority.data;

public class CertificateSigningRequest {
    private String name;
    private String publicKey;
    private long created;
    private String temporaryCode;

    public CertificateSigningRequest() {

    }

    public CertificateSigningRequest(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
        this.created = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public long getCreated() {
        return created;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getTemporaryCode() {
        return temporaryCode;
    }

    public void setTemporaryCode(String temporaryCode) {
        this.temporaryCode = temporaryCode;
    }
}
