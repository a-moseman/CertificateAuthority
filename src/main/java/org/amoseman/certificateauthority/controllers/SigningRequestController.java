package org.amoseman.certificateauthority.controllers;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.TransientSigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<String> request(CertificateSigningRequest csr) {
        Optional<String> maybe = transientSigningService.request(csr);
        if (maybe.isEmpty())  {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.format("code=%s", maybe.get()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(
            value = "/request",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<byte[]> pending(@RequestParam("code") String code) {
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
