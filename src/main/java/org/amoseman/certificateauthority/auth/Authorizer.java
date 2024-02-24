package org.amoseman.certificateauthority.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;

@Component("authorizer")
public class Authorizer {
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MEMBER = "MEMBER";
    public static final String ROLE_GUEST = "GUEST";

    @Autowired
    @Qualifier("rootName")
    private String rootName;
    @Autowired
    @Qualifier("adminName")
    private String adminName;
    @Autowired
    @Qualifier("issuerName")
    private String issuerName;

    public User authorize(HttpServletRequest httpServletRequest) {
        X509Certificate certificate = (X509Certificate) httpServletRequest.getAttribute("jakarta.servlet.request.X509Certificate");
        String subject = certificate.getSubjectX500Principal().getName();
        String issuer = certificate.getIssuerX500Principal().getName();
        Date now = Date.from(Instant.now());
        boolean isExpired = certificate.getNotAfter().after(now) || certificate.getNotBefore().before(now);

        UserBuilder builder = new UserBuilder();
        builder.setUsername(subject);
        builder.setIsExpired(isExpired);

        if (issuer.equals(rootName) && subject.equals(adminName)) {
            builder.setRoles(ROLE_ADMIN, ROLE_MEMBER, ROLE_GUEST);
        }
        else if (issuer.equals(issuerName)) {
            builder.setRoles(ROLE_MEMBER, ROLE_GUEST);
        }
        else {
            builder.setRoles(ROLE_GUEST);
        }
        return builder.build();
    }
}
