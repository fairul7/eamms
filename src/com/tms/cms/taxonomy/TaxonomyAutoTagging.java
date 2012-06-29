package com.tms.cms.taxonomy;

import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import kacang.Application;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;


/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Aug 2, 2005
 * Time: 11:57:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaxonomyAutoTagging {

    private String fileName = "";
    private String contentId = "";
    private String userId = "";

    public TaxonomyAutoTagging(){
    }

    public TaxonomyAutoTagging(String fileName, String contentId, String userId) {
        this.fileName = fileName;
        this.contentId = contentId;
        this.userId = userId;
    }

    public void run() {
        TaxonomyUtil util = new TaxonomyUtil();

        int iIndex = fileName.lastIndexOf(".");
        String extension = fileName.substring(iIndex+1,fileName.length());

        switch(getExtensionBelongsTo(extension)) {
            case 1:
                {
                    // word / excel document
                    try {
                        String[] sProperties = util.getMSFileProperties(fileName);
                        map(sProperties);
                    }
                    catch(Exception e) {}
                    break;
                }
            case 2:
                {
                    //pdf document
                    try {
                        String[] sProperties = util.getPDFFileProperties(fileName);
                        map(sProperties);
                    }catch(Exception e) {}
                    break;
                }
            default:
                {
                    break;
                }
        }
    }

    public void map(String[] properties) {
        if (properties != null && properties.length>0) {
            TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
            for (int i=0; i<properties.length;i++) {
                Collection col = mod.getRelatedNodes(properties[i],-1);
                if (col!=null && col.size()>0) {
                    for (Iterator ite=col.iterator();ite.hasNext();) {
                        TaxonomyNode node = (TaxonomyNode)ite.next();
                        TaxonomyMap map = new TaxonomyMap();
                        map.setContentId(contentId);
                        map.setContentType("document");
                        map.setTaxonomyId(node.getTaxonomyId());
                        map.setMapBy(userId);
                        map.setMapDate(Calendar.getInstance().getTime());

                        mod.addMapping(map);
                        Log.getLog(getClass()).info("Taxonomy Auto Tagging - contentId="+contentId+",taxonomyId="+node.getTaxonomyId()+",date="+map.getMapDate());
                    }
                }
            }
        }

    }

    public int getExtensionBelongsTo(String s) {
        int iRet=0;
        String[] msExtension={"xls","doc","pps","ppt"};
        String[] pdfExtension={"pdf"};
        boolean isChecked=false;

        for (int i=0;i<msExtension.length;i++) {
             if (s.toLowerCase().equals(msExtension[i])) {
                 iRet=1;
                 isChecked=true;
             }
        }

        if (!isChecked) {
            for (int i=0;i<pdfExtension.length;i++) {
                if (s.toLowerCase().equals(pdfExtension[i])) {
                    iRet=2;
                }
            }
        }

        return iRet;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
