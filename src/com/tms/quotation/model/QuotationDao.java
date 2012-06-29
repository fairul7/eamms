package com.tms.quotation.model;

import com.tms.quotation.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

public class QuotationDao extends DataSourceDao {

	Log log = Log.getLog(getClass());

	private String QuotationCols = "quotationId, customerId, templateId, subject, content, status, openDate, closeDate, recdate, whoModified, tableId, companyId"; //header, footer,

    private String QuotationItemCols = "itemId, quotationId, itemTableRow";//"itemId, itemName, itemDescription, itemUnit, itemQuantity, itemCost, version";

	private String TemplateCols = "templateId, templateName, templateHeader, templateFooter, recdate, whoModified, active";

    private String CustomerCols = "customerId, contactFirstName, contactLastName, companyName, address1, address2, address3, postcode, state, country, gender, salutation, active";
	
//    private String QuotationItemMapCols = "quotationId, itemId, sortNum";
    
    private String QtnColumnCols = "columnId, tableId, position, header, columnClassName, columnStyle, compulsory";

    private String QtnTableCols = "tableId, tableDescription, tableCaption, tableStyle, active";
    
    private String QtnTempltCols = "templateId, templateDescription, templateBody, active";
    
    private String TemplateMapCols = "templateId, templateTextId, sortNum";
    
    private String QtnContentCols = "quotationId, contentType, contentId, sortNum";
    
	public void init() throws DaoException {
		
		
		try {
            super.update("ALTER TABLE quotation "
                  + "ADD COLUMN companyId VARCHAR(100) NULL",
                    null);
        } catch (Exception e) {
        }
        
		try {
			super.update("CREATE TABLE quotation("
					+ "quotationId VARCHAR(255) PRIMARY KEY, "
					+ "customerId VARCHAR(255) NULL, "
					+ "templateId VARCHAR(255) NULL, "
					+ "subject VARCHAR(255) NULL, "
					+ "content TEXT NULL, "
					+ "status VARCHAR(255) NULL, " + "openDate DATETIME NULL, "
					+ "closeDate DATETIME NULL, " + "recdate DATETIME NULL, "
                    + "whoModified VARCHAR(255) NULL )",
					null);
		} catch (Exception e) {
		}

        try {
            super.update("ALTER TABLE quotation "
                  + "ADD COLUMN tableId VARCHAR(255) NULL",
                    null);
        } catch (Exception e) {
        }
        try {
          super.update("CREATE TABLE quotation_item("
              + "itemId VARCHAR(40) PRIMARY KEY, "
              + "quotationId VARCHAR(40) NULL, "
              + "itemTableRow TEXT NULL )", null);
        } catch (Exception e) {}
        
		try {
			super.update("CREATE TABLE quotation_template("
					+ "templateId VARCHAR(255) PRIMARY KEY, "
					+ "templateName VARCHAR(255) NULL, "
					+ "templateHeader TEXT NULL, "
					+ "templateFooter TEXT NULL, " + "recdate DATETIME NULL, "
					+ "whoModified VARCHAR(255) NULL, "
					+ "active VARCHAR(1) NULL)", null);
		} catch (Exception e) {
		}

            
		try{
			super.update("CREATE TABLE quotation_customer(" +
					"customerId VARCHAR(255) PRIMARY KEY, " +
					"contactFirstName VARCHAR(255) NULL, " +
					"contactLastName VARCHAR(255) NULL, " +
					"companyName VARCHAR(255) NULL, " +
					"address1 VARCHAR(255) NULL, " +
					"address2 VARCHAR(255) NULL, " +
					"address3 VARCHAR(255) NULL, " +
					"postcode VARCHAR(255) NULL, " +
					"state VARCHAR(255) NULL, " +
					"country VARCHAR(255) NULL, " +
					"gender VARCHAR(255) NULL, " +
					"salutation VARCHAR(255) NULL)", null);
		} catch (Exception e) {
		}

        try {
          super.update("ALTER TABLE quotation_customer " +
                "ADD COLUMN active VARCHAR(1) NULL", null);
        } catch (Exception e) {}
        
       try{
          super.update("CREATE TABLE quotation_column( " +
              "columnId VARCHAR(40) PRIMARY KEY, " +
              "tableId VARCHAR(40), " +
              "position INT NULL, " +
              "header VARCHAR(255) NULL, " +
              "columnClassName VARCHAR(255) NULL, " +
              "columnStyle TEXT NULL)", null );
       } catch (Exception e) {}

       try{
         super.update("ALTER TABLE quotation_column " +
                "ADD COLUMN compulsory VARCHAR(1) NULL", null);
       } catch (Exception e) {}
       
       try {
         super.update("CREATE TABLE quotation_table( " +
             "tableId VARCHAR(40) PRIMARY KEY, " +
             "tableDescription VARCHAR(255) NULL, " +
             "tableCaption TEXT NULL, " +
             "tableStyle TEXT NULL)", null );
       } catch (Exception e) {}
       
       try {
         super.update("ALTER TABLE quotation_table " +
                "ADD COLUMN active VARCHAR(1) NULL", null);
       } catch (Exception e) {}
       
       try {
         super.update("CREATE TABLE quotation_templates ( " +
             "templateId VARCHAR(40) PRIMARY KEY, " +
             "templateDescription VARCHAR(255) NULL, " +
             "templateBody TEXT NULL )", null );
       } catch (Exception e) {}

       try {
         super.update("ALTER TABLE quotation_templates " +
                "ADD COLUMN active VARCHAR(1) NULL", null);
       } catch (Exception e) {}
       
       String insertTemplates = "INSERT INTO quotation_templates ("+
           QtnTempltCols+") VALUES (?,?,?,?)";
       
       try {
         super.update(insertTemplates, new String[]{QtnTemplateTextDataObject.DB_SUBJECT_ID, "DB - Quotation Subject", "", "1"});
       } catch( Exception e) {}
       try {       
         super.update(insertTemplates, new String[]{QtnTemplateTextDataObject.DB_CUSTOMER_ID, "DB - Customer Information", "", "1"});
       } catch( Exception e) {}
       try {    
         super.update(insertTemplates, new String[]{QtnTemplateTextDataObject.DB_TABLE_ID, "DB - Quotation Items Table", "", "1"});
       } catch( Exception e) {}
       try {
         super.update(insertTemplates, new String[]{QtnTemplateTextDataObject.DB_DATE_ID, "DB - Quotation Date", "", "1"});
       } catch( Exception e) {}
       
       try {
       super.update("CREATE TABLE quotn_tmptext ( " +
            "templateId VARCHAR(40) NOT NULL, " +
            "templateTextId VARCHAR (40) NOT NULL, " +
            "sortNum INT NULL, " +
            "CONSTRAINT PRIMARY KEY (templateId, templateTextId))", null);
       } catch (Exception e) {}
       
       try {
         super.update("CREATE TABLE quotation_content ( "+
             "quotationId VARCHAR(40) NOT NULL, " +
             "contentId VARCHAR(40) NOT NULL, " +
             "contentType VARCHAR(255) NULL, " +
             "sortNum INT NULL," +
             "CONSTRAINT PRIMARY KEY(quotationId, contentId))", null);
       } catch (Exception e) {}
	}

