package com.tms.util.license;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * For managing general licenses (serial codes).
 * <p>
 * <pre>
 * A - Product prefix
 * B - Product version
 * C - Maximum number of 'license'. Can be number of users, sites, etc.
 * D - Country code
 * E - Running number
 * F - Encoded expiry date. "00000" means no expiry
 * G - Check codes for the serial
 *
 * A  B C D E     F    G
 * ___--__--______-----__________
 * OPU40TDMY000503WU1JOJJI9RRSUKJ
 * </pre>
 *
 */
public class License implements Serializable {
    private String serialCode;          // full serial code with check codes
    private boolean validSerialCode;
    private String prefix;
    private String version;
    private int maxLicense;
    private String countryCode;
    private Date expiryDate;


    /**
     * Generates a generic license with checkcodes.
     *
     * @param prefix
     * @param version
     * @param variation
     * @param countryCode
     * @param runningNumber
     * @param expiry specifies expiry date, null if no expiry date
     * @return
     */
    public static License generateLicense(
            String prefix, String version, String variation,
            String countryCode, int runningNumber, Date expiry) throws LicenseException {

        String serialCode;
        String checkCode;

        serialCode = prefix + version + variation + countryCode +
                makeRunningNumber(runningNumber) +
                makeEncodedDate(expiry);

        serialCode = serialCode.toUpperCase();
        checkCode = generateCheckCodes(serialCode);

        License l = new License(serialCode + checkCode);
        if(!l.isValidSerialCode()) {
            throw new LicenseException("Error generating license based on given parameters!");
        }

        return l;
    }

    private static String makeEncodedDate(Date expiry) {
        if (expiry == null) {
            return "00000";
        }

        String dayCodes = "ZAQ1XSW2CDE3VFR4BGT5NHY6MJU7KI8";
        String monthCodes = "IJNUHBYGVTFC";
        int day,month,year;
        Calendar cal = Calendar.getInstance();

        cal.setTime(expiry);
        day = cal.get(Calendar.DAY_OF_MONTH) - 1;
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        String encodedDate = "";
        encodedDate += dayCodes.charAt(day);
        encodedDate += monthCodes.charAt(month);
        encodedDate += Integer.toString(year, 36);

        return encodedDate;
    }

    private static String makeRunningNumber(int runningNumber) {
        String str = Integer.toString(runningNumber);

        while (str.length() < 6) {
            str = "0" + str;
        }

        while (str.length() > 6) {
            str = str.substring(1);
        }

        return str;
    }

    /**
     * Default constructor.
     */
    public License(String serialCode) {
        this.serialCode = serialCode;
        this.validSerialCode = calcSerialCode();
        this.prefix = calcPrefix();
        this.version = calcVersion();
        this.maxLicense = calcMaxLicense();
        this.countryCode = calcCountry();
        this.expiryDate = calcExpiryDate();
    }

    /**
     * Checks to see whether or not the supplied serial code is valid
     * @return true if the serial code is valid
     */
    public boolean calcSerialCode() {
        boolean isValid = true;
        String actualRegCode = "";
        String suppliedCheckCodes = "";
        String actualCheckCodes = "";

        if (serialCode == null) {
            return false;
        }

        if (serialCode.length() == 30) {
            actualRegCode = serialCode.substring(0, 20);
            suppliedCheckCodes = serialCode.substring(20, 30);
            actualCheckCodes = generateCheckCodes(actualRegCode);
            if (!actualCheckCodes.equals(suppliedCheckCodes))
                isValid = false;
        } else {
            isValid = false;
        }
        return (isValid);
    }

    /**
     * Generates check codes.
     *
     * @param actualRegCode specifies the registration code without check
     * codes. Length of this String should be 20.
     * @return check codes for the given actual registration code
     */
    private static String generateCheckCodes(String actualRegCode) {
        String actualCheckCodes = "";
        int i;
        int combinedAsciiValue;

        for (i = 9; i >= 0; i--) {
            combinedAsciiValue = actualRegCode.charAt(i) + actualRegCode.charAt(i + 10);
            actualCheckCodes += Integer.toString((combinedAsciiValue % 36), 36).toUpperCase();
        }
        return actualCheckCodes;
    }

