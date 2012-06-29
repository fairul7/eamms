package com.tms.portlet.portlets.notes.ui;



import com.tms.portlet.portlets.notes.model.Notes;
import com.tms.portlet.portlets.notes.model.NotesModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.stdui.validator.Validator;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.services.security.User;


public class AddNotesForm extends Form {	
	protected TextBox notesTextBox;
	protected Button saveButton;
	protected ResetButton clearButton;
	
	public AddNotesForm() {		
	}
	
	public void init() {
		removeChildren();
				
		notesTextBox = new RichTextBox("notesTextBox");		
		notesTextBox.setCols("60");
		notesTextBox.setRows("20");
		saveButton = new Button("saveButton");
		saveButton.setText("Save");
		clearButton = new ResetButton("clearButton");
		clearButton.setText("Clear");
			
		Validator notesTextBoxVal = new ValidatorNotEmpty("notesTextBoxVal", "Notes cannot be empty !");
		notesTextBox.addChild(notesTextBoxVal);	
		addChild(notesTextBox);
		addChild(saveButton);
		addChild(clearButton);
		
	}
		
	

	public Forward onValidate(Event evt) {
		//save the notes		
		createNotes();
		return new Forward(
		"notes",
		"notes.jsp",
		true);			
	 }
	 
	protected void createNotes () throws RuntimeException {
		// get current user
		User user = getWidgetManager().getUser();
		String userId = user.getId();
		
		// create notes object
		Notes notes = new Notes();
		notes.setContent(notesTextBox.getValue().toString().trim());
		notes.setUserId(userId);
		
		try {
			Application application = Application.getInstance();		
			NotesModule handler = (NotesModule)application.getModule(NotesModule.class);
			handler.addNotes(notes);
		}
		catch(Exception e) {
			throw new RuntimeException(e.toString());
		}
	}
	 
	 
	 	
	
	public String getDefaultTemplate() {	
       return "notes/addNotesForm";				 
    }
}
    