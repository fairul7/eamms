package com.tms.fms.transport.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DataSourceDao;
import kacang.services.security.User;
import kacang.util.Log;

import com.tms.crm.sales.misc.DateUtil;
import com.tms.ekms.manpowertemp.model.ManpowerAssignmentObject;
import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.abw.model.AbwTransferCostObject;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;

public class TransportDao extends DataSourceDao {

	public void init() throws DaoException {

		try {
			super
					.update(
							"CREATE TABLE fms_facility_inactive_reason(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_facility_location(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_workshop(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_category(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_maketype(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_bodytype(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_outsourcepanel(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_channel(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"CREATE TABLE fms_tran_ratecard(setup_id varchar(255) NOT NULL PRIMARY KEY, name varchar(255) NULL, type char(1) NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_ratecard_detail(detail_id varchar(255) NOT NULL PRIMARY KEY, card_id varchar(255) null, amount varchar(255) NULL, effective_date datetime NULL, createdby varchar(255) NULL, createdby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"INSERT INTO fms_tran_ratecard(setup_id, name, type)values('driver', 'Driver', 'H')",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"INSERT INTO fms_tran_ratecard(setup_id, name, type)values('default', 'Default Rental', 'H')",
							null);
		} catch (Exception e) {
		}

		try {
			Collection args = new ArrayList();
			args.add(new Date());
			args
					.add("kacang.services.security.User_e53cbea0-c0a8c860-1abf8700-d57cad4c");
			args.add(new Date());
			super
					.update(
							"INSERT INTO fms_tran_ratecard_detail(detail_id, card_id, amount, effective_date, "
									+ "createdby, createdby_date)values('default-detail', 'default', '0', ?, ?, ?)",
							args.toArray());
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"CREATE TABLE fms_tran_petrolcard(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_inactive_reason(setup_id int NOT NULL IDENTITY (1, 1) PRIMARY KEY, name varchar(255) NULL, description text NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_vehicle(vehicle_num varchar(255) PRIMARY KEY, channel_id int Null, category_id int NULL, type char(1) NULL, engine_num varchar(255) NULL, casis_no varchar(255) NULL, maketype_id int NULL, model text NULL, engine_cap varchar(255) NULL, color varchar(255) NULL, bodytype_id int NULL, location varchar(255) NULL, year varchar(255) NULL, ncb varchar(255) NULL, reg_date datetime NULL, passenger_cap int NULL, fueltype_id int NULL, rental_pd varchar(50) NULL, rental_ph varchar(50) NULL, maintain_type char(1) NULL, by_km varchar(50) NULL, by_month varchar(50) NULL, roadtax_date datetime NULL, ins_date datetime NULL, status char(1) NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle add bodytype_name varchar(255) NULL",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle add charge_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle ALTER Column channel_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle ALTER Column category_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle ALTER Column maketype_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_vehicle ALTER Column bodytype_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"update fms_tran_vehicle set charge_id = 'default' where charge_id is null",
							null);
		} catch (Exception e) {
		}
		try {
			super.update(
					"ALTER TABLE fms_tran_vehicle drop column fueltype_id",
					null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_maintenance(id varchar(255) PRIMARY KEY, vehicle_num varchar(255), service_date datetime NULL, send_date datetime NULL, ws_name varchar(255) NULL, ws_address text NULL, order_num varchar(255) NULL, inv_num varchar(255) NULL, cost varchar(255) NULL, reason text NULL, remark text NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_maintenance drop column ws_name, ws_address",
							null);
		} catch (Exception e) {
		}
		try {
			super.update("ALTER TABLE fms_tran_maintenance add ws_id int NULL",
					null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_maintenance ALTER Column ws_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_insurance(id varchar(255) PRIMARY KEY, vehicle_num varchar(255), rt_renew datetime NULL, rt_amount varchar(255) NULL, rt_period_from datetime NULL, rt_period_to datetime NULL, is_name varchar(255) NULL, is_renew datetime NULL, is_amount varchar(255) NULL, is_period_from datetime NULL, is_period_to datetime NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_inactive(id varchar(255) PRIMARY KEY, vehicle_num varchar(255), date_from datetime NULL, date_to datetime NULL, reason_id int NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"ALTER TABLE fms_tran_inactive ALTER Column reason_id varchar(255) null",
							null);
		} catch (Exception e) {
		}
		try {
			super
					.update(
							"CREATE TABLE fms_tran_writeoff(id varchar(255) PRIMARY KEY, vehicle_num varchar(255), file_name varchar(255) NULL, file_path varchar(255) NULL, file_type varchar(255) NULL, file_size int NULL, reason text NULL, createdby varchar(255) NULL, createdby_date datetime NULL, updatedby varchar(255) NULL, updatedby_date datetime NULL)",
							null);
		} catch (Exception e) {
		}

		// transport request
		try {
			super.update("CREATE TABLE fms_tran_request( "
					+ "id varchar(255) NOT NULL default '',"
					+ "requestTitle varchar(255) default NULL, "
					+ "requestType char(1) default NULL, "
					+ "program varchar(255) default NULL,"
					+ "startDate datetime default '0000-00-00 00:00:00',  "
					+ "endDate datetime default '0000-00-00 00:00:00',  "
					+ "destination varchar(255) default NULL,"
					+ "purpose varchar(255) default NULL,"
					+ "remarks varchar(255) default NULL,"
					+ "status char(1) default NULL, "
					+ "reason varchar(255) default NULL,"
					+ "requestBy varchar(255) NOT NULL,"
					+ "requestDate datetime default NULL,"
					+ "updatedBy varchar(255) default NULL,"
					+ "updatedDate datetime default NULL,"
					+ "approvedBy varchar(255) default NULL, "
					+ "approvedDate varchar(255) default NULL, "
					+ "statusRequest char(1) default NULL,"
					+ "rate numeric(18,2) NULL, "
					+ "engineeringRequestId VARCHAR(255) NULL, "
					+ "blockBooking CHAR(1) NULL" + ") ", null);
		} catch (Exception e) {
		}

		try {
			super.update("ALTER TABLE fms_tran_request ADD rate numeric(18,2)",
					null);
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request ADD engineeringRequestId VARCHAR(255)",
							null);
		} catch (Exception e) {
		}

		try {
			super.update(
					"ALTER TABLE fms_tran_request ADD blockBooking CHAR(1)",
					null);
		} catch (Exception e) {
		}

		// create primary key for fms_tran_request
		try {
			super
					.update(
							"ALTER TABLE fms_tran_request ADD CONSTRAINT PK_fms_tran_request PRIMARY KEY CLUSTERED (id)",
							null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_request
		try {
			super.update(
					"CREATE INDEX engineeringRequestId ON fms_tran_request(engineeringRequestId)", null);
		} catch (Exception e) {
		}

		try {
			super.update("CREATE TABLE fms_tran_request_vehicle("
					+ "id varchar(255) NOT NULL default '', "
					+ "requestId varchar(255) NOT NULL default '', "
					+ "category_id varchar(255) NOT NULL default '', "
					+ "quantity int NOT NULL default 0, "
					+ "driver int default 0 " + ")", null);
		} catch (Exception e) {
		}

		// create primary key for fms_tran_request_vehicle
		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_vehicle ADD CONSTRAINT PK_fms_tran_request_vehicle PRIMARY KEY CLUSTERED (id)",
							null);
		} catch (Exception e) {
		}

		// create index for fms_tran_request_vehicle(requestId)
		try {
			super
					.update(
							"CREATE INDEX requestId ON fms_tran_request_vehicle(requestId)",
							null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_request_driver(requestId, assignmentId, flagId)
		try {
			super
					.update(
							"CREATE INDEX idx_fms_tran_request_driver_id on fms_tran_request_driver (requestId, assignmentId, flagId)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_request_driver(manpowerId, assignmentId, requestId)
		try {
			super.update("CREATE INDEX manpowerId_assignmentId_requestId ON fms_tran_request_driver(manpowerId, assignmentId, requestId)", null);
		} catch (Exception e) {
		}


		try {
			super.update("CREATE TABLE fms_tran_request_assignment("
					+ "id varchar(255) NOT NULL default '', "
					+ "requestId varchar(255) NOT NULL default '', "
					+ "vehicle_num varchar(255) NOT NULL default '' " + ")",
					null);
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_assignment ADD startDate datetime",
							null);
			super
					.update(
							"ALTER TABLE fms_tran_request_assignment ADD endDate datetime",
							null);
		} catch (Exception e) {
		}
		
		try {
			super.update("CREATE INDEX IX_fms_tran_request_assignment_id ON fms_tran_request_assignment(id)", null);
		} catch (Exception e) {
		}

		try {
			super.update("CREATE TABLE fms_tran_request_driver("
					+ "assgId varchar(255) default NULL, "
					+ "manpowerId varchar(255) default NULL " + ")", null);
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_driver ADD completionDate datetime",
							null);
			super
					.update(
							"ALTER TABLE fms_tran_request_driver ADD reason varchar(255)",
							null);
			super.update(
					"ALTER TABLE fms_tran_request_driver ADD status char(1)",
					null);
		} catch (DaoException e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_driver ADD assignmentId varchar(255)",
							null);
		} catch (Exception er) {
		}

		try { // flagId created just to get unique in each tupple
			super
					.update(
							"ALTER TABLE fms_tran_request_driver ADD flagId varchar(255)",
							null);
			super
					.update(
							"ALTER TABLE fms_tran_request_assignment ADD flagId varchar(255)",
							null);
		} catch (Exception er) {
		}

		try {
			super
					.update(
							"EXEC sp_rename 'fms_tran_request_driver.assgId', 'requestId', 'COLUMN';",
							null);
		} catch (Exception er) {
			Log.getLog(getClass()).info("Error on CHANGING COLUMN NAME :" + er);
		}

		try {
			super.update("CREATE TABLE fms_tran_request_assignment_details("
					+ "assgId varchar(255) NOT NULL default '', "
					+ "checkin_date datetime default NULL, "
					+ "checkout_date datetime default NULL, "
					+ "meterStart bigint default 0, "
					+ "meterEnd bigint default 0, "
					+ "remarks varchar(255) default NULL, "
					+ "petrolCard varchar(255) default NULL, "
					+ "createdBy varchar(255) default NULL, "
					+ "createdDate datetime default NULL, "
					+ "updatedBy varchar(255) default NULL, "
					+ "updatedDate datetime default NULL " + ")", null);
		} catch (Exception e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_assignment_details ADD status char(1)",
							null);
		} catch (DaoException e) {
		}

		try {
			super
					.update(
							"ALTER TABLE fms_tran_request_assignment_details ADD vehicle_num varchar(255)",
							null);
		} catch (DaoException e) {
		}
		
		try {
			super.update("CREATE INDEX IX_fms_tran_request_assignment_details_assgid ON fms_tran_request_assignment_details(assgid)", null);
		} catch (Exception e) {
		}

		try {
			super.update("CREATE TABLE fms_tran_request_outsource("
					+ "id varchar(255) NOT NULL default '', "
					+ "startDate datetime default NULL,  "
					+ "endDate datetime default NULL,  "
					+ "requestId varchar(255) NOT NULL default '', "
					+ "setup_id varchar(255) default NULL,  "
					+ "quotationNo varchar(255) default NULL,  "
					+ "quotationPrice bigint default 0,  "
					+ "invoiceNo varchar(35) default NULL,  "
					+ "invoicePrice bigint default 0,  "
					+ "remark varchar(255) default NULL,  "
					+ "createdBy varchar(255) default NULL,  "
					+ "createdDate datetime default NULL,  "
					+ "updatedBy varchar(255) default NULL,  "
					+ "updatedDate datetime default NULL  " + ")", null);
		} catch (Exception e) {
		}

		try {
			super
					.update("CREATE TABLE fms_tran_request_outsource_panel ("
							+ "outsourceId varchar(255) NOT NULL default '0', "
							+ "setup_id varchar(255) NOT NULL default '0' "
							+ ")", null);

		} catch (Exception e) {

		}

		try {
			super.update("CREATE TABLE fms_tran_outsourcepanel( "
					+ "setup_id varchar(255) NOT NULL,"
					+ "name varchar(255) NULL," + "description text NULL,"
					+ "status char(1) NULL," + "type char(1) NULL,"
					+ "createdby varchar(255) NULL,"
					+ "createdby_date datetime NULL,"
					+ "updatedby varchar(255) NULL,"
					+ "updatedby_date datetime NULL)", null);
		} catch (Exception e) {

		}

		try {
			super.update(
					"ALTER TABLE fms_tran_outsourcepanel ADD type CHAR(1)",
					null);
		} catch (Exception e) {

		}

		try {
			super
					.update("ALTER TABLE fms_tran_maketype ADD type CHAR(1)",
							null);
		} catch (Exception e) {

		}

		// fms_tran_request_status

		try {
			super
					.update(
							"CREATE TABLE fms_tran_request_status(id varchar(255) NOT NULL, "
									+ " status char(1) NOT NULL, reason text NULL, "
									+ " additionalInfo varchar(255) NULL, createdBy varchar(255) NULL, "
									+ " createdDate datetime NULL, PRIMARY KEY(id, status))",
							null);
		} catch (Exception e) {
		}

		// table to manage assignment
		try {
			super.update("CREATE TABLE fms_tran_assignment( "
					+ "id varchar(255) NOT NULL,"
					+ "requestId varchar(255) NULL,"
					+ "startDate datetime NULL," + "endDate datetime NULL,"
					+ "status char(1) NULL)", null);
		} catch (Exception e) {

		}

		// create primary key for fms_tran_assignment
		try {
			super
					.update(
							"ALTER TABLE fms_tran_assignment ADD CONSTRAINT PK_fms_tran_assignment PRIMARY KEY CLUSTERED (id)",
							null);
		} catch (Exception e) {
		}

		// create index for fms_tran_assignment(requestId)
		try {
			super.update(
					"CREATE INDEX requestId ON fms_tran_assignment(requestId)",
					null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_assignment(endDate, startDate)
		try {
			super.update("CREATE INDEX endDate_startDate ON fms_tran_assignment(endDate, startDate)", null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_request_outsource_panel
		try {
			super.update(
					"CREATE INDEX idx_fms_tran_request_outsource_panel on fms_tran_request_outsource_panel (outsourceId)",
					null);
		} catch (Exception e) {
		}
		
		// create index for fms_tran_request_outsource
		try {
			super.update(
					"CREATE INDEX idx_fms_tran_request_outsource on fms_tran_request_outsource (requestId)",
					null);
		} catch (Exception e) {
		}
		
		try {
			super.update("ALTER TABLE fms_tran_request ADD rateVehicle numeric(18,2)",
					null);
		} catch (Exception e) {
		}
		
		try {
			super.update("CREATE TABLE fms_tran_request_driver_vehicle(" +
					"id varchar(255) NOT NULL, " +
					"assignmentId varchar(255) NOT NULL, " +
					"vehicle_num varchar(35) NOT NULL, " +
					"driver varchar(250) NOT NULL, PRIMARY KEY(id))", null);
		} catch (Exception e) {
			Log.getLog(getClass()).error(e);
		}
		
		try{
			super.update("ALTER TABLE fms_tran_rateCard ADD abw_code VARCHAR(20) DEFAULT NULL", null);
		}
		catch(DaoException e){
			//do nothing
		}
	}

	// rate card
	public void insertRateCard(RateCardObject o) throws DaoException {
		String sql = "INSERT INTO fms_tran_ratecard (setup_id, name, type) VALUES (#setup_id#, #name#, #type#)";
		super.update(sql, o);

		String sql2 = "INSERT INTO fms_tran_ratecard_detail(detail_id, card_id, amount, effective_date, "
				+ "createdby, createdby_date) values (#detail_id#, #setup_id#, #amount#, #effective_date#, "
				+ "#createdby#, #createdby_date#)";
		super.update(sql2, o);
	}

	public void updateRateCard(RateCardObject o) throws DaoException {
		String sql = "update fms_tran_ratecard set name=#name#, type=#type# where setup_id=#setup_id#";
		super.update(sql, o);

		String sql2 = "INSERT INTO fms_tran_ratecard_detail(detail_id, card_id, amount, effective_date, "
				+ "createdby, createdby_date) values (#detail_id#, #setup_id#, #amount#, #effective_date#, "
				+ "#createdby#, #createdby_date#)";
		super.update(sql2, o);
	}

	public Collection selectRateCard(String id, Date effDate)
			throws DaoException {
		Collection args = new ArrayList();
		args.add(id);
		args.add(effDate);
		return super
				.select(
						"SELECT * FROM fms_tran_ratecard left join fms_tran_ratecard_detail on "
								+ "setup_id=card_id WHERE setup_id=? and effective_date <= ? order by effective_date desc, createdby_date desc",
						RateCardObject.class, args.toArray(), 0, 1);
	}

	public Collection selectRateCard(Date effDate, String sort, boolean desc)
			throws DaoException {
		Collection args1 = new ArrayList();
		String sql = "select * from fms_tran_ratecard where setup_id != 'driver'";
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort;
		}
		if (desc) {
			sql = sql + " DESC";
		}
		Collection rows = super.select(sql, RateCardObject.class, args1
				.toArray(), 0, -1);

		ArrayList result = new ArrayList();

		if (rows.size() > 0) {
			for (Iterator i = rows.iterator(); i.hasNext();) {
				RateCardObject o = (RateCardObject) i.next();
				Collection args2 = new ArrayList();
				args2.add(o.getSetup_id());
				args2.add(effDate);
				String sql2 = "SELECT r.*, d.*, u.username as createdby_name FROM security_user u, "
						+ "fms_tran_ratecard r left join fms_tran_ratecard_detail d on "
						+ "setup_id=card_id WHERE setup_id=? and effective_date <= ? AND u.id=d.createdby "
						+ "order by effective_date desc, createdby_date desc";
				Collection rateCard = super.select(sql2, RateCardObject.class,
						args2.toArray(), 0, 1);
				Iterator j = rateCard.iterator();
				result.add(j.next());
			}
		}

		return result;
	}

	public Collection selectRateCardName() throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT setup_id, name FROM fms_tran_ratecard where setup_id != 'driver' order by name";
		return super.select(sql, RateCardObject.class, args.toArray(), 0, -1);
	}

	public Collection selectRateCardLog(String id) throws DaoException {
		Collection args = new ArrayList();
		args.add(id);
		String sql = "SELECT d.*, u.username as createdby_name FROM security_user u, "
				+ "fms_tran_ratecard_detail d WHERE card_id=? and u.id=d.createdby "
				+ "order by effective_date desc, createdby_date desc";
		return super.select(sql, RateCardObject.class, args.toArray(), 0, -1);
	}

	public int selectRateCardCount(String search) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM fms_tran_ratecard WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND name = ?";
			args.add(search);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public RateCardObject selectRateCardByNameDate(String name, Date effDate)
			throws DaoException {
		Collection args = new ArrayList();
		Collection rateCol = new ArrayList();
		RateCardObject rateObj = null;

		args.add(name);
		args.add(effDate);
		rateCol = super
				.select(
						"SELECT TOP 1 "
								+ "  * FROM fms_tran_ratecard left join fms_tran_ratecard_detail on "
								+ "setup_id=card_id WHERE name=? and effective_date <= ? 	"
								+ "order by effective_date desc, createdby_date desc 	",
						RateCardObject.class, args.toArray(), 0, 1);

		if (rateCol.size() > 0) {
			for (Iterator it = rateCol.iterator(); it.hasNext();) {
				rateObj = (RateCardObject) it.next();
			}
		}
		return rateObj;
	}

	/* writeoff */
	public void insertWriteoff(WriteoffObject w) throws DaoException {
		String sql = "INSERT INTO fms_tran_writeoff (id, vehicle_num, file_name, file_path, file_type, file_size, reason, "
				+ "createdby, createdby_date) VALUES (#id#, #vehicle_num#, #file_name#, #file_path#, #file_type#, #file_size#, #reason#,"
				+ " #createdby#, #createdby_date#)";
		super.update(sql, w);
	}

	public Collection selectWriteoff(String vehicle_num) throws DaoException {
		return super
				.select(
						"SELECT w.*, s.username as createdby_name from fms_tran_writeoff w, security_user s WHERE vehicle_num=? AND s.id=w.createdby",
						WriteoffObject.class, new String[] { vehicle_num }, 0,
						-1);
	}

	/* Inactive */
	public void insertInactive(InactiveObject i) throws DaoException {
		String sql = "INSERT INTO fms_tran_inactive (id, vehicle_num, date_from, date_to, reason_id, "
				+ "createdby, createdby_date) VALUES (#id#, #vehicle_num#, #date_from#, #date_to#, #reason_id#,"
				+ " #createdby#, #createdby_date#)";
		super.update(sql, i);
	}

	public void deleteInactive(String id) throws DaoException {
		super.update("DELETE FROM fms_tran_inactive WHERE id=?",
				new String[] { id });
	}

	public Collection selectInactive(String search, String vehicle_num,
			int reason_id, String sort, boolean desc, int start, int rows)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT i.*, s.username as createdby_name, r.name as reason FROM fms_tran_inactive i, fms_tran_inactive_reason r, security_user s WHERE 1=1 AND i.reason_id=r.setup_id AND s.id=i.createdby";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (r.name like ? OR s.username like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND i.vehicle_num=?";
			args.add(vehicle_num);
		}
		if (reason_id != -1) {
			sql = sql + " AND i.reason_id = ?";
			args.add(reason_id);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort;
		}
		if (desc) {
			sql = sql + " DESC";
		}
		return super.select(sql, InactiveObject.class, args.toArray(), start,
				rows);
	}

	public int selectInactiveCount(String search, String vehicle_num,
			int reason_id) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total FROM fms_tran_inactive i, fms_tran_inactive_reason r, security_user s WHERE 1=1 AND i.reason_id=r.setup_id AND s.id=i.createdby";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (r.name like ? OR s.username like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND i.vehicle_num=?";
			args.add(vehicle_num);
		}
		if (reason_id != -1) {
			sql = sql + " AND i.reason_id = ?";
			args.add(reason_id);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	/* Insurance */
	public void insertInsurance(InsuranceObject i) throws DaoException {
		String sql = "INSERT INTO fms_tran_insurance (id, vehicle_num, rt_renew, rt_amount, rt_period_from, "
				+ "rt_period_to, is_name, is_renew, is_amount, is_period_from, is_period_to, createdby, createdby_date) "
				+ "VALUES (#id#, #vehicle_num#, #rt_renew#, #rt_amount#, #rt_period_from#, #rt_period_to#, #is_name#, "
				+ "#is_renew#, #is_amount#, #is_period_from#, #is_period_to#, #createdby#, #createdby_date#)";
		super.update(sql, i);
	}

	public void updateInsurance(InsuranceObject i) throws DaoException {
		String sql = "UPDATE fms_tran_insurance SET vehicle_num=#vehicle_num#, rt_renew=#rt_renew#,"
				+ "rt_amount=#rt_amount#, rt_period_from=#rt_period_from#, rt_period_to=#rt_period_to#, is_name=#is_name#, is_renew=#is_renew#,"
				+ " is_amount=#is_amount#, is_period_from=#is_period_from#, is_period_to=#is_period_to#, updatedby=#updatedby#,"
				+ " updatedby_date=#updatedby_date# WHERE id=#id#";
		super.update(sql, i);
	}

	public void deleteInsurance(String id) throws DaoException {
		super.update("DELETE FROM fms_tran_insurance WHERE id=?",
				new String[] { id });
	}

	public Collection selectInsurance(String id) throws DaoException {
		return super.select("SELECT * from fms_tran_insurance WHERE id=?",
				InsuranceObject.class, new String[] { id }, 0, -1);
	}

	public Collection selectInsurance(String search, String vehicle_num,
			String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT i.*, s.username as createdby_name FROM fms_tran_insurance i, security_user s WHERE 1=1 AND s.id=i.createdby";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql
					+ " AND (rt_amount LIKE ? or is_amount like ? or username like ? or is_name like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND vehicle_num=?";
			args.add(vehicle_num);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort;
		}
		if (desc) {
			sql = sql + " DESC";
		}
		return super.select(sql, InsuranceObject.class, args.toArray(), start,
				rows);
	}

	public int selectInsuranceCount(String search, String vehicle_num)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) as total FROM fms_tran_insurance i, security_user s WHERE 1=1 AND s.id=i.createdby";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql
					+ " AND (rt_amount LIKE ? or is_amount like ? or username like ? or is_name like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND vehicle_num=?";
			args.add(vehicle_num);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	/* maintenance */
	public void insertMaintenance(MaintenanceObject m) throws DaoException {
		String sql = "INSERT INTO fms_tran_maintenance (id, vehicle_num, service_date, send_date, ws_id, "
				+ "order_num, inv_num, cost, reason, remark, createdby, createdby_date) "
				+ "VALUES (#id#, #vehicle_num#, #service_date#, #send_date#, #ws_id#, "
				+ "#order_num#, #inv_num#, #cost#, #reason#, #remark#, #createdby#, #createdby_date#)";
		super.update(sql, m);
	}

	public void updateMaintenance(MaintenanceObject m) throws DaoException {
		String sql = "UPDATE fms_tran_maintenance SET vehicle_num=#vehicle_num#, service_date=#service_date#,"
				+ "send_date=#send_date#, ws_id=#ws_id#, order_num=#order_num#,"
				+ " inv_num=#inv_num#, cost=#cost#, reason=#reason#, remark=#remark#, updatedby=#updatedby#,"
				+ " updatedby_date=#updatedby_date# WHERE id=#id#";
		super.update(sql, m);
	}

	public void deleteMaintenance(String id) throws DaoException {
		super.update("DELETE FROM fms_tran_maintenance WHERE id=?",
				new String[] { id });
	}

	public Collection selectMaintenance(String id) throws DaoException {
		return super
				.select(
						"SELECT m.*, w.name as ws_name, w.description as ws_address from fms_tran_maintenance m, fms_tran_workshop w WHERE m.ws_id=w.setup_id and id=?",
						MaintenanceObject.class, new String[] { id }, 0, -1);
	}

	public Collection selectMaintenance(String search, String vehicle_num,
			String sort, boolean desc, int start, int rows) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT m.*, w.name as ws_name, w.description as ws_address, s.username as createdby_name "
				+ "FROM fms_tran_maintenance m, security_user s, fms_tran_workshop w WHERE 1=1 AND "
				+ "s.id=m.createdby and m.ws_id=w.setup_id ";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql
					+ " AND (w.name LIKE ? or inv_num like ? or username like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND vehicle_num=?";
			args.add(vehicle_num);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort;
		}
		if (desc) {
			sql = sql + " DESC";
		}
		return super.select(sql, MaintenanceObject.class, args.toArray(),
				start, rows);
	}

	public int selectMaintenanceCount(String search, String vehicle_num)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(m.id) as total FROM fms_tran_maintenance m, security_user s, fms_tran_workshop w"
				+ " WHERE 1=1 AND s.id=m.createdby and m.ws_id=w.setup_id ";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql
					+ " AND (w.name LIKE ? or inv_num like ? or username like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!"".equals(vehicle_num) && !"null".equals(vehicle_num)
				&& vehicle_num != null) {
			sql = sql + " AND vehicle_num=?";
			args.add(vehicle_num);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public int selectWorkshopUsedCount(String workshop_id) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(id) as total FROM fms_tran_maintenance WHERE 1=1";

		if (!"".equals(workshop_id) && !"null".equals(workshop_id)
				&& workshop_id != null) {
			sql = sql + " AND ws_id=?";
			args.add(workshop_id);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	/* vehicle */
	public void insertVehicle(VehicleObject v) throws DaoException {
		String sql = "INSERT INTO fms_tran_vehicle (vehicle_num, channel_id, category_id, type, engine_num, "
				+ "casis_no, maketype_id, model, engine_cap, color, location, year, ncb, reg_date, passenger_cap, "
				+ "charge_id, rental_ph, maintain_type, by_km, by_month, "
				+ "status, createdby, createdby_date, bodytype_name) VALUES (#vehicle_num#, #channel_id#, #category_id#, "
				+ "#type#, #engine_num#, #casis_no#, #maketype_id#, #model#, #engine_cap#, #color#, "
				+ "#location#, #year#, #ncb#, #reg_date#, #passenger_cap#, #charge_id#, #rental_ph#, "
				+ "#maintain_type#, #by_km#, #by_month#, #status#, #createdby#"
				+ ", #createdby_date#, #bodytype_name#)";
		super.update(sql, v);
	}

	public void updateVehicle(VehicleObject v) throws DaoException {
		String sql = "UPDATE fms_tran_vehicle SET channel_id=#channel_id#, category_id=#category_id#, "
				+ "type=#type#, engine_num=#engine_num#, casis_no=#casis_no#, maketype_id=#maketype_id#, "
				+ "model=#model#, engine_cap=#engine_cap#, color=#color#, bodytype_id=#bodytype_id#, "
				+ "location=#location#, year=#year#, ncb=#ncb#, bodytype_name=#bodytype_name#, "
				+ "reg_date=#reg_date#, passenger_cap=#passenger_cap#, charge_id=#charge_id#, "
				+ "rental_ph=#rental_ph#, maintain_type=#maintain_type#, "
				+ "by_km=#by_km#, by_month=#by_month#, "
				+ "status=#status#, updatedby=#updatedby#, updatedby_date=#updatedby_date# WHERE vehicle_num=#vehicle_num#";
		super.update(sql, v);
	}

	public void deleteVehicle(String vehicle_num) throws DaoException {
		super.update("DELETE FROM fms_tran_vehicle WHERE vehicle_num=?",
				new String[] { vehicle_num });
	}

	public Collection selectVehicle(String vehicle_num) throws DaoException {
		String sql = "SELECT v.*, m.name as maketype_name, c.name as channel_name, "
				+ "ca.name as category_name, f.name as charge_name "
				+ "FROM fms_tran_vehicle v, fms_tran_maketype m, fms_tran_category ca, "
				+ "fms_tran_channel c, fms_tran_ratecard f "
				+ "WHERE 1=1 AND v.maketype_id = m.setup_id AND v.channel_id=c.setup_id AND "
				+ "v.category_id=ca.setup_id AND v.charge_id=f.setup_id "
				+ "AND vehicle_num=?";
		return super.select(sql, VehicleObject.class,
				new String[] { vehicle_num }, 0, -1);
	}

	public Collection selectVehicle(String search, String type, String channel,
			String category, String makeType, String bodyType, String charge,
			String status, String sort, boolean desc, int start, int rows)
			throws DaoException {
		Collection args = new ArrayList();
		/*String sql = "SELECT v.*, m.name as maketype_name, c.name as channel_name, "
				+ "ca.name as category_name, f.name as charge_name "
				+ "FROM fms_tran_vehicle v, fms_tran_maketype m, fms_tran_category ca, "
				+ "fms_tran_channel c, fms_tran_ratecard f "
				+ "WHERE 1=1 AND v.maketype_id = m.setup_id AND v.channel_id=c.setup_id AND "
				+ "v.category_id=ca.setup_id AND v.charge_id=f.setup_id";*/
		//sql changed by fairul on 1st july 2011
		
		String sql = "SELECT v.*, m.name as maketype_name, c.name as channel_name, ca.name as category_name, f.name as charge_name "+ 
		"FROM fms_tran_vehicle v "+
		"inner join fms_tran_maketype m on v.maketype_id = m.setup_id "+ 
		"inner join fms_tran_category ca on ca.setup_id = v.category_id "+
		"inner join fms_tran_channel c on c.setup_id = v.channel_id "+
		"inner join fms_tran_ratecard f on f.setup_id = v.charge_id "+
		"WHERE 1=1 ";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (vehicle_num LIKE ? OR c.name LIKE ? OR ca.name LIKE ?) ";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!("-1".equals(type) || "".equals(type))) {
			sql = sql + " AND v.type = ?";
			args.add(type);
		}
		if (!("-1".equals(channel) || "".equals(channel))) {
			sql = sql + " AND channel_id = ?";
			args.add(channel);
		}
		if (!("-1".equals(category) || "".equals(category) || null == category)) {
			sql = sql + " AND category_id = ?";
			args.add(category);
		}
		if (!("-1".equals(makeType) || "".equals(makeType))) {
			sql = sql + " AND maketype_id = ?";
			args.add(makeType);
		}
		if (!("-1".equals(bodyType) || "".equals(bodyType))) {
			sql = sql + " AND bodytype_name = ?";
			args.add(bodyType);
		}
		if (!("-1".equals(charge) || "".equals(charge))) {
			sql = sql + " AND charge_id = ?";
			args.add(charge);
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND v.status = ?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY v." + sort;
		} else {
			sql = sql + " ORDER BY ca.name, v.vehicle_num ";
		}
		if (desc) {
			sql = sql + " DESC";
		}
		return super.select(sql, VehicleObject.class, args.toArray(), start,
				rows);
	}

	public int selectVehicleCount(String search, String type, String channel,
			String category, String makeType, String bodyType, String charge,
			String status) throws DaoException {
		Collection args = new ArrayList();
		
		//String sql = "SELECT count(*) AS total FROM fms_tran_vehicle WHERE 1=1";		
		//sql changed by fairul on 1st july 2011
		
		String sql ="SELECT count(*) as total "+ 
		"FROM fms_tran_vehicle v "+
		"inner join fms_tran_maketype m on v.maketype_id = m.setup_id "+ 
		"inner join fms_tran_category ca on ca.setup_id = v.category_id "+
		"inner join fms_tran_channel c on c.setup_id = v.channel_id "+
		"inner join fms_tran_ratecard f on f.setup_id = v.charge_id "+
		"WHERE 1=1 ";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND v.vehicle_num LIKE ?";
			args.add("%" + search + "%");
		}
		if (!("-1".equals(type) || "".equals(type))) {
			sql = sql + " AND v.type = ?";
			args.add(type);
		}
		if (!("-1".equals(channel) || "".equals(channel))) {
			sql = sql + " AND v.channel_id = ?";
			args.add(channel);
		}
		if (!("-1".equals(category) || "".equals(category) || null == category)) {
			sql = sql + " AND v.category_id = ?";
			args.add(category);
		}
		if (!("-1".equals(makeType) || "".equals(makeType))) {
			sql = sql + " AND v.maketype_id = ?";
			args.add(makeType);
		}
		if (!("-1".equals(bodyType) || "".equals(bodyType))) {
			sql = sql + " AND v.bodytype_name = ?";
			args.add(bodyType);
		}
		if (!("-1".equals(charge) || "".equals(charge))) {
			sql = sql + " AND v.charge_id = ?";
			args.add(charge);
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND v.status = ?";
			args.add(status);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	/* setup */
	public void insertCategoryObject(String tableName, SetupObject o)
			throws DaoException {
		super
				.update(
						"INSERT INTO "
								+ tableName
								+ " (setup_id, name, description, type, status, createdby, createdby_date) VALUES (#setup_id#, #name#, #description#, #type#, #status#, #createdby#, #createdby_date#)",
						o);
	}

	public void insertSetupObject(String tableName, SetupObject o)
			throws DaoException {
		super
				.update(
						"INSERT INTO "
								+ tableName
								+ " (setup_id, name, description, status, createdby, createdby_date) VALUES (#setup_id#, #name#, #description#, #status#, #createdby#, #createdby_date#)",
						o);
	}

	public void updateCategoryObject(String tableName, SetupObject o)
			throws DaoException {
		super
				.update(
						"UPDATE "
								+ tableName
								+ " SET name=#name#, description=#description#, type=#type#, status=#status#, updatedby=#updatedby#, updatedby_date=#updatedby_date# WHERE setup_id=#setup_id#",
						o);
	}

	public void updateSetupObject(String tableName, SetupObject o)
			throws DaoException {
		super
				.update(
						"UPDATE "
								+ tableName
								+ " SET name=#name#, description=#description#, status=#status#, updatedby=#updatedby#, updatedby_date=#updatedby_date# WHERE setup_id=#setup_id#",
						o);
	}

	public void deleteSetupObject(String tableName, String setupId)
			throws DaoException {
		super.update("DELETE FROM " + tableName + " WHERE setup_id=?",
				new String[] { setupId });
	}

	public Collection selectSetupObject(String tableName, String setupId)
			throws DaoException {
		return super.select("SELECT * FROM " + tableName + " WHERE setup_id=?",
				SetupObject.class, new String[] { setupId }, 0, -1);
	}

	public Collection selectSetupObject(String tableName, String search,
			String status, String sort, boolean desc, int start, int rows)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT * FROM " + tableName + " WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (name LIKE ? or description Like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND status = ?";
			args.add(status);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort + (desc ? " DESC " : "");
		} else {
			sql += " ORDER BY name ";
		}
		return super
				.select(sql, SetupObject.class, args.toArray(), start, rows);
	}

	public int selectSetupObjectCount(String tableName, String search,
			String status) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM " + tableName
				+ " WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (name LIKE ? or description Like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND status = ?";
			args.add(status);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	// /
	public Collection selectOutsourcePanel(String search, String status,
			String type, String sort, boolean desc, int start, int rows)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT * FROM fms_tran_outsourcepanel WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (name LIKE ? or description Like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND status = ?";
			args.add(status);
		}
		if (!("-1".equals(type) || "".equals(type))) {
			sql = sql + " AND type = ?";
			args.add(type);
		}
		if (!"".equals(sort) && !"null".equals(sort) && sort != null) {
			sql = sql + " ORDER BY " + sort;
		}
		if (desc) {
			sql = sql + " DESC";
		}
		return super
				.select(sql, SetupObject.class, args.toArray(), start, rows);
	}

	public int selectCountOutsourcePanel(String search, String status,
			String type) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM fms_tran_outsourcepanel WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND (name LIKE ? or description Like ?)";
			args.add("%" + search + "%");
			args.add("%" + search + "%");
		}
		if (!("-1".equals(status) || "".equals(status))) {
			sql = sql + " AND status = ?";
			args.add(status);
		}
		if (!("-1".equals(type) || "".equals(type))) {
			sql = sql + " AND type = ?";
			args.add(type);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	// /
	public Collection getChannel(String id) throws DaoException {

		return super.select(
				"SELECT setup_id, name FROM fms_tran_channel WHERE setup_id=?",
				SetupObject.class, new String[] { id }, 0, -1);
	}

	public int selectSetupObjectCount(String tableName, String search)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM " + tableName
				+ " WHERE 1=1";

		if (!"".equals(search) && !"null".equals(search) && search != null) {
			sql = sql + " AND name = ?";
			args.add(search);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public Collection getLocation(String id) throws DaoException {

		return super
				.select(
						"SELECT setup_id, name FROM fms_facility_location WHERE setup_id=?",
						SetupObject.class, new String[] { id }, 0, -1);
	}

	public Collection selectVehicles(String reqId, boolean showInvalid) throws DaoException {
		String join = "INNER JOIN";
		if (showInvalid) {
			join = "LEFT OUTER JOIN";
		}
		
		String sql = "SELECT v.id, requestId, category_id, quantity, driver, c.name as name, c.status as categoryStatus, r.type "+
					 "FROM fms_tran_request_vehicle v "+ 
					 join + " fms_tran_category c on v.category_id=c.setup_id "+  
					 join + " fms_tran_ratecard r on r.name=c.name "+
					 "where requestId = ?";	

		return super.select(sql, VehicleRequest.class, new String[] { reqId },
				0, -1);
	}

	public Collection selectVehicleByType(String reqId, String type)
			throws DaoException {

		String sql = "SELECT a.id, requestId, category_id, quantity, driver, b.name as name FROM fms_tran_request_vehicle a "
				+ "inner join fms_tran_category b on a.category_id=b.setup_id  where 1=1 	"
				+ "AND (requestId = ?) AND (b.name = ?)";
		return super.select(sql, VehicleRequest.class, new String[] { reqId,
				type }, 0, -1);

		// return eventKeys;
	}

	public void insertTranVehicle(VehicleRequest vr) throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_request_vehicle(id, requestId, category_id, quantity, driver) VALUES (#id#, #requestId#, #category_id#, #quantity#, #driver#)",
						vr);

	}

	public void deleteTranVehicle(String id) throws DaoException {

		super.update("DELETE FROM fms_tran_request_vehicle WHERE id=?",
				new String[] { id });
	}

	public void deleteAssignedVehicle(String assgId, String vehicle_num)
			throws DaoException {

		super
				.update(
						"DELETE FROM fms_tran_request_assignment WHERE id=? AND vehicle_num=?",
						new String[] { assgId, vehicle_num });
	}

	public void deleteAssignedDriver(String assgId, String manpowerId)
			throws DaoException {

		super
				.update(
						"DELETE FROM fms_tran_request_driver WHERE assignmentId=? AND manpowerId=?",
						new String[] { assgId, manpowerId });
	}

	public void insertTransportRequest(TransportRequest tr) throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_request(id, requestTitle, requestType, program, startDate, endDate, destination, purpose, remarks,"
								+ "status, reason, requestBy, requestDate, updatedBy, updatedDate, approvedBy, approvedDate, blockBooking, engineeringRequestId) VALUES "
								+ "(#id#, #requestTitle#, #requestType#, #program#, #startDate#, #endDate#, #destination#, #purpose#, #remarks#,"
								+ "#status#, #reason#, #requestBy#, #requestDate#, #updatedBy#, #updatedDate#, #approvedBy#, #approvedDate#, #blockBooking#, #engineeringRequestId#)",
						tr);

	}

	public void updateTransportRequest(TransportRequest tr) throws DaoException {

		String sql = "UPDATE fms_tran_request SET requestTitle=#requestTitle#, requestType=#requestType#, program=#program#, "
			+ "startDate=#startDate#, endDate=#endDate#, destination=#destination#, purpose=#purpose#, remarks=#remarks#,"
			+ "status=#status#, reason=#reason#, requestBy=#requestBy#, requestDate=#requestDate#, updatedBy=#updatedBy#, "
			+ "updatedDate=#updatedDate#, approvedBy=#approvedBy#, approvedDate=#approvedDate#, statusRequest=#statusRequest#, "
			+ "rate=#rate#, blockBooking=#blockBooking#, rateVehicle=#rateVehicle#	 "
			+ "WHERE id=#id#";
		
		super.update(sql, tr);
	}

	public Collection selectAllTransportRequest(String filter,
			String statusFil, String department, String sort, String userId,
			boolean desc, int startIndex, int maxRows) throws DaoException {

		ArrayList args = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		if (sort != null) {
			if ("id".equals(sort))
				sort = "r.id";

			strSort += " ORDER BY " + sort;
			if (desc)
				strSort += " DESC";
		} else
			strSort += " ORDER BY requestDate DESC";

		String filterClause = "";
		if (filter != null && !filter.equals("")) {
			filterClause = "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0) {
			filterUser = "  AND r.requestBy='" + userId + "' ";
		}

		if (statusFil != null && statusFil.trim().length() > 0) {
			catFilterClause = " AND r.status='" + statusFil + "' ";
		}

		if (!("".equals(department) || null == department)) {
			catFilterClause += "AND su.department='" + department + "'  ";
		}

		String sql = "SELECT DISTINCT r.id, r.* FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id  "
				+ "WHERE 1=1 "
				+ filterClause + filterUser + catFilterClause + strSort;

		return super.select(sql, TransportRequest.class, args.toArray(), startIndex,
				maxRows);

	}

	public int selectAllCountTransportRequest(String filter, String statusFil,
			String department, String userId) throws DaoException {

		ArrayList args = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		String filterClause = "";
		if (filter != null && !filter.equals("")) {
			filterClause = "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}
		
		if (userId != null && userId.trim().length() > 0)
			// filterUser = "  AND requestBy=?";
			filterUser = "  AND r.requestBy='" + userId + "' ";

		if (statusFil != null && statusFil.trim().length() > 0) {
			catFilterClause = " AND r.status='" + statusFil + "' ";
		}

		if (!("".equals(department) || null == department)) {
			catFilterClause += "AND su.department='" + department + "'  ";
		}

		String sql = "SELECT count(*) AS total FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id  "
				+ filterClause + filterUser + catFilterClause + strSort;

		// return super.select(sql,TransportRequest.class, null
		// ,startIndex,maxRows);

		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());

	}

	public Collection selectIncomingRequestHOD(String filter, String statusFil,
			String department, String sort, String userId, boolean desc,
			int startIndex, int maxRows) throws DaoException {

		ArrayList args = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		if (sort != null) {
			strSort += " ORDER BY " + sort;

			if ("id".equals(sort))
				sort = "r.id";

			if (desc)
				strSort += " DESC";
		} else
			strSort += " ORDER BY requestDate DESC";

		String filterClause = "";
		if (filter != null && !filter.equals("")) {
			filterClause += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0)
			// filterUser = "  AND requestBy=?";
			filterUser = "  AND r.approvedBy='" + userId + "' ";

		if (statusFil != null && statusFil.trim().length() > 0) {
			catFilterClause = " AND r.status='" + statusFil + "' ";
		}

		if (!("".equals(department) || null == department)) {
			catFilterClause += "AND su.department='" + department + "'  ";
		}

		String sql = "SELECT DISTINCT r.id, r.* FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id  "
				+ "WHERE 1=1 "
				+ filterClause + filterUser + catFilterClause + strSort;

		return super.select(sql, TransportRequest.class, args.toArray(), startIndex,
				maxRows);

	}

	public int selectCountIncomingRequestHOD(String filter, String statusFil,
			String department, String userId) throws DaoException {

		ArrayList args = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		String filterClause = "";
		if (filter != null && !filter.equals("")) {
			filterClause += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0)
			// filterUser = "  AND requestBy=?";
			filterUser = "  AND r.approvedBy='" + userId + "' ";

		if (statusFil != null && statusFil.trim().length() > 0) {
			catFilterClause = " AND r.status='" + statusFil + "' ";
		}

		if (!("".equals(department) || null == department)) {
			catFilterClause += "AND su.department='" + department + "'  ";
		}

		String sql = "SELECT COUNT(*) AS total FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id  "
				+ "WHERE 1=1 "
				+ filterClause + filterUser + catFilterClause + strSort;

		// return super.select(sql,TransportRequest.class, null
		// ,startIndex,maxRows);
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());

	}

	public TransportRequest selectAllTransportRequest(String id)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"SELECT a.*,a.program,b.category_id,c.name FROM fms_tran_request a "
								+ "inner join fms_tran_request_vehicle b ON a.id=b.requestId "
								+ "inner join fms_tran_category c ON b.category_id=c.setup_id "
								+ "where a.id = ?", TransportRequest.class,
						new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
			// R.setDeptApprover(selectFMSUnitApprovers(id));
		}

		return TR;
	}

	public Collection selectAllComingRequest(Date from, Date to,
			String department, String filter, String statusFil, String sort,
			String userId, boolean desc, int startIndex, int maxRows)
			throws DaoException {

		String sql = "SELECT r.id, r.requestTitle, r.requestType, r.program, r.startDate, r.endDate, r.destination, r.purpose, r.remarks,"
				+ "r.status, r.reason, r.requestBy, r.requestDate, r.updatedBy, r.updatedDate, r.approvedBy, r.approvedDate, r.statusRequest "
				+ "FROM fms_tran_request r "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id "
				+ "INNER JOIN fms_department d ON (su.department = d.id) WHERE 1=1 ";

		String assignedBy = Application.getInstance().getCurrentUser().getId();
		ArrayList args = new ArrayList();

		if (filter != null && !filter.equals("")) {
			sql += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0) {
			// filterUser = "  AND requestBy=?";
			sql += "  AND r.requestBy='" + userId + "' ";
		}
		if (statusFil != null && statusFil.trim().length() > 0) {

			if (SetupModule.ASSIGNED_STATUS.equals(statusFil))
				sql += " AND (r.status='" + statusFil + "' AND r.updatedBy='"
						+ assignedBy + "') ";
			else
				sql += " AND r.status='" + statusFil + "' ";
		} else {

			statusFil = SetupModule.OUTSOURCED_STATUS
					+ "' "
					+ // all just show certain status
					" OR r.status='" + SetupModule.PROCESS_STATUS + "' "
					+ " OR r.status='" + SetupModule.REJECTED_STATUS + "' "
					+ " OR r.status='" + SetupModule.APPROVED_STATUS + "' "
					+ " OR (r.status='" + SetupModule.ASSIGNED_STATUS
					+ "' AND r.updatedBy='" + assignedBy + "') "
					+ " OR r.status='" + SetupModule.OUTSOURCED_STATUS;

			sql += " AND ( r.status='" + statusFil + "' )";
		}

		if (!("".equals(department) || null == department)
				&& !"-1".equals(department)) {
			sql += "AND d.id='" + department + "'  ";
		}

		if (from != null && to != null) {
			// sql +=
			// " AND ((r.startDate >= ? AND r.startDate <= ?) OR (r.startDate <= ? AND r.endDate >= ?))";
			// args.add(from);
			// args.add(to);
			// args.add(from);
			// args.add(from);
			sql += " AND (("
					+ "CAST((STR( YEAR( r.startDate ) ) + '/' + STR( MONTH( r.startDate ) ) + '/' +STR( DAY( r.startDate ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?) OR "
					+ "(CAST((STR( YEAR( r.endDate ) ) + '/' + STR( MONTH( r.endDate  ) ) + '/' +STR( DAY( r.endDate  ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?)) ";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			args.add(sdf.format(from));
			args.add(sdf.format(to));
			args.add(sdf.format(from));
			args.add(sdf.format(to));
		}

		if (sort != null) {
			if ("id".equals(sort)) {
				sort = "r.id";
			} else if ("requiredDateFrom".equals(sort)) {
				sort = "r.startDate";
			} else if ("status".equals(sort)) {
				sort = "r.status";
			} else if ("requestBy".equals(sort)) {
				sort = "su.firstName";
			}

			sql += " ORDER BY " + sort;

			if (desc)
				sql += " DESC";
		} else
			sql += " ORDER BY r.requestDate DESC";

		return super.select(sql, TransportRequest.class, args.toArray(),
				startIndex, maxRows);

	}

	public int selectCountAllComingRequest(Date from, Date to,
			String department, String filter, String statusFil, String userId)
			throws DaoException {

		Collection args = new ArrayList();
		String assignedBy = Application.getInstance().getCurrentUser().getId();

		String sql = "SELECT COUNT(r.id) AS total FROM fms_tran_request r "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id "
				+ "INNER JOIN fms_department d ON (su.department = d.id) WHERE 1=1";

		if (filter != null && !filter.equals("")) {
			sql += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (!"".equals(statusFil) && !"null".equals(statusFil)
				&& statusFil != null) {
			if (SetupModule.ASSIGNED_STATUS.equals(statusFil)) {
				sql = sql + " AND (r.status = ? AND r.updatedBy = ?) ";
				args.add(new String[] { statusFil, assignedBy });
			}

			else {
				sql = sql + " AND r.status = ? ";
				args.add(statusFil);
			}
		} else {

			statusFil = SetupModule.OUTSOURCED_STATUS
					+ "' "
					+ // all just show certain status
					" OR r.status='" + SetupModule.PROCESS_STATUS + "' "
					+ " OR r.status='" + SetupModule.REJECTED_STATUS + "' "
					+ " OR r.status='" + SetupModule.APPROVED_STATUS + "' "
					+ " OR (r.status='" + SetupModule.ASSIGNED_STATUS
					+ "' AND updatedBy='" + assignedBy + "') "
					+ " OR r.status='" + SetupModule.OUTSOURCED_STATUS;

			sql = sql + "  AND ( r.status='" + statusFil + "' )";
		}

		if (!"".equals(userId) && !"null".equals(userId) && userId != null) {
			sql = sql + "  AND r.requestBy = ? ";
			args.add(userId);
		}

		if (!("".equals(department) || null == department)
				&& !"-1".equals(department)) {
			sql += "AND d.id='" + department + "'  ";
		}

		if (from != null && to != null) {
			// sql +=
			// " AND ((r.startDate >= ? AND r.startDate <= ?) OR (r.startDate <= ? AND r.endDate >= ?))";
			// args.add(from);
			// args.add(to);
			// args.add(from);
			// args.add(from);
			sql += " AND (("
					+ "CAST((STR( YEAR( r.startDate ) ) + '/' + STR( MONTH( r.startDate ) ) + '/' +STR( DAY( r.startDate ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?) OR "
					+ "(CAST((STR( YEAR( r.endDate ) ) + '/' + STR( MONTH( r.endDate  ) ) + '/' +STR( DAY( r.endDate  ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?)) ";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			args.add(sdf.format(from));
			args.add(sdf.format(to));
			args.add(sdf.format(from));
			args.add(sdf.format(to));
		}

		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public void updateStatusFromEngineering(String status, String reason,
			String engRequestId, String userId, Date updateDate)
			throws DaoException {

		super
				.update(
						"UPDATE fms_tran_request SET status=?, reason=?, updatedBy=?, updatedDate=? WHERE engineeringRequestId=?",
						new Object[] { status, reason, userId, updateDate,
								engRequestId });
	}

	public void updateStatus(String status, String reason, String id,
			String userId, Date updateDate) throws DaoException {

		super
				.update(
						"UPDATE fms_tran_request SET status=?, reason=?, updatedBy=?, updatedDate=? WHERE id=?",
						new Object[] { status, reason, userId, updateDate, id });
	}

	public TransportRequest selectTransportRequest(String id)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super.select("select r.*, su.department "
				+ "from fms_tran_request r, " + "security_user su " + "where "
				+ "su.id=r.requestBy AND " + "r.id = ?",
				TransportRequest.class, new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public TransportRequest selectTransportRequestByEngId(String engRequestId)
			throws DaoException {
		TransportRequest TR = null;

		Collection result = super.select(
				"SELECT * FROM fms_tran_request WHERE engineeringRequestId=?",
				TransportRequest.class, new Object[] { engRequestId }, 0, 1);
		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}
	
	public Collection listTransportRequestByEngId(String engRequestId) throws DaoException {
		Collection result = super.select(
				"SELECT * FROM fms_tran_request WHERE engineeringRequestId=?",
				TransportRequest.class, new Object[] { engRequestId }, 0, -1);
		return result;
	}

	public OutsourceObject selectTransportOutsource(String id)
			throws DaoException {

		OutsourceObject oO = null;

		Collection result = super
				.select(
						"SELECT o.id, o.requestId, r.requestTitle,r.requestType,r.startDate,r.endDate,  "
								+ "o.quotationNo,o.quotationPrice,o.invoiceNo,o.invoicePrice,o.remark  "
								+ "from fms_tran_request r  "
								+ "INNER JOIN fms_tran_request_outsource o ON o.requestId = r.id AND  "
								+ "r.id = ?", OutsourceObject.class,
						new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			oO = (OutsourceObject) result.iterator().next();
			oO.setSetup_id(selectOutsources(oO.getId()));
		}

		return oO;
	}
	
	public void deleteTransportAssignmentByRequestId(String id) throws DaoException {

		super.update("DELETE FROM fms_tran_assignment WHERE requestId=?",
				new String[] { id });
	}
	
	public void deleteTransportRequestVehicleByRequestId(String id) throws DaoException {

		super.update("DELETE FROM fms_tran_request_assignment WHERE requestId=?",
				new String[] { id });
	}
	
	public void deleteTransportRequestDriverByRequestId(String id) throws DaoException {

		super.update("DELETE FROM fms_tran_request_driver WHERE requestId=?",
				new String[] { id });
	}

	public void deleteVehicles(String requestId) throws DaoException {

		super.update("DELETE FROM fms_tran_request_vehicle WHERE requestId=?",
				new String[] { requestId });
	}

	public void approveStatus(String status, String reason, String userId,
			String statusRequest, String id) throws DaoException {

		Date dateNow = new Date(); // for approved date

		super
				.update(
						"UPDATE fms_tran_request SET status=?, reason=?, approvedBy=?, approvedDate=?, statusRequest=?  WHERE id=?",
						new Object[] { status, reason, userId, dateNow,
								statusRequest, id });
	}

	public Collection selectVehicleCategory(String setup_id)
			throws DaoException {

		String sql = "SELECT setup_id,name,description,status,createdBy,createdby_date,updatedby,updatedby_date "
				+ "FROM fms_tran_category where setup_id = ?";
		return super.select(sql, SetupObject.class, new String[] { setup_id },
				0, -1);

	}

	public Collection selectVehicleByAssignment(String vehicle_num)
			throws DaoException {
		String sql = "SELECT ca.name as category_name, v.vehicle_num,v.category_id FROM fms_tran_vehicle v, fms_tran_category ca  "
				+ "WHERE 1=1 AND  v.category_id=ca.setup_id AND v.vehicle_num = ?";
		return super.select(sql, VehicleObject.class,
				new String[] { vehicle_num }, 0, -1);

	}

	public Collection selectDriversByLeave(String userId, Date start, Date end)
			throws DaoException {
		// //
		String filterDate = "";
		Object arg[] = new Object[] { userId, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(a.dateFrom  >= ? AND a.dateTo <= ?) ";

		}
		// //

		String sql = "SELECT DISTINCT b.manpowerId,a.dateFrom as startDate,a.dateTo as endDate,c.leaveType	"
				+ "FROM fms_manpow_leave_setup a, fms_manpower_leave b, fms_leaveType_setup c		"
				+ "WHERE b.leaveId=a.id AND (a.leaveType=c.id) AND b.manpowerId = ?"
				+ filterDate;

		return super.select(sql, ManpowerLeaveObject.class, arg, 0, -1);

	}

	public Collection selectDriversLeave(String userId) throws DaoException {
		String sql = "SELECT DISTINCT b.manpowerId,a.dateFrom as startDate,a.dateTo as endDate,c.leaveType	"
				+ "FROM fms_manpow_leave_setup a, fms_manpower_leave b, fms_leaveType_setup c		"
				+ "WHERE b.leaveId=a.id AND (a.leaveType=c.id) AND b.manpowerId = ?";// +
		// filterDate;

		return super.select(sql, ManpowerLeaveObject.class,
				new Object[] { userId }, 0, -1);

	}

	public Collection selectDriversByAssignment(String userId, Date start,
			Date end) throws DaoException {

		// //
		String filterDate = "";
		Object arg[] = new Object[] { userId, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(startDate >= ? AND endDate <= ?) ";

		}
		// //

		String sql = "SELECT DISTINCT R.id as requestId,R.requestTitle,D.manpowerId,  "
				+ "R.startDate, R.endDate  FROM fms_tran_request_driver D  "
				+ "INNER JOIN fms_tran_request R ON D.requestId = R.id   "
				+ "INNER JOIN security_user SU ON SU.id =  D.manpowerId  "
				+ "WHERE 1=1 AND D.manpowerId = ?" + filterDate;
		return super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);

	}

	public Collection selectDriversDuty(String userId, Date start, Date end)
			throws DaoException {
		String filterDate = "";
		Object arg[] = new Object[] { userId, start, end, start, end, start,
				end, end, start, start, end };

		if (start != null && end != null) {
			filterDate += " AND "
					+

					"((A.startDate >= ?  AND A.endDate <= ?) OR  "
					+ "(A.startDate <= ? AND A.endDate >= ?)  OR  "
					+ "(A.startDate >= ? AND A.startDate <= ? AND A.endDate >= ?) OR  "
					+ "(A.startDate <= ? AND A.endDate >= ? AND A.endDate <= ?))  ";
		}

		/*
		 * String sql2 =
		 * "SELECT DISTINCT R.id as requestId,R.requestTitle,D.manpowerId,	 "+
		 * "R.startDate, R.endDate, R.destination FROM fms_tran_request_driver D	 "
		 * + "INNER JOIN fms_tran_request R ON D.requestId = R.id 	"+
		 * "WHERE 1=1 AND  "+ "D.manpowerId = ?" + filterDate;
		 */

		String sql = "SELECT DISTINCT A.id, R.requestTitle, D.manpowerId, D.requestId as id, "
				+ "A.startDate, A.endDate, R.destination FROM fms_tran_request_driver D	 "
				+ "INNER JOIN fms_tran_assignment A ON A.id = D.assignmentId 	"
				+ "INNER JOIN fms_tran_request R ON D.requestId = R.id	"
				+ "WHERE 1=1 AND  D.manpowerId = ?  " + filterDate;

		return super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);
	}

	public Collection selectEngineeringDuty(String userId, Date start, Date end)
			throws DaoException {
		Collection duties = new ArrayList();

		String filterDate = "AND M.requiredFrom <= ? AND M.requiredTo >= ? ";
		Object arg[] = new Object[] { userId, end, start };
		
		String sql = "SELECT R.requestId as id, R.title as requestTitle,M.userId,M.requiredFrom as startDate,M.requiredTo as endDate, "
				+ "M.fromTime as startTime, M.toTime as endTime  FROM fms_eng_request R	 "
				+ "INNER JOIN fms_eng_assignment A ON R.requestId=A.requestId  	"
				+ "INNER JOIN fms_eng_assignment_manpower M ON A.assignmentId = M.assignmentId   "
				+ "WHERE 1=1 AND M.userId = ? " + filterDate;

		duties = super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);

		return duties;
	}

	public Collection selectHolidays(String year, String month)
			throws DaoException {
		Collection holidays = new ArrayList();

		String sql = "SELECT id,holiday,dateFrom,dateTo FROM fms_manpow_leave_setup WHERE type='H' AND (year(dateFrom) = "
				+ year + ")	" + "AND (month(dateFrom) = " + month + ")";
		holidays = super.select(sql, HashMap.class, null, 0, -1);
		return holidays;
	}

	public Collection selectWorkingProfile(String userId, Date start, Date end) throws DaoException {
		Object arg[] = new Object[] { userId, end, start };
		
		String sql = "SELECT DISTINCT(Man.userId), Pro.name AS workingProfileCode, Dur.startDate AS workStart,Dur.endDate AS workEnd  "
				+ "FROM fms_working_profile_duration_manpower Man  "
				+ "INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId  "
				+ "INNER JOIN fms_working_profile Pro ON Pro.workingProfileId = Dur.workingProfileId  "
				+ "WHERE 1=1 "
				+ "AND Man.userId = ? "
				+ "AND Dur.startDate <= ? "
				+ "AND Dur.endDate >=  ? ";

		return super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);
	}

	public String userWorkingProfile(String userId, Date startDate, Date endDate)
			throws DaoException {

		ArrayList params = new ArrayList();

		String workprofile = null;

		String sql = "";
		Collection col = new ArrayList();

		// Set to default workprofile
		sql = "SELECT name AS workprofile	"
				+ "FROM fms_working_profile WHERE defaultProfile=1";
		col = super.select(sql, HashMap.class, params.toArray(), 0, 1);
		if (col != null) {
			if (col.size() > 0) {
				HashMap map = (HashMap) col.iterator().next();
				workprofile = map.get("workprofile").toString();
			}
		}

		sql = "SELECT Man.userId, Pro.name AS workprofile 	"
				+ "FROM fms_working_profile_duration_manpower Man  	"
				+ "INNER JOIN fms_working_profile_duration Dur ON Dur.workingProfileDurationId=Man.workingProfileDurationId	"
				+ "INNER JOIN fms_working_profile Pro ON Pro.workingProfileId = Dur.workingProfileId  "
				+ "WHERE 1=1";

		if (startDate != null) {
			sql += " AND ((Dur.startDate BETWEEN ? AND ?) "
					+ " OR (Dur.endDate BETWEEN ? AND ?)) ";
			params.add(startDate);
			params.add(endDate);
			params.add(startDate);
			params.add(endDate);
		}

		if (userId != null || !"".equals(userId)) {
			sql += " AND ( Man.userId = ?) ";
			params.add(userId);
		}

		col = super.select(sql, HashMap.class, params.toArray(), 0, 1);
		if (col != null) {
			if (col.size() > 0) {
				HashMap map = (HashMap) col.iterator().next();
				workprofile = map.get("workprofile").toString();
			}
		} else {

		}

		return workprofile;

	}

	public Collection selectAssigmentByVehicle(String vehicle_num, Date start,
			Date end) throws DaoException {
		// //
		String filterDate = "";
		Object arg[] = new Object[] { vehicle_num, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(r.startDate >= ? AND r.endDate <= ?) ";

		}
		// //

		String sql = "SELECT DISTINCT v.vehicle_num, ca.name as category_name, r.startDate as startDate,r.endDate as endDate,r.id as requestId,v.category_id "
				+ "FROM fms_tran_vehicle v, fms_tran_category ca, fms_tran_request_assignment ra, fms_tran_request r "
				+ "WHERE 1=1 AND "
				+ "v.category_id=ca.setup_id AND "
				+ "ra.vehicle_num=v.vehicle_num AND "
				+ "ra.requestId = r.id "
				+ "AND v.vehicle_num  = ?" + filterDate;
		return super.select(sql, VehicleObject.class, arg, 0, -1);

	}

	public Collection selectAssigmentByDriver(String userId, Date start,
			Date end) throws DaoException {
		// //
		String filterDate = "";
		Object arg[] = new Object[] { userId, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(startDate >= ? AND endDate <= ?) ";

		}

		String sql = "SELECT DISTINCT d.manpowerId, d.assignmentId, a.startDate, a.endDate, r.id as requestId "
				+ "FROM fms_tran_request_driver d	 "
				+ "INNER JOIN fms_tran_assignment a ON a.id=d.assignmentId	 "
				+ "INNER JOIN fms_tran_request r ON a.requestId=r.id	 	"
				+ "WHERE 1=1 AND d.manpowerId  = ?" + filterDate;

		return super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);

	}

	public Collection selectAssigmentByRequestId(String requestId, String assgId)
			throws DaoException {

		String sql = "SELECT DISTINCT D.manpowerId, a.id, R.requestTitle,	a.startDate, a.endDate  "
				+ "FROM fms_tran_request_driver D    "
				+ "INNER JOIN fms_tran_assignment a ON a.id=D.assignmentId   "
				+ "INNER JOIN fms_tran_request R ON D.requestId = R.id	"
				+ "INNER JOIN security_user SU ON SU.id =  D.manpowerId  " +
				// "WHERE 1=1 AND D.manpowerId = ?";
				"WHERE 1=1 AND	a.requestId=? AND a.id=?	";

		return super.select(sql, ManpowerAssignmentObject.class, new Object[] {
				requestId, assgId }, 0, -1);

	}

	public void insertVehicleAssigment(VehicleObject vo) throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_request_assignment(id, requestId, flagId, vehicle_num, startDate, endDate) VALUES (#id#, #requestId#, #flagId#, #vehicle_num#, #startDate#, #endDate#)",
						vo);

	}

	public void insertDriverAssigment(ManpowerLeaveObject ml)
			throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_request_driver(assignmentId, requestId, flagId, manpowerId) VALUES (#assignmentId#, #requestId#, #flagId#, #manpowerId#)",
						ml);

	}

	public Collection selectVehicleByRequestId(String requestId)
			throws DaoException {

		/*String sql = "SELECT DISTINCT v.vehicle_num, ca.name as category_name, r.startDate as startDate,r.endDate as endDate,r.id as requestId, "
				+ "FROM fms_tran_vehicle v, fms_tran_category ca, fms_tran_request_assignment ra, fms_tran_request r "
				+ "WHERE 1=1 AND "
				+ "v.category_id=ca.setup_id AND "
				+ "ra.vehicle_num=v.vehicle_num AND "
				+ "ra.requestId = r.id "
				+ "AND r.id  = ?";*/
		String sql = "SELECT DISTINCT v.vehicle_num, ca.name as category_name, r.startDate as startDate,r.endDate as endDate,r.id as requestId," +
				"ra.id, rad.status " +
				"FROM fms_tran_vehicle v, fms_tran_category ca, fms_tran_request_assignment ra, fms_tran_request r, fms_tran_request_assignment_details rad " +
				"WHERE 1=1 " +
				"AND v.category_id=ca.setup_id " +
				"AND ra.vehicle_num=v.vehicle_num " +
				"AND ra.requestId = r.id " +
				"AND ra.id=rad.assgId " +
				"AND r.id  = ?";
		
		return super.select(sql, VehicleObject.class,new String[] { requestId }, 0, -1);
	}

	public Collection selectDriverByRequestId(String requestId)
			throws DaoException {
		String sql = "SELECT DISTINCT D.*,R.startDate, R.endDate,R.id as id  FROM fms_tran_request_driver D, fms_tran_request R "
				+ "WHERE 1=1 AND D.requestId = R.id AND R.id = ?";

		return super.select(sql, ManpowerLeaveObject.class,
				new String[] { requestId }, 0, -1);

	}

	public Collection selectDriverByAssgId(String assgId) throws DaoException {

		String sql = "SELECT DISTINCT D.*,A.startDate, A.endDate,A.id  FROM fms_tran_request_driver D, fms_tran_assignment A	 "
				+ "WHERE 1=1 AND D.assignmentId = A.id AND A.id = ?";

		return super.select(sql, ManpowerLeaveObject.class,
				new String[] { assgId }, 0, -1);

	}

	public void addOutsource(OutsourceObject oo) throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_request_outsource(id, startDate, endDate, requestId, quotationNo, quotationPrice, invoiceNo, invoicePrice, "
								+ "remark, createdBy, createdDate, updatedBy, updatedDate) VALUES (#id#, #startDate#, #endDate#, #requestId#, #quotationNo#, #quotationPrice#, #invoiceNo#, #invoicePrice#, "
								+ "#remark#, #createdBy#, #createdDate#, #updatedBy#, #updatedDate#)",
						oo);

		addOutsourcePanel(oo);

	}

	public void addOutsourcePanel(OutsourceObject oo) throws DaoException {
		String[] str = oo.getSetup_id();
		for (int i = 0; i < str.length; i++) {

			super.update(
					"INSERT INTO fms_tran_request_outsource_panel(outsourceId,setup_id) "
							+ "VALUES(?,?)",
					new Object[] { oo.getId(), str[i] });
		}
	}

	public String[] selectOutsources(String outsourceId) throws DaoException {
		Collection outList = new ArrayList();
		String sql = "SELECT setup_id FROM fms_tran_request_outsource_panel  "
				+ "WHERE 1=1 AND outsourceId = ?";

		Object[] args = new Object[] { outsourceId };

		Collection tmp = super.select(sql.toString(), HashMap.class, args, 0,
				-1);
		for (Iterator i = tmp.iterator(); i.hasNext();) {
			HashMap m = (HashMap) i.next();
			outList.add(m.get("setup_id"));
		}

		return (String[]) outList.toArray(new String[0]);

	}

	public String[] selectFMSDepartmentApprovers(String deptId)
			throws DaoException, SecurityException {
		Collection userIdList = new ArrayList();

		String sql = "SELECT userId as userId FROM fms_department_alternate_approver WHERE departmentId=?";
		Object[] args = new Object[] { deptId };

		Collection tmp = super.select(sql.toString(), HashMap.class, args, 0,
				-1);
		for (Iterator i = tmp.iterator(); i.hasNext();) {
			HashMap m = (HashMap) i.next();
			userIdList.add(m.get("userId"));
		}

		return (String[]) userIdList.toArray(new String[0]);
	}

	// //Assigment

	public Collection selectTodayAssignment(String filter, String filDept,
			Date start, Date end, String sort, String userId, boolean desc,
			int startIndex, int maxRows) throws DaoException {

		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		if (sort != null) {
			strSort += " ORDER BY " + sort;
			if (desc)
				strSort += " DESC";
		} else
			strSort += " ORDER BY startDate ASC";

		String filterClause = " AND requestTitle LIKE '%"
				+ (filter == null ? "" : filter) + "%'  ";

		if (userId != null && userId.trim().length() > 0)
			filterUser = "  AND requestBy='" + userId + "' ";

		if (filDept != null && filDept.trim().length() > 0) {
			catFilterClause = " AND department='" + filDept + "' ";
		}

		String filterDate = "";
		Object arg[] = new Object[] { start, end };
		if (start != null && end != null) {
			filterDate += " AND " + "(startDate >= ? AND endDate <= ?) ";
		}

		Collection requests = new ArrayList();

		String sql = "select DISTINCT a.id as id,r.id as requestId,r.requestTitle,r.requestBy,r.startDate as startDate,r.endDate,r.status,d.manpowerId,a.vehicle_num,su.department "
				+ "from fms_tran_request r, "
				+ "fms_tran_request_driver d, "
				+ "fms_tran_request_assignment a, "
				+ "security_user su "
				+ "where r.id=d.requestId AND "
				+ "r.id=a.requestId AND "
				+ "su.id=r.requestBy "
				+ filterClause
				+ filterUser
				+ catFilterClause + filterDate + strSort;

		requests = super.select(sql, TransportRequest.class, arg, startIndex,
				maxRows);

		if (requests.size() <= 0) {

			String sqlNoDriver = "select DISTINCT a.id as id, r.id as requestId, r.requestTitle,r.requestBy,r.startDate as startDate,r.endDate,r.status,a.vehicle_num,su.department "
					+ "from fms_tran_request r, "
					+ "fms_tran_request_assignment a, "
					+ "security_user su "
					+ "WHERE r.id=a.requestId AND "
					+ "su.id=r.requestBy "
					+ filterClause
					+ filterUser
					+ catFilterClause
					+ filterDate
					+ strSort;

			requests = super.select(sqlNoDriver, TransportRequest.class, arg,
					startIndex, maxRows);
		}

		return requests;

	}

	public Collection selectAssignments(int settingValue, String filter, String filDept,
			Date start, Date end, String sort, String userId, boolean desc,
			int startIndex, int maxRows) throws DaoException {

		Collection args = new ArrayList();
		Collection requests = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";
		String daysSetting = "";
		Date dateMax = new Date();
		dateMax.setDate(dateMax.getDate()+settingValue-1);  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if (sort != null) {
			if ("id".equals(sort)) {
				sort = "a.id";
			}

			strSort += " ORDER BY " + sort;
			if (desc) {
				strSort += " DESC";
			}
		} else {
			strSort += " ORDER BY a.startDate ASC";
		}

		String filterClause = " AND (r.requestTitle LIKE '%"+ (filter == null ? "" : filter) + "%'  "
				+ "OR ra.vehicle_num LIKE '%" + (filter == null ? "" : filter)+ "%' " 
				+ "OR a.id LIKE '%" + (filter == null ? "" : filter)+ "%' " 
				+ "OR pr.programName LIKE '%"+ (filter == null ? "" : filter) + "%' " 
				+ "OR su.firstName LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+ "OR su.lastName LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+ "OR r.destination LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+ ") ";

		if (userId != null && userId.trim().length() > 0)
			filterUser = "  AND r.requestBy='" + userId + "' ";

		if (filDept != null && filDept.trim().length() > 0) {
			catFilterClause = " AND su.department='" + filDept + "' ";
		}
		if (settingValue!=0){
			daysSetting = 
				"AND (((CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' + " +
				"STR( DAY( a.startDate ) )) AS DATETIME) <=? ) AND (CAST((STR( YEAR( a.endDate ) ) + '/' + " +
				"STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) > ?)) " +
				"OR ((CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' + " +
				"STR( DAY( a.startDate ) )) AS DATETIME) <=? ) AND (CAST((STR( YEAR( a.endDate ) ) + '/' + " +
				"STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) > ?))) ";
			args.add(sdf.format(start));
			args.add(sdf.format(start));
			args.add(sdf.format(dateMax));
			args.add(sdf.format(dateMax));
		}
		
		else{
			daysSetting = " AND (("
				+ "CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' +STR( DAY( a.startDate ) )) AS DATETIME) "
				+ "BETWEEN ? AND ?) OR "
				+ "(CAST((STR( YEAR( a.endDate ) ) + '/' + STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) "
				+ "BETWEEN ? AND ?)) ";
			args.add(sdf.format(start));
			args.add(sdf.format(end));
			args.add(sdf.format(start));
			args.add(sdf.format(end));
		}

