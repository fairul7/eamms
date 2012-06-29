package com.tms.util.license;

import kacang.util.Encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Singleton Activation class.
 */
public class Activation {
    private String seedKey = null;

    private static Activation activation = null;

    public static Activation getInstance() {

        if(activation==null) {
            synchronized(Activation.class) {
                if(activation==null) {
                    activation = new Activation();
                }
            }
        }

        return activation;
    }

    private Activation() {
    }

    /**
     * Get a 32 character MD5-encoded system key that is specific to a
     * PC/server. This key should be used to generate an activation key.
     *
     * @return systemKey
     * @throws java.io.IOException
     */
    public String getSystemKey() throws IOException {
        return Encryption.encrypt(getSystemSeedRawString());
    }

    /**
     * Generates an activation key for the given system key.
     *
     * @param systemKey
     * @param serialNumber
     * @return
     * @throws java.io.IOException
     */
    public String generateActivationKey(String systemKey, String serialNumber) throws IOException {
        String key = systemKey;

        if(systemKey==null || systemKey.trim().length()!=32) {
            throw new IOException("Invalid System Key!");
        }

        for(int i=0; i<5; i++) {
            key = Encryption.encrypt(key + serialNumber);
        }

        return key;
    }

    /**
     * Checks if the activation key is valid for the given system key.
     * @param systemKey
     * @param activationKey
     * @param serialNumber
     * @return true if activationKey is valid for the given systemKey, false
     * otherwise
     * @throws java.io.IOException
     */
    public boolean isActivationKeyValid(String systemKey, String activationKey, String serialNumber) throws IOException {
        String validActivationKey;

        validActivationKey = generateActivationKey(systemKey, serialNumber);

        if(validActivationKey.equals(activationKey)) {
            // activation key correct
            return true;
        } else {
            // invalid activation key
            return false;
        }
    }

    /**
     * This method will generate a system specific seed for machine specific
     * activation.
     *
     * @throws java.io.IOException
     */
    protected String getSystemSeedRawString() throws IOException {
        if(seedKey!=null) {
            return seedKey;
        }

        Process p;
        Runtime rt = Runtime.getRuntime();
        StringBuffer output = new StringBuffer();
        String os = "";

        os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) { //window version
            // get the harddisk volume label
            p = rt.exec("cmd /c vol");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = br.readLine();
            while (line != null) {
                output.append(line);
                line = br.readLine();
            }

            seedKey = output.toString();

        } else if (os.startsWith("linux")) { //linux version
            // get the network card serial number
            p = rt.exec("/sbin/ifconfig eth0");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = br.readLine();

            seedKey = line;
        } else {
            // for other OS, we use generic seed - meaning, might as well don't use a seed! ;-)
            seedKey = "generic seed";
        }

        return seedKey;
    }
}
