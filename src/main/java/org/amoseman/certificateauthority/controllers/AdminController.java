package org.amoseman.certificateauthority.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.SigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private String rootCertificateName;
    @Autowired
    private String adminCertificateName;
    @Autowired
    private SigningService signingService;

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.POST
    )
    public HttpStatus accept(HttpServletRequest httpServletRequest, String name, String password) {
        if (!authorize(httpServletRequest)) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (signingService.accept(name, password)) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping
    public ResponseEntity<List<CertificateSigningRequest>> list(HttpServletRequest httpServletRequest) {
        if (!authorize(httpServletRequest)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(signingService.list(), HttpStatus.OK);
    }

    private boolean authorize(HttpServletRequest httpServletRequest) {
        X509Certificate certificate = (X509Certificate) httpServletRequest.getAttribute("jakarta.servlet.request.X509Certificate");
        if (!certificate.getIssuerX500Principal().getName().equals(rootCertificateName)) {
            return false;
        }
        if (!certificate.getSubjectX500Principal().getName().equals(adminCertificateName)) {
            return false;
        }
        Date now = Date.from(Instant.now());
        if (certificate.getNotAfter().after(now)) {
            return false;
        }
        if (certificate.getNotBefore().before(now)) {
            return false;
        }
        return true;
    }


}
