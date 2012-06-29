package com.tms.fms.transport.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.model.operator.OperatorLike;
import kacang.model.operator.OperatorParenthesis;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.util.Log;

import com.tms.ekms.security.ui.UsersSelectBox.UsersPopupTable;
import com.tms.fms.register.model.FMSRegisterManager;

public class ManPowerSelect extends UsersSelectBox {

	private Date sDate;
	private Date eDate;

	public ManPowerSelect() {
		super();
		setSortable(false);
	}

	public ManPowerSelect(String name) {
		super(name);
		setSortable(false);
	}

	public ManPowerSelect(Date startDate, Date endDate) {

		sDate = startDate;
		eDate = endDate;
		setSortable(false);
	}

	protected Table initPopupTable() {
		return new CalendarUserTable();
	}

	public class CalendarUserTable extends UsersSelectBox.UsersPopupTable {
		public CalendarUserTable() {
			super();
		}

		public CalendarUserTable(String name) {
			super(name);
		}

		public void init() {
			setWidth("100%");
			setModel(new CalendarUserTableModel());
			loadGroups();
		}

		public class CalendarUserTableModel extends UsersSelectBox.UsersPopupTable.UserTableModel {

			int count = 0;

			public CalendarUserTableModel() {
				super();
				Application application = Application.getInstance();

				TableColumn staffId = new TableColumn("property1", application.getMessage("fms.userSelect.staffId"));
				addColumn(staffId);

				TableColumn telMobile = new TableColumn("telMobile", application.getMessage("fms.userSelect.telMobile"));
				addColumn(telMobile);

				TableColumn telOffice = new TableColumn("telOffice", application.getMessage("fms.userSelect.telOffice"));
				addColumn(telOffice);
			}

			public Collection getTableRows() {
				Collection list = new ArrayList();
				Collection dept = new ArrayList();

				SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
				try {
					// get list of users
					String sort  = getSort();
					if(sort==null||sort.trim().length()==0) {
						sort = "firstName";
						setSort(sort);
					}

					String groupId = getGroupFilter();
					if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
						list = service.getUsers(generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
					}
					else {
						list = service.getGroupUsers(groupId, generateDaoProperties(), getStart(), getRows(), getSort(), isDesc());
					}

					//Filter only user from Transport Department
					//String deptId = Application.getInstance().getProperty("TransportDepartment");
					//                    String deptId = Application.getInstance().getProperty("ManagementService");

					//                    FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);

					//filter by working profile shift
					/*NumberFormat nf = NumberFormat.getInstance(); 		
            		nf.setMinimumIntegerDigits(2);       			//make format 00	

    		        String hourS = (nf.format((int)sDate.getHours()));
    		        String minS = (nf.format((int)sDate.getMinutes()));
    		        String startTime = hourS +":"+ minS;            		                    		        

    		        String hourE = (nf.format((int)eDate.getHours()));           		        
    		        String minE = (nf.format((int)eDate.getMinutes()));
    		        String endTime = hourE +":"+ minE;

    		        Calendar start = Calendar.getInstance();
    		        start.setTime(sDate);                
    		        start.set(Calendar.HOUR_OF_DAY, 00);
    		        start.set(Calendar.MINUTE, 00);
    		        start.set(Calendar.SECOND, 00);
    		        Date sDateTmp = start.getTime();

    		        Calendar end = Calendar.getInstance();
    		        end.setTime(eDate);
    		        end.set(Calendar.HOUR_OF_DAY, 00);
    		        end.set(Calendar.MINUTE, 00);
    		        end.set(Calendar.SECOND, 00);   
					Date eDateTmp = end.getTime();	

                    dept = FRM.selectManpowerWorkingProfile(deptId, sDateTmp, eDateTmp, startTime, endTime);*/
					//                    count = list.size();

					/*
                    dept = FRM.selectUsersByDepartment(deptId);                   
                    count = dept.size();

                    // get selected page
                    int sStart = getStart();
                    int sEnd = sStart + getRows();
                    if (sStart < 0) {
                        sStart = 0;
                    }
                    else if (sStart > dept.size()) {
                        sStart = dept.size()-1;
                    }
                    if (sEnd > dept.size()) {
                        sEnd = dept.size();
                    }
                    else if (sEnd <= sStart) {
                        sEnd = sStart + 1;
                    }
                    dept = new ArrayList(dept).subList(sStart, sEnd);
					 */
				}
				catch (Exception e) {
					Log.getLog(UsersPopupTable.class).error("Error retrieving calendar users", e);
				}
				return list;


			}

			public int getTotalRowCount() {
				try {
					SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
					String groupId = getGroupFilter();
					if (groupId == null || "".equals(groupId) || "-1".equals(groupId)) {
						return service.getUserCount(generateDaoProperties());
					}
					else {
						return service.getGroupUsersCount(groupId, generateDaoProperties());
					}
				}
				catch (Exception e) {
					Log.getLog(UsersPopupTable.class).error(e.toString(), e);
				}
				return 0;
			}

			protected DaoQuery generateDaoProperties() {
				DaoQuery properties = new DaoQuery();
				OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
				op.addOperator(new OperatorLike("username", getFilterValue("query"), null));
				op.addOperator(new OperatorLike("firstName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
				op.addOperator(new OperatorLike("lastName", getFilterValue("query"), DaoOperator.OPERATOR_OR));
				op.addOperator(new OperatorLike("property1", getFilterValue("query"), DaoOperator.OPERATOR_OR));
				properties.addProperty(op);
				properties.addProperty(new OperatorEquals("u.active", "1", DaoOperator.OPERATOR_AND));
				properties.addProperty(new OperatorEquals("department", Application.getInstance().getProperty("ManagementService"), DaoOperator.OPERATOR_AND));
				//properties.addProperty(new Op)
				return properties;
			}

		}

	}

	public Date getSDate() {
		return sDate;
	}

	public void setSDate(Date date) {
		sDate = date;
	}

	public Date getEDate() {
		return eDate;
	}

	public void setEDate(Date date) {
		eDate = date;
	}
}
