package com.tms.fms.eamms.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultDataObject;
import kacang.model.DefaultModule;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageFile;
import kacang.stdui.FileUpload;
import kacang.ui.Event;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import com.tms.fms.eamms.ui.DailyFeedsListingWithExport;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.util.MailUtil;

public class EammsFeedsModule extends DefaultModule 
{
	public static final String SQL_SELECT = "select";
	public static final String SQL_SELECT_COUNT = "count";
	public static final String SQL_WHERE = "where";
	public static final String SQL_PARAM = "param";
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String NETWORK_BOOKING_STATUS_CONFIRMED = "Confirmed";
	public static final String NETWORK_BOOKING_STATUS_PENDING = "Pending";
	public static final String NETWORK_BOOKING_STATUS_PROCESSED = "Processed";
	public static final String NETWORK_STATUS_OF_ASSIGNMENT_ALLOCATED = "Allocated";
	public static final String NETWORK_STATUS_OF_ASSIGNMENT_CANCELLED = "Cancelled";
	public static final String TIME_ZONE_MST = "MST";
	public static final String TIME_ZONE_GMT = "GMT";
	public static final String TIME_REQ_MIN = "M";
	public static final String TIME_REQ_HOUR = "H";
	public static final String DATE_FROM = "Date From";
	public static final String DATE_TO = "Date To";
	
	public static final String FEED_LOG_ADHOC = "1";
	public static final String FEED_LOG_NON_ADHOC = "0";
	
	public void init() 
	{
		super.init();
	}
	
