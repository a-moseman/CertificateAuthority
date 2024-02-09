package org.amoseman.certificateauthority.encoding;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class Encoding {
    public static Optional<X509Certificate> decode(byte[] data) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            InputStream inputStream = new ByteArrayInputStream(data);
            X509Certificate certificate = (X509Certificate) factory.generateCertificate(inputStream);
            return Optional.of(certificate);
        }
        catch (CertificateException e) {
            return Optional.empty();
        }
    }
}
