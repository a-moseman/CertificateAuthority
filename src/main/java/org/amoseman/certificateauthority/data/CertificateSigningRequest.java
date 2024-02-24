package org.amoseman.certificateauthority.data;

public class CertificateSigningRequest {
    private String organizationalUnit;
    private String name;
    private String publicKey;
    private long created;
    private String temporaryCode;

    public CertificateSigningRequest() {
        created = System.currentTimeMillis();
    }

    public String getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(String organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getCreated() {
        return created;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CertificateSigningRequest)) {
            return false;
        }
        CertificateSigningRequest other = (CertificateSigningRequest) obj;
        if (name.equals(other.name)) {
            return true;
        }
        if (publicKey.equals(other.publicKey)) {
            return true;
        }
        return false;
    }
}
