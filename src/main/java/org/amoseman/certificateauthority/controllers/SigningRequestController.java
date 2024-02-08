package org.amoseman.certificateauthority.controllers;

import org.amoseman.certificateauthority.csr.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.SigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SigningRequestController {
    @Autowired
    private SigningService signingService;

    @RequestMapping(
            value = "/request",
            method = RequestMethod.POST
    )
    public HttpStatus request(CertificateSigningRequest csr) {
        if (signingService.request(csr)) {
            return HttpStatus.ACCEPTED;
        }
        return HttpStatus.BAD_REQUEST;
    }
}
