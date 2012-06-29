package com.tms.hr.competency;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kacang.model.DaoException;

public class CompetencyDaoMsSql extends CompetencyDao {
	
	public Collection retrieveAllCompetencies() throws DaoException{

        return super.select("SELECT competencyId, competencyName, competencyType, competencyDescription from competency", Competency.class,null, 0,-1);
    }
	
	public Collection retrieveCompetencies(String username,
			String competencyId, String level, String sort, boolean desc,
			int start, int rows) throws DaoException {

		String orderBy = (sort != null) ? sort : " comp.competencyName";

		if (desc) {
			orderBy += " DESC";
		}

		if (username != null)
			username = "%" + username + "%";

		if (username == null && competencyId == null && level == null)
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id ORDER BY "+ orderBy, 
					CompetencyListObject.class,null, start, rows);

		else if (username != null && competencyId == null && level == null)
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id WHERE u.username LIKE ? ORDER BY "+ orderBy, 
					CompetencyListObject.class,new Object[] { username }, start, rows);

		else if (username == null && competencyId != null && level == null)
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id WHERE comp.competencyId=? ORDER BY "+ orderBy, 
					CompetencyListObject.class,new Object[] { competencyId }, start, rows);

		else if (username == null && competencyId == null && level != null)
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id WHERE cuser.competencyLevel LIKE ? ORDER BY "+ orderBy, 
					CompetencyListObject.class,new Object[] { level }, start, rows);
		
		else if (username != null && competencyId == null && level != null)
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id WHERE u.username LIKE ? AND cuser.competencyLevel LIKE ? ORDER BY "+ orderBy, 
					CompetencyListObject.class,new Object[] { username, level }, start, rows);

		else
			return super.select("SELECT u.username, comp.competencyName, comp.competencyType, comp.competencyDescription, " +
					"cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId " +
					"left join security_user u on cuser.userId = u.id WHERE u.username LIKE ? AND comp.competencyId=? AND cuser.competencyLevel LIKE ? ORDER BY "+ orderBy, 
					CompetencyListObject.class,new Object[] { username, competencyId, level },start, rows);
	}
	
	public int countRetrieveCompetencies(String username,String competencyId, String level) throws DaoException{

        Collection result =null;

        if(username !=null)
        	username ="%"+username+"%";

        else if(username ==null && competencyId ==null && level==null)
        	result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id",HashMap.class,null,0,1);

        else if(username !=null  && competencyId ==null && level==null)
        	result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id WHERE u.username LIKE ?",HashMap.class,new Object[]{username},0,1);

        else if(username==null && competencyId !=null && level==null )
        	result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id WHERE comp.competencyId=?",HashMap.class,new Object[]{competencyId},0,1);
        
        else if(username==null && competencyId ==null && level!=null)
        	result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id WHERE cuser.competencyLevel LIKE ? ",HashMap.class,new Object[]{level},0,1);
        
        else if(username!=null && competencyId ==null && level!=null)
            result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id WHERE u.username LIKE ? AND cuser.competencyLevel LIKE ? ",HashMap.class,new Object[]{username,level},0,1);
        
        else
        	result = super.select("SELECT count(*) as total FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user u on cuser.userId = u.id WHERE u.username LIKE ? AND comp.competencyId=? AND cuser.competencyLevel LIKE ?",HashMap.class,new Object[]{username,competencyId,level},0,1);

        Map map= new HashMap();

        if(result.size()>0)
        	map = (HashMap) result.iterator().next();

        return Integer.parseInt(map.get("total").toString());

    }

}
