package org.amoseman.certificateauthority.controllers;

import org.amoseman.certificateauthority.dao.KeyStoreCertificateDAO;
import org.amoseman.certificateauthority.dao.TextFileRevocationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CertificateController {
    @Autowired
    @Qualifier("keyStoreCertificateDAO")
    private KeyStoreCertificateDAO keyStoreCertificateDAO;
    @Autowired
    @Qualifier("textFileRevocationDAO")
    private TextFileRevocationDAO textFileRevocationDAO;

    @RequestMapping(
            value = "/certificates",
            method = RequestMethod.GET
    )
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<List<byte[]>> list() {
        List<X509Certificate> certificates = keyStoreCertificateDAO.list();
        List<byte[]> data = new ArrayList<>();
        for (X509Certificate certificate : certificates) {
            try {
                data.add(certificate.getEncoded());
            }
            catch (CertificateEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(
            value = "crl",
            method = RequestMethod.GET
    )
    @PreAuthorize("hasAuthority('GUEST')")
    public ResponseEntity<List<BigInteger>> certificateRevocationList() {
        List<BigInteger> revokedSerialNumbers = textFileRevocationDAO.list();
        return new ResponseEntity<>(revokedSerialNumbers, HttpStatus.OK);
    }
}
