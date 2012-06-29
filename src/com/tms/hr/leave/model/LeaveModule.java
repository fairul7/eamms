package com.tms.hr.leave.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorIn;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.calendar.model.Appointment;
import com.tms.collab.calendar.model.Attendee;
import com.tms.collab.calendar.model.CalendarEvent;
import com.tms.collab.calendar.model.CalendarModule;
import com.tms.collab.messaging.model.IntranetAccount;
import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.messaging.model.SmtpAccount;
import com.tms.ekms.setup.model.SetupModule;
import com.tms.hr.employee.model.DepartmentDataObject;
import com.tms.hr.employee.model.EmployeeDataObject;
import com.tms.hr.employee.model.EmployeeModule;
import com.tms.util.FormatUtil;

public class LeaveModule extends DefaultModule {

	public static final String APPLICATION_PROPERTY_CALCULATOR = "hr.leave.calculator";

	public static final String APPLICATION_PROPERTY_ANNUAL_LEAVE_TYPE = "hr.leave.annualLeave";

	public static final String PERMISSION_MANAGE_LEAVE = "com.tms.cms.leave.ManageLeaves";

	public static final String DEFAULT_LEAVE_TYPE_ANNUAL = "Annual"; // only

	// hardcoded
	// leave
	// type
	// for
	// carry
	// forward

	public static final int DEFAULT_SETTINGS_YEAR = 0;

	public static final String STATUS_SUBMITTED = "Submitted";

	public static final String STATUS_APPROVED = "Approved";

	public static final String STATUS_REJECTED = "Rejected";

	public static final String STATUS_CANCEL_SUBMITTED = "CancelSubmitted";

	public static final String STATUS_CANCEL_APPROVED = "Cancelled";

	public static final String STATUS_CANCEL_REJECTED = "CancelRejected";

	public static final String LEAVE_CALENDAR_WORKINGDAY_CODE = "A";

	public static final String LEAVE_CALENDAR_NORMALHOLIDAY_CODE = "B";

	public static final String LEAVE_CALENDAR_PUBLICHOLIDAY_CODE = "C";

	public static final String LEAVE_CALENDAR_WORKINGDAY_DESC = "Working Day";

	public static final String LEAVE_CALENDAR_NORMALHOLIDAY_DESC = "Normal Holiday";

	public static final String LEAVE_CALENDAR_PUBLICHOLIDAY_DESC = "Public Holiday";

	public static final String LEAVE_EMPLOYEE_GENDER_MALE = "MALE";

	public static final String LEAVE_EMPLOYEE_GENDER_FEMALE = "FEMALE";

	private SimpleDateFormat sdf = FormatUtil.getInstance().getDateFormat(
			FormatUtil.LONG_DATE);

	private Class calculatorClass;

	private boolean fixedCalendar;

	// --- leave application

	/**
	 * Note: Leave start and end dates must be within the same year.
	 * 
	 * @param leaveTypeId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param reason
	 * @param user
	 * @return The newly created leave entry.
	 * @throws InvalidDateException
	 *             When dates are invalid, e.g. different years
	 * @throws DuplicateDateException
	 *             When leave already applied.
	 * @throws BalanceException
	 *             When user has insufficient balance.
	 * @throws HolidayException
	 *             When leave falls entirely on weekends or holidays
	 * @throws LeaveException
	 *             Other exceptions
	 */
	public LeaveEntry applyLeave(String leaveTypeId, Date startDate,
			Date endDate, boolean halfDay, String userId, String reason,
			User user) throws InvalidDateException, DuplicateDateException,
			BalanceException, HolidayException, LeaveException {
		try {
			// check permission?
			// ensure entry period within same year
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);

			int startYear = start.get(Calendar.YEAR);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			if (startYear != end.get(Calendar.YEAR)) {
				throw new InvalidDateException();
			}

			// check for duplicate
			prepareDates(startDate, endDate);

			int count = countLeaveList(startDate, endDate,
					new String[] { userId }, null, null, new String[] {
							STATUS_SUBMITTED, STATUS_APPROVED,
							STATUS_CANCEL_SUBMITTED, STATUS_CANCEL_REJECTED },
					user);

			if (count > 0) {
				throw new DuplicateDateException();
			}

			// get leave type
			LeaveType lt = viewLeaveType(leaveTypeId);
			String leaveType = lt.getLeaveType();
			String leaveTypeName = lt.getName();

			// set values
			LeaveEntry entry = new LeaveEntry();
			String uuid = UuidGenerator.getInstance().getUuid();
			entry.setId(uuid);
			entry.setStartDate(startDate);
			entry.setEndDate(endDate);
			entry.setLeaveType(leaveType);
			entry.setLeaveTypeId(leaveTypeId);
			entry.setLeaveTypeName(leaveTypeName);
			entry.setHalfDay(halfDay);
			entry.setUserId(userId);
			entry.setReason(reason);
			entry.setAdjustment(false);
			entry.setCredit(false);
			entry.setApplicantId(user.getId());
			entry.setApplicationDate(new Date());
			entry.setApprovalDate(null);
			entry.setApprovalUserId(null);
			entry.setStatus(STATUS_SUBMITTED);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// calculate actual days
			LeaveCalculator calculator = LeaveModule
					.getLeaveCalculator(startYear);
			calculator.fixedCalendar(fixedCalendar);

			float actualDays = calculator.calculateActualDaysForLeave(
					startYear, entry, true);
			entry.setDays(new Float(actualDays));

			// check for weekends and holidays
			if (actualDays == 0) {
				throw new HolidayException();
			}

			// check and update balance
			LeaveSummary summary = viewLeaveSummary(calculator, leaveType,
					userId, startYear, false, user);
			float balance = summary.getBalance();
			balance = Math.round(balance);

			float newBalance = balance + actualDays;

			if (newBalance < 0) {
				throw new BalanceException();
			}

			// store
			LeaveDao dao = (LeaveDao) getDao();
			dao.insertLeaveEntry(entry);

			// notify
			String subject = entry.getLeaveTypeName()
					+ " "
					+ Application.getInstance()
							.getMessage("leave.label.approvalRequired",
									"Approval Required");
			User applicant = utilGetUser(userId);
			String approverId = utilGetApproverId(userId);
			String body = applicant.getName()
					+ " "
					+ Application.getInstance().getMessage(
							"leave.label.hasappliedfor", "has applied for")
					+ " "
					+ entry.getLeaveTypeName()
					+ " "
					+
					/*
					 * Application.getInstance().getMessage("leave.label.leaveType2",
					 * "\n\n<p>Type:") + entry.getLeaveTypeName() +
					 */
					Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate())
					+ Application.getInstance().getMessage(
							"leave.label.Reason", "\n<br>Reason:")
					+ entry.getReason() + "\n\n<p>";

			String link = "/ekms/hrleave/viewApproveList.jsp";
			String linkMessage = Application.getInstance().getMessage(
					"leave.label.ApplyLeaveLink", "Click Here To View");
			utilSendNotification(user.getId(), approverId, null, body, subject,
					link, linkMessage);

			/**
			 * * old notification String subject =
			 * Application.getInstance().getMessage("leave.label.leaveApprovalRequired",
			 * "Leave Approval Required"); User applicant = utilGetUser(userId);
			 * String approverId = utilGetApproverId(userId); String body =
			 * applicant.getName() + " " +
			 * Application.getInstance().getMessage("leave.label.hasappliedforleave",
			 * "has applied for leave.") +
			 * Application.getInstance().getMessage("leave.label.leaveType2",
			 * "\n\n
			 * <p>
			 * Type:") + entry.getLeaveTypeName() +
			 * Application.getInstance().getMessage("leave.label.StartDate",
			 * "\n\n
			 * <p>
			 * Start Date:") + utilFormatDate(entry.getStartDate()) +
			 * Application.getInstance().getMessage("leave.label.EndDate", "\n<br>
			 * End Date:") + utilFormatDate(entry.getEndDate()) +
			 * Application.getInstance().getMessage("leave.label.Reason", "\n<br>
			 * Reason:") + entry.getReason() + "\n\n
			 * <p>"; String link = "/ekms/hrleave/viewApproveList.jsp"; String
			 * linkMessage =
			 * Application.getInstance().getMessage("leave.label.ApplyLeaveLink",
			 * "Click Here To View"); utilSendNotification(user.getId(),
			 * approverId, null, body, subject, link, linkMessage);
			 * 
			 */

