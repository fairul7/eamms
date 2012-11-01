package com.tms.fms.department.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.util.Log;

public class FMSDepartmentDao extends DataSourceDao
{
    Log log = Log.getLog(getClass());
    
    
    public void init() throws DaoException
    {
    	try{
			super.update("CREATE TABLE fms_department ("+
			"id varchar(255) NOT NULL default '',"+
			"name varchar(255) default NULL,"+
			"description varchar(255) default NULL,"+
			"HOD varchar(255) NOT NULL default '',"+
			"status CHAR(1) default '' NOT NULL " +
            ")",null);      
			
		}catch(Exception er){
			
		}
		
		// create primary key for fms_department
		try {
			super.update("ALTER TABLE fms_department ADD CONSTRAINT PK_fms_department PRIMARY KEY CLUSTERED (id)", null);
		} catch (Exception e) {
		}
		
		try{
			super.update("CREATE TABLE fms_department_alternate_approver (" +
	                "departmentId varchar(255) NOT NULL default '0', " +
	                "userId varchar(255) NOT NULL default '0' " +
	                ")",null);

		}catch(Exception e){
			
		}
		
		// create primary key for fms_department_alternate_approver
		try {
			super.update("ALTER TABLE fms_department_alternate_approver ADD CONSTRAINT PK_fms_department_alternate_approver PRIMARY KEY CLUSTERED (departmentId, userId)", null);
		} catch (Exception e) {
		}
		
		try{
			super.update("CREATE TABLE fms_unit ("+
			"id varchar(255) NOT NULL default '',"+
			"name varchar(255) default NULL,"+
			"description varchar(255) default NULL,"+
			"HOU varchar(255) NOT NULL default ''," +
			"department_id varchar(255) NOT NULL default '',"+
			"status CHAR(1) default '' NOT NULL " +
            ")",null);      
			
		}catch(Exception er){}
		
		// create primary key for fms_unit
		try {
			super.update("ALTER TABLE fms_unit ADD CONSTRAINT PK_fms_unit PRIMARY KEY CLUSTERED (id)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_unit(department_id)
		try {
			super.update("CREATE INDEX department_id ON fms_unit(department_id)", null);
		} catch (Exception e) {
		}
    		
		try{
			super.update("CREATE TABLE fms_unit_alternate_approver (" +
	                "unitId varchar(255) NOT NULL default '0', " +
	                "userId varchar(255) NOT NULL default '0' " +
	                ")",null);

		}catch(Exception e){}
		
		// create primary key for fms_unit_alternate_approver
		try {
			super.update("ALTER TABLE fms_unit_alternate_approver ADD CONSTRAINT PK_fms_unit_alternate_approver PRIMARY KEY CLUSTERED (unitId, userId)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_unit_alternate_approver(userId)
		try {
			super.update("CREATE INDEX userId ON fms_unit_alternate_approver(userId)", null);
		} catch (Exception e) {
		}
		
		addTransportDepartment();
	}
    
	public void addDepartment(FMSDepartment fd)throws DaoException{

        String sql = "INSERT INTO fms_department(id,name,description,HOD,status) " +
            		 "VALUES(?,?,?,?,?)";
        super.update(sql, new Object[]{fd.getId(),fd.getName(),fd.getDescription(),fd.getHOD(),fd.getStatus()});
        	
        addDepartmentApprover(fd);
            
	}
	
	public void addTransportDepartment() throws DaoException {    	 
        //String deptId = Application.getInstance().getProperty("TransportDepartment");
        String deptId = Application.getInstance().getProperty("ManagementService");
        
		Collection groupExist = super.select("SELECT id FROM fms_department WHERE id=?", HashMap.class, new Object[] { deptId }, 0, 1);
        if (groupExist.size() == 0) {
        	FMSDepartment fmsdept= new FMSDepartment();    
        	fmsdept.setId(deptId);
    		fmsdept.setName(FMSDepartmentManager.TRANSPORT_DEPT);
    		fmsdept.setDescription(FMSDepartmentManager.TRANSPORT_DEPT);
    		fmsdept.setHOD(FMSDepartmentManager.SYSTEM_ADMIN);
    		fmsdept.setStatus("1");		
    		addDepartment(fmsdept);
        }
    }
	    
	public void addDepartmentApprover(FMSDepartment dept) throws DaoException
	{				
		String[] str = dept.getDeptApprover();
		for(int i = 0; i < str.length; i++){
				
			super.update("INSERT INTO fms_department_alternate_approver(departmentId,userId) " +		            
			             "VALUES(?,?)", new Object[] {dept.getId(),str[i]});
		}					       
	}
	    
	public Collection selectDepartment(String filter,String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
	    	
	   String strSort= "";
	    	
	   if (sort != null) {
	   		strSort += " ORDER BY " + sort;
	        if (desc)
	        	strSort += " DESC";
	   }else 
	   		strSort += " ORDER BY id ASC";
	        	
	        
	   String filterClause = " WHERE name LIKE '%"+ (filter==null?"":filter) +"%'  ";	        		
	       	        	        
	   String sql = "SELECT id,name,description,HOD,status FROM fms_department " + filterClause + strSort;
	        
	   return super.select(sql,FMSDepartment.class,null,startIndex,maxRows);	       
	       	        	        	
	}
	    
    public Collection getAltApprover(String departmentId) throws DaoException{

    	Collection list = super.select("SELECT userId FROM fms_department_alternate_approver WHERE departmentId =?",HashMap.class,new Object[]{departmentId}, 0, -1);			
		return list;						
	}
	
    public void updateDeptStatus(String deptId, String status)throws DaoException {
    	
    	super.update("UPDATE fms_department SET status = ? WHERE id = ?", new Object[] {status, deptId});
    	
    }
    
    public Collection selectFMSDepartments() throws DaoException
    {
		
        String sql = "SELECT id as id, name as deptName FROM fms_department ORDER BY name";
        return super.select(sql,DefaultDataObject.class,null,0,-1);
    }
    
    public Collection selectActiveFMSDepartments() throws DaoException
    {
		
        String sql = "SELECT id as id, name as deptName FROM fms_department WHERE status='1' ORDER BY name";
        return super.select(sql,DefaultDataObject.class,null,0,-1);
    }
    
    public Collection selectDeptById(String deptId)throws DaoException{
    	
    	String sql = "SELECT id,name,description,HOD,status FROM fms_department WHERE id = ?";        
 	   return super.select(sql,FMSDepartment.class,new Object[]{deptId},0,-1);	 
    	
    }
    
    public Collection selectDepartment() throws DaoException{
    	String sql = "select id, name from fms_department where status='1' ORDER BY name ASC ";
    	return super.select(sql, FMSDepartment.class, null, 0, -1);
    }
    
    public Collection selectUnitBaseOnDepartment(String id) throws DaoException{
    	String sql = "select id, name from fms_unit where department_id=? and status='1' order by name";
    	return super.select(sql, FMSUnit.class, new String[]{id}, 0, -1);
    }
    
    public Collection selectUnit() throws DaoException{
    	String sql = "select id, name, department_id from fms_unit where status='1' order by name";
    	return super.select(sql, FMSUnit.class, null, 0, -1);
    }
    public Collection selectUnitById(String userId) throws DaoException{
    	String sql = 
    		"SELECT u.id, u.name, u.department_id " +
    		"FROM fms_unit u " +
    		"INNER JOIN fms_unit_alternate_approver ap " +
    		"ON u.id = ap.unitId " +
    		"WHERE u.HOU = ? OR ap.userId = ? " +
    		"ORDER BY u.name ";
    	return super.select(sql, FMSUnit.class, new String[] {userId, userId}, 0, -1);
    }
    public void addUnit(FMSUnit fu)throws DaoException{

        String sql = "INSERT INTO fms_unit(id,name,description,HOU,department_id,status) " +
            		 "VALUES(?,?,?,?,?,?)";
        super.update(sql, new Object[]{fu.getId(),fu.getName(),fu.getDescription(),fu.getHOU(),fu.getDepartment_id(),fu.getStatus()});
        	
        addUnitApprover(fu);
            
	}
	    
	public void addUnitApprover(FMSUnit unit) throws DaoException
	{				
		String[] str = unit.getDeptApprover();
		for(int i = 0; i < str.length; i++){
				
			super.update("INSERT INTO fms_unit_alternate_approver(unitId,userId) " +		            
			             "VALUES(?,?)", new Object[] {unit.getId(),str[i]});
		}					       
	}
	
	public Collection selectUnit(String filter,String statusFilter,String sort, boolean desc, int startIndex,int maxRows) throws DaoException{
    	
		   String strSort= "";
		    	
		   if (sort != null) {
		   		strSort += " ORDER BY " + sort;
		        if (desc)
		        	strSort += " DESC";
		   }else 
		   		//strSort += " ORDER BY id ASC";
			   strSort += " ORDER BY name ASC";
		        	
		        
		   String filterClause = " WHERE name LIKE '%"+ (filter==null?"":filter) +"%'  ";
		   
		   String filterStatus = "";
		   if(!statusFilter.equals("") && !statusFilter.equals("-1")){
			   filterStatus = " AND status ="+ statusFilter +" ";
		   }
		       	        	        
		   String sql = "SELECT id,name,description,HOU,department_id,status FROM fms_unit " + filterClause + filterStatus + strSort;
		        
		   return super.select(sql,FMSUnit.class,null,startIndex,maxRows);	       
		       	        	        	
		}
	
	public void updateUnitStatus(String unitId, String status)throws DaoException {
    	
    	super.update("UPDATE fms_unit SET status = ? WHERE id = ?", new Object[] {status, unitId});
    	
    }
	
	public FMSDepartment selectFMSDepartment(String id)throws DaoException{
		
		FMSDepartment fmsDepartment  = null;		
		
		Collection result = super.select("SELECT DISTINCT id,name,description,HOD,status "+        
        "from fms_department WHERE id=?", FMSDepartment.class, new Object[] {id}, 0, 1);
		
		if(result.size()>0){
			fmsDepartment = (FMSDepartment) result.iterator().next();
			fmsDepartment.setDeptApprover(selectFMSDepartmentApprovers(id));
		}
		
		
		return fmsDepartment;
	}
			
	public String[] selectFMSDepartmentApprovers(String deptId) throws DaoException, SecurityException {
        Collection userIdList = new ArrayList();
        
        String sql = "SELECT userId as userId FROM fms_department_alternate_approver WHERE departmentId=?";
        Object[] args = new Object[] { deptId };

        Collection tmp = super.select(sql.toString(), HashMap.class, args, 0, -1);
        for (Iterator i=tmp.iterator(); i.hasNext();) {
            HashMap m = (HashMap)i.next();
            userIdList.add(m.get("userId"));
        }

        return (String[])userIdList.toArray(new String[0]);
	}
	
	public void updateDepartment(FMSDepartment fd)throws DaoException{
    	
    	String sql = "UPDATE fms_department SET name=#name#, description=#description#, HOD=#HOD#,status=#status#" +
    			" WHERE id=#id#";
    	super.update(sql,fd);    	
    	 	
    }
	
	public void deleteDepartmentApprover(String id)throws DaoException{
		
		super.update("DELETE FROM fms_department_alternate_approver WHERE departmentId=?", new Object[] {id});
	}
	
	public FMSUnit selectFMSUnit(String id)throws DaoException{
		
		FMSUnit fmsUnit  = null;		
		
		Collection result = super.select("SELECT DISTINCT id,name,description,HOU,department_id,status "+        
        "from fms_unit WHERE id=?", FMSUnit.class, new Object[] {id}, 0, 1);
		
		if(result.size()>0){
			fmsUnit = (FMSUnit) result.iterator().next();
			fmsUnit.setDeptApprover(selectFMSUnitApprovers(id));
		}
		
		
		return fmsUnit;
	}
	
	public String[] selectFMSUnitApprovers(String unitId) throws DaoException, SecurityException {
        Collection userIdList = new ArrayList();
        
        String sql = "SELECT userId as userId FROM fms_unit_alternate_approver WHERE unitId=?";
        Object[] args = new Object[] { unitId };

        Collection tmp = super.select(sql.toString(), HashMap.class, args, 0, -1);
        for (Iterator i=tmp.iterator(); i.hasNext();) {
            HashMap m = (HashMap)i.next();
            userIdList.add(m.get("userId"));
        }

        return (String[])userIdList.toArray(new String[0]);
	}
	
	public void updateUnit(FMSUnit fu)throws DaoException{
    	
    	String sql = "UPDATE fms_unit SET name=#name#, description=#description#, HOU=#HOU#, department_id=#department_id#, status=#status#" +
    			" WHERE id=#id#";
    	super.update(sql,fu);    	
    	 	
    }
	
	public void deleteUnitApprover(String id)throws DaoException{
		
		super.update("DELETE FROM fms_unit_alternate_approver WHERE unitId=?", new Object[] {id});
	}
	
	public Collection getUnitApprover(String unitId) throws DaoException{

    	Collection list = super.select("SELECT userId FROM fms_unit_alternate_approver WHERE unitId =?",HashMap.class,new Object[]{unitId}, 0, -1);			
		return list;						
	}
	
	public Collection getHODDepartment(String userId, String deptId)throws DaoException{
		
		Collection list = super.select("SELECT id FROM fms_department WHERE HOD = ? AND id = ?",HashMap.class,new Object[]{userId, deptId}, 0, -1);			
		return list;
	}
	
	public Collection getApproverDepartment(String userId, String deptId)throws DaoException{
		
		Collection list = super.select("SELECT departmentId FROM fms_department_alternate_approver WHERE userId = ? AND departmentId = ?",HashMap.class,new Object[]{userId, deptId}, 0, -1);			
		return list;
	}
	
	
	public boolean isHOUUnit(String userId, String unitId)throws DaoException{
		Collection list = super.select("SELECT id FROM fms_unit WHERE HOU = ? AND id = ?",HashMap.class,new Object[]{userId, unitId}, 0, -1);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean isAlternateUnitApprover(String userId)throws DaoException{
		Collection list = super.select("SELECT userId FROM fms_unit_alternate_approver WHERE userId = ?",HashMap.class,new Object[]{userId}, 0, -1);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	
	public String[] getEmailApprovers(String deptId)throws DaoException{
		
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		
		Collection list = super.select("SELECT DISTINCT su.email1 as email FROM fms_department d   " +
										"INNER join fms_department_alternate_approver a ON a.departmentId=d.id   " +
										"INNER join security_user su ON su.department=d.id    " +
										"WHERE d.id=?  " +
										"AND (HOD=su.id OR userId=su.id)",HashMap.class,new Object[]{deptId}, 0, -1);
			
		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		
			colEmails.add(map.get("email"));
		}
		
		return (String[])colEmails.toArray(new String[0]);
		
	}
	
	public String[] getEmailRequestor(String requestId)throws DaoException{
		
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		
		Collection list = super.select("SELECT DISTINCT su.email1 as email FROM fms_tran_request r   " +
										"INNER join security_user su ON su.id=r.requestBy   " +										
										"WHERE r.id=?  ",HashMap.class,new Object[]{requestId}, 0, -1);
			
		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		
			colEmails.add(map.get("email"));
		}
		
		return (String[])colEmails.toArray(new String[0]);
		
	}
	 
	public Map getUserDeptUnit(String userId)throws DaoException{
		
		Map unitdeptmap = new HashMap();
			
		String sql = "SELECT d.name AS department,u.name AS unit FROM fms_register_user r	" +
				"INNER JOIN fms_department d ON d.id=r.department	" +
				"INNER JOIN fms_unit u ON u.id = r.unit		" +
				"where r.id=?	";
		
		Collection coll = super.select(sql, HashMap.class, new Object[] {userId}, 0, -1);
		
		for(Iterator it = coll.iterator(); it.hasNext(); ){
			unitdeptmap = (HashMap) it.next();
			//unitdeptmap.put(map.get("department"), map.get("unit"));
		}
		
		return unitdeptmap;
	}
	
	public Map getExistingUserDeptUnit(String userId)throws DaoException{
		
		Map unitdeptmap = new HashMap();
			
		String sql = "SELECT d.id AS department,u.id AS unit FROM security_user su	" +
				"INNER JOIN fms_department d ON d.id=su.department	" +
				"INNER JOIN fms_unit u ON u.id = su.unit		" +
				"where su.id=?	";
		
		Collection coll = super.select(sql, HashMap.class, new Object[] {userId}, 0, -1);
		
		for(Iterator it = coll.iterator(); it.hasNext(); ){
			unitdeptmap = (HashMap) it.next();			
		}
		
		return unitdeptmap;
	}
	
	
}
