package com.tms.cms.core.ui;

import kacang.Application;
import kacang.services.indexing.IndexingThread;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tms.cms.core.model.ContentManager;
import com.tms.cms.section.ui.SectionSelectBox;


/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 9, 2003
 * Time: 12:04:51 PM
 * To change this template use Options | File Templates.
 */
public class ContentSearchForm extends Form {

    private String query;
    private String sort;
    private int pageSize;

    private TextField searchField;
    private TextField searchField2;
    private Radio andRadio;
    private Radio orRadio;
    private Radio notRadio;
    private Radio dateRadio;
    private Radio dateRangeRadio;
    private DateField dateField;
    private DateField startDateField;
    private DateField endDateField;
    private SelectBox archiveBox;
    private SelectBox parentBox;
    private SelectBox sortBox;
    private SelectBox pageSizeSelectBox;
    private Button submitButton;
    private Button cancelButton;
    
    private CustomSectionSelectBox ssb;
    private TextField contentName;
    private TextField contentAuthor;
    private TextField fileType;
    
    public ContentSearchForm() {
    }

    public ContentSearchForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        return "cms/admin/contentSearchForm";
    }

    public void onRequest(Event evt) {
    	super.onRequest(evt);
    	init();
    	this.setMessage("");
    }
    public void init() {
        // remove existing widgets
        removeChildren();
        // get content form
        try {
            setMethod("POST");

            // add search fields
            Application application = Application.getInstance();
            searchField = new TextField("searchField");
            /*searchField.addChild(new ValidatorNotEmpty("searchFieldValidator"));*/
            addChild(searchField);
            andRadio = new Radio("andRadio");
            andRadio.setGroupName("op");
            andRadio.setChecked(true);
            addChild(andRadio);
            orRadio = new Radio("orRadio");
            orRadio.setGroupName("op");
            addChild(orRadio);
            notRadio = new Radio("notRadio");
            notRadio.setGroupName("op");
            addChild(notRadio);
            searchField2 = new TextField("searchField2");
            addChild(searchField2);
            contentName = new TextField("contentName");
            addChild(contentName);
            contentAuthor = new TextField("contentAuthor");
            addChild(contentAuthor);
            fileType = new TextField("fileType");
            addChild(fileType);


            // add date fields
            dateRadio = new Radio("dateRadio");
            dateRadio.setGroupName("date");
            addChild(dateRadio);
            dateField = new DateField("dateField");
            addChild(dateField);
            dateRangeRadio = new Radio("dateRangeRadio");
            dateRangeRadio.setGroupName("date");
            dateRangeRadio.setChecked(true);
            addChild(dateRangeRadio);
            Calendar TodayDate = Calendar.getInstance();
            Date now=TodayDate.getTime();
            if(now.getMonth()>1){
            now.setMonth(now.getMonth()-1);}
            else{
            now.setMonth(12);
            now.setYear(now.getYear()-1);
            }
            startDateField = new DateField("startDateField");  
            startDateField.setDate(now);
            addChild(startDateField);            
            endDateField = new DateField("endDateField");            
            addChild(endDateField);

            // add select boxes
            archiveBox = new SelectBox("archiveBox");
            archiveBox.setOptions("null="+application.getMessage("general.label.anyContent", "Any Content")+";false="+application.getMessage("general.label.currentContent", "Current Content")+";true="+application.getMessage("general.label.archivedContent", "Archived Content")+"");
            addChild(archiveBox);
            parentBox = new SelectBox("parentBox");
            addChild(parentBox);
            sortBox = new SelectBox("sortBox");
            sortBox.setOptions("score="+application.getMessage("general.label.closestMatch", "Closest Match")+";date="+application.getMessage("general.label.mostRecent", "Most Recent")+"");
            addChild(sortBox);

            // add page size select box
            pageSizeSelectBox = new SelectBox("pageSizeSelectBox");
            pageSizeSelectBox.setOptions(Table.PAGE_SIZE_OPTIONS);
            List list = new ArrayList();
            list.add(new Integer(Table.DEFAULT_PAGE_SIZE).toString());
            pageSizeSelectBox.setValue(list);
            addChild(pageSizeSelectBox);
            
            // add section selectbox
            ssb = new CustomSectionSelectBox("sectionSelectBox");
            String permission = (Boolean.valueOf(true).booleanValue()) ? ContentManager.USE_CASE_VIEW : null;
            ssb.setPermissionId(permission);
            ssb.setType(true);
            addChild(ssb);        

            // add buttons
            submitButton = new Button("submitButton");
            submitButton.setText(application.getMessage("general.label.search", "Search"));
            addChild(submitButton);
            cancelButton = new Button(Form.CANCEL_FORM_ACTION);
            cancelButton.setText(application.getMessage("general.label.cancel", "Cancel"));
            addChild(cancelButton);

        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    /*public Forward onSubmit(Event evt) {
        
        String error="";
        if((searchField.getValue() == null || searchField.getValue().toString().trim().length() == 0) && (contentName.getValue() == null || contentName.getValue().toString().trim().length() == 0) && (contentAuthor.getValue() == null || contentAuthor.getValue().toString().trim().length() == 0) && (fileType.getValue() == null || fileType.getValue().toString().trim().length() == 0)){
        	error+="Error in search, please fill in a field. ";
        	
        }else if((searchField.getValue() == null || searchField.getValue().toString().trim().length() == 0)&&(searchField2.getValue() != null && searchField2.getValue().toString().trim().length() > 0)){       	
        	error+="Please enter search words. ";       	        	
        }
        if(error.length()>0){
        this.setInvalid(true);
        }this.setMessage(error);
        return super.onSubmit(evt);
    }*/
    
    public Forward onValidate(Event evt) {
    	 String error="";
         if((searchField.getValue() == null || searchField.getValue().toString().trim().length() == 0) && (contentName.getValue() == null || contentName.getValue().toString().trim().length() == 0) && (contentAuthor.getValue() == null || contentAuthor.getValue().toString().trim().length() == 0) && (fileType.getValue() == null || fileType.getValue().toString().trim().length() == 0)){
         	error+="Error in search, please fill in a field. ";
         	
         }else if((searchField.getValue() == null || searchField.getValue().toString().trim().length() == 0)&&(searchField2.getValue() != null && searchField2.getValue().toString().trim().length() > 0)){       	
         	error+="Please enter search words. ";       	        	
         }
         if(error.length()>0){
         this.setInvalid(true);
         }this.setMessage(error);
         if(!this.isInvalid()){
        // formulate query
        StringBuffer queryBuffer = new StringBuffer();
        int count=0;
        // search terms
        
        String val = (String)searchField.getValue();
        String val2 = (String)searchField2.getValue();
        if ((val != null && val.trim().length() > 0)&&(val2 != null && val2.trim().length() > 0)) {
        	queryBuffer.append("(");
            queryBuffer.append("(");
            queryBuffer.append(val);
            queryBuffer.append(") ");
            if (andRadio.isChecked()) {
                queryBuffer.append(" AND ");
            }
            else if (orRadio.isChecked()) {
                queryBuffer.append("  OR ");
            }
            else if (notRadio.isChecked()) {
                queryBuffer.append("  AND NOT ");
            }
            queryBuffer.append("(");
            queryBuffer.append(val2);
            queryBuffer.append(") ");
            queryBuffer.append(")");
            count++;
        }
        else if(val != null && val.trim().length() > 0) {
        	queryBuffer.append("(");
            queryBuffer.append("(");
            queryBuffer.append(val);
            queryBuffer.append(") ");
            queryBuffer.append(") ");
            count++;
        }
        

        // search content name
        val = (String)contentName.getValue();
        if (val != null && val.trim().length() > 0) {
            
            queryBuffer.append("AND (name:");
            queryBuffer.append(val);
            queryBuffer.append(") ");
        }
        // search content author
        val = (String)contentAuthor.getValue();
        if (val != null && val.trim().length() > 0) {
            
            queryBuffer.append("AND (author:");
            queryBuffer.append(val);
            queryBuffer.append(") ");
        }
        // search file type
        val = (String)fileType.getValue();
        if (val != null && val.trim().length() > 0) {
            
            queryBuffer.append("AND (fileName:");
            queryBuffer.append(val);
            queryBuffer.append(") ");
        }
        // search section
        String section = null;
        List sectionList = (List)ssb.getValue();
        if (sectionList != null && sectionList.size() > 0) {
        	section = (String)sectionList.get(0);
        	if(!"Any".equals(section)){
        	queryBuffer.append("AND (parentId:");
            queryBuffer.append(section);
            queryBuffer.append(") ");}
        }
        
        // archived?
        List archiveList = (List)archiveBox.getValue();
        if (archiveList != null && archiveList.size() > 0) {
            String archived = (String)archiveList.get(0);
            if ("false".equals(archived)) {
                queryBuffer.append("AND (archived:false) ");
            }
            else if ("true".equals(archived)) {
                queryBuffer.append("AND (archived:true) ");
            }
        }

        // dates
        if (dateRadio.isChecked()) {
            SimpleDateFormat sdf = new SimpleDateFormat(IndexingThread.DATE_FORMAT);
            queryBuffer.append("AND (date:");
            String date = sdf.format(dateField.getDate());
            queryBuffer.append(date);
            queryBuffer.append(") ");
        }
        else if (dateRangeRadio.isChecked()) {
            SimpleDateFormat sdf = new SimpleDateFormat(IndexingThread.DATE_FORMAT);
            queryBuffer.append("AND (date:[");
            String date = sdf.format(startDateField.getDate());
            queryBuffer.append(date);
            queryBuffer.append(" TO ");
            date = sdf.format(endDateField.getDate());
            queryBuffer.append(date);
            queryBuffer.append("]) ");
        }
       
        // sorting
        String srt = null;
        List sortList = (List)sortBox.getValue();
        if (sortList != null && sortList.size() > 0) {
            srt = (String)sortList.get(0);
            setSort(srt);
        }

        // set query
        setQuery(queryBuffer.toString());
        if(count==0){
     	   query=query.substring(3,query.length());
        }
        // page size
        int size = -1;
        List list = (List)pageSizeSelectBox.getValue();
        if (list != null && list.size() > 0) {
            try {
                size = Integer.parseInt((String)list.get(0));
                setPageSize(size);
            }
            catch(Exception e) {
            }
        }
         }
        return super.onValidate(evt);
    }

    class CustomSectionSelectBox extends SectionSelectBox {
        public CustomSectionSelectBox() {
       
        }

        public CustomSectionSelectBox(String name) {
            super(name);
            
        }

        public Forward onSubmit(Event evt) {
            super.onSubmit(evt);
            Map selected = getSelectedOptions();
            if (selected != null && selected.size() > 0) {
                // get selected section
                String key = (String)selected.keySet().iterator().next();
                if (key != null && key.trim().length() > 0) {
                    ContentHelper.setId(evt, key);
                }
            }
            return null;
        }

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


	public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