	/* Quotation */

	public void deleteQuotation(String quotationId) throws DaoException {
	  super.update("DELETE FROM quotation WHERE quotationId=?",
	      new String[] { quotationId });
	}

    public void deleteCustomerQuotation( String customerId ) throws DaoException {
      String sel = "SELECT * FROM quotation WHERE customerId=?";
     
      Collection c  = super.select(sel, QuotationDataObject.class, new String[]{customerId}, 0, -1);
      for( Iterator i = c.iterator(); i.hasNext();) {
        QuotationDataObject q = (QuotationDataObject)i.next();
        deleteQtnContent(q.getQuotationId());
        deleteQuotation(q.getQuotationId());
      }
//      super.update("DELETE FROM quotation WHERE customerId=?",
//          new String[] {customerId});
    }
    
    public void deleteTableQuotation( String tableId ) throws DaoException {
      String del = "DELETE FROM quotation WHERE tableId=?";
      String args[] = { tableId };
      super.update(del, args);
    }
    
	public void insertQuotation(QuotationDataObject z) throws DaoException {
		super.update("INSERT INTO quotation ("
					+ QuotationCols
					+ ") VALUES (#quotationId#, " +
                            "#customerId#, " +
                            "#templateId#, " +
                            "#subject#, " +
                            "#content#, " +
                            "#status#, " +
                            "#openDate#, " +
                            "#closeDate#, " +
                            "#recdate#, " +
                            "#whoModified#, " +
//                          "#header#, #footer#, " +
                            
                            "#tableId#,#companyId#)",
					z);
	}

	public Collection selectQuotation(String quotationId) throws DaoException {
		return super.select("SELECT " + QuotationCols
				+ " FROM quotation WHERE quotationId=?",
				QuotationDataObject.class, new String[] { quotationId }, 0, -1);
	}

	public Collection selectQuotation() throws DaoException {
	  return super.select("SELECT " + QuotationCols + " FROM quotation",
				QuotationDataObject.class, new String[] {}, 0, -1);
	}

