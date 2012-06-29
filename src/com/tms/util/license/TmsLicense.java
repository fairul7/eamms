package com.tms.util.license;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;
import kacang.Application;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;
import kacang.util.Encryption;
import kacang.util.Log;

import java.io.*;
import java.util.Date;
import java.util.Calendar;

public class TmsLicense {
    public static final String PREFIX_OPU = "OPU";
    public static final String PREFIX_EKP = "EKP";

    protected static String VERSION = "40";

    public static final String LICENSE_PAGE = "/cmsadmin/license.jsp";

    public static final String LICENSE_KEY = "siteLicense";
    public static final int LICENSE_CACHE_DURATION = 60;

    public static final String ACTIVATION_KEY = "siteActivationKey";
    public static final int ACTIVATION_CACHE_DURATION = 60;

    public static final boolean REQUIRE_ACTIVATION = true;

    private static Cache licenseCache;
    private static Cache activationCache;
    public static final String ACTIVATION_STORAGE_DIRECTORY = "/system/";
    public static final String ACTIVATION_STORAGE_FILE = "activationKey.dat";

    static {
        licenseCache = new Cache(true, false, true);
        activationCache = new Cache(true, false, true);
    }

    public static License getLicense() throws SetupException {
        try {
            // get from cache
            License license = null;
            String activationKey = null;

            try {
                license = (License) licenseCache.getFromCache(LICENSE_KEY, LICENSE_CACHE_DURATION);
                if (REQUIRE_ACTIVATION) {
                    activationKey = (String) activationCache.getFromCache(ACTIVATION_KEY, ACTIVATION_CACHE_DURATION);
                }
            } catch (NeedsRefreshException e) {
            }

            Application application = Application.getInstance();
            SetupModule setup = (SetupModule) application.getModule(SetupModule.class);

            if (license == null) {
                // read license from module
                String serialCode = setup.get(LICENSE_KEY);
                license = new License(serialCode);
                licenseCache.putInCache(LICENSE_KEY, license);
            }

            if (REQUIRE_ACTIVATION && activationKey == null) {
                // read activation key from module
                activationKey = TmsLicense.loadActivationKeyFromStorage();
                activationCache.putInCache(ACTIVATION_KEY, activationKey);
            }

            return license;

        } catch (SetupException e) {
            Log.getLog(TmsLicense.class).error("Error retrieving license: " + e.toString());
            throw e;
        }
    }

    public static String getActivationKey() {
            // get from cache
            String activationKey = null;

            try {
                activationKey = (String) activationCache.getFromCache(ACTIVATION_KEY, ACTIVATION_CACHE_DURATION);
            } catch (NeedsRefreshException e) {
            }

            if (activationKey == null) {
                // read activation key from module
                activationKey = TmsLicense.loadActivationKeyFromStorage();
                activationCache.putInCache(ACTIVATION_KEY, activationKey);
            }

            return activationKey;
    }

    public static void flush() {
        licenseCache.flushEntry(LICENSE_KEY);
        if (REQUIRE_ACTIVATION) {
            activationCache.flushEntry(ACTIVATION_KEY);
        }
    }

    public static boolean saveLicense(String serialCode) throws SetupException {
        Application application = Application.getInstance();
        SetupModule setup = (SetupModule) application.getModule(SetupModule.class);

        try {
            // check for valid license
            License license = new License(serialCode);
            boolean isValid = checkLicense(license);
            if (isValid) {
                setup.save(TmsLicense.LICENSE_KEY, serialCode);
                TmsLicense.flush();
            }

            return isValid;
        } catch (SetupException e) {
            throw e;
        }
    }

    public static void saveActivationKey(String activationKey) throws SetupException {
        License license = TmsLicense.getLicense();

        try {
            // check for valid license
            if (REQUIRE_ACTIVATION) {
                Activation act = Activation.getInstance();
                if (act.isActivationKeyValid(act.getSystemKey(), activationKey, license.getRegistrationCode())) {
                    TmsLicense.saveActivationKeyToStorage(activationKey);
                    TmsLicense.flush();
                }
            }
        } catch (Exception e) {
            throw new SetupException("Error saving activation key: " + e.getMessage());
        }
    }

    private static String systemKey = null;

