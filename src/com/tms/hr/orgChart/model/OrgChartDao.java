package com.tms.hr.orgChart.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.JdbcUtil;
import kacang.util.UuidGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: blake
 * Date: Mar 15, 2006
 * Time: 4:07:16 PM
 */
public class OrgChartDao extends DataSourceDao {
    private static final String orgSetupProperties = "code, shortDesc, longDesc, active";
    private static final String deptTable = "org_chart_department";
    private static final String countryTable = "org_chart_country";
    private static final String titleTable = "org_chart_title";
    private static final String stationTable = "org_chart_station";
    private static final String hierachyProperties = "userId, titleCode, deptCode, countryCode, stationCode, org_chart_hierachy.active";
    private static final String hierachyProperties2 = "userId, titleCode, deptCode, countryCode, stationCode, org_chart_hierachy.active, " +
    		"hod, gender, staffNumber, contactHouseTelNumber, contactOfficeDirectLineNumber, contactOfficeGeneralLineNumber, " +
    		"contactHpNumber, passportNumber, passportExpiryDate, icNumber, dateOfBirth, remarks, email, deleted";

    public void init() throws DaoException {
    	try{
            super.update("CREATE TABLE org_chart_department (\n" +
                    "  code varchar(40) NOT NULL default '0',\n" +
                    "  shortDesc varchar(255) NOT NULL default '0',\n" +
                    "  longDesc varchar(255) default '0',\n" +
                    "  active char(1) NOT NULL default '0',\n" +
                    "  PRIMARY KEY  (code),\n" +
                    "  UNIQUE KEY code (code),\n" +
                    "  KEY (shortDesc)" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_country (\n" +
                    "  code varchar(40) NOT NULL default '0',\n" +
                    "  shortDesc varchar(255) NOT NULL default '0',\n" +
                    "  longDesc varchar(255) default '0',\n" +
                    "  active char(1) NOT NULL default '0',\n" +
                    "  PRIMARY KEY  (code),\n" +
                    "  UNIQUE KEY code (code),\n" +
                    "  KEY (shortDesc)" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }
        
        try{
            super.update("CREATE TABLE org_chart_dept_country (\n" +
            		"  associativityId varchar(255) NOT NULL default '0',\n" +
                    "  deptCode varchar(40) NOT NULL default '0',\n" +
                    "  countryCode varchar(40) NOT NULL default '0'\n" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_title (\n" +
                    "  code varchar(40) NOT NULL default '0',\n" +
                    "  shortDesc varchar(255) NOT NULL default '0',\n" +
                    "  longDesc varchar(255) default '0',\n" +
                    "  active char(1) NOT NULL default '0',\n" +
                    "  PRIMARY KEY  (code),\n" +
                    "  UNIQUE KEY code (code),\n" +
                    "  KEY (shortDesc)" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_station (\n" +
                    "  code varchar(40) NOT NULL default '0',\n" +
                    "  shortDesc varchar(255) NOT NULL default '0',\n" +
                    "  longDesc varchar(255) default '0',\n" +
                    "  active char(1) NOT NULL default '0',\n" +
                    "  PRIMARY KEY  (code),\n" +
                    "  UNIQUE KEY code (code),\n" +
                    "  KEY (shortDesc)" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_hierachy (\n" +
                    "  userId varchar(255) NOT NULL default '0',\n" +
                    "  titleCode varchar(40) default '0',\n" +
                    "  deptCode varchar(40) default '0',\n" +
                    "  countryCode varchar(40) default '0',\n" +
                    "  stationCode varchar(40) default '0',\n" +
                    "  active char(1) NOT NULL default '0',\n" +
                    "  PRIMARY KEY  (userId),\n" +
                    "  UNIQUE KEY userId (userId),\n" +
                    "  KEY titleCode (titleCode,deptCode,countryCode,stationCode,active)\n" +
                    ") ", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_communicates (\n" +
                    "  id varchar(100) NOT NULL default '0',\n" +
                    "  communicatesId varchar(100) default '0',\n" +
                    "  KEY communicatesId (id, communicatesId)\n" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }

        try{
            super.update("CREATE TABLE org_chart_subordinates (\n" +
                    "  id varchar(100) NOT NULL default '0',\n" +
                    "  subordinatesId varchar(100) default '0',\n" +
                    "  KEY subordinatesId (id, subordinatesId)\n" +
                    ")", null);
        }catch (Exception e){
            //ignore
        }
        
        try{
    		// new field(s) added by Ken Wei on top of ori structure
            super.update("ALTER TABLE org_chart_hierachy ADD hod char(1) NOT NULL default '0' ", null);
            super.update("ALTER TABLE org_chart_hierachy ADD gender char(1) NOT NULL", null);
            super.update("ALTER TABLE org_chart_hierachy ADD staffNumber varchar(255)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD contactHouseTelNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD contactOfficeDirectLineNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD contactOfficeGeneralLineNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD contactHpNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD passportNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD passportExpiryDate date", null);
            super.update("ALTER TABLE org_chart_hierachy ADD icNumber varchar(40)", null);
            super.update("ALTER TABLE org_chart_hierachy ADD dateOfBirth date", null);
            super.update("ALTER TABLE org_chart_hierachy ADD remarks mediumtext", null);
            super.update("ALTER TABLE org_chart_hierachy ADD email varchar(255)", null);
            
        }catch (Exception e){}
        
        try{
    		// new field(s) added by Tien Soon on top of ori structure
            super.update("ALTER TABLE org_chart_hierachy ADD deleted char(1) NOT NULL default '0' ", null);
            
        }catch (Exception e){}
        
        try{
    		// new field(s) added by Khai Foo on top of ori structure
            super.update("ALTER TABLE org_chart_department ADD deptSectionCode varchar(255) default '' ", null);
            
        }catch (Exception e){}

    }

    public Collection findAllSetup(String type, String shortDesc, int start, int rows, String sort, boolean desc, boolean onlyActive) throws DaoException {
        String where = "";
        if(shortDesc != null && !shortDesc.equals("")){
            where = "WHERE (shortDesc LIKE '%" + shortDesc +"%' OR code LIKE '%" + shortDesc + "%')";
            if(onlyActive) where += " AND active='1'";
        }else if(onlyActive){
            where = "WHERE active='1'";
        }

        String table = identifyTable(type);

        return super.select("SELECT "+ orgSetupProperties + " FROM " + table + " " + where + JdbcUtil.getSort(sort, desc), OrgSetup.class, null, start, rows);
    }

    public int countAllSetup(String type, String shortDesc, boolean onlyActive) throws DaoException {
        String where = "";
        if(shortDesc != null && !shortDesc.equals("")){
            where = "WHERE shortDesc LIKE '%" + shortDesc +"%' OR code LIKE '%" + shortDesc + "%')";
            if(onlyActive) where += " AND active='1'";
        }else if(onlyActive){
            where = "WHERE active='1'";
        }

        String table = identifyTable(type);

        int count = 0;
        Collection list = super.select("SELECT COUNT(code) as intCount FROM " + table + " " + where, HashMap.class, null, 0, -1);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public void insertSetup(String type, OrgSetup obj) throws DaoException {
        String table = identifyTable(type);

        super.update("INSERT INTO "+table+"("+orgSetupProperties +") VALUES(#code#, #shortDesc#,#longDesc#,#active#)", obj);
    }

    public boolean codeExist(String type, String code) throws DaoException {
        String table = identifyTable(type);

        Collection col = super.select("SELECT code FROM "+table+ " WHERE code=?", HashMap.class, new Object[]{code}, 0, -1);
        if(col.size() > 0){
            return true;
        }else return false;
    }

    public void deleteSetup(String type, String selectedKey) throws DaoException {
        String table = identifyTable(type);

        super.update("DELETE FROM " +table+" WHERE code=?", new Object[]{selectedKey});
    }

    public OrgSetup selectSetup(String type, String selectedKey) throws DaoException {
        String table = identifyTable(type);

        Collection col = super.select("SELECT " + orgSetupProperties + " from "+table+" WHERE code=?", OrgSetup.class, new Object[]{selectedKey}, 0, -1);
        if(col.size() > 0){
            return (OrgSetup) col.iterator().next();
        }
        return null;
    }

    public void updateSetup(String type, OrgSetup obj) throws DaoException {
        String table = identifyTable(type);
        super.update("UPDATE "+table+" SET shortDesc=#shortDesc#, longDesc=#longDesc#, active=#active# WHERE code=#code#", obj);
    }

    public void insertHierachy(StaffHierachy sh) throws DaoException {
        if(hierachyExist(sh.getUserId())){
            deleteHierachy(sh.getUserId(), true);
        }
        super.update("INSERT INTO org_chart_hierachy ("+hierachyProperties2+") VALUES(#userId#, #titleCode#, #deptCode#, #countryCode#, #stationCode#, #active#, " +
    		"#hod#, #gender#, #staffNumber#, #contactHouseTelNumber#, #contactOfficeDirectLineNumber#, #contactOfficeGeneralLineNumber#, " +
    		"#contactHpNumber#, #passportNumber#, #passportExpiryDate#, #icNumber#, #dateOfBirth#, #remarks#, #email#, '0')", sh);
        //now add new records
        if(sh.getComIds()!=null){
            for(Iterator itr = sh.getComIds().iterator(); itr.hasNext();){
                String comId = (String) itr.next();
                super.update("INSERT INTO org_chart_communicates(id, communicatesId) VALUES(?,?)", new Object[]{sh.getUserId(), comId});
            }
        }
        if(sh.getSubordinatesId()!=null){
            for(Iterator itr = sh.getSubordinatesId().iterator(); itr.hasNext();){
                String subordinatesId = (String) itr.next();
                super.update("INSERT INTO org_chart_subordinates(id, subordinatesId) VALUES(?,?)", new Object[]{sh.getUserId(), subordinatesId});
            }
        }
    }
    
    private String identifyTable(String type){
        String table = "";
        if(OrgChartHandler.TYPE_DEPT.equals(type))
            table = deptTable;
        else if(OrgChartHandler.TYPE_COUNTRY.equals(type))
            table = countryTable;
        else if(OrgChartHandler.TYPE_STATION.equals(type))
            table = stationTable;
        else if(OrgChartHandler.TYPE_TITLE.equals(type))
            table = titleTable;

        return table;
    }

    public boolean hierachyExist(String userId) throws DaoException {
        Collection col = super.select("SELECT userId FROM org_chart_hierachy WHERE userId=? AND deleted='0'", HashMap.class, new Object[]{userId}, 0, -1);
        if(col.size() > 0){
            return true;
        }else return false;
    }
    
    public boolean hierachyDeleted(String userId) throws DaoException {
        Collection col = super.select("SELECT userId FROM org_chart_hierachy WHERE userId=? AND deleted='1'", HashMap.class, new Object[]{userId}, 0, -1);
        if(col.size() > 0){
            return true;
        }else return false;
    }


    public Collection findAllHierachy(String name, int start, int rows, String sort, boolean desc, boolean onlyActive, String deptCode, String countryCode, String titleCode) throws DaoException {
        String filterCode = " deleted='0' ";
        if(deptCode!=null && !deptCode.equals("")){
            //filter deptartment
        	if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " deptCode='"+deptCode+"'";
        }

        if(countryCode!=null && !countryCode.equals("")){
            //filter country
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " countryCode='"+countryCode+"'";
        }

        if(titleCode!=null && !titleCode.equals("")){
            //filter title
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode +=" titleCode='"+titleCode+"'";
        }

        if(onlyActive){
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " org_chart_hierachy.active='1'";
        }

        if(name != null && !name.equals("")){
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " (firstname LIKE '%"+name+"%' OR lastname LIKE '%"+name+"%' )";
        }

        Collection partial = new ArrayList();

        if(filterCode.length() > 1){
            partial = super.select("SELECT "+hierachyProperties+" FROM org_chart_hierachy JOIN security_user ON org_chart_hierachy.userId=security_user.id WHERE " + filterCode + JdbcUtil.getSort(sort, desc), StaffHierachy.class, null, start, rows);
        }else partial = super.select("SELECT "+hierachyProperties+" FROM org_chart_hierachy WHERE deleted='0' "+ JdbcUtil.getSort(sort, desc), StaffHierachy.class, null, start, rows);

        Collection full = new ArrayList();

        for(Iterator itr=partial.iterator(); itr.hasNext();){
            StaffHierachy sh = (StaffHierachy) itr.next();

            Collection communicates = super.select("SELECT communicatesId FROM org_chart_communicates WHERE id=?", HashMap.class, new Object[]{sh.getUserId()}, 0, -1);
            for(Iterator comItr = communicates.iterator(); comItr.hasNext();){
                HashMap comMap = (HashMap) comItr.next();
                sh.addCommunicatesId((String) comMap.get("communicatesId"));
            }

            Collection subordinates = super.select("SELECT subordinatesId FROM org_chart_subordinates WHERE id=?", HashMap.class, new Object[]{sh.getUserId()}, 0, -1);
            for(Iterator subItr = subordinates.iterator(); subItr.hasNext();){
                HashMap subMap = (HashMap) subItr.next();
                sh.addSubordinatesId((String) subMap.get("subordinatesId"));
            }
            full.add(sh);

        }
        return full;
    }

    public int countHierachy(String name, boolean onlyActive, String deptCode, String countryCode, String titleCode) throws DaoException {
        Collection partial = new ArrayList();
        String filterCode = " deleted='0' ";
        if(deptCode!=null && !deptCode.equals("")){
            //filter deptartment
        	if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " deptCode='"+deptCode+"'";
        }

        if(countryCode!=null && !countryCode.equals("")){
            //filter country
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " countryCode='"+countryCode+"'";
        }

        if(titleCode!=null && !titleCode.equals("")){
            //filter title
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode +=" titleCode='"+titleCode+"'";
        }

        if(onlyActive){
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " org_chart_hierachy.active='1'";
        }

        if(name != null && !name.equals("")){
            if(filterCode.length() > 1){
                filterCode += " AND";
            }
            filterCode += " (firstname LIKE '%"+name+"%' OR lastname LIKE '%"+name+"%' )";
        }

        if(filterCode.length() > 1){
            partial = super.select("SELECT COUNT(userId) as intCount FROM org_chart_hierachy JOIN security_user ON org_chart_hierachy.userId=security_user.id WHERE" + filterCode, HashMap.class, null, 0, -1);
        }else partial = super.select("SELECT COUNT(userId) as intCount FROM org_chart_hierachy WHERE deleted='0'", HashMap.class, null, 0, -1);

        int count = 0;
        if(partial.size() > 0){
            HashMap map = (HashMap) partial.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;

    }

    public Collection findHierachyUser(DaoQuery userProperties, String sort, boolean desc, int start, int rows) throws DaoException {
    	StringBuffer sql = new StringBuffer("SELECT id, username, password, weakpass, firstName, lastName, nickName, " +
    			"title, designation, hierachy.email as email1, email2, email3, company, homepage, address, postcode, city, state, country, " +
    			"contactOfficeDirectLineNumber as telOffice, contactHouseTelNumber as telHome, contactHpNumber as telMobile, fax, notes, " +
    			"property1, property2, property3, property4, property5, hierachy.active, locale " +
    			"FROM security_user u, org_chart_hierachy hierachy " +
    			"WHERE u.id = hierachy.userId " + userProperties.getStatement());
    	if(sort != null && !"".equals(sort)) {
    		sql.append(" ORDER BY " + sort + (desc ? " DESC" : ""));
    	}
    	
    	Collection selectedCol = super.select(sql.toString(), HashMap.class, userProperties.getArray(), start, rows);
    	Collection list = new ArrayList();
    	
    	if(selectedCol != null) {
    		if(selectedCol.size() > 0) {
    			for(Iterator itr=selectedCol.iterator(); itr.hasNext();) {
    				HashMap map = (HashMap) itr.next();
    				User user = new User();
                    user.setId(map.get("id").toString());
                    user.setUsername(map.get("username") != null ? map.get("username").toString() : "");
                    user.setPassword(map.get("password") != null ? map.get("password").toString() : "");
                    user.setProperty("weakpass", map.get("weakpass"));
                    user.setProperty("firstName", map.get("firstName"));
                    user.setProperty("lastName", map.get("lastName"));
                    user.setProperty("email1", map.get("email1"));
                    user.setProperty("address", map.get("address"));
                    user.setProperty("postcode", map.get("postcode"));
                    user.setProperty("city", map.get("city"));
                    user.setProperty("state", map.get("state"));
                    user.setProperty("country", map.get("country"));
                    user.setProperty("telOffice", map.get("telOffice"));
                    user.setProperty("telHome", map.get("telHome"));
                    user.setProperty("telMobile", map.get("telMobile"));
                    user.setProperty("fax", map.get("fax"));
                    user.setProperty("active", map.get("active"));
                    user.setProperty("notes", map.get("notes"));
                    user.setProperty("property1", map.get("property1"));
                    user.setProperty("property2", map.get("property2"));
                    user.setProperty("property3", map.get("property3"));
                    user.setProperty("property4", map.get("property4"));
                    user.setProperty("property5", map.get("property5"));
                    user.setProperty("locale", map.get("locale"));
                    
                    list.add(user);
    			}
    		}
    	}
    	
    	return list;
    }
    
    public int countHierachyUser(DaoQuery userProperties) throws DaoException {
    	int totalRecord = 0;
    	StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total " +
    			"FROM security_user u, org_chart_hierachy hierachy " +
    			"WHERE u.id = hierachy.userId " + userProperties.getStatement());
    	
    	Collection selectedCol = super.select(sql.toString(), HashMap.class, userProperties.getArray(), 0, 1);
    	
    	if(selectedCol != null) {
    		if(selectedCol.size() > 0) {
    			HashMap map = (HashMap) selectedCol.iterator().next();
    			totalRecord = Integer.parseInt(map.get("total").toString());
    		}
    	}
    	
    	return totalRecord;
    }

    public Collection findHierachyGroupUser(String groupId, DaoQuery userProperties, String sort, boolean desc, int start, int rows) throws DaoException {
    	StringBuffer sql = new StringBuffer("SELECT id, username, password, weakpass, firstName, lastName, nickName, " +
    			"title, designation, hierachy.email as email1, email2, email3, company, homepage, address, postcode, city, state, country, " +
    			"contactOfficeDirectLineNumber as telOffice, contactHouseTelNumber as telHome, contactHpNumber as telMobile, fax, notes, " +
    			"property1, property2, property3, property4, property5, hierachy.active, locale " +
    			"FROM security_user u, security_user_group userGroup, org_chart_hierachy hierachy " +
    			"WHERE u.id = hierachy.userId " +
    			"AND u.id = userGroup.userId " +
    			"AND userGroup.groupId = ? " + userProperties.getStatement());
    	if(sort != null && !"".equals(sort)) {
    		sql.append(" ORDER BY " + sort + (desc ? " DESC" : ""));
    	}
    	Collection args = new ArrayList();
        args.add(groupId);
        args.addAll(Arrays.asList(userProperties.getArray()));
    	
    	Collection selectedCol = super.select(sql.toString(), HashMap.class, args.toArray(), start, rows);
    	Collection list = new ArrayList();
    	
    	if(selectedCol != null) {
    		if(selectedCol.size() > 0) {
    			for(Iterator itr=selectedCol.iterator(); itr.hasNext();) {
    				HashMap map = (HashMap) itr.next();
    				User user = new User();
                    user.setId(map.get("id").toString());
                    user.setUsername(map.get("username") != null ? map.get("username").toString() : "");
                    user.setPassword(map.get("password") != null ? map.get("password").toString() : "");
                    user.setProperty("weakpass", map.get("weakpass"));
                    user.setProperty("firstName", map.get("firstName"));
                    user.setProperty("lastName", map.get("lastName"));
                    user.setProperty("email1", map.get("email1"));
                    user.setProperty("address", map.get("address"));
                    user.setProperty("postcode", map.get("postcode"));
                    user.setProperty("city", map.get("city"));
                    user.setProperty("state", map.get("state"));
                    user.setProperty("country", map.get("country"));
                    user.setProperty("telOffice", map.get("telOffice"));
                    user.setProperty("telHome", map.get("telHome"));
                    user.setProperty("telMobile", map.get("telMobile"));
                    user.setProperty("fax", map.get("fax"));
                    user.setProperty("active", map.get("active"));
                    user.setProperty("notes", map.get("notes"));
                    user.setProperty("property1", map.get("property1"));
                    user.setProperty("property2", map.get("property2"));
                    user.setProperty("property3", map.get("property3"));
                    user.setProperty("property4", map.get("property4"));
                    user.setProperty("property5", map.get("property5"));
                    user.setProperty("locale", map.get("locale"));
                    
                    list.add(user);
    			}
    		}
    	}
    	
    	return list;
    }
    
    public int countHierachyGroupUser(String groupId, DaoQuery userProperties) throws DaoException {
    	int totalRecord = 0;
    	StringBuffer sql = new StringBuffer("SELECT COUNT(id) AS total " +
    			"FROM security_user u, security_user_group userGroup, org_chart_hierachy hierachy " +
    			"WHERE u.id = hierachy.userId " +
    			"AND u.id = userGroup.userId " +
    			"AND userGroup.groupId = ? " + userProperties.getStatement());
    	Collection args = new ArrayList();
        args.add(groupId);
        args.addAll(Arrays.asList(userProperties.getArray()));
        
    	Collection selectedCol = super.select(sql.toString(), HashMap.class, args.toArray(), 0, 1);
    	
    	if(selectedCol != null) {
    		if(selectedCol.size() > 0) {
    			HashMap map = (HashMap) selectedCol.iterator().next();
    			totalRecord = Integer.parseInt(map.get("total").toString());
    		}
    	}
    	
    	return totalRecord;
    }
    
    public int countAllHierachy() throws DaoException {
        int count = 0;
        Collection list = super.select("SELECT COUNT(userId) as intCount FROM org_chart_hierachy WHERE deleted='0'", HashMap.class, null, 0, -1);
        if(list.size() > 0){
            HashMap map = (HashMap) list.iterator().next();
            count = ((Number)map.get("intCount")).intValue();
        }
        return count;
    }

    public StaffHierachy selectHierachy(String userId) throws DaoException {
        Collection partial = super.select("SELECT " + hierachyProperties2 + " FROM org_chart_hierachy WHERE userId=?", StaffHierachy.class, new Object[]{userId}, 0, -1);
        Collection full = new ArrayList();

        for(Iterator itr=partial.iterator(); itr.hasNext();){
            StaffHierachy sh = (StaffHierachy) itr.next();

            Collection communicates = super.select("SELECT communicatesId FROM org_chart_communicates WHERE id=?", HashMap.class, new Object[]{sh.getUserId()}, 0, -1);
            for(Iterator comItr = communicates.iterator(); comItr.hasNext();){
                HashMap comMap = (HashMap) comItr.next();
                sh.addCommunicatesId((String) comMap.get("communicatesId"));
            }

            Collection subordinates = super.select("SELECT subordinatesId FROM org_chart_subordinates WHERE id=?", HashMap.class, new Object[]{sh.getUserId()}, 0, -1);
            for(Iterator subItr = subordinates.iterator(); subItr.hasNext();){
                HashMap subMap = (HashMap) subItr.next();
                sh.addSubordinatesId((String) subMap.get("subordinatesId"));
            }
            full.add(sh);

        }

        if(full.size() > 0){
            return (StaffHierachy) full.iterator().next();
        }
        return null;
    }

    public void deleteHierachy(String userId, boolean isPhysicalDelete) throws DaoException {
    	if(isPhysicalDelete) {
	        super.update("DELETE FROM org_chart_hierachy WHERE userId=?", new Object[]{userId});
	        super.update("DELETE FROM org_chart_communicates WHERE id=?", new Object[]{userId});
	        super.update("DELETE FROM org_chart_subordinates WHERE id=?", new Object[]{userId});
    	}
    	else { 
    		super.update("UPDATE org_chart_hierachy SET deleted='1' WHERE userId=?", new Object[]{userId});
    	}
    }
    
    public void undeleteHierachy(String userId) throws DaoException {
    	super.update("UPDATE org_chart_hierachy SET deleted='0' WHERE userId=?", new Object[] {userId});
    }
    
    public void insertDepartmentCountryAssociativity(DepartmentCountryAssociativityObject obj) throws DaoException {
    	if(obj.getAssociativityId() == null ||
    			"".equals(obj.getAssociativityId())) {
    		obj.setAssociativityId(UuidGenerator.getInstance().getUuid());
    	}
    	
    	super.update("INSERT INTO org_chart_dept_country (" +
    			"associativityId, deptCode, countryCode) " +
    			"VALUES(#associativityId#, #deptCode#, #countryCode#)", obj);
    }
    
    public Collection selectDepartmentCountryAssociativity(String associativityId, String deptCode, String countryCode,
    		String sort, boolean desc, int start, int rows) throws DaoException {
    	String whereClause = "WHERE dept.code = deptCode AND country.code = countryCode ";
    	if(associativityId != null &&
    			!"".equals(associativityId)) {
    		whereClause += "AND associativityId = '" + associativityId + "' ";
    	}
    	if(deptCode != null &&
    			!"".equals(deptCode)) {
    		whereClause += "AND deptCode = '" + deptCode + "' ";
    	}
    	if(countryCode != null &&
    			!"".equals(countryCode)) {
    		whereClause += "AND countryCode = '" + countryCode + "' ";
    	}
    	
    	String orderClause = "ORDER BY ";
    	if(sort != null && !"".equals(sort)) {
    		orderClause += sort + " ";
    	}
    	else {
    		orderClause += "deptDesc ";
    	}
    	
    	if(desc) {
    		orderClause += " DESC";
    	}
    	
    	Collection col = super.select("SELECT associativityId, deptCode, countryCode, dept.shortDesc deptDesc, country.shortDesc countryDesc " +
    			"FROM org_chart_dept_country, org_chart_department dept, org_chart_country country " + 
    			whereClause +
    			orderClause, DepartmentCountryAssociativityObject.class, null, start, rows);
    	
    	return col;
    }
    
    public int countDepartmentCountryAssociativity(String associativityId, String deptCode, String countryCode) throws DaoException {
    	int totalRecord = 0;
    	
    	String whereClause = "WHERE 0=0 ";
    	if(associativityId != null &&
    			!"".equals(associativityId)) {
    		whereClause += "AND associativityId = '" + associativityId + "' ";
    	}
    	if(deptCode != null &&
    			!"".equals(deptCode)) {
    		whereClause += "AND deptCode = '" + deptCode + "' ";
    	}
    	if(countryCode != null &&
    			!"".equals(countryCode)) {
    		whereClause += "AND countryCode = '" + countryCode + "' ";
    	}
    	
    	Collection col = super.select("SELECT COUNT(associativityId) AS totalRecord " +
    			"FROM org_chart_dept_country " +    			
    			whereClause, HashMap.class, null, 0, -1);
    	if(col != null) {
    		if(col.size() > 0) {
    			HashMap map = (HashMap) col.iterator().next();
    			totalRecord = Integer.parseInt(map.get("totalRecord").toString());
    		}
    	}
    	
    	return totalRecord;
    }
    
    public DepartmentCountryAssociativityObject getAssociatedCountryDept(String userId) throws DaoException {
    	DepartmentCountryAssociativityObject obj = null;
    	
    	Collection col = super.select("SELECT associativityId, dept.code deptCode, dept.shortDesc deptDesc, " +
    			"country.code countryCode, country.shortDesc countryDesc, dept.deptSectionCode deptSectionCode " +
    			"FROM org_chart_dept_country mapping, org_chart_hierachy hierachy, org_chart_department dept, org_chart_country country " +
    			"WHERE hierachy.userId = ? " +
    			"AND hierachy.deptCode = mapping.deptCode " +
    			"AND hierachy.countryCode = mapping.countryCode " +
    			"AND mapping.deptCode = dept.code " +
    			"AND mapping.countryCode = country.code", 
    			DepartmentCountryAssociativityObject.class, new Object[] {userId}, 0, 1);
    	
    	if(col != null) {
    		if(col.size() > 0) {
    			obj = (DepartmentCountryAssociativityObject) col.iterator().next();
    		}
    	}
    	
    	return obj;
    }
    
    public void deleteDepartmentCountryAssociativity(String associativityId) throws DaoException {
    	if(associativityId != null && 
    			!"".equals(associativityId)) {
	    	super.update("DELETE FROM " +
	    			"org_chart_dept_country " +
	    			"WHERE associativityId = ?", new Object[] {associativityId});
    	}
    }
    
    public void insertDeptSetup(OrgSetup obj) throws DaoException {
        super.update("INSERT INTO org_chart_department(code, shortDesc,longDesc,active,deptSectionCode) VALUES(#code#, #shortDesc#,#longDesc#,#active#,#deptSectionCode#)", obj);
    }
    public OrgSetup selectDeptSetup(String selectedKey) throws DaoException {

        Collection col = super.select("SELECT code, shortDesc,longDesc,active,deptSectionCode from org_chart_department WHERE code=?", OrgSetup.class, new Object[]{selectedKey}, 0, -1);
        if(col.size() > 0){
            return (OrgSetup) col.iterator().next();
        }
        return null;
    }

    public void updateDeptSetup(OrgSetup obj) throws DaoException {
        super.update("UPDATE org_chart_department SET shortDesc=#shortDesc#, longDesc=#longDesc#, active=#active#, deptSectionCode=#deptSectionCode# WHERE code=#code#", obj);
    }
    
    public Collection findDeptSetup(String shortDesc, int start, int rows, String sort, boolean desc, boolean onlyActive) throws DaoException {
        String where = "";
        if(shortDesc != null && !shortDesc.equals("")){
            where = "WHERE (shortDesc LIKE '%" + shortDesc +"%' OR code LIKE '%" + shortDesc + "%')";
            if(onlyActive) where += " AND active='1'";
        }else if(onlyActive){
            where = "WHERE active='1'";
        }
        return super.select("SELECT code, shortDesc,longDesc,active,deptSectionCode,cms_content_published.name AS deptDesc FROM org_chart_department LEFT OUTER JOIN cms_content_published ON deptSectionCode=cms_content_published.id  " + where + JdbcUtil.getSort(sort, desc), OrgSetup.class, null, start, rows);
    }
}