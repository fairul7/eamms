package com.tms.cms.taxonomy;

import java.util.HashMap;

import kacang.util.Log;

import com.lowagie.text.pdf.PdfReader;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Jul 26, 2005
 * Time: 4:10:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxonomyUtil {

    public TaxonomyUtil() {

    }

    

    public String[] getPDFFileProperties(String fileName) {
        String[] retList={""};

        try {
            PdfReader reader = new PdfReader(fileName);
            HashMap map = reader.getInfo();
            String subject = map.get("Subject")!=null?map.get("Subject").toString():"";
            String keywords = map.get("Keywords")!=null?map.get("Keywords").toString():"";
            String titles = map.get("Title")!=null?map.get("Title").toString():"";

            String[] subjectList = extractToList(subject);
            String[] keywordsList = extractToList(keywords);
            String[] titlesList = extractToList(titles);
            
            retList = new String[9];
            int iCounter = 0;
            for (int i=0;i<3;i++) {
                if (i>=subjectList.length) {
                     retList[iCounter]="";
                }
                else{
                     retList[iCounter]=subjectList[i];
                }
                iCounter++;
            }
            for (int i=0;i<3;i++) {
                if (i>=keywordsList.length) {
                     retList[iCounter]="";
                }
                else{
                     retList[iCounter]=keywordsList[i];
                }
                iCounter++;
            }
            for (int i=0;i<3;i++) {
                if (i>=titlesList.length) {
                     retList[iCounter]="";
                }
                else{
                     retList[iCounter]=titlesList[i];
                }
                iCounter++;
            }
        }
        catch(Exception e) {
        	Log.getLog(getClass()).error("Error in getting pdf file properties, ", e);
        }
        return retList;
    }

    public String[] getMSFileProperties(String fileName) {
        POIFSPropertyInfo info = new POIFSPropertyInfo(fileName);
        String category = info.getCategory();
        String subject = info.getSubject();
        String keywords = info.getKeywords();
        String title = info.getTitle();

        String[] categoryList = extractToList(category);
        String[] subjectList = extractToList(subject);
        String[] keywordsList = extractToList(keywords);
        String[] titleList = extractToList(title);

        int iCounter=0;
        String[] retList = new String[12];

        for (int i=0;i<3;i++) {
            if (i>=categoryList.length) {
                 retList[iCounter]="";
            }
            else{
                 retList[iCounter]=categoryList[i];
            }
            iCounter++;
        }

        for (int i=0;i<3;i++) {
            if (i>=keywordsList.length) {
                 retList[iCounter]="";
            }
            else{
                 retList[iCounter]=keywordsList[i];
            }
            iCounter++;
        }

        for (int i=0;i<3;i++) {
            if (i>=subjectList.length) {
                 retList[iCounter]="";
            }
            else{
                 retList[iCounter]=subjectList[i];
            }
            iCounter++;
        }

        for (int i=0;i<3;i++) {
            if (i>=titleList.length) {
                 retList[iCounter]="";
            }
            else{
                 retList[iCounter]=titleList[i];
            }
            iCounter++;
        }

        return retList;
    }

    public String[] extractToList(String s) {
        String[] retList={""};
        if (s!=null && s.length()>0) {
            retList = s.split(",");
            if (retList==null || retList.length<=0) {
                retList= new String[1];
                retList[0]=s;
            }
        }

        return retList;
    }

}
