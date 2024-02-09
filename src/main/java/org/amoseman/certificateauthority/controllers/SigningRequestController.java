package org.amoseman.certificateauthority.controllers;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.SigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SigningRequestController {
    @Autowired
    private SigningService signingService;

    @RequestMapping(
            value = "/request",
            method = RequestMethod.POST
    )
    public ResponseEntity<String> request(CertificateSigningRequest csr) {
        Optional<String> maybe = signingService.request(csr);
        if (maybe.isEmpty())  {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(maybe.get(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(
            value = "/request",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> pending(String code) {
        if (signingService.isPending(code)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        Optional<byte[]> maybe = signingService.getAcceptedRequestCertificate(code);
        if (maybe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(maybe.get(), HttpStatus.CREATED);
    }
}
