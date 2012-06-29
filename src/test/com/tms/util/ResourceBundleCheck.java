package test.com.tms.util;

import junit.framework.TestCase;

import java.io.*;
import java.util.*;

/**
 * Note: System.out used intentionally to ease copy & paste missing keys.
 * TODO: update property files directly by copying missing keys to secondary file?d
 * TODO: IMPORTANT! BUG! does not check for '\n' - must be converted manually
 */
public class ResourceBundleCheck extends TestCase {
    String dirBase = "../languages/";
    String dirMain = "en/";
    String dirSecondary = "es/";

    /**
     * Check for missing keys in secondary language bundle.
     */
    public void testMissingKeys() {
        int totalMissing = 0;
        String[] files = getFiles(dirBase + dirMain);

        try {
            for (int i = 0; i < files.length; i++) {
                String file = files[i];
                if (file.toLowerCase().endsWith(".properties")) {
                    totalMissing += checkMissingKeys(file);
                }
            }

            assertTrue("TOTAL missing keys is " + totalMissing, totalMissing==0);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Error!");
        }
    }

    private String[] getFiles(String path) {
        File file = new File(path);

        return file.list();
    }

    /**
     * @param file
     * @return number of missing keys, 0 is no missing key
     * @throws IOException
     */
    private int checkMissingKeys(String file) throws IOException {
        PropertyResourceBundle main;
        PropertyResourceBundle second;
        InputStream in1 = null;
        InputStream in2 = null;
        Map missingMap = new LinkedHashMap();

        System.out.println("================================================================================");
        System.out.print("Checking " + file + "...");

        try {
            in1 = new FileInputStream(new File(dirBase + dirMain, file));
            in2 = new FileInputStream(new File(dirBase + dirSecondary, file));
            main = new PropertyResourceBundle(in1);
            second = new PropertyResourceBundle(in2);
        } finally {
            if (in1 != null) {
                try {
                    in1.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }

        Enumeration enume;
        enume = main.getKeys();
        while (enume.hasMoreElements()) {
            String key = (String) enume.nextElement();
            try {
                second.getString(key);
            } catch (MissingResourceException e) {
                missingMap.put(key, main.getString(key));
            }
        }

        if (!missingMap.isEmpty()) {
            System.out.println(missingMap.size() + " missing items in " + dirSecondary);
            System.out.println("# === Missing keys identified by " + getClass().getName() + " ===");
            Iterator ite = missingMap.keySet().iterator();
            while (ite.hasNext()) {
                String key = (String) ite.next();
                System.out.println(key + "=" + missingMap.get(key));
            }

            return missingMap.size();
        } else {
            System.out.println("no missing item in " + dirSecondary);
            return 0;
        }

    }
}
