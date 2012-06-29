package com.tms.collab.formwizard.ui;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Timestamp;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.DateFieldElement;
import com.tms.collab.formwizard.xmlwidget.FileUploadElement;
import com.tms.collab.formwizard.xmlwidget.TableGridElement;
import com.tms.util.FormatUtil;

import kacang.Application;
import kacang.services.security.*;
import kacang.services.security.SecurityException;
import kacang.util.Log;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DaoQuery;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;


public class ExportCSVServlet {

    protected StringBuffer columnBuffer = null;
    protected StringBuffer orderBuffer = null;


	protected void doGet(HttpServletRequest request, HttpServletResponse response, String formId, Map optionMap,
                         String sort, boolean isDesc, DaoQuery query)
            throws IOException, ParseException, FormDaoException  {
        getTableGridOptionMap(formId,optionMap);
		Collection reportData = getReportData(query, formId,optionMap,sort,isDesc);
		StringBuffer buffer = processReport(reportData,optionMap,formId);
        request.setAttribute("exportedData",buffer);
	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response, String formId, Map optionMap,
                         String userId,String sort, boolean isDesc, DaoQuery query)
            throws IOException, ParseException, FormDaoException, DaoException  {
        getTableGridOptionMap(formId,optionMap);
		Collection reportData = getReportData(query,formId,optionMap,userId,sort,isDesc);
		StringBuffer buffer = processReport(reportData,optionMap,formId);
        request.setAttribute("exportedData",buffer);
	}

	private Collection getReportData(DaoQuery query, String formId,Map optionMap, String sort, boolean isDesc)
            throws FormDaoException {
		FormDataObject fdo;
		String tableName;
		
		FormModule module  = (FormModule) Application.getInstance().getModule(FormModule.class);
        FormDao dao = (FormDao) module.getDao();

		fdo = module.getForm(formId);
		tableName = FormModule.FORM_PREFIX  + fdo.getFormName();

									

        getColumnBufferOrderBuffer(optionMap,sort,isDesc,tableName);

		
		return dao.getDynamicRows(query, tableName,columnBuffer.toString(),orderBuffer.toString(),0,-1);
		
	}

    	private Collection getReportData(DaoQuery query, String formID,Map optionMap,String userID,
                                         String sort, boolean isDesc) throws FormDaoException {


        FormDao dao  = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();
        String tableName = getTableName(formID);
        getColumnBufferOrderBuffer(optionMap,sort,isDesc,tableName);

		return dao.getDynamicRows(query,tableName,columnBuffer.toString(),userID,orderBuffer.toString(),0,-1);

	}

    private String getTableName(String formId) throws FormDaoException {
        FormDataObject fdo;
        String tableName ;
        FormModule module = (FormModule) Application.getInstance().getModule(FormModule.class);

		fdo = module.getForm(formId);
		tableName = FormModule.FORM_PREFIX  + fdo.getFormName();


        return tableName;
    }

    private void getColumnBufferOrderBuffer(Map optionMap, String sort, boolean isDesc, String tableName) {
        Set values = optionMap.keySet();
		columnBuffer = new StringBuffer();
		orderBuffer = new StringBuffer();
		String value;
		for (Iterator valuesIter = values.iterator(); valuesIter.hasNext();) {
			value = String.valueOf(valuesIter.next()).trim();
            columnBuffer.append(value).append(",");
			if( sort!=null &&  sort.equals(value))  {
		   		orderBuffer = orderBuffer.append(" ORDER BY "+value  );
				if(isDesc)
			   		orderBuffer =orderBuffer.append(" DESC");
			}
		}

        columnBuffer.append(tableName).append(".formUid AS id,").append(tableName).append(".userId,").append(tableName).append(".datePosted");

    }

