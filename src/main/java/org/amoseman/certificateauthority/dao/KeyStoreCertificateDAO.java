package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.distributedcomputersystemtools.certificates.CertificateGenerationResult;
import org.amoseman.distributedcomputersystemtools.certificates.CertificateGenerator;
import org.amoseman.distributedcomputersystemtools.encoding.KeyEncoding;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class KeyStoreCertificateDAO implements CertificateDAO {
    @Autowired
    private String keyStorePath;
    @Autowired
    private String signingKeyStorePath;
    @Autowired
    private String signingCertificateAlias;
    @Autowired
    private String signingPrivateKeyAlias;

    @Override
    public boolean exists(CertificateSigningRequest csr) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(keyStorePath), null);
            Enumeration<String> enumeration = keyStore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                String[] parts = alias.split("-");
                if (parts[0].equals(csr.getName())) {
                    return true;
                }
                if (!parts[1].equals("CERTIFICATE")) {
                    continue;
                }
                X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
                if (certificate.getPublicKey().equals(KeyEncoding.toPublicKey(csr.getPublicKey()))) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public X509Certificate create(CertificateSigningRequest csr, String password) {
        PublicKey publicKey = KeyEncoding.toPublicKey(csr.getPublicKey());
        CertificateGenerationResult result = CertificateGenerator.generateSigned(publicKey, false, csr.getName(), keyStorePath, signingCertificateAlias, signingPrivateKeyAlias, signingKeyStorePath, password);
        return result.getCertificate();
    }

    @Override
    public List<X509Certificate> list() {
        List<X509Certificate> certificates = new ArrayList<>();
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(keyStorePath), null);
            Enumeration<String> enumeration = keyStore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                String[] parts = alias.split("-");
                if (parts[1].equals("CERTIFICATE")) {
                    X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
                    certificates.add(certificate);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return certificates;
    }
}