	public Collection getSetupTable(String tableName, String id)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getSetupTable(tableName, id);
		} 
		catch (DaoException e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getSetupTable(2) : " + e, e);
			return null;
		}
	}

	public Collection getAssignments(String requestId, String assignmentId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection<EammsAssignment> col = dao.getAssignments(requestId, assignmentId);
			if(col != null && !col.isEmpty())
			{
				for(EammsAssignment obj : col)
				{
					//Date reqDate = obj.getRequiredDate();
					String reqTimeFr = obj.getRequiredTimeFrom();
					String reqTimeTo = obj.getRequiredTimeTo();
					String totalReqTime = obj.getTotalTimeReq();
					String timeMeasure = obj.getTimeMeasure();
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
					/*if(reqDate != null)
					{
						try
						{
							obj.setProperty("requiredDateStr", sdf.format(reqDate));
						}
						catch(Exception e){}
					}*/
					
					Date reqDateFr = obj.getRequiredDateFrom();
					Date reqDateTo = obj.getRequiredDateTo();
					if(reqDateFr != null && reqDateTo != null)
					{
						try
						{
							obj.setProperty("requiredDateRangeStr", sdf.format(reqDateFr) + " - " + sdf.format(reqDateTo));
						}
						catch(Exception e){}
					}
					
					obj.setProperty("hourFrStr", "");
					obj.setProperty("hourToStr", "");
					obj.setProperty("minFrStr", "");
					obj.setProperty("minToStr", "");
					if(reqTimeFr != null && reqTimeFr.length() == 4)
					{
						obj.setProperty("hourFrStr", reqTimeFr.substring(0, 2));
						obj.setProperty("minFrStr", reqTimeFr.substring(2, 4));
						
						if(reqTimeTo != null && reqTimeTo.length() == 4)
						{
							obj.setProperty("hourToStr", reqTimeTo.substring(0, 2));
							obj.setProperty("minToStr", reqTimeTo.substring(2, 4));
						}
					}
					
					if(EammsFeedsModule.TIME_REQ_HOUR.equals(timeMeasure))
					{
						obj.setProperty("totalReqTime_measure", totalReqTime + " " 
								+ Application.getInstance().getMessage("eamms.feed.list.opt.hour"));
					}
					else
					{
						obj.setProperty("totalReqTime_measure", totalReqTime + " " 
								+ Application.getInstance().getMessage("eamms.feed.list.opt.min"));
					}
				}
				return col;
			}
			return new ArrayList();
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getAssignments(2) : " + e, e);
			return null;
		}
	}
	
	public void insertEammsFeedsDetails(EammsFeedsDetails obj) 
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.insertEammsFeedsDetails(obj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertEammsFeedsDetails(1) : " + e, e);
		}
	}
	
	public Collection getEammsFeedsDetails(String requestId, String feedsDetailsId) 
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getEammsFeedsDetails(requestId, feedsDetailsId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getEammsFeedsDetails(2) : " + e, e);
			return null;
		}
	}
	
	public String getUserGroupsNetworkStatus(String nwStatus)
	{
		SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
		String currentUserId =  Application.getInstance().getCurrentUser().getId();
		
		Collection<Group> groups = null;
		try
		{
			groups = ss.getUserGroups(currentUserId);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(e, e);
		}
		
		String nwStatusStr = "";
		ArrayList<String> arr = new ArrayList();
		if(groups != null && !groups.isEmpty())
		{
			for(Group grp : groups)
			{
				String groupId = grp.getId();
				if(Application.getInstance().getProperty("UnitHeadEngineering").equals(groupId))
				{
					arr.add("02");
				}
				else if(Application.getInstance().getProperty("UnitHeadNetwork").equals(groupId))
				{
					arr.add("03");
				}
				else if(Application.getInstance().getProperty("NetworkEngineer").equals(groupId))
				{
					arr.add("04");
					arr.add("05");
				}
			}
			
			if(nwStatus != null && !nwStatus.equals("") && !nwStatus.equals("-1"))
			{
				if(arr.contains(nwStatus))
				{
					return nwStatus;
				}
				
				if(!arr.isEmpty())
				{
					return "@#"; //make query return empty
				}
			}
			else
			{
				for(String s : arr)
				{
					if(nwStatusStr.equals(""))
					{
						nwStatusStr = nwStatusStr + "'" + s + "'";
						continue;
					}
					nwStatusStr = nwStatusStr + ", '" + s + "'";
				}
			}
		}
		return nwStatusStr;
	}
	
	public Collection getEammsFeedsDetails(String searchBy, String nwStatus, String dateFrom, String dateTo, 
			String sort, boolean isDesc, int start, int rows)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection result = new ArrayList();
			String nwStatusStr = getUserGroupsNetworkStatus(nwStatus);
			if(nwStatusStr.equals(""))
			{
				result = getEammsFeedsDetailsEC(searchBy, nwStatus, dateFrom, dateTo, sort, isDesc, start, rows);
			}
			else
			{
				result = dao.getEammsFeedsDetails(searchBy, nwStatusStr, dateFrom, dateTo, sort, isDesc, start, rows);
			}
			
			return result;
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getEammsFeedsDetails(8) : " + e, e);
			return null;
		}
	}

	public int getCountEammsFeedsDetails(String searchBy, String nwStatus, String dateFrom, String dateTo)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			int result = 0;
			String nwStatusStr = getUserGroupsNetworkStatus(nwStatus);
			if(nwStatusStr.equals(""))
			{
				result = getCountEammsFeedsDetailsEC(searchBy, nwStatus, dateFrom, dateTo);
			}
			else
			{
				result = dao.getCountEammsFeedsDetails(searchBy, nwStatusStr, dateFrom, dateTo);
			}
			
			return result;
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getCountEammsFeedsDetails(4) : " + e, e);
			return 0;
		}
	}
	
	public Collection getEammsFeedsDetailsEC(String searchBy, String nwStatus, String dateFrom, String dateTo, 
			String sort, boolean isDesc, int start, int rows)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			// check requestedBy == EC ? 01 : {}
			if(nwStatus != null && !(nwStatus.equals("01") || nwStatus.equals("08")))
			{
				nwStatus = "@#"; //make query return empty
			}
			else if(nwStatus == null || nwStatus.equals(""))
			{
				nwStatus = "'01', '08'";
			}
			
			String userId = Application.getInstance().getCurrentUser().getId();
			return dao.getEammsFeedsDetailsEC(userId, searchBy, nwStatus, dateFrom, dateTo, sort, isDesc, start, rows);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getEammsFeedsDetailsEC(8) : " + e, e);
			return null;
		}
	}
	
	public int getCountEammsFeedsDetailsEC(String searchBy, String nwStatus,
			String dateFrom, String dateTo)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			// check requestedBy == EC ? 01 : 0
			if(nwStatus != null && !(nwStatus.equals("01") || nwStatus.equals("08")))
			{
				nwStatus = "@#"; //make query return empty
			}
			else if(nwStatus == null || nwStatus.equals(""))
			{
				nwStatus = "'01', '08'";
			}
			
			String userId = Application.getInstance().getCurrentUser().getId();
			return dao.getCountEammsFeedsDetailsEC(userId, searchBy, nwStatus, dateFrom, dateTo);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getCountEammsFeedsDetailsEC(4) : " + e, e);
			return 0;
		}
	}
	
	public boolean isWithinTvroFeedDateRange(String tvroServiceId, Date dateFrom, Date dateTo)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.getFmsTvroService(null, tvroServiceId, dateFrom, dateTo);
			if(col != null && !col.isEmpty())
			{
				return true;
			}
			return false;
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.isWithinTvroFeedDateRange(3) : " + e, e);
			throw new RuntimeException(e);
		}
	}

	public void processFeedsDetails(Collection<DefaultDataObject> result)
	{
		if(result != null && !result.isEmpty())
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			
			HashMap feedTypeMap = new HashMap();
			feedTypeMap.put("0", Application.getInstance().getMessage("fms.facility.label.localFeed"));
			feedTypeMap.put("1", Application.getInstance().getMessage("fms.facility.label.foreignFeed"));
			feedTypeMap.put("2", Application.getInstance().getMessage("fms.facility.label.visualFeed"));
			
			HashMap nwStatusMap = new HashMap();
			Collection col = getSetupTable("fms_feed_network_status", null);
			if(col != null && !col.isEmpty())
			{
				for(Iterator itr = col.iterator(); itr.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) itr.next();
					nwStatusMap.put(obj.getId(), (String)obj.getProperty("c_name"));
				}
			}
			
			for(DefaultDataObject obj : result)
			{
				Date requiredDate = (Date) obj.getProperty("requiredFrom");
				Date requiredDateTo = (Date) obj.getProperty("requiredTo");
				
				String requiredDateRange = "";
				try
				{
					requiredDateRange = sdf.format(requiredDate) + " - " + sdf.format(requiredDateTo);
				}
				catch(Exception e){}
				obj.setProperty("requiredDateRange", requiredDateRange);
				
				String feedType = (String) feedTypeMap.get(obj.getProperty("feedType"));
				obj.setProperty("feedType", feedType);
				
				String networkStatus = (String) nwStatusMap.get(obj.getProperty("networkStatus"));
				obj.setProperty("networkStatus", networkStatus);
			}
		}
	}

	public EammsFeedsDetails getFeedsRequestDetails(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getFeedsRequestDetails(requestId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFeedsRequestDetails(1) : " + e, e);
			return null;
		}
	}
	
	public void insertStatusTrail(StatusTrail obj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.insertStatusTrail(obj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertStatusTrail(1) : " + e, e);
		}
	}

	public Collection getStatusTrail(String feedsDetailsId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getStatusTrail(feedsDetailsId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getStatusTrail(1) : " + e, e);
			return null;
		}
	}

	public void updateFeedsRequestDetails(DefaultDataObject obj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.updateFeedsRequestDetails(obj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.updateFeedsRequestDetails(1) : " + e, e);
		}
	}

	public boolean isAllAssignmentsHvStatus(String feedsDetailsId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.isAllAssignmentsHvStatus(feedsDetailsId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.isAllAssignmentsHvStatus(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}

	public void saveAssignment(EammsAssignment assignObj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			if(assignObj != null)
			{
				if(assignObj.getAssignmentId() == null || assignObj.getAssignmentId().equals(""))
				{
					assignObj.setAssignmentId(generateNewAssignmentId());
					dao.insertAssignment(assignObj);
				}
				else
				{
					dao.editAssignment(assignObj);
				}
			}
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.saveAssignment(1) : " + e, e);
		}
	}
	
	public String generateNewAssignmentId()
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			String latestAssignId = dao.getLatestAssignId();
			Date currDate = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String yr = sdf.format(currDate);
			
			sdf = new SimpleDateFormat("MM");
			String mn = sdf.format(currDate);
			
			int max = 9999;
			if(latestAssignId != null)
			{
				String[] arr = latestAssignId.split("E");
				if(arr.length == 2)
				{
					int num = Integer.parseInt(arr[1]);
					String runingNum = String.valueOf(max);
					if(num < max)
					{
						runingNum = String.valueOf(num+1);
						while(runingNum.length() < 4)
						{
							runingNum = ("0" + runingNum);
						}
					}
					runingNum = "TVRO/" + yr + mn + "/E" + runingNum;
					return runingNum;
				}
				else
				{
					return "TVRO/" + yr + mn + "/E" + "0000";
				}
			}
			else
			{
				return "TVRO/" + yr + mn + "/E" + "0000";
			}
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.generateNewAssignmentId() : " + e, e);
			return null;
		}
	}

	public void updateAssignment(EammsAssignment assignObj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.updateAssignment(assignObj);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.updateAssignment(1) : " + e, e);
		}
	}

	public String getFeedsDetailsIdByRequestId(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection<EammsFeedsDetails> col = dao.getEammsFeedsDetails(requestId, null);
			if(col != null && !col.isEmpty())
			{
				EammsFeedsDetails obj = col.iterator().next();
				return obj.getId();
			}
			return "";
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFeedsDetailsIdByRequestId(1) : " + e, e);
			return null;
		}
	}

	public Collection getTvroServiceIds(String feedsDetailsId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getTvroServiceIds(feedsDetailsId);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getTvroServiceIds(1) : " + e, e);
			return null;
		}
	}
	
	public boolean isFileSizeValid(double fileSizeLimit, FileUpload upload, Event evt)
	{
		try
		{
			StorageFile sf = upload.getStorageFile(evt.getRequest());
			if (sf != null) 
			{
				long fileSize = sf.getSize();
				if(fileSize < fileSizeLimit)
				{
					return true;
				}
			}
		}
		catch (IOException e)
		{ 
			Log.getLog(getClass()).error(e.toString(), e);
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error(e.toString(), e);
		}
		
		return false;
	}

	public void deleteAssignment(String assignmentId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.deleteAssignment(assignmentId);
		} 
		catch (Exception e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.deleteAssignment(1) : " + e, e);
		}
	}

	public Collection getEammsFeedsDetailsListing(String mode, String searchBy, String nwStatus, 
			String dateFr, String dateTo, String sort, boolean desc, int start, int rows)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			if(DailyFeedsListingWithExport.VIEW_HISTORY.equals(mode))
			{
				nwStatus = "06"; // Closed
			}
			else if (DailyFeedsListingWithExport.VIEW_ALL_OWN.equals(mode))
			{
				String userId = Application.getInstance().getCurrentUser().getId(); 
				return dao.getEammsFeedsDetailsEC(userId, searchBy, nwStatus, dateFr, dateTo, sort, desc, start, rows);
			}
			return dao.getEammsFeedsDetails(searchBy, nwStatus, dateFr, dateTo, sort, desc, start, rows);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getEammsFeedsDetailsListing(9) : " + e, e);
			return null;
		}
	}

	public int getCountEammsFeedsDetailsListing(String mode, String searchBy, String nwStatus, 
			String dateFr, String dateTo)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			if(DailyFeedsListingWithExport.VIEW_HISTORY.equals(mode))
			{
				nwStatus = "06"; // Closed
			}
			else if (DailyFeedsListingWithExport.VIEW_ALL_OWN.equals(mode))
			{
				String userId = Application.getInstance().getCurrentUser().getId(); 
				return dao.getCountEammsFeedsDetailsEC(userId, searchBy, nwStatus, dateFr, dateTo);
			}
			return dao.getCountEammsFeedsDetails(searchBy, nwStatus, dateFr, dateTo);
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.getCountEammsFeedsDetailsListing(5) : " + e, e);
			return 0;
		}
	}
	
	public void feeds_sendMail(Map args)
	{
		SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
		Application app = Application.getInstance();
		
		String smtpServer = app.getProperty("smtp.server");
		String adminEmail = app.getProperty("admin.email");
		
		String requestId = (String) args.get("requestId");
		String requestTitle = (String) args.get("requestTitle");
		String status = (String) args.get("status");
		
		String emailSubject = "";
		String toEmail = "";
		String ccEmail = "";
		String cHead = "";
		String content = "";
		
		Collection<User> ecCol = (Collection<User>) args.get("engineeringCoordinatorCol");
		Collection<User> unitHeadEngCol = null;
		Collection<User> unitHeadNWCol = null;
		Collection<User> NWEngCol = null;
		try
		{
			unitHeadEngCol = ss.getGroupUsers(app.getProperty("UnitHeadEngineering"));
			unitHeadNWCol = ss.getGroupUsers(app.getProperty("UnitHeadNetwork"));
			NWEngCol = ss.getGroupUsers(app.getProperty("NetworkEngineer"));
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(e, e);
		}
		
		if("01".equals(status)) //New : TO Engineering Coordinator, CC Unit Head Engineering
		{
			emailSubject = app.getMessage("eamms.feed.list.email.ec_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(ecCol);
			ccEmail = addEmailToStrArr(unitHeadEngCol);
			
			String usernameArr = addNameToStrArr(ecCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.ec_content", " ", new Object[] { requestTitle, requestId });
		}
		else if("02".equals(status)) //Submitted : TO Unit Head Engineering
		{
			emailSubject = app.getMessage("eamms.feed.list.email.unit_head_eng_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(unitHeadEngCol);
			
			String usernameArr = addNameToStrArr(unitHeadEngCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.unit_head_eng_content", " ", new Object[] { requestTitle, requestId });
		}
		else if("03".equals(status)) //Verified : TO Unit Head Network, CC Engineering Coordinator
		{
			emailSubject = app.getMessage("eamms.feed.list.email.unit_head_network_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(unitHeadNWCol);
			ccEmail = addEmailToStrArr(ecCol);
			
			String usernameArr = addNameToStrArr(unitHeadNWCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.unit_head_network_content");
		}
		else if("04".equals(status)) //Approved : TO Network Engineer, CC Engineering Coordinator
		{
			emailSubject = app.getMessage("eamms.feed.list.email.approved_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(NWEngCol);
			ccEmail = addEmailToStrArr(ecCol);
			
			String usernameArr = addNameToStrArr(NWEngCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.approved_content", " ", new Object[] { requestTitle, requestId });
		}
		else if("08".equals(status)) //Rejected : TO Engineering Coordinator, CC Unit Head Engineering
		{
			emailSubject = app.getMessage("eamms.feed.list.email.rejected_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(ecCol);
			ccEmail = addEmailToStrArr(unitHeadEngCol);
			
			String usernameArr = addNameToStrArr(ecCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.rejected_content", " ", new Object[] { requestTitle, requestId });
		}
		else if("06".equals(status)) //Closed : TO Engineering Coordinator, CC Unit Head Network & Unit Head Engineering
		{
			ArrayList headNW_and_headEng = new ArrayList();
			headNW_and_headEng.addAll(unitHeadNWCol);
			headNW_and_headEng.addAll(unitHeadEngCol);
			
			emailSubject = app.getMessage("eamms.feed.list.email.complete_subject", " ", new Object[] { requestTitle, requestId });
			toEmail = addEmailToStrArr(ecCol);
			ccEmail = addEmailToStrArr(headNW_and_headEng);
			
			String usernameArr = addNameToStrArr(ecCol);
			cHead = app.getMessage("eamms.feed.list.email.dear", " ", new Object[] { usernameArr });
			content = app.getMessage("eamms.feed.list.email.complete_content", " ", new Object[] { requestTitle, requestId });
		}
		
		content = cHead + "<br><br><br>" + content + "<br><br><br>" + app.getMessage("eamms.feed.list.email.system_signature"); 
		
		/*System.out.println("\n==============================================\n");
    	System.out.println(content);
    	System.out.println("\n==============================================\n");*/
		
    	MailUtil.sendEmail(smtpServer, true, adminEmail, toEmail, ccEmail, null, emailSubject , content);
	}
	
	private String addEmailToStrArr(Collection<User> col)
	{
		String emailArr = ""; 
		if(col != null && !col.isEmpty())
		{
			for(User obj : col)
			{
				if(emailArr.equals(""))
				{
					emailArr = (String) obj.getProperty("email1");
				}
				else
				{
					emailArr = emailArr + ", " + (String) obj.getProperty("email1");
				}
			}
		}
		return emailArr;
	}

	private String addNameToStrArr(Collection<User> col)
	{
		String usernameArr = "all";
		if(col != null && !col.isEmpty())
		{
			for(User obj : col)
			{
				if(usernameArr.equals("all"))
				{
					usernameArr = obj.getProperty("firstName") + " " + obj.getProperty("lastName");
				}
				else
				{
					usernameArr = usernameArr + ", " + obj.getProperty("firstName") + " " + obj.getProperty("lastName");
				}
			}
		}
		return usernameArr;
	}
	
	public String generateNewFeedsLogId()
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			String latestFeedsLogId = dao.getLatestFeedsLogId();
			Date currDate = new Date();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String yr = sdf.format(currDate);
			
			sdf = new SimpleDateFormat("MM");
			String mn = sdf.format(currDate);
			
			int max = 99999;
			if(latestFeedsLogId != null && latestFeedsLogId.length() >= 11)
			{
				String rn = latestFeedsLogId.substring(9, 13);
				if(rn != null && !rn.equals(""))
				{
					int num = Integer.parseInt(rn);
					String runingNum = String.valueOf(max);
					if(num < max)
					{
						runingNum = String.valueOf(num+1);
						while(runingNum.length() < 5)
						{
							runingNum = ("0" + runingNum);
						}
					}
					runingNum = "FD" + yr + mn + runingNum;
					return runingNum;
				}
				else
				{
					return "FD" + yr + mn + "00000";
				}
			}
			else
			{
				return "FD" + yr + mn + "00000";
			}
		} 
		catch (DaoException e) 
		{
			Log.getLog(getClass()).error("error @ EammsModule.generateNewFeedsLogId() : " + e, e);
			return null;
		}
	}
	
	public void insertFeedsLog(FeedsLogObject obj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.insertFeedsLog(obj);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertFeedsLog(1) : " + e, e);
		}
	}
	
	public void updateFeedsLog(FeedsLogObject obj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.updateFeedsLog(obj);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.updateFeedsLog(1) : " + e, e);
		}
	}
	
	public Collection getFeedsLog(String feedsLogId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getFeedsLog(feedsLogId);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFeedsLog(1) : " + e, e);
			return null;
		}
	}
	
	public Collection selectFeedsLog(String searchBy, String telco, String adhoc, Date dateFrom, Date dateTo, 
				String sort, boolean isDesc, int start, int rows)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.selectFeedsLog(searchBy, telco, adhoc, dateFrom, dateTo, sort, isDesc, start, rows);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.selectFeedsLog(9) : " + e, e);
			return null;
		}
	}
	
	public int selectCountFeedsLog(String searchBy, String telco, String adhoc, Date dateFrom, Date dateTo)
{
	EammsFeedsDao dao = (EammsFeedsDao) getDao();
	try 
	{
		return dao.selectCountFeedsLog(searchBy, telco, adhoc, dateFrom, dateTo);
	} 
	catch (Exception e)
	{
		Log.getLog(getClass()).error("error @ EammsModule.selectCountFeedsLog(5) : " + e, e);
		return 0;
	}
}

	public Collection getUserBelongToGroup(String groupIds)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getUserBelongToGroup(groupIds);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getUserBelongToGroup(1) : " + e, e);
			return null;
		}
	}

	public String getReqTitle(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.getFmsRequest(requestId, null, null, null, false, 0, 1);
			if(col != null && !col.isEmpty())
			{
				DefaultDataObject obj = (DefaultDataObject) col.iterator().next();
				String requestTitle = (String) obj.getProperty("title");
				
				return requestTitle;
			}
			return "";
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getReqTitle(1) : " + e, e);
			return null;
		}
	}
	
	public Collection getFmsRequest(String programId, String keyword, String sort, boolean desc, int start, int rows)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getFmsRequest(null, programId, keyword, sort, desc, start, rows);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFmsRequest(6) : " + e, e);
			return null;
		}
	}

	public int getCountFmsRequest(String programId, String keyword)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getCountFmsRequest(programId, keyword);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getCountFmsRequest(2) : " + e, e);
			return 0;
		}
	}

	public Collection getFmsAssignments(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			return dao.getFmsAssignments(requestId);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFmsAssignments(1) : " + e, e);
			return null;
		}
	}

	public boolean isBlockbookingTvroService(String tvroServiceId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.getFmsTvroService(null, tvroServiceId, null, null);
			if(col != null && !col.isEmpty())
			{
				DefaultDataObject obj = (DefaultDataObject) col.iterator().next();
				if("1".equals(obj.getProperty("blockBooking")))
				{
					return true;
				}
			}
			return false;
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.isBlockbookingTvroService(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}

	public boolean isAllManpowerAndTvroAssigned(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection totalEC = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_MANPOWER, null, false);
			Collection ECAssigned = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_MANPOWER, null, true);
			Collection totalTvro = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_TVRO, null, false);
			Collection tvroAssigned = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_TVRO, null, true);
			
			if(totalEC != null && ECAssigned != null && totalTvro != null && tvroAssigned != null)
			{
				if(totalEC.size() == ECAssigned.size() && totalTvro.size() == tvroAssigned.size())
				{
					return true;
				}
			}
			return false;
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.isAllManpowerAndTvroAssigned(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}

	public void sendAssignmentsToEamms(String requestId, String requestTitle)
	{
		insertFeedsDetails(requestId);
		
		Collection<User> engineeringCoordinatorCol = getECs(requestId);
		if(engineeringCoordinatorCol != null && !engineeringCoordinatorCol.isEmpty())
		{
			for(User ec : engineeringCoordinatorCol)
			{
				DefaultDataObject feedsAssignedEngObj = new DefaultDataObject();
				feedsAssignedEngObj.setProperty("requestId", requestId);
				feedsAssignedEngObj.setProperty("userId", ec.getId());
				
				insertFeedsAssignedEng(feedsAssignedEngObj);
			}
		}
		
		Map emailArgsMap = new HashMap();
		emailArgsMap.put("requestId", requestId);
		emailArgsMap.put("requestTitle", requestTitle);
		emailArgsMap.put("status", "01");
		
		emailArgsMap.put("engineeringCoordinatorCol", engineeringCoordinatorCol);
		feeds_sendMail(emailArgsMap);
	}

	public void insertFeedsAssignedEng(DefaultDataObject feedsAssignedEngObj)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			dao.insertFeedsAssignedEng(feedsAssignedEngObj);
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertFeedsAssignedEng(1) : " + e, e);
		}
	}

	public void insertFeedsAssignments(String requestId, String feedsDetailsId, String tvroServiceId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection<DefaultDataObject> col = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_TVRO, tvroServiceId, false);
			if(col != null && !col.isEmpty())
			{
				for(DefaultDataObject obj : col)
				{
					EammsAssignment assignObj = new EammsAssignment();
					assignObj.setId(UuidGenerator.getInstance().getUuid());
					assignObj.setFeedsDetailsId(feedsDetailsId);
					assignObj.setTvroServiceId(tvroServiceId);
					assignObj.setAssignmentId((String)obj.getProperty("code"));
					assignObj.setRequiredDateFrom((Date)obj.getProperty("requiredFrom"));
					assignObj.setRequiredDateTo((Date)obj.getProperty("requiredTo"));
					assignObj.setRemarks((String)obj.getProperty("remarks"));
					
					String fromTime = (String)obj.getProperty("fromTime");
					String toTime = (String)obj.getProperty("toTime");
					if(fromTime != null)
					{
						String[] ft = fromTime.split(":");
						if(ft.length == 2)
						{
							assignObj.setRequiredTimeFrom(ft[0] + ft[1]);
						}
					}
					
					if(toTime != null)
					{
						String[] tt = toTime.split(":");
						if(tt.length == 2)
						{
							assignObj.setRequiredTimeTo(tt[0] + tt[1]);
						}
					}
					
					Collection tvroCol = dao.getFmsTvroService(null, tvroServiceId, null, null);
					if(tvroCol != null && !tvroCol.isEmpty())
					{
						DefaultDataObject tvroObj = (DefaultDataObject) tvroCol.iterator().next();
						assignObj.setTimezone((String)tvroObj.getProperty("timezone"));
						assignObj.setTimeMeasure((String)tvroObj.getProperty("timeMeasure"));
						assignObj.setCreatedBy((String)tvroObj.getProperty("createdBy"));
						assignObj.setCreatedDate((Date)tvroObj.getProperty("createdDate"));
						
						if(tvroObj.getProperty("totalTimeReq") != null)
						{
							assignObj.setTotalTimeReq(String.valueOf(tvroObj.getProperty("totalTimeReq")));
						}
					}
					
					dao.insertAssignment(assignObj);
				}
			}
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertFeedsAssignments(3) : " + e, e);
		}
	}

	public void insertFeedsDetails(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection tvroCol = dao.getFmsTvroService(requestId, null, null, null);
			if(tvroCol != null && !tvroCol.isEmpty())
			{
				EammsFeedsDetails feedsDetailsObj = new EammsFeedsDetails();
				feedsDetailsObj.setId(UuidGenerator.getInstance().getUuid());
				feedsDetailsObj.setNetworkStatus("01");
				feedsDetailsObj.setRequestId(requestId);
				feedsDetailsObj.setRequestedDate(new Date());
				
				dao.insertEammsFeedsDetails(feedsDetailsObj);
				
				for(Iterator itr = tvroCol.iterator(); itr.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) itr.next();
					insertFeedsAssignments(requestId, feedsDetailsObj.getId(), obj.getId());
				}
			}
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.insertFeedsDetails(1) : " + e, e);
		}
	}

	public Collection getECs(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.getFmsServices(requestId, ServiceDetailsForm.SERVICE_MANPOWER, null, false);
			Collection result = new ArrayList();
			if(col != null && !col.isEmpty())
			{
				SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
				for(Iterator itr = col.iterator(); itr.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) itr.next();
					String userId = (String) obj.getProperty("userId");
					
					if(userId != null && !userId.equals(""))
					{
						User ec = ss.getUser(userId);
						result.add(ec);
					}
				}
			}
			return result;
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getECs(1) : " + e, e);
			return null;
		}
	}

	public boolean isRequestExistInEamms(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.isRequestExistInEamms(requestId);
			if(col != null && !col.isEmpty())
			{
				return true;
			}
			return false;
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.isRequestExistInEamms(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}
	
	public String getFeedsRequestedDate(String requestId)
	{
		EammsFeedsDao dao = (EammsFeedsDao) getDao();
		try 
		{
			Collection col = dao.isRequestExistInEamms(requestId);
			if(col != null && !col.isEmpty())
			{
				DefaultDataObject obj = (DefaultDataObject)col.iterator().next();
				Date requestedDate = (Date)obj.getProperty("requestedDate");
				if(requestedDate != null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");
					try
					{
						return sdf.format(requestedDate);
					}
					catch(Exception e){}
				}
			}
			return "";
		} 
		catch (Exception e)
		{
			Log.getLog(getClass()).error("error @ EammsModule.getFeedsRequestedDate(1) : " + e, e);
			throw new RuntimeException(e);
		}
	}
}
