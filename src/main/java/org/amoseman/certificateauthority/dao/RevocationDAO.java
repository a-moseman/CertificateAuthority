package org.amoseman.certificateauthority.dao;

import java.math.BigInteger;
import java.util.List;

public interface RevocationDAO {
    /**
     * Check if the certificate revocation list contains the provided serial number.
     * @param serialNumber the serial number to compare against the list.
     * @return true if it is in the list, false if it is not.
     */
    boolean contains(BigInteger serialNumber);


    /**
     * Add a serial number to the certificate revocation list.
     * @param serialNumber the serial number of the certificate to revoke.
     */
    void add(BigInteger serialNumber);

    /**
     * Remove a serial number from the certificate revocation list.
     * @param serialNumber the serial number of the certificate to un-revoke.
     */
    void remove(BigInteger serialNumber);

    /**
     * Get the list of revoked certificate serial numbers.
     * @return the list of revoked certificate serial numbers.
     */
    List<BigInteger> list();
}