    public String calcPrefix() {
        if (!isValidSerialCode())
            return null;
        return serialCode.substring(0, 3);
    }

    /**
     * Calculates the version from the serial number
     * @return
     */
    public String calcVersion() {
        if (!isValidSerialCode())
            return null;
        return serialCode.substring(3, 5);
    }

    /**
     * Retrieves the maximum number of licensed content sources allowed
     * @return the maximum number of content sources, -1 if invalid serial code.
     */
    public int calcMaxLicense() {
        String alphaCodes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String strMaxSources = "00";
        int maxSources = -1, digit1, digit2;

        if (!isValidSerialCode())
            return -1;

        try {
            strMaxSources = serialCode.substring(5, 6);
            if (alphaCodes.indexOf(strMaxSources) >= 0) {
                digit1 = alphaCodes.indexOf(strMaxSources) + 1;
                digit1 *= 100;
                strMaxSources = serialCode.substring(6, 7);
                digit2 = Integer.parseInt(strMaxSources);
                digit2 *= 10;
                maxSources = digit1 + digit2;
            } else {
                strMaxSources += serialCode.substring(6, 7);
                maxSources = Integer.parseInt(strMaxSources);
            }
        } catch (Exception e) {
            maxSources = -1; // Illegal maxSources Code
        }

        return (maxSources);
    }

    public String calcCountry() {
        if (!isValidSerialCode())
            return null;
        return serialCode.substring(7, 9);
    }

    /**
     * Retrieves the expiry date for the license
     * @return a Date representing the expiry date, null if code is invalid or no expiry
     */
    public Date calcExpiryDate() {
        Calendar expiryCal = Calendar.getInstance();
        Date expiryDate = null;
        String dateCode = "";
        String dayCodes = "ZAQ1XSW2CDE3VFR4BGT5NHY6MJU7KI8";
        String monthCodes = "IJNUHBYGVTFC";
        int day,month,year;

        if (!isValidSerialCode())
            return null;

        try {
            dateCode = serialCode.substring(15, 20);
            if (!dateCode.equals("00000")) {
                day = dayCodes.indexOf(serialCode.substring(15, 16));
                month = monthCodes.indexOf(serialCode.substring(16, 17));
                year = Integer.parseInt(serialCode.substring(17, 20), 36);
                expiryCal.set(Calendar.DAY_OF_MONTH, day + 1);
                expiryCal.set(Calendar.MONTH, month);
                expiryCal.set(Calendar.YEAR, year);
                expiryCal.set(Calendar.HOUR_OF_DAY, 23);
                expiryCal.set(Calendar.MINUTE, 59);
                expiryCal.set(Calendar.SECOND, 59);
                expiryDate = expiryCal.getTime();
            }
        } catch (Exception e) {
            expiryDate = null;
        }
        return (expiryDate);
    }

    /**
     * Checks to see whether the license is still valid for the current date
     * @return true if valid
     */
    public boolean isExpired() {
        return hasNotExpired(new Date());
    }

    /**
     * Checks to see whether the license is still valid for the specified date
     * @return true if valid
     */
    public boolean hasNotExpired(Date date) {
        Date expiryDate = getExpiryDate();
        if (expiryDate == null)
            return true;
        else
            return (expiryDate.compareTo(date) > 0);
    }

    /**
     * Returns the registration code (20 chars) for a valid license.
     * @return null if the license is invalid.
     */
    public String getRegistrationCode() {
        if (isValidSerialCode()) {
            return serialCode.substring(0, 20);
        } else {
            return null;
        }
    }


    // === [ getters/setters] ==================================================
    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public boolean isValidSerialCode() {
        return validSerialCode;
    }

    public void setValidSerialCode(boolean validSerialCode) {
        this.validSerialCode = validSerialCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMaxLicense() {
        return maxLicense;
    }

    public void setMaxLicense(int maxLicense) {
        this.maxLicense = maxLicense;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
