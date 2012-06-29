package com.tms.cms.profile.model;

import kacang.model.*;

public class ContentProfileDaoSybase extends ContentProfileDaoMsSql {
	public void init() throws DaoException {
		try {
			super.init();
		} catch (DaoException e) {
		}
		
		/* Indexes */
		try {
			super.update("CREATE INDEX contentId ON cms_profile_data(contentId)", null);
			super.update("CREATE INDEX version ON cms_profile_data(version)", null);
			super.update("CREATE INDEX profileId ON cms_profile_map(profileId)", null);
			super.update("CREATE INDEX parentId ON cms_profile_map(parentId)", null);
		}
		catch(Exception e) {}
	}
}
