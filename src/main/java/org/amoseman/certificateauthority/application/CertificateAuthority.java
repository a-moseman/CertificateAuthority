package org.amoseman.certificateauthority.application;

import org.amoseman.certificateauthority.auth.Authorizer;
import org.amoseman.certificateauthority.controllers.AdminController;
import org.amoseman.certificateauthority.controllers.CertificateController;
import org.amoseman.certificateauthority.controllers.SigningRequestController;
import org.amoseman.certificateauthority.dao.KeyStoreCertificateDAO;
import org.amoseman.certificateauthority.dao.TextFileRevocationDAO;
import org.amoseman.certificateauthority.services.TransientSigningService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        AdminController.class, CertificateController.class, SigningRequestController.class,
        CertificateAuthorityConfiguration.class,
        KeyStoreCertificateDAO.class, TextFileRevocationDAO.class, Authorizer.class, TransientSigningService.class
})
public class CertificateAuthority {
    public static void main(String[] args) {
        SpringApplication.run(CertificateAuthority.class, args);
    }
}