	public Collection selectQuotation(String search, String id, String status,
			String sort, boolean desc, int start, int rows) throws DaoException {
      ArrayList args = new ArrayList();
      
      
		String search2 = search;
		if ("null".equals(search)) {
			search2 = "";
		} else if (search == null) {
			search2 = "";
		}
		boolean desc2 = desc;
		String sql = "SELECT " + QuotationCols
				+ " FROM quotation WHERE (customerId LIKE ?"
				+ " OR templateId LIKE ?"
				+ " OR subject LIKE ?"
                + " OR content LIKE ?"
				+ " OR status LIKE ?)";
        
        args.add("%"+search2+"%");
        args.add("%"+search2+"%");
        args.add("%"+search2+"%");
        args.add("%"+search2+"%");
        args.add("%"+search2+"%");
        
        if (!("".equals(id))) {
            sql = sql + " AND whoModified=?";
          args.add(id);
        }
        
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND status=?";
            args.add(status);
        }
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY ?";
            args.add(sort);
        }
		if (desc2)
			sql = sql + " DESC";
		return super.select(sql, QuotationDataObject.class, args.toArray(), start, rows);
	}

	public int selectQuotationCount(String search, String id, String status)
	throws DaoException {
	  ArrayList args = new ArrayList();
	  String search2 = search;
	  if ("null".equals(search)) {
	    search2 = "";
	  } else if (search == null) {
	    search2 = "";
	  }
	  String sql = "SELECT count(*) AS total"
	    + " FROM quotation WHERE (customerId LIKE ?"
	    + " OR templateId LIKE ?"
	    + " OR subject LIKE ?"
	    + " OR content LIKE ?"
	    + " OR status LIKE ?)";

	  args.add("%"+search2+"%");
	  args.add("%"+search2+"%");
	  args.add("%"+search2+"%");
	  args.add("%"+search2+"%");
      args.add("%"+search2+"%");
      
	  if (!("".equals(id))) {
	    sql = sql + " AND whoModified=?";
	    args.add(id);
	  }
	  if (!("-1".equals(status) || "".equals(status))) {
	    sql = sql + " AND status=?";
	    args.add(status);
	  }
	  Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	  return Integer.parseInt(row.get("total").toString());
	}

	public Collection selectQuotationstatus() throws DaoException {
		return super.select("SELECT DISTINCT status FROM quotation",
				QuotationDataObject.class, new String[] {}, 0, -1);
	}

	public void updateQuotation(QuotationDataObject z) throws DaoException {
      String sql = "UPDATE quotation " +
            "SET customerId=#customerId#, templateId=#templateId#, " +
            "subject=#subject#, content=#content#, " +
            "status=#status#, openDate=#openDate#, " +
            "closeDate=#closeDate#, recdate=#recdate#, " +
            "whoModified=#whoModified#, " +
//            "header=#header#, " +
//            "footer=#footer#, " +
            "tableId=#tableId#" +
            "WHERE quotationId=#quotationId#";
		super.update(sql, z);
	}
	
	/* QuotationItem */

	  public void deleteQuotationItem(String itemId) throws DaoException {
	    super.update("DELETE FROM quotation_item WHERE itemId=?", new String[] {itemId});
	  }
	  
      public void deleteQuotationIdItem(String quotationId) throws DaoException {
        String sql = "DELETE FROM quotation_item WHERE quotationId=?";
        super.update(sql, new String[] {quotationId});
      }
	  public void insertQuotationItem(QuotationItemDataObject z) throws DaoException {
        String sql = "INSERT INTO quotation_item (" 
          + QuotationItemCols
          + ") VALUES (#itemId#, #quotationId#, #itemTableRow#)"; //#itemName#, #itemDescription#, #itemUnit#, #itemQuantity#, #itemCost#, #version#)";
	    super.update(sql, z);
	  }
      
	  public Collection selectQuotationItemQtnId( String quotationId ) throws DaoException {
        String sql = "SELECT "+QuotationItemCols+" FROM quotation_item WHERE quotationId=?";
	    return super.select(sql, QuotationItemDataObject.class, new String[] {quotationId}, 0, -1);
      }
      
	  public Collection selectQuotationItem(String itemId) throws DaoException {
	    return super.select("SELECT "+QuotationItemCols+" FROM quotation_item WHERE itemId=?", QuotationItemDataObject.class, new String[] {itemId}, 0, -1);
	  }

	  public Collection selectQuotationItem() throws DaoException {
	    return super.select("SELECT "+QuotationItemCols+" FROM quotation_item", QuotationItemDataObject.class, new String[] {}, 0, -1);
	  }

      public void updateQuotationItem(QuotationItemDataObject z) throws DaoException {
        String sql = "UPDATE quotation_item SET itemTableRow=#itemTableRow# WHERE itemId=#itemId#";
        super.update(sql, z);
//	    super.update("UPDATE quotation_item SET itemName=#itemName#, itemDescription=#itemDescription#, itemUnit=#itemUnit#, itemQuantity=#itemQuantity#, itemCost=#itemCost#, version=#version#, WHERE itemId=#itemId#", z);
	  }
	  
	/* Template */

	public void deleteTemplate(String templateId) throws DaoException {
      String args[] = {templateId};
      super.update("DELETE FROM quotn_tmptext WHERE templateId=?", args);
      super.update("DELETE FROM quotation_template WHERE templateId=?",args);
	}

	public void insertTemplate(TemplateDataObject z) throws DaoException {
	  super
	  .update(
	      "INSERT INTO quotation_template ("
	      + TemplateCols
	      + ") VALUES (#templateId#, #templateName#, #templateHeader#, #templateFooter#, #recdate#, #whoModified#, #active#)",
	      z);
	}

	public Collection selectTemplate(String templateId) throws DaoException {
		return super.select("SELECT " + TemplateCols
				+ " FROM quotation_template WHERE templateId=?",
				TemplateDataObject.class, new String[] { templateId }, 0, -1);
	}

	public Collection selectTemplate() throws DaoException {
		return super.select("SELECT " + TemplateCols
				+ " FROM quotation_template", TemplateDataObject.class,
				new String[] {}, 0, -1);
	}

    public Collection selectTemplateActive() throws DaoException {
      String sql = "SELECT "+TemplateCols+" FROM quotation_template " +
            "WHERE active='1'";
      return super.select(sql, TemplateDataObject.class, null, 0, -1);
    }
    
	public Collection selectTemplate(String search, String active, String sort,
			boolean desc, int start, int rows) throws DaoException {
		String search2 = search;
		if ("null".equals(search)) {
			search2 = "";
		} else if (search == null) {
			search2 = "";
		}
		boolean desc2 = desc;
		String sql = "SELECT " + TemplateCols
				+ " FROM quotation_template WHERE (templateName LIKE '%"
				+ search2 + "%' OR templateHeader LIKE '%" + search2
				+ "%' OR templateFooter LIKE '%" + search2
				+ "%' OR recdate LIKE '%" + search2
				+ "%' OR whoModified LIKE '%" + search2
				+ "%' OR active LIKE '%" + search2 + "%')";
		if (!("-1".equals(active) || "".equals(active)))
			sql = sql + " AND active='" + active + "'";
		if (!"".equals(sort) && !"null".equals(sort) && sort != null)
			sql = sql + " ORDER BY " + sort;
		if (desc2)
			sql = sql + " DESC";
		return super.select(sql, TemplateDataObject.class, null, start, rows);
	}

	public int selectTemplateCount(String search, String active)
			throws DaoException {
		String search2 = search;
		if ("null".equals(search)) {
			search2 = "";
		} else if (search == null) {
			search2 = "";
		}
		String sql = "SELECT count(*) AS total"
				+ " FROM quotation_template WHERE (templateName LIKE '%"
				+ search2 + "%' OR templateHeader LIKE '%" + search2
				+ "%' OR templateFooter LIKE '%" + search2
				+ "%' OR recdate LIKE '%" + search2
				+ "%' OR whoModified LIKE '%" + search2
				+ "%' OR active LIKE '%" + search2 + "%')";
		if (!("-1".equals(active) || "".equals(active)))
			sql = sql + " AND active='" + active + "'";
		Map row = (Map) super.select(sql, HashMap.class, null, 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public Collection selectTemplateactive() throws DaoException {
		return super.select("SELECT DISTINCT active FROM quotation_template",
				TemplateDataObject.class, new String[] {}, 0, -1);
	}

	public void updateTemplate(TemplateDataObject z) throws DaoException {
		super.update("UPDATE quotation_template "
				+ "SET templateName=#templateName#, "
				+ "templateHeader=#templateHeader#, "
				+ "templateFooter=#templateFooter#, " + "recdate=#recdate#, "
				+ "whoModified=#whoModified#, " + "active=#active# "
				+ "WHERE templateId=#templateId#", z);
	}

	  /* Customer */

	  public void deleteCustomer(String customerId) throws DaoException {
	    super.update("DELETE FROM quotation_customer WHERE customerId=?", new String[] {customerId});
	  }

	  public void insertCustomer(CustomerDataObject z) throws DaoException {
	    super.update("INSERT INTO quotation_customer ("+CustomerCols+") " +
                "VALUES (" +
                "#customerId#, " +
                "#contactFirstName#, " +
                "#contactLastName#, " +
                "#companyName#, " +
                "#address1#, " +
                "#address2#, " +
                "#address3#, " +
                "#postcode#, " +
                "#state#, " +
                "#country#, " +
                "#gender#, " +
                "#salutation#, " +
                "#active#)", z);
	  }

	  public Collection selectCustomer(String customerId) throws DaoException {
	    return super.select("SELECT "+CustomerCols+" FROM quotation_customer WHERE customerId=?", CustomerDataObject.class, new String[] {customerId}, 0, -1);
	  }

	  public Collection selectCustomer() throws DaoException {
	    return super.select("SELECT "+CustomerCols+" FROM quotation_customer", CustomerDataObject.class, new String[] {}, 0, -1);
	  }

      public Collection selectActiveCustomer( String search, String sort, boolean desc, int start, int rows) throws DaoException {
        ArrayList args = new ArrayList();
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        boolean desc2 = desc;
        String sql = "SELECT "+CustomerCols+" FROM quotation_customer " +
        "WHERE (active = '1' AND (contactFirstName LIKE ? " +
        "OR contactLastName LIKE ? " +
        "OR companyName LIKE ? ))";
        for( int i=3; i>0; i--) {
          args.add( "%"+search2+"%" );
        }          
        if (!"".equals(sort) && !"null".equals(sort) && sort != null)
          sql = sql+" ORDER BY "+sort;
        if (desc2)
          sql = sql+" DESC";
        return super.select(sql, CustomerDataObject.class, args.toArray(), start, rows);
      }

      public int selectActiveCustomerCount(String search) throws DaoException {
        ArrayList args = new ArrayList();
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        String sql =  "SELECT count(*) AS total FROM quotation_customer " +
        "WHERE (active='1' AND " +
        "(contactFirstName LIKE ? " +
        "OR contactLastName LIKE ? " +
        "OR companyName LIKE ? ))";
        for( int i=3; i>0; i--) {
          args.add( "%"+search2+"%" );
        }          
        Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
        
      }
	  public Collection selectCustomer(String search, String sort, boolean desc, int start, int rows) throws DaoException {
        ArrayList args = new ArrayList();
	    String search2 = search;
	    if ("null".equals(search)) {search2 = "";}
	    else if (search == null) {search2 = "";}
	    boolean desc2 = desc;
/*        
	    String sql =
	      "SELECT "+CustomerCols+" FROM quotation_customer " +
                "WHERE (contactFirstName LIKE '%"+search2+"%' " +
                "OR contactLastName LIKE '%"+search2+"%' " +
                "OR companyName LIKE '%"+search2+"%' " +
                "OR address1 LIKE '%"+search2+"%' " +
                "OR address2 LIKE '%"+search2+"%' " +
                "OR address3 LIKE '%"+search2+"%' " +
                "OR postcode LIKE '%"+search2+"%' " +
                "OR state LIKE '%"+search2+"%' " +
                "OR country LIKE '%"+search2+"%' " +
                "OR gender LIKE '%"+search2+"%' " +
                "OR salutation LIKE '%"+search2+"%')";
*/
        String sql = "SELECT "+CustomerCols+" FROM quotation_customer " +
                "WHERE (contactFirstName LIKE ? " +
                "OR contactLastName LIKE ? " +
                "OR companyName LIKE ? " +
//                "OR address1 LIKE ? " +
//                "OR address2 LIKE ? " +
//                "OR address3 LIKE ? " +
//                "OR postcode LIKE ? " +
//                "OR state LIKE ? " +
//                "OR country LIKE ? " +
//                "OR gender LIKE ? " +
//                "OR salutation LIKE ?" +
                ")";
        
        for( int i=3; i>0; i--) {
          args.add( "%"+search2+"%" );
        }          
        
	    if (!"".equals(sort) && !"null".equals(sort) && sort != null)
	      sql = sql+" ORDER BY "+sort;
	    if (desc2)
	      sql = sql+" DESC";
	    return super.select(sql, CustomerDataObject.class, args.toArray(), start, rows);
	  }

	  public int selectCustomerCount(String search) throws DaoException {
        ArrayList args = new ArrayList();
	    String search2 = search;
	    if ("null".equals(search)) {search2 = "";}
	    else if (search == null) {search2 = "";}
	    String sql =  "SELECT count(*) AS total FROM quotation_customer " +
        "WHERE (contactFirstName LIKE ? " +
        "OR contactLastName LIKE ? " +
        "OR companyName LIKE ? " +
//        "OR address1 LIKE ? " +
//        "OR address2 LIKE ? " +
//        "OR address3 LIKE ? " +
//        "OR postcode LIKE ? " +
//        "OR state LIKE ? " +
//        "OR country LIKE ? " +
//        "OR gender LIKE ? " +
//        "OR salutation LIKE ?" +
        ")";

	    for( int i=3; i>0; i--) {
	      args.add( "%"+search2+"%" );
	    }          

	    Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
	    return Integer.parseInt(row.get("total").toString());
	  }

	  public void updateCustomer(CustomerDataObject z) throws DaoException {
	    super.update("UPDATE quotation_customer " +
                "SET contactFirstName=#contactFirstName#, " +
                "contactLastName=#contactLastName#, " +
                "companyName=#companyName#, " +
                "address1=#address1#, " +
                "address2=#address2#, " +
                "address3=#address3#, " +
                "postcode=#postcode#, " +
                "state=#state#, " +
                "country=#country#, " +
                "gender=#gender#, " +
                "salutation=#salutation#," +
                "active=#active# " +
                "WHERE customerId=#customerId#", z);
	  }
 
      /* QtnColumn */
      public void deleteQtnColumn(String columnId) throws DaoException {
        String sql = "DELETE FROM quotation_column WHERE columnId=?";
        super.update(sql, new String [] {columnId});
      }
      
      public void insertQtnColumn(QtnColumnDataObject z) throws DaoException {
        super.update("INSERT INTO quotation_column (" +
            QtnColumnCols +
            ") VALUES (" +
            "#columnId#, #tableId#, #position#," +
            " #header#, #columnClassName#, #columnStyle#, #compulsory#)",
            z);
      }

      public void updateQtnColumn(QtnColumnDataObject z) throws DaoException {
        String sql = "UPDATE quotation_column SET " +
          "tableId=#tableId#, " +
          "position=#position#, " +
          "header=#header#, " +
          "columnClassName=#columnClassName#, " +
          "columnStyle=#columnStyle#, " +
          "compulsory=#compulsory# " +
          "WHERE columnId=#columnId#";
        
        super.update(sql, z);
      }
/*      
      public Collection selectQtnColumn() throws DaoException {
        return super.select("SELECT " + QtnColumnCols + " FROM quotation_columns", 
            QtnColumnDataObject.class, new String[]{}, 0, -1);
      }
*/      
      public Collection selectQtnColumn(String columnId) throws DaoException {
        return super.select("SELECT * FROM quotation_column " +
            "WHERE columnId=?",
            QtnColumnDataObject.class, new String[]{columnId}, 0, -1);
      }
/*
      public Collection selectQtnColumn(String tableId, String search, String sort, boolean desc, int start, int rows) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        boolean desc2 = desc;
        String sql = "SELECT "+QtnColumnCols+" FROM quotation_column" +
                " WHERE (tableId=?" +
                " AND header LIKE ?)";        
//              " WHERE (tableId LIKE '%"+search2+"%'" +
//              " OR position LIKE '%"+search2+"%'" +
//              " OR header LIKE '%"+search2+"%')";
//              " OR columnClassName LIKE '%"+search2+"%'" +
//              " OR columnStyle LIKE '%"+search2+"%')";
        if (!"".equals(sort) && !"null".equals(sort) && sort != null)
          sql = sql+" ORDER BY "+sort;
        if (desc2)
          sql = sql+" DESC";
        ArrayList args = new ArrayList();
        args.add(tableId);
        args.add("%"+search2+"%");
        
        return super.select(sql, QtnColumnDataObject.class, args.toArray(), start, rows);
      }
*/
      
      public Collection selectQtnColumn(String tableId, String search, int start, int rows) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        String sql = "SELECT "+QtnColumnCols+" FROM quotation_column" +
                " WHERE (tableId=?" +
                " AND header LIKE ?)" +
                " ORDER BY position";        
        ArrayList args = new ArrayList();
        args.add(tableId);
        args.add("%"+search2+"%");
        
        return super.select(sql, QtnColumnDataObject.class, args.toArray(), start, rows);
      }      
      public int selectQtnColumnCount(String tableId, String search) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        String sql = "SELECT count(*) AS total FROM quotation_column" +
                  " WHERE (tableId=?" +
                  " AND header LIKE ?)";
//                " WHERE (tableId LIKE '%"+search2+"%'" +
//                " OR position LIKE '%"+search2+"%'" +
//                " OR header LIKE '%"+search2+"%'"+
//                " OR columnClassName LIKE '%"+search2+"%'" +
//                " OR columnStyle LIKE '%"+search2+"%')";
        
        ArrayList args = new ArrayList();
        args.add(tableId);
        args.add("%"+search2+"%");
        
        Map row = (Map)super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
      }
      
