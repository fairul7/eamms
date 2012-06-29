package com.tms.elearning.testware.model;

import kacang.model.*;

public class AssessmentModuleDaoSybase extends AssessmentModuleDaoMsSql {
	public void init() throws DaoException {
		try {
		super.update(
			"CREATE TABLE cel_content_assessment_students_history (" +
			"  user_id varchar(255) default '0' NOT NULL," +
			"  question varchar(255) default '0' NOT NULL," +
			"  wrong_answer varchar(255) default '0' NOT NULL," +
			"  right_answer varchar(255) default '0' NOT NULL," +
			"  assessment_id varchar(255) default '0' NOT NULL," +
			"  last_date datetime default '0000-00-00 00:00:00' NOT NULL," +
			"  frequency integer default 0 NOT NULL" + ")", null);
		} catch (DaoException e) {
		}
	}
}