//		String filterDate = "";
//		if (start != null && end != null) {
//			//filterDate += " AND "
//					//+
//					// "(startDate >= ? AND endDate <= ?) "; -problem if endDate
//					// > today
//					// "(a.startDate >= ? AND a.endDate <= ?) ";
//					//" ((a.startDate BETWEEN ? AND ?) "
//					//+ "OR ((a.endDate BETWEEN ? AND ?))) ";
//			filterDate += " AND (("
//					+ "CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' +STR( DAY( a.startDate ) )) AS DATETIME) "
//					+ "BETWEEN ? AND ?) OR "
//					+ "(CAST((STR( YEAR( a.endDate ) ) + '/' + STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) "
//					+ "BETWEEN ? AND ?)) ";
//			args.add(sdf.format(start));
//			args.add(sdf.format(end));
//			args.add(sdf.format(start));
//			args.add(sdf.format(end));
//		}

		String sql = "SELECT DISTINCT a.id, a.requestId, r.requestTitle, r.requestBy, a.startDate, a.endDate,	"
				+ "r.status, ra.vehicle_num, ra.flagId, su.department, r.destination, r.purpose	"
				+ "FROM fms_tran_assignment a		"
				+ "INNER JOIN fms_tran_request_assignment ra ON ra.id=a.id	"
				+ "INNER JOIN fms_tran_request r ON r.id=a.requestId	"
				+ "INNER JOIN security_user su ON su.id = r.requestBy		"
				+ "LEFT JOIN fms_prog_setup pr ON r.program = pr.programId "
				+ "WHERE 1=1 "
				+ daysSetting
				+ filterClause
				+ filterUser
				+ catFilterClause