/* QtnTable<->QtnColumn */
      public Collection selectQtnTableColumn(String tableId) throws DaoException {
        String sql = "SELECT * FROM quotation_column " +
          "WHERE (tableId=?) ORDER BY position";
        return super.select(sql, QtnColumnDataObject.class, new String[]{tableId}, 0, -1);
      }

      public void deleteQtnTableColumn(String tableId) throws DaoException {
        String sql = "DELETE FROM quotation_column WHERE tableId=?";
        super.update(sql, new String[]{tableId});
      }
      
/* QtnTable */
      public void insertQtnTable(QtnTableDataObject z) throws DaoException {
        String sql = "INSERT INTO quotation_table (" + QtnTableCols +
                      ") VALUES (" +
                      "#tableId#, " +
                      "#tableDescription#, " +
                      "#tableCaption#, " +
                      "#tableStyle#, #active#)";
        super.update(sql,z);
      }
      
      public void deleteQtnTable(String tableId) throws DaoException {
        String sql = "DELETE FROM quotation_table WHERE tableId=?";
        super.update(sql, new String [] {tableId});
      }
      
      public void updateQtnTable(QtnTableDataObject z) throws DaoException {
        String sql = "UPDATE quotation_table SET " +
        "tableCaption=#tableCaption#, " +
        "tableDescription=#tableDescription#, " +        
        "tableStyle=#tableStyle#, " +
        "active=#active# " +
        "WHERE tableId=#tableId#";
        super.update(sql, z);
      }
      
      public Collection selectQtnTable() throws DaoException {
        String sql = "SELECT * FROM quotation_table WHERE active='1'";
        return super.select( sql, QtnTableDataObject.class, new String[]{}, 0, -1);
      }
      
      public Collection selectQtnTable(String tableId) throws DaoException {
        String sql = "SELECT " + QtnTableCols + " FROM quotation_table WHERE tableId=?";
        return super.select( sql, QtnTableDataObject.class, new String[]{tableId}, 0, -1 );
      }

      public Collection selectQtnTable(String search, String sort, boolean desc, int start, int rows) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        boolean desc2 = desc;
        String sql =
          "SELECT "+QtnTableCols+
          " FROM quotation_table WHERE (tableDescription LIKE '%"+search2+"%' OR tableCaption LIKE '%"+search2+"%' OR tableStyle LIKE '%"+search2+"%')";
        if (!"".equals(sort) && !"null".equals(sort) && sort != null)
          sql = sql+" ORDER BY "+sort;
        if (desc2)
          sql = sql+" DESC";
        return super.select(sql, QtnTableDataObject.class, null, start, rows);
      }

      public int selectQtnTableCount(String search) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        String sql =
          "SELECT count(*) AS total"+
          " FROM quotation_table WHERE (tableDescription LIKE '%"+search2+"%' OR tableCaption LIKE '%"+search2+"%' OR tableStyle LIKE '%"+search2+"%')";
        Map row = (Map)super.select(sql, HashMap.class, null, 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
      }
      
