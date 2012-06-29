package com.tms.crm.sales.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DefaultDataObject;

public class LeadDaoDB2 extends LeadDao{
	
	public Collection listLeads(String keyword,String userId, String sort, boolean desc,int sIndex,int rows) throws DaoException {
        String whereClause = getWhereClause(keyword,userId);
        String orderBy     = " ORDER BY " + ((sort != null) ? sort : "creationDate") + ((desc) ? " DESC" : "");
        String sql = "SELECT id,creationDate,companyId,companyName,modifiedDate,modifiedBy,remarks,source.sourceText as sourceName,source,tel,userId,contactName " +
                "FROM sfa_lead LEFT JOIN source ON source.sourceID= sfa_lead.source " + whereClause + orderBy;
        ArrayList objs = new ArrayList();
        if(keyword!=null&&keyword.trim().length()>0){
            String query = "%"+keyword.trim()+"%";
            objs.add(query.toUpperCase());
            objs.add(query);
            objs.add(query.toUpperCase());
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
	
	public int countLeads(String keyword,String userId) throws DaoException {
        String sql = "SELECT COUNT(id) as total FROM sfa_lead "+ getWhereClause(keyword,userId);
        ArrayList objs = new ArrayList();
        if(keyword!=null&&keyword.trim().length()>0){
            String query = "%"+keyword.trim()+"%";
            objs.add(query.toUpperCase());
            objs.add(query);
            objs.add(query.toUpperCase());
            objs.add(query);
        }
        if(userId!=null&&userId.trim().length()>0){
            objs.add(userId);
        }
        Collection col = super.select(sql,HashMap.class,objs.toArray(),0,-1);
        return Integer.parseInt(((HashMap)col.iterator().next()).get("total").toString());
    }
	
	private String getWhereClause(String keyword, String userId){
        String whereClause ="";
        if(keyword!=null&&keyword.trim().length()>0){
            whereClause = " (UPPER(companyName) LIKE ?  OR remarks LIKE ? OR UPPER(contactName) LIKE ? OR tel LIKE ? ) ";
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

}
