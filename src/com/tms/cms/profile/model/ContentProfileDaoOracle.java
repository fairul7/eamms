package com.tms.cms.profile.model;

import kacang.model.DaoException;
import kacang.util.Transaction;

import java.util.Collection;
import java.util.Iterator;

public class ContentProfileDaoOracle extends ContentProfileDao
{
	public void updateData(String contentId, String version, ContentProfile profile) throws DaoException
	{
        if (contentId == null || version == null || profile == null)
            throw new DaoException("ContentProfile is null and data cannot be updated");
        Transaction tx = null;
        try
		{
            tx = getTransaction();
            tx.begin();
            tx.update("DELETE FROM cms_profile_data WHERE contentId=? AND version=?", new Object[] { contentId, version });
            Collection fields = profile.getFields();
            if (fields != null)
			{
                int count=10;
                for (Iterator i=fields.iterator(); i.hasNext(); count+=10)
				{
                    ContentProfileField field = (ContentProfileField)i.next();
                    tx.update("INSERT INTO cms_profile_data (id, type, name, value, contentId, version, ordering, label) VALUES (' ',?,?,?,?,?,?,?)", new Object[] { field.getType(), field.getName(), field.getValue(), contentId, version, new Integer(count), field.getLabel() });
                }
            }
            tx.commit();
        }
        catch (Exception e)
		{
            try
			{
                tx.rollback();
            }
            catch(Exception re)
			{
                ;
            }
            throw new DaoException("Unable to update profile data: " + e.getMessage(), e);
        }
    }
}