/* QtnTemplate */
      public void insertQtnTemplateText(QtnTemplateTextDataObject z ) throws DaoException {
        String sql = "INSERT INTO quotation_templates (" + QtnTempltCols +
        ") VALUES (#templateId#, #templateDescription#, #templateBody#, #active#)";
        super.update(sql, z);
      }

      public void deleteQtnTemplateText(String templateId) throws DaoException {
        String args[] = { templateId };
        String sql1 = "DELETE FROM quotn_tmptext WHERE templateTextId=?";
        String sql2 = "DELETE FROM quotation_content WHERE contentId=?";
        String sql3 = "DELETE FROM quotation_templates WHERE templateId=?";
        super.update(sql1, args);
        super.update(sql2, args);
        super.update(sql3, args);
      }
      
      public void updateQtnTemplateText(QtnTemplateTextDataObject z ) throws DaoException {
        String sql = "UPDATE quotation_templates SET "+
          "templateDescription=#templateDescription#, "+
          "templateBody=#templateBody#, " +
          "active=#active# "+
          "WHERE templateId=#templateId#";
        super.update(sql, z);
      }
      
      public Collection selectQtnTemplateText() throws DaoException {
        String sql = "SELECT * FROM quotation_templates WHERE active='1'";
        return super.select(sql, QtnTemplateTextDataObject.class, new String[]{}, 0, -1);
      }
      
      public Collection selectQtnTemplateText( String templateId ) throws DaoException {
        String sql = "SELECT "+QtnTempltCols+" FROM quotation_templates WHERE templateId=?";
        return super.select( sql, QtnTemplateTextDataObject.class, new String[]{templateId}, 0, -1 );
      }
      
      public Collection selectQtnTemplateText(String search, String sort, boolean desc, int start, int rows) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        boolean desc2 = desc;
        String sql = "SELECT "+QtnTempltCols+" FROM quotation_templates" +
                " WHERE (templateId NOT LIKE '-%'" +
                " AND (templateDescription LIKE '%"+search2+"%'" +
                " OR templateBody LIKE '%"+search2+"%'))";
        if (!"".equals(sort) && !"null".equals(sort) && sort != null)
          sql = sql+" ORDER BY "+sort;
        if (desc2)
          sql = sql+" DESC";
        return super.select(sql, QtnTemplateTextDataObject.class, null, start, rows);
      }

      public int selectQtnTemplateTextCount(String search) throws DaoException {
        String search2 = search;
        if ("null".equals(search)) {search2 = "";}
        else if (search == null) {search2 = "";}
        String sql = "SELECT count(*) AS total FROM quotation_templates" +
                " WHERE (templateId NOT LIKE '-%'" +
                " AND (templateDescription LIKE '%"+search2+"%'" +
                " OR templateBody LIKE '%"+search2+"%'))";
        Map row = (Map)super.select(sql, HashMap.class, null, 0, -1).iterator().next();
        return Integer.parseInt(row.get("total").toString());
      }

