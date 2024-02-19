package org.amoseman.certificateauthority.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;

public class Authorizer {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MEMBER = "MEMBER";
    public static final String ROLE_GUEST = "GUEST";

    @Autowired
    private String rootCertificateName;
    @Autowired
    private String adminCertificateName;
    @Autowired
    private String signingCertificateName;

    public User authorize(HttpServletRequest httpServletRequest) {
        X509Certificate certificate = (X509Certificate) httpServletRequest.getAttribute("jakarta.servlet.request.X509Certificate");
        String subject = certificate.getSubjectX500Principal().getName();
        String issuer = certificate.getIssuerX500Principal().getName();
        Date now = Date.from(Instant.now());
        boolean isExpired = certificate.getNotAfter().after(now) || certificate.getNotBefore().before(now);

        UserBuilder builder = new UserBuilder();
        builder.setUsername(subject);
        builder.setIsExpired(isExpired);

        if (issuer.equals(rootCertificateName) && subject.equals(adminCertificateName)) {
            builder.setRoles(ROLE_ADMIN, ROLE_MEMBER);
        }
        else if (issuer.equals(signingCertificateName)) {
            builder.setRoles(ROLE_MEMBER);
        }
        else {
            builder.setRoles(ROLE_GUEST);
        }
        return builder.build();
    }
}
