package com.tms.fms.transport.model;

import kacang.model.DefaultModule;

public class TransportFeedbackModule extends DefaultModule {
	public void addFeedback(TransportFeedbackDataObject trans) {	
		TransportFeedbackDao dao = (TransportFeedbackDao) getDao();
		dao.insertFeedback(trans);
	}
	public boolean searchId(String id) {
		TransportFeedbackDao dao = (TransportFeedbackDao) getDao();
		return dao.getRequestId(id);
	}
}
