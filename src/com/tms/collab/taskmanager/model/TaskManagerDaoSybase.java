package com.tms.collab.taskmanager.model;

import kacang.model.*;

public class TaskManagerDaoSybase extends TaskManagerDaoMsSql {
	public void init() throws DaoException {
		try {
			super.update("ALTER TABLE tm_task ADD estimationType VARCHAR(255) DEFAULT 'Mandays' NULL", null);
			super.update("ALTER TABLE tm_task ADD estimation decimal(11, 2) NULL", null);
			super.update("ALTER TABLE tm_task ADD comments TEXT NULL", null);
		} catch(Exception e) {
		}
		
		try {
			super.update("ALTER TABLE tm_assignee ADD completedSetBy VARCHAR(255) NULL", null);
			super.update("ALTER TABLE tm_assignee ADD completedDateSetOn DATETIME NULL", null);
			super.update("ALTER TABLE tm_assignee ADD updateBy VARCHAR(255) NULL", null);
			super.update("ALTER TABLE tm_assignee ADD updateDate DATETIME NULL", null);
		} catch(Exception e) {
		}
	}
}
