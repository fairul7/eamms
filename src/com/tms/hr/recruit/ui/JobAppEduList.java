package com.tms.hr.recruit.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.hr.recruit.model.ApplicantObj;
import com.tms.hr.recruit.model.RecruitAppModule;

public class JobAppEduList extends Table{
	
	private Collection objSession;

	public void init(){
		setWidth("100%");
		initTableList();
	}
	
	public void onRequest(Event evt) {
		//important stage-editing status
		if(evt.getRequest().getSession().getAttribute("JAFtype")!=null && evt.getRequest().getSession().getAttribute("JAFtype").equals("edit")){
			init();
			//validation for editing status
			ApplicantObj editMethod = new ApplicantObj();
			boolean hasEditStatus=editMethod.validateEditStatus(evt);
			if(hasEditStatus){
				String vacancyCodeE=editMethod.getSessionData(evt, "vacancyCodeE");
				String applicantIdE=editMethod.getSessionData(evt, "applicantIdE");
				String codeStatusE=editMethod.getSessionData(evt, "codeStatusE");
				objSession = populateEditType(vacancyCodeE, applicantIdE, codeStatusE);
			}
			
			JobAppEdu jAppEdu = (JobAppEdu) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel2.Form"); //get class data.
			Collection getObjSession = jAppEdu.getEduCol();
			for(Iterator ite= getObjSession.iterator(); ite.hasNext();){
				ApplicantObj aObj = (ApplicantObj)ite.next();
				objSession.add(aObj);
			}
		}else{
			init();
			JobAppEdu jAppEdu = (JobAppEdu) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel2.Form"); //get class data.
			objSession = jAppEdu.getEduCol();
		}
	}
	
	public Collection  populateEditType(String vacancyCodeE, String applicantIdE, String codeStatusE){
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		JobAppEdu jAE = new JobAppEdu();
		Map mapData = jAE.getEduMap();
		Collection objEditCol = ram.findJobAppEdu(applicantIdE);
		for(Iterator ite= objEditCol.iterator(); ite.hasNext();){
			ApplicantObj aObj = (ApplicantObj)ite.next();
			aObj.setHighEduLevelDesc(mapData.get(aObj.getHighEduLevel()).toString());
		}
		
		return objEditCol;
	}
	
	public void initTableList(){
		setShowPageSize(false);
		setModel(new EduListModel());
	}

	public class EduListModel extends TableModel{
		public EduListModel(){
			setWidth("100%");
			Application app = Application.getInstance();
			TableColumn highEduLevel = new TableColumn("highEduLevelDesc", app.getMessage("recruit.general.label.highEduLevel"));
			highEduLevel.setUrlParam("eduId");
			addColumn(highEduLevel);
			
			TableColumn institute = new TableColumn("institute", app.getMessage("recruit.general.label.institute"));
			addColumn(institute);
			
			TableColumn courseTitle = new TableColumn("courseTitle", app.getMessage("recruit.general.label.courseTitle"));
			addColumn(courseTitle);
			
			TableColumn cdateStart = new TableColumn("startDate", app.getMessage("recruit.general.label.startDate"));
			addColumn(cdateStart);
			
			TableColumn cendStart = new TableColumn("endDate", app.getMessage("recruit.general.label.endDate"));
			addColumn(cendStart);
			
			TableColumn cgrade = new TableColumn("grade", app.getMessage("recruit.general.label.grade"));
			addColumn(cgrade);
			
			//action
			addAction(new TableAction("delete", app.getMessage("recruit.general.label.delete"), app.getMessage("recruit.applicantEdu.alert.delete")));
		}
		
	public Collection getTableRows() {
		return objSession;
	}
	
	public int getTotalRowCount() {
		int total = objSession.size();
		return total ;
	}
	
	public String getTableRowKey() {
		return "eduId";
	}
	
	public Forward processAction(Event evt, String action, String[] selectedKeys) {
		Application app = Application.getInstance();
		RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
		JobAppEdu jAppEdu = (JobAppEdu) Application.getInstance().getWidget(evt.getRequest(), "jobApplication.tab1.panel2.Form");
		
		//editing status
		if(evt.getRequest().getSession().getAttribute("JAFtype")!=null && evt.getRequest().getSession().getAttribute("JAFtype").equals("edit")){
			ApplicantObj applicantObj;
			if("delete".equals(action)){
				for(int i=0; i<selectedKeys.length; i++){
					//look up the edu key exist in db or not
					boolean hasEduId = ram.findJobAppEduKey(selectedKeys[i]);
					if(hasEduId){
						ram.deleteJobAppEdu(selectedKeys[i]);
					}else{
						for(Iterator ite = objSession.iterator(); ite.hasNext();){
							applicantObj = (ApplicantObj) ite.next();
							if(applicantObj.getEduId().equals(selectedKeys[i])){
								ite.remove();
							}
						}
					}
			    }
				jAppEdu.setEduId(null);
			}	
			return new Forward("deleteEdu");
		}else{
			//adding status
			if("delete".equals(action)){
				ApplicantObj applicantObj;
				for(int i=0; i<selectedKeys.length; i++){
					for(Iterator ite = objSession.iterator(); ite.hasNext();){
						applicantObj = (ApplicantObj) ite.next();
						if(applicantObj.getEduId().equals(selectedKeys[i])){
							ite.remove();
						}
					}
				}
				jAppEdu.setEduId(null);
			}
			return new Forward("deleteEdu");
		}
		
	}

	}
}