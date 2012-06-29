package com.tms.elearning.lesson.model;

import kacang.model.*;

public class LessonDaoSybase extends LessonDaoMsSql {
	public void init() throws DaoException {
		try {
			super.update("ALTER TABLE cel_content_lesson ADD associatedoc text NULL", null);
		} catch (DaoException e) {
		}
	}
}
