package com.tms.hr.competency;

import java.util.Collection;

import kacang.model.DaoException;

public class CompetencyDaoDB2 extends CompetencyDao{
	
	public Collection retrieveCompetencies(String username,String competencyId,String level, String sort, boolean desc, int start, int rows ) throws DaoException{

	   String orderBy = (sort != null) ? sort : " comp.competencyName";
	
	   if (desc) {
		   orderBy += " DESC";
	   }
	
	   if(username !=null)
		   username ="%"+username.toUpperCase()+"%";
	   
	   if(username ==null && competencyId ==null && level==null)
		   return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id ORDER BY "+orderBy,CompetencyListObject.class,null,0,-1);
	
	   if(username !=null  && competencyId ==null && level==null)
		   return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE UPPER(user.username) LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username},0,-1);
	
	   if(username==null && competencyId !=null && level==null)
		   return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE comp.competencyId=? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{competencyId},0,-1);
	   
	   if(username==null && competencyId ==null && level!=null)
	       return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{level},0,-1);
	   
	   if(username!=null && competencyId ==null && level!=null)
	       return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE UPPER(user.username) LIKE ? AND cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username,level},0,-1);
	   
	   else
		   return super.select("SELECT user.username, comp.competencyName, comp.competencyType, comp.competencyDescription, cuser.competencyLevel FROM competency_user cuser left join competency comp ON cuser.competencyId = comp.competencyId left join security_user user on cuser.userId = user.id WHERE UPPER(user.username) LIKE ? AND comp.competencyId=? AND cuser.competencyLevel LIKE ? ORDER BY "+orderBy,CompetencyListObject.class,new Object[]{username,competencyId,level},0,-1);
	
	
	
	
	}

}
