package org.amoseman.certificateauthority.application;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ca")
public class CertificateAuthorityConfiguration {
    private String keyStorePath;
    private String rootCertificateName;
    private String signingCertificateName;
    private String adminCertificateName;
    private int requestTimeoutMinutes;

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getRootCertificateName() {
        return rootCertificateName;
    }

    public void setRootCertificateName(String rootCertificateName) {
        this.rootCertificateName = rootCertificateName;
    }

    public String getSigningCertificateName() {
        return signingCertificateName;
    }

    public void setSigningCertificateName(String signingCertificateName) {
        this.signingCertificateName = signingCertificateName;
    }

    public String getAdminCertificateName() {
        return adminCertificateName;
    }

    public void setAdminCertificateName(String adminCertificateName) {
        this.adminCertificateName = adminCertificateName;
    }

    public int getRequestTimeoutMinutes() {
        return requestTimeoutMinutes;
    }

    public void setRequestTimeoutMinutes(int requestTimeoutMinutes) {
        this.requestTimeoutMinutes = requestTimeoutMinutes;
    }
}
