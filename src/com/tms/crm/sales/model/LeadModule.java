package com.tms.crm.sales.model;

import kacang.model.DefaultModule;
import kacang.model.DaoException;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Date;
import java.util.Collection;

import com.tms.crm.sales.misc.AccessUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Jul 21, 2004
 * Time: 2:10:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeadModule extends DefaultModule {
    private LeadDao dao = null;


    public Lead getLead(String id) throws LeadException {
        dao = (LeadDao) getDao();
        try {
            return dao.selectLead(id);
        } catch (DaoException e) {
            throw new LeadException("Error selecting lead"+e.getMessage(),e);
        }

    }


    public int countLeads(String keyword,String userId) throws LeadException {
        dao = (LeadDao) getDao();
        try {
            return dao.countLeads(keyword,userId);
        } catch (DaoException e) {
            throw new LeadException("Error counting leads"+e.getMessage(),e);
        }
    }

    public Collection listLeads(String keyword, String userId, String sort, boolean desc,int startIndex,int rows) throws LeadException {
        dao = (LeadDao) getDao();

        try {
            return dao.listLeads(keyword,userId,sort,desc,startIndex,rows);
        } catch (DaoException e) {

            throw new LeadException("Error listing leads "+e.getMessage(),e);
        }
    }

    public void updateLead(Lead lead) throws LeadException {
        dao = (LeadDao) getDao();
        try {
            dao.updateLead(lead);
        } catch (DaoException e) {
            throw new LeadException("Error updating lead"+e.getMessage(),e);
        }

    }


    public void addLead(Lead lead) throws LeadException {
        dao = (LeadDao) getDao();
        lead.setId(UuidGenerator.getInstance().getUuid());
        lead.setCreationDate(new Date());
        try {
            dao.insertLead(lead);
        } catch (DaoException e) {
            throw new LeadException("Error adding lead "+e.getMessage(),e);
        }
    }

    public void deleteLead(String userId, String leadId) throws LeadException {
        if(AccessUtil.isSalesManager(userId)||AccessUtil.isLeadOwner(userId,leadId)){
            dao = (LeadDao) getDao();
            try {
                dao.deleteLead(leadId);
            } catch (DaoException e) {
                throw new LeadException("Error deleting lead "+e.getMessage(),e);
            }
        }
    }

    public int countLeadBySource(String sourceId){
		LeadDao dao = (LeadDao) getDao();
        try {
            return dao.countLeadBySource(sourceId);
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        return 0;

    }

}
