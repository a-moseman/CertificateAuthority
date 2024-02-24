package org.amoseman.certificateauthority.controllers;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.TransientSigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("transientSigningService")
    private TransientSigningService transientSigningService;

    @RequestMapping(
            value = "/request",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> request(CertificateSigningRequest csr) {
        Optional<String> maybe = transientSigningService.request(csr);
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
        if (transientSigningService.isPending(code)) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        Optional<byte[]> maybe = transientSigningService.getAcceptedRequestCertificate(code);
        if (maybe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(maybe.get(), HttpStatus.CREATED);
    }
}
