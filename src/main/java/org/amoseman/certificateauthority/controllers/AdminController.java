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

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    @Qualifier("transientSigningService")
    private TransientSigningService transientSigningService;

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.POST
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public HttpStatus accept(@RequestParam("name") String name, @RequestParam("password") String password) {
        if (transientSigningService.accept(name, password)) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CertificateSigningRequest>> list() {
        return new ResponseEntity<>(transientSigningService.list(), HttpStatus.OK);
    }


}