/* TemplateMap */
      public void insertTemplateMap( TemplateMapDataObject z ) throws DaoException {
        String sql = "INSERT into quotn_tmptext ("+ TemplateMapCols +") VALUES "+
        "(#templateId#, #templateTextId#, #sortNum#)";
        super.update(sql, z);
      }

      public void deleteTemplateMap( String templateId, String templateTextId ) throws DaoException {
        String sql = "DELETE FROM quotn_tmptext tt WHERE tt.templateId=? AND tt.templateTextId=?";
        super.update(sql, new String[] {templateId, templateTextId} );
      }

      public void deleteTemplateMap( String templateId ) throws DaoException {
        String sql = "DELETE from quotn_tmptext WHERE templateId=?";
        super.update(sql, new String[] {templateId});
      }
      
      public Collection selectTemplateMap( String templateId, String textId ) throws DaoException {
        String sql = "SELECT "+TemplateMapCols+" FROM quotn_tmptext "
                      +"WHERE templateId=? AND templateTextId=?";
        return super.select(sql, TemplateMapDataObject.class, new String[]{templateId, textId}, 0, -1);
      }
      
      public Collection selectTemplateMap( String templateId, boolean selected ) throws DaoException {
        
        String cols = Util.PrependToList(QtnTempltCols, "t.", ", ");
        String sql = "SELECT "+cols+" FROM quotation_templates t "+
          "LEFT JOIN quotn_tmptext tm "+
          "ON (t.templateId=tm.templateTextId AND tm.templateId=?) " +
          "WHERE (tm.templateId IS " + (selected? "NOT NULL":"NULL") +
          " AND t.active='1')" +
          " ORDER BY tm.sortNum ASC";
        Collection c = super.select(sql, QtnTemplateTextDataObject.class, new String[]{templateId}, 0, -1); 
        return c; 
      }
      
      public void updateTemplateMap(TemplateMapDataObject z) throws DaoException {
        String sql = "UPDATE quotn_tmptext SET sortNum=#sortNum# " +
                "WHERE templateId=#templateId# AND templateTextId=#templateTextId#";
        super.update(sql, z);
      }
      
/* quotationContent */
      public Collection selectQtnContent( String quotationId ) throws DaoException {
        String sql = "SELECT "+QtnContentCols+" FROM quotation_content " +
                "WHERE quotationId=? ORDER BY sortNum ASC";
        return super.select( sql, QtnContentDataObject.class, new String[]{quotationId}, 0, -1);
      }
      
      public void insertQtnContent( QtnContentDataObject z ) throws DaoException {
        String sql = "INSERT INTO quotation_content ("+QtnContentCols+
                ") VALUES (#quotationId#,#contentType#,#contentId#,#sortNum#)";
        super.update(sql, z);
      }
      
      public void deleteQtnContent( String quotationId, String contentId ) throws DaoException {
        String sql = "DELETE FROM quotation_content WHERE quotationId=? AND contentId=?";
        super.update(sql, new String[]{quotationId, contentId});
      }
      
      public void deleteQtnContent( String quotationId ) throws DaoException {
        String sql = "DELETE FROM quotation_content WHERE quotationId=?";
        super.update(sql, new String[] {quotationId} );
      }
}
