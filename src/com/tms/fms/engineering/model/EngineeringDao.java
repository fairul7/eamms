package com.tms.fms.engineering.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import kacang.util.UuidGenerator;
import com.tms.cms.core.model.InvalidKeyException;
import com.tms.crm.sales.misc.DateUtil;
import com.tms.fms.engineering.ui.ServiceAssignmentDetailsForm;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.facility.model.RateCard;
import com.tms.fms.setup.model.ProgramObject;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.ui.AbwEmailsSetup;
import com.tms.fms.util.DateDiffUtil;
import com.tms.fms.util.JobUtil;
import com.tms.hr.competency.Competency;


public class EngineeringDao extends DataSourceDao {
	public void init() throws DaoException{
		//start added new method for CR# 158
		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD prepareCheckOutBy VARCHAR(255) ", null);
		} catch (Exception e) {
		}
		//------------added cancellation field for calculate cancellation cost
		try {
			super.update("ALTER TABLE fms_eng_cancel_service_log ADD systemCalculatedCharges float(50) ", null);
		} catch (Exception e) {
		}
		//--------------------------------------------------------------------
		//------------added flag
		try {
			super.update("ALTER TABLE fms_eng_cancel_service_log ADD flagMainCharges VARCHAR(1) ", null);
		} catch (Exception e) {
		}
		//--------------------------------------------------------------------
		try {
			super.update("ALTER TABLE fms_eng_assignment_equipment ADD prepareCheckOutDate datetime ", null);
		} catch (Exception e) {
		}
		//end added new method for CR# 158


		try {
			String sql = "CREATE TABLE fms_sequence_code (type VARCHAR(1) NOT NULL, year int NOT NULL , sequence int NOT NULL )";
			super.update(sql, null);
		} catch (Exception e) {
		}

		// create primary key for fms_sequence_code
		try {
			super.update("ALTER TABLE fms_sequence_code ADD CONSTRAINT PK_fms_sequence_code PRIMARY KEY CLUSTERED (type, year)", null);
		} catch (Exception e) {
		}

		try {
			String sql = "CREATE TABLE fms_sequence_group_assignment_code (type VARCHAR(2) NOT NULL, year int NOT NULL , sequence int NOT NULL )";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try{
			super.update("sp_rename 'fms_request.createdDate', createdOn", null);
			super.update("sp_rename 'fms_request.modifiedDate', modifiedOn", null);
		} catch (Exception e) {}

		//create index for fms_sequence_group_assignment_code
		try {
			super
			.update(
					"CREATE INDEX idx_fms_sequence_group_assignment_code on fms_sequence_group_assignment_code (type, year)", null);
		} catch (Exception e) {
		}

		try{
			super.update("sp_rename 'fms_request', fms_eng_request", null);
			super.update("sp_rename 'fms_services', fms_eng_services", null);
		} catch (Exception e) {}

		try{
			super.update("CREATE TABLE fms_eng_services(serviceId varchar(255) NOT NULL PRIMARY KEY, title varchar(255) NOT NULL)", null);

		} catch (Exception e) {
			Log.getLog(getClass()).error(e);

		}
		try{
			super.update("INSERT INTO fms_eng_services values ('1','fms.facility.msg.service.1')", null);
			super.update("INSERT INTO fms_eng_services values ('2','fms.facility.msg.service.2')", null);
			super.update("INSERT INTO fms_eng_services values ('3','fms.facility.msg.service.3')", null);
			super.update("INSERT INTO fms_eng_services values ('4','fms.facility.msg.service.4')", null);
			super.update("INSERT INTO fms_eng_services values ('5','fms.facility.msg.service.5')", null);
			super.update("INSERT INTO fms_eng_services values ('6','fms.facility.msg.service.6')", null);
			super.update("INSERT INTO fms_eng_services values ('7','fms.facility.msg.service.7')", null);
		}catch(Exception e){
			Log.getLog(getClass()).error(e);
		}

		try{
			super.update(
					"CREATE TABLE fms_eng_request(" +
					"requestId varchar(255) NOT NULL PRIMARY KEY, " +
					"title varchar(255) NOT NULL, " +
					"requestType char(1) NOT NULL, " +
					"clientName varchar(255) NULL, " +
					"programType char(1) NOT NULL, " +
					"program varchar(255) NULL, " +
					"description text NULL, " +
					"requiredFrom datetime NULL, " +
					"requiredTo datetime NULL, " +
					"createdBy varchar(255) NULL, " +
					"createdOn datetime NULL, " +
					"modifiedBy varchar(255) NULL, " +
					"modifiedOn datetime NULL, " +
					"submittedDate datetime NULL, " +
					"status char(1) NOT NULL, " +
					"state char(1) NULL, " +
					"approvedBy varchar(255) NULL, " +
					"approvedDate datetime NULL, " +
					"approverRemarks text NULL, " +
					"cancellationCharges float(50) NULL, " +
					"lateCharges float(50) NULL, " +
					"fcId varchar(255) NULL, " +
					"suggestion TEXT NULL, " +
					"totalInternalRate float(50) NULL, " +
					"totalExternalRate float(50) NULL" +
					")", null);
		} catch (Exception e) {}

		try{
			super.update("ALTER TABLE fms_eng_request ADD submittedDate datetime ", null);
		}catch (Exception e) {
		}

		try{
			super.update("ALTER TABLE fms_eng_request ADD status char(1) NOT NULL", null);
		}catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_eng_request ADD state char(1)", null);
		}catch (Exception e) {
		}

