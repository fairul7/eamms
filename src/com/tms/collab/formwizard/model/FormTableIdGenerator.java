package com.tms.collab.formwizard.model;



import java.util.Random;

public class FormTableIdGenerator {
  public static FormTableIdGenerator uuidGenerator;

    // random to provide non-repeating seed
    private Random seeder;



    // used to pad short strings in hexFormat()
    private char[] zero = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};

    /**
     * Returns an instance of UUID generator.
     *
     * @return     An instance of UUID generator.
     */
    public static FormTableIdGenerator getInstance() {
        if (uuidGenerator == null) {
            uuidGenerator = new FormTableIdGenerator();
        }
        return uuidGenerator;
    }

    private FormTableIdGenerator() {
        seeder = new Random();

//        int node = seeder.nextInt();
    }


    /**
     * Returns a UUID.
     *
     * @return     a 35 long UUID String.
     */
    public synchronized String getUuid() {
        long timeNow = System.currentTimeMillis();

        // get int value as unsigned
        int timeLow = (int) timeNow & 0xFFFFFFFF;

        // get next random value
        int node = seeder.nextInt();

        return (hexFormat(timeLow, 8)+ hexFormat(node, 8));
    }

    /**
     * Returns hex String (max 10 zeros).
     *
     * @param   val  Number to convert to hex.
     * @param   length  Length of string returned. It will be padded with 0.
     * @return     Zero padded Hex string of specified length.
     */
    private String hexFormat(int val, int length) {
        StringBuffer sb = new StringBuffer(Integer.toHexString(val));

        if (sb.length() < length) {
            sb.append(zero, 0, length - sb.length());
        }

        return sb.toString();
    }
}
