package com.tms.elearning.testware.model;

import kacang.model.*;

public class QuestionModuleDaoSybase extends QuestionModuleDaoMsSql {
	public void init() throws DaoException {
		try {
			super.update("CREATE TABLE cel_content_assessment_statistic (" +
					"  assessment_id varchar(255) default '0' NOT NULL," +
					"  user_id varchar(255) default '0' NOT NULL," +
					"  dateTook datetime NULL," +
					"  total_questions integer default 0 NULL," +
					"  wrong_questions integer default 0 NULL" + ")", null);
		} catch (DaoException e) {
		}
	}
}
