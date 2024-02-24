package org.amoseman.certificateauthority.application;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ca")
@ConfigurationPropertiesScan
public class CertificateAuthorityConfiguration {
    private String organizationName;
    private String path;
    private String keyStorePath;
    private String rootName;
    private String issuerName;
    private String adminName;
    private long requestTimeoutMinutes;

    @Bean
    @Qualifier("organizationName")
    public String organizationName() {
        return organizationName;
    }

    @Bean
    @Qualifier("path")
    public String path() {
        return path;
    }

    @Bean
    @Qualifier("keyStorePath")
    public String keyStorePath() {
        return keyStorePath;
    }

    @Bean
    @Qualifier("rootName")
    public String rootName() {
        return rootName;
    }

    @Bean
    @Qualifier("issuerName")
    public String issuerName() {
        return issuerName;
    }

    @Bean
    @Qualifier("adminName")
    public String adminName() {
        return adminName;
    }

    @Bean
    @Qualifier("requestTimeoutMinutes")
    public long requestTimeoutMinutes() {
        return requestTimeoutMinutes;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setRequestTimeoutMinutes(long requestTimeoutMinutes) {
        this.requestTimeoutMinutes = requestTimeoutMinutes;
    }
}
