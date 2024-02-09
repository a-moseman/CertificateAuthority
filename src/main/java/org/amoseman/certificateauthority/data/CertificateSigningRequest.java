package org.amoseman.certificateauthority.data;

import java.security.cert.X509Certificate;

public class CertificateSigningRequest {
    private X509Certificate selfSignedCertificate;
    private long created;
    private String temporaryCode;

    public CertificateSigningRequest() {

    }

    public CertificateSigningRequest(X509Certificate selfSignedCertificate) {
        this.selfSignedCertificate = selfSignedCertificate;
        this.created = System.currentTimeMillis();
    }

    public X509Certificate getSelfSignedCertificate() {
        return selfSignedCertificate;
    }

    public void setSelfSignedCertificate(X509Certificate selfSignedCertificate) {
        this.selfSignedCertificate = selfSignedCertificate;
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
        // different name
        if (!selfSignedCertificate.getSubjectX500Principal().getName().equals(other.getSelfSignedCertificate().getSubjectX500Principal().getName())) {
            return false;
        }
        // different public key
        if (!selfSignedCertificate.getPublicKey().equals(other.selfSignedCertificate.getPublicKey())) {
            return false;
        }
        // different serial number
        if (!selfSignedCertificate.getSerialNumber().equals(other.selfSignedCertificate.getSerialNumber())) {
            return false;
        }
        return super.equals(obj);
    }
}
