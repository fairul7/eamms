package com.tms.portlet.portlets.notes.model;

import java.util.Collection;
import java.util.Date;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;



public class NotesModule extends DefaultModule  {
	
	Log log = Log.getLog(getClass());

	/**
	  * Initializes the Calendar Module Handler, called once during startup.
	  */
	public void init() {
	}
	
	public void addNotes(Notes notes) throws NotesException  {
		
		if (notes.getNotesId() == null) 
			notes.setNotesId(UuidGenerator.getInstance().getUuid());
		notes.setLastModified(new Date());
		notes.setCreationDate(new Date());
		// create notes
		try {
			NotesDao dao = (NotesDao)getDao();
			dao.insertNotes(notes);
		}
		catch(Exception e) {
			throw new NotesException(e.toString());
		}			
	}		
	
	public void updateNotes(Notes notes) throws NotesException  {
		
			 			
			notes.setLastModified(new Date());
		
			// update notes
			try {
				NotesDao dao = (NotesDao)getDao();
				dao.updateNotes(notes);
			}
			catch(Exception e) {
				throw new NotesException(e.toString());
			}			
	}
	
	public boolean deleteNotes(String notesId) {
		// call dao to delete
		NotesDao dao = (NotesDao)getDao();
		try {
			dao.deleteNotes(notesId);
			return true;
		} catch (DaoException e) {
			log.error("Error delete notes " + e.toString(), e);
			return false;
		}
	}
	
		
	public Collection findNotes(String name, String sort, boolean desc, int start, int rows) throws NotesException {
		NotesDao dao = (NotesDao)getDao();
	   	try {	   	
			return dao.selectNotes(name, sort, desc, start, rows);			
		} catch (DaoException e) {
			log.error("Error finding notes " + e.toString(), e);
			throw new NotesException(e.toString());
		}
	}

    public Collection findNotes(String userId, String sort, boolean desc) throws NotesException, DataObjectNotFoundException {
        NotesDao dao = (NotesDao)getDao();
        try {
            return dao.selectNotesByUser(userId, sort, desc, 0,-1);
        }catch (DaoException e) {
            log.error("Error finding notes " + e.toString(), e);
            throw new NotesException(e.toString());
        }
    }

	public Notes findNotes(String notesId, String userId) throws DataObjectNotFoundException, NotesException {
		NotesDao dao = (NotesDao)getDao();
		try {	   	
			return dao.selectNotes(notesId, userId);			
		} catch (DaoException e) {
			log.error("Error finding notes " + e.toString(), e);
			throw new NotesException(e.toString());
		}
	}
	
	public int countNotes() throws NotesException {
		NotesDao dao = (NotesDao)getDao();
	    try {
			return dao.count();
	    } catch (DaoException e) {
			log.error("Error finding notes " + e.toString(), e);
			throw new NotesException(e.toString());
		}
	}
}