	private StringBuffer processReport(Collection reportData, Map optionMap, String formID) throws ParseException {
		List list = new ArrayList();
		List elementList = new ArrayList();
		String value = "";
		StringBuffer buffer = new StringBuffer();
		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		
		//process the header  
		Set option = optionMap.keySet();
		for (Iterator it = option.iterator(); it.hasNext();) {
			value = String.valueOf(it.next()).trim();
            list.add(value);
            
            
			buffer.append("\"" + escapteCSV((String)optionMap.get(value)) + "\"\t");
			
			elementList.add(Util.getNodeElement(formID,value));
		}
        buffer.append("\"Posted By\",\"Submission Date\"");
		buffer.append("\r\n");
			
		//process the body
		for (Iterator it = reportData.iterator(); it.hasNext();) {
			DefaultDataObject ddo = (DefaultDataObject) it.next();
			
			for (int i =0; i < list.size(); i++) {
				value = nullToEmpty(String.valueOf(ddo.getProperty(escapeSingleQuote(replaceString(list.get(i).toString()," ",""))))) ;
				if (elementList.get(i).equals(DateFieldElement.ELEMENT_NAME)) {
					value = processDate(value);
				}
                else if (elementList.get(i).equals(FileUploadElement.ELEMENT_NAME)) {
                    value = processFile(value); 
                }



				buffer.append("\"" + escapteCSV(value) +"\"\t");
			}

            buffer.append("\"");
            try {

                buffer.append(escapteCSV(service.getUser((String)ddo.getProperty("userId")).getName()));
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error("Error locating user - userID:"+ ddo.getProperty("userId"),e);
            }
            buffer.append("\"\t");

            buffer.append("\"").append(Util.parseFormDataDate( ((Timestamp)ddo.getProperty("datePosted")).toString())).append("\"");
            buffer.append("\r\n");
			
			
		}	
		
		return buffer;
	}
	
	private String processDate(String date) throws ParseException {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat outFormat = new SimpleDateFormat(FormatUtil.getInstance().getLongDateFormat());
		if (date != null && !date.equals("")) {
		    Date inDate = inFormat.parse(date);
		    return outFormat.format(inDate);
        }

        return "";

	}

    private String processFile(String file) {
        StringTokenizer stk = null;
        String fileName = "";

        if (file != null && file.trim().length() > 0) {
            stk = new StringTokenizer(file,"/");

            while(stk.hasMoreTokens())
                fileName = stk.nextToken();
        }
        return fileName;

    }

	private static String replaceString(String str, String c, String r)	   {
		int location = 0;
		int startingLoc = 0;
		int strLength = c.length();
		while (location != -1)        {
            
			StringBuffer strBuffer = new StringBuffer(str);
			location = str.indexOf(c,startingLoc);
			if (location != -1)
				strBuffer.replace(location,location + strLength, r);
			str = strBuffer.toString();
			startingLoc = location + strLength;
            
            
		}	
		return str;
	}
	
	private String escapeSingleQuote(String str) {
		StringBuffer stringBuffer = new StringBuffer();
    	
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\'')
				stringBuffer.append("\\");
			
			stringBuffer.append(str.charAt(i));
			}
			return stringBuffer.toString();
	}
	
	private String nullToEmpty(String str) {
		if (str == null)
			str = "";
		if (str.equals("null"))
			str = "";
		return str;
	}

    private String escapteCSV(String str) {
        str = str.replaceAll("\"", "\"\"");

        return str;
    }

    public void getTableGridOptionMap(String formId,Map optionMap) {
        Element form = Util.getFormElement(formId);
        List tableGridList = null;
        Element element = null;
        try {
            tableGridList = XPath.selectNodes(form,"/form/"+ TableGridElement.ELEMENT_NAME);
            for (Iterator iterator = tableGridList.iterator(); iterator.hasNext();) {
                element =  (Element) iterator.next();
                optionMap.put(element.getAttributeValue("name"),element.getAttributeValue("title"));

            }
        }
        catch (JDOMException e) {
            Log.getLog(getClass()).error("Error getting /form/" + TableGridElement.ELEMENT_NAME + " element" ,e);
        }
    }
}