			return entry;
		} catch (LeaveException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error applying entry", e);
			throw new LeaveException("Error applying entry", e);
		}
	}

	public LeaveEntry checkEffectiveDays(String leaveTypeId, Date startDate,
			Date endDate, boolean halfDay, String userId, String reason,
			User user) throws InvalidDateException, DuplicateDateException,
			BalanceException, HolidayException, LeaveException {
		try {
			// check permission?
			// ensure entry period within same year
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);

			int startYear = start.get(Calendar.YEAR);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			if (startYear != end.get(Calendar.YEAR)) {
				throw new InvalidDateException();
			}

			// check for duplicate
			prepareDates(startDate, endDate);

			int count = countLeaveList(startDate, endDate,
					new String[] { userId }, null, null, new String[] {
							STATUS_SUBMITTED, STATUS_APPROVED,
							STATUS_CANCEL_SUBMITTED, STATUS_CANCEL_REJECTED },
					user);

			if (count > 0) {
				throw new DuplicateDateException();
			}

			// get leave type
			LeaveType lt = viewLeaveType(leaveTypeId);
			String leaveType = lt.getLeaveType();
			String leaveTypeName = lt.getName();

			// set values
			LeaveEntry entry = new LeaveEntry();
			String uuid = UuidGenerator.getInstance().getUuid();
			entry.setId(uuid);
			entry.setStartDate(startDate);
			entry.setEndDate(endDate);
			entry.setLeaveType(leaveType);
			entry.setLeaveTypeId(leaveTypeId);
			entry.setLeaveTypeName(leaveTypeName);
			entry.setHalfDay(halfDay);
			entry.setUserId(userId);
			entry.setReason(reason);
			entry.setAdjustment(false);
			entry.setCredit(false);
			entry.setApplicantId(user.getId());
			entry.setApplicationDate(new Date());
			entry.setApprovalDate(null);
			entry.setApprovalUserId(null);
			entry.setStatus(STATUS_SUBMITTED);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// calculate actual days
			LeaveCalculator calculator = LeaveModule
					.getLeaveCalculator(startYear);
			calculator.fixedCalendar(fixedCalendar);

			float actualDays = calculator.calculateActualDaysForLeave(
					startYear, entry, true);

			// entry.setDays(new Float(actualDays));

			// check for weekends and holidays
			if (actualDays == 0) {
				throw new HolidayException();
			}

			// check and update balance
			LeaveSummary summary = viewLeaveSummary(calculator, leaveType,
					userId, startYear, false, user);
			float balance = summary.getBalance();
			balance = Math.round(balance);

			float newBalance = balance + actualDays;

			if (newBalance < 0) {

				entry.setDays(new Float(-99999)); // give invalid effective
				// day if balance not
				// enought
				throw new BalanceException();
			} else {
				entry.setDays(new Float(actualDays));
			}
			return entry;
		} catch (LeaveException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error applying entry", e);
			throw new LeaveException("Error applying entry", e);
		}
	}

	public Collection viewLeaveToApproveList(Date startDate, Date endDate,
			User user, String sort, boolean desc, int start, int rows)
			throws LeaveException {
		try {
			Collection results;
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			// check for admin access
			String[] userId;
			boolean isAdmin = checkForAdmin(user);

			if (isAdmin) {
				// get all leave to approve
				userId = null;
			} else {
				// get relevant users only
				userId = viewEmployeeListForApprover(user.getId());
			}

			// query from dao
			results = dao.selectLeaveEntryList(null, startDate, endDate,
					userId, null, null, new String[] { STATUS_SUBMITTED,
							STATUS_CANCEL_SUBMITTED }, Boolean.FALSE,
					Boolean.FALSE, sort, desc, start, rows);

			// get calculator cache
			CalculatorCache calculatorCache = new CalculatorCache();

			// set calculator and user for each entry
			Map employeeMap = viewEmployeeMap(userId);

			for (Iterator i = results.iterator(); i.hasNext();) {
				LeaveEntry entry = (LeaveEntry) i.next();
				LeaveCalculator calculator = calculatorCache
						.getCalculator(entry.getStartDate());
				entry.setCalculator(calculator);

				User u = (User) employeeMap.get(entry.getUserId());

				if (u != null) {
					entry.setUser(u);

					Date date = entry.getStartDate();

					Calendar cal = Calendar.getInstance();
					cal.setTime(date);

					// get leave entitlement
					float entitlementDays = calculator
							.calculateLeaveEntitlement(entry.getLeaveType(),
									entry.getUser().getId(), cal
											.get(Calendar.YEAR));

					// calc carry forward
					float carryForward = calculator.calculateCarryForward(entry
							.getLeaveType(), entry.getUser().getId(), cal
							.get(Calendar.YEAR));

					// calc credited days
					float creditedDays = calculator.calculateLeaveCredited(
							entry.getLeaveType(), entry.getUser().getId(), cal
									.get(Calendar.YEAR));

					// calc days taken
					float takenDays = calculator.calculateLeaveDeducted(entry
							.getLeaveType(), entry.getUser().getId(), cal
							.get(Calendar.YEAR));

					// calc adjustments
					float adjustments = calculator.calculateAdjustments(entry
							.getLeaveType(), entry.getUser().getId(), cal
							.get(Calendar.YEAR));

					// calc balance
					float balance = calculator.calculateBalance(
							entitlementDays, carryForward, creditedDays,
							takenDays, adjustments);

					entry.setBalance(balance);
				}
			}

			return results;
		} catch (Exception e) {
			Log.getLog(getClass())
					.error("Error retrieving leave to approve", e);
			throw new LeaveException("Error retrieving leave to approve", e);
		}
	}

	public int countLeaveToApproveList(Date startDate, Date endDate, User user)
			throws LeaveException {
		try {
			int results;
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			// check for admin access
			String[] userId;
			boolean isAdmin = checkForAdmin(user);

			if (isAdmin) {
				// get all leave to approve
				userId = null;
			} else {
				// get relevant users only
				userId = viewEmployeeListForApprover(user.getId());
			}

			// query from dao
			results = dao.countLeaveEntryList(null, startDate, endDate, userId,
					null, null, new String[] { STATUS_SUBMITTED,
							STATUS_CANCEL_SUBMITTED }, Boolean.FALSE,
					Boolean.FALSE);

			return results;
		} catch (Exception e) {
			Log.getLog(getClass())
					.error("Error retrieving count to approve", e);
			throw new LeaveException("Error retrieving count to approve", e);
		}
	}

	/**
	 * Approve leave
	 * 
	 * @param leaveId
	 * @param comments
	 * @param user
	 * @return
	 * @throws LeaveException
	 * @throws DataObjectNotFoundException
	 */
	public LeaveEntry approveLeave(String leaveId, String comments, User user)
			throws LeaveException, DataObjectNotFoundException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// handle cancellation
			if (STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {
				return approveCancelLeave(leaveId, user);
			}

			// check status
			if (!STATUS_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// check permission
			// update status
			entry.setStatus(STATUS_APPROVED);
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setApprovalComments(comments);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"approvalDate", "approvalUserId", "approvalComments",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());

			String subject = entry.getLeaveTypeName()
					+ " "
					+ Application.getInstance().getMessage(
							"leave.label.Approved", "Approved");
			String body = Application.getInstance().getMessage(
					"leave.label.leaveApprovedBy", "Leave Approved By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());

			/*
			 * old notification String subject =
			 * Application.getInstance().getMessage("leave.label.leaveApproved",
			 * "Leave Approved"); String body =
			 * Application.getInstance().getMessage("leave.label.leaveApprovedBy",
			 * "Leave Approved By") + " " + approver.getName() +
			 * Application.getInstance().getMessage("leave.label.leaveType2",
			 * "\n\n<p>Type:") + entry.getLeaveTypeName() +
			 * Application.getInstance().getMessage("leave.label.StartDate",
			 * "\n\n<p>Start Date:") + utilFormatDate(entry.getStartDate()) +
			 * Application.getInstance().getMessage("leave.label.EndDate", "\n<br>End
			 * Date:") + utilFormatDate(entry.getEndDate());
			 */

			if (entry.isHalfDay()) {
				body += Application.getInstance().getMessage(
						"leave.label.halfDayApprovalMessage", "\n\n<p>");
			}
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			// create calendar event
			String eventId = utilCreateCalendarEvent(entry);

			if (eventId != null) {
				entry.setEventId(eventId);
				dao.updateLeaveEntry(entry, new String[] { "eventId" });
			}
			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (LeaveException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error approving leave", e);
			throw new LeaveException("Error approving leave", e);
		}
	}

	public LeaveEntry rejectLeave(String leaveId, String comments, User user)
			throws DataObjectNotFoundException, InvalidStatusException,
			LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// handle cancellation
			if (STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {
				return rejectCancelLeave(leaveId, user);
			}

			// check status
			if (!STATUS_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// check permission
			// update status
			entry.setStatus(STATUS_REJECTED);
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setApprovalComments(comments);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"approvalDate", "approvalUserId", "approvalComments",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());
			String subject = entry.getLeaveTypeName()
					+ " "
					+ Application.getInstance().getMessage(
							"leave.label.rejected", "Rejected");
			String body = Application.getInstance().getMessage(
					"leave.label.leaveRejectedBy", "Leave Rejected By")
					+ " "
					+ approver.getName()
					+

					Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			/*
			 * User approver = utilGetUser(user.getId()); String subject =
			 * Application.getInstance().getMessage("leave.label.leaveRejected",
			 * "Leave Rejected"); String body =
			 * Application.getInstance().getMessage("leave.label.leaveRejectedBy",
			 * "Leave Rejected By") + " " + approver.getName() +
			 * Application.getInstance().getMessage("leave.label.leaveType2",
			 * "\n\n<p>Type:") + entry.getLeaveTypeName() +
			 * Application.getInstance().getMessage("leave.label.StartDate",
			 * "\n\n<p>Start Date:") + utilFormatDate(entry.getStartDate()) +
			 * Application.getInstance().getMessage("leave.label.EndDate", "\n<br>End
			 * Date:") + utilFormatDate(entry.getEndDate());
			 * utilSendNotification(user.getId(), entry.getUserId(), null, body,
			 * subject, null, null);
			 */

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error rejecting leave", e);
			throw new LeaveException("Error rejecting leave", e);
		}
	}

	public LeaveEntry rejectLeaveWithReason(String leaveId, String reason,
			String comments, User user) throws DataObjectNotFoundException,
			InvalidStatusException, LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// handle cancellation
			if (STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {
				return rejectCancelLeave(leaveId, user);
			}

			// check status
			if (!STATUS_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// check permission
			// update status
			entry.setStatus(STATUS_REJECTED);
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setApprovalComments(comments);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"approvalDate", "approvalUserId", "approvalComments",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());
			String subject = Application.getInstance().getMessage(
					"leave.label.leaveRejected", "Leave Rejected");
			String body = Application.getInstance().getMessage(
					"leave.label.leaveRejectedBy", "Leave Rejected By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate())
					+ "\n\n<p>"
					+ Application.getInstance().getMessage(
							"leave.label.rejectreason", "\n<br>Reject Reason:")
					+ reason;

			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error rejecting leave", e);
			throw new LeaveException("Error rejecting leave", e);
		}
	}

	public LeaveEntry cancelLeave(String leaveId, User user)
			throws DataObjectNotFoundException, InvalidStatusException,
			LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// check permission
			// update status
			if (STATUS_SUBMITTED.equals(entry.getStatus())) {
				entry.setStatus(STATUS_CANCEL_APPROVED);
			} else if (!entry.isCredit()
					&& STATUS_APPROVED.equals(entry.getStatus())) {
				entry.setStatus(STATUS_CANCEL_SUBMITTED);
			} else {
				throw new InvalidStatusException();
			}

			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			if (STATUS_CANCEL_APPROVED.equals(entry.getStatus())) {
				String subject = Application.getInstance().getMessage(
						"leave.label.leaveCancelled", "Leave Cancelled");
				User applicant = utilGetUser(entry.getUserId());
				String approverId = utilGetApproverId(entry.getUserId());
				String body = applicant.getName()
						+ " "
						+ Application.getInstance().getMessage(
								"leave.label.hascancelledleave",
								"has cancelled leave.")
						+ Application.getInstance().getMessage(
								"leave.label.leaveType2", "\n\n<p>Type:")
						+ entry.getLeaveTypeName()
						+ Application.getInstance().getMessage(
								"leave.label.StartDate", "\n\n<p>Start Date:")
						+ utilFormatDate(entry.getStartDate())
						+ Application.getInstance().getMessage(
								"leave.label.EndDate", "\n<br>End Date:")
						+ utilFormatDate(entry.getEndDate()) + "\n\n<p>";
				String link = null;
				String linkMessage = null;
				utilSendNotification(user.getId(), approverId, null, body,
						subject, link, linkMessage);
			} else {
				String subject = Application.getInstance().getMessage(
						"leave.label.leaveCancellationApprovalRequired",
						"Leave Cancellation Approval Required");
				User applicant = utilGetUser(entry.getUserId());
				String approverId = utilGetApproverId(entry.getUserId());
				String body = applicant.getName()
						+ " "
						+ Application.getInstance().getMessage(
								"leave.label.hasappliedtocancelleave",
								"has applied to cancel leave.")
						+ Application.getInstance().getMessage(
								"leave.label.leaveType2", "\n\n<p>Type:")
						+ entry.getLeaveTypeName()
						+ Application.getInstance().getMessage(
								"leave.label.StartDate", "\n\n<p>Start Date:")
						+ utilFormatDate(entry.getStartDate())
						+ Application.getInstance().getMessage(
								"leave.label.EndDate", "\n<br>End Date:")
						+ utilFormatDate(entry.getEndDate()) + "\n\n<p>";
				String link = "/ekms/hrleave/viewApproveList.jsp";

				String linkMessage = Application.getInstance().getMessage(
						"leave.label.ApplyLeaveLink", "Click Here To View");
				utilSendNotification(user.getId(), approverId, null, body,
						subject, link, linkMessage);
			}

			// delete calendar event
			if (STATUS_CANCEL_APPROVED.equals(entry.getStatus())) {
				utilDeleteCalendarEvent(entry);
			}

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error submitting leave cancellation",
					e);
			throw new LeaveException("Error submitting leave cancellation", e);
		}
	}

	public LeaveEntry approveCancelLeave(String leaveId, User user)
			throws DataObjectNotFoundException, InvalidStatusException,
			LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// check permission
			// check status
			if (!STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// update status
			entry.setStatus(STATUS_CANCEL_APPROVED);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			String subject = Application.getInstance().getMessage(
					"leave.label.leaveCancellationApproved",
					"Leave Cancellation Approved");
			User approver = utilGetUser(user.getId());
			String body = Application.getInstance().getMessage(
					"leave.label.cancellationofLeaveApprovedBy",
					"Cancellation of Leave Approved By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			// delete calendar event
			if (STATUS_CANCEL_APPROVED.equals(entry.getStatus())) {
				utilDeleteCalendarEvent(entry);
			}
			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error approving leave cancellation",
					e);
			throw new LeaveException(
					"Error approving leave leave cancellation", e);
		}
	}

	public LeaveEntry rejectCancelLeave(String leaveId, User user)
			throws DataObjectNotFoundException, InvalidStatusException,
			LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// check permission
			// check status
			if (!STATUS_CANCEL_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// update status
			entry.setStatus(STATUS_CANCEL_REJECTED);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());
			String subject = Application.getInstance().getMessage(
					"leave.label.leaveCancellationRejected",
					"Leave Cancellation Rejected");
			String body = Application.getInstance().getMessage(
					"leave.label.cancellationofLeaveRejectedBy",
					"Cancellation of Leave Rejected By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error rejecting leave cancellation",
					e);
			throw new LeaveException(
					"Error rejecting leave leave cancellation", e);
		}
	}

	// --- credit leave application
	public LeaveEntry applyCreditLeave(String leaveTypeId, Date startDate,
			Date endDate, boolean halfDay, String userId, String reason,
			User user) throws InvalidDateException, DuplicateDateException,
			BalanceException, HolidayException, LeaveException {
		try {
			// ensure entry period within same year
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);

			int startYear = start.get(Calendar.YEAR);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			if (startYear != end.get(Calendar.YEAR)) {
				throw new InvalidDateException();
			}

			// check for duplicate
			prepareDates(startDate, endDate);

			int count = countCreditLeaveList(startDate, endDate,
					new String[] { userId }, null, null, new String[] {
							STATUS_SUBMITTED, STATUS_APPROVED }, user);

			if (count > 0) {
				throw new DuplicateDateException();
			}

			// get leave type
			LeaveType lt = viewLeaveType(leaveTypeId);
			String leaveType = lt.getLeaveType();
			String leaveTypeName = lt.getName();

			// set values
			LeaveEntry entry = new LeaveEntry();
			String uuid = UuidGenerator.getInstance().getUuid();
			entry.setId(uuid);
			entry.setStartDate(startDate);
			entry.setEndDate(endDate);
			entry.setLeaveType(leaveType);
			entry.setLeaveTypeId(leaveTypeId);
			entry.setLeaveTypeName(leaveTypeName);
			entry.setHalfDay(halfDay);
			entry.setUserId(userId);
			entry.setReason(reason);
			entry.setAdjustment(false);
			entry.setCredit(true);
			entry.setApplicantId(user.getId());
			entry.setApplicationDate(new Date());
			entry.setApprovalDate(null);
			entry.setApprovalUserId(null);
			entry.setStatus(STATUS_SUBMITTED);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// calculate actual days
			LeaveCalculator calculator = LeaveModule
					.getLeaveCalculator(startYear);
			float actualDays = calculator.calculateActualDaysForCreditLeave(
					startYear, entry, true);
			entry.setDays(new Float(actualDays));

			// store
			LeaveDao dao = (LeaveDao) getDao();
			dao.insertLeaveEntry(entry);

			// notify
			String subject = Application.getInstance().getMessage(
					"leave.label.creditLeaveApprovalRequired",
					"Credit Leave Approval Required");
			User applicant = utilGetUser(userId);
			String approverId = utilGetApproverId(userId);
			String body = applicant.getName()
					+ " "
					+ Application.getInstance().getMessage(
							"leave.label.hasappliedforcreditleave",
							"has applied for credit leave.")
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate()) + "\n\n<p>";

			String link = "/ekms/hrleave/viewApproveList.jsp";
			String linkMessage = Application.getInstance().getMessage(
					"leave.label.ApplyLeaveLink", "Click Here To View");
			utilSendNotification(user.getId(), approverId, null, body, subject,
					link, linkMessage);

			return entry;
		} catch (LeaveException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error applying entry", e);
			throw new LeaveException("Error applying entry", e);
		}
	}

	public Collection viewCreditLeaveToApproveList(Date startDate,
			Date endDate, User user, String sort, boolean desc, int start,
			int rows) throws LeaveException {
		try {
			Collection results;
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			// check for admin access
			String[] userId;
			boolean isAdmin = checkForAdmin(user);

			if (isAdmin) {
				// get all leave to approve
				userId = null;
			} else {
				// get relevant users only
				userId = viewEmployeeListForApprover(user.getId());
			}

			// query from dao
			results = dao.selectLeaveEntryList(null, startDate, endDate,
					userId, null, null, new String[] { STATUS_SUBMITTED,
							STATUS_CANCEL_SUBMITTED }, Boolean.TRUE,
					Boolean.FALSE, sort, desc, start, rows);

			// get calculator
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);

			int year = cal.get(Calendar.YEAR);
			LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);

			// set calculator and user for each entry
			Map employeeMap = viewEmployeeMap(userId);

			for (Iterator i = results.iterator(); i.hasNext();) {
				LeaveEntry entry = (LeaveEntry) i.next();
				entry.setCalculator(calculator);

				User u = (User) employeeMap.get(entry.getUserId());

				if (u != null) {
					entry.setUser(u);

					Date date = entry.getStartDate();

					Calendar cal2 = Calendar.getInstance();
					cal.setTime(date);

					// get leave entitlement
					float entitlementDays = calculator
							.calculateLeaveEntitlement(entry.getLeaveType(),
									entry.getUser().getId(), cal2
											.get(Calendar.YEAR));

					// calc carry forward
					float carryForward = calculator.calculateCarryForward(entry
							.getLeaveType(), entry.getUser().getId(), cal2
							.get(Calendar.YEAR));

					// calc credited days
					float creditedDays = calculator.calculateLeaveCredited(
							entry.getLeaveType(), entry.getUser().getId(), cal2
									.get(Calendar.YEAR));

					// calc days taken
					float takenDays = calculator.calculateLeaveDeducted(entry
							.getLeaveType(), entry.getUser().getId(), cal2
							.get(Calendar.YEAR));

					// calc adjustments
					float adjustments = calculator.calculateAdjustments(entry
							.getLeaveType(), entry.getUser().getId(), cal2
							.get(Calendar.YEAR));

					// calc balance
					float balance = calculator.calculateBalance(
							entitlementDays, carryForward, creditedDays,
							takenDays, adjustments);

					entry.setBalance(balance);
				}
			}
			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Error retrieving credit leave to approve", e);
			throw new LeaveException(
					"Error retrieving credit leave to approve", e);
		}
	}

	public int countCreditLeaveToApproveList(Date startDate, Date endDate,
			User user) throws LeaveException {
		try {
			int results;
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			// check for admin access
			String[] userId;
			boolean isAdmin = checkForAdmin(user);

			if (isAdmin) {
				// get all leave to approve
				userId = null;
			} else {
				// get relevant users only
				userId = viewEmployeeListForApprover(user.getId());
			}

			// query from dao
			results = dao.countLeaveEntryList(null, startDate, endDate, userId,
					null, null, new String[] { STATUS_SUBMITTED,
							STATUS_CANCEL_SUBMITTED }, Boolean.TRUE,
					Boolean.FALSE);

			return results;
		} catch (Exception e) {
			Log.getLog(getClass())
					.error("Error retrieving count to approve", e);
			throw new LeaveException("Error retrieving count to approve", e);
		}
	}

	public LeaveEntry approveCreditLeave(String leaveId, String comments,
			User user) throws DataObjectNotFoundException,
			InvalidStatusException, LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// check status
			if (!entry.isCredit()
					|| !STATUS_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// check permission
			// update status
			entry.setStatus(STATUS_APPROVED);
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setApprovalComments(comments);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"approvalDate", "approvalUserId", "approvalComments",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());
			String subject = Application.getInstance().getMessage(
					"leave.label.creditLeaveApproved", "Credit Leave Approved");
			String body = Application.getInstance().getMessage(
					"leave.label.creditLeaveApprovedBy",
					"Credit Leave Approved By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error approving credit leave", e);
			throw new LeaveException("Error approving credit leave", e);
		}
	}

	public LeaveEntry rejectCreditLeave(String leaveId, String comments,
			User user) throws LeaveException, InvalidStatusException,
			DataObjectNotFoundException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// check status
			if (!entry.isCredit()
					|| !STATUS_SUBMITTED.equals(entry.getStatus())) {
				throw new InvalidStatusException();
			}

			// check permission
			// update status
			entry.setStatus(STATUS_REJECTED);
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setApprovalComments(comments);
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// save
			dao.updateLeaveEntry(entry, new String[] { "status",
					"approvalDate", "approvalUserId", "approvalComments",
					"lastModifiedDate", "lastModifiedUserId" });

			// notify
			User approver = utilGetUser(user.getId());
			String subject = Application.getInstance().getMessage(
					"leave.label.creditLeaveRejected", "Credit Leave Rejected");
			String body = Application.getInstance().getMessage(
					"leave.label.creditLeaveRejectedBy",
					"Credit Leave Rejected By")
					+ " "
					+ approver.getName()
					+ Application.getInstance().getMessage(
							"leave.label.leaveType2", "\n\n<p>Type:")
					+ entry.getLeaveTypeName()
					+ Application.getInstance().getMessage(
							"leave.label.StartDate", "\n\n<p>Start Date:")
					+ utilFormatDate(entry.getStartDate())
					+ Application.getInstance().getMessage(
							"leave.label.EndDate", "\n<br>End Date:")
					+ utilFormatDate(entry.getEndDate());
			utilSendNotification(user.getId(), entry.getUserId(), null, body,
					subject, null, null);

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (InvalidStatusException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error rejecting credit leave", e);
			throw new LeaveException("Error rejecting credit leave", e);
		}
	}

	// --- adjustments
	public void submitAdjustment(String leaveTypeId, int year, float days,
			String userId, String reason, User user)
			throws DataObjectNotFoundException, LeaveException {
		try {
			// check permission
			// create dates
			Calendar cal = Calendar.getInstance();

			if (cal.get(Calendar.YEAR) > year) {
				cal.set(year, Calendar.DECEMBER, 31);
			} else if (cal.get(Calendar.YEAR) < year) {
				cal.set(year, Calendar.JANUARY, 1);
			}

			Date date = cal.getTime();

			// get leave type
			LeaveType lt = viewLeaveType(leaveTypeId);
			String leaveType = lt.getLeaveType();
			String leaveTypeName = lt.getName();

			// create entry
			LeaveEntry entry = new LeaveEntry();
			String uuid = UuidGenerator.getInstance().getUuid();
			entry.setId(uuid);
			entry.setStartDate(date);
			entry.setEndDate(date);
			entry.setLeaveType(leaveType);
			entry.setLeaveTypeId(leaveTypeId);
			entry.setLeaveTypeName(leaveTypeName);
			entry.setUserId(userId);
			entry.setReason(reason);
			entry.setAdjustment(true);
			entry.setCredit(false);
			entry.setApplicantId(user.getId());
			entry.setApplicationDate(new Date());
			entry.setApprovalDate(new Date());
			entry.setApprovalUserId(user.getId());
			entry.setStatus(STATUS_APPROVED);
			entry.setDays(new Float(days));
			entry.setLastModifiedDate(new Date());
			entry.setLastModifiedUserId(user.getId());

			// store
			LeaveDao dao = (LeaveDao) getDao();
			dao.insertLeaveEntry(entry);

			// notify
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error submitting adjustment", e);
			throw new LeaveException("Error submitting adjustment", e);
		}
	}

	// --- view leave entries
	public LeaveEntry viewLeaveEntry(String leaveId, User user)
			throws LeaveException, DataObjectNotFoundException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// get entry
			LeaveEntry entry = dao.selectLeaveEntry(leaveId);

			// load user
			try {
				SecurityService security = (SecurityService) Application
						.getInstance().getService(SecurityService.class);
				User leaveUser = (User) security.getUser(entry.getUserId());
				entry.setUser(leaveUser);
			} catch (Exception e) {
				Log.getLog(getClass()).debug(
						"Unable to retrieve user " + entry.getUserId());
			}

			return entry;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Error retrieving leave entry " + leaveId, e);
			throw new LeaveException("Error retrieving leave entry " + leaveId,
					e);
		}
	}

	/**
	 * Retrieves a list of leave taken
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param user
	 * @param sort
	 * @param desc
	 * @param start
	 * @param rows
	 * @return A Collection LeaveEntry objects
	 * @throws LeaveException
	 */
	public Collection viewLeaveList(Date startDate, Date endDate,
			String[] userId, String applicantId, String leaveType,
			String[] status, User user, String sort, boolean desc, int start,
			int rows) throws LeaveException {
		return viewAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.FALSE, Boolean.FALSE, user, sort,
				desc, start, rows);
	}

	/**
	 * Retrieves a count of leave taken
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param user
	 * @return
	 * @throws LeaveException
	 */
	public int countLeaveList(Date startDate, Date endDate, String[] userId,
			String applicantId, String leaveType, String[] status, User user)
			throws LeaveException {
		return countAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.FALSE, Boolean.FALSE, user);
	}

	/**
	 * Retrieves a Collection of credit leave entries for the specified
	 * parameters
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param user
	 * @param sort
	 * @param desc
	 * @param start
	 * @param rows
	 * @return A Collection of LeaveEntry objects.
	 * @throws LeaveException
	 */
	public Collection viewCreditLeaveList(Date startDate, Date endDate,
			String[] userId, String applicantId, String leaveType,
			String[] status, User user, String sort, boolean desc, int start,
			int rows) throws LeaveException {
		return viewAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.TRUE, Boolean.FALSE, user, sort,
				desc, start, rows);
	}

	/**
	 * Retrieves a count of credit leave entries for the specified parameters
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param status
	 * @param user
	 * @return
	 * @throws LeaveException
	 */
	public int countCreditLeaveList(Date startDate, Date endDate,
			String[] userId, String applicantId, String leaveType,
			String[] status, User user) throws LeaveException {
		return countAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.TRUE, Boolean.FALSE, user);
	}

	/**
	 * Retrieves a list of adjustments
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param user
	 * @param sort
	 * @param desc
	 * @param start
	 * @param rows
	 * @return A Collection of LeaveEntry objects.
	 * @throws LeaveException
	 */
	public Collection viewAdjustmentList(Date startDate, Date endDate,
			String[] userId, String applicantId, String leaveType,
			String[] status, User user, String sort, boolean desc, int start,
			int rows) throws LeaveException {
		return viewAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.FALSE, Boolean.TRUE, user, sort,
				desc, start, rows);
	}

	/**
	 * Retrieves a count of adjustments
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param user
	 * @return
	 * @throws LeaveException
	 */
	public int countAdjustmentList(Date startDate, Date endDate,
			String[] userId, String applicantId, String leaveType,
			String[] status, User user) throws LeaveException {
		return countAllLeaveList(null, startDate, endDate, userId, applicantId,
				leaveType, status, Boolean.FALSE, Boolean.TRUE, user);
	}

	/**
	 * Returns all leave entries (including credit and adjustments)
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param credit
	 * @param adjustment
	 * @param user
	 * @param sort
	 * @param desc
	 * @param start
	 * @param rows
	 * @return
	 * @throws LeaveException
	 */
	public Collection viewAllLeaveList(String leaveTypeName, Date startDate,
			Date endDate, String[] userId, String applicantId,
			String leaveType, String[] status, Boolean credit,
			Boolean adjustment, User user, String sort, boolean desc,
			int start, int rows) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			Collection results = dao.selectLeaveEntryList(leaveTypeName,
					startDate, endDate, userId, applicantId, leaveType, status,
					credit, adjustment, sort, desc, start, rows);

			// set calculator for each entry
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);

			int year = cal.get(Calendar.YEAR);
			LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);

			for (Iterator i = results.iterator(); i.hasNext();) {
				LeaveEntry entry = (LeaveEntry) i.next();

				calculator
						.fixedCalendar(entry.getFixedCalendar().equals("1") ? true
								: false);
				entry.setCalculator(calculator);
			}
			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving leave entries", e);
			throw new LeaveException("Error retrieving leave entries", e);
		}
	}

	/**
	 * Counts all leave entries (including credit and adjustments
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param applicantId
	 * @param leaveType
	 * @param status
	 * @param credit
	 * @param adjustment
	 * @param user
	 * @return
	 * @throws LeaveException
	 */
	public int countAllLeaveList(String leaveTypeName, Date startDate,
			Date endDate, String[] userId, String applicantId,
			String leaveType, String[] status, Boolean credit,
			Boolean adjustment, User user) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			int results = dao.countLeaveEntryList(leaveTypeName, startDate,
					endDate, userId, applicantId, leaveType, status, credit,
					adjustment);

			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving leave count", e);
			throw new LeaveException("Error retrieving leave count", e);
		}
	}

	// --- view leave summary

	/**
	 * Returns the leave type for annual leave (used for carry forward)
	 * 
	 * @return
	 */
	public String getAnnualLeaveType() {
		Application app = Application.getInstance();
		String leaveType = app
				.getProperty(APPLICATION_PROPERTY_ANNUAL_LEAVE_TYPE);

		if (leaveType == null) {
			leaveType = DEFAULT_LEAVE_TYPE_ANNUAL;
		}
		return leaveType;
	}

	/**
	 * Returns a LeaveCalculator that is responsible for calcuting leave
	 * entitlement, balance, etc. You can implement a new implementation by
	 * extending LeaveCalculator and overriding the appropriate methods, and
	 * defining the application property "hr.leave.calculator" with the value of
	 * fully qualified class name.
	 * 
	 * @return
	 */
	protected LeaveCalculator getLeaveCalculator() {
		LeaveCalculator calculator = null;

		if (calculatorClass == null) {
			String className = Application.getInstance().getProperty(
					APPLICATION_PROPERTY_CALCULATOR);

			// determine class
			try {
				if ((className != null) && (className.trim().length() > 0)) {
					calculatorClass = Class.forName(className);
				}
			} catch (ClassNotFoundException e) {
				Log.getLog(getClass()).error(
						"Error creating leave calculator class " + className
								+ ", using default LeaveCalculator", e);
			}
			if (calculatorClass == null) {
				calculatorClass = LeaveCalculator.class;
			}
		}
		try {
			calculator = (LeaveCalculator) calculatorClass.newInstance();
		} catch (Exception e) {
			Log.getLog(getClass()).debug(
					"Error instantiating leave calculator "
							+ calculatorClass.getName(), e);
			calculator = new LeaveCalculator();
		}

		return calculator;
	}

	/**
	 * Retrieves summaries for specified users.
	 * 
	 * @param leaveType
	 * @param userId
	 * @param year
	 * @param user
	 * @return A Collection of LeaveSummary objects.
	 * @throws LeaveException
	 */
	public Collection viewLeaveSummaryList(String leaveType, String[] userId,
			int year, User user) throws LeaveException {
		try {
			Collection results = new ArrayList();

			// get users
			Map employeeMap = viewEmployeeMap(userId);

			// get calculator
			LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);

			// loop and get leave summary
			for (int i = 0; i < userId.length; i++) {
				String uid = userId[i];

				try {
					LeaveSummary summary = viewLeaveSummary(calculator,
							leaveType, uid, year, false, user);
					User u = (User) employeeMap.get(summary.getUserId());

					if (u != null) {
						summary.setUser(u);
					}
					results.add(summary);
				} catch (DataObjectNotFoundException e) {
					Log.getLog(getClass()).debug(
							"Error leave summary for user " + uid, e);
				}
			}
			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving leave summary list",
					e);
			throw new LeaveException("Error retrieving leave summary list", e);
		}
	}

	/**
	 * Retrieves summary of leave, indicating entitlement, carry forward, etc.
	 * 
	 * @param leaveType
	 * @param userId
	 * @param year
	 * @param loadUser
	 *            Set true to load the user object from the security service
	 * @param user
	 * @return
	 */
	public LeaveSummary viewLeaveSummary(String leaveType, String userId,
			int year, boolean loadUser, User user)
			throws DataObjectNotFoundException, LeaveException {
		LeaveCalculator calculator = LeaveModule.getLeaveCalculator(year);

		return viewLeaveSummary(calculator, leaveType, userId, year, loadUser,
				user);
	}

	/**
	 * Retrieves summary of leave, indicating entitlement, carry forward, etc.
	 * 
	 * @param calculator
	 * @param leaveType
	 * @param userId
	 * @param year
	 * @param loadUser
	 *            Set true to load the user object from the security service
	 * @param user
	 * @return
	 */
	public LeaveSummary viewLeaveSummary(LeaveCalculator calculator,
			String leaveType, String userId, int year, boolean loadUser,
			User user) throws DataObjectNotFoundException, LeaveException {
		LeaveSummary summary = new LeaveSummary();

		// get user
		User leaveUser = null;

		if (loadUser) {
			try {
				SecurityService security = (SecurityService) Application
						.getInstance().getService(SecurityService.class);
				leaveUser = security.getUser(userId);
			} catch (Exception e) {
				;
			}
		}
		try {
			// get leave entitlement
			float entitlementDays = calculator.calculateLeaveEntitlement(
					leaveType, userId, year);

			// calc carry forward
			float carryForward = calculator.calculateCarryForward(leaveType,
					userId, year);

			// calc credited days
			float creditedDays = calculator.calculateLeaveCredited(leaveType,
					userId, year);

			// calc days taken
			float takenDays = calculator.calculateLeaveDeducted(leaveType,
					userId, year);

			// calc adjustments
			float adjustments = calculator.calculateAdjustments(leaveType,
					userId, year);

			// calc balance
			float balance = calculator.calculateBalance(entitlementDays,
					carryForward, creditedDays, takenDays, adjustments);

			summary.setUserId(userId);
			summary.setUser(leaveUser);
			summary.setYear(year);
			summary.setLeaveType(leaveType);
			summary.setEntitlement(entitlementDays);
			summary.setCarryForward(carryForward);
			summary.setCredited(creditedDays);
			summary.setTaken(takenDays);
			summary.setAdjustments(adjustments);
			summary.setBalance(balance);

			return summary;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (LeaveException e) {
			throw e;
		} finally {
			calculator = null;
		}
	}

	// --- entitlement

	/**
	 * Returns the entitlement for a user for a specified year
	 * 
	 * @param serviceClass
	 * @param leaveType
	 * @param year
	 * @return A Collection of LeaveEntitlement objects
	 */
	public Collection viewLeaveEntitlementList(String serviceClass,
			String leaveType, int year) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			Collection results = dao.selectLeaveEntitlement(serviceClass,
					leaveType, year);

			if (results.size() == 0) {
				// entitlement not set for specified year, get default setting
				results = dao.selectLeaveEntitlement(serviceClass, leaveType,
						DEFAULT_SETTINGS_YEAR);
			}
			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving leave entitlement",
					e);
			throw new LeaveException("Error retrieving leave entitlement", e);
		}
	}

	public void updateLeaveEntitlement(String serviceClassId, String leaveType,
			int year, int serviceYears, int entitlement) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			// retrieve existing entitlement for year
			boolean updated = false;
			// Collection list = viewLeaveEntitlementList(null, null, year);
			// Collection list = viewLeaveEntitlementList(serviceClassId,
			// leaveType, year);
			Collection list = viewLeaveEntitlementList(null, leaveType, year);

			for (Iterator i = list.iterator(); i.hasNext();) {
				LeaveEntitlement le = (LeaveEntitlement) i.next();

				if (serviceClassId.equals(le.getServiceClassId())
						&& leaveType.equals(le.getLeaveType())
						&& (serviceYears == le.getServiceYears())) {
					le.setEntitlement(entitlement);
					updated = true;
				}
			}
			if (!updated) {
				// not found, create new
				LeaveEntitlement le = new LeaveEntitlement();
				le.setServiceClassId(serviceClassId);
				le.setLeaveType(leaveType);
				le.setYear(year);
				le.setServiceYears(serviceYears);
				le.setEntitlement(entitlement);
				list.add(le);
			}
			// update for year
			LeaveEntitlement[] array = (LeaveEntitlement[]) list
					.toArray(new LeaveEntitlement[0]);
			dao.updateLeaveEntitlement(array, year);

			// update default settings
			dao.updateLeaveEntitlement(array, DEFAULT_SETTINGS_YEAR);
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error updating entitlement", e);
			throw new LeaveException("Error updating entitlement", e);
		}
	}

	public void deleteLeaveEntitlement(String[] ids) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();

			for (int i = 0; i < ids.length; i++) {
				// retrieve entitlement to delete
				String id = ids[i];

				try {
					LeaveEntitlement le = dao.selectLeaveEntitlementById(id);
					dao.deleteLeaveEntitlement(le.getServiceClassId(), le
							.getLeaveType(), (int) le.getYear(), (int) le
							.getServiceYears());

					dao.deleteLeaveEntitlement(le.getServiceClassId(), le
							.getLeaveType(), DEFAULT_SETTINGS_YEAR, (int) le
							.getServiceYears());
				} catch (DataObjectNotFoundException e) {
					Log.getLog(getClass()).debug(
							"Error deleting entitlement " + id);
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error deleting entitlement", e);
			throw new LeaveException("Error deleting entitlement", e);
		}
	}

	public float viewCarryForward(String leaveType, String userId, long year)
			throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			float result = dao.selectCarryForward(leaveType, userId, year);

			return result;
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Error retrieving leave carry forward", e);
			throw new LeaveException("Error retrieving leave carry forward", e);
		}
	}

	public void processCarryForward(int year) throws LeaveException {
		try {
			// get calculator
			LeaveCalculator calculator = getLeaveCalculator(year);
			LeaveDao dao = (LeaveDao) getDao();
			String leaveType = getAnnualLeaveType();
			int previousYear = year - 1;

			// iterate through all employees
			Application app = Application.getInstance();
			EmployeeModule em = (EmployeeModule) app
					.getModule(EmployeeModule.class);
			Collection employeeList = em.getAllEmployees();

			for (Iterator i = employeeList.iterator(); i.hasNext();) {
				EmployeeDataObject edo = (EmployeeDataObject) i.next();

				// calculate carry forward days
				String userId = edo.getEmployeeID();
				LeaveSummary summary = viewLeaveSummary(calculator, leaveType,
						userId, previousYear, false, null);
				float balance = summary.getBalance();
				float carryForwardDays = calculator
						.calculateCarryForward(balance);

				// update
				dao.updateCarryForward(leaveType, userId, year,
						carryForwardDays);
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Error processing leave carry forward", e);
			throw new LeaveException("Error processing leave carry forward", e);
		}
	}

	// --- leave types
	public LeaveType viewLeaveType(String id) throws LeaveException,
			DataObjectNotFoundException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			LeaveType results = dao.selectLeaveType(id);

			return results;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass())
					.error("Error retrieving leave type " + id, e);
			throw new LeaveException("Error retrieving leave type " + id, e);
		}
	}

	/**
	 * 
	 * @param userId
	 * @param onlyCredit
	 *            Only show leave types that can be credited
	 * @param showAll
	 *            Show all leave types irrespective of gender and can be
	 *            credited or not
	 * @return A Collection of LeaveType objects
	 * @throws LeaveException
	 */
	public Collection viewLeaveTypeList(String userId, boolean onlyCredit,
			boolean showAll) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			Collection results = dao.selectLeaveTypeList();

			String gender = null;

			if (!showAll) {
				// get user gender
				EmployeeModule em = (EmployeeModule) Application.getInstance()
						.getModule(EmployeeModule.class);
				Collection col = em.getEmployee(userId);

				if (col.size() > 0) {
					EmployeeDataObject edo = (EmployeeDataObject) col
							.iterator().next();

					if (LEAVE_EMPLOYEE_GENDER_MALE.equals(edo.getGender())) {
						gender = "M";
					} else if (LEAVE_EMPLOYEE_GENDER_FEMALE.equals(edo
							.getGender())) {
						gender = "F";
					}
				}
			}
			// filter unsuitable types
			if (!showAll || onlyCredit) {
				for (Iterator i = results.iterator(); i.hasNext();) {
					LeaveType lt = (LeaveType) i.next();

					if ((lt.getGender() != null)
							&& (lt.getGender().trim().length() > 0)
							&& !lt.getGender().equals(gender)) {
						i.remove();
					} else if (onlyCredit && !lt.isCreditAllowed()) {
						i.remove();
					}
				}
			}
			return results;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving leave types", e);
			throw new LeaveException("Error retrieving leave types", e);
		}
	}

	// CRUD

	/**
	 * Inserts or updates a leave type
	 * 
	 * @param leaveType
	 */
	public void updateLeaveType(LeaveType leaveType) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			dao.updateLeaveType(leaveType);
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error updating leave type", e);
			throw new LeaveException("Error updating leave type", e);
		}
	}

	public void fixedCalendar(boolean fixedCalendar) {
		this.fixedCalendar = fixedCalendar;
	}

	public void deleteLeaveType(String leaveTypeId) throws LeaveException,
			DataObjectNotFoundException {
		try {
			// check and disallow annual leave deletion
			LeaveType lt = viewLeaveType(leaveTypeId);
			String annualLeaveType = getAnnualLeaveType();

			if (annualLeaveType.equals(lt.getId())) {
				return;
			}
			LeaveDao dao = (LeaveDao) getDao();
			dao.deleteLeaveType(leaveTypeId);
		} catch (DataObjectNotFoundException e) {
			Log.getLog(getClass()).error(
					"Error deleting leave type, cannot find ID " + leaveTypeId,
					e);
			throw e;
		} catch (LeaveException e) {
			Log.getLog(getClass()).error("Error deleting leave type", e);
			throw e;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error deleting leave type", e);
			throw new LeaveException("Error deleting leave type", e);
		}
	}

	// --- holidays

	/**
	 * Retrieves a list of holidays for a date range
	 * 
	 * @param startDate
	 *            Required
	 * @param endDate
	 *            Required
	 * @param search
	 * @param sort
	 * @param desc
	 * @param start
	 * @param rows
	 * @return A Collection of LeaveHoliday objects
	 * @throws LeaveException
	 */
	public Collection viewHolidayList(Date startDate, Date endDate,
			String search, String sort, boolean desc, int start, int rows)
			throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			prepareDates(startDate, endDate);

			Collection result = dao.selectHolidayList(startDate, endDate,
					search, sort, desc, start, rows);

			return result;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving holidays", e);
			throw new LeaveException("Error retrieving holidays", e);
		}
	}

	// --- Employee Info

	/**
	 * Retrieves user service class and years of service
	 * 
	 * @param userId
	 * @return Map with keys "gender", "dateJoined", "dateResigned" and
	 *         "serviceClass"
	 * @throws DataObjectNotFoundException
	 * @throws LeaveException
	 */
	public Map viewEmployeeServiceInfo(String userId)
			throws DataObjectNotFoundException, LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			Map map = dao.selectEmployeeServiceInfo(userId);

			return map;
		} catch (DataObjectNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new LeaveException(
					"Unable to retrieve service info for user " + userId, e);
		}
	}

	public String[] viewEmployeeListForApprover(String userId)
			throws LeaveException {
		try {
			// retrieve employees for user
			LeaveDao dao = (LeaveDao) getDao();
			String[] userList = dao.selectEmployeeListForApprover(userId);

			// retrieve employees for department head?
			return userList;
		} catch (Exception e) {
			throw new LeaveException("Unable to retrieve employees for user "
					+ userId, e);
		}
	}

	public Collection viewEmployeeListForApprover(String userId,
			DaoQuery properties, int start, int maxResults, String sort,
			boolean descending) throws LeaveException {
		try {
			// retrieve employees for user
			LeaveDao dao = (LeaveDao) getDao();
			return dao.selectEmployeeListForApprover(userId, properties, start,
					maxResults, sort, descending);

		} catch (Exception e) {
			throw new LeaveException("Unable to retrieve employees for user "
					+ userId, e);
		}
	}

	public Collection viewEmployeeListForApproverWithAllHierarchy(
			String userId, String groupId, String queryString,
			DaoQuery properties, int start, int maxResults, String sort,
			boolean descending) throws LeaveException {
		try {
			// retrieve employees for user
			LeaveDao dao = (LeaveDao) getDao();
			// Collection result= dao.selectEmployeeListForApprover(userId,
			// properties, start, maxResults, sort, descending);

			// hardcode to 10 level first
			Collection result = new ArrayList();
			int hierarchyCount = 10;
			// hierarchy engine start
			SecurityService service = (SecurityService) Application
					.getInstance().getService(SecurityService.class);

			int counting = 0;
			Map hierarchyUsers = new SequencedHashMap();

			hierarchyUsers.put("" + 0 + " " + 0, new String[] { userId });

			counting = 1;

			String[] tempFirstElement = (String[]) hierarchyUsers.get("" + 0
					+ " " + 0);

			hierarchyUsers.put("" + counting + " " + 0,
					viewEmployeeListForApprover(tempFirstElement[0]));

			String[] tempTest = (String[]) hierarchyUsers.get("" + 1 + " " + 0);
			for (int i = 0; i < tempTest.length; i++) {


           
           
           try{
        	   service.getUser(tempTest[i]);
           }
           catch(Exception e){
        	          System.out.println(tempTest[i]);
        	   if(i< tempTest.length)
        		   i++;
        	   else 
        		   break;
           }

				if (groupId != null && !"".equals(groupId)
						&& !"-1".equals(groupId)) {

					if (service.isInGroup(tempTest[i], groupId)) {

						if (queryString != null && !"".equals(queryString)) {
							String firstName = (String) service.getUser(
									tempTest[i]).getProperty("firstName");

							String lastName = (String) service.getUser(
									tempTest[i]).getProperty("lastName");

							if (service.getUser(tempTest[i]).getUsername()
									.indexOf(queryString) >= 0
									|| firstName.indexOf(queryString) >= 0
									|| lastName.indexOf(queryString) >= 0)
								result.add(service.getUser(tempTest[i]));

						} else
							result.add(service.getUser(tempTest[i]));
					}

				} else {

					if (queryString != null && !"".equals(queryString)) {
						String firstName = (String) service
								.getUser(tempTest[i]).getProperty("firstName");
						String lastName = (String) service.getUser(tempTest[i])
								.getProperty("lastName");
						if (service.getUser(tempTest[i]).getUsername().indexOf(
								queryString) >= 0
								|| firstName.indexOf(queryString) >= 0
								|| lastName.indexOf(queryString) >= 0)
							result.add(service.getUser(tempTest[i]));

					} else
						result.add(service.getUser(tempTest[i]));
				}

				
			}

			if (hierarchyCount >= 2) {
				for (counting = 2; counting <= hierarchyCount; counting++) {
					try {

						String[] tempString = (String[]) hierarchyUsers.get(""
								+ (counting - 1) + " " + 0);
						for (int i = 0; i < tempString.length; i++) {

							hierarchyUsers.put("" + counting + " " + i,
									viewEmployeeListForApprover(tempString[i]));

							String[] tempdebug = (String[]) hierarchyUsers
									.get("" + counting + " " + i);
							for (int z = 0; z < tempdebug.length; z++) {

								if (groupId != null && !"".equals(groupId)
										&& !"-1".equals(groupId)) {

									if (service
											.isInGroup(tempdebug[z], groupId)) {

										if (queryString != null
												&& !"".equals(queryString)) {
											String firstName = (String) service
													.getUser(tempdebug[z])
													.getProperty("firstName");
											String lastName = (String) service
													.getUser(tempdebug[z])
													.getProperty("lastName");
											if (service.getUser(tempdebug[z])
													.getUsername().indexOf(
															queryString) >= 0
													|| firstName
															.indexOf(queryString) >= 0
													|| lastName
															.indexOf(queryString) >= 0)
												result.add(service
														.getUser(tempdebug[z]));

										} else
											result.add(service
													.getUser(tempdebug[z]));
									}

								} else {

									if (queryString != null
											&& !"".equals(queryString)) {
										String firstName = (String) service
												.getUser(tempdebug[z])
												.getProperty("firstName");
										String lastName = (String) service
												.getUser(tempdebug[z])
												.getProperty("lastName");
										if (service.getUser(tempdebug[z])
												.getUsername().indexOf(
														queryString) >= 0
												|| firstName
														.indexOf(queryString) >= 0
												|| lastName
														.indexOf(queryString) >= 0)
											result.add(service
													.getUser(tempdebug[z]));
									} else
										result.add(service
												.getUser(tempdebug[z]));

								}

							}

						}

					}// try
					catch (NullPointerException e) {

						// hierarchy is lesser than expected
						// this person might be top management and doesnt
						// required approval

					}

				}

			}

			// hierarchy engine end

			// }

			if (sort != null) {

				// do sorting
				// if(sort !=null){

				// if(sort.equalsIgnoreCase("username")){
				Collection afterSortResult = new ArrayList();
				// String [] sortString = new String[tempResult.size()];
				int countSort = 0;
				for (Iterator icount = result.iterator(); icount.hasNext();) {
					User tempUser = (User) icount.next();
					customUser tempUser2 = new customUser();

					tempUser2.setId(tempUser.getId());
					tempUser2.setUsername(tempUser.getUsername());
					tempUser2.setPropertyMap(tempUser.getPropertyMap());
					afterSortResult.add(tempUser2);
					// sortString[countSort] = tempUser.getUsername();
					countSort++;
				}

				if (descending) {
					if (sort.equalsIgnoreCase("lastName")) {

						// Collections.reverseOrder();
						Collections.sort((List) afterSortResult,
								customUser.LastNameComparator);
						Collections.reverse((List) afterSortResult);
					} else if (sort.equalsIgnoreCase("firstName")) {
						Collections.sort((List) afterSortResult,
								customUser.FistNameComparator);
						Collections.reverse((List) afterSortResult);
					} else
						Collections.sort((List) afterSortResult, Collections
								.reverseOrder());

				} else {
					if (sort.equalsIgnoreCase("lastName")) {
						Collections.sort((List) afterSortResult,
								customUser.LastNameComparator);
					} else if (sort.equalsIgnoreCase("firstName")) {
						Collections.sort((List) afterSortResult,
								customUser.FistNameComparator);
					} else
						Collections.sort((List) afterSortResult);
				}
				result.clear();

				for (Iterator icount = afterSortResult.iterator(); icount
						.hasNext();) {

					customUser tempUser = (customUser) icount.next();
					User tempUser2 = new User();
					tempUser2.setId(tempUser.getId());
					tempUser2.setUsername(tempUser.getUsername());
					tempUser2.setPropertyMap(tempUser.getPropertyMap());
					result.add(tempUser2);

				}
				// }

			}

			// trim according of number of pages required start
			// iterate and return what is necessary depend on table
			Collection tempResult = new ArrayList();
			int countingReturn = 0;

			int rangeWanted = start + maxResults;
			if (maxResults <= -1) {
				// get all for counting

				return result;
			}

			for (Iterator icount = result.iterator(); icount.hasNext();) {
				int tempValue = countingReturn;

				if (start <= countingReturn && (tempValue) < rangeWanted /*
																			 * &&
																			 * countingReturn <
																			 * rangeWanted
																			 */) {

					User tempUser = (User) icount.next();

					tempResult.add(tempUser);

				} else {

					icount.next();

					// break;

				}

				countingReturn++;

			}

			// trim according of number of pages required end

			return tempResult;

		} catch (Exception e) {
			throw new LeaveException("Unable to retrieve employees for user "
					+ userId, e);

		}

	}

	/**
	 * 
	 * @param userId
	 * @return Map of userID (String) -> User object
	 * @throws SecurityException
	 */
	public Map viewEmployeeMap(String[] userId) throws SecurityException {
		DaoQuery q = new DaoQuery();

		if ((userId != null) && (userId.length > 0)) {
			q
					.addProperty(new OperatorIn("id", userId,
							DaoOperator.OPERATOR_AND));
		}

		SecurityService security = (SecurityService) Application.getInstance()
				.getService(SecurityService.class);
		Collection userList = security.getUsers(q, 0, -1, "firstName", false);
		Map userMap = new HashMap();

		for (Iterator i = userList.iterator(); i.hasNext();) {
			User u = (User) i.next();
			userMap.put(u.getId(), u);
		}
		return userMap;
	}

	// --- misc

	/**
	 * Prepares date ranges by setting the appropriate time for both the start
	 * and end dates.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void prepareDates(Date startDate, Date endDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		startDate.setTime(start.getTime().getTime());

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		endDate.setTime(end.getTime().getTime());
	}

	/**
	 * Retrieves leave settings for the specified year
	 * 
	 * @param year
	 * @return
	 * @throws LeaveException
	 */
	public LeaveSettings viewLeaveSettings(int year) throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			LeaveSettings settings = dao.selectLeaveSettings(year);

			if ((settings == null) && (year != 0)) {
				settings = dao.selectLeaveSettings(DEFAULT_SETTINGS_YEAR);
			}
			return settings;
		} catch (Exception e) {
			throw new LeaveException("Unable to retrieve settings for year "
					+ year, e);
		}
	}

	public void updateLeaveSettings(LeaveSettings settings, int year)
			throws LeaveException {
		try {
			LeaveDao dao = (LeaveDao) getDao();
			dao.updateLeaveSettings(settings, year);
			dao.updateLeaveSettings(settings, DEFAULT_SETTINGS_YEAR);
		} catch (Exception e) {
			throw new LeaveException("Unable to update settings for year "
					+ year, e);
		}
	}

	// --- Utility Methods
	protected void utilSendNotification(String senderId, String receiverId,
			String ccId, String body, String subject, String url,
			String urlMessage) {
		try {
			Collection memoList;
			Collection emailList;
			Collection ccMemoList;
			Collection ccEmailList;
			memoList = new HashSet(1);
			emailList = new HashSet(1);
			ccMemoList = new HashSet();
			ccEmailList = new HashSet();

			// get settings
			LeaveSettings settings = viewLeaveSettings(DEFAULT_SETTINGS_YEAR);
			boolean memoAlert = settings.isNotifyMemo();
			boolean emailAlert = settings.isNotifyEmail();
			boolean ccAdmin = settings.isCcAdmin();

			if (!ccAdmin && (receiverId == null)) {
				// no one to send to, return
				return;
			}

			// get sender
			SecurityService security = (SecurityService) Application
					.getInstance().getService(SecurityService.class);
			MessagingModule mm = (MessagingModule) Application.getInstance()
					.getModule(MessagingModule.class);
			SmtpAccount smtpAccount = mm.getSmtpAccountByUserId(senderId);
			IntranetAccount intranetAccount = mm
					.getIntranetAccountByUserId(senderId);

			// send to receiver
			if ((receiverId != null) && !senderId.equals(receiverId)) {
				if (memoAlert) {
					intranetAccount = mm.getIntranetAccountByUserId(receiverId);

					if (intranetAccount != null) {
						String add = intranetAccount.getIntranetUsername()
								+ "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
						memoList.add(add);

						if ((ccId != null) && !ccId.equals("")) {
							intranetAccount = mm
									.getIntranetAccountByUserId(ccId);
							add = intranetAccount.getIntranetUsername() + "@"
									+ MessagingModule.INTRANET_EMAIL_DOMAIN;
							ccMemoList.add(add);
						}
					}
				}
				if (emailAlert) {
					User tempuser = utilGetUser(receiverId);
					String email = (String) tempuser.getProperty("email1");
					emailList.add(email);

					if ((ccId != null) && !ccId.equals("")) {
						tempuser = utilGetUser(ccId);
						email = (String) tempuser.getProperty("email1");
						ccEmailList.add(email);
					}
				}
			}

			// cc to admin
			if (ccAdmin) {
				if (memoAlert) {
					try {
						Collection cUsers = security.getUsersByPermission(
								PERMISSION_MANAGE_LEAVE, Boolean.TRUE, "id",
								false, 0, -1);

						for (Iterator i = cUsers.iterator(); i.hasNext();) {
							User adminUsers = (User) i.next();
							intranetAccount = mm
									.getIntranetAccountByUserId(adminUsers
											.getId());

							if (intranetAccount != null) {
								String add = intranetAccount
										.getIntranetUsername()
										+ "@"
										+ MessagingModule.INTRANET_EMAIL_DOMAIN;

								if (!memoList.contains(add)) {
									ccMemoList.add(add);
								}
							}
						}
					} catch (Exception e) {
						Log.getLog(getClass()).error(
								"Error sending cc memo to admin", e);
					}
				}
				if (emailAlert) {
					try {
						Collection cUsers = security.getUsersByPermission(
								PERMISSION_MANAGE_LEAVE, Boolean.TRUE, "id",
								false, 0, -1);

						for (Iterator i = cUsers.iterator(); i.hasNext();) {
							User adminUsers = (User) i.next();
							String email = (String) adminUsers
									.getProperty("email1");

							if (!emailList.contains(email)) {
								ccEmailList.add(email);
							}
						}
					} catch (Exception e) {
						Log.getLog(getClass()).error(
								"Error sending cc email to admin", e);
					}
				}
			}

			// construct message
			Message message = new Message();
			message.setMessageId(UuidGenerator.getInstance().getUuid());
			message.setSubject(subject);
			message.setMessageFormat(Message.MESSAGE_FORMAT_HTML);
			message.setDate(new Date());

			// set URL link
			if (url != null) {
				SetupModule setup = (SetupModule) Application.getInstance()
						.getModule(SetupModule.class);
				String siteUrl = setup.get("siteUrl");
				/*
				 * String link = "<a href=\"" + siteUrl + url + "\">" +
				 * urlMessage + "</a>";
				 */
				String link = "<a href=\"" + url + "\">" + urlMessage + "</a>";
				body += ("\n\n<p>" + link);
			}
			message.setBody(body);

			// send message
			if (memoAlert && (memoList.size() > 0)) {
				message.setToIntranetList(new ArrayList(memoList));
				message.setCcIntranetList(new ArrayList(ccMemoList));
				mm.sendMessage(smtpAccount, message, senderId, false);
			}
			if (emailAlert && (emailList.size() > 0)) {
				message.setToIntranetList(new ArrayList());
				message.setCcIntranetList(new ArrayList());
				message.setToList(new ArrayList(emailList));
				message.setCcList(new ArrayList(ccEmailList));
				mm.sendMessage(smtpAccount, message, senderId, false);
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error sending notification", e);
		}
	}

	public static User utilGetUser(String userId) throws SecurityException {
		SecurityService security = (SecurityService) Application.getInstance()
				.getService(kacang.services.security.SecurityService.class);

		return security.getUser(userId);
	}

	protected String utilGetApproverId(String userId) {
		try {
			String approverId = null;
			EmployeeModule em = (EmployeeModule) Application.getInstance()
					.getModule(EmployeeModule.class);
			Collection department = em.getEmployeeReportTo(userId);

			if (department.size() > 0) {
				DepartmentDataObject ddo = (DepartmentDataObject) department
						.iterator().next();
				approverId = ddo.getReportTo();
			}
			return approverId;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Error retrieving approver ID", e);

			return null;
		}
	}

	protected String utilFormatDate(Date date) {
		return sdf.format(date);
	}

	protected String utilCreateCalendarEvent(LeaveEntry entry) {
		try {
			String userId = entry.getUserId();
			String title = Application.getInstance().getMessage(
					"leave.label.onLeave", "On Leave");
			String description = Application.getInstance().getMessage(
					"leave.label.onLeaveDescription", "On Leave:")
					+ entry.getLeaveTypeName();
			String eventId = Appointment.class.getName() + "_"
					+ UuidGenerator.getInstance().getUuid();

			// create event object
			CalendarEvent event = new CalendarEvent();
			event.setEventId(eventId);
			event.setTitle(title);
			event.setDescription(description);
			event.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);

			// set dates
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(entry.getStartDate());

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(entry.getEndDate());

			if (!entry.isHalfDay()) {
				event.setAllDay(true);
				startCal.set(Calendar.HOUR_OF_DAY, 0);
				startCal.set(Calendar.MINUTE, 0);
				startCal.set(Calendar.SECOND, 0);
				endCal.set(Calendar.HOUR_OF_DAY, 23);
				endCal.set(Calendar.MINUTE, 59);
				endCal.set(Calendar.SECOND, 59);
			} else {
				event.setAllDay(false);
				startCal.set(Calendar.HOUR_OF_DAY, 8);
				startCal.set(Calendar.MINUTE, 0);
				startCal.set(Calendar.SECOND, 0);
				endCal.set(Calendar.HOUR_OF_DAY, 13);
				endCal.set(Calendar.MINUTE, 0);
				endCal.set(Calendar.SECOND, 0);
			}
			event.setStartDate(startCal.getTime());
			event.setEndDate(endCal.getTime());

			// set attendee
			User user = utilGetUser(userId);
			Attendee attendee = new Attendee();
			attendee.setEventId(eventId);
			attendee.setUserId(userId);
			attendee.setProperty("username", user.getUsername());
			attendee.setProperty("firstName", user.getProperty("firstName"));
			attendee.setProperty("lastName", user.getProperty("lastName"));
			attendee.setCompulsory(true);
			attendee.setStatus(CalendarModule.ATTENDEE_STATUS_CONFIRMED);

			Collection attendeeList = new ArrayList();
			attendeeList.add(attendee);
			event.setAttendees(attendeeList);

			// invoke handler
			Application application = Application.getInstance();
			CalendarModule handler = (CalendarModule) application
					.getModule(CalendarModule.class);
			handler.addCalendarEvent(Appointment.class.getName(), event,
					userId, true);

			return eventId;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Unable to create calendar event", e);

			return null;
		}
	}

	protected void utilDeleteCalendarEvent(LeaveEntry entry) {
		try {
			String userId = entry.getUserId();
			String eventId = entry.getEventId();

			if (eventId != null) {
				// invoke handler
				Application application = Application.getInstance();
				CalendarModule handler = (CalendarModule) application
						.getModule(CalendarModule.class);
				handler.deleteCalendarEvent(eventId, userId);
			}
		} catch (DataObjectNotFoundException e) {
			;
		} catch (Exception e) {
			Log.getLog(getClass()).error("Unable to delete calendar event", e);
		}
	}

	public boolean checkForAdmin(User user) throws SecurityException {
		SecurityService security = (SecurityService) Application.getInstance()
				.getService(SecurityService.class);
		boolean isAdmin = security
				.hasPermission(user.getId(), PERMISSION_MANAGE_LEAVE,
						"com.tms.cms.leave.ManageLeaves", null);

		return isAdmin;
	}

	public static LeaveCalculator getLeaveCalculator(int year)
			throws LeaveException {
		Application application = Application.getInstance();
		LeaveModule lm = (LeaveModule) application.getModule(LeaveModule.class);
		LeaveCalculator calculator = lm.getLeaveCalculator();
		LeaveSettings settings = lm.viewLeaveSettings(year);
		calculator.setSettings(settings);

		if (settings.isAllowShift()) {
			// retrieve employees
			Map employeeMap = new HashMap();

			try {
				EmployeeModule em = (EmployeeModule) Application.getInstance()
						.getModule(EmployeeModule.class);
				Collection list = em.getAllEmployees();

				for (Iterator i = list.iterator(); i.hasNext();) {
					EmployeeDataObject edo = (EmployeeDataObject) i.next();
					employeeMap.put(edo.getEmployeeID(), edo);
				}
			} catch (Exception e) {
				Log.getLog(LeaveModule.class).error(
						"Error retrieving employees for leave calculator", e);
			}
			calculator.setEmployeeMap(employeeMap);
		}
		return calculator;
	}

	/**
	 * Utility method to return the number of days between 2 dates.
	 * 
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static int utilDateDifference(Date endDate, Date startDate) {
		int result;
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(endDate);
		cal2.setTime(startDate);
		result = (int) ((cal1.getTime().getTime() - cal2.getTime().getTime()) / 86400000);

		return result + 1;
	}

	// --- Holidays
	protected void createHolidayCalendarEvent(SetupDataObject sdo, User user) {
		try {
			String userId = user.getId();
			String title = Application.getInstance().getMessage(
					"leave.label.holiday", "Holiday:")
					+ sdo.getDescription();
			String description = Application.getInstance().getMessage(
					"leave.label.holidayDescription", "Holiday:")
					+ sdo.getDescription();
			String eventId = CalendarEvent.class.getName() + "_"
					+ UuidGenerator.getInstance().getUuid();

			// create event object
			CalendarEvent event = new CalendarEvent();
			event.setEventId(eventId);
			event.setTitle(title);
			event.setDescription(description);
			event.setAllDay(true);
			event.setClassification(CalendarModule.CLASSIFICATION_PUBLIC);
			event.setUniversal(true);

			// set dates
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(sdo.getHolidayDate());
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			event.setStartDate(startCal.getTime());

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(sdo.getHolidayDate());
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 59);
			event.setEndDate(endCal.getTime());

			// invoke handler
			Application application = Application.getInstance();
			CalendarModule handler = (CalendarModule) application
					.getModule(CalendarModule.class);
			handler.addCalendarEvent(CalendarEvent.class.getName(), event,
					userId, true);
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Unable to create calendar event for holiday", e);
		}
	}
	
	
	
	
	
	
	
	public String returnEventId(Date date, String description){
		
		LeaveDao dao = (LeaveDao) getDao();
		
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(date);
		startCalendar.set(Calendar.HOUR, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND,0);
		
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(date);
		endCalendar.set(Calendar.HOUR, 23);
		endCalendar.set(Calendar.MINUTE, 59);
		endCalendar.set(Calendar.SECOND,59);
		
		
		try {
			return dao.returnEventId(startCalendar.getTime(), endCalendar.getTime(), description);
		} catch (DaoException e) {
			
			Log.getLog(getClass()).warn("problem retrieving eventId because old records doesnt have this feature supported", e);
		}
				
	
		
		return null;
	}
	
	
	
	
	protected boolean updateHolidayCalendarEvent(SetupDataObject sdo,SetupDataObject oldSetup, User user) {
		try {
			
			
			String eventId = returnEventId(oldSetup.getHolidayDate(), oldSetup.getDescription());
			if(eventId != null)
			{
		
			CalendarModule module = (CalendarModule) Application.getInstance().getModule(CalendarModule.class);
			// create event object
			CalendarEvent event = new CalendarEvent();
			event = module.getCalendarEvent(eventId);
			

			// set dates
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(sdo.getHolidayDate());
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			event.setStartDate(startCal.getTime());

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(sdo.getHolidayDate());
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
			endCal.set(Calendar.SECOND, 59);
			event.setEndDate(endCal.getTime());
			
			
			String description = Application.getInstance().getMessage(
					"leave.label.holidayDescription", "Holiday:")
					+ sdo.getDescription();
			
			event.setDescription(description);
			String title = Application.getInstance().getMessage(
					"leave.label.holiday", "Holiday:")
					+ sdo.getDescription();
            event.setTitle(title);
			
			// invoke handler
			Application application = Application.getInstance();
			CalendarModule handler = (CalendarModule) application
					.getModule(CalendarModule.class);
			handler.updateCalendarEvent( event,
					Application.getInstance().getCurrentUser().getId(), true);
			
			
			return true;
			}//if record is entered after new feature just added
			
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(
					"Unable to update calendar event for holiday", e);
		}
		
		return false;
		
	}

	public void setupHolidays(SetupDataObject setup, User user)
			throws EntitlementException, DuplicateKeyException, LeaveException {
		LeaveDao dao = (LeaveDao) getDao();

		try {
			if (dao.hasHoliday(setup) > 0) {
				throw new DuplicateKeyException("Holiday Already Set");
			}
			dao.insertSetupHolidays(setup);

			// create calendar event
			createHolidayCalendarEvent(setup, user);  
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}
	}

	public void updateHolidays(SetupDataObject setup, String date)
			throws EntitlementException, DuplicateKeyException, LeaveException {
		LeaveDao dao = (LeaveDao) getDao();

		try {
			if (dao.hasHoliday(setup) > 0) {
				throw new DuplicateKeyException("Holiday Already Set");
			}
			dao.updateSetupHolidays(setup, date);
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}
	}

	public boolean updateHolidays(SetupDataObject setup, SetupDataObject oldSetup)
			throws EntitlementException, DuplicateKeyException, LeaveException {
		LeaveDao dao = (LeaveDao) getDao();

		try {
			// if these lines doesnt commentted out , user will not be able to
			// change/update holiday
			/*
			 * if (dao.hasHoliday(setup) > 0) { throw new
			 * DuplicateKeyException("Holiday Already Set"); }
			 */

			dao.updateSetupHolidays(setup); 
			SecurityService ss= (SecurityService) Application.getInstance().getService(SecurityService.class);
			
			
			return updateHolidayCalendarEvent(setup,oldSetup, ss.getUser(setup.getUserID()));
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}
	}

	public int deleteHolidays(String date) throws LeaveException {
		LeaveDao dao = (LeaveDao) getDao();

		try {
						
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar startDate = Calendar.getInstance();
			Calendar endDate =  Calendar.getInstance();
			startDate.setTime(dateFormat.parse(date));
			startDate.set(Calendar.HOUR,0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			endDate.setTime(dateFormat.parse(date));
			endDate.set(Calendar.HOUR,23);
			endDate.set(Calendar.MINUTE, 59);
			endDate.set(Calendar.SECOND, 59);
			
			return dao.deleteSetupHolidays(date, startDate.getTime(), endDate.getTime());
			
			
			
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}
	}

	public Collection getPublicHoliday(String date) throws LeaveException {
		Collection holidays = new ArrayList();
		LeaveDao dao = (LeaveDao) getDao();

		try {
			holidays = dao.selectPublicHoliday(date);
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}
		return holidays;
	}

	public int getPublicHolidays(String year) throws LeaveException {
		int total = 0;
		LeaveDao dao = (LeaveDao) getDao();

		try {
			total = dao.selectPublicHolidays(year);
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}

		return total;
	}

	public Collection getPublicHolidays(String year, String sort, boolean desc,
			int start, int rows) throws LeaveException {
		Collection holidays = new ArrayList();
		LeaveDao dao = (LeaveDao) getDao();

		if (sort == null) {
			sort = "holidayDate";
		}

		try {
			holidays = dao.selectPublicHolidays(year, sort, desc, start, rows);
		} catch (Exception e) {
			throw new LeaveException(e.toString());
		}

		return holidays;
	}

	public Collection getLeaveType() {
		LeaveDao dao = (LeaveDao) getDao();
		Collection results = null;

		try {
			results = dao.selectLeaveTypeList();
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error loading leave type");
		}

		return results;
	}

	public Collection getLeave(String[] users, String startDate, String endDate)
			throws DaoException, DataObjectNotFoundException {
		LeaveDao dao = (LeaveDao) getDao();
		Collection results = null;

		results = dao.getLeave(users, startDate, endDate);

		return results;
	}

	public Collection getLeaveDistinct(String[] users, String startDate,
			String endDate) throws DaoException, DataObjectNotFoundException {
		LeaveDao dao = (LeaveDao) getDao();
		Collection results = null;

		results = dao.getLeaveDistinct(users, startDate, endDate);

		return results;
	}
}
