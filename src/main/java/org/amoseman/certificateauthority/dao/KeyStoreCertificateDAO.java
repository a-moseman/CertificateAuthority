package org.amoseman.certificateauthority.dao;

import org.amoseman.certificateauthority.data.CertificateSigningRequest;
import org.amoseman.distributedcomputersystemtools.certificates.CertificateGenerationResult;
import org.amoseman.distributedcomputersystemtools.certificates.CertificateGenerator;
import org.amoseman.distributedcomputersystemtools.encoding.KeyEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class KeyStoreCertificateDAO implements CertificateDAO {
    @Autowired
    private String path;
    @Autowired
    private String keyStorePath;
    @Autowired
    private String issuerName;
    @Autowired
    private String organizationName;

    @Override
    public boolean exists(CertificateSigningRequest csr) {
        try {
            File file = new File(String.format("%s/%s.jks", path, csr.getName()));
            if (!file.exists()) {
                return false;
            }
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(file), null);
            Enumeration<String> enumeration = keyStore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                String[] parts = alias.split("-");
                if (!parts[1].equals("CERTIFICATE")) {
                    continue;
                }
                if (parts[0].equals(csr.getName())) {
                    return true;
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
        CertificateGenerationResult result = CertificateGenerator.generateCertificate(publicKey, false, organizationName, csr.getName(), path + "/" + keyStorePath, null, issuerName, password, -1, csr.getOrganizationalUnit());
        return result.getCertificate();
    }

    @Override
    public List<X509Certificate> list() {
        List<X509Certificate> certificates = new ArrayList<>();
        File dir = new File(path + "/" + keyStorePath);
        File[] files = dir.listFiles((d, n)-> n.toLowerCase().endsWith(".jks"));
        if (null == files) {
            throw new RuntimeException("Error with key store directory");
        }
        for (File file : files) {
            try {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(new FileInputStream(file), null);
                Enumeration<String> enumeration = keyStore.aliases();
                while (enumeration.hasMoreElements()) {
                    String alias = enumeration.nextElement();
                    if (!alias.endsWith("-CERTIFICATE")) {
                        continue;
                    }
                    X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
                    certificates.add(certificate);
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return certificates;
    }
}
