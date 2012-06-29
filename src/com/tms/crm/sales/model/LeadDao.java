package com.tms.crm.sales.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 21, 2004
 * Time: 2:10:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadDao extends DataSourceDao{


    public void insertLead(Lead lead) throws DaoException {
        super.update("INSERT INTO sfa_lead(id,creationDate,companyId,companyName,modifiedDate," +
                "modifiedBy,remarks,source,tel,userId,contactName)VALUES( #id#, #creationDate#, #companyId#, " +
                "#companyName#, #modifiedDate#, #modifiedBy#, #remarks#, #source#, #tel#, #userId#, #contactName#  ) ",lead);
    }


    public void updateLead(Lead lead) throws DaoException {
        super.update("UPDATE sfa_lead SET companyId=#companyId#, companyName=#companyName#," +
                " modifiedDate= #modifiedDate#, modifiedBy= #modifiedBy#, remarks= #remarks#," +
                " source= #source#, tel = #tel# , userId= #userId#,contactName= #contactName# WHERE" +
                " id = #id#  ",lead );
    }

    public Lead selectLead(String id) throws DaoException {
        Collection col = super.select("SELECT id,creationDate,companyId,companyName,modifiedDate,modifiedBy,remarks,source.sourceText as sourceName,source,tel,userId,contactName " +
                "FROM sfa_lead LEFT JOIN source ON source.sourceID= sfa_lead.source  WHERE id=? AND source.sourceID=sfa_lead.source",Lead.class, new Object[]{id},0,-1);
        Lead lead = null;
        if(col.size()>0){
            lead = (Lead) col.iterator().next();
            if(lead.getCompanyId()!=null&&lead.getCompanyId().trim().length()>0){
                DefaultDataObject obj = selectCompanyInfo(lead.getCompanyId());
                if(obj!=null){
                    lead.setCompanyName(selectCompanyName(obj));
                    if(lead.getTel()==null||lead.getTel().trim().length()==0)
                        lead.setTel(selectCompanyTel(obj));
                }
            }
        }
        return lead;
    }


    private DefaultDataObject selectCompanyInfo(String companyId) throws DaoException {
        Collection col = super.select("SELECT companyName as companyName,companyTel as companyTel  FROM company WHERE companyID=?",DefaultDataObject.class, new Object[]{companyId},0,-1);
        DefaultDataObject obj = null;
        if(col!=null&&col.size()>0)
            obj = (DefaultDataObject) col.iterator().next();
        return obj;
    }


    private String selectCompanyName(DefaultDataObject obj){
        String companyName = "Unknown Company";
        //String companyName =(String) obj.getProperty("companyName");
        String tempName = (String) obj.getProperty("companyName");
        if(tempName!=null&&tempName.trim().length()>0)
            companyName = tempName;
        return companyName;
    }

    private String selectCompanyTel(DefaultDataObject obj){
        String companyTel = "-";
        //String companyName =(String) obj.getProperty("companyName");
        String tempTel = (String) obj.getProperty("companyTel");
        if(tempTel!=null&&tempTel.trim().length()>0)
            companyTel= tempTel;
        return companyTel;
    }

    public int countLeads(String keyword,String userId) throws DaoException {
        String sql = "SELECT COUNT(id) as total FROM sfa_lead "+ getWhereClause(keyword,userId);
        ArrayList objs = new ArrayList();
        if(keyword!=null&&keyword.trim().length()>0){
            String query = "%"+keyword.trim()+"%";
            objs.add(query);
            objs.add(query);
            objs.add(query);
            objs.add(query);
        }
        if(userId!=null&&userId.trim().length()>0){
            objs.add(userId);
        }
        Collection col = super.select(sql,HashMap.class,objs.toArray(),0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }



    public Collection listLeads(String keyword,String userId, String sort, boolean desc,int sIndex,int rows) throws DaoException {
        String whereClause = getWhereClause(keyword,userId);
        String orderBy     = " ORDER BY " + ((sort != null) ? sort : "creationDate") + ((desc) ? " DESC" : "");
        String sql = "SELECT id,creationDate,companyId,companyName,modifiedDate,modifiedBy,remarks,source.sourceText as sourceName,source,tel,userId,contactName " +
                "FROM sfa_lead LEFT JOIN source ON source.sourceID= sfa_lead.source " + whereClause + orderBy;
        ArrayList objs = new ArrayList();
        if(keyword!=null&&keyword.trim().length()>0){
            String query = "%"+keyword.trim()+"%";
            objs.add(query);
            objs.add(query);
            objs.add(query);
            objs.add(query);
        }
        if(userId!=null&&userId.trim().length()>0){
            objs.add(userId);

        }
       Collection col = super.select(sql,Lead.class,objs.toArray(),sIndex,rows);
        for (Iterator iterator = col.iterator(); iterator.hasNext();) {
            Lead lead = (Lead) iterator.next();
            if(lead.getCompanyId()!=null&&lead.getCompanyId().trim().length()>0){
                DefaultDataObject obj = selectCompanyInfo(lead.getCompanyId());
                if(obj!=null){
                    lead.setCompanyName(selectCompanyName(obj));
                    if(lead.getTel()==null||lead.getTel().trim().length()==0)
                        lead.setTel(selectCompanyTel(obj));
                }
            }
        }
        return col;        
    }


    private String getWhereClause(String keyword, String userId){
        String whereClause ="";
        if(keyword!=null&&keyword.trim().length()>0){
            whereClause = " (companyName LIKE ?  OR remarks LIKE ? OR contactName LIKE ? OR tel LIKE ? ) ";
        }
        if(userId!=null&&userId.trim().length()>0){
            if(whereClause.length()>0){
                whereClause += " AND ";
            }
            whereClause += " userId = ? ";
        }
        if(whereClause.length()>0)
            whereClause =" WHERE "+whereClause;
        return whereClause;
    }

    public void deleteLead(String leadId) throws DaoException {
        DefaultDataObject obj = new DefaultDataObject();
        obj.setId(leadId);
        super.update("DELETE FROM sfa_lead WHERE id= #id#", obj);

    }
    
    public int countLeadBySource(String sourceId) throws DaoException {
        String sql = "SELECT COUNT(id) AS total FROM sfa_lead " +
                "WHERE source=? ";
        Collection col = super.select(sql,HashMap.class,new Object[]{sourceId},0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }

}
