package com.tms.collab.forum.model;

import kacang.model.*;

public class ForumDaoSybase extends ForumDaoMsSql {
	public void init() throws DaoException {
		/* Alter Tables */
		try {
			super.update("ALTER TABLE frm_message ADD email VARCHAR(250) NULL", null);
		}
		catch(Exception e) {}
		try {
			super.update("ALTER TABLE frm_thread ADD email VARCHAR(250) NULL", null);
		}
		catch(Exception e) {}
		
		/* Indexes */
		try {
			super.update("CREATE INDEX frm_forum_name_idx ON frm_forum(name)", null);
			super.update("CREATE INDEX frm_thread_forumId_idx ON frm_thread(forumId)", null);
			super.update("CREATE INDEX frm_thread_cDate_idx ON frm_thread(creationDate)", null);
			super.update("CREATE INDEX frm_thread_mDate_idx ON frm_thread(modificationDate)", null);
			super.update("CREATE INDEX frm_message_forumId_idx ON frm_message(forumId)", null);
			super.update("CREATE INDEX frm_message_threadId_idx ON frm_message(threadId)", null);
			super.update("CREATE INDEX frm_message_ownerId_idx ON frm_message(ownerId)", null);
			super.update("CREATE INDEX frm_message_cDate_idx ON frm_message(creationDate)", null);
			super.update("CREATE INDEX frm_message_mDate_idx ON frm_message(modificationDate)", null);
		}
		catch(Exception e) {}
	}
}