		try{
			super.update("ALTER TABLE fms_eng_request ADD approvedBy varchar(255) ", null);
		}catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_eng_request ADD approvedDate datetime ", null);
		}catch (Exception e) {
		}
		try{
			super.update("ALTER TABLE fms_eng_request ADD approverRemarks text ", null);
		}catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_request ADD cancellationCharges float(50)", null);
		} catch (Exception e) {			
		}

		try {
			super.update("ALTER TABLE fms_eng_request ADD lateCharges float(50)", null);
		} catch (Exception e) {			
		}

		try{
			super.update("ALTER TABLE fms_eng_request ADD fcId varchar(255) ", null);
		}catch (Exception e) {
		}

		try{
			super.update("ALTER TABLE fms_eng_request ADD suggestion TEXT ", null);
		}catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_request ADD totalInternalRate float(50)", null);
		} catch (Exception e) {			
		}

		try {
			super.update("ALTER TABLE fms_eng_request ADD totalExternalRate float(50)", null);
		} catch (Exception e) {			
		}

		try{
			super.update("ALTER TABLE fms_eng_request ADD checkoutFlag char(1) ", null);
		}catch (Exception e) {
		}
		
		try{
			super.update("ALTER TABLE fms_eng_request ADD cancellationChargeManpower float(50) NULL ", null);
		}catch (Exception e) {
		}
		

		// create index for fms_eng_request(status)
		try {
			super.update("CREATE INDEX status ON fms_eng_request(status)", null);
		} catch (Exception e) {
		}

		// create index for fms_eng_request(requiredFrom, requiredTo)
		try {
			String sql = "CREATE INDEX requiredFrom_requiredTo ON fms_eng_request(requiredFrom, requiredTo)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try{
			super.update("CREATE TABLE fms_eng_request_services(requestId varchar(255) NOT NULL, serviceId " +
					"varchar(255) NOT NULL, PRIMARY KEY( requestId, serviceId))", null);
		} catch (Exception e) {
		}

		try{
			super.update("ALTER TABLE fms_eng_request_services ADD feedType char(1) ", null);
		}catch (Exception e) {
		}

		try{
			super.update("CREATE TABLE fms_eng_request_status(requestId varchar(255) NOT NULL, " +
					" status char(1) NOT NULL, remarks text NULL, " +
					" additionalInfo varchar(255) NULL, createdBy varchar(255) NULL, " +
					" createdDate datetime NULL, PRIMARY KEY( requestId, status))", null);
		} catch (Exception e) {
		}
		try{
			super.update("CREATE TABLE fms_eng_service_postproduction(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, facilityId varchar(255) NOT NULL, requiredDate datetime," +
					"fromTime varchar(6) , toTime varchar(6), createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD requiredDateTo datetime", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_postproduction(requestId)", null);
		} catch (Exception e){
		}
		
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_postproduction(facilityId, requiredDateTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_scp(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, facilityId varchar(255) NOT NULL, " +
					"requiredFrom datetime,requiredTo datetime,departureTime varchar(6),location varchar(255),segment varchar(255)," +
					"settingFrom varchar(6),settingTo varchar(6), rehearsalFrom varchar(6),rehearsalTo varchar(6), " +
					"recordingFrom varchar(6), recordingTo varchar(6), createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_scp ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_scp ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_scp ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("ALTER TABLE fms_eng_service_scp ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_scp(requestId)", null);
		} catch (Exception e){
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_scp(facilityId, requiredTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_vtr(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, service varchar(255) NOT NULL, " +
					"requiredDate datetime,requiredFrom varchar(6),requiredTo varchar(6), formatFrom char(1),formatTo char(1)," +
					"conversionFrom char(1),conversionTo char(1), duration int, noOfCopies int, " +
					"remarks text, createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD facilityId varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD requiredDateTo datetime", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ALTER COLUMN duration VARCHAR(255)", null);
		} catch (Exception e) {
		}
		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_vtr(requestId)", null);
		} catch (Exception e){
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_vtr(facilityId, requiredDateTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_other(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, facilityId varchar(255) NOT NULL, " +
					"quantity int, requiredFrom datetime,requiredTo datetime, fromTime varchar(6), toTime varchar(6), " +
					"remarks varchar(255), createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_other ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_other ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}
		try {
			super.update("ALTER TABLE fms_eng_service_other ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("ALTER TABLE fms_eng_service_other ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_other(requestId)", null);
		} catch (Exception e){
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_other(facilityId, requiredTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_manpower(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, competencyId varchar(255) NOT NULL, " +
					"quantity int, requiredFrom datetime,requiredTo datetime, fromTime varchar(6), toTime varchar(6), " +
					"remarks varchar(255), createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}
		try {
			super.update("ALTER TABLE fms_eng_service_manpower ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_manpower ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_manpower ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("ALTER TABLE fms_eng_service_manpower ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_manpower(requestId)", null);
		} catch (Exception e){
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_manpower(competencyId, requiredTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_studio(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, facilityId varchar(255) NOT NULL, " +
					"bookingDate datetime,segment varchar(255),requiredFrom varchar(6),requiredTo varchar(6)," +
					"settingFrom varchar(6),settingTo varchar(6), rehearsalFrom varchar(6),rehearsalTo varchar(6), " +
					"vtrFrom varchar(6), vtrTo varchar(6), createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD bookingDateTo datetime", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_studio(requestId)", null);
		} catch (Exception e){
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_studio(facilityId, bookingDateTo)", null);
		} catch (Exception e){
		}
		
		try{
			super.update("CREATE TABLE fms_eng_service_tvro(id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, serviceId varchar(255) NOT NULL, feedTitle varchar(255) NOT NULL, " +
					"location varchar(255), requiredDate datetime, fromTime varchar(6), toTime varchar(6), timezone char(1), " +
					"remarks varchar(255), totalTimeReq int, timeMeasure char(1), feedType int, createdBy varchar(255) NULL, createdDate datetime" +
					" NULL, modifiedBy varchar(255) NULL, modifiedDate datetime NULL)", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD internalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD externalRate numeric(18,2)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD facilityId varchar(255)", null);
		} catch (Exception e){
		}
		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD requiredDateTo datetime", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD blockBooking CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("ALTER TABLE fms_eng_service_tvro ADD submitted CHAR(1)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX requestId ON fms_eng_service_tvro(requestId)", null);
		} catch (Exception e){			
		}
		try {
			super.update("CREATE INDEX IX_rateCard_requiredTo ON fms_eng_service_tvro(facilityId, requiredDateTo)", null);
		} catch (Exception e){			
		}
		
		try {
			super.update("ALTER TABLE competency ADD unitId VARCHAR(255) NULL ", null);
		} catch(Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_eng_service_other ADD location varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_manpower ADD location varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_postproduction ADD location varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_vtr ADD location varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("ALTER TABLE fms_eng_service_studio ADD location varchar(255)", null);
		} catch (Exception e){
		}

		try {
			super.update("CREATE TABLE fms_eng_request_notification ( " +
					"id varchar(255) NOT NULL PRIMARY KEY, " +
					"requestId varchar(255) NOT NULL, " +
					"notificationDate datetime)", null);
		} catch (Exception e) {

		}

		try {
			super.update("ALTER TABLE fms_eng_request_notification ADD type CHAR(1) NULL ", null);
		} catch(Exception e) {
		}
		
		// create index for fms_eng_request_notification(requestId)
		try {
			String sql = "CREATE INDEX requestId ON fms_eng_request_notification(requestId)";
			super.update(sql, null);
		} catch (Exception e) {
		}

		try {
			super.update("CREATE TABLE fms_req_feedback ( " +
					"id VARCHAR(255) NOT NULL PRIMARY KEY, " +
					"requestId VARCHAR(255) NOT NULL, " +
					"serviceType CHAR(1), " +
					"question VARCHAR(255), " +
					"answer VARCHAR(255))", null);
		} catch (Exception e) {

		}

		try {
			super.update("CREATE TABLE fms_eng_request_unit (" +
					"id VARCHAR(255) NOT NULL PRIMARY KEY, " +
					"requestId VARCHAR(255) , " +
					"unitId VARCHAR(255))", null);
		} catch (Exception e) {
		}        

		try {
			super.update("ALTER TABLE fms_eng_request_unit ADD serviceId VARCHAR(255)", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_request_unit ADD rateCardId VARCHAR(255)", null);
		} catch (Exception e){			
		}
		
		// create index for fms_eng_request_unit(requestId)
		try {
			super.update("CREATE INDEX requestId ON fms_eng_request_unit(requestId)", null);
		} catch (Exception e){			
		}
		
		// create index for fms_eng_request_unit(rateCardId)
		try {
			super.update("CREATE INDEX rateCardId ON fms_eng_request_unit(rateCardId)", null);
		} catch (Exception e){			
		}

		try {
			super.update("ALTER TABLE fms_eng_assignment_setting ADD scheduledDate DATETIME", null);
		} catch (Exception e){			
		}

		// fms_global_assignment
		try {
			super.update("CREATE TABLE fms_global_assignment (" +
					"id VARCHAR(255) NOT NULL PRIMARY KEY, " +
					"refreshRate INT , " +
					"noOfDays INT, " +
					"footerMessage text null," +
					"createdBy varchar(255)," +
					"createdDate datetime, " +
					"modifiedBy varchar(255), " +
					"modifiedDate datetime)", null);
		} catch (Exception e){
		}

		// fms_cancel item in the request log
		try {
			String sql = 
				"CREATE TABLE fms_eng_cancel_service_log (" +
				"assignmentId VARCHAR(255) NOT NULL, " +
				"code VARCHAR(255) NOT NULL, " +
				"requestId VARCHAR(255) NOT NULL, " +
				"serviceType CHAR(1) NOT NULL, " +
				"assignmentType CHAR(1), " +
				"rateCardId VARCHAR(255), " +
				"competencyId VARCHAR(255), " +
				"rateCardCategoryId VARCHAR (255), " +
				"requiredFrom DATETIME, " +
				"requiredTo DATETIME, " +
				"fromTime VARCHAR(6), " +
				"toTime VARCHAR(6), " +
				"userId VARCHAR(255), " +
				"barcode VARCHAR(255), " +
				"checkedOutBy VARCHAR(255), " +
				"checkedOutDate DATETIME, " +
				"checkedInBy VARCHAR(255), " +
				"checkedInDate DATETIME, " +
				"status CHAR(1), " +
				"createdBy VARCHAR(255), " +
				"createdDate DATETIME, " +
				"completionDate DATETIME, " +
				"remarks TEXT, " +
				"reasonUnfulfilled TEXT, " +
				"chargeBack CHAR(1), " +
				"callBack CHAR(1), " +
				"modifiedBy VARCHAR(255), " +
				"modifiedDate DATETIME, " +
				"takenBy VARCHAR(255), " +
				"preparedBy VARCHAR(255), " +
				"assignmentLocation VARCHAR(255), " +
				"groupId VARCHAR(255), " +
				"damage CHAR(1), " +
				"prepareCheckOutBy VARCHAR(255), " +
				"prepareCheckOutDate DATETIME, " +
				"cancelBy VARCHAR(255), " +
				"cancelDate DATETIME, " +
				"PRIMARY KEY (assignmentId))";
			super.update(sql, null);
		} catch (Exception e){
		}
		
		try {
			super.update("CREATE INDEX requestId ON fms_eng_cancel_service_log(requestId) ", null);
		} catch (Exception e){			
		}
		
		try {
			super.update("ALTER TABLE fms_eng_request_services ADD cancelFlag CHAR(1)", null);
		} catch (Exception e){			
		}

		try{
			String sql = 
				"CREATE TABLE fms_eng_assignment_setting " +
				"(settingId VARCHAR(255) NOT NULL PRIMARY KEY, " +
				"settingValue INT)";
			super.update(sql, null);
		}catch (Exception e){
		}

		try{
			String sql = 
				"CREATE TABLE fms_eng_auto_assignment_setting " +
				"(settingId VARCHAR(255) NOT NULL PRIMARY KEY, " +
				"settingValue INT, " +
				"scheduledDate DATETIME)";
			super.update(sql, null);
		}catch (Exception e){
		}
		
		try{super.update("CREATE TABLE fms_global_setup (id varchar(255) NOT NULL PRIMARY KEY, " +
				" taskId varchar(10) NOT NULL, " +
				" scheduleTime1 varchar(6) NULL," +
				" scheduleTime2 varchar(6) NULL," +
				" scheduleTime3 varchar(6) NULL," +
				" createdBy varchar(255) NULL, createdDate datetime NULL, " +
				" modifiedBy  varchar(255) NULL, modifiedDate datetime NULL )", null);
        } catch (Exception e) {}
        
        try{
			String sql = 
				"ALTER TABLE fms_prog_setup " +
				" ADD noOfEpisodes INT NULL , " +
				" duration varchar(20) NULL ";
			super.update(sql, null);
		}catch (Exception e){			
		}

		//		try {
		//			super.update(" update security_user set email1 = 'xx'+email1 ", null);
		//		} catch (Exception e){			
		//		} 

		//JobUtil.scheduleMinuteTask("PendingRequestNotificationTask", "Notification", "Sending email to alternate approver", 
		//		new PendingRequestNotificationTask(), Integer.parseInt(Application.getInstance().getMessage("fms.notification.intervalTaskInMinute")));
        
        try 
        {
			super.update(" ALTER TABLE fms_global_setup ADD email1 VARCHAR(255) ", null);
			super.update(" ALTER TABLE fms_global_setup ADD email2 VARCHAR(255) ", null);
			super.update(" ALTER TABLE fms_global_setup ADD email3 VARCHAR(255) ", null);
			super.update(" ALTER TABLE fms_global_setup ADD email4 VARCHAR(255) ", null);
			super.update(" ALTER TABLE fms_global_setup ADD email5 VARCHAR(255) ", null);
		} 
        catch (Exception e){}

		JobUtil.scheduleDailyTask("PendingRequestNotificationTask", "Notification", "Sending email to alternate approver", 
				new PendingRequestNotificationTask(), 20, 00);
	}

	public int getSequence(String type, String year) throws DaoException {
		Collection col = super.select("SELECT sequence,year,type FROM fms_sequence_code where type=? AND year=? ",HashMap.class, new String[] { type, year}, 0, -1);
		if (col.size() == 0) {
			return 0;
		} else {
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				try {
					HashMap map = (HashMap) iterator.next();
					int i = (Integer) map.get("sequence");
					return i;
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public int getSequenceGroupAssignment(String type, String year) throws DaoException {
		Collection col = super.select("SELECT sequence,year,type FROM fms_sequence_group_assignment_code" +
				" where type=? AND year=? ",HashMap.class, new String[] { type, year}, 0, -1);
		if (col.size() == 0) {
			return 0;
		} else {
			for (Iterator iterator = col.iterator(); iterator.hasNext();) {
				try {
					HashMap map = (HashMap) iterator.next();
					int i = (Integer) map.get("sequence");
					return i;
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	protected void insertStatus(Status s) throws DaoException {
		super.update("INSERT INTO fms_eng_request_status (requestId,status,remarks,additionalInfo,createdBy,createdDate) VALUES (#requestId#,#status#,#remarks#,#additionalInfo#,#createdBy#,#createdDate#)",s);
	}

	protected void updateStatusPartial(Status s) throws DaoException {
		super.update("UPDATE fms_eng_request_status SET createdBy = #createdBy#, createdDate = #createdDate# WHERE requestId = #requestId# AND status = #status#",s);
	}
	
	protected void updateStatusFull(Status s) throws DaoException {
		super.update(
				"UPDATE fms_eng_request_status " +
				"SET " +
					"createdBy = #createdBy#, " +
					"createdDate = #createdDate#, " +
					"remarks = #remarks#, " +
					"additionalInfo = #additionalInfo# " +
				"WHERE requestId = #requestId# " +
				"AND status = #status#", s);
	}
	
	public void updateServiceStatus(String serviceType, String serviceId) throws DaoException {
		String serviceName = "";
		if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceType)){
			serviceName = "fms_eng_service_scp";
		}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceType)){
			serviceName = "fms_eng_service_postproduction";
		}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceType)){
			serviceName = "fms_eng_service_other";
		}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceType)){
			serviceName = "fms_eng_service_studio";
		}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceType)){
			serviceName = "fms_eng_service_manpower";
		}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceType)){
			serviceName = "fms_eng_service_tvro";
		}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceType)){
			serviceName = "fms_eng_service_vtr";
		}
		super.update("UPDATE "+serviceName+" SET submitted = '1' where id='"+serviceId+"'",null);
	}

	public void updateServiceByRequestId(String serviceType, String requestId) throws DaoException {
		String serviceName = "";
		if(ServiceDetailsForm.SERVICE_SCPMCP.equals(serviceType)){
			serviceName = "fms_eng_service_scp";
		}else if(ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(serviceType)){
			serviceName = "fms_eng_service_postproduction";
		}else if(ServiceDetailsForm.SERVICE_OTHER.equals(serviceType)){
			serviceName = "fms_eng_service_other";
		}else if(ServiceDetailsForm.SERVICE_STUDIO.equals(serviceType)){
			serviceName = "fms_eng_service_studio";
		}else if(ServiceDetailsForm.SERVICE_MANPOWER.equals(serviceType)){
			serviceName = "fms_eng_service_manpower";
		}else if(ServiceDetailsForm.SERVICE_TVRO.equals(serviceType)){
			serviceName = "fms_eng_service_tvro";
		}else if(ServiceDetailsForm.SERVICE_VTR.equals(serviceType)){
			serviceName = "fms_eng_service_vtr";
		}
		super.update("UPDATE "+serviceName+" SET submitted = '0' where requestId='"+requestId+"'",null);
	}
	
	public boolean searchModifyStatus(String requestId)throws DaoException{
		String sql="Select rs.requestId,rs.status,rs.remarks,rs.additionalInfo,(u.firstName + ' ' + u.lastName) AS createdBy,rs.createdDate " +
		"from fms_eng_request_status rs " +
		"LEFT JOIN security_user u ON (rs.createdBy = u.username) " +
		"where requestId=? and rs.status = 'M'";
		Collection col = super.select(sql, Status.class, new String[]{requestId}, 0, 1);
		if (col!= null && col.size()==1){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isPendingModification(String requestId) throws DaoException {
		String sql="Select rs.requestId,rs.status,rs.remarks,rs.additionalInfo,(u.firstName + ' ' + u.lastName) AS createdBy,rs.createdDate " +
		"from fms_eng_request_status rs " +
		"LEFT JOIN security_user u ON (rs.createdBy = u.username) " +
		"where requestId=? and rs.status = 'M'";
		Collection col = super.select(sql, Status.class, new String[]{requestId}, 0, 1);
		if (col!= null && col.size()==1) {
			Status status = (Status) col.iterator().next();
			String additionalInfo = status.getAdditionalInfo();
			
			if (additionalInfo != null && additionalInfo.length() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public Collection selectStatusTrail(String requestId) throws DaoException{
		String sql="Select rs.requestId,rs.status,rs.remarks,rs.additionalInfo,(u.firstName + ' ' + u.lastName) AS createdBy,rs.createdDate " +
		"from fms_eng_request_status rs " +
		"LEFT JOIN security_user u ON (rs.createdBy = u.username) " +
		"where requestId=? order By createdDate desc";
		return super.select(sql, Status.class, new String[]{requestId}, 0, -1);
	}
	public void insertSequence(Sequence s) throws DaoException {
		super.update("INSERT INTO fms_sequence_code (sequence,year,type) VALUES (#sequence#,#year#,#type#)",s);
	}

	public void updateSequence(Sequence s) throws DaoException {
		super.update("UPDATE fms_sequence_code SET sequence=#sequence# WHERE type=#type# AND year=#year# ",s);
	}

	public void insertSequenceGroupAssignment(Sequence s) throws DaoException {
		super.update("INSERT INTO fms_sequence_group_assignment_code (sequence,year,type) VALUES (#sequence#,#year#,#type#)",s);
	}

	public void updateSequenceGroupAssignment(Sequence s) throws DaoException {
		super.update("UPDATE fms_sequence_group_assignment_code SET sequence=#sequence# WHERE type=#type# AND year=#year# ",s);
	}

	protected Collection selectServices() throws DaoException{
		String sql="Select serviceId,title from fms_eng_services order By serviceId";
		return super.select(sql, Service.class, null, 0, -1);
	}

	protected void insertRequestService(Service service) throws DaoException{
		String sql="INSERT INTO fms_eng_request_services (requestId, serviceId, feedType, cancelFlag ) VALUES ( #requestId#,#serviceId#, #feedType#, #cancelFlag# ) ";
		super.update(sql,service);
	}

	public void updateRequestService(Service service) throws DaoException {
		super.update("UPDATE fms_eng_request_services SET feedType=#feedType# WHERE requestId=#requestId# AND serviceId=#serviceId# ",service);
	}

	protected int deleteRequestService(String requestId) throws DaoException{
		Service service=new Service();
		service.setRequestId(requestId);
		String sql="DELETE from fms_eng_request_services WHERE requestId=#requestId# ";
		return super.update(sql,service);
	}

	protected void insertRequest(EngineeringRequest request) throws DaoException{
		String sql="INSERT INTO fms_eng_request (requestId,title,requestType, clientName, programType,program,description,requiredFrom,requiredTo," +
		"createdBy, createdOn, modifiedBy, modifiedOn,status,state) VALUES (#requestId#,#title#,#requestType#, #clientName#, #programType#,#program#,#description#,#requiredFrom#,#requiredTo#," +
		"#createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#,#status#,#state# ) ";
		super.update(sql,request);
	}
	protected void updateRequest(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set title=#title#, requestType=#requestType#, clientName=#clientName#," +
		"programType=#programType#, program=#program#, description=#description#, requiredFrom=#requiredFrom#,requiredTo=#requiredTo#," +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#,state=#state# where requestId=#requestId#";
		super.update(sql,request);
	}

	protected void submitRequest(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set submittedDate=#submittedDate#, status=#status#," +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where requestId=#requestId#";
		super.update(sql,request);
	}

	protected void updateTotalAmountRequest(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set totalInternalRate=#totalInternalRate#, totalExternalRate=#totalExternalRate# " +
		" where requestId=#requestId#";
		super.update(sql,request);
	}

	protected void updateRequestStatus(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set status=#status#," +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where requestId=#requestId#";
		super.update(sql,request);
	}

	protected void closeRequest(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set status=#status#, suggestion = #remarks#, " +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where requestId=#requestId#";
		super.update(sql,request);
	}

	protected void approveRequest(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set status=#status#,approvedBy=#approvedBy#,approvedDate=#approvedDate#,approverRemarks=#approverRemarks#," +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#, state=#state# where requestId=#requestId#";
		super.update(sql,request);
	}
	protected void assignFC(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set status=#status#,fcId=#fcId#, " +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn# where requestId=#requestId#";
		super.update(sql,request);
	}
	protected Collection selectRequestServices(String requestId) throws DaoException{
		String sql="Select s.serviceId,s.title,rs.requestId,rs.feedType, rs.cancelFlag from fms_eng_services s" +
		" Inner join fms_eng_request_services rs on s.serviceId=rs.serviceId where rs.requestId=? order By s.serviceId";
		return super.select(sql, Service.class, new String[]{requestId}, 0, -1);
	}

	public EngineeringRequest selectRequest(String requestId) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType,r.program,r.description,r.requiredFrom," +
		" r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,p.programName,r.submittedDate, " +
		" r.approvedBy, r.approvedDate, r.approverRemarks, " +
		//" r.cancellationCharges, " +
		" r.cancellationCharges + r.cancellationChargeManpower as cancellationCharges, " +
		" r.lateCharges, r.totalInternalRate, r.totalExternalRate, checkoutFlag,p.pfecode  " +
		" FROM fms_eng_request r " +
		" LEFT JOIN fms_prog_setup p on r.program=p.programId " +
		" where r.requestId=?";
		Collection col=super.select(sql, EngineeringRequest.class, new String[]{requestId}, 0, 1);
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;
	}

	protected Collection selectManpowerAssignmentId(String requestId){
		try {
			String sql =
				"SELECT * " +
				"FROM fms_eng_assignment " +
				"WHERE requestId = ? ";

			return super.select(sql, EngineeringRequest.class, new String[] {requestId}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected EngineeringRequest selectManpowerStatus(String assignmentId){
		try {
			String sql =
				"SELECT * " +
				"FROM fms_eng_assignment_manpower " +
				"WHERE assignmentId = ? ";

			Collection col = super.select(sql, EngineeringRequest.class, new String[] {assignmentId}, 0, 1);
			if (col!= null && col.size()==1){
				return (EngineeringRequest) col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	public String getFullName(String username, String userId){
		String sql = "";
		ArrayList params = new ArrayList();

		if (userId != null && !"".equals(userId)){
			sql = "SELECT firstName + ' ' + lastName AS fullName FROM security_user " +
			"WHERE id = ? ";
			params.add(userId);
		} else {
			sql = "SELECT firstName + ' ' + lastName AS fullName FROM security_user " +
			"WHERE username = ? ";
			params.add(username);
		}

		String name="";
		try{
			Collection col=super.select(sql, EngineeringRequest.class, params.toArray(), 0, 1);
			if(col!=null && col.size()>0){
				EngineeringRequest er = (EngineeringRequest)col.iterator().next();
				name= er.getFullName();
			}
		}catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return name;
	}

	public String getFullName(String username)  {
		return getFullName(username, "");
	}

	public EngineeringRequest selectRequestWOProgram(String requestId) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType,r.program,r.description,r.requiredFrom," +
		" r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r " +
		//" INNER JOIN fms_prog_setup p on r.program=p.programId " +
		" where r.requestId=?";
		Collection col=super.select(sql, EngineeringRequest.class, new String[]{requestId}, 0, 1);
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;
	}

	//For current user (Requestor)
	public Collection selectRequest(String search, String requestType, String status, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="Select requestId,title,requestType, clientName, programType,program,description,requiredFrom,requiredTo,createdBy, createdOn, modifiedBy, modifiedOn, status, state,submittedDate" +
		" FROM fms_eng_request r where 1=1 AND r.createdBy=?";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title LIKE '%"+search+"%' OR r.description LIKE '%"+search+"%' ) ";
		}
		if(requestType!=null && !"".equals(requestType) && !"-1".equals(requestType)){
			sql+=" AND ( r.requestType= '"+requestType+"' ) ";
		}
		if(status!=null && !"".equals(status) && !"-1".equals(status)){
			sql+=" AND ( r.status= '"+status+"' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			/*if(sort.equals("workingHours")){
				sort="startTime";
			}*/
			if (sort.equals("requestIdWithLink")){
				sort = "requestId";
			}

			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}

		return super.select(sql, EngineeringRequest.class, new String[]{Application.getInstance().getCurrentUser().getUsername()}, start, rows);
	}

	public int selectRequestCount(String search, String requestType, String status) throws DaoException{
		String sql="Select count(*) As COUNT " +
		" FROM fms_eng_request r where 1=1 AND r.createdBy=? ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title LIKE '%"+search+"%' OR r.description LIKE '%"+search+"%' ) ";
		}
		if(requestType!=null && !"".equals(requestType) && !"-1".equals(requestType)){
			sql+=" AND ( r.requestType= '"+requestType+"' ) ";
		}
		if(status!=null && !"".equals(status) && !"-1".equals(status)){
			sql+=" AND ( r.status= '"+status+"' ) ";
		}
		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{Application.getInstance().getCurrentUser().getUsername()}, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	// 
	public Collection selectHODRequest() throws DaoException {
		String sql = "Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		" r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		" where ( r.status= '"+EngineeringModule.PENDING_STATUS+"' ) AND d.status='1' " +
		" group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, " +
		" r.requiredFrom,r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
		" r.status, r.state,r.submittedDate ";

		return super.select(sql, EngineeringRequest.class, null, 0, -1);
	}

	public Collection selectHOURequest() throws DaoException {
		//		String sql = "Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		//					" r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		//					" FROM fms_eng_request r INNER JOIN security_user u on " +
		//					" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		//					" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		//					" where ( r.status= '"+EngineeringModule.ASSIGNMENT_STATUS +"' ) AND d.status='1' " +
		//					" group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, " +
		//					" r.requiredFrom,r.requiredTo, r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, " +
		//					" r.status, r.state,r.submittedDate ";
		String sql = "Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, r.requiredFrom, " +
		"r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		"FROM fms_eng_request r INNER JOIN security_user u on r.createdBy=u.username " +
		"INNER JOIN fms_unit d on u.unit=d.id " +
		"WHERE d.status='1' AND ( r.status= '" + EngineeringModule.ASSIGNMENT_STATUS + "') " +
		"group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo, " +
		"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate";

		return super.select(sql, EngineeringRequest.class, null, 0, -1);
	}

	public Collection selectNotification(String requestId, String type) throws DaoException {
		String sql = "SELECT id, requestId, notificationDate " +
		"FROM fms_eng_request_notification " +
		"WHERE requestId =? AND type=? ";

		return super.select(sql, HashMap.class, new String[] {requestId, type}, 0, -1);
	}

	public void insertNotification(String requestId, String notificationId, Date notificationDate, String type) throws DaoException {
		String sql = "INSERT INTO fms_eng_request_notification (id, requestId, notificationDate, type) " +
		"VALUES (?, ?, ?, ?) ";
		ArrayList params = new ArrayList();
		params.add(notificationId);
		params.add(requestId);		
		params.add(notificationDate);
		params.add(type);

		super.update(sql, params.toArray());
	}

	//	For HOD
	public Collection selectHODRequest(String search, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		"r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		" where (d.HOD=? OR a.userId=?) AND ( r.status= '"+EngineeringModule.PENDING_STATUS+"' ) AND d.status='1' ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title LIKE '%"+search+"%' OR r.description LIKE '%"+search+"%' ) ";
		}
		sql+="group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo," +
		"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate, u.firstName ";
		if(sort!=null && !"".equals(sort)){
			String table = "r";
			if (sort.equals("requestIdWithLink")){
				sort = "requestId";
			}
			if (sort.equals("createdUserName")){
				sort = "firstName";
				table = "u";
			}
			sql+=" order by "+table+"."+sort+" ";
		}else{
			sql+=" order by r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, EngineeringRequest.class, new String[]{Application.getInstance().getCurrentUser().getId(),Application.getInstance().getCurrentUser().getId()}, start, rows);
	}

	public int selectHODRequestCount(String search) throws DaoException{
		String sql="Select count(distinct r.requestId) AS COUNT " +
		" FROM fms_eng_request r INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		" where (d.HOD=? OR a.userId=?) AND ( r.status= '"+EngineeringModule.PENDING_STATUS+"' ) AND d.status='1' ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		//sql+="group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo," +
		//"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate";

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{Application.getInstance().getCurrentUser().getId(),Application.getInstance().getCurrentUser().getId()}, 0, -1);
			if(col!=null){
				if (col.size()>0){
					HashMap map=(HashMap)col.iterator().next();
					count=(Integer)map.get("COUNT");
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	public Collection selectAllHODRequest(String search, String status, String sort, boolean desc,int start,int rows) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		"r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		" where (d.HOD=? OR a.userId=?) AND d.status='1' ";

		if(status!=null && !"-1".equals(status)){
			sql += " AND ( r.status= '"+ status +"' ) "; 
		} else {
			sql += " AND ( " +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "'" +
			") ";
		}

		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		sql+="group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo," +
		"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate, u.firstName ";
		if(sort!=null && !"".equals(sort)){
			String table = "r";
			if (sort.equals("requestIdWithLink")){
				sort = "requestId";
			}
			if (sort.equals("createdUserName")){
				sort = "firstName";
				table = "u";
			}
			sql+=" order by "+table+"."+sort+" ";
		}else{
			sql+=" order by r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, EngineeringRequest.class, new String[]{Application.getInstance().getCurrentUser().getId(),Application.getInstance().getCurrentUser().getId()}, start, rows);
	}

	public int selectAllHODRequestCount(String search, String status) throws DaoException{
		String sql="Select count(distinct r.requestId) AS COUNT " +
		" FROM fms_eng_request r INNER JOIN security_user u on " +
		" r.createdBy=u.username INNER JOIN fms_department d on u.department=d.id " +
		" LEFT JOIN fms_department_alternate_approver a on a.departmentId=d.id" +
		" where (d.HOD=? OR a.userId=?) AND d.status='1' ";

		if(status!=null && !"-1".equals(status)){
			sql += " AND ( r.status= '"+ status +"' ) "; 
		} else {
			sql += " AND ( " +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "'" +
			") ";
		}

		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}

		//sql+="group By r.requestId ,r.title,r.requestType, r.clientName, r.programType,program, r.requiredFrom,r.requiredTo," +
		//	"r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate";

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{Application.getInstance().getCurrentUser().getId(),Application.getInstance().getCurrentUser().getId()}, 0, -1);
			if(col!=null){			
				//count=col.size();
				if (col.size()>0){
					HashMap map=(HashMap)col.iterator().next();
					count=(Integer)map.get("COUNT");
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	//	For FCHead
	public Collection selectFCHeadRequest(Date requiredFrom, Date requiredTo, String departmentId, String search, String sort,boolean desc,int start,int rows) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		"r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate " +
		" FROM fms_eng_request r  " +
		" INNER JOIN security_user u ON (r.createdBy = u.username) " +
		" INNER JOIN fms_department d ON (u.department = d.id) " +
		" WHERE 1=1 AND ( r.status= '"+EngineeringModule.PROCESS_STATUS+"' ) ";
		if(search!=null && !"".equals(search)){
			sql+=" AND (  r.requestId LIKE '%"+search+"%' OR r.title LIKE '%"+search+"%' OR r.description LIKE '%"+search+"%' ) ";
		}

		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}

		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}

		if(sort!=null && !"".equals(sort)){
			String table = "r";
			if (sort.equals("requestIdWithLink")){
				sort = "requestId";
			}
			if (sort.equals("createdUserName")){
				sort = "createdBy";
			}
			sql+=" order by "+table+"."+sort+" ";
		}else{
			sql+=" order by r.modifiedOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}

	public int selectFCHeadRequestCount(Date requiredFrom, Date requiredTo, String departmentId, String search) throws DaoException{
		ArrayList args = new ArrayList();
		String sql="Select count(r.requestId) AS COUNT FROM fms_eng_request r  " +
		" INNER JOIN security_user u ON (r.createdBy = u.username) " +
		" INNER JOIN fms_department d ON (u.department = d.id) " +
		" WHERE 1=1 AND ( r.status= '"+EngineeringModule.PROCESS_STATUS+"' ) ";
		if(search!=null && !"".equals(search)){
			sql+=" AND (  r.requestId LIKE '%"+search+"%' OR r.title LIKE '%"+search+"%' OR r.description LIKE '%"+search+"%' ) ";
		}

		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}

		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	public Collection getFCHeadAllRequestListByBarcode( String barcode, int top ) throws DaoException {
		String topRec = (top >= 0? "TOP " + top : "" ) + " ";

		String list = "SELECT " + topRec + "b.barcode, a.code AS assignmentCode, a.assignmentId, "
		+ "r.requestId as requestId, r.title, b.checkedOutBy, b.checkedOutDate, b.checkedInBy, b.checkedInDate, b.takenBy, b.createdDate " +
		"FROM fms_eng_assignment a " + 
		"INNER JOIN fms_eng_assignment_equipment b ON ( a.assignmentId = b.assignmentId ) " +
		"INNER JOIN fms_eng_request r ON ( r.requestId = a.requestId ) " +
		//"WHERE b.barcode = ? ORDER BY ISNULL( b.checkedInDate, b.createdDate ) DESC";
		"WHERE b.barcode = ? ORDER BY b.checkedOutDate DESC";

		return super.select(list, EngineeringRequest.class, new String[] { barcode }, 0, -1 );
	}
	
	public Collection getFCHeadRequestListByBarcode( String barcode) throws DaoException {
		
		Collection top5 = new ArrayList();
		
		String sql = "SELECT  distinct r.requestId as requestId, b.barcode, "+
			"r.title, b.checkedOutBy, b.checkedOutDate, b.checkedInBy, b.checkedInDate, b.takenBy, b.createdDate "+
			"FROM fms_eng_assignment_equipment b "+
			"LEFT JOIN fms_eng_assignment a on a.groupId=b.groupId "+
			"INNER JOIN fms_eng_request r ON ( r.requestId = a.requestId )  "+
			"WHERE b.barcode = ? ORDER BY b.checkedOutDate DESC";

		int i = 0;
		Collection list = super.select(sql, EngineeringRequest.class, new String[] { barcode }, 0, -1 );
		
		for(Iterator it = list.iterator(); it.hasNext();){
			EngineeringRequest engine = (EngineeringRequest) it.next();
			top5.add(engine);
			i++;
			if(i == 5)
				break;
		}
		return top5;
	}
	
	public Collection getInternalCheckoutListByBarcode(String barcode)throws DaoException {
		ArrayList params = new ArrayList();
		
		String sql="SELECT c.id, c.checkout_date, c.checkout_by, c.checkin_date, c.checkin_by, c.barcode, c.purpose, c.status, " +
				" (u2.firstName + ' ' + u2.lastName)as requestedByName, c.createdby_date, c.updatedby, c.updatedby_date, c.location, c.groupId, c.takenBy, " +
				"(u.firstName + ' ' + u.lastName) as checkout_by, f.name as name" +
				", loc.name AS location_name, (u1.firstName + ' ' + u1.lastName) as checkin_by_name " +
				" from fms_facility_item_checkout c INNER JOIN fms_facility_item i on c.barcode=i.barcode " +
				"LEFT JOIN fms_facility_location loc ON (i.location_id = loc.setup_id) " +
				"INNER JOIN fms_facility f on i.facility_id=f.id " +
				"LEFT JOIN security_user u on c.checkout_by=u.id " +
				"LEFT JOIN security_user u1 on c.checkin_by=u1.id " +
				"LEFT JOIN security_user u2 on c.requestedBy=u2.id " +
				"WHERE c.barcode=?  ";
		params.add(barcode);		
		
		sql += "order by c.checkout_date desc ";
		return super.select(sql, FacilityObject.class, params.toArray(),0,-1);
	}

	public Collection selectFCHeadAllRequest(Date requiredFrom, Date requiredTo, String departmentId, String search, String status, String sort, boolean desc, int start, int rows) throws DaoException{
		String sql="Select r.requestId,r.title,r.requestType, r.clientName, r.programType, program, " +
		"r.requiredFrom,r.requiredTo,r.createdBy, r.createdOn, r.modifiedBy, r.modifiedOn, r.status, r.state,r.submittedDate, " +
		"r.fcId, (u.firstName + ' ' + u.lastName) AS fullName " +
		//" FROM fms_eng_request r INNER JOIN security_user u ON (r.fcId = u.id) " +
		" FROM fms_eng_request r INNER JOIN security_user u ON (r.createdBy = u.username) " +
		" INNER JOIN fms_department d ON (u.department = d.id) where 1=1 ";
		if (status!=null && !"-1".equals(status)){	
			sql += " AND ( r.status= '"+ status +"' ) ";
		} else {
			sql += " AND ( " +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' " +
			"AND r.status <> '" + EngineeringModule.CANCELLED_STATUS + "' " +
			") ";
		}
		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}
		ArrayList args = new ArrayList();
		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}

		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}

		if(sort!=null && !"".equals(sort)){
			String table = "r";
			if (sort.equals("requestIdWithLink")){
				sort = "requestId";
			}
			if (sort.equals("createdUserName")){
				sort = "firstName";
				table = "u";
			}
			sql+=" order by "+table+"."+sort+" ";
		}else{
			sql+=" order by r.createdOn DESC ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);
	}

	public int selectFCHeadAllRequestCount(Date requiredFrom, Date requiredTo, String departmentId, String search, String status) throws DaoException{
		String sql="Select count(*) AS COUNT " +
		" FROM fms_eng_request r INNER JOIN security_user u ON (r.fcId = u.id) " +
		" INNER JOIN fms_department d ON (u.department = d.id) where 1=1 ";
		if (status!=null && !"-1".equals(status)){	
			sql += " AND ( r.status= '"+ status +"' ) ";
		} else {
			sql += " AND ( " +
			"r.status <> '" + EngineeringModule.DRAFT_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PENDING_STATUS + "' AND " +
			"r.status <> '" + EngineeringModule.PROCESS_STATUS + "' " +
			"AND r.status <> '" + EngineeringModule.CANCELLED_STATUS + "' " +
			") ";
		}

		if(search!=null && !"".equals(search)){
			sql+=" AND ( r.requestId LIKE '%"+search+"%' OR r.title like '%"+search+"%' OR r.description like '%"+search+"%' ) ";
		}

		ArrayList args = new ArrayList();
		if(requiredFrom != null && requiredTo != null){
			sql += " AND ((r.requiredFrom >= ? AND r.requiredFrom <= ?) OR (r.requiredFrom <= ? AND r.requiredTo >= ?))";
			args.add(requiredFrom);
			args.add(requiredTo);
			args.add(requiredFrom);
			args.add(requiredFrom);
		}

		if(departmentId!=null && !"".equals(departmentId) && !"-1".equals(departmentId)){
			sql+=" AND ( d.id = '"+departmentId+"' ) ";
		}

		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	public void insertPostProductionService(PostProductionService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_postproduction " +
		"(id,requestId,serviceId,facilityId,requiredDate,requiredDateTo,fromTime,toTime,internalRate,externalRate," +
		"blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate,location) " +
		"VALUES " +
		"(#id#,#requestId#,#serviceId#,#facilityId#,#requiredDate#,#requiredDateTo#,#fromTime#,#toTime#,#internalRate#,#externalRate#," +
		"#blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#,#location#)";
		super.update(sql,service);
	}

	protected void updatePostProductionService(PostProductionService service)  throws DaoException{
		String sql="Update fms_eng_service_postproduction set facilityId=#facilityId#, requiredDate=#requiredDate#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " +
		"requiredDateTo=#requiredDateTo#," +
		"blockBooking=#blockBooking#, submitted=#submitted#, " +
		"fromTime=#fromTime#," +
		"toTime=#toTime#, modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#, location=#location# where id=#id#";
		super.update(sql,service);
	}

	public Collection selectPostProductionService(String requestId, String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.requiredDate,p.fromTime," +
		"p.toTime,p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility," +
		"p.internalRate, p.externalRate, p.blockBooking, p.submitted, p.requiredDateTo, p.location " +
		" from fms_eng_service_postproduction p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, PostProductionService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectPostProductionServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId," +
		"p.fromTime, p.toTime, f.name as facility," +
		"p.internalRate, p.externalRate, p.blockBooking, p.location " +
		" from fms_eng_service_postproduction p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, PostProductionService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	protected boolean searchPostProductionService(String requestId) {
		try {
			String sql = 
				"SELECT id " +
				"FROM fms_eng_service_postproduction " +
				"WHERE requestId = ? ";

			Collection col = super.select(sql, PostProductionService.class, new String[]{requestId}, 0, -1);
			if (col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}


	public Collection selectPostProductionService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.requiredDate, p.requiredDateTo,p.fromTime,p.blockBooking, " +
		"p.toTime,p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.location,f.name as facility from fms_eng_service_postproduction  p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, PostProductionService.class, new String[]{id}, 0, -1);
	}

	public Collection selectRateCardFacility(String search,String serviceId,String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="select rc.id, rc.name, rc.remarksRequest " +
		" from fms_rate_card rc " +
		" where rc.status!='0' AND rc.serviceTypeId=? ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( rc.name like '%"+search+"%' OR rc.description like '%"+search+"%' OR rc.remarksRequest like '%"+search+"%' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			/*if(sort.equals("workingHours")){
				sort="startTime";
			}*/
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by rc.name ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, HashMap.class, new String[]{serviceId}, start, rows);
	}
	public Collection selectTiedRateCardStudio(String search, String sort,boolean desc,int start,int rows) throws DaoException{
		String sql=" select rc.id, rc.name, rc.remarksRequest " +
		" from fms_rate_card rc " +
		" where rc.status!='0' AND rc.serviceTypeId='5' ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( rc.name like '%"+search+"%' OR rc.description like '%"+search+"%' OR rc.remarksRequest like '%"+search+"%' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			/*if(sort.equals("workingHours")){
				sort="startTime";
			}*/
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by rc.name ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, HashMap.class, null, start, rows);
	}
	public int selectRateCardFacilityCount(String search,String serviceId) throws DaoException{
		String sql="select Count(*) AS COUNT " +
		" from fms_rate_card rc " +
		" where rc.status!='0' AND rc.serviceTypeId=? ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( rc.name like '%"+search+"%' OR rc.description like '%"+search+"%' OR rc.remarksRequest like '%"+search+"%' ) ";
		}
		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{serviceId}, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	public Collection selectRateCardManpower(String search,String serviceId,String sort,boolean desc,int start,int rows) throws DaoException{
		String sql="select c.competencyId, c.competencyName,c.competencyType,c.competencyDescription " +
		"from fms_rate_card rc INNER JOIN fms_rate_card_manpower rcm on rc.id=rcm.rateCardId" +
		" INNER JOIN competency c on c.competencyId=rcm.manpower where 1=1 ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( c.competencyName like '%"+search+"%' OR c.competencyType like '%"+search+"%'  OR c.competencyDescription like '%"+search+"%' ) ";
		}
		if(sort!=null && !"".equals(sort)){
			sql+=" order by "+sort+" ";
		}else{
			sql+=" order by c.competencyName ";
		}
		if(desc){
			sql+=" DESC ";
		}
		return super.select(sql, Competency.class, new String[]{}, start, rows);
	}

	public int selectRateCardManpowerCount(String search,String serviceId) throws DaoException{
		String sql="select Count(*) AS COUNT " +
		"from fms_rate_card rc INNER JOIN fms_rate_card_manpower rcm on rc.id=rcm.rateCardId" +
		" INNER JOIN competency c on c.competencyId=rcm.manpower where 1=1 ";
		if(search!=null && !"".equals(search)){
			sql+=" AND ( c.competencyName like '%"+search+"%' OR c.competencyType like '%"+search+"%'  OR c.competencyDescription like '%"+search+"%' ) ";
		}
		int count=0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{}, 0, 1);
			if(col!=null){
				HashMap map=(HashMap)col.iterator().next();
				count=(Integer)map.get("COUNT");
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return count;
	}

	public void insertScpService(ScpService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_scp " +
		"(id,requestId,serviceId,facilityId,requiredFrom,requiredTo,departureTime,location,segment," +
		"settingFrom,settingTo,rehearsalFrom,rehearsalTo,recordingFrom,recordingTo," +
		"internalRate, externalRate," +
		"blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate) " +
		"VALUES " +
		"(#id#,#requestId#,#serviceId#,#facilityId#,#requiredFrom#,#requiredTo#,#departureTime#,#location#,#segment#," +
		"#settingFrom#,#settingTo#,#rehearsalFrom#,#rehearsalTo#,#recordingFrom#,#recordingTo#," +
		"#internalRate#, #externalRate#, " +
		"#blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#)";
		super.update(sql,service);
	}

	protected void updateScpService(ScpService service)  throws DaoException{
		String sql="Update fms_eng_service_scp set facilityId=#facilityId#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " +
		"requiredFrom=#requiredFrom#, requiredTo=#requiredTo#,departureTime=#departureTime#,location=#location#," +
		"segment=#segment#,settingFrom=#settingFrom#,settingTo=#settingTo#,rehearsalFrom=#rehearsalFrom#,rehearsalTo=#rehearsalTo#, " +
		"recordingFrom=#recordingFrom#,recordingTo=#recordingTo#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#," +
		"blockBooking=#blockBooking#, submitted=#submitted# " +
		"where id=#id#";
		super.update(sql,service);
	}


	public Collection selectScpService(String requestId,String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId," +
		"p.requiredFrom,p.requiredTo,p.departureTime,p.location,p.segment," +
		"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility," +
		"p.internalRate, p.externalRate, p.blockBooking, p.submitted  " +
		"from fms_eng_service_scp p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, ScpService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectScpServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql=
			"Select p.id,p.requestId,p.serviceId,p.facilityId," +
			"p.departureTime,p.location,p.segment," +
			"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo," +
			"f.name as facility," +
			"p.internalRate, p.externalRate, p.blockBooking  " +
			"from fms_eng_service_scp p " +
			" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
			"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, ScpService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectScpService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId," +
		"p.requiredFrom,p.requiredTo,p.departureTime,p.location,p.segment," +
		"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility," +
		"p.blockBooking " +
		"from fms_eng_service_scp p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, ScpService.class, new String[]{id}, 0, -1);
	}

	protected boolean searchScpService(String requestId){
		try {
			String sql = 
				"Select id FROM fms_eng_service_scp where requestId = ?";

			Collection col = super.select(sql, ScpService.class, new String[] {requestId}, 0, -1);

			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return false;
	}

	public void insertOtherService(OtherService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_other " +
		"(id,requestId,serviceId,facilityId,quantity,requiredFrom,requiredTo,remarks,fromTime,toTime," +
		"internalRate, externalRate, blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate,location) VALUES " +
		"(#id#,#requestId#,#serviceId#,#facilityId#,#quantity#,#requiredFrom#,#requiredTo#,#remarks#,#fromTime#,#toTime#," +
		"#internalRate#, #externalRate#, #blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#,#location#)";
		super.update(sql,service);
	}

	protected void updateOtherService(OtherService service)  throws DaoException{
		String sql="Update fms_eng_service_other set facilityId=#facilityId#,quantity=#quantity#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " + 
		"requiredFrom=#requiredFrom#, requiredTo=#requiredTo#,remarks=#remarks#,fromTime=#fromTime#," +
		"toTime=#toTime#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#," +
		"blockBooking=#blockBooking#, location=#location#, submitted=#submitted# " +
		"where id=#id#";
		super.update(sql,service);
	}

	public Collection selectOtherService(String requestId, String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.quantity," +
		"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility," +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location, p.submitted " +
		"from fms_eng_service_other p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, OtherService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectOtherServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.quantity," +
		"f.name as facility,p.fromTime,p.toTime," +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location " +
		"from fms_eng_service_other p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, OtherService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	protected boolean searchOtherServiceByRequestId(String requestId){
		try {
			String sql = 
				"select id " +
				"from fms_eng_service_other " +
				"where requestId =?";

			Collection col = super.select(sql, OtherService.class, new String[]{requestId}, 0, -1);

			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}

	public Collection selectOtherService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.quantity," +
		"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
		"p.blockBooking, p.location, " +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility " +
		"from fms_eng_service_other p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, OtherService.class, new String[]{id}, 0, -1);
	}

	public void insertStudioService(StudioService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_studio " +
		"(id,requestId,serviceId,facilityId,bookingDate, bookingDateTo, segment,requiredFrom,requiredTo," +
		"settingFrom,settingTo,rehearsalFrom,rehearsalTo,vtrFrom,vtrTo," +
		"internalRate, externalRate, blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate,location) VALUES " +
		"(#id#,#requestId#,#serviceId#,#facilityId#,#bookingDate#,#bookingDateTo#,#segment#,#requiredFrom#,#requiredTo#," +
		"#settingFrom#,#settingTo#,#rehearsalFrom#,#rehearsalTo#,#vtrFrom#,#vtrTo#," +
		"#internalRate#,#externalRate#, #blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#,#location#)";
		super.update(sql,service);
	}

	protected void updateStudioService(StudioService service)  throws DaoException{
		String sql="Update fms_eng_service_studio set facilityId=#facilityId#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " +
		"bookingDate=#bookingDate#,bookingDateTo=#bookingDateTo#,segment=#segment#,requiredFrom=#requiredFrom#, " +
		"requiredTo=#requiredTo#," +
		"settingFrom=#settingFrom#,settingTo=#settingTo#,rehearsalFrom=#rehearsalFrom#,rehearsalTo=#rehearsalTo#, " +
		"vtrFrom=#vtrFrom#,vtrTo=#vtrTo#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#," +
		"blockBooking=#blockBooking#, location=#location#, submitted=#submitted# " +
		"where id=#id#";
		super.update(sql,service);
	}

	public Collection selectStudioService(String requestId,String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate,p.bookingDateTo," +
		"p.requiredFrom,p.requiredTo,p.segment," +
		"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility," +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location,p.submitted " +
		"from fms_eng_service_studio p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, StudioService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectStudioServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate,p.bookingDateTo," +
		"p.requiredFrom,p.requiredTo,p.segment,f.name as facility," +
		"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo," +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location " +
		"from fms_eng_service_studio p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, StudioService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public boolean searchStudioServiceByRequestId(String requestId){
		try {
			String sql = 
				"select id " +
				"from fms_eng_service_studio " +
				"where requestId = ?";

			Collection col = super.select(sql, StudioService.class, new String[]{requestId}, 0, -1);
			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}

	public Collection selectStudioService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate,p.bookingDateTo," +
		"p.requiredFrom,p.requiredTo,p.segment," +
		"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.location,f.name as facility," +
		"p.blockBooking, p.location " +
		"from fms_eng_service_studio p " +
		" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, StudioService.class, new String[]{id}, 0, -1);
	}

	public void insertManpowerService(ManpowerService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_manpower " +
		"(id,requestId,serviceId,competencyId,quantity,requiredFrom,requiredTo,remarks,fromTime,toTime," +
		"internalRate, externalRate, blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate,location) VALUES " +
		"(#id#,#requestId#,#serviceId#,#competencyId#,#quantity#,#requiredFrom#,#requiredTo#,#remarks#,#fromTime#,#toTime#," +
		"#internalRate#, #externalRate#, #blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#,#location#)";
		super.update(sql,service);
	}

	protected void updateManpowerService(ManpowerService service)  throws DaoException{
		String sql="Update fms_eng_service_manpower set competencyId=#competencyId#,quantity=#quantity#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " +
		"requiredFrom=#requiredFrom#, requiredTo=#requiredTo#,remarks=#remarks#,fromTime=#fromTime#," +
		"toTime=#toTime#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#," +
		"blockBooking=#blockBooking#, location=#location#, submitted=#submitted# " +
		" where id=#id#";
		super.update(sql,service);
	}

	public Collection selectManpowerService(String requestId,String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.competencyId,p.quantity," +
		"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as competencyName," +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location,p.submitted " +
		"from fms_eng_service_manpower p " +
		joinType + " JOIN fms_rate_card f on f.id=p.competencyId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, ManpowerService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectManpowerServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.competencyId,p.quantity," +
		"f.name as competencyName,p.fromTime,p.toTime, " +
		"p.internalRate, p.externalRate," +
		"p.blockBooking, p.location " +
		"from fms_eng_service_manpower p " +
		" INNER JOIN fms_rate_card f on f.id=p.competencyId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, ManpowerService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	protected boolean searchManpowerByRequestId(String requestId){
		try {
			String sql = 
				"select id " +
				"from fms_eng_service_manpower " +
				"where requestId = ?";

			Collection col = super.select(sql, ManpowerService.class, new String[]{requestId}, 0, -1);
			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}

	public Collection selectManpowerService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.competencyId,p.quantity," +
		"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as competencyName," +
		"p.blockBooking, p.location " +
		"from fms_eng_service_manpower p " +
		" INNER JOIN fms_rate_card f on f.id=p.competencyId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, ManpowerService.class, new String[]{id}, 0, -1);
	}

	public void insertTvroService(TvroService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_tvro " +
		"(id,requestId,serviceId,feedTitle,location,requiredDate,requiredDateTo,remarks,fromTime,toTime," +
		"internalRate, externalRate,facilityId, blockBooking, submitted, " +
		"timezone,totalTimeReq,timeMeasure,createdBy,createdDate,modifiedBy,modifiedDate) VALUES " +
		"(#id#,#requestId#,#serviceId#,#feedTitle#,#location#,#requiredDate#,#requiredDateTo#,#remarks#,#fromTime#,#toTime#," +
		"#internalRate#, #externalRate#, #facilityId#, #blockBooking#, #submitted#, " +
		"#timezone#,#totalTimeReq#,#timeMeasure#,#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#)";
		super.update(sql,service);
	}


	protected void updateTvroService(TvroService service)  throws DaoException{
		String sql="Update fms_eng_service_tvro set feedTitle=#feedTitle#,location=#location#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, " +
		"requiredDate=#requiredDate#, requiredDateTo=#requiredDateTo#, timezone=#timezone#, totalTimeReq=#totalTimeReq#,timeMeasure=#timeMeasure#," +
		"remarks=#remarks#,fromTime=#fromTime#," +
		"facilityId=#facilityId#, blockBooking=#blockBooking#, submitted=#submitted#, " +
		"toTime=#toTime#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate# where id=#id#";
		super.update(sql,service);
	}

	public Collection selectTvroService(String requestId, String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.feedTitle,p.location," +
		"p.requiredDate,p.requiredDateTo,p.timezone,p.totalTimeReq,p.timeMeasure,p.remarks,p.fromTime,p.toTime," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.feedType,f.name as facility," +
		"p.internalRate, p.externalRate," +
		"p.facilityId, p.blockBooking, p.submitted  " +
		"from fms_eng_service_tvro p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, TvroService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectTvroServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.feedTitle,p.location," +
		"p.totalTimeReq,p.timeMeasure,p.feedType,p.fromTime,p.toTime, " +
		"p.internalRate, p.externalRate," +
		"p.facilityId, p.blockBooking " +
		"from fms_eng_service_tvro p " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, TvroService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	protected boolean searchTvroServiceByRequestId(String requestId){
		try {
			String sql = 
				"select id " +
				"from fms_eng_service_tvro " +
				"where requestId = ?";

			Collection col = super.select(sql, TvroService.class, new String[]{requestId}, 0, -1);
			if (col!= null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}

	public Collection selectTvroService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.feedTitle,p.location," +
		"p.requiredDate,p.requiredDateTo,p.timezone,p.totalTimeReq,p.timeMeasure,p.remarks,p.fromTime,p.toTime," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.feedType," +
		"p.facilityId, f.name as facility,p.blockBooking "+
		"from fms_eng_service_tvro p " +
		"LEFT JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, TvroService.class, new String[]{id}, 0, -1);
	}

	public void insertVtrService(VtrService service) throws DaoException {
		String sql="INSERT INTO fms_eng_service_vtr " +
		"(id,requestId,serviceId,service,requiredDate,requiredFrom,requiredTo,formatFrom,formatTo," +
		"conversionFrom,conversionTo,duration,noOfCopies,remarks," +
		"internalRate, externalRate,facilityId, " +
		"requiredDateTo, blockBooking, submitted, " +
		"createdBy,createdDate,modifiedBy,modifiedDate, location) VALUES " +
		"(#id#,#requestId#,#serviceId#,#service#,#requiredDate#,#requiredFrom#,#requiredTo#,#formatFrom#,#formatTo#," +
		"#conversionFrom#,#conversionTo#,#duration#,#noOfCopies#,#remarks#," +
		"#internalRate#, #externalRate#, #facilityId#, " +
		"#requiredDateTo#, #blockBooking#, #submitted#, " +
		"#createdBy#,#createdDate#,#modifiedBy#,#modifiedDate#, #location#)";
		super.update(sql,service);
	}

	protected void updateVtrService(VtrService service)  throws DaoException{
		String sql="Update fms_eng_service_vtr set service=#service#, " +
		"requiredDate=#requiredDate#, requiredFrom=#requiredFrom#,requiredTo=#requiredTo#,formatFrom=#formatFrom#," +
		"formatTo=#formatTo#,conversionFrom=#conversionFrom#,conversionTo=#conversionTo#,duration=#duration#,noOfCopies=#noOfCopies#, " +
		"remarks=#remarks#,modifiedBy=#modifiedBy#, modifiedDate=#modifiedDate#, " +
		"internalRate=#internalRate#, externalRate=#externalRate#, facilityId=#facilityId#," +
		"requiredDateTo=#requiredDateTo#, blockBooking=#blockBooking#, location=#location#, submitted=#submitted# " +
		"where id=#id#";
		super.update(sql,service);
	}

	public Collection selectVtrService(String requestId, String serviceId, boolean includeInvalidRateCard) throws DaoException {
		String joinType = "INNER";
		if (includeInvalidRateCard) {
			joinType = "LEFT OUTER";
		}
		
		String sql="Select p.id,p.requestId,p.serviceId,p.service," +
		"p.requiredDate,p.requiredDateTo,p.requiredFrom,p.requiredTo,p.formatFrom,p.formatTo," +
		"p.conversionFrom,p.conversionTo,p.duration,p.noOfCopies,p.remarks," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate," +
		"p.facilityId, f.name as facility," +
		"p.internalRate, p.externalRate, p.blockBooking, p.location, p.submitted " +
		"from fms_eng_service_vtr p " +
		joinType + " JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, VtrService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	public Collection selectVtrServiceForTemplate(String requestId,String serviceId) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.service,p.conversionFrom,p.conversionTo, " +
		"p.requiredFrom,p.requiredTo, p.duration, p.noOfCopies,p.formatFrom,p.formatTo, " +
		"p.facilityId, f.name as facility," +
		"p.internalRate, p.externalRate, p.blockBooking, p.location " +
		"from fms_eng_service_vtr p " +
		"LEFT JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.requestId=? AND serviceId=? order By createdDate desc";
		return super.select(sql, VtrService.class, new String[]{requestId,serviceId}, 0, -1);
	}

	protected boolean searchVtrServiceByRequestId(String requestId){
		try {
			String sql = 
				"select id " +
				"from fms_eng_service_vtr " +
				"where requestId = ?";

			Collection col = super.select(sql, VtrService.class, new String[]{requestId}, 0, -1);
			if(col!=null && col.size()>0){
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}

	public Collection selectVtrService(String id) throws DaoException{
		String sql="Select p.id,p.requestId,p.serviceId,p.service," +
		"p.requiredDate, p.requiredDateTo,p.requiredFrom,p.requiredTo,p.formatFrom,p.formatTo," +
		"p.conversionFrom,p.conversionTo,p.duration,p.noOfCopies,p.remarks," +
		"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate," +
		"p.facilityId, f.name as facility, p.blockBooking, p.location  " +
		"from fms_eng_service_vtr p " +
		"LEFT JOIN fms_rate_card f on f.id=p.facilityId " +
		"where p.id=? order By p.createdDate desc";
		return super.select(sql, VtrService.class, new String[]{id}, 0, -1);
	}

	protected void insertVtrAttachment(VtrService vtr) throws DaoException {
		String sqlFile = "INSERT INTO fms_attachment " +
		"(id, fileName, path, referenceId, createdBy, createdOn) " +
		"VALUES " +
		"(#fileId#, #fileName#, #filePath#, #id#, #modifiedBy#, #modifiedDate#) ";

		super.update(sqlFile, vtr);
	}

	protected void deleteVtrFile(String fileId) throws DaoException{
		super.update("DELETE FROM fms_attachment WHERE id=?", new String[] {fileId});
	}

	public Collection selectFiles(String vtrId) throws DaoException {
		String sql = "SELECT id AS fileId, fileName AS fileName, path AS filePath, referenceId AS id " +
		"FROM fms_attachment WHERE referenceId=?";
		return super.select(sql, VtrService.class, new String[] {vtrId}, 0, -1);
	}

	public VtrService selectFile(String fileId) throws DaoException {
		String sql = "SELECT id AS fileId, fileName AS fileName, path AS filePath FROM fms_attachment WHERE id=?";

		Collection col=super.select(sql, VtrService.class, new String[] {fileId} , 0, -1);

		if(col!=null && col.size()>0){
			return (VtrService)col.iterator().next();
		}
		return null;	
	}

	public void storeFile(StorageFile sf) throws InvalidKeyException, DaoException {
		// store uploaded file
		if (sf != null) {
			try {
				Application application = Application.getInstance();
				StorageService storage = (StorageService) application.getService(StorageService.class);
				storage.store(sf);

			} catch (Exception e) {
				throw new DaoException("Unable to save uploaded file " +
						sf.getAbsolutePath() + ": " + e.toString());
			}
		}
	}

	public void deleteFile(StorageFile sf) throws InvalidKeyException, DaoException {
		// store uploaded file
		if (sf != null) {
			try {
				Application application = Application.getInstance();
				StorageService storage = (StorageService) application.getService(StorageService.class);
				storage.delete(sf);

			} catch (Exception e) {
				throw new DaoException("Unable to save delete file " +
						sf.getAbsolutePath() + ": " + e.toString());
			}
		}
	}

	public void insertBooking(FacilityObject facility) throws DaoException {
		String sql="INSERT INTO fms_facility_booking " +
		"(id, facilityId, requestId, bookFrom,bookTo,timeFrom,timeTo,quantity,bookingType," +
		"createdBy,createdOn,modifiedBy,modifiedOn) VALUES " +
		"(#id#,#facilityId#,#requestId#,#bookFrom#,#bookTo#,#timeFrom#,#timeTo#,#quantity#,#bookingType#," +
		"#createdby#,#createdby_date#,#updatedby#,#updatedby_date#)";
		super.update(sql,facility);
	}

	public void deleteBooking(String requestId) throws DaoException {
		String sql="DELETE FROM fms_facility_booking " +
		"where requestId=? ";
		super.update(sql,new String[] {requestId});
	}

	public void deleteReport(String requestId) throws DaoException {
		String sql="DELETE FROM fms_eng_resources_report " +
		"where requestId=? ";
		super.update(sql,new String[] {requestId});
	}

	public boolean isUnitApprover(String userId){
		boolean result=false;
		String sql="select unitId,name from fms_unit u LEFT JOIN fms_unit_alternate_approver a on u.id=a.unitId " +
		"where (a.userId=? OR u.hou=? )";
		try {
			Collection col=super.select(sql, HashMap.class, new String[] {userId,userId} , 0, -1);
			if(col.size()>0){
				result=true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	public int selectDuration(String serviceName, String requestId) throws DaoException {
		String tableName 	= "fms_eng_service_scp";		
		String serviceId 	= ServiceDetailsForm.SERVICE_SCPMCP;
		String startDateCol	= "";
		String endDateCol	= "";

		if (serviceName.equals("scp")){
			tableName = "fms_eng_service_scp";
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("post")){
			tableName = "fms_eng_service_postproduction";
			serviceId = ServiceDetailsForm.SERVICE_POSTPRODUCTION;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDate";

		} else if (serviceName.equals("other")){
			tableName = "fms_eng_service_other";			
			serviceId = ServiceDetailsForm.SERVICE_OTHER;
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("studio")){
			tableName = "fms_eng_service_studio";
			serviceId = ServiceDetailsForm.SERVICE_STUDIO;
			startDateCol = "bookingDate";
			endDateCol	 = "bookingDate";

		} else if (serviceName.equals("vtr")){
			tableName = "fms_eng_service_vtr";
			serviceId = ServiceDetailsForm.SERVICE_VTR;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDate";

		} else if (serviceName.equals("manpower")){
			tableName = "fms_eng_service_manpower";
			serviceId = ServiceDetailsForm.SERVICE_MANPOWER;
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("tvro")){
			tableName = "fms_eng_service_tvro";
			serviceId = ServiceDetailsForm.SERVICE_TVRO;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDate";
		} 

		String sql = "SELECT DATEDIFF(day, svc."+ startDateCol + ", svc." + endDateCol + ") AS duration," +
		"svc." + startDateCol + " AS startDate, svc." + endDateCol + " AS endDate " +
		"FROM " + tableName + " svc INNER JOIN fms_eng_request_services rs " +
		"ON (svc.requestId = rs.requestId) " +
		"WHERE 1=1 " +
		"AND svc.requestId = ? " +
		"AND rs.serviceId = '" + serviceId +"'";

		int count = 0;
		try {			
			Collection col = super.select(sql, HashMap.class, new String[]{ requestId }, 0, 1);
			if (col != null) {
				if (col.size()>0){
					HashMap map = (HashMap) col.iterator().next();
					if ((((Integer)map.get("duration")) == 0) && (map.get("startDate").equals(map.get("endDate")))){
						count = 1;
					} else {
						count = (Integer) map.get("duration");
					}
				}
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}		

		return count;
	}

	public double selectTotalRate(String serviceName, String requestId, String rateType) throws DaoException{
		String tableName 	= "fms_eng_service_scp";
		String rateColumn	= "internalRate";
		String serviceId	= ServiceDetailsForm.SERVICE_SCPMCP;
		String startDateCol	= "";
		String endDateCol	= "";
		//String quantityOt	="";

		if (serviceName.equals("scp")){
			tableName = "fms_eng_service_scp";
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("post")){
			tableName = "fms_eng_service_postproduction";
			serviceId = ServiceDetailsForm.SERVICE_POSTPRODUCTION;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";

		}else if (serviceName.equals("other")){
			tableName = "fms_eng_service_other";			
			serviceId = ServiceDetailsForm.SERVICE_OTHER;
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";
			//quantityOt 	 = ", svc.quantity ";

		} else if (serviceName.equals("studio")){
			tableName = "fms_eng_service_studio";
			serviceId = ServiceDetailsForm.SERVICE_STUDIO;
			startDateCol = "bookingDate";
			endDateCol	 = "bookingDateTo";

		} else if (serviceName.equals("vtr")){
			tableName = "fms_eng_service_vtr";
			serviceId = ServiceDetailsForm.SERVICE_VTR;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";

		} else if (serviceName.equals("manpower")){
			tableName = "fms_eng_service_manpower";
			serviceId = ServiceDetailsForm.SERVICE_MANPOWER;
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("tvro")){
			tableName = "fms_eng_service_tvro";
			serviceId = ServiceDetailsForm.SERVICE_TVRO;
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";
		}  

		if (rateType.equals("E")){
			rateColumn = "externalRate";
		}

		String sql="SELECT svc."+ rateColumn +" AS totalRate, ";

		if ((serviceName.equals("manpower")) || (serviceName.equals("other"))){
			sql += "svc.quantity AS quantity, ";
		}

		sql +=	
			"DATEDIFF(day, svc."+ startDateCol + ", svc." + endDateCol + ") AS duration," +
			"svc." + startDateCol + " AS startDate, svc." + endDateCol + " AS endDate " +
			"FROM " + tableName + " svc INNER JOIN fms_eng_request_services rs " +
			"ON (svc.requestId = rs.requestId) " +
			"WHERE 1=1 " +
			"AND svc.requestId = ? " +
			"AND rs.serviceId = '" + serviceId +"'";


		double sum=0.0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{ requestId }, 0, -1);
			if(col!=null){
				if (col.size()>0) {
					for (Iterator i = col.iterator();i.hasNext();){
						HashMap map=(HashMap)i.next();

						if (map.get("totalRate") != null){
							Double totalRate = new Double(((Number)map.get("totalRate")).doubleValue());
							//long duration = dateDiff((Date)map.get("startDate"), (Date)map.get("endDate"));
							int duration = (Integer)map.get("duration") + 1;

							if ((serviceName.equals("manpower")) || (serviceName.equals("other"))){
								int quantity = (Integer)map.get("quantity");
								totalRate = totalRate * quantity * duration;
							}else{
								totalRate = totalRate * duration;
							}
							sum += totalRate;
						}
					}
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return sum;
	}

	public EngineeringRequest selectAssignment(String assignmentId) throws DaoException{
		String sql="SELECT a.requestId, a.assignmentId, a.code AS assignmentCode, req.title, m.requiredFrom, m.requiredTo, m.fromTime, m.toTime," +
		"p.programName, c.competencyName," +
		"m.status, m.remarks, m.reasonUnfulfilled, m.completionDate, m.chargeBack, m.callBack, m.userId AS manpowerId, " +
		"req.description, u.firstName + ' ' + u.lastName AS manpowerName " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId)" +
		"LEFT JOIN fms_eng_assignment_manpower m ON (a.assignmentId = m.assignmentId)" +
		"LEFT JOIN competency c ON (m.competencyId = c.competencyId)" +
		"LEFT JOIN fms_prog_setup p ON (req.program = p.programId) " +
		"LEFT JOIN security_user u on (u.id = m.userId) " +
		"WHERE a.assignmentId=? " +
		"AND a.assignmentType=? ";

		Collection col=super.select(sql, EngineeringRequest.class, new String[]{ assignmentId, EngineeringModule.ASSIGNMENT_TYPE_MANPOWER }, 0, 1);
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;
	}

	public EngineeringRequest selectEquipmentAssignment(String assignmentId) throws DaoException{
		String sql="SELECT a.requestId, a.assignmentId, a.code AS assignmentCode, req.title, " +
		"e.requiredFrom, e.requiredTo, e.fromTime, e.toTime, e.rateCardId, " +
		"req.description " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId)" +
		"LEFT JOIN fms_eng_assignment_equipment e ON (a.assignmentId = e.assignmentId)" +
		"WHERE a.assignmentId=? " +
		"AND a.assignmentType=? ";

		Collection col=super.select(sql, EngineeringRequest.class, new String[]{ assignmentId, EngineeringModule.ASSIGNMENT_TYPE_FACILITY }, 0, 1);
		if(col!=null && col.size()>0){
			return (EngineeringRequest)col.iterator().next();
		}
		return null;
	}

	public Collection selectAssignmentByRequestId(String requestId) throws DaoException {
		String sql = "SELECT assignmentId, code, requestId, " +
		" serviceType, serviceId,assignmentType " +
		" FROM fms_eng_assignment " +
		" WHERE requestId = ? ";

		return super.select(sql, Assignment.class, new String[] {requestId} , 0, -1);
	}

	public String selectWorkingProfile(String userId, Date startDate, Date endDate) throws DaoException {
		String sql = "SELECT distinct(wpdm.userId) AS userId, wp.name AS workingProfile " +
		"FROM fms_working_profile_duration_manpower wpdm LEFT JOIN fms_working_profile_duration wpd ON " +
		"(wpdm.workingProfileDurationId = wpd.workingProfileDurationId) " +
		"LEFT JOIN fms_working_profile wp ON (wpd.workingProfileId = wp.workingProfileId) " +
		"WHERE wpdm.userId = ? " +
		"AND (wpd.startDate <= ? and wpd.endDate >= ?) ";

		String workingProfile = "";
		Collection col = super.select(sql, HashMap.class, new Object[] { userId, endDate, startDate}, 0, -1);

		if(col != null) {
			if(col.size() > 0) {
				int x = 1;
				for(Iterator i = col.iterator();i.hasNext();){
					HashMap map = (HashMap) i.next();
					workingProfile += map.get("workingProfile").toString();
					if (x < col.size()){
						workingProfile += ", ";
					}
					x++;
				}
			} else {
				workingProfile = "N";
			}
		} 

		return workingProfile;
	}

	public boolean isServiceTypeExist(String requestId, String serviceType){
		boolean result=false;
		String sql="SELECT " +
		"assignmentId " +
		"FROM fms_eng_assignment " +
		"WHERE requestId=? AND serviceType= ? ";

		try {
			Collection col=super.select(sql, HashMap.class, new String[] {requestId, serviceType}, 0, 1);

			if (col != null && col.size()>0){
				result = true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}


	public void deleteAssignmentByAssignmentId(String assignmentId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment WHERE assignmentId = ? ";
		super.update(sql, new String[] {assignmentId});
	}
	
	protected void reduceFacilityBookingById(String id) {
		try {
			String sql =
					"UPDATE fms_facility_booking " +
					"SET quantity = quantity - 1 " +
					"WHERE id = ?";

			super.update(sql,new Object[] {id});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	protected void deleteFacilityBookingById(String id) {
		try {
			String sql =
					"DELETE FROM fms_facility_booking " +
					"WHERE id = ?";

			super.update(sql,new Object[] {id});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	protected Collection selectFacilityBooking(String requestId, String facilityId, Date bookFrom, Date bookTo, String timeFrom, String timeTo) {
		try {
			String sql =
					"SELECT id, quantity " +
					"FROM fms_facility_booking " +
					"WHERE requestId = ? " +
					"AND facilityId = ? " +
					"AND bookFrom = ? " +
					"AND bookTo = ? " +
					"AND timeFrom = ? " +
					"AND timeTo = ? ";

			return super.select(sql, DefaultDataObject.class, new Object[] {requestId, facilityId, bookFrom, bookTo, timeFrom, timeTo}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return null;
		}
	}
	
	public void deleteBookingByRequesIdtAndFacilityId(String requestId, String facilityId){
		try {

			String sql=" DELETE FROM fms_facility_booking " +
			"where requestId=? and facilityId=? ";
			
			super.update(sql,new String[] {requestId,facilityId});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}

	}

	public void deleteServiceFromRequestServices(String requestId, String serviceType) throws DaoException {
		String sql=
			"UPDATE fms_eng_request_services " +
			"SET cancelFlag = '1' " +
			"WHERE requestId=? and serviceId=? ";
		super.update(sql,new String[] {requestId,serviceType});
	}

	public boolean isServiceExist(String serviceId){
		boolean result=false;
		String sql="SELECT " +
		"assignmentId " +
		"FROM fms_eng_assignment " +
		"WHERE serviceId= ? ";

		try {
			Collection col=super.select(sql, HashMap.class, new String[] {serviceId}, 0, 1);

			if (col != null && col.size()>0){
				result = true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	public String getRateCardByAssignmentId(String assignmentId) throws DaoException {
		Collection col = new ArrayList();
		String rateCardId = "";

		String sql = "SELECT " +
		"rateCardId " +
		"FROM fms_eng_assignment_manpower " + 
		"WHERE assignmentId =? ";

		try {
			col = super.select(sql, HashMap.class, new String[] {assignmentId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						rateCardId = (String) map.get("rateCardId");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return rateCardId;
	}
	
	public boolean isRateCardExistForRequest(String requestId, String rateCardId) {
		String sql = 
				"SELECT esm.rateCardId " +
				"FROM fms_eng_assignment ea " +
				"INNER JOIN fms_eng_assignment_equipment esm ON (esm.assignmentId = ea.assignmentId) " +
				"WHERE ea.requestId = ? " +
				"AND esm.rateCardId = ? " + 
					"UNION " +
				"SELECT esm.rateCardId " +
				"FROM fms_eng_assignment ea " +
				"INNER JOIN fms_eng_assignment_manpower esm ON (esm.assignmentId = ea.assignmentId) " +
				"WHERE ea.requestId = ? " +
				"AND esm.rateCardId = ? ";

		try {
			Collection col = super.select(sql, HashMap.class, new String[] {requestId, rateCardId, requestId, rateCardId}, 0, 1);
			if (col != null && col.size() > 0) {
				return true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return false;
	}
	
	public void deleteRateCardByReqIdFromRequestUnit(String requestId, String rateCardId){
		try {
			String sql=" DELETE FROM fms_eng_request_unit " +
			"where requestId=? AND rateCardId=? ";

			super.update(sql,new String[] {requestId, rateCardId});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public void deleteRequestFromServicesTypeTable(String requestId, String serviceType) throws DaoException {
		String tableName="";
		
		if (serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP))
			tableName = "fms_eng_service_scp";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION))
			tableName = "fms_eng_service_postproduction";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_OTHER))
			tableName = "fms_eng_service_other";			
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_STUDIO))
			tableName = "fms_eng_service_studio";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_VTR))
			tableName = "fms_eng_service_vtr";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			tableName = "fms_eng_service_manpower";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_TVRO))
			tableName = "fms_eng_service_tvro";

		// logging
		TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
		transLog.info(requestId, "SERVICE_DELETE", "tableName=" + tableName);
		
		String sql = "DELETE FROM "+tableName+" WHERE requestId = ? ";
		super.update(sql, new String[] {requestId});
	}
	/*
	 *   
	 */
	public void deleteServiceFacility(String assignmentId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment WHERE assignmentId = ? ";
		super.update(sql, new String[] {assignmentId});
	}

	public void deleteAssignment(String requestId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment WHERE requestId = ? ";
		super.update(sql, new String[] {requestId});
	}

	public void deleteAssignmentByServiceId(String serviceId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment WHERE serviceId = ? ";
		super.update(sql, new String[] {serviceId});
	}

	public void deleteEquipmentAssignment(String assignmentId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment_equipment WHERE assignmentId = ? ";
		super.update(sql, new String[] {assignmentId});
	}

	public void deleteEquipmentAssignmentById(String assignmentEquipmentId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment_equipment WHERE id = ? ";
		super.update(sql, new String[] {assignmentEquipmentId});
	}

	public void deleteManpowerAssignment(String assignmentId) throws DaoException {
		String sql = "DELETE FROM fms_eng_assignment_manpower WHERE assignmentId = ? ";
		super.update(sql, new String[] {assignmentId});
	}

	public Map selectManpowerAssignment(String manpowerId, String assignmentId, Date startDate, Date endDate, String fromTime, String toTime) throws DaoException {
		Map manpowerMap = new HashMap();
		long diff = dateDiff(startDate, endDate);
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		String sql = "SELECT u.id AS userId, u.username AS username, u.firstName AS firstName, u.lastName AS lastName" +
		", c.competencyName AS competencyName " +
		"FROM security_user u LEFT JOIN competency_user cu ON (u.id = cu.userId) " +
		"LEFT JOIN competency c ON (cu.competencyId = c.competencyId) " +
		"WHERE u.id = ? ";	

		String sql1 = "SELECT distinct(u.id) AS userId, u.username AS username, u.firstName AS firstName, u.lastName AS lastName, " +
		"a.assignmentId, a.requiredFrom AS requiredFrom, a.requiredTo AS requiredTo, a.fromTime AS fromTime, " +
		"a.toTime AS toTime, asg.requestId, asg.code," +
		"req.title, req.requestId " +
		"FROM security_user u LEFT JOIN fms_eng_assignment_manpower a ON (u.id = a.userId) " +
		"LEFT JOIN fms_eng_assignment asg ON (a.assignmentId = asg.assignmentId) " +
		"LEFT JOIN fms_eng_request req ON (asg.requestId = req.requestId) " +
		"WHERE u.id = ? " +
		"AND (a.assignmentId = ? OR (a.requiredFrom <= ? AND a.requiredTo >= ?))";
		

		String sqlCheckLeave = "SELECT distinct s.id, l.manpowerId, s.dateFrom, s.dateTo " +
		"FROM fms_manpow_leave_setup s left join fms_manpower_leave l ON (s.id = l.leaveId) ";

		String whereClause = "WHERE l.manpowerId = ? AND (? BETWEEN s.dateFrom AND s.dateTo ) " ;

		String orderClause = "ORDER BY u.id DESC ";

		Collection col = super.select(sql + orderClause, HashMap.class, new Object[] {manpowerId}, 0, -1);

		String userId = "";
		String userName = "";
		String competency = "";
		String assignment = "<ul>";
		String leaveList = "";

		for (Iterator itr = col.iterator(); itr.hasNext();) {
			HashMap map = (HashMap) itr.next();
			userId = (String) map.get("userId");
			userName = (String) map.get("firstName") + " " + (String) map.get("lastName");
			competency = (String) map.get("competencyName");

			Collection col2 = new ArrayList();
			Collection leave = new ArrayList();


			for (int i = 0; i <= diff; i++){
				long msChecked = startDate.getTime() + (DateDiffUtil.MS_IN_A_DAY * i);
				Date dateChecked = new Date(msChecked);		

				leave = super.select(sqlCheckLeave + whereClause, HashMap.class, new Object[] {userId, dateChecked}, 0, -1);				
			}

			col2 = super.select(sql1 + orderClause, 
					HashMap.class, new Object[] { userId, assignmentId, endDate, startDate}, 0, -1);//TODO: check


			if (leave != null && leave.size()>0){
				for (Iterator it = leave.iterator(); it.hasNext();){
					HashMap mapLeave = (HashMap) it.next();
					leaveList = "(On leave : " + sdf.format(mapLeave.get("dateFrom")) + " - " + sdf.format(mapLeave.get("dateTo")) + ")";
				}
			}

			if (col2 != null && col2.size()>0){
				for (Iterator itrx = col2.iterator(); itrx.hasNext();){
					HashMap map2 = (HashMap) itrx.next();

					if (map2.get("requestId")!=null && map2.get("assignmentId")!=null && map2.get("title")!=null){
						assignment += "<li>";
						assignment += "<a href='requestAssignmentDetails.jsp?requestId=" + (String) map2.get("requestId") + "'>";
						assignment += (String) map2.get("title") + " (" + (String) map2.get("requestId") + ")";
						assignment += "</a><br />";
						assignment += (String) map2.get("code") + "<br />";
						assignment += sdf.format(map2.get("requiredFrom")) + " - " + sdf.format(map2.get("requiredTo")) + "<br />";
						assignment += (String) map2.get("fromTime") + " - " + (String) map2.get("toTime") ;
						assignment += "</li>";
					}
				}					
			}
		}
		assignment += "</ul>";

		manpowerMap.put("competencyId", userId);
		if (!"".equals(leaveList)){
			manpowerMap.put("competencyName", "<font style='background-color:red'>" + userName + "</font><br />" + leaveList);
		} else {
			manpowerMap.put("competencyName", userName );
		}
		manpowerMap.put("workingProfile", selectWorkingProfile(userId, startDate, endDate));
		manpowerMap.put("competency", competency);
		manpowerMap.put("totalUser", assignment);
		manpowerMap.put("asgId", assignmentId);

		return manpowerMap;
	}

	public long dateDiff(Date start, Date end){

		long diff = Math.round((end.getTime() - start.getTime()) / (DateDiffUtil.MS_IN_A_DAY));

		return diff;
	}

	public long durationRequired(Date start, Date end){

		long diff = Math.round((end.getTime() - start.getTime()) / (24 * 60 *60));

		return diff;
	}

	public void updateManpowerAssignment(EngineeringRequest eRequest) throws DaoException {
		super.update("UPDATE fms_eng_assignment_manpower SET userId=#manpowerId#, " +
				"status = #status#, " +
				"chargeBack = #chargeBack#, " +
				"callBack = #callBack#, " +
				"modifiedBy = #modifiedBy# , " +
				"modifiedDate= #modifiedOn# " +
				"WHERE assignmentId = #assignmentId# ", eRequest);
	}


	public void completeManpowerAssignment(EngineeringRequest eRequest) throws DaoException {
		super.update("UPDATE fms_eng_assignment_manpower SET completionDate=#completionDate#, " +
				"status = #status#, " +
				"remarks = #description#, " +
				"reasonUnfulfilled = NULL, " +
				"modifiedBy = #modifiedBy# , " +
				"modifiedDate= #modifiedOn# " +
				"WHERE assignmentId = #assignmentId# ", eRequest);
	}

	public void unfulfillManpowerAssignment(EngineeringRequest eRequest) throws DaoException {
		super.update("UPDATE fms_eng_assignment_manpower SET " +
				"status = #status#, " +
				"reasonUnfulfilled = #description#, " +
				"remarks = NULL, " +
				"completionDate = NULL, " +
				"modifiedBy = #modifiedBy# , " +
				"modifiedDate= #modifiedOn# " +
				"WHERE assignmentId = #assignmentId# ", eRequest);
	}

	public Collection selectAssignmentByUserId(String userId, String search, String status, String sort, boolean desc, int start, int rows) throws DaoException {
		String sql="SELECT a.assignmentId, a.code AS assignmentCode, req.title, " +
		"m.requiredFrom, m.requiredTo, " +
		"(m.fromTime + ' - ' + m.toTime) as requiredTime, " +
		"m.status, " +
		"p.programName, c.competencyName, a.serviceType AS requestType " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId) " +
		"LEFT JOIN fms_eng_assignment_manpower m ON (a.assignmentId = m.assignmentId) " +
		"LEFT JOIN competency c ON (m.competencyId = c.competencyId) " +
		"LEFT JOIN fms_prog_setup p ON (req.program = p.programId) " +
		"WHERE m.userId=? " +
		"AND a.assignmentType=? ";
		Collection col = null;
		ArrayList args = new ArrayList();
		args.add(userId);
		args.add(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER);

		if(search!=null && !"".equals(search)){
			sql+=" AND ( a.code like ? OR p.programName like ? OR req.title like ? ) ";
			args.add("%"+ search +"%");
			args.add("%"+ search +"%");
			args.add("%"+ search +"%");
		}
		if(status!=null && !"".equals(status)){
			sql+=" AND ( m.status = ? ) ";
			args.add(status);
		}

		if(sort == null){
			sort="m.status, m.requiredFrom";
		}
		sql += "ORDER BY " + sort + " DESC";

		col = super.select(sql, EngineeringRequest.class, args.toArray(), start, rows);

		return col;
	}

	public int countAssignmentByUserId(String userId, String search, String status) throws DaoException {
		String sql="SELECT count(*) AS COUNT " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId)" +
		"LEFT JOIN fms_eng_assignment_manpower m ON (a.assignmentId = m.assignmentId)" +
		"LEFT JOIN competency c ON (m.competencyId = c.competencyId)" +
		"LEFT JOIN fms_prog_setup p ON (req.program = p.programId) " +
		"WHERE m.userId=? " +
		"AND a.assignmentType=? ";

		ArrayList args = new ArrayList();
		args.add(userId);
		args.add(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER);

		if(search!=null && !"".equals(search)){
			sql+=" AND ( a.code like ? OR p.programName like ? OR req.title like ? ) ";
			args.add("%"+ search +"%");
			args.add("%"+ search +"%");
			args.add("%"+ search +"%");
		}
		if(status!=null && !"".equals(status)){
			sql+=" AND ( m.status = ? ) ";
			args.add(status);
		}

		int count = 0;
		try {			
			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, 1);
			if (col != null) {
				HashMap map = (HashMap) col.iterator().next();
				count = (Integer) map.get("COUNT");
			}
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}


		return count;
	}

	protected void insertAttachment(EngineeringRequest er) throws DaoException {
		String sqlFile = "INSERT INTO fms_attachment " +
		"(id, fileName, path, referenceId, createdBy, createdOn, modifiedBy, modifiedOn) " +
		"VALUES " +
		"(#fileId#, #fileName#, #filePath#, #assignmentId#, #createdBy#, #createdOn#, #modifiedBy#, #modifiedOn#) ";

		super.update(sqlFile, er);
	}

	public Collection selectManpowerFiles(String id) throws DaoException {
		String sql = "SELECT id AS fileId, fileName AS fileName, path AS filePath, referenceId AS assignmentId " +
		"FROM fms_attachment WHERE referenceId=?";
		return super.select(sql, EngineeringRequest.class, new String[] {id}, 0, -1);
	}

	public Collection selectCategoryByBarcode(String barcode, String rateCardId) throws DaoException {
		String sql = "select c.categoryId " +
		"FROM fms_facility_item i INNER JOIN fms_eng_rate_card_cat_item c " +
		"ON (i.facility_id = c.facilityId) " +
		"INNER JOIN fms_rate_card_equipment rce ON (c.categoryId=rce.equipment) " +
		"WHERE i.barcode =? " +
		"AND rce.rateCardId = ? ";
		return super.select(sql, RateCard.class, new String[] {barcode, rateCardId}, 0, -1);
	}

	public Collection selectCategoryByBarcode(String barcode) throws DaoException {
		String sql = "select c.categoryId " +
		"FROM fms_facility_item i INNER JOIN fms_eng_rate_card_cat_item c " +
		"ON (i.facility_id = c.facilityId) " +
		"INNER JOIN fms_rate_card_equipment rce ON (c.categoryId=rce.equipment) " +
		"WHERE i.barcode =? " ;
		//"AND rce.rateCardId = ? ";
		return super.select(sql, RateCard.class, new String[] {barcode}, 0, -1);
	}

	public Collection selectRateCardIdByGroup(String groupId) throws DaoException {
		String sql = "SELECT e.rateCardId, e.rateCardCategoryId, e.assignmentId " +
		"FROM fms_eng_assignment_equipment" +
		"WHERE groupId = ? ";
		return super.select(sql, Assignment.class, new String[] {groupId}, 0, -1);
	}

	public Collection selectCheckOutEquipment(String assignmentId, String rateCardCategoryId) throws DaoException {
		String sql = "SELECT ae.id AS assignmentEquipmentId, ae.assignmentId, ae.rateCardCategoryId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.assignmentId=? AND ae.rateCardCategoryId=? AND ae.barcode IS NULL " +
		"AND ae.checkedOutBy IS NULL AND ae.status=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{assignmentId, rateCardCategoryId, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW}, 0, -1);
	}

	public Collection selectCheckOutEquipmentByGroup(String groupId, String rateCardCategoryId) throws DaoException {
		String sql = "SELECT ae.id AS assignmentEquipmentId, ae.assignmentId, ae.rateCardCategoryId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.groupId=? AND ae.rateCardCategoryId=? AND ae.barcode IS NULL " +
		"AND ae.checkedOutBy IS NULL AND ae.status=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{groupId, rateCardCategoryId, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW}, 0, -1);
	}

	public Collection selectEquipmentByBc(String assignmentId, String barcode) throws DaoException {
		String sql = "SELECT ae.id AS assignmentEquipmentId, ae.assignmentId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.assignmentId=? AND ae.barcode=? " +
		"AND ae.checkedInBy IS NULL AND ae.status=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{assignmentId, barcode, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT}, 0, -1);
	}

	public Collection selectEquipmentByBcGroupId(String groupId, String barcode) throws DaoException {
		String sql = "SELECT ae.id AS assignmentEquipmentId, ae.assignmentId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.groupId=? AND ae.barcode=? " +
		"AND ae.checkedInBy IS NULL AND ae.status=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{groupId, barcode, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT}, 0, -1);
	}

	public Collection selectEquipmentByBc(String barcode) throws DaoException {
		String sql = "SELECT ae.id AS assignmentEquipmentId, ae.assignmentId, ae.groupId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.barcode=? " +
		"AND ae.checkedInBy IS NULL AND ae.status=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{barcode, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT}, 0, -1);
	}

	public Collection selectAssignmentEquipment(String assignmentId) throws DaoException {
		String sql = "SELECT TOP 1 ae.assignmentId, ae.requiredFrom, ae.requiredTo, ae.fromTime, ae.toTime, ae.status," +
		"ae.createdBy, ae.createdDate AS createdOn " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.assignmentId=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{assignmentId}, 0, -1);
	}

	public Collection selectAssignmentEquipmentByGroup(String groupId) throws DaoException {
		String sql = "SELECT TOP 1 ae.assignmentId, ae.requiredFrom, ae.requiredTo, ae.fromTime, ae.toTime, ae.status," +
		"ae.createdBy, ae.createdDate AS createdOn, ae.groupId " +
		"FROM fms_eng_assignment_equipment ae " +
		"WHERE ae.groupId=? ";
		return super.select(sql, EngineeringRequest.class, new Object[]{groupId}, 0, -1);
	}

	protected void updateCheckOutEquipment(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_assignment_equipment SET " +
		"barcode=#barcode#, checkedOutBy=#checkedOutBy#, checkedOutDate=#checkedOutDate#, status=#status#, " +
		"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#, takenBy=#takenBy#, preparedBy=#preparedBy#, assignmentLocation=#assignmentLocation# " +
		"WHERE id=#assignmentEquipmentId# ";
		super.update(sql,request);
	}

	public void undoCheckOutEquipment(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_assignment_equipment SET " +
		"barcode=NULL, checkedOutBy=NULL, checkedOutDate=NULL, status=#status#, " +
		"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#, takenBy=NULL, preparedBy=NULL, assignmentLocation=NULL " +
		"WHERE barcode=#barcode# AND id=#id# " +
		"AND checkedInBy IS NULL AND checkedInDate IS NULL " +
		"AND status='" + EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT + "' ";
		super.update(sql,request);
	}

	public boolean hasAssignmentCheckOut(String barcode) throws DaoException {
		String sql = "SELECT barcode FROM fms_eng_assignment_equipment WHERE barcode=? and status=? ";
		Collection col = super.select(sql, DefaultDataObject.class, new String[] {barcode, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_CHECKOUT}, 0, 1);
		if (col.size() != 0) {
			return true;
		} else {
			return false;
		}
	}

	protected void updateCheckInEquipment(EngineeringRequest request) throws DaoException {
		String sql = "UPDATE fms_eng_assignment_equipment SET " +
		"checkedInBy=#checkedInBy#, checkedInDate=#checkedInDate#, status=#status#, damage=#damage#, " +
		"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn# " +
		"WHERE id=#assignmentEquipmentId# ";
		super.update(sql, request);
	}

	protected void insertCheckOutEquipment(EngineeringRequest request) throws DaoException {
		String sql = "INSERT INTO fms_eng_assignment_equipment " +
		"(id, assignmentId, requiredFrom, requiredTo, fromTime, toTime, barcode, " +
		"checkedOutBy, checkedOutDate, status, createdBy, createdDate, " +
		"modifiedBy, modifiedDate,takenBy, preparedBy, assignmentLocation) " +
		"VALUES " +
		"(#assignmentEquipmentId#, #assignmentId#, #requiredFrom#, #requiredTo#, #fromTime#, #toTime#, #barcode#, " +
		"#checkedOutBy#, #checkedOutDate#, #status#, #createdBy#, #createdOn#, " +
		"#modifiedBy#, #modifiedOn#, #takenBy#, #preparedBy#, #assignmentLocation#) ";
		super.update(sql, request);
	}

	protected void insertCheckOutEquipmentByGroup(EngineeringRequest request) throws DaoException {
		String sql = "INSERT INTO fms_eng_assignment_equipment " +
		"(id, assignmentId, requiredFrom, requiredTo, fromTime, toTime, barcode, " +
		"checkedOutBy, checkedOutDate, status, createdBy, createdDate, " +
		"modifiedBy, modifiedDate,takenBy, preparedBy, assignmentLocation, groupId) " +
		"VALUES " +
		"(#assignmentEquipmentId#, '-', #requiredFrom#, #requiredTo#, #fromTime#, #toTime#, #barcode#, " +
		"#checkedOutBy#, #checkedOutDate#, #status#, #createdBy#, #createdOn#, " +
		"#modifiedBy#, #modifiedOn#, #takenBy#, #preparedBy#, #assignmentLocation#, #groupId#) ";
		super.update(sql, request);
	}

	protected String[] getRequestorEmail(String requestId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		String sql = "SELECT su.email1 AS email " +
		"FROM fms_eng_request req LEFT JOIN security_user su ON (req.createdBy = su.username) " +
		"WHERE req.requestId = ? ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{requestId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		
			colEmails.add(map.get("email"));
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected String[] getAllFCEmail() throws DaoException {
		Collection colEmails = new ArrayList();
		Map map = new HashMap();
		String sql = "SELECT su.email1 AS email " +
		"FROM security_user su INNER JOIN security_user_group sug ON (su.id = sug.userId) " +
		"INNER JOIN security_group sg ON (sug.groupId = sg.id) " +
		"WHERE sg.id = '" + EngineeringModule.FC_GROUP_ID + "'";

		Collection list = super.select(sql, HashMap.class, null, 0, -1);

		for (Iterator it = list.iterator(); it.hasNext();) {
			map = (HashMap) it.next();
			colEmails.add(map.get("email"));
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected String[] getFCEmail(String requestId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		String sql = "SELECT su.email1 AS email " +
		"FROM fms_eng_request req LEFT JOIN security_user su ON (req.fcId = su.id) " +
		"WHERE req.requestId = ? ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{requestId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		
			colEmails.add(map.get("email"));
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected String[] getHOUEmails(String requestId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		String sql = "SELECT su.unit AS unit " +
		"FROM fms_eng_request req LEFT JOIN security_user su ON (req.createdBy = su.username) " +
		"WHERE req.requestId = ? ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{requestId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		

			Collection HOU = getHOUEmail((String)map.get("unit"));
			for (Iterator hou = HOU.iterator(); hou.hasNext();){
				HashMap houMap = (HashMap) hou.next();
				colEmails.add(houMap.get("email"));
			}
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected String[] getHOUEmailsByAssignmentId(String assignmentId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		String sql = "select su.email1 AS email, u.id as unit from fms_eng_assignment_manpower am " +
		" INNER JOIN competency c on c.competencyId=am.competencyId " +
		" left join fms_unit u on u.id = c.unitId " +
		" LEFT JOIN security_user su ON (u.HOU = su.id) " +
		" where assignmentId=? ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{assignmentId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		

			colEmails.add(map.get("email"));
			Collection altHOU = getHOUEmailAltApprover((String)map.get("unit"));
			for (Iterator hou = altHOU.iterator(); hou.hasNext();){
				HashMap houMap = (HashMap) hou.next();
				colEmails.add(houMap.get("email"));

			}
		}

		return (String[])colEmails.toArray(new String[0]);
	}


	protected Collection getHOUEmail(String unitId) throws DaoException {
		String sqlHOD = "SELECT su.email1 AS email " +
		"FROM fms_unit u LEFT JOIN security_user su ON (u.HOU = su.id) " +
		"WHERE u.id = ? ";
		return super.select(sqlHOD, HashMap.class, new Object[]{unitId}, 0, -1);
	}

	protected Collection getHOUEmailAltApprover(String unitId) throws DaoException {
		String sqlHOD = "select su.email1 as email from fms_unit_alternate_approver ap " +
		" LEFT JOIN security_user su ON (ap.userId = su.id) " +
		" where unitId=? " ;
		return super.select(sqlHOD, HashMap.class, new Object[]{unitId}, 0, -1);
	}

	protected String[] getApproverEmail(String requestId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();

		String sql = "SELECT su.department AS deptid " +
		"FROM fms_eng_request req LEFT JOIN security_user su ON (req.createdBy = su.username) " +
		"WHERE req.requestId = ? ";		


		Collection list = super.select(sql, HashMap.class, new Object[]{requestId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();	

			Collection HOD = getHODEmail((String)map.get("deptid"));
			//Collection alternate = getAlternateApproverEmail((String)map.get("deptid"));

			for (Iterator hod = HOD.iterator(); hod.hasNext();) {
				HashMap hodMap = (HashMap) hod.next();
				colEmails.add(hodMap.get("email"));
			}
			//for (Iterator alt = alternate.iterator(); alt.hasNext();){
			//	HashMap altMap = (HashMap) alt.next();
			//	colEmails.add(altMap.get("email"));
			//}
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected String[] getAlternateApproverDeptEmail(String requestId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();

		String sql = "SELECT su.department AS deptid " +
		"FROM fms_eng_request req LEFT JOIN security_user su ON (req.createdBy = su.username) " +
		"WHERE req.requestId = ? ";		


		Collection list = super.select(sql, HashMap.class, new Object[]{requestId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();	

			//Collection HOD = getHODEmail((String)map.get("deptid"));
			Collection alternate = getAlternateApproverEmail((String)map.get("deptid"));

			//for (Iterator hod = HOD.iterator(); hod.hasNext();) {
			//	HashMap hodMap = (HashMap) hod.next();
			//	colEmails.add(hodMap.get("email"));
			//}
			for (Iterator alt = alternate.iterator(); alt.hasNext();){
				HashMap altMap = (HashMap) alt.next();
				colEmails.add(altMap.get("email"));
			}
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	protected Collection getHODEmail(String deptId) throws DaoException {
		String sqlHOD = "SELECT u.email1 AS email " +
		"FROM fms_department d INNER JOIN security_user u ON (d.HOD = u.id) " +
		"WHERE d.id = ? ";
		return super.select(sqlHOD, HashMap.class, new Object[]{deptId}, 0, -1);
	}

	protected Collection getAlternateApproverEmail(String deptId) throws DaoException {
		String sqlApprover = "SELECT u.email1 AS email " +
		"FROM fms_department_alternate_approver a INNER JOIN security_user u ON (a.userId = u.id) " +
		"WHERE a.departmentId = ?"; 
		return super.select(sqlApprover, HashMap.class, new Object[] {deptId}, 0, -1);
	}

	protected Collection getUnitAlternateApproverEmail(String rateCardId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();

		String sql = "SELECT distinct(u.email1) AS email " +
		"FROM fms_rate_card rc INNER JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		"INNER JOIN competency com ON (rcm.manpower = com.competencyId) " +
		"LEFT JOIN fms_unit_alternate_approver a ON (com.unitId = a.unitId) " +
		"LEFT JOIN security_user u ON (a.userId = u.id) " +
		"WHERE 1=1 " +
		"AND rcm.rateCardId=? " +
		"AND u.email1 IS NOT NULL ";				

		Collection list = super.select(sql, HashMap.class, new Object[]{rateCardId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();	

			colEmails.add(map.get("email"));			
		}

		return colEmails;//(String[])colEmails.toArray(new String[0]);
	}

	protected String[] getEngineeringUnitAppEmail(String unitId) throws DaoException {
		Collection colEmails = new ArrayList();
		Map map =  new HashMap();
		String sql = "SELECT u.email1 AS email " +
		"FROM fms_unit_alternate_approver a INNER JOIN security_user u ON (a.userId = u.id) " +
		"WHERE a.unitId = ? ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{unitId}, 0, -1);

		for(Iterator it = list.iterator(); it.hasNext(); ){
			map = (HashMap) it.next();		
			colEmails.add(map.get("email"));
		}

		return (String[])colEmails.toArray(new String[0]);
	}

	public void insertFeedBack(Collection col, String id) throws DaoException{
		for (Iterator i = col.iterator(); i.hasNext();) {
			HashMap feedBack = (HashMap) i.next();
			super.update(
					"INSERT INTO fms_req_feedback (id, requestId, serviceType, question, answer) VALUES (?, ?, ?, ?, ?)",
					new Object[] { UuidGenerator.getInstance().getUuid(),
							feedBack.get("requestId"), 
							feedBack.get("serviceType"),
							feedBack.get("question"),
							feedBack.get("answer")
					});
		}

	}

	public Collection selectUnitByRequestId(String requestId, String tableName, String column) throws DaoException {
		String sql = "SELECT distinct(fc.unit_id) AS unitId, service.requestId AS requestId, service." + column + " AS facilityId " +
		"FROM " + tableName + " service INNER JOIN fms_rate_card rc ON (service." + column + " = rc.id) " +
		"INNER JOIN fms_rate_card_equipment rce ON (rc.id = rce.rateCardId) " +
		"INNER JOIN fms_eng_rate_card_cat_item rcci ON (rce.equipment = rcci.categoryId) " +
		"INNER JOIN fms_facility f ON (rcci.facilityId = f.id) " +
		"INNER JOIN fms_facility_category fc ON (f.category_Id = fc.id) " +
		"WHERE service.requestId = ?";

		return super.select(sql, HashMap.class, new String[] {requestId}, 0, -1);
	}

	public Collection selectUnitManpowerByRequestId(String requestId, String tableName, String column) throws DaoException {
		String sql = "SELECT distinct(c.unitId) AS unitId, service.requestId AS requestId, service." + column + " AS facilityId " +
		"FROM " + tableName + " service INNER JOIN fms_rate_card rc ON (service." + column +" = rc.id) "+
		"	INNER JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		"	INNER JOIN competency c ON (rcm.manpower = c.competencyId) " +
		"WHERE service.requestId = ?";

		return super.select(sql, HashMap.class, new String[] {requestId}, 0, -1);
	}

	public Collection selectManpowerUnitByRequestId(String requestId) throws DaoException {
		String sql = "SELECT distinct(c.unitId) AS unitId, service.requestId, service.competencyId AS facilityId " +
		"FROM fms_eng_service_manpower service INNER JOIN fms_rate_card rc ON (service.competencyId = rc.id) " +
		"INNER JOIN fms_rate_card_manpower rcm ON (rc.id = rcm.rateCardId) " +
		"INNER JOIN competency c ON (c.competencyId = rcm.manpower) " +
		"WHERE service.requestId = ?";

		return super.select(sql, HashMap.class, new String[] {requestId}, 0, -1);
	}

	public void insertRequestUnit(String id, String requestId, String unitId, String serviceId, String rateCardId) throws DaoException {
		String sql="INSERT INTO fms_eng_request_unit " +
		"(id, requestId, unitId, serviceId, rateCardId) " +
		"VALUES " +
		"(?,?,?,?,?)";
		super.update(sql,new Object[] {id, requestId, unitId, serviceId, rateCardId});
	}

	public void deleteRequestUnit(String requestId, String serviceId) throws DaoException {
		String sql = "DELETE FROM fms_eng_request_unit WHERE requestId = ? AND serviceId = ?";
		super.update(sql, new String[] {requestId, serviceId});
	}

	public Collection selectRequestUnit(String requestId) throws DaoException {
		String sql = "SELECT id, requestId, unitId, serviceId " +
		"FROM fms_eng_request_unit " +
		"WHERE requestId = ?";

		return super.select(sql, HashMap.class, new String[] {requestId}, 0, -1);
	}

	public Collection selectRequestUnitByRc(String rateCardId, String requestId) throws DaoException {
		String sql = "SELECT id, requestId, unitId, serviceId " +
		"FROM fms_eng_request_unit " +
		"WHERE requestId = ? AND rateCardId = ?";

		return super.select(sql, HashMap.class, new String[] {requestId, rateCardId}, 0, -1);
	}

	public Collection selectEquipmentAssignmentByRequestId(String requestId) throws DaoException{
		String sql="SELECT a.requestId, a.assignmentId, a.code AS assignmentCode, req.title, " +
		"e.requiredFrom, e.requiredTo, e.fromTime, e.toTime, e.rateCardId, " +
		"req.description " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId)" +
		"LEFT JOIN fms_eng_assignment_equipment e ON (a.assignmentId = e.assignmentId)" +
		"WHERE a.requestId=? " +
		"AND a.assignmentType=? ";

		return super.select(sql, EngineeringRequest.class, new String[]{ requestId, EngineeringModule.ASSIGNMENT_TYPE_FACILITY }, 0, -1);
		//		if(col!=null && col.size()>0){
		//			return (EngineeringRequest)col.iterator().next();
		//		}
		//		return null;
	}

	public EngineeringRequest selectAssignmentRequest(String groupId) throws DaoException {
		String sql = "SELECT distinct(a.requestId) AS requestId, r.title " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request r ON (a.requestId = r.requestId) " +
		"WHERE a.groupId = ? " +
		"AND a.assignmentType = ?";

		Collection col = super.select(sql, EngineeringRequest.class, new String[] {groupId, EngineeringModule.ASSIGNMENT_TYPE_FACILITY}, 0, 1);
		if (col != null && col.size()>0){
			return (EngineeringRequest) col.iterator().next();
		}

		return null;
	}

	public Collection selectEquipmentAssignmentByGroupId(String groupId) throws DaoException{
		String sql="SELECT a.requestId, a.assignmentId, a.code AS assignmentCode, req.title, " +
		"e.requiredFrom, e.requiredTo, e.fromTime, e.toTime, e.rateCardId, " +
		"req.description " +
		"FROM fms_eng_assignment a LEFT JOIN fms_eng_request req ON ( a.requestId = req.requestId)" +
		"LEFT JOIN fms_eng_assignment_equipment e ON (a.assignmentId = e.assignmentId)" +
		"WHERE a.groupId=? " +
		"AND a.assignmentType=? ";

		Collection col=super.select(sql, EngineeringRequest.class, new String[]{ groupId, EngineeringModule.ASSIGNMENT_TYPE_FACILITY }, 0, 1);

		return col;
	}

	public void completeEquipmentAssignment(EngineeringRequest eRequest) throws DaoException {
		super.update("UPDATE fms_eng_assignment_equipment " +
				"SET checkedInDate=#checkedInDate#, " +
				"checkedInBy= #modifiedBy#, " +
				"status = #status#, " +				
				"modifiedBy = #modifiedBy# , " +
				"modifiedDate= #modifiedOn# " +
				"WHERE groupId = #groupId# ", eRequest);
	}

	public void unfulfilledEquipmentAssignment(EngineeringRequest eRequest) throws DaoException {
		super.update("UPDATE fms_eng_assignment_equipment " +
				"SET reasonUnfulfilled=#reasonUnfulfilled#, " +
				"status = #status#, " +				
				"modifiedBy = #modifiedBy# , " +
				"modifiedDate= #modifiedOn# " +
				"WHERE groupId = #groupId# ", eRequest);
	}

	public boolean isBlockBooking(String assignmentId){
		boolean result=false;
		String sql="SELECT " +
		"esm.blockBooking AS blockBooking " +
		"FROM fms_eng_service_manpower esm INNER JOIN fms_eng_assignment ea " +
		"ON (esm.id = ea.serviceId) " +
		"WHERE ea.assignmentId = ?";
		try {
			Collection col=super.select(sql, HashMap.class, new String[] {assignmentId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						String block = (String) map.get("blockBooking");
						result = "1".equals(block)?true:false;
					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	public String getServiceBlockBooking(String servId, String serviceType){
		String tableName="";
		
		if (serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP))
			tableName = "fms_eng_service_scp";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION))
			tableName = "fms_eng_service_postproduction";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_OTHER))
			tableName = "fms_eng_service_other";			
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_STUDIO))
			tableName = "fms_eng_service_studio";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_VTR))
			tableName = "fms_eng_service_vtr";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			tableName = "fms_eng_service_manpower";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_TVRO))
			tableName = "fms_eng_service_tvro";
		
		String sql="SELECT " +
		"esm.blockBooking AS blockBooking " +
		"FROM "+tableName+" esm " +
		"WHERE esm.id = ?";
		try {
			Collection col=super.select(sql, HashMap.class, new String[] {servId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						String block = (String) map.get("blockBooking");
						return block;
					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return "";
	}
	
	public String selectServiceId(String assignmentId) throws DaoException {
		Collection col = new ArrayList();
		String serviceId = "";

		String sql = "SELECT " +
		"serviceId " +
		"FROM fms_eng_assignment " + 
		"WHERE assignmentId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {assignmentId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						serviceId = (String) map.get("serviceId");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return serviceId;
	}

	public String selectServiceIdByRequestId(String requestId) throws DaoException {
		Collection col = new ArrayList();
		String serviceId = "";

		String sql = "SELECT " +
		"serviceId " +
		"FROM fms_eng_assignment " + 
		"WHERE requestId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {requestId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						serviceId = (String) map.get("serviceId");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return serviceId;
	}
	
	public String selectRequestId(String assignmentId) {
		Collection col = new ArrayList();
		String requestId = "";

		String sql = "SELECT requestId " +
		"FROM fms_eng_assignment " + 
		"WHERE assignmentId = ?";

		try {
			col = super.select(sql, HashMap.class, new String[] {assignmentId}, 0, 1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						requestId = (String) map.get("requestId");
					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return requestId;
	}
	
	public String selectRequestIdByGroupId(String groupId) {
		Collection col = new ArrayList();
		String requestId = "";

		String sql = "SELECT requestId " +
		"FROM fms_eng_assignment " + 
		"WHERE groupId = ? ";

		try {
			col = super.select(sql, HashMap.class, new String[] {groupId}, 0, 1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						requestId = (String) map.get("requestId");
					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return requestId;
	}

	public String selectRequestTitle(String requestId) throws DaoException {
		Collection col = new ArrayList();
		String requestTitle = "";

		String sql = "SELECT " +
		"title " +
		"FROM fms_eng_request " + 
		"WHERE requestId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {requestId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						requestTitle = (String) map.get("title");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return requestTitle;
	}

	public String selectRequestType(String requestId) throws DaoException {
		Collection col = new ArrayList();
		String requestType = "";

		String sql = "SELECT " +
		"requestType " +
		"FROM fms_eng_request " + 
		"WHERE requestId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {requestId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						requestType = (String) map.get("requestType");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return requestType;
	}

	public String selectServiceTitle(String serviceType) throws DaoException {
		Collection col = new ArrayList();
		String serviceTitle = "";

		String sql = "SELECT " +
		"title " +
		"FROM fms_eng_services " + 
		"WHERE serviceId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {serviceType} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						serviceTitle = (String) map.get("title");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return serviceTitle;
	}

	public String selectCurrentManpowerId(String assignmentId) throws DaoException {
		Collection col = new ArrayList();
		String manpowerId = "";

		String sql = "SELECT " +
		"userId " +
		"FROM fms_eng_assignment_manpower " + 
		"WHERE assignmentId =?";

		try {
			col = super.select(sql, HashMap.class, new String[] {assignmentId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						manpowerId = (String) map.get("userId");

					} catch (Exception e) {
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}

		return manpowerId;
	}

	public Collection selectGroupId(String requestId, String serviceId) throws DaoException {
		String sql = "SELECT distinct(groupId) AS groupId " +
		"FROM fms_eng_assignment " +
		"WHERE requestId = ? AND " +
		"serviceId = ? ";

		return super.select(sql, EngineeringRequest.class, new String[] {requestId, serviceId}, 0, -1);
	}

	protected String getOtherAssignmentId(String assignmentId, String groupId, String currentManpowerId) throws DaoException {
		String result = "";
		Map map =  new HashMap();
		String sql = "SELECT TOP 1 a.assignmentId AS assignmentId " +
		"FROM fms_eng_assignment a INNER JOIN fms_eng_assignment_manpower m ON (a.assignmentId = m.assignmentId) " +
		"WHERE a.groupId = ? AND (a.assignmentId = ? OR m.userId IS NULL OR m.userId = ?) ORDER BY m.userId DESC ";		

		Collection list = super.select(sql, HashMap.class, new Object[]{groupId, assignmentId, currentManpowerId}, 0, -1);

		if (list != null && list.size() > 0) {
			for(Iterator it = list.iterator(); it.hasNext(); ){
				map = (HashMap) it.next();		
				result = (String) map.get("assignmentId");
			}
		}

		return result;
	}

	public boolean isGlobalAssignmentExist(){
		boolean result=false;
		String sql="SELECT " +
		"id " +
		"FROM fms_global_assignment ";

		try {
			Collection col=super.select(sql, HashMap.class, null , 0, -1);

			if (col != null && col.size()>0){
				Iterator i = col.iterator();
				if (i.hasNext()) {
					result = true;
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	protected void insertGlobalAssignment(EngineeringRequest request) throws DaoException {
		String sql = "INSERT INTO fms_global_assignment " +
		"(id, refreshRate, noOfDays, footerMessage, createdBy, createdDate) " +
		"VALUES " +
		"(#globalAssignmentId#, #refreshRate#, #noOfDays#, #footerMessage#, #createdBy#, #createdOn#) ";
		super.update(sql, request);
	}

	protected void updateGlobalAssignment(EngineeringRequest request) throws DaoException {
		String sql = "UPDATE fms_global_assignment " +
		"SET refreshRate=#refreshRate#, noOfDays=#noOfDays#, footerMessage=#footerMessage#, " +
		"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#";
		super.update(sql, request);
	}

	protected void insertAssignmentSetting(int value, Date input){
		try {
			UuidGenerator.getInstance().getUuid();
			ArrayList args = new ArrayList();
			String sql = 
				"INSERT INTO fms_eng_assignment_setting (" +
				"settingId, settingValue, scheduledDate) " +
				"VALUES (?,?,?)";
			args.add(UuidGenerator.getInstance().getUuid());
			args.add(value);
			args.add(input);

			super.update(sql, args.toArray());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}

	protected void insertAutoAssignmentSetting(int value, Date input){
		try {
			UuidGenerator.getInstance().getUuid();
			ArrayList args = new ArrayList();
			String sql = 
				"INSERT INTO fms_eng_auto_assignment_setting (" +
				"settingId, settingValue, scheduledDate) " +
				"VALUES (?,?,?)";
			args.add(UuidGenerator.getInstance().getUuid());
			args.add(value);
			args.add(input);
			super.update(sql, args.toArray());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}

	protected void updateAssignmentSetting(String id, int value, Date scheduledDate){
		try {
			UuidGenerator.getInstance().getUuid();
			ArrayList args = new ArrayList();
			String sql = 
				"UPDATE fms_eng_assignment_setting " +
				"SET settingValue = ?, scheduledDate = ? " +
				"WHERE settingId = ?";
			args.add(value);
			args.add(scheduledDate);
			args.add(id);

			super.update(sql, args.toArray());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}

	protected void updateAutoAssignmentSetting(String id, int value, Date scheduledDate){
		try {
			UuidGenerator.getInstance().getUuid();
			ArrayList args = new ArrayList();
			String sql = 
				"UPDATE fms_eng_auto_assignment_setting " +
				"SET settingValue = ?, scheduledDate = ? " +
				"WHERE settingId = ?";
			args.add(value);
			args.add(scheduledDate);
			args.add(id);

			super.update(sql, args.toArray());
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
	}

	protected String searchAssignmentSetting(){
		try {
			String sql = 
				"SELECT settingId " +
				"FROM fms_eng_assignment_setting ";
			Collection col = super.select(sql, HashMap.class, null, 0, -1);
			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				String settingId = (String) map.get("settingId");
				return settingId;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected String searchAutoAssignmentSetting(){
		try {
			String sql = 
				"SELECT settingId " +
				"FROM fms_eng_auto_assignment_setting ";
			Collection col = super.select(sql, HashMap.class, null, 0, -1);
			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				String settingId = (String) map.get("settingId");
				return settingId;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return null;
	}

	protected int selectSettingValue(){
		try {
			String sql =
				"SELECT settingValue " +
				"FROM fms_eng_assignment_setting ";
			Collection col = super.select(sql, HashMap.class, null, 0, -1);
			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				int settingValue = ((Number) map.get("settingValue")).intValue();
				return settingValue;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return 0;
	}

	protected int selectAutoSettingValue(){
		try {
			String sql =
				"SELECT settingValue " +
				"FROM fms_eng_auto_assignment_setting ";
			Collection col = super.select(sql, HashMap.class, null, 0, -1);
			if (col!=null && col.size()==1){
				HashMap map = (HashMap) col.iterator().next();
				int settingValue = ((Number) map.get("settingValue")).intValue();
				return settingValue;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return 0;
	}

	public EngineeringRequest selectGlobalAssignment() throws DaoException {
		String sql = "SELECT TOP 1 id AS globalAssignmentId, refreshRate, noOfDays, footerMessage, createdBy, createdDate," +
		"modifiedBy, modifiedDate " +
		"FROM fms_global_assignment " +
		"ORDER BY createdDate DESC ";

		Collection col = super.select(sql, EngineeringRequest.class, null, 0, 1);
		if (col != null && col.size()>0){
			return (EngineeringRequest) col.iterator().next();
		}

		return null;
	}

	public Collection selectAssignmentInformation(Date startDate, Date endDate) throws DaoException{
		ArrayList params = new ArrayList();
		Date now = new Date();  
		Calendar cal = Calendar.getInstance();  
		cal.setTime(now);  

		cal.add(Calendar.DAY_OF_YEAR, 1);   
		Date tomorrow = cal.getTime();  

		cal.add(Calendar.DAY_OF_YEAR, 1);   
		Date afterTomorrow = cal.getTime();  

		// join with facility booking to choose the request with assignment type Manpower only
		String sql="SELECT DISTINCT(r.requestId), r.title, r.requiredFrom, r.requiredTo, prog.producer " +
		"FROM fms_eng_request r " +
		"INNER JOIN fms_facility_booking book ON (r.requestId = book.requestId) " +
		"LEFT JOIN fms_prog_setup prog ON (r.program = prog.programId) " +
		"WHERE 1=1 " +
		"AND  (r.status = '" + EngineeringModule.ASSIGNMENT_STATUS + "' " +
		"OR r.status = '" + EngineeringModule.OUTSOURCED_STATUS + "') " +
		"AND book.bookingType = '" + EngineeringModule.ASSIGNMENT_TYPE_MANPOWER + "' ";

		//// commented for development purpose
		if(startDate!=null && endDate!=null){
			//sql+=" AND (( dateadd(dd,0, datediff(dd,0, CONVERT(DATETIME,?,110))) between r.requiredFrom  AND r.requiredTo  ) " +
			//		"OR ( dateadd(dd,0, datediff(dd,0, CONVERT(DATETIME,?,110))) between r.requiredFrom AND r.requiredTo )) ";
			sql +=  " AND ((r.requiredFrom BETWEEN ? AND ?) " +		
			" OR (? BETWEEN r.requiredFrom AND r.requiredTo) " +
			" OR (? BETWEEN r.requiredFrom AND r.requiredTo) " +
			" OR (? BETWEEN r.requiredFrom AND r.requiredTo) " +
			" OR (r.requiredTo BETWEEN ? AND ?))";
			params.add(startDate);
			params.add(endDate);
			params.add(now);
			params.add(tomorrow);
			params.add(afterTomorrow);
			params.add(startDate);
			params.add(endDate);
		}
		//// end of commented for development purpose

		sql += " ORDER BY r.requiredFrom ASC, r.requestId ASC ";

		return super.select(sql, EngineeringRequest.class, params.toArray(), 0, -1);
	}

	public Collection getManpowerAssignments(String requestId, Date startDate, Date endDate){
		ArrayList params = new ArrayList();

		String sql="SELECT " +
		" DISTINCT(m.competencyId),competencyName, m.requiredFrom, m.requiredTo, m.fromTime, m.toTime " +
		" FROM fms_eng_assignment a " +
		" INNER JOIN fms_eng_assignment_manpower m on a.assignmentId=m.assignmentId " +
		" INNER JOIN competency c on c.competencyId=m.competencyId " +
		" LEFT JOIN security_user u on m.userId=u.id " +
		" where a.requestId=? ";
		params.add(requestId);

		//// commented for development purpose			
		if(startDate!=null && endDate!=null){
			//sql+=" AND (( dateadd(dd,0, datediff(dd,0, CONVERT(DATETIME,?,110))) between m.requiredFrom  AND m.requiredTo  ) " +
			//"OR ( dateadd(dd,0, datediff(dd,0, CONVERT(DATETIME,?,110))) between m.requiredFrom AND m.requiredTo )) ";
			sql += " AND ((m.requiredFrom BETWEEN ? AND ?) " +
			"OR (m.requiredTo BETWEEN ? AND ?))";
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
			params.add(endDate);
		}		
		//// end of commented for development purpose

		sql += "ORDER by requiredFrom ";

		Collection col=new ArrayList();
		try{
			col= super.select(sql, Assignment.class, params.toArray(), 0, -1);
			if (col != null && col.size()>0) {
				for (Iterator i = col.iterator(); i.hasNext();) {
					Assignment asg = (Assignment) i.next();

					String sql2="Select " +
					"DISTINCT(u.id), u.firstName + ' ' + u.lastName as assignedFacility " +
					"from fms_eng_assignment a " +
					"INNER JOIN fms_eng_assignment_manpower m on a.assignmentId=m.assignmentId " +
					"INNER JOIN competency c on c.competencyId=m.competencyId " +
					"LEFT JOIN security_user u on m.userId=u.id  " +
					"WHERE a.requestId = ? " +
					"AND m.competencyId = ? " +
					"AND m.requiredFrom = ? ";
					Collection userCol= super.select(sql2, Assignment.class, new Object[]{requestId, asg.getCompetencyId(),
						asg.getRequiredFrom()}, 0, -1);

					String assignee = "";
					if (userCol!= null && userCol.size()>0){
						for (Iterator x = userCol.iterator(); x.hasNext();) {
							Assignment asg2 = (Assignment) x.next(); 

							if (asg2.getAssignedFacility()!=null && !"".equals(asg2.getAssignedFacility())) {								

								if (!"".equals(assignee)) {
									assignee += ", " + asg2.getAssignedFacility();
								} else {
									assignee += asg2.getAssignedFacility();
								}
							}
						}

						asg.setAssignee(assignee);
					}
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return col;
	}

	public Collection selectAllRequestToRevalidate() throws DaoException{
		ArrayList params = new ArrayList();

		String sql = "SELECT DISTINCT(s.requestId) AS requestId " +
		"FROM fms_eng_request_services s INNER JOIN fms_eng_request r " +
		"ON (s.requestId = r.requestId) " +
		"WHERE (s.serviceId = " + ServiceAssignmentDetailsForm.SERVICE_POSTPRODUCTION + " " +
		"OR s.serviceId= " + ServiceAssignmentDetailsForm.SERVICE_VTR + " " +
		"OR s.serviceId = " + ServiceAssignmentDetailsForm.SERVICE_STUDIO + " " +
		"OR s.serviceId = " + ServiceAssignmentDetailsForm.SERVICE_TVRO + ") " +
		"AND  (r.status = '" + EngineeringModule.ASSIGNMENT_STATUS + "' " +
		"OR r.status = '" + EngineeringModule.OUTSOURCED_STATUS + "') ";		
		sql += " ORDER BY s.requestId ASC ";

		return super.select(sql, EngineeringRequest.class, params.toArray(), 0, -1);
	}

	//start added new method for CR# 158
	protected void updatePrepareCheckOutEquipment(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_assignment_equipment SET " +
		"barcode=#barcode#, prepareCheckOutBy=#prepareCheckOutBy#, prepareCheckOutDate=#prepareCheckOutDate#, status=#status#, " +
		"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#, takenBy=#takenBy#, preparedBy=#preparedBy#, assignmentLocation=#assignmentLocation# " +
		"WHERE id=#assignmentEquipmentId# ";
		super.update(sql,request);
	}


	protected void insertPrepareCheckOutEquipmentByGroup(EngineeringRequest request) throws DaoException {
		String sql = "INSERT INTO fms_eng_assignment_equipment " +
		"(id, assignmentId, requiredFrom, requiredTo, fromTime, toTime, barcode, " +
		"prepareCheckOutBy, prepareCheckOutDate, status, createdBy, createdDate, " +
		"modifiedBy, modifiedDate,takenBy, preparedBy, assignmentLocation, groupId, rateCardCategoryId) " +
		"VALUES " +
		"(#assignmentEquipmentId#, '-', #requiredFrom#, #requiredTo#, #fromTime#, #toTime#, #barcode#, " +
		"#prepareCheckOutBy#, #prepareCheckOutDate#, #status#, #createdBy#, #createdOn#, " +
		"#modifiedBy#, #modifiedOn#, #takenBy#, #preparedBy#, #assignmentLocation#, #groupId#, #rateCardCategoryId#) ";
		super.update(sql, request);
	}



	public Collection getGroupIdsList(String requestId) throws DaoException{
		String sql="select distinct groupId as groupId "
			+ "from fms_eng_assignment where requestId=?";
		Collection list= super.select(sql, HashMap.class, new String[]{requestId}, 0, -1);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}


	protected boolean updateExtraPrepareCheckOutEquipment(EngineeringRequest request, String status)  throws DaoException{
		String sql="";
		if(status.equals(EngineeringModule.ACTION_PREPARE_CHECKOUT)){
			sql="update fms_eng_assignment_equipment SET " +
			"barcode=#barcode#, prepareCheckOutBy=#prepareCheckOutBy#, prepareCheckOutDate=#prepareCheckOutDate#, status=#status#, " +
			"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#, takenBy=#takenBy#, preparedBy=#preparedBy#, " +
			"assignmentLocation=#assignmentLocation# " +
			"WHERE  id=#assignmentEquipmentId# and assignmentId='-' and groupId=#groupId#";
		}else{
			sql="update fms_eng_assignment_equipment SET " +
			"barcode=#barcode#, checkedOutBy=#checkedOutBy#, checkedOutDate=#checkedOutDate#, status=#status#, " +
			"modifiedBy=#modifiedBy#, modifiedDate=#modifiedOn#, takenBy=#takenBy#, preparedBy=#preparedBy#, " +
			"assignmentLocation=#assignmentLocation# " +
			"WHERE id=#assignmentEquipmentId# and  assignmentId='-' and groupId=#groupId#";
		}
		int update=super.update(sql,request);
		if(update>0){
			return true; 
		}else{
			return false;
		}
	}


	public Collection getEquipmentListFromRequestId(String requestId, String rateCardCategoryId, boolean today) throws DaoException{
		String sql ="SELECT distinct ae.id AS assignmentEquipmentId, ae.assignmentId, ae.rateCardCategoryId, a.groupId, a.requestId " +
		"FROM fms_eng_assignment_equipment ae " +
		"left join fms_eng_assignment a on a.groupId = ae.groupId " +
		"WHERE a.requestId=? " +
		"AND ae.rateCardCategoryId=? AND (ae.status=? OR ae.status=?) and ae.assignmentId not like '-' " ;
		Collection list = new ArrayList();
		ArrayList params = new ArrayList();
		params.add(requestId);
		params.add(rateCardCategoryId);
		params.add(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW);
		params.add(EngineeringModule.ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT);
		if(today){
			sql+="AND ? BETWEEN ae.requiredFrom AND ae.requiredTo";
			params.add(DateUtil.getToday());

		}

		list = super.select(sql, EngineeringRequest.class, params.toArray(), 0, -1);

		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}

	}

	public Collection getExtraEquipmentListFromRequestId(String requestId, String barcode, String rateCardCategoryId) throws DaoException{
		String sql="select distinct e.id AS assignmentEquipmentId, e.assignmentId, e.rateCardCategoryId, a.groupId, a.requestId " +
		"from fms_eng_assignment a  " +
		"left join fms_eng_assignment_equipment e on e.groupId=a.groupId " +
		"where a.requestId=? " +
		"and e.assignmentId='-' " +
		"AND (e.status=? OR e.status=?) ";

		if(barcode!=null && !barcode.equals("")){
			sql+=" AND e.barcode='"+barcode+"' ";
		}
		if(rateCardCategoryId!=null && !rateCardCategoryId.equals("")){
			sql+=" AND e.rateCardCategoryId='"+rateCardCategoryId+"' ";
		}
		Collection list= super.select(sql, EngineeringRequest.class, new Object[]{requestId, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_NEW, EngineeringModule.ASSIGNMENT_FACILITY_STATUS_PREPARE_CHECKOUT}, 0, -1);
		if(list!=null && list.size()>0){
			return list;
		}else{
			return null;
		}
	}

	public EngineeringRequest selectTopGroupId(String requestId) throws DaoException{
		String sql="select TOP 1 groupId as groupId "
			+ "from fms_eng_assignment where requestId=? AND assignmentType = 'F'";

		Collection col = super.select(sql, EngineeringRequest.class, new String[]{requestId}, 0, -1);
		if (col != null && col.size()>0){
			return (EngineeringRequest) col.iterator().next();
		}

		return null;
	}

	public void updateCheckoutFlag(EngineeringRequest request)  throws DaoException{
		String sql="Update fms_eng_request set title=#title#, requestType=#requestType#, clientName=#clientName#," +
		"programType=#programType#, program=#program#, description=#description#, requiredFrom=#requiredFrom#,requiredTo=#requiredTo#," +
		" modifiedBy=#modifiedBy#, modifiedOn=#modifiedOn#,state=#state#, checkoutFlag=#checkoutFlag# where requestId=#requestId#";
		super.update(sql,request);
	}
	//end added new method for CR# 158

	public Collection getRequestServices(String requestId) throws DaoException{
		String sql="Select s.serviceId,s.title,rs.requestId,rs.feedType from fms_eng_services s" +
		" Inner join fms_eng_request_services rs on s.serviceId=rs.serviceId where rs.requestId=? order By s.serviceId";
		return super.select(sql, Service.class, new String[]{requestId}, 0, -1);
	}

	//Added for bug in PIC name
	public String getPICFullName(String userId) throws DaoException{
		String sql = 
			"SELECT (firstName + ' ' +lastName) as fullName FROM security_user " +
			"WHERE id = ? ";
		Collection col = super.select(sql, HashMap.class, new String[]{userId}, 0, 1);
		if(col!=null && col.size()>0) {
			HashMap hm = (HashMap) col.iterator().next();
			String fullName = (String) hm.get("fullName");
			return fullName;
		}
		return null;
	}

	protected EngineeringRequest selectParticularAssignment(String assignmentId, String assignmentType){
		String field = "";
		String join = "";
		if (assignmentType.equals("M")){
			field = ", m.* ";
			join = "INNER JOIN fms_eng_assignment_manpower m ON a.assignmentId=m.assignmentId ";
		}else {
			field = ", e.* ";
			join = "INNER JOIN fms_eng_assignment_equipment e ON a.assignmentId=e.assignmentId";
		}
		try {
			String sql  = 
				"SELECT a.assignmentId, a.code as assignmentCode, a.requestId, " +
				"a.serviceType, a.assignmentType " +field+
				" FROM fms_eng_assignment a " +join+
				" WHERE a.assignmentId = ? ";

			Collection col = super.select(sql, EngineeringRequest.class, new String[] {assignmentId}, 0, 1);
			if (col != null && col.size()==1){
				return (EngineeringRequest) col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	
	protected EngineeringRequest selectOneAssignmentFacility(String assignmentId, String assignmentType, String facilityId) {
		String joinTable;
		if (assignmentType.equals(EngineeringModule.ASSIGNMENT_TYPE_MANPOWER)) {
			joinTable = "fms_eng_assignment_manpower";
		} else {
			joinTable = "fms_eng_assignment_equipment";
		}
		
		try {
			String sql  = 
				"SELECT a.assignmentId, a.code as assignmentCode, a.requestId, a.serviceType, a.assignmentType, e.* " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN " + joinTable + " e ON (a.assignmentId = e.assignmentId) " +
				"WHERE a.assignmentId = ? ";
			
			Collection col = super.select(sql, EngineeringRequest.class, new String[] {assignmentId}, 0, 1);
			if (col != null && col.size() == 1) {
				return (EngineeringRequest) col.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	//	protected EngineeringRequest selectManpowerRecord(String assignmentId){
	//		try {
	//			String sql = 
	//				"SELECT * " +
	//				"from fms_eng_assignment_manpower " +
	//				"where assignmentId = ?";
	//			
	//			Collection col = super.select(sql, EngineeringRequest.class, new String[] {assignmentId}, 0, 1);
	//			if (col!= null && col.size()==1){
	//				return (EngineeringRequest) col.iterator().next();
	//			}
	//		} catch (DaoException e) {
	//			Log.getLog(getClass()).error(e.toString(), e);
	//		}
	//		return null;
	//	}
	//	protected EngineeringRequest selectEquipmentRecord(String assignmentId){
	//		try {
	//			String sql = 
	//				"SELECT * " +
	//				"from fms_eng_assignment_equipment " +
	//				"where assignmentId = ?";
	//			
	//			Collection col = super.select(sql, EngineeringRequest.class, new String[] {assignmentId}, 0, 1);
	//			if (col!= null && col.size()==1){
	//				return (EngineeringRequest) col.iterator().next();
	//			}
	//		} catch (DaoException e) {
	//			Log.getLog(getClass()).error(e.toString(), e);
	//		}
	//		return null;
	//	}
	protected void insertCancelLog(EngineeringRequest engine) throws DaoException {
		String sql = 
			"INSERT INTO fms_eng_cancel_service_log (" +
			"assignmentId, code, requestId, serviceType, assignmentType, rateCardId, competencyId, rateCardCategoryId, requiredFrom, requiredTo, fromTime, toTime, userId, " +
			"barcode, checkedOutBy, checkedOutDate, checkedInBy, checkedInDate, status, createdBy, createdDate, " +
			"completionDate, remarks, reasonUnfulfilled, chargeBack, callBack, modifiedBy, modifiedDate, takenBy, " +
			"preparedBy, assignmentLocation, groupId, damage, prepareCheckOutBy, prepareCheckOutDate, cancelBy, cancelDate, " +
			"systemCalculatedCharges,flagMainCharges) " +
			"VALUES (" +
			"#assignmentId#, #assignmentCode#, #requestId#, #serviceType#, #assignmentType#, #rateCardId#, #competencyId#, #rateCardCategoryId#, #requiredFrom#, #requiredTo#, #fromTime#, #toTime#, #userId#, " +
			"#barcode#, #checkedOutBy#, #checkedOutDate#, #checkedInBy#, #checkedInDate#, #status#, #createdBy#, #createdDate#, " +
			"#completionDate#, #remarks#, #reasonUnfulfilled#, #chargeBack#, #callBack#, #modifiedBy#, #modifiedDate#, #takenBy#, " +
			"#preparedBy#, #assignmentLocation#, #groupId#, #damage#, #prepareCheckOutBy#, #prepareCheckOutDate#, #cancelBy#, #cancelDate#, " +
			"#systemCalculatedCharges#,#flagMainCharges#) ";

		super.update(sql, engine);
	}

	protected Collection selectCancelRecord(String requestId, String assignmentType){
		String field = "";
		String join = "";
		String orderBy = "";
		if (assignmentType.equals("M")){
			field = ", c.unitId, c.competencyName ";
			orderBy = ", c.unitId, c.competencyName ";
			join = "INNER JOIN competency c on c.competencyId=l.competencyId ";
		}else {
			field = ", e.rateCardCategoryId, c.name as rateCardCategoryName,e.checkedInDate ";
			orderBy = ", e.rateCardCategoryId, c.name,e.checkedInDate ";
			join = " INNER JOIN fms_eng_assignment_equipment e on l.assignmentId=e.assignmentId " +
			" INNER JOIN fms_eng_rate_card_category c on c.id=e.rateCardCategoryId ";
		}
		try {
			String sql = 
				"SELECT distinct(l.assignmentId), l.code, l.requestId, l.serviceType, l.assignmentType, " +
				"l.rateCardId, l.competencyId, l.rateCardCategoryId, l.requiredFrom, l.requiredTo, l.fromTime, l.toTime, " +
				"l.userId, l.barcode, l.checkedOutBy, l.checkedOutDate, l.checkedInDate, l.status, l.CreatedBy, l.createdDate, " +
				"l.completionDate, l.chargeBack, l.callBack, l.modifiedBy, l.modifiedDate, " +
				"l.takenBy, l.preparedBy, l.assignmentLocation, l.groupId, l.damage, l.prepareCheckOutBy, l.prepareCheckOutDate, " +
				"l.cancelBy, l.cancelDate, l.systemCalculatedCharges, l.flagMainCharges " +field+
				" FROM fms_eng_cancel_service_log l " +join+
				" WHERE requestId = ? AND assignmentType = ? " +
				"ORDER BY l.assignmentId, l.code, l.requestId, l.serviceType, l.assignmentType, " +
				"l.rateCardId, l.competencyId, l.rateCardCategoryId, l.requiredFrom, l.requiredTo, l.fromTime, l.toTime, " +
				"l.userId, l.barcode, l.checkedOutBy, l.checkedOutDate, l.checkedInDate, l.status, l.CreatedBy, l.createdDate, " +
				"l.completionDate, l.chargeBack, l.callBack, l.modifiedBy, l.modifiedDate, " +
				"l.takenBy, l.preparedBy, l.assignmentLocation, l.groupId, l.damage, l.prepareCheckOutBy, l.prepareCheckOutDate, " +
				"l.cancelBy, l.cancelDate, l.systemCalculatedCharges, l.flagMainCharges "+orderBy;

			return super.select(sql, Assignment.class, new String[] {requestId, assignmentType}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	protected void decreaseQuantityService(String serviceId){
		try {
			String sql = 
				"Update fms_eng_service_manpower " +
				"SET quantity = quantity - 1 " +
				"WHERE id = ?";

			super.update(sql, new String[] {serviceId});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	protected Collection selectAssignmentByServiceId(String serviceId){
		try {
			String sql = 
				"SELECT * " +
				"FROM fms_eng_assignment " +
				"where serviceId = ?";

			return super.select(sql, EngineeringRequest.class, new String[] {serviceId}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	protected Collection selectAssignmentByServiceIdTopQuantity(String serviceId, int qty){
		try {
			String sql = 
				"SELECT top "+qty+" * " +
				"FROM fms_eng_assignment " +
				"where serviceId = ? order by a.code desc ";

			return super.select(sql, EngineeringRequest.class, new String[] {serviceId}, 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	protected int countAssignmentByServiceId(String serviceId) {
		try {
			String sql =
				"SELECT count(assignmentId) as total " +
				"FROM fms_eng_assignment  " + 
				"WHERE serviceId = ? ";
			
			Collection col = super.select(sql, HashMap.class, new String[]{serviceId}, 0, -1);

			if (col!= null && col.size()>0){
				HashMap map = (HashMap) col.iterator().next();

				int total = (Integer) map.get("total");
				return total;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return 0;
	}
	
	protected Collection selectAssignmentByRequestIdAndServiceType(String requestId, Collection servType){
		try {
			ArrayList args = new ArrayList();

			String sql = 
				"SELECT * " +
				"FROM fms_eng_assignment " +
				"where requestId = ? ";
			args.add(requestId);
			
			if (servType.size() > 0) {
				sql += " and ( ";
			
				for (Iterator iterator = servType.iterator(); iterator.hasNext();) {
					String srvId= (String)iterator.next();
					if(!iterator.hasNext())
						sql+=" serviceType= ? ";
					else
						sql+=" serviceType= ? OR ";
	
					args.add(srvId);
				}
				sql+= " ) ";
			}
			return super.select(sql, EngineeringRequest.class, args.toArray(), 0, -1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	protected void deleteService(String serviceId, String requestId, String serviceType){
		String tableName="";
		
		if (serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP))
			tableName = "fms_eng_service_scp";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION))
			tableName = "fms_eng_service_postproduction";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_OTHER))
			tableName = "fms_eng_service_other";			
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_STUDIO))
			tableName = "fms_eng_service_studio";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_VTR))
			tableName = "fms_eng_service_vtr";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			tableName = "fms_eng_service_manpower";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_TVRO))
			tableName = "fms_eng_service_tvro";

		try {
			// logging
			TransLogModule transLog = (TransLogModule) Application.getInstance().getModule(TransLogModule.class);
			transLog.info(requestId, "SERVICE_DELETE", "tableName=" + tableName + " id=" + serviceId);
			
			String sql = 
				"DELETE FROM "+tableName+ " " +
				"WHERE id = ? AND requestId = ? ";

			super.update(sql, new String[] {serviceId, requestId});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	public String selectSystemCalculatedCharges(String requestId, String assignmentId, String reqType, String serviceType) throws DaoException {
		Collection col = new ArrayList();
		Number charges = 0;
		String join="";
		String formattedCharges="";

		if(serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			join = " fms_eng_assignment_manpower ";
		else
			join = " fms_eng_assignment_equipment";

		String sql = "select top 1 rcd.internalRate as internalRate, rcd.externalRate as externalRate " +
		" from fms_eng_assignment ea " +
		" inner join "+join+" esm on esm.assignmentId=ea.assignmentId " +
		" inner join fms_rate_card_detail rcd on rcd.rateCardId=esm.ratecardId " +
		" where ea.requestId=? and ea.assignmentId=? " +
		" ORDER BY rcd.effectiveDate DESC, rcd.createdOn DESC ";

		try {
			col = super.select(sql, HashMap.class, new String[] {requestId,assignmentId} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();

						if(reqType.equals(EngineeringModule.REQUEST_TYPE_INTERNAL) || reqType.equals(EngineeringModule.REQUEST_TYPE_NONPROGRAM))
							charges = (Number) map.get("internalRate");
						else
							charges = (Number) map.get("externalRate");

					} catch (Exception e) {
						Log.getLog(getClass()).error(e.toString(), e);
					}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		formattedCharges = charges.toString();
		return formattedCharges;
	}
	
	public Collection selectFacilityByAssignment(String requestId, String assignmentId) {
		String sql =
				"SELECT ea.assignmentId, ea.serviceType, esm.rateCardId, requiredFrom, requiredTo, fromTime, toTime, " +
				"       requiredTo, status, esm.rateCardCategoryId as facilityId, ea.assignmentType " +
				"FROM fms_eng_assignment ea " +
				"INNER JOIN fms_eng_assignment_equipment esm ON (esm.assignmentId = ea.assignmentId) " +
				"WHERE ea.requestId = ? " +
				"AND ea.assignmentId = ? " +
					"UNION ALL " +
				"SELECT ea.assignmentId, ea.serviceType, esm.rateCardId, requiredFrom, requiredTo, fromTime, toTime, " +
				"       requiredTo, status, esm.competencyId as facilityId, ea.assignmentType " +
				"FROM fms_eng_assignment ea " +
				"INNER JOIN fms_eng_assignment_manpower esm ON (esm.assignmentId = ea.assignmentId) " +
				"WHERE ea.requestId = ? " +
				"AND ea.assignmentId = ? ";
		
		try {
			Collection col = super.select(sql, EngineeringRequest.class, 
					new Object[] {
						requestId, assignmentId,
						requestId, assignmentId
					}, 0, -1);
			return col;
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
			return null;
		}
	}

	public boolean isOutsource(String requestId){
		boolean result=false;
		String sql="select id from fms_eng_request_outsource where requestId=?";
		try {
			Collection col=super.select(sql, HashMap.class, new String[] {requestId} , 0, -1);
			if(col.size()>0){
				result=true;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return result;
	}

	protected Collection getRequestList(String keyword, String userName, String sort, boolean desc, int startIndex,int maxRows){
		ArrayList args = new ArrayList();
		try {
			String sql = 
				"SELECT r.requestId, r.title, p.programName, r.requiredFrom, r.requiredTo " +
				"FROM fms_eng_request r " +
				"INNER JOIN fms_prog_setup p " +
				"ON r.program = p.programId " +
				"WHERE r.status not in('D','H') ";

			if(userName!=null && !userName.equals("")){
				sql += "AND r.createdBy = ? ";
				args.add(userName);
			}

			if(keyword!=null && !keyword.equals("")){
				sql += "AND (r.title LIKE '%"+keyword+"%' OR p.programName LIKE '%"+keyword+"%') ";
			}

			if (sort != null) {
				sql += " ORDER BY " + sort;
				if (desc)
					sql += " DESC";
			}else 
				sql += " ORDER BY r.title ASC";

			return super.select(sql, EngineeringRequest.class, args.toArray(), startIndex, maxRows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return null;
	}

	protected int countRequestList(String keyword, String userName) {
		ArrayList args = new ArrayList();
		try {
			String sql =
				"SELECT count(r.requestId) as total " +
				"FROM fms_eng_request r " + 
				"INNER JOIN fms_prog_setup p " + 
				"ON r.program = p.programId " +
				"WHERE r.status not in('D','H') ";

			if(userName!=null && !userName.equals("")){
				sql += "AND r.createdBy = ? ";
				args.add(userName);
			}

			if(keyword!=null && !keyword.equals("")){
				sql += "AND (r.title LIKE '%"+keyword+"%' OR p.programName LIKE '%"+keyword+"%') ";
			}

			Collection col = super.select(sql, HashMap.class, args.toArray(), 0, -1);

			if (col!= null && col.size()>0){
				HashMap map = (HashMap) col.iterator().next();

				int total = (Integer) map.get("total");
				return total;
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return 0;
	}

	public void deleteBookingByManpower(String requestId, String bookingType){
		try {
			String sql=" DELETE FROM fms_facility_booking " +
			"where requestId=? and bookingType=? ";

			super.update(sql,new String[] {requestId,bookingType});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}

	protected Collection collectBlockBookingServices(String serviceTable){
		try {
			String facilityField = "";
			String selectFromField = "";

			if(serviceTable.equals("manpower")){
				facilityField = "p.competencyId ";
				selectFromField = 
					"p.id,p.requestId,p.serviceId,p.competencyId,p.quantity, " +
					"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime, " +
					"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as competencyName, " +
					"p.internalRate, p.externalRate, " +
					"p.blockBooking, p.location,p.submitted ";

			}else {
				facilityField = "p.facilityId ";
				if(serviceTable.equals("scp")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId, " +
						"p.requiredFrom,p.requiredTo,p.departureTime,p.location,p.segment, " +
						"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.submitted  ";
				
				}else if (serviceTable.equals("postproduction")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.requiredDate,p.fromTime, " +
						"p.toTime,p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.submitted, p.requiredDateTo, p.location ";

				}else if(serviceTable.equals("studio")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate,p.bookingDateTo, " +
						"p.requiredFrom,p.requiredTo,p.segment, " +
						"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, " +
						"p.blockBooking, p.location,p.submitted ";

				}else if(serviceTable.equals("other")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.quantity, " +
						"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, " +
						"p.blockBooking, p.location, p.submitted ";

				}else if (serviceTable.equals("vtr")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.service, " +
						"p.requiredDate,p.requiredDateTo,p.requiredFrom,p.requiredTo,p.formatFrom,p.formatTo, " +
						"p.conversionFrom,p.conversionTo,p.duration,p.noOfCopies,p.remarks, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate, " +
						"p.facilityId, f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.location, p.submitted ";

				}else if(serviceTable.equals("tvro")){
					selectFromField =
						"p.id,p.requestId,p.serviceId,p.feedTitle,p.location, " +
						"p.requiredDate,p.requiredDateTo,p.timezone,p.totalTimeReq,p.timeMeasure,p.remarks,p.fromTime,p.toTime, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.feedType, " +
						"p.internalRate, p.externalRate, " +
						"p.facilityId, p.blockBooking, p.submitted  ";
				}
			}


			String sql =
				"SELECT " +selectFromField +
				"from fms_eng_service_"+serviceTable+" p " +
				"INNER JOIN fms_rate_card f on f.id="+facilityField+" " +
				"WHERE p.blockBooking = '1' ";

			if(serviceTable.equals("manpower")){
				return super.select(sql, ManpowerService.class, null, 0, -1);
			}else if(serviceTable.equals("scp")){
				return super.select(sql, ScpService.class, null, 0, -1);
			}else if (serviceTable.equals("postproduction")){
				return super.select(sql, PostProductionService.class, null, 0, -1);
			}else if(serviceTable.equals("studio")){
				return super.select(sql, StudioService.class, null, 0, -1);
			}else if(serviceTable.equals("other")){
				return super.select(sql, OtherService.class, null, 0, -1);
			}else if (serviceTable.equals("vtr")){
				return super.select(sql, VtrService.class, null, 0, -1);
			}else if(serviceTable.equals("tvro")){
				return super.select(sql, TvroService.class, null, 0, -1);
			}

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}

	protected Collection collectNonBlockBookingInParticularRequest(String requestId, String facilityId, String serviceTable){
		ArrayList args = new ArrayList();
		try {
			String facilityField = "";
			String selectFromField = "";

			if(serviceTable.equals("manpower")){
				facilityField = "p.competencyId ";
				selectFromField = 
					"p.id,p.requestId,p.serviceId,p.competencyId,p.quantity, " +
					"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime, " +
					"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as competencyName, " +
					"p.internalRate, p.externalRate, " +
					"p.blockBooking, p.location,p.submitted ";

			}else {
				facilityField = "p.facilityId ";
				if(serviceTable.equals("scp")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId, " +
						"p.requiredFrom,p.requiredTo,p.departureTime,p.location,p.segment, " +
						"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.recordingFrom,p.recordingTo, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.submitted  ";
				
				}else if (serviceTable.equals("postproduction")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.requiredDate,p.fromTime, " +
						"p.toTime,p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.submitted, p.requiredDateTo, p.location ";

				}else if(serviceTable.equals("studio")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.bookingDate,p.bookingDateTo, " +
						"p.requiredFrom,p.requiredTo,p.segment, " +
						"p.settingFrom,p.settingTo,p.rehearsalFrom,p.rehearsalTo,p.vtrFrom,p.vtrTo, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, " +
						"p.blockBooking, p.location,p.submitted ";

				}else if(serviceTable.equals("other")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.facilityId,p.quantity, " +
						"p.requiredFrom,p.requiredTo,p.remarks,p.fromTime,p.toTime, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,f.name as facility, " +
						"p.internalRate, p.externalRate, " +
						"p.blockBooking, p.location, p.submitted ";

				}else if (serviceTable.equals("vtr")){
					selectFromField = 
						"p.id,p.requestId,p.serviceId,p.service, " +
						"p.requiredDate,p.requiredDateTo,p.requiredFrom,p.requiredTo,p.formatFrom,p.formatTo, " +
						"p.conversionFrom,p.conversionTo,p.duration,p.noOfCopies,p.remarks, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate, " +
						"p.facilityId, f.name as facility, " +
						"p.internalRate, p.externalRate, p.blockBooking, p.location, p.submitted ";

				}else if(serviceTable.equals("tvro")){
					selectFromField =
						"p.id,p.requestId,p.serviceId,p.feedTitle,p.location, " +
						"p.requiredDate,p.requiredDateTo,p.timezone,p.totalTimeReq,p.timeMeasure,p.remarks,p.fromTime,p.toTime, " +
						"p.createdBy,p.createdDate,p.modifiedBy,p.modifiedDate,p.feedType, " +
						"p.internalRate, p.externalRate, " +
						"p.facilityId, p.blockBooking, p.submitted  ";
				}
			}

			String sql =
				"SELECT " +selectFromField +
				"from fms_eng_service_"+serviceTable+" p " +
				"INNER JOIN fms_rate_card f on f.id="+facilityField+" " +
				"WHERE p.blockBooking = '0' ";

			if(requestId!=null && !requestId.equals("")){
				sql += "AND p.requestId = ? ";
				args.add(requestId);
			}
			if(facilityId!=null && !facilityId.equals("")){
				sql += "AND "+facilityField+" = ? ";
				args.add(facilityId);
			}

			if(serviceTable.equals("manpower")){
				return super.select(sql, ManpowerService.class, args.toArray(), 0, -1);
			}else if(serviceTable.equals("scp")){
				return super.select(sql, ScpService.class, args.toArray(), 0, -1);
			}else if (serviceTable.equals("postproduction")){
				return super.select(sql, PostProductionService.class, args.toArray(), 0, -1);
			}else if(serviceTable.equals("studio")){
				return super.select(sql, StudioService.class, args.toArray(), 0, -1);
			}else if(serviceTable.equals("other")){
				return super.select(sql, OtherService.class, args.toArray(), 0, -1);
			}else if (serviceTable.equals("vtr")){
				return super.select(sql, VtrService.class, args.toArray(), 0, -1);
			}else if(serviceTable.equals("tvro")){
				return super.select(sql, TvroService.class, args.toArray(), 0, -1);
			}

		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	
	protected void deleteFacilityBooking(String requestId, String facilityId, Date bookFrom, Date bookTo){
		try {
			String sql =
				"DELETE FROM fms_facility_booking " +
				"WHERE requestId = ? " +
				"AND facilityId = ? " +
				"AND bookFrom = ? " +
				"AND bookTo = ? ";
			
			super.update(sql, new Object[] {requestId, facilityId, bookFrom, bookTo});
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	
	protected Collection collectAssignment(String requestId, String seviceId, Date requiredFrom, Date requiredTo, String type){
		ArrayList args = new ArrayList();
		
		try {
			String sql =
				"SELECT a.requestId, t.requiredFrom, t.requiredTo " +
				"FROM fms_eng_assignment a " +
				"INNER JOIN fms_eng_assignment_"+type+" t ON t.assignmentId = a.assignmentId " +
				"WHERE 1=1 ";
			
			if(requestId!=null && !requestId.equals("")){
				sql += 
					"AND a.requestId = ? ";
				args.add(requestId);
			}
			if(seviceId!=null && !seviceId.equals("")){
				sql += 
					"AND a.serviceId = ? ";
				args.add(seviceId);
			}
			if(requiredFrom!=null){
				sql += 
					"AND t.requiredFrom = ? ";
				args.add(requiredFrom);
			}
			if(requiredTo!=null){
				sql += 
					"AND t.requiredTo = ? ";
				args.add(requiredTo);
			}
			
			return super.select(sql, EngineeringRequest.class, args.toArray(), 0, 1);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	
	protected int countDeletedFacilityBooking(String requestId, String facilityId, Date bookFrom, Date bookTo){
		try {
			String sql =
				"SELECT count(*) as total " +
				"FROM fms_facility_booking " +
				"WHERE requestId = ? " +
				"AND facilityId = ? " +
				"AND bookFrom = ? " +
				"AND bookTo = ? ";
			
			Collection col = super.select(sql, HashMap.class, new Object[] {requestId, facilityId, bookFrom, bookTo}, 0, -1);
			if(col!=null && col.size()>0){
				HashMap map = (HashMap) col.iterator().next();
				return ((Number) map.get("total")).intValue();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return 0;
	}
	
	public Collection getAssignments(String requestId, String facilityId)throws DaoException{
		
		String sql = "SELECT distinct a.assignmentId, e.rateCardCategoryId, a.requestId, requiredFrom, requiredTo, fromTime, toTime,e.createdBy,e.createdDate, "+
					"rce.quantity from fms_eng_assignment a  "+
					"INNER JOIN fms_eng_assignment_equipment e on a.assignmentId=e.assignmentId "+ 
					"INNER JOIN fms_rate_card_equipment rce on rce.equipment = rateCardCategoryId "+
					"WHERE requestId=? AND rateCardCategoryId=? ";	
		
		return super.select(sql, HashMap.class, new String[]{requestId, facilityId}, 0, -1);
	
	}
	
	public boolean isTRExist(String engRequestId)
	throws DaoException {
		boolean exist=false;
		
		Collection result = super.select(
				"SELECT * FROM fms_tran_request WHERE engineeringRequestId=?",
				TransportRequest.class, new Object[] { engRequestId }, 0, -1);
		if (result.size() > 0) {
			exist= true;
		}
		
		return exist;
	}
	
	public Collection getRequestIdLinkToTR(String serviceType){
		String tableName="";
		Collection requests = new ArrayList();
		if (serviceType.equals(ServiceDetailsForm.SERVICE_SCPMCP))
			tableName = "fms_eng_service_scp";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_POSTPRODUCTION))
			tableName = "fms_eng_service_postproduction";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_OTHER))
			tableName = "fms_eng_service_other";			
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_STUDIO))
			tableName = "fms_eng_service_studio";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_VTR))
			tableName = "fms_eng_service_vtr";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_MANPOWER))
			tableName = "fms_eng_service_manpower";
		else if (serviceType.equals(ServiceDetailsForm.SERVICE_TVRO))
			tableName = "fms_eng_service_tvro";

		try {
			Collection result = super.select(
				"Select DISTINCT p.requestId from "+tableName+" p " +
				" INNER JOIN fms_rate_card f on f.id=p.facilityId " +
				" INNER JOIN fms_eng_request r on r.requestId=p.requestId " +
				" where f.transportRequest='1' and r.status='A' " +
				" order By p.requestId desc ",
					HashMap.class, null, 0, -1);	
			
			for (Iterator iterator = result.iterator(); iterator.hasNext();) {
				try {
					HashMap map = (HashMap) iterator.next();
					String i = (String) map.get("requestId");
					requests.add(i);
				} catch (Exception e) {
				}
			}
			
			return requests;
			
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		return null;
	}
	
	public Collection getDefectedUnitHeadRequest(String requestId)throws DaoException{
		
		String sql = "SELECT ru.requestId, ru.rateCardId AS facilityId, r.title AS requestTitle, rc.name FROM fms_eng_request_unit ru " +
				" INNER JOIN fms_eng_request r on r.requestId=ru.requestId " +
				" INNER JOIN fms_rate_card rc on rc.id=ru.rateCardId " +
				" where ru.requestId=? and ru. serviceId  " +
				" not in (select serviceId from fms_eng_request_services where requestId =?) ";	
		
		return super.select(sql, FacilityObject.class, new String[]{requestId, requestId}, 0, -1);
		
	}
	
	/** for abw integration ********************************************/
	public DefaultDataObject selectGlobalSetup(String taskId) throws DaoException {
		
		String sql = "SELECT id, taskId, scheduleTime1, scheduleTime2, scheduleTime3, createdDate, createdBy, modifiedDate, modifiedBy " +
				" FROM fms_global_setup WHERE taskId = ? ";	
		
		Collection col = super.select(sql, DefaultDataObject.class, new String[]{taskId}, 0, 1);
		if (col!= null && col.size()>0){
			DefaultDataObject obj = (DefaultDataObject) col.iterator().next();

			return obj;
		}else{
			return new DefaultDataObject();
		}
		
	}
	
	
	public void insertGlobalSetup(DefaultDataObject obj) throws DaoException {
		String sql = "INSERT INTO fms_global_setup " +
			" (id, taskId, scheduleTime1, scheduleTime2, scheduleTime3, createdDate, createdBy ) " +
			" VALUES " +
			" (#id#, #taskId#, #scheduleTime1#, #scheduleTime2#, #scheduleTime3#,#createdDate#, #createdBy#)";
		
		super.update(sql, obj);
		
	}

	public void updateGlobal(DefaultDataObject obj) throws DaoException {
		String sql = "UPDATE fms_global_setup SET " +				
				"scheduleTime1 = #scheduleTime1#, " +
				"scheduleTime2 = #scheduleTime2#, " +
				"scheduleTime3 = #scheduleTime3#, " +
				"modifiedDate = #modifiedDate#, " +
				"modifiedBy = #modifiedBy# " +						
				"WHERE taskId = #taskId# ";
		
		super.update(sql, obj);
		
	}
	public boolean isExistGlobalSetup(String taskId) throws DaoException {
		Collection col = new ArrayList();		
		String sql= " SELECT count(*) as total FROM fms_global_setup " +
				" WHERE taskId = ? ";
		
		
		col = super.select(sql, HashMap.class, new String[]{taskId}, 0, 1);
		if (col.size() >= 1) {
			HashMap map = (HashMap) col.iterator().next();
			int total = Integer.parseInt(map.get("total").toString());
			
			if(total > 0) return true;
			else return false;
		}
		return false;
	}
	
	public void insertProgramCode(DefaultDataObject obj) throws DaoException {
		
		//default status to active
		String programId = (String)obj.getProperty("programId");
		Collection existPrograms = 
				super.select("SELECT * FROM fms_prog_setup WHERE programId = ?", ProgramObject.class, new Object[]{programId}, 0, 1);
		
		if(existPrograms.size() == 0){
			String sql = "INSERT INTO fms_prog_setup " +
				" (programId, description, producer, department, startProductionDate, endProductionDate, pfeCode, programName, noOfEpisodes, duration, status ) " +
				" VALUES " +
				" (#programId#, #description#, #producer#, #department#, #startProductionDate#, #endProductionDate#, #pfeCode#, #programName#, #noOfEpisodes#, #duration#, '1' )";
			
			super.update(sql, obj);
		}
		else{
			Log.getLog(getClass()).error("This programId is already exist in fms_prog_setup table :"+programId);
		}
	}
	/** end code for abw integration ********************************************/

	public Collection getAbWEmailSetup() throws DaoException
	{
		String sql = 
			" SELECT email1, email2, email3, email4, email5 " +
			" FROM fms_global_setup " +
			" WHERE taskId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, 
				new Object[]{AbwEmailsSetup.TASK_ID_FOR_ABW_EMAIL_SETUP}, 0, -1);
		return result;
	}

	public void insertAbWEmailSetup(DefaultDataObject obj) throws DaoException
	{
		String sql = 
			" INSERT INTO fms_global_setup ( " +
			"	id, taskId, email1, email2, email3, email4, email5 ) " +
			" VALUES (" +
			"	#id#, #taskId#, #email1#, #email2#, #email3#, #email4#, #email5# ) ";
			
		super.update(sql, obj);
	}

	public void deleteAbWEmailSetup() throws DaoException
	{
		String sql = " DELETE FROM fms_global_setup WHERE taskId = ? ";
		super.update(sql, new Object[]{AbwEmailsSetup.TASK_ID_FOR_ABW_EMAIL_SETUP});
	}

	public Collection getRequest(String requestId, String reqStartDate, String redEndDate, ArrayList status) throws DaoException
	{
		ArrayList param = new ArrayList();
		String sql =
			" SELECT DISTINCT(r.requestId), fps.pfeCode " +
			" FROM fms_eng_request r " +
			" LEFT JOIN Fms_prog_setup fps on (r.program = fps.programId) " +
			" WHERE 1=1 ";
		
		if(requestId != null && !requestId.equals(""))
		{
			sql += " AND r.requestId = ? ";
			param.add(requestId);
		}
		else
		{
			if((reqStartDate != null && !reqStartDate.equals("")) && (redEndDate != null && !redEndDate.equals("")))
			{
				sql += " AND ( r.requiredTo >= ? AND r.requiredTo <= ? ) ";
				param.add(reqStartDate);
				param.add(redEndDate);
			}
			else
			{
				if(reqStartDate != null && !reqStartDate.equals(""))
				{
					sql += " AND r.requiredTo >= ? ";
					param.add(reqStartDate);
				}
				else if(redEndDate != null && !redEndDate.equals(""))
				{
					sql += " AND r.requiredTo <= ? ";
					param.add(redEndDate);
				}
			}
			
			if(status != null && !status.isEmpty())
			{
				StringBuffer sb = new StringBuffer();
				sb.append(" AND r.status IN ( ?");
				for(int i = 1; i < status.size(); i++)
				{
					sb.append(", ?");
				}
				sb.append(" ) ");
				sql += sb.toString();
				param.addAll(status);
			}
		}
		
		Collection result = super.select(sql, DefaultDataObject.class, param.toArray(), 0, -1);
		return result;
	}

	public Collection getManpowerByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT man.id, man.serviceId, abw_code AS ratecardAbwCode, quantity AS noOfUnit, requiredFrom AS requiredDateFrom, " +
			" requiredTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_manpower man " +
			" INNER JOIN fms_eng_request_services srv ON (man.requestId = srv.requestId AND man.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc ON (frc.id = man.competencyId) " +
			" WHERE man.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getScpByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT scp.id, scp.serviceId, abw_code AS ratecardAbwCode, 1 AS noOfUnit, requiredFrom AS requiredDateFrom, " +
			" requiredTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM fms_eng_service_scp scp " +
			" INNER JOIN fms_eng_request_services srv ON (scp.requestId = srv.requestId AND scp.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc ON (frc.id = scp.facilityId) " +
			" WHERE scp.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getOtherByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT other.id, other.serviceId, abw_code AS ratecardAbwCode, quantity AS noOfUnit, requiredFrom AS requiredDateFrom, " +
			" requiredTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_other other " +
			" INNER JOIN fms_eng_request_services srv ON (other.requestId = srv.requestId AND other.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc on (frc.id = other.facilityId) " +
			" WHERE other.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getPostproductionByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT pp.id, pp.serviceId, abw_code AS ratecardAbwCode, 1 AS noOfUnit, requiredDate AS requiredDateFrom, " +
			" requiredDateTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_postproduction pp " +
			" INNER JOIN fms_eng_request_services srv ON (pp.requestId = srv.requestId AND pp.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc on (frc.id = pp.facilityId) " +
			" WHERE pp.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getStudioByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT studio.id, studio.serviceId, abw_code AS ratecardAbwCode, 1 AS noOfUnit, bookingDate AS requiredDateFrom, " +
			" bookingDateTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_studio studio " +
			" INNER JOIN fms_eng_request_services srv ON (studio.requestId = srv.requestId AND studio.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc on (frc.id = studio.facilityId) " +
			" WHERE studio.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getTvroByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT tvro.id, tvro.serviceId, abw_code AS ratecardAbwCode, 1 AS noOfUnit, requiredDate AS requiredDateFrom, " +
			" requiredDateTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_tvro tvro " +
			" INNER JOIN fms_eng_request_services srv ON (tvro.requestId = srv.requestId AND tvro.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc on (frc.id = tvro.facilityId) " +
			" WHERE tvro.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}

	public Collection getVtrByRequestId(String requestId) throws DaoException
	{
		String sql =
			" SELECT vtr.id, vtr.serviceId, abw_code AS ratecardAbwCode, 1 AS noOfUnit, requiredDate AS requiredDateFrom, " +
			" requiredDateTo AS requiredDateTo, internalRate AS cost, blockBooking AS blockBooking " +
			" FROM Fms_eng_service_vtr vtr " +
			" INNER JOIN fms_eng_request_services srv ON (vtr.requestId = srv.requestId AND vtr.serviceId = srv.serviceId) " +
			" LEFT JOIN Fms_rate_card frc on (frc.id = vtr.facilityId) " +
			" WHERE vtr.requestId = ? ";
		
		Collection result = super.select(sql, DefaultDataObject.class, new Object[]{requestId}, 0, -1);
		return result;
	}
	
	public double selectTotalRate(String serviceName, String id) throws DaoException
	{
		String tableName 	= "fms_eng_service_scp";
		String rateColumn	= "internalRate";
		String startDateCol	= "";
		String endDateCol	= "";

		if (serviceName.equals("scp")){
			tableName = "fms_eng_service_scp";
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("post")){
			tableName = "fms_eng_service_postproduction";
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";

		}else if (serviceName.equals("other")){
			tableName = "fms_eng_service_other";			
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("studio")){
			tableName = "fms_eng_service_studio";
			startDateCol = "bookingDate";
			endDateCol	 = "bookingDateTo";

		} else if (serviceName.equals("vtr")){
			tableName = "fms_eng_service_vtr";
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";

		} else if (serviceName.equals("manpower")){
			tableName = "fms_eng_service_manpower";
			startDateCol = "requiredFrom";
			endDateCol	 = "requiredTo";

		} else if (serviceName.equals("tvro")){
			tableName = "fms_eng_service_tvro";
			startDateCol = "requiredDate";
			endDateCol	 = "requiredDateTo";
		}  

		String sql="SELECT svc."+ rateColumn +" AS totalRate, ";
		if ((serviceName.equals("manpower")) || (serviceName.equals("other"))){
			sql += "svc.quantity AS quantity, ";
		}

		sql +=	
			"DATEDIFF(day, svc."+ startDateCol + ", svc." + endDateCol + ") AS duration," +
			"svc." + startDateCol + " AS startDate, svc." + endDateCol + " AS endDate " +
			"FROM " + tableName + " svc " +
			"WHERE 1=1 " +
			"AND svc.id = ? ";

		double sum=0.0;
		try{
			Collection col= super.select(sql, HashMap.class, new String[]{ id }, 0, 1);
			if(col!=null){
				if (col.size()>0) {
					HashMap map=(HashMap) col.iterator().next();

					if (map.get("totalRate") != null){
						Double totalRate = new Double(((Number)map.get("totalRate")).doubleValue());
						int duration = (Integer)map.get("duration") + 1;

						if ((serviceName.equals("manpower")) || (serviceName.equals("other"))){
							int quantity = (Integer)map.get("quantity");
							totalRate = totalRate * quantity * duration;
						}else{
							totalRate = totalRate * duration;
						}
						sum += totalRate;
					}
				}
			}
		}catch(Exception e){
			Log.getLog(getClass()).error(e.getMessage(),e);
		}
		return sum;
	}

	public String getAdditionalInfo(String requestId) {
		String status = EngineeringModule.APPLIED_CANCELLATION;
		Collection col = new ArrayList();
		String remark = "";

		String sql = "SELECT additionalInfo FROM fms_eng_request_status " +
				"WHERE requestId=? AND status=?";

		try {
			col = super.select(sql, HashMap.class, new String[] {requestId,status} , 0, -1);
			if (col != null && col.size()>0){
				for (Iterator iterator = col.iterator(); iterator.hasNext();) {
					try {
						HashMap map = (HashMap) iterator.next();
						remark = (String) map.get("additionalInfo");
					} catch (Exception e) {}
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e.getMessage(), e);
		}
		return remark;
	}
}