    public static boolean checkLicense() throws SetupException {
        License license = getLicense();

        // check valid license
        boolean isValid = checkLicense(license);
        if (!isValid) {
            licenseCache.flushEntry(LICENSE_KEY);
        }

        // check valid activation, if need to
        if(REQUIRE_ACTIVATION) {
            String activationKey = getActivationKey();
            Activation activation = Activation.getInstance();

            try {
                if(systemKey==null) {
                    synchronized(TmsLicense.class) {
                        systemKey = activation.getSystemKey();
                    }
                }
                isValid = isValid && activation.isActivationKeyValid(systemKey, activationKey, license.getRegistrationCode());
            } catch (IOException e) {
                throw new SetupException("Error checking activation key");
            }

        }

        return isValid;
    }

    protected static boolean checkLicense(License license) throws SetupException {
        try {

            // check validity
            boolean isValid =
                    license.isValidSerialCode()
                    && VERSION.equals(license.getVersion())
                    && license.isExpired();

            // check for valid prefix
            if(PREFIX_OPU.equals(license.getPrefix()) ||
                    PREFIX_EKP.equals(license.getPrefix())) {
                return isValid;
            }

            // no valid prefix found, return false
            return false;

        } catch (Exception e) {
            throw new SetupException("Unable to check license: " + e.getMessage());
        }
    }

    public static void saveActivationKeyToStorage(String activationKey) {
        StorageFile sf;
        StorageService ss;
        String systemDir;
        try {
			systemDir = Encryption.encrypt(Activation.getInstance().getSystemSeedRawString());
		} catch (Exception e1) {
			systemDir = "";
		}
        String activationPath = ACTIVATION_STORAGE_DIRECTORY + systemDir + "/" + ACTIVATION_STORAGE_FILE;  
        ss = (StorageService) Application.getInstance().getService(StorageService.class);
        sf = new StorageFile(activationPath);
        sf.setInputStream(new ByteArrayInputStream(activationKey.getBytes()));
        try {
            ss.store(sf);
            Log.getLog(TmsLicense.class).debug("Activation key saved via storage service");
        } catch (StorageException e) {
            Log.getLog(TmsLicense.class).error("Error saving activation key via storage service", e);
        }
    }

    public static String loadActivationKeyFromStorage() {
        StorageFile sf;
        StorageService ss;
        StringBuffer sb = new StringBuffer();
        BufferedReader br;
        String line;
        String systemDir;
        try {
			systemDir = Encryption.encrypt(Activation.getInstance().getSystemSeedRawString());
		} catch (Exception e1) {
			systemDir = "";
		}
        String activationPath = ACTIVATION_STORAGE_DIRECTORY + systemDir + "/" + ACTIVATION_STORAGE_FILE;  
        ss = (StorageService) Application.getInstance().getService(StorageService.class);
        sf = new StorageFile(activationPath);
        try {
            try {
				sf = ss.get(sf);
			} catch (FileNotFoundException e) {
				// revert to previous, for backward compatibility
				sf = new StorageFile(ACTIVATION_STORAGE_DIRECTORY + ACTIVATION_STORAGE_FILE);
				sf = ss.get(sf);
			}

            br = new BufferedReader(new InputStreamReader(sf.getInputStream()));
            line = br.readLine();
            while(line!=null) {
                sb.append(line);
                line = br.readLine();
            }
            br.close();

            Log.getLog(TmsLicense.class).debug("Activation key loaded via storage service");

            return sb.toString();

        } catch (FileNotFoundException e) {
            Log.getLog(TmsLicense.class).info("Could not load activation key via storage service");
            return "-";
        } catch (StorageException e) {
            Log.getLog(TmsLicense.class).info("Could not load activation key via storage service");
            return "-";
        } catch (IOException e) {
            Log.getLog(TmsLicense.class).info("Could not load activation key via storage service");
            return "-";
        }

    }

    public static Long getLicenseValidityDays(Date dateToCheck) {
        try {
            Date expiryDate = getLicense().getExpiryDate();
            if (expiryDate == null) {
                return null;
            }

            long days = ((expiryDate.getTime() - dateToCheck.getTime()) / (1000*60*60*24)) + 1;
            return new Long(days);
        }
        catch (Exception e) {
            Log.getLog(TmsLicense.class).error("Error calculating license validity days", e);
            return null;
        }
    }

}
