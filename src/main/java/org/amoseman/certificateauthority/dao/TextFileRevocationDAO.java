package org.amoseman.certificateauthority.dao;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TextFileRevocationDAO implements RevocationDAO {
    @Autowired
    private String revocationListPath;

    @Override
    public boolean contains(BigInteger serialNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(revocationListPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BigInteger number = new BigInteger(line);
                if (serialNumber.equals(number)) {
                    return true;
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void add(BigInteger serialNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(revocationListPath))) {
            writer.append(serialNumber.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(BigInteger serialNumber) {
        // read the file minus the target serial number
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(revocationListPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BigInteger number = new BigInteger(line);
                if (serialNumber.equals(number)) {
                    continue;
                }
                builder.append(line).append('\n');
            }
            builder.delete(0, builder.length()); // remove the last newline
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        // write the new contents that lack the target serial number
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(revocationListPath))) {
            writer.write(builder.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BigInteger> list() {
        List<BigInteger> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(revocationListPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BigInteger number = new BigInteger(line);
                list.add(number);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
