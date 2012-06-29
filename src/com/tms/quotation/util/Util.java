package com.tms.quotation.util;

import java.util.regex.Pattern; 
import java.lang.reflect.Array;

public final class Util {
  
  public static String ArrayToList(String[] string, String pattern) {
    StringBuffer tempString = new StringBuffer();

    for (int i = 0; i < Array.getLength(string); i++) {
        if (string[i] != null) {
            if (!"".equals(string[i].trim())) {
                if (i == 0) {
                    tempString.append(string[i]);
                }
                if (i != 0) {
                    tempString.append(pattern + string[i]);
                }
            }
        }
    }
    return tempString.toString();
  }

  public static String ArrayToList(String[] string, char pattern) {
    return ArrayToList(string, String.valueOf(pattern));
  }

  public static String ArrayToList(String[] string) {
    return ArrayToList(string, ",");
  }
  
  public static String[] ListToArray(String string, String pattern) {
    Pattern p = Pattern.compile(pattern);
    String[] arrTemp = p.split(string);
    return arrTemp;
  }
  
  public static String[] ListToArray(String string, char pattern) {
    return ListToArray(string, String.valueOf(pattern));
  }

  public static String[] ListToArray(String string) {
    return ListToArray(string, ",");
  }
  
  public static String PrependToList(String string, String pre, String pattern) {
    String [] arrTemp = ListToArray(string, pattern);
    for( int i=0; i<arrTemp.length; i++){
      arrTemp[i]=pre+arrTemp[i].trim();
    }
    return ArrayToList(arrTemp, pattern);
  }
}
