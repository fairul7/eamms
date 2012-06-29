package com.tms.portlet.portlets.notes.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;

import kacang.util.Log;
import java.util.Collection;
import java.util.HashMap;

/**
 * Data Access Object for the Calendar Module,
 * specifically targetted at MySQL although generic SQL is used as much as possible.
 */

public class NotesDao extends DataSourceDao {
	Log log = Log.getLog(getClass());
	
	/**
	  * Creates database tables.
	  */
	public void init() throws DaoException  {
				
		super.update("CREATE TABLE notes (notesId varchar(255) NOT NULL default ''," +
				 "userId varchar(255) NOT NULL default ''," +				 
				 "content text, " + 
				 "lastModified datetime default NULL," +
                 "creationDate datetime default NULL,"+
				 "PRIMARY KEY (notesId), KEY(userId))", null);
	}
	
	/**
	  * Inserts a notes into the database.
	  * @param notes The notes object to insert.
	  * @throws kacang.model.DaoException when a database error occurs.
	  */
	public void insertNotes(Notes notes) throws DaoException { 		

		try {
			// insert notes
		 	String sql =
				 "INSERT INTO notes (notesId, userId, content, lastModified,creationDate) " +
				 "VALUES (#notesId#, #userId#, #content#, #lastModified#,#creationDate#)";
				 
			super.update(sql, notes);
						

	 	}
		catch(Exception e) {			
			log.error(e.getMessage(), e);
			throw new DaoException(e.toString());
		}
	}
	
	/**
	  * Deletes an existing notes. 
	  * @param notesId The unique identifier for the notes.
	  * @throws kacang.model.DaoException
	  */
	public void deleteNotes(String notesId) throws DaoException {
		try {
			// delete notes
		 	Notes notes = new Notes();
		    notes.setNotesId(notesId);
		    String sql = "DELETE FROM notes WHERE notesId=#notesId#";
		    super.update(sql, notes);

		}
		catch(DaoException e) {
			log.error(e.getMessage(), e);
			throw new DaoException(e.toString());
		}
	}
	
	/**
	  * Updates a notes.
	  * @param notes Notes to update.
	  * @throws kacang.model.DaoException
	  */
	public void updateNotes(Notes notes) throws DaoException {
		deleteNotes(notes.getNotesId());
		insertNotes(notes);
	}
	
	
	/**
	  * Returns a specific notes based on the notes's unique identifier.
	  * @param notesId The unique ID to identify the notes.
	  * @return A Notes object representing the desired notes.
	  * @throws kacang.model.DataObjectNotFoundException when the notes does not exist.
	  * @throws kacang.model.DaoException when a database error occurs.
	  */
	public Notes selectNotes(String notesId, String userId) throws DataObjectNotFoundException, DaoException {
		try {
			Notes notes = null;

			// select notes
			String sql =
					"SELECT notesId,userId,content,lastModified,creationDate " +
					"FROM notes WHERE notesId=? AND userId = ?";
			Object[] args = new Object[] {
								notesId, userId
							};
			Collection notesList = super.select(sql, Notes.class, args, 0, -1);
			
			if (notesList.size() == 0)
				throw new DataObjectNotFoundException();
				
			notes = (Notes)notesList.iterator().next();
						
			return notes;
		}
		catch(DaoException e) {
			log.error(e.getMessage(), e);
			throw new DaoException(e.toString());
		}
	}

    /**
     * Returns a specific notes based on the notes's unique identifier.
     * @return A Notes object representing the desired notes.
     * @throws kacang.model.DataObjectNotFoundException when the notes does not exist.
     * @throws kacang.model.DaoException when a database error occurs.
     */
    public Collection selectNotesByUser(String userId , String sort, boolean desc, int start, int rows) throws DataObjectNotFoundException, DaoException {
        try {
            // select notes
            String orderBy = (sort != null) ? sort : "lastModified";
            if (desc)
                orderBy += " DESC";
            String sql =
                    "SELECT notesId,userId,content,lastModified, creationDate " +
                    "FROM notes WHERE userId = ? ORDER BY " + orderBy;
            Object[] args = new Object[] {
                userId
            };
            return super.select(sql, Notes.class, args, 0, -1);

        }
        catch(DaoException e) {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

	public Collection selectNotes(String name, String sort, boolean desc, int start, int rows) throws DaoException {
		
							 
		String condition = (name != null) ? "%" + name + "%" : "%";
		Object[] args = { condition };			 
		
		String orderBy = (sort != null) ? sort : "lastModified";
		if (desc)
			orderBy += " DESC";
		
		// select notes
		String sql =
					"SELECT notesId,userId,content,lastModified,creationDate " +
					"FROM notes WHERE content like ? ORDER BY " + orderBy;
		Collection notesList = super.select(sql, Notes.class, args, start, rows);
				
											
		return notesList;
	}
	
	public int count() throws DaoException {

		 Collection list = super.select("SELECT COUNT(*) AS total FROM notes", HashMap.class, null, 0, 1);
		 HashMap map = (HashMap)list.iterator().next();
		 return Integer.parseInt(map.get("total").toString());
	}


	
	

	
}
