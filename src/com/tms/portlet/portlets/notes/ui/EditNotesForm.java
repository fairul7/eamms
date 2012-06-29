package com.tms.portlet.portlets.notes.ui;

import com.tms.portlet.portlets.notes.model.Notes;
import com.tms.portlet.portlets.notes.model.NotesModule;

import kacang.Application;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.Forward;


public class EditNotesForm extends AddNotesForm  {
	
	private String notesId;
	
	public EditNotesForm() {
	}	

	public String getNotesId() {
		return this.notesId;
	}

	public void setNotesId(String notesId) {
		this.notesId = notesId;
	}

	

	public void onRequest(Event evt) {
		loadNote();
	}

	protected void loadNote() {
		if (notesId == null)
			return;
			
		// load note object
		try {
			String userId = getWidgetManager().getUser().getId();
			Application application = Application.getInstance();		
			NotesModule handler = (NotesModule)application.getModule(NotesModule.class);
			Notes notes = handler.findNotes(getNotesId(), userId);			
						
			
			notesTextBox.setValue(notes.getContent());
		}
		catch(Exception e) {
			throw new RuntimeException(e.toString());
		}		
	}
	
	public Forward onValidate(Event evt) {
		//save the notes		
		updateNotes();
		return new Forward(
		"notes",
		"notes.jsp",
		true);			
	}
	 
	protected void updateNotes () throws RuntimeException {
		// get current user
		User user = getWidgetManager().getUser();
		String userId = user.getId();
		
		// create notes object
		Notes notes = new Notes();
		notes.setContent(notesTextBox.getValue().toString().trim());
		notes.setUserId(userId);
		notes.setNotesId(getNotesId());
		
		try {
			Application application = Application.getInstance();		
			NotesModule handler = (NotesModule)application.getModule(NotesModule.class);
			handler.updateNotes(notes);
		}
		catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
	}	
	
}
