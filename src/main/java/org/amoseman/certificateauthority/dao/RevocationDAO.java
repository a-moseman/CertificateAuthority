package org.amoseman.certificateauthority.dao;

import java.math.BigInteger;
import java.util.List;

public interface RevocationDAO {
    boolean contains(BigInteger serialNumber);
    void add(BigInteger serialNumber);
    void remove(BigInteger serialNumber);
    List<BigInteger> list();
}
