package com.tms.cms.taxonomy;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfReader;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 26, 2005
 * Time: 3:03:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReadPDF {

    //public static String fileName="C:\\Projects\\doc\\J2EE and XML Development - Manning.pdf";
    public static String fileName="C:\\Documents and Settings\\oilai\\My Documents\\My Received Files\\fearfactorMalaysia_Application.pdf";
    public static void main (String[] args) {
        Document doc = new Document();

        try {
            //InputStream fileInputStream = new FileInputStream(fileName);
            PdfReader reader = new PdfReader(fileName);
            HashMap map = reader.getInfo();
            for (Iterator i = map.keySet().iterator();i.hasNext();) {
                System.out.println((String)i.next());
            }
            try {
                System.out.println(map.get("Title").toString());
                System.out.println(map.get("Author").toString());
                System.out.println(map.get("Subject").toString());
                System.out.println(map.get("Keywords").toString());
            }
            catch(Exception e) {}

        }
        catch(Exception e) {
            System.out.println("error : "+e.toString());
        }
    }
}
