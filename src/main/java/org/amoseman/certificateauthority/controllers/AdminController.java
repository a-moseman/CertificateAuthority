package org.amoseman.certificateauthority.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.amoseman.certificateauthority.auth.Authorizer;
import org.amoseman.certificateauthority.auth.User;
import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.certificateauthority.services.TransientSigningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    @Qualifier("authorizer")
    private Authorizer authorizer;
    @Autowired
    @Qualifier("transientSigningService")
    private TransientSigningService transientSigningService;

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.POST
    )
    public HttpStatus accept(HttpServletRequest httpServletRequest, String name, String password) {
        User user = authorizer.authorize(httpServletRequest);
        if (!user.hasRole(Authorizer.ROLE_ADMIN)) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (transientSigningService.accept(name, password)) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<CertificateSigningRequest>> list(HttpServletRequest httpServletRequest) {
        User user = authorizer.authorize(httpServletRequest);
        if (!user.hasRole(Authorizer.ROLE_ADMIN)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(transientSigningService.list(), HttpStatus.OK);
    }


}