//				+ filterDate 
				+ strSort;

		requests = super.select(sql, TransportRequest.class, args.toArray(), startIndex, maxRows);

		return requests;
	}

	public int selectCountAssignments(int settingValue, String filter, String filDept,
			Date start, Date end, String userId) throws DaoException {

		Collection args = new ArrayList();
		//Collection requests = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";
		String daysSetting = "";
		Date dateMax = new Date();
		dateMax.setDate(dateMax.getDate()+settingValue-1);  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// String filterClause = " AND r.requestTitle LIKE '%"+
		// (filter==null?"":filter) +"%'  ";
		String filterClause = " AND (r.requestTitle LIKE '%" + (filter == null ? "" : filter) + "%'  "
				+ "OR ra.vehicle_num LIKE '%" + (filter == null ? "" : filter)+ "%' " 
				+ "OR a.id LIKE '%" + (filter == null ? "" : filter) + "%' " 
				+ "OR pr.programName LIKE '%" + (filter == null ? "" : filter) + "%' " 
				+ "OR su.firstName LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+ "OR su.lastName LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+ "OR r.destination LIKE '%"+ (filter == null ? "" : filter) + "%' "
				+		") ";

		if (userId != null && userId.trim().length() > 0)
			filterUser = "  AND r.requestBy='" + userId + "' ";

		if (filDept != null && filDept.trim().length() > 0) {
			catFilterClause = " AND su.department='" + filDept + "' ";
		}
		
		if (settingValue!=0){
			daysSetting = 
				" AND ((CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' + " +
				"STR( DAY( a.startDate ) )) AS DATETIME) <=? ) AND " +
				"(CAST((STR( YEAR( a.endDate ) ) + '/' + STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) " +
				"> ?)) ";
			args.add(sdf.format(start));
			args.add(sdf.format(dateMax));
		}
		else{
			daysSetting = " AND (("
				+ "CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' +STR( DAY( a.startDate ) )) AS DATETIME) "
				+ "BETWEEN ? AND ?) OR "
				+ "(CAST((STR( YEAR( a.endDate ) ) + '/' + STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) "
				+ "BETWEEN ? AND ?)) ";
			args.add(sdf.format(start));
			args.add(sdf.format(end));
			args.add(sdf.format(start));
			args.add(sdf.format(end));
		}
//		String filterDate = "";
//		if (start != null && end != null) {
//			//filterDate += " AND "
//					//+
//					// "(startDate >= ? AND endDate <= ?) "; -problem if endDate
//					// > today
//					// "(a.startDate >= ? AND a.endDate <= ?) ";
//					//" ((a.startDate BETWEEN ? AND ?) "
//					//+ "OR ((a.endDate BETWEEN ? AND ?))) ";
//			filterDate += " AND (("
//					+ "CAST((STR( YEAR( a.startDate ) ) + '/' + STR( MONTH( a.startDate ) ) + '/' +STR( DAY( a.startDate ) )) AS DATETIME) "
//					+ "BETWEEN ? AND ?) OR "
//					+ "(CAST((STR( YEAR( a.endDate ) ) + '/' + STR( MONTH( a.endDate  ) ) + '/' +STR( DAY( a.endDate  ) )) AS DATETIME) "
//					+ "BETWEEN ? AND ?)) ";
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			
//			args.add(sdf.format(start));
//			args.add(sdf.format(end));
//			args.add(sdf.format(start));
//			args.add(sdf.format(end));
//		}

		String sql = "SELECT COUNT (*) AS total 	"
				+ "FROM fms_tran_assignment a		"
				+ "INNER JOIN fms_tran_request_assignment ra ON ra.id=a.id	"
				+ "INNER JOIN fms_tran_request r ON r.id=a.requestId	"
				+ "INNER JOIN security_user su ON su.id = r.requestBy		"
				+ "LEFT JOIN fms_prog_setup pr ON r.program = pr.programId "
				+ "WHERE 1=1 " + daysSetting + filterClause + filterUser + catFilterClause
//				+ filterDate 
				+ strSort;

		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1).iterator()
				.next();
		return Integer.parseInt(row.get("total").toString());

	}

	public Collection selectDriverAssignments(String filter, Date start,
			Date end, String sort, String userId, boolean desc, int startIndex,
			int maxRows) throws DaoException {

		Collection requests = new ArrayList();
		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		if (sort != null) {

			strSort += " ORDER BY " + sort;
			if (desc)
				strSort += " DESC";
		} else
			strSort += " ORDER BY a.startDate ASC";

		String filterClause = " AND (a.id LIKE '%"
				+ (filter == null ? "" : filter) + "%'  "
				+ "OR r.requestTitle LIKE '%" + (filter == null ? "" : filter)
				+ "%' " + "OR su.firstName LIKE '%"
				+ (filter == null ? "" : filter) + "%' "
				+ "OR su.lastName LIKE '%" + (filter == null ? "" : filter)
				+ "%' " + "OR pr.programName LIKE '%"
				+ (filter == null ? "" : filter) + "%' ) ";

		if (userId != null && userId.trim().length() > 0)
			filterUser = "  AND r.requestBy='" + userId + "' ";

		String filterDate = "";
		Object arg[] = new Object[] { start, end };
		if (start != null && end != null) {
			filterDate += " AND " + "(a.startDate >= ? AND a.endDate <= ?) ";
		}

		String sql = "SELECT DISTINCT a.id AS id, d.flagId, d.manpowerId, "
				+ "(su.firstName + ' ' + su.lastName) AS driver, "
				+ "a.requestId, a.startDate, a.endDate,r.requestTitle,d.status AS status,"
				+ "pr.programName AS program	"
				+ "FROM fms_tran_request_driver d		"
				+ "INNER JOIN fms_tran_assignment a ON a.id=D.assignmentId	"
				+ "INNER JOIN fms_tran_request r ON D.requestId = r.id "
				+ "INNER JOIN security_user su ON (d.manpowerId = su.id) "
				+ "LEFT JOIN fms_prog_setup pr ON r.program = pr.programId "
				+ "WHERE 1=1 		" + filterClause + filterUser + catFilterClause
				+ filterDate + strSort;

		requests = super.select(sql, TransportRequest.class, arg, startIndex,
				maxRows);

		return requests;
	}

	public int selectCountDriverAssignments(String filter, Date start,
			Date end, String userId) throws DaoException {

		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		// String filterClause = " AND r.requestTitle LIKE '%"+
		// (filter==null?"":filter) +"%'  ";
		String filterClause = " AND (a.id LIKE '%"
				+ (filter == null ? "" : filter) + "%'  "
				+ "OR r.requestTitle LIKE '%" + (filter == null ? "" : filter)
				+ "%' " + "OR su.firstName LIKE '%"
				+ (filter == null ? "" : filter) + "%' "
				+ "OR su.lastName LIKE '%" + (filter == null ? "" : filter)
				+ "%'" + "OR pr.programName LIKE '%"
				+ (filter == null ? "" : filter) + "%' ) ";

		if (userId != null && userId.trim().length() > 0)
			filterUser = "  AND r.requestBy='" + userId + "' ";

		String filterDate = "";
		Object arg[] = new Object[] { start, end };
		if (start != null && end != null) {
			filterDate += " AND " + "(a.startDate >= ? AND a.endDate <= ?) ";
		}

		String sql = "SELECT COUNT (*) AS total		"
				+ "FROM fms_tran_request_driver d		"
				+ "INNER JOIN fms_tran_assignment a ON a.id=D.assignmentId	"
				+ "INNER JOIN fms_tran_request r ON D.requestId = r.id	"
				+ "INNER JOIN security_user su ON (d.manpowerId = su.id) "
				+ "LEFT JOIN fms_prog_setup pr ON r.program = pr.programId "
				+ "WHERE 1=1 		" + filterClause + filterUser + catFilterClause
				+ filterDate + strSort;

		Map row = (Map) super.select(sql, HashMap.class, arg, 0, -1).iterator()
				.next();
		return Integer.parseInt(row.get("total").toString());
	}

	public Collection selectAllRequestForBulk(String search,
			String departmentId, String programId, Date fromDate, Date toDate)
			throws DaoException {
		ArrayList params = new ArrayList();

		/*
		 * Stringsql=
		 * "SELECT DISTINCT a.id as id, r.id as requestId, r.requestTitle,(su.firstName + ' ' + su.lastName) AS name,r.status,a.vehicle_num,dept.name as department,   "
		 * +
		 * "(SELECT checkin_date FROM fms_tran_request_assignment_details WHERE assgId=a.id) as checkin_date,   "
		 * +
		 * "(SELECT checkout_date FROM fms_tran_request_assignment_details WHERE assgId=a.id) as checkout_date,  "
		 * +
		 * "(SELECT meterStart FROM fms_tran_request_assignment_details WHERE assgId=a.id) as meterStart,  "
		 * +
		 * "(SELECT meterEnd FROM fms_tran_request_assignment_details WHERE assgId=a.id) as meterEnd,  "
		 * +
		 * "(SELECT status FROM fms_tran_request_assignment_details WHERE assgId=a.id) as status,  "
		 * +
		 * "(SELECT remarks FROM fms_tran_request_assignment_details WHERE assgId=a.id) as remarks  "
		 * + " FROM fms_tran_request r  " +
		 * " INNER JOIN fms_tran_request_assignment a ON r.id=a.requestId  " +
		 * " INNER JOIN security_user su ON su.id=r.requestBy  " +
		 * " INNER JOIN fms_department dept ON su.department=dept.id  " +
		 * " WHERE 1=1 ";
		 */

		String sql = "SELECT DISTINCT assg.id as id, r.id as requestId, r.requestTitle,(su.firstName + ' ' + su.lastName) AS name," 
				+ "r.status,ra.vehicle_num,dept.name as department, p.programName,	"
				+ "(SELECT TOP 1 checkin_date FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as checkin_date,	"
				+ "(SELECT TOP 1 checkout_date FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as checkout_date, 	"
				+ "(SELECT TOP 1 meterStart FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as meterStart,  "
				+ "(SELECT TOP 1 meterEnd FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as meterEnd,  "
				+ "(SELECT TOP 1 status FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as status,  "
				+ "(SELECT TOP 1 remarks FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as remarks, "
				+ "(SELECT TOP 1 petrolCard FROM fms_tran_request_assignment_details WHERE assgId=assg.id AND vehicle_num = ra.vehicle_num) as petrolCard "
				+ "FROM fms_tran_assignment assg "
				+ "INNER JOIN fms_tran_request_assignment ra ON ra.id=assg.id "
				+ "INNER JOIN fms_tran_request r ON r.id=assg.requestId	"
				+ "INNER JOIN security_user su ON su.id = r.requestBy "
				+ "INNER JOIN fms_department dept ON su.department=dept.id 	" 
				+ "LEFT JOIN fms_prog_setup p on (r.program = p.programId) "
				+ "WHERE 1=1 	";

		if (fromDate != null && toDate != null) {
			toDate.setHours(23);
			toDate.setMinutes(59);
			toDate.setSeconds(59);
			sql += " AND ((assg.startDate BETWEEN ? AND ?) "
					+ " OR (assg.endDate BETWEEN ? AND ?)) ";
			params.add(fromDate);
			params.add(toDate);
			params.add(fromDate);
			params.add(toDate);
		}

		if (search != null && !"".equals(search)) {
			sql += " AND (ra.vehicle_num LIKE '%" + search + "%')  ";// OR
			// u.firstName
			// like
			// '%"+search+"%'
			// OR
			// u.lastName
			// like
			// '%"+search+"%'
			// OR
			// r.title
			// like
			// '%"+search+"%'
			// ) ";
			// params.add(search);
		}

		if (departmentId != null && !"-1".equals(departmentId)) {
			sql += " AND ( dept.id = ?) ";
			params.add(departmentId);
		}

		if (programId != null && !"-1".equals(programId)) {
			sql += " AND ( r.program = ?) ";
			params.add(programId);
		}

		// sql+=" GROUP BY a.assignmentId, a.code, r.title, r.requestId, rc.name ,u.firstName, u.lastName, d.name, fa.status, fa.fromTime,fa.toTime,fa.rateCardId,fa.competencyId, fa.completionDate ";

		return super.select(sql, HashMap.class, params.toArray(), 0, -1);
	}

	public Collection getVehicleByNo(String vehicle_num, Date start, Date end)
			throws DaoException {

		String filterDate = "";
		Object arg[] = new Object[] { start, end };
		if (start != null && end != null) {

			filterDate += " AND " + "(startDate >= ? AND endDate <= ?) ";
		}

		String filterVehicle = "";
		if (vehicle_num != null) {
			filterVehicle = " AND vehicle_num = '" + vehicle_num + "'";
		}

		String sql = "select r.id as id,r.requestTitle,r.startDate,r.requestBy,r.startDate,r.endDate,r.status,a.vehicle_num as vehicle_num "
				+ "from fms_tran_request r, "
				+ "fms_tran_request_assignment a "
				+ "where r.id=a.requestId "
				+ filterDate + filterVehicle;

		return super.select(sql, SetupObject.class, arg, 0, -1);
	}

	public Collection selectAssignmentDetailRequestId(String requestId)
			throws DaoException {

		/*String sql = "SELECT DISTINCT D.* FROM fms_tran_request_assignment_details D "
				+ "WHERE 1=1 AND D.assgId = ?";*/
		String sql = "SELECT DISTINCT D.*,requestId FROM fms_tran_request_assignment_details D, fms_tran_request_assignment ra " +
				"WHERE 1=1 " +
				"AND D.assgId = ra.id " +
				"AND D.assgId = ?";

		return super.select(sql, AssignmentObject.class,
				new String[] { requestId }, 0, -1);

	}

	public Collection selectAssignmentDetailVehicleNo(String vehicleNo)
			throws DaoException {

		String sql = "SELECT DISTINCT D.* FROM fms_tran_request_assignment_details D "
				+ "WHERE 1=1 AND D.vehicle_num = ?";

		return super.select(sql, AssignmentObject.class,
				new String[] { vehicleNo }, 0, -1);

	}

	public Collection selectAssignmentDetailFlagId(String flagId)
			throws DaoException {

		String sql = "SELECT DISTINCT ad.assgId, ad.* FROM fms_tran_request_assignment_details ad 	"
				+ "INNER JOIN fms_tran_request_assignment ra ON ad.assgId=ra.id	"
				+ "WHERE 1=1 AND ra.flagId = ?";

		return super.select(sql, AssignmentObject.class,
				new String[] { flagId }, 0, -1);

	}

	public Collection selectAssignmentDetailFlagIdVehicleNo(String flagId,
			String vehicleNo) throws DaoException {

		String sql = "SELECT DISTINCT ad.assgId, ad.* FROM fms_tran_request_assignment_details ad 	"
				+ "INNER JOIN fms_tran_request_assignment ra ON ad.assgId=ra.id	"
				+ "WHERE 1=1 AND ra.flagId = ? AND ad.vehicle_num = ?";

		return super.select(sql, AssignmentObject.class, new String[] { flagId,
				vehicleNo }, 0, -1);

	}

	public TransportRequest selectTransportAssignment(String id)
			throws DaoException {

		TransportRequest TR = null;

		/*
		 * Collection result =
		 * super.select("SELECT r.id as id,r.requestTitle,a.startDate,r.requestBy,"
		 * + "a.endDate,r.status,a.vehicle_num,su.department ,r.destination " +
		 * "FROM fms_tran_request r, " + "fms_tran_request_assignment a, " +
		 * "security_user su " + "WHERE r.id=a.requestId AND " +
		 * "su.id=r.requestBy AND " + "r.id = ?",TransportRequest.class, new
		 * Object[] {id}, 0, 1);
		 */

		Collection result = super
				.select(
						"SELECT r.id as id,r.requestTitle,a.startDate,r.requestBy,"
								+ "a.endDate,r.status,a.vehicle_num,su.department ,r.destination "
								+ "FROM fms_tran_request r, "
								+ "fms_tran_request_assignment a, "
								+ "security_user su "
								+ "WHERE r.id=a.requestId AND "
								+ "su.id=r.requestBy AND " + "r.id = ?",
						TransportRequest.class, new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public TransportRequest selectTransportAssignmentByAssgId(String assgId)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"SELECT DISTINCT a.id, a.requestId, r.requestTitle,r.requestBy,a.startDate,	"
								+ "a.endDate ,r.status,ra.vehicle_num,su.department,r.destination 	"
								+ "FROM fms_tran_assignment a		"
								+ "INNER JOIN fms_tran_request_assignment ra ON ra.requestId=a.requestId		"
								+ "INNER JOIN fms_tran_request r ON a.requestId=r.id		"
								+ "INNER JOIN security_user su ON su.id=r.requestBy		"
								+ "WHERE a.id = ?	 ", TransportRequest.class,
						new Object[] { assgId }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public TransportRequest selectTransportAssignmentByAssignmentId(String id)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"SELECT r.id as id,r.requestTitle,a.startDate,r.requestBy,"
								+ "a.endDate,r.status,a.vehicle_num,su.department ,r.destination "
								+ "FROM fms_tran_request r, "
								+ "fms_tran_request_assignment a, "
								+ "security_user su "
								+ "WHERE r.id=a.requestId AND "
								+ "su.id=r.requestBy AND " + "a.id = ?",
						TransportRequest.class, new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public void addAssignmentDetails(AssignmentObject AO) throws DaoException {

		super.update("INSERT INTO fms_tran_request_assignment_details(assgId, checkin_date, checkout_date, meterStart, " 
				+ "meterEnd, remarks, petrolCard, "
				+ "createdBy, createdDate, updatedBy, updatedDate, status, vehicle_num) VALUES "
				+ "(#assgId#, #checkin_date#, #checkout_date#, #meterStart#, #meterEnd#, #remarks#, #petrolCard#, "
				+ "#createdBy#, #createdDate#, #updatedBy#, #updatedDate#, #status#, #vehicle_num#)",
		AO);

	}

	public void updateAssignmentDetails(AssignmentObject AO)
			throws DaoException {

		String sql = "UPDATE fms_tran_request_assignment_details " +
				"SET checkin_date=#checkin_date#, checkout_date=#checkout_date#, " +
				"meterStart=#meterStart#, " + 
				"meterEnd=#meterEnd#, remarks=#remarks#, petrolCard=#petrolCard#, " + 
				"updatedBy=#updatedBy#, " +
				"updatedDate=#updatedDate#, status=#status# " +
				"WHERE assgId=#assgId# AND vehicle_num=#vehicle_num#";
		super.update(sql, AO);
	}
	
	public void upsertAssignmentToUnfulfilled(AssignmentObject AO) throws DaoException {
		String sql = "UPDATE fms_tran_request_assignment_details " +
				"SET remarks=#remarks#, status=#status#, " +
				"updatedBy=#updatedBy#, updatedDate=#updatedDate# " +
				"WHERE assgId=#assgId# AND vehicle_num=#vehicle_num#";
		if (super.update(sql, AO) <= 0) {
			addAssignmentDetails(AO);
		}
	}

	public void addUnfulfilledAssignment(String remarks, String status, String id) throws DaoException {
		super.update("INSERT INTO fms_tran_request_assignment_details(remarks, status, assgId) VALUES (?,?,?)",
			new Object[] { remarks, status, id });
	}

	public TransportRequest selectTransportAssignments(String id)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"select r.id as id,r.requestTitle,r.startDate,r.requestBy,"
								+ "r.startDate,r.endDate,r.status,a.vehicle_num,su.department ,r.destination "
								+ "from fms_tran_request r, "
								+ "fms_tran_request_assignment a, "
								+ "security_user su "
								+ "where r.id=a.requestId AND "
								+ "su.id=r.requestBy AND " + "r.id = ?",
						TransportRequest.class, new Object[] { id }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public TransportRequest selectTransportByAssignment(String id)
			throws DaoException {

		TransportRequest tr = null;
		Collection ctr = new ArrayList();

		String sql = "select a.id as id, r.id as requestId, r.requestTitle,r.requestBy,r.startDate as startDate,r.endDate,r.status,a.vehicle_num,su.department "
				+ "from fms_tran_request r, "
				+ "fms_tran_request_assignment a, "
				+ "security_user su "
				+ "WHERE r.id=a.requestId AND "
				+ "su.id=r.requestBy AND "
				+ "a.id=? ";

		ctr = super.select(sql, TransportRequest.class, new Object[] { id }, 0,
				1);
		// }
		if (ctr.size() > 0) {
			for (Iterator it = ctr.iterator(); it.hasNext();) {
				tr = (TransportRequest) it.next();
			}
		}

		return tr;
	}

	public AssignmentObject selectAssignmentObject(String id, String vehicleNo)
			throws DaoException {

		AssignmentObject AO = null;
		Collection cao = new ArrayList();

		String sql = "SELECT d.* FROM fms_tran_request_assignment_details d WHERE d.assgId=? AND d.vehicle_num = ? ";

		cao = super.select(sql, AssignmentObject.class, new Object[] { id,
				vehicleNo }, 0, 1);
		if (cao.size() > 0) {
			for (Iterator it = cao.iterator(); it.hasNext();) {
				AO = (AssignmentObject) it.next();
			}

		}

		return AO;
	}

	public Collection selectVehicleRequestId(String requestId, String type,
			String assgId) throws DaoException {
		// to find existing assignment

		/*
		 * String sql = "SELECT * FROM fms_tran_request_vehicle veh	"+
		 * "INNER JOIN fms_tran_category cat ON veh.category_id=cat.setup_id	"+
		 * "WHERE veh.requestId=? AND cat.name=?";
		 */

		String sql = "SELECT v.*,ca.name from fms_tran_vehicle v	"
				+ "INNER JOIN fms_tran_category ca ON  v.category_id = ca.setup_id	"
				+ "INNER JOIN fms_tran_request_assignment a ON a.vehicle_num=v.vehicle_num	"
				+ "INNER JOIN fms_tran_assignment assg ON assg.id=a.id	"
				+ "WHERE a.requestId=? AND ca.name=? AND assg.id=?";

		/*
		 * String sql =
		 * "SELECT DISTINCT r.vehicle_num, a.id, a.requestId, a.category_id, a.quantity, a.driver, b.name as name,assg.id FROM fms_tran_request_vehicle a	"
		 * + "INNER JOIN fms_tran_category b on	a.category_id=b.setup_id	" +
		 * "INNER JOIN fms_tran_assignment assg ON assg.requestId=a.requestId	"
		 * + "INNER JOIN fms_tran_request_assignment r ON r.id=assg.id	" +
		 * "WHERE 1=1 AND (assg.id=?) AND (b.name = ?)";
		 */
		return super.select(sql, AssignmentObject.class, new String[] {
				requestId, type, assgId }, 0, -1);

	}

	public int selectVehicleQuantity(String requestId, String type)
			throws DaoException {

		String sql = "SELECT quantity FROM fms_tran_request_vehicle veh	"
				+ "INNER JOIN fms_tran_category cat ON veh.category_id=cat.setup_id	"
				+ "WHERE veh.requestId=? AND cat.name=?";

		Map row = (Map) super.select(sql, HashMap.class,
				new Object[] { requestId, type }, 0, -1).iterator().next();
		return Integer.parseInt(row.get("quantity").toString());
	}

	public int selectCountTotalAssigned(String requestId) throws DaoException {
		Collection args = new ArrayList();

		String sql = "SELECT count(DISTINCT a.vehicle_num) as total FROM fms_tran_request_assignment a	"
				+ "INNER JOIN fms_tran_vehicle v ON a.vehicle_num=v.vehicle_num	"
				+ "INNER JOIN fms_tran_category c ON c.setup_id=v.category_id		"
				+ "where 1=1  ";

		if (!"".equals(requestId) && !"null".equals(requestId)
				&& requestId != null) {
			sql = sql + " AND requestId = ?";
			args.add(requestId);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public TransportRequest getTransportRequest(String requestId)
			throws DaoException {
		Collection treq = new ArrayList();

		TransportRequest transportRequest = null;
		;

		String sql = "SELECT id, requestTitle, requestType, program, startDate, endDate, destination, purpose, remarks,"
				+ "status, reason, requestBy, requestDate, updatedBy, updatedDate, approvedBy, approvedDate, statusRequest,blockBooking  FROM fms_tran_request WHERE id=?";

		treq = super.select(sql, TransportRequest.class,
				new String[] { requestId }, 0, -1);

		if (treq.size() > 0) {
			for (Iterator it = treq.iterator(); it.hasNext();) {
				transportRequest = (TransportRequest) it.next();
			}
		}

		return transportRequest;
	}

	// My Assignment Listing
	public Collection selectMyAssignment(String filter, String statusFil, String userId, Date endDate, 
			String sort, boolean desc, int startIndex, int maxRows) throws DaoException {
		
		Application app = Application.getInstance();
		String department = null;
		ArrayList args = new ArrayList();
		
		FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
		try{
			department = FRM.getUserDepartment(userId);
		}catch(Exception e){
			Log.getLog(getClass()).error(e);
		}
		String transportdept = app.getProperty("ManagementService");

		String sql = "SELECT DISTINCT a.id, d.manpowerId, a.requestId, a.startDate, a.endDate,r.requestTitle,d.status "
			+ "FROM fms_tran_request_driver d "
			+ "INNER JOIN fms_tran_assignment a ON a.id=D.assignmentId "
			+ "INNER JOIN fms_tran_request r ON D.requestId = r.id "
			+ "WHERE 1=1 ";
		
		if (filter!=null && !filter.equals("")){
			sql += "AND (r.requestTitle LIKE ? OR a.id LIKE ?) ";
			args.add("%"+filter+"%");
			args.add("%"+filter+"%");
		}
		if (statusFil!=null && statusFil.trim().length() > 0){
			if(transportdept.equals(department) && statusFil.equals("W")){
				sql += "AND d.status IS NULL ";
			}else {
				sql += "AND d.status = ? ";
				args.add(statusFil);
			}
		}
		
		if (userId!=null && !userId.equals("")){
			sql += "AND d.manpowerId = ? ";
			args.add(userId);
		}
		
		if (endDate!=null){
			sql += "AND a.endDate <= ? ";
			args.add(endDate);
		}
		
		if (sort == null){
			sort = "r.requestTitle ";
		}
		
		sql += "ORDER BY " + sort + (desc ? " DESC" : "");

		return super.select(sql, ManpowerAssignmentObject.class, args.toArray(), 0, -1);
	}

	// Count request for vehicle by requestId
	public int selectCountRequestedVehicle(String requestId)
			throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM fms_tran_request_vehicle WHERE 1=1";

		if (!"".equals(requestId) && !"null".equals(requestId)
				&& requestId != null) {
			sql = sql + " AND requestId = ?";
			args.add(requestId);
		}
		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public int selectCountMyAssignment(String search, String status,
			String userId) throws DaoException {
		int countMyAssignment = 0;

		String strSort = "";
		String catFilterClause = "";
		String filterUser = "";

		String sql = "SELECT id, requestTitle, requestType, program, startDate, endDate, destination, purpose, remarks,"
				+ "status, reason, requestBy, requestDate, updatedBy, updatedDate, approvedBy, approvedDate, statusRequest FROM fms_tran_request "
				+ filterUser + catFilterClause + strSort;

		return countMyAssignment;
	}

	public void insertStatus(Status s) throws DaoException {
		super
				.update(
						"INSERT INTO fms_tran_request_status (id, status, reason, createdBy, createdDate) "
								+ "VALUES (#id#,#status#,#reason#,#createdBy#,#createdDate#)",
						s);
	}

	public void updateStatusTrail(Status s) throws DaoException {
		super
				.update(
						"UPDATE fms_tran_request_status SET reason = #reason#, createdBy = #createdBy#, createdDate=#createdDate# "
								+ "WHERE id=#id# AND status=#status# ", s);
	}

	public int countStatusTrail(String id, String status) throws DaoException {
		Collection args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM fms_tran_request_status WHERE 1=1";

		if (!"".equals(id) && !"null".equals(id) && id != null) {
			sql = sql + " AND id = ?";
			args.add(id);
		}

		if (!"".equals(status) && !"null".equals(status) && status != null) {
			sql = sql + " AND status = ?";
			args.add(status);
		}

		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());
	}

	public Collection selectStatusTrail(String requestId) throws DaoException {
		String sql = "SELECT t.id AS id, t.status AS status, t.reason AS reason, t.createdBy AS createdBy, "
				+ "t.createdDate AS createdDate "
				+ "FROM fms_tran_request_status t "
				+ "WHERE t.id=? ORDER BY t.createdDate desc";
		return super.select(sql, Status.class, new String[] { requestId }, 0,
				-1);
	}

	public Collection selectStatusTrailByStatus(String requestId, String status)
			throws DaoException {
		String sql = "SELECT t.id AS id, t.status AS status, t.reason AS reason, t.createdBy AS createdBy, "
				+ "t.createdDate AS createdDate "
				+ "FROM fms_tran_request_status t "
				+ "WHERE t.id=? AND t.status=?";
		return super.select(sql, Status.class,
				new String[] { requestId, status }, 0, -1);
	}

	public Collection selectAssignmentDetailDriver(String requestId)
			throws DaoException {

		String sql = "SELECT DISTINCT D.* FROM fms_tran_request_driver_details D "
				+ "WHERE 1=1 AND D.requestId = ?";

		return super.select(sql, ManpowerAssignmentObject.class,
				new String[] { requestId }, 0, -1);

	}
	
	public String getDriverAssignmentIdByRequestId(String requestId, String userId)throws DaoException{
		String sql = 
			"SELECT assignmentId " +
			"FROM fms_tran_request_driver " +
			"WHERE requestId = ? AND manpowerId = ? ";
		
		Collection col = super.select(sql,HashMap.class,new String[]{requestId, userId},0,-1);
		if(col.size()>0){
			Map map = (Map) col.iterator().next();
			return (String) map.get("assignmentId");		
		} else {
			return "-";
		}
	}

	public TransportRequest selectDriverAssignment(String requestId,
			String userId) throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"SELECT r.id, r.requestTitle,  "
								+ "r.remarks,d.manpowerId,a.vehicle_num,r.startDate,r.endDate,d.reason,  "
								+ "(select programName from fms_prog_setup where programId=r.program) as program  "
								+ "FROM fms_tran_request r  "
								+ "INNER JOIN fms_tran_request_driver d ON d.requestId=r.id  "
								+ "INNER JOIN fms_tran_request_assignment a ON a.requestId=r.id  "
								+ "WHERE 1=1 AND d.manpowerId = ? AND r.id = ? ",
						TransportRequest.class, new Object[] { userId,
								requestId }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public TransportRequest selectDriverAssignmentByAssgIdUserId(String assgId,
			String userId) throws DaoException {

		TransportRequest TR = null;

		String sql = "SELECT DISTINCT a1.id as id, r.id as requestId, r.requestTitle,  "
				+ "r.remarks,d.manpowerId,a1.startDate,a1.endDate,d.reason,su.department,d.status, "
				+ "(select programName from fms_prog_setup where programId=r.program) as program		"
				+ "FROM fms_tran_assignment a1	"
				+ "INNER JOIN fms_tran_request r  ON r.id=a1.requestId	"
				+ "INNER JOIN fms_tran_request_driver d ON d.requestId=r.id		"
				+ "INNER JOIN security_user su ON su.id=r.requestBy		"
				+ "WHERE 1=1 AND a1.id=? AND d.manpowerId=? AND a1.id=d.assignmentId ";

		Collection result = super.select(sql, TransportRequest.class,
				new Object[] { assgId, userId }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public void updateCompleteAssignment(
			ManpowerAssignmentObject manpowerAssignmentObject)
			throws DaoException {

		super
				.update(
						"UPDATE fms_tran_request_driver SET completionDate=?, reason=?, status=? WHERE assignmentId=? AND manpowerId=?",
						new Object[] {
								manpowerAssignmentObject.getCompletionDate(),
								manpowerAssignmentObject.getReason(),
								manpowerAssignmentObject.getStatus(),
								manpowerAssignmentObject.getReferenceId(),
								manpowerAssignmentObject.getManpowerId() });
	}

	public void addFileAttachment(ManpowerAssignmentObject mAObj)
			throws DaoException {

		super
				.update(
						"INSERT INTO fms_attachment(id, fileName, path, createdBy, createdOn, referenceId) VALUES (?,?,?,?,?,?)",
						new Object[] { mAObj.getId(), mAObj.getDocumentsName(),
								mAObj.getDocuments(), mAObj.getManpowerId(),
								mAObj.getCreatedDate(), mAObj.getReferenceId() });
	}

	public void updateDrivers(String userId, String id, String newUserId)
			throws DaoException {

		super
				.update(
						"UPDATE fms_tran_request_driver SET manpowerId=? WHERE requestId=? AND manpowerId =? ",
						new Object[] { newUserId, id, userId });
	}

	public void updateUnfulfilledAssignment(
			ManpowerAssignmentObject manpowerAssignmentObject)
			throws DaoException {

		super
				.update(
						"UPDATE fms_tran_request_driver SET reason=?, status=? WHERE assignmentId=? AND manpowerId=?",
						new Object[] { manpowerAssignmentObject.getReason(),
								manpowerAssignmentObject.getStatus(),
								manpowerAssignmentObject.getId(),
								manpowerAssignmentObject.getManpowerId() });
	}

	public void alterTableType(String table_name, String column_name,
			String new_type) throws DaoException {

		String constraint_name = getConstraintObject(table_name, column_name);
		if (!(null == constraint_name)) {

			// Drop Contraint Object
			try {
				super.update("ALTER TABLE " + table_name + " DROP CONSTRAINT "
						+ constraint_name + "  ", null);

				// Alter table
				try {
					super.update("ALTER TABLE " + table_name + " ALTER COLUMN "
							+ column_name + "  " + new_type + " ", null);
				} catch (DaoException e) {
					Log.getLog(getClass()).error(
							"Error on ALTER COLUMN >> :" + e);
				}

			} catch (DaoException e) {
				Log.getLog(getClass()).error(
						"Error on DROP CONSTRAINT >> :" + e);
			}

		}

	}

	protected String getConstraintObject(String table_name, String column_name)
			throws DaoException {

		String contraint_name = null;
		String sql = "SELECT object_name(const.constid) AS contraint_name  "
				+ "FROM sys.sysconstraints const  "
				+ "JOIN sys.columns cols on cols.object_id = const.id  "
				+ "and cols.column_id = const.colid  "
				+ "WHERE object_name(const.id) = ?  " + "AND cols.name = ?  ";

		Collection col = super.select(sql, HashMap.class, new Object[] {
				table_name, column_name }, 0, 1);
		if (col != null) {
			if (col.size() > 0) {
				HashMap map = (HashMap) col.iterator().next();
				contraint_name = map.get("contraint_name").toString();
			}
		}

		return contraint_name;
	}

	public Collection selectUnitUsers(String search, String unitId,
			String unitHeadId, String groupId, String sort, boolean desc,
			int start, int rows) throws DaoException {
		String sql = "Select distinct su.id as id,su.username,firstName,lastName,email1,active,department,u.id as unitId,u.name as unitName"
				+ " from fms_unit u Inner join security_user su on u.id=su.unit "
				+ "INNER JOIN security_user_group g ON (su.id = g.userId) "
				+ "WHERE su.active='1' and u.status = '1' ";

		if (search != null && !"".equals(search)) {
			sql += " AND ( su.username like '%" + search
					+ "%' OR su.firstName like '%" + search
					+ "%' OR su.lastName like '%" + search + "%' ) ";
		}

		if (unitId != null && !"".equals(unitId)) {
			sql += " AND ( u.id= '" + unitId + "' ) ";
		}
		if (groupId != null && !"".equals(groupId)) {
			if (!groupId.equals("-1")) {
				sql += " AND (g.groupId = '" + groupId + "') ";
			}
		}
		if (sort != null && !"".equals(sort)) {
			sql += " order by " + sort + " ";
		}
		if (desc) {
			sql += " DESC ";
		}

		return super.select(sql, User.class, null, start, rows);
	}

	public void insertTranAssigment(TransportRequest tr) throws DaoException {

		super
				.update(
						"INSERT INTO fms_tran_assignment(id, requestId, startDate, endDate, status) VALUES (#id#, #requestId#, #startDate#, #endDate#, #status#)",
						tr);

	}

	public Collection selectAssignmentListing(String filter, String sort,
			String userId, String requestId, boolean desc, int startIndex,
			int maxRows) throws DaoException {

		String strSort = "";
		String filterUser = "";

		if (sort != null) {
			strSort += " ORDER BY " + sort;
			if (desc)
				strSort += " DESC";
		} else
			strSort += " ORDER BY startDate ASC";

		String filterClause = " WHERE (id LIKE '%"
				+ (filter == null ? "" : filter) + "%'  " + "OR status LIKE '%"
				+ (filter == null ? "" : filter) + "%')  AND (requestId = '"
				+ requestId + "') ";

		/*
		 * if(userId != null && userId.trim().length() > 0) //filterUser =
		 * "  AND requestBy=?"; filterUser = "  AND r.requestBy='"+userId+"' ";
		 */

		String sql = "SELECT DISTINCT id,requestId,startDate,endDate,status FROM fms_tran_assignment  "
				+ filterClause + strSort;

		return super.select(sql, TransportRequest.class, null, startIndex,
				maxRows);

	}

	public Collection selectAssignmentByRequestId(String requestId)
			throws DaoException {

		String sql = "SELECT DISTINCT id,requestId,startDate,endDate,status FROM fms_tran_assignment "
				+ "WHERE 1=1 AND requestId = ?";

		return super.select(sql, TransportRequest.class,
				new String[] { requestId }, 0, -1);

	}

	public Collection selectAssignmentByAssignmentId(String assId)
			throws DaoException {

		String sql = "SELECT DISTINCT id,requestId,startDate,endDate,status FROM fms_tran_assignment "
				+ "WHERE 1=1 AND id = ?";

		return super.select(sql, TransportRequest.class,
				new String[] { assId }, 0, -1);

	}

	public Collection selectAssignmentVehicles(String assId)
			throws DaoException {

		String sql = "SELECT DISTINCT ra.vehicle_num,ra.* from fms_tran_assignment a INNER JOIN fms_tran_request_assignment ra ON ra.id=a.id "
				+ "WHERE 1=1 AND a.id = ?";

		return super.select(sql, TransportRequest.class,
				new String[] { assId }, 0, -1);

	}

	public Collection selectDriverByAssignmentId(String assId)
			throws DaoException {
		String sql = "SELECT DISTINCT rd.manpowerId, rd.*  FROM fms_tran_assignment a	"
				+ "INNER JOIN fms_tran_request_driver rd ON rd.assignmentId=a.id		"
				+ "WHERE 1=1 AND rd.assignmentId = ?";

		return super.select(sql, ManpowerLeaveObject.class,
				new String[] { assId }, 0, -1);

	}

	public Collection selectDetailsVehicleByAssignmentId(String assId)
			throws DaoException {
		String sql = "SELECT DISTINCT v.category_id, v.* FROM fms_tran_assignment a	"
				+ "INNER JOIN fms_tran_request_vehicle v ON v.requestId=a.requestId		"
				+ "WHERE 1=1 AND a.id = ?";

		return super.select(sql, VehicleRequest.class, new String[] { assId },
				0, -1);

	}

	public Collection selectVehicleAssigmentByVehicleNum(String vehicle_num,
			Date start, Date end) throws DaoException {
		// //
		String filterDate = "";
		Object arg[] = new Object[] { vehicle_num, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(a.startDate >= ? AND a.endDate <= ?) ";

		}
		// //

		String sql = "SELECT DISTINCT ra.vehicle_num, a.id, a.requestId, a.startDate, a.endDate,r.requestTitle FROM fms_tran_request_assignment ra		"
				+ "INNER JOIN fms_tran_assignment a ON a.id=ra.id	 "
				+ "INNER JOIN fms_tran_request r ON a.requestId=r.id	 "
				+ "WHERE 1=1 AND ra.vehicle_num = ?" + filterDate;
		return super.select(sql, VehicleObject.class, arg, 0, -1);

	}

	public Collection selectDriversAssignmentByUserId(String userId,
			Date start, Date end) throws DaoException {
		String filterDate = "";
		Object arg[] = new Object[] { userId, start, end };
		if (start != null && end != null) {
			filterDate += " AND " +

			"(a.startDate >= ? AND a.endDate <= ?) ";

		}
		String sql = "SELECT DISTINCT D.manpowerId, a.id, a.requestId, a.startDate, a.endDate,r.requestTitle 	"
				+
				// String sql =
				// "SELECT DISTINCT D.manpowerId, a.id, R.requestTitle,	a.startDate, a.endDate  "
				// +
				"FROM fms_tran_request_driver D    "
				+ "INNER JOIN fms_tran_assignment a ON a.id=D.assignmentId      "
				+ "INNER JOIN fms_tran_request R ON D.requestId = R.id	"
				+ "WHERE 1=1 AND D.manpowerId = ?" + filterDate;
		return super.select(sql, ManpowerAssignmentObject.class, arg, 0, -1);

	}

	public Collection selectVehicleDriverQuantityByType(String reqId,
			String type, String assgId) throws DaoException {
		if (type == null) {
			// for drivers
			Object arg[] = new Object[] { reqId, assgId };
			String sql = "SELECT a.id, a.requestId, a.category_id, a.quantity, a.driver, b.name as name,assg.id FROM fms_tran_request_vehicle a	"
					+ "INNER JOIN fms_tran_category b on a.category_id=b.setup_id	"
					+ "INNER JOIN fms_tran_assignment assg ON assg.requestId= a.requestId	"
					+ "WHERE 1=1 AND (a.requestId = ?) AND (assg.id=?)";
			return super.select(sql, VehicleRequest.class, arg, 0, -1);

		} else {
			// for vehicle
			Object arg[] = new Object[] { reqId, type, assgId };
			String sql = "SELECT a.id, a.requestId, a.category_id, a.quantity, a.driver, b.name as name,assg.id FROM fms_tran_request_vehicle a	"
					+ "INNER JOIN fms_tran_category b on a.category_id=b.setup_id	"
					+ "INNER JOIN fms_tran_assignment assg ON assg.requestId= a.requestId	"
					+ "WHERE 1=1 AND (a.requestId = ?) AND (b.name = ?) AND (assg.id=?)";
			return super.select(sql, VehicleRequest.class, arg, 0, -1);
		}
	}

	public Map getQuantityVehiclesDrivers(String requestId) throws DaoException {

		Map map = new HashMap();

		String sql = "SELECT sum(quantity) as qtyVeh, sum(driver) as qtyDri FROM fms_tran_assignment a	"
				+ "INNER JOIN fms_tran_request_vehicle v ON a.requestId=v.requestId	"
				+ "WHERE a.requestId=?	";

		Collection coll = super.select(sql, HashMap.class,
				new Object[] { requestId }, 0, -1);

		for (Iterator it = coll.iterator(); it.hasNext();) {
			map = (HashMap) it.next();
		}

		return map;
	}

	public int selectCountVehicleByRequest(String requestId)
			throws DaoException {

		String sql = "SELECT count(vehicle_num) as totalVeh from fms_tran_request_assignment where requestId=? ";

		Map row = (Map) super.select(sql, HashMap.class,
				new Object[] { requestId }, 0, -1).iterator().next();
		return Integer.parseInt(row.get("totalVeh").toString());

	}

	public int selectCountDriverByRequest(String requestId) throws DaoException {

		String sql = "SELECT count(manpowerId) as totalMan from fms_tran_request_driver where requestId=? ";

		Map row = (Map) super.select(sql, HashMap.class,
				new Object[] { requestId }, 0, -1).iterator().next();
		return Integer.parseInt(row.get("totalMan").toString());

	}

	public TransportRequest selectTransportByFlagId(String flagId)
			throws DaoException {

		TransportRequest tr = null;
		Collection ctr = new ArrayList();

		String sql = "SELECT DISTINCT a.id, a.requestId, r.requestTitle,r.requestBy,a.startDate,	"
				+ "a.endDate ,r.status,ra.vehicle_num,su.department, r.destination, r.rate, r.rateVehicle 	"
				+ "FROM fms_tran_assignment a		"
				+ "INNER JOIN fms_tran_request_assignment ra ON ra.id = a.id	"
				+ "INNER JOIN fms_tran_request r ON a.requestId=r.id		"
				+ "INNER JOIN security_user su ON su.id=r.requestBy		"
				+ "WHERE 1=1 AND ra.flagId=?	 ";

		ctr = super.select(sql, TransportRequest.class,
				new Object[] { flagId }, 0, 1);

		if (ctr.size() > 0) {
			for (Iterator it = ctr.iterator(); it.hasNext();) {
				tr = (TransportRequest) it.next();
			}
		}

		return tr;
	}

	public TransportRequest selectDriverAssignmentsForAdmin(String flagId)
			throws DaoException {

		TransportRequest TR = null;

		Collection result = super
				.select(
						"SELECT DISTINCT d.flagId AS flagId, d.assignmentId AS id, r.id as requestId, r.requestTitle,   "
								+ "r.remarks,d.manpowerId,a.startDate,a.endDate,d.reason,su.department,d.status,d.flagId, "
								+ "(select programName from fms_prog_setup where programId=r.program) as program		"
								+ "FROM fms_tran_request_driver d	"
								+ "INNER JOIN fms_tran_assignment a ON a.id=d.assignmentId	"
								+ "INNER JOIN fms_tran_request r  ON r.id=a.requestId			"
								+ "INNER JOIN security_user su ON su.id=r.requestBy		"
								+ "WHERE 1=1 AND d.flagId=?  ",
						TransportRequest.class, new Object[] { flagId }, 0, 1);

		if (result.size() > 0) {
			TR = (TransportRequest) result.iterator().next();
		}

		return TR;
	}

	public Collection selectAllTransportRequest(Date from, Date to,
			String status, String filter, String statusFil, String department,
			String sort, String userId, boolean desc, int startIndex,
			int maxRows) throws DaoException {

		ArrayList args = new ArrayList();
		String sql = "SELECT DISTINCT r.id, r.* FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id "
				+ "INNER JOIN fms_department d ON (su.department = d.id) "
				+ "WHERE 1=1 "
				+ "AND NOT(r.status like '%C%' OR r.status like '%D%' OR r.status like '%P%') ";

		if (filter != null && !filter.equals("")) {
			sql += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0)
			sql += "  AND r.requestBy='" + userId + "' ";

		if (status != null && !status.equals("") && !status.equals("-1")) {
			sql += " AND r.status='" + status + "' ";
		}/*
		 * else{ sql += " AND r.status='S' "; }
		 */

		if (!("".equals(department) || null == department)
				&& !"-1".equals(department)) {
			sql += "AND d.id='" + department + "'  ";
		}

		if (from != null && to != null) {
			// sql +=
			// " AND ((r.startDate >= ? AND r.startDate <= ?) OR (r.startDate <= ? AND r.endDate >= ?)) ";
			sql += " AND (("
					+ "CAST((STR( YEAR( r.startDate ) ) + '/' + STR( MONTH( r.startDate ) ) + '/' +STR( DAY( r.startDate ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?) OR "
					+ "(CAST((STR( YEAR( r.endDate ) ) + '/' + STR( MONTH( r.endDate  ) ) + '/' +STR( DAY( r.endDate  ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?)) ";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			args.add(sdf.format(from));
			args.add(sdf.format(to));
			args.add(sdf.format(from));
			args.add(sdf.format(to));
		}

		if (sort != null) {
			if ("id".equals(sort)) {
				sort = "r.id";
			} else if ("requiredDateFrom".equals(sort)) {
				sort = "r.startDate";
			} else if ("status".equals(sort)) {
				sort = "r.status";
			}

			sql += " ORDER BY " + sort;
			if (desc)
				sql += " DESC";
		} else {
			sql += " ORDER BY r.id DESC, r.status DESC";
		}

		return super.select(sql, TransportRequest.class, args.toArray(),
				startIndex, maxRows);
	}

	public int selectAllCountTransportRequest(Date from, Date to,
			String status, String filter, String statusFil, String department,
			String userId) throws DaoException {

		ArrayList args = new ArrayList();
		String sql = "SELECT count(*) AS total FROM fms_tran_request r  "
				+ "INNER JOIN security_user su  ON r.requestBy = su.id  "
				+ "INNER JOIN fms_department d ON (su.department = d.id) "
				+ "WHERE 1=1 "
				+ "AND NOT(r.status like '%C%' OR r.status like '%D%' OR r.status like '%P%') ";

		if (filter != null && !filter.equals("")) {
			sql += "AND (r.requestTitle LIKE ? OR r.id LIKE ? OR r.engineeringRequestId = ?) ";
			args.add("%" + filter + "%");
			args.add("%" + filter + "%");
			args.add(filter);
		}

		if (userId != null && userId.trim().length() > 0)
			sql += "  AND r.requestBy='" + userId + "' ";

		if (status != null && !status.equals("") && !status.equals("-1")) {
			sql += " AND r.status='" + status + "' ";
		}

		if (!("".equals(department) || null == department)
				&& !"-1".equals(department)) {
			sql += "AND d.id='" + department + "'  ";
		}

		if (from != null && to != null) {
			// sql +=
			// " AND ((r.startDate >= ? AND r.startDate <= ?) OR (r.startDate <= ? AND r.endDate >= ?))";
			// args.add(from);
			// args.add(to);
			// args.add(from);
			// args.add(from);
			sql += " AND (("
					+ "CAST((STR( YEAR( r.startDate ) ) + '/' + STR( MONTH( r.startDate ) ) + '/' +STR( DAY( r.startDate ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?) OR "
					+ "(CAST((STR( YEAR( r.endDate ) ) + '/' + STR( MONTH( r.endDate  ) ) + '/' +STR( DAY( r.endDate  ) )) AS DATETIME) "
					+ "BETWEEN ? AND ?)) ";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			args.add(sdf.format(from));
			args.add(sdf.format(to));
			args.add(sdf.format(from));
			args.add(sdf.format(to));
		}

		Map row = (Map) super.select(sql, HashMap.class, args.toArray(), 0, -1)
				.iterator().next();
		return Integer.parseInt(row.get("total").toString());

	}
	
	public void updateCloseReqStatus(String id, String status)throws DaoException {
		super.update(
				"UPDATE fms_tran_request SET status=? WHERE id=?",new Object[] {status,id});
	}
	
	public VehicleRequest getVehicleRequestByRequestId(String requestId)throws DaoException {
		
		VehicleRequest vehRequest = null;
		String sql = "SELECT id, name, requestId, category_id, quantity, driver FROM fms_tran_request req	" +
					 "INNER JOIN fms_tran_request_vehicle veh on veh.requestId=req.id WHERE req.id = ? ";
		
		Collection collVehicle = super.select(sql, VehicleRequest.class, new String[]{requestId}, 0, -1);
		for(Iterator it = collVehicle.iterator(); it.hasNext(); ){
			vehRequest = (VehicleRequest) it.next();			
		}
		
		return vehRequest;
	}
	
	public void updateVehicleRate(String id, String rate)throws DaoException {
		
		super.update("UPDATE fms_tran_request SET rateVehicle=? WHERE id=?",new Object[] {rate,id});
	}
	
	public VehicleObject selectVehicleByNo(String vehicle_num) throws DaoException {
		
		VehicleObject vehObj = new VehicleObject();
			
		String sql = "SELECT v.vehicle_num, m.name as maketype_name, c.name as channel_name,  "
				+ "ca.name as category_name, f.name as charge_name, f.type    "
				+ "FROM fms_tran_vehicle v "
				+ "INNER JOIN fms_tran_maketype m on v.maketype_id = m.setup_id "
				+ "INNER JOIN fms_tran_category ca on v.category_id=ca.setup_id "
				+ "INNER JOIN fms_tran_channel c on v.channel_id=c.setup_id "
				+ "INNER JOIN fms_tran_ratecard f on f.name=ca.name "
				+ "WHERE 1=1 AND  v.vehicle_num = ?";
		
		
		Collection col = super.select(sql, VehicleObject.class,	new String[] { vehicle_num }, 0, -1);
		
		if(col.size() > 0){
			for(Iterator it = col.iterator(); it.hasNext(); ){
				vehObj = (VehicleObject) it.next();			
			}
		}
		return vehObj;
	}
	
	public void deleteOBRequest(String requestId) throws DaoException {
		
		String sql = "DELETE FROM fms_tran_request WHERE id = ?";
		super.update(sql, new String[] { requestId });
	}
	
	public void deleteOBRequestVehicle(String requestId) throws DaoException {
		
		String sql = "DELETE FROM fms_tran_request_vehicle WHERE requestId = ?";
		super.update(sql, new String[] { requestId });
	}

	public void deleteOBRequestStatus(String requestId) throws DaoException {
	
		String sql = "DELETE FROM fms_tran_request_status WHERE id = ?";
		super.update(sql, new String[] { requestId });
	}
	
	public Collection selectVehiclesByAssignmentId(String id) throws DaoException {
		
		String sql = "SELECT DISTINCT v.vehicle_num, c.name as category_name FROM fms_tran_request_assignment a "+
		"INNER JOIN fms_tran_request_driver d on a.id=d.assignmentId "+
		"INNER JOIN fms_tran_vehicle v on v.vehicle_num=a.vehicle_num "+
		"INNER JOIN fms_tran_category c on v.category_id=c.setup_id "+
		"WHERE 1=1 and id = ?"; 
			
		return super.select(sql, VehicleObject.class, new String[]{id}, 0, -1);

	}
	
	public void insertDriverVehicle(DriverVehicleObject dvObject) throws DaoException {
		
		String sql = "INSERT INTO fms_tran_request_driver_vehicle (id, assignmentId, vehicle_num, driver) VALUES (#id#, #assignmentId#, #vehicle_num#, #driver#)";
		super.update(sql, dvObject);
	}
	
	public DriverVehicleObject selectDriverVehicle(String userId, String assignmentId)throws DaoException {
		
		
		String sql = "SELECT id, assignmentId, vehicle_num, driver FROM fms_tran_request_driver_vehicle " +
				"WHERE assignmentId=? AND driver=? ";
		
		DriverVehicleObject dvObject = null;
		Collection driversVehicleColl = super.select(sql, DriverVehicleObject.class, new String[]{assignmentId, userId}, 0, -1);
		for(Iterator it = driversVehicleColl.iterator(); it.hasNext(); ){
			dvObject = (DriverVehicleObject) it.next();
		}
		
		return dvObject;
	}
	
	public void deleteVehicleDriver(String assignmentId, String userId)	throws DaoException {
		
		super.update("DELETE FROM fms_tran_request_driver_vehicle WHERE assignmentId=? AND driver=?",new String[]{assignmentId, userId});
	}
	
	public Collection selectAssignments(String assgId, String status)	throws DaoException {
		
		
		String sql = "SELECT * FROM fms_tran_request_assignment_details "
				+ "WHERE 1=1 AND assgId = ? AND status = ?  ";
		
		return super.select(sql, AssignmentObject.class, new String[] { assgId, status }, 0, -1);

}
	
	public Collection<AbwTransferCostObject> getInfoToCreateAbwTransferCostObjects() throws DaoException{
		String query = "SELECT r.id AS requestId, r.startDate AS requiredDateFrom, " +
				"r.endDate AS requiredDateTo, s.pfeCode AS projectCode, " +
				"r.rateVehicle AS cost, 'C' AS type, rv.quantity, tc.setup_id as categoryId, rc.abw_code AS facilityCode, su.id AS createdBy, " +
				"GETDATE() AS createdDate " +
				"FROM security_user su, fms_tran_request r " +
				"LEFT JOIN fms_prog_setup s ON (s.programId = r.program) " +
				"LEFT JOIN fms_tran_request_vehicle rv ON (rv.requestId = r.id) " +
				"LEFT JOIN fms_tran_category tc ON (tc.setup_id = rv.category_id) " +
				"LEFT JOIN fms_tran_rateCard rc ON (rc.name = tc.name) " +
				"WHERE (r.status = 'E' OR r.status = 'G') " +
				"AND (r.endDate  between DATEADD(dd, datediff(dd, 0, getdate())-1, 0) and DATEADD(dd, datediff(dd, 0, getdate()), 0)) " +
				"AND su.username = 'admin' AND r.program <> '-' AND tc.name NOT LIKE 'OB%' " +
				"UNION ALL " +
				"SELECT r.id AS requestId, r.startDate AS requiredDateFrom, " +
				"r.endDate AS requiredDateTo, s.pfeCode AS projectCode, " +
				"r.rate AS cost, 'D' AS type, rv.driver AS quantity, tc.setup_id as categoryId, (Select abw_code from fms_tran_rateCard where setup_id='Driver') AS facilityCode, su.id AS createdBy, " +
				"GETDATE() AS createdDate " +
				"FROM security_user su, fms_tran_request r " +
				"LEFT JOIN fms_prog_setup s ON (s.programId = r.program) " +
				"LEFT JOIN fms_tran_request_vehicle rv ON (rv.requestId = r.id) " +
				"LEFT JOIN fms_tran_category tc ON (tc.setup_id = rv.category_id) " +
				"LEFT JOIN fms_tran_rateCard rc ON (rc.name = tc.name) " +
				"WHERE (r.status = 'E' OR r.status = 'G') " +
				"AND (r.endDate  between DATEADD(dd, datediff(dd, 0, getdate())-1, 0) and DATEADD(dd, datediff(dd, 0, getdate()), 0)) " +
				"AND su.username = 'admin' AND r.program <> '-' AND tc.name NOT LIKE 'OB%' " +
				"ORDER BY requestId";			 
		
		return (Collection<AbwTransferCostObject>) super.select(query, AbwTransferCostObject.class, null, 0, -1);
	}
	
	
	public Collection<AbwTransferCostObject> getInfoToCreateAbwTransferCostObjects(Date dateFrom, Date dateTo) throws DaoException{
		String query = "SELECT r.id AS requestId, r.startDate AS requiredDateFrom, " +
				"r.endDate AS requiredDateTo, s.pfeCode AS projectCode, " +
				"r.rateVehicle AS cost, 'C' AS type, rv.quantity, tc.setup_id as categoryId, rc.abw_code AS facilityCode, su.id AS createdBy, " +
				"GETDATE() AS createdDate " +
				"FROM security_user su, fms_tran_request r " +
				"LEFT JOIN fms_prog_setup s ON (s.programId = r.program) " +
				"LEFT JOIN fms_tran_request_vehicle rv ON (rv.requestId = r.id) " +
				"LEFT JOIN fms_tran_category tc ON (tc.setup_id = rv.category_id) " +
				"LEFT JOIN fms_tran_rateCard rc ON (rc.name = tc.name) " +
				"WHERE (r.status = 'E' OR r.status = 'G') AND r.endDate >= ? AND r.endDate <= ?  " +
				"AND su.username = 'admin' AND r.program <> '-' AND tc.name NOT LIKE 'OB%' " +
				"UNION ALL " +
				"SELECT r.id AS requestId, r.startDate AS requiredDateFrom, " +
				"r.endDate AS requiredDateTo, s.pfeCode AS projectCode, " +
				"r.rate AS cost, 'D' AS type, rv.driver AS quantity, tc.setup_id as categoryId, (Select abw_code from fms_tran_rateCard where setup_id='Driver') AS facilityCode, su.id AS createdBy, " +
				"GETDATE() AS createdDate " +
				"FROM security_user su, fms_tran_request r " +
				"LEFT JOIN fms_prog_setup s ON (s.programId = r.program) " +
				"LEFT JOIN fms_tran_request_vehicle rv ON (rv.requestId = r.id) " +
				"LEFT JOIN fms_tran_category tc ON (tc.setup_id = rv.category_id) " +
				"LEFT JOIN fms_tran_rateCard rc ON (rc.name = tc.name) " +
				"WHERE (r.status = 'E' OR r.status = 'G') AND r.endDate >= ? AND r.endDate <= ?  " +
				"AND su.username = 'admin' AND r.program <> '-' AND tc.name NOT LIKE 'OB%' " +
				"ORDER BY requestId";			 
			 
         	Date endDate = DateUtil.getDateWithEndTime(dateTo);
			ArrayList args = new ArrayList();
			args.add(dateFrom);
			args.add(endDate);
			args.add(dateFrom);
			args.add(endDate);
		
		return (Collection<AbwTransferCostObject>) super.select(query, AbwTransferCostObject.class, args.toArray(), 0, -1);
	}
	
	public int selectQuantityDriver(String requestId, String categoryId) throws DaoException {
		
		int quantity = 0;
		String sql = "SELECT driver as totalDriver FROM fms_tran_request_vehicle "+
					 "WHERE requestId = ? AND category_id = ? ";
		
		
		Map row = (Map) super.select(sql, HashMap.class, new String[]{requestId,categoryId}, 0, -1)
				.iterator().next();
		
		quantity = Integer.parseInt(row.get("totalDriver").toString());
		return quantity;
	}
	
	public int selectQuantityVehicle(String requestId, String categoryId) throws DaoException {
		
		int quantity = 0;
		String sql = "SELECT quantity as totalVehicle FROM fms_tran_request_vehicle "+
					 "WHERE requestId = ? AND category_id = ? ";
		
		
		Map row = (Map) super.select(sql, HashMap.class, new String[]{requestId,categoryId}, 0, -1)
				.iterator().next();
		
		quantity = Integer.parseInt(row.get("totalVehicle").toString());
		return quantity;
	}

	public String getCategoryName(String categoryId)throws DaoException {
		
		String name = null;
		String sql = "SELECT name  FROM fms_tran_category "+ 
					" WHERE setup_id = ?";
		Collection col = super.select(sql,HashMap.class,new String[]{categoryId},0,-1);
		if(col.size()>0){
			Map map = (Map) col.iterator().next();
			name = (String) map.get("name");		
		} 
		
		return name;
	}
	
	public Collection getTransportRequestListing(Date dateFrom, Date dateTo) throws DaoException{
		String sql  = "SELECT * from fms_tran_request WHERE requestDate BETWEEN ? AND ?  ";
		Collection col = super.select(sql,TransportRequest.class,new Date[]{dateFrom, dateTo},0,-1);
		return col;
	}
	
	public void updateBackDatedTransportRequest(TransportRequest tr) throws DaoException {

		String sql = "UPDATE fms_tran_request SET  updatedBy=#updatedBy#, "
			+ "updatedDate=#updatedDate#, "
			+ "rate=#rate#, rateVehicle=#rateVehicle#	 "
			+ "WHERE id=#id# ";
		
		super.update(sql, tr);
	}

}
