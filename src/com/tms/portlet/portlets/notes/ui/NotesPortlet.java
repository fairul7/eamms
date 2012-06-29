package com.tms.portlet.portlets.notes.ui;

import kacang.stdui.Form;
import kacang.stdui.Button;
import kacang.stdui.TextBox;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.ui.Widget;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DataObjectNotFoundException;
import com.tms.portlet.portlets.notes.model.Notes;
import com.tms.portlet.portlets.notes.model.NotesModule;
import com.tms.portlet.portlets.notes.model.NotesException;
import com.tms.portlet.Entity;
import com.tms.portlet.PortletPreference;
import com.tms.portlet.theme.ThemeManager;
import com.tms.collab.messaging.model.Message;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class NotesPortlet extends Form
{
    public static final String DEFAULT_ROW = "10";
    public static final String DEFAULT_COL = "35";
    public static final String PREFERENCE_ROW = "Rows";
    public static final String PREFERENCE_COL = "Columns";

    private Button saveButton, clearButton,newPageButton,deleteButton;
    private TextBox notepad;
    private Notes currentNote;
    private int currentPageNum;
    private boolean newPage;
    private List pages;

    public NotesPortlet()
    {
    }

    public NotesPortlet(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        newPage = false;
        setMethod("post");
        saveButton = new Button("savebutton",Application.getInstance().getMessage("notepad.label.save","Save"));
        clearButton = new Button("clearButton",Application.getInstance().getMessage("notepad.label.clear","Clear"));
        newPageButton = new Button("newpagebutton",Application.getInstance().getMessage("notepad.label.new","New"));
        deleteButton = new Button("deletebutton",Application.getInstance().getMessage("notepad.label.delete","Delete"));
        notepad = new TextBox("notepad");
        notepad.setRows("7");
        if(currentNote!=null)
            notepad.setValue(currentNote.getContent());
        addChild(notepad);
        addChild(saveButton);
        addChild(clearButton);
        addChild(newPageButton);
        addChild(deleteButton);
    }

    public Forward onValidate(Event evt)
    {
        String buttonClicked = findButtonClicked(evt);
        if(saveButton.getAbsoluteName().equals(buttonClicked)){
            deleteButton.setHidden(false);
            NotesModule nm = (NotesModule)Application.getInstance().getModule(NotesModule.class);
            if(newPage){
                currentNote = new Notes();
                currentNote.setContent((String)notepad.getValue());
                currentNote.setUserId(getWidgetManager().getUser().getId());
                try
                {
                    nm.addNotes(currentNote);
                    if(pages==null)
                        currentPageNum= 1;
                    else
                        currentPageNum = pages.size() +1;
                    newPage = false;
                } catch (NotesException e)
                {
                    Log.getLog(getClass()).error(e);
                }
            }else{
                if(currentNote!=null){
                    currentNote.setContent((String)notepad.getValue());
                    currentNote.setUserId(getWidgetManager().getUser().getId());
                    try
                    {
                        nm.updateNotes(currentNote);
                    } catch (NotesException e)
                    {
                        Log.getLog(getClass()).error(e);
                    }
                }
            }
        } else if(clearButton.getAbsoluteName().equals(buttonClicked)){
            notepad.setValue("");
        } else if(newPageButton.getAbsoluteName().equals(buttonClicked)){
            newPage=true;
            notepad.setValue("");
            deleteButton.setHidden(true);
        } else if(deleteButton.getAbsoluteName().equals(buttonClicked)){
            try{
            if(!(currentNote==null || newPage)){
                NotesModule nm = (NotesModule)Application.getInstance().getModule(NotesModule.class);
                nm.deleteNotes(currentNote.getNotesId());
                Collection notes = nm.findNotes(getWidgetManager().getUser().getId(),"creationDate",false);
                int page=1;
                if(notes!=null&&notes.size()>0){
                    Notes note = null;
                    for(Iterator i = notes.iterator();i.hasNext();){
                        note =(Notes)i.next();
                        if(currentPageNum==page)
                        {
                            currentNote = note;
                            notepad.setValue(currentNote.getContent());
                            return null;
                        }
                        page++;
                    }
                    currentNote = note;
                    notepad.setValue(currentNote.getContent());
                }else{
                    notepad.setValue("");
                    newPage = true;
                }
                currentPageNum = page>1?--page:page;
            } } catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
        }  else{
            if(pages!=null){
                deleteButton.setHidden(false);
                for (int i = 0; i < pages.size(); i++)
                {
                    Button b = (Button)pages.get(i);
                    if(b.getAbsoluteName().equals(buttonClicked)){
                        String notesId = b.getMessage();
                        NotesModule nm = (NotesModule)Application.getInstance().getModule(NotesModule.class);
                        try
                        {
                            currentNote = nm.findNotes(notesId,getWidgetManager().getUser().getId());
                            notepad.setValue(currentNote.getContent());
                            currentPageNum = Integer.parseInt(b.getText());
                            newPage= false;
                        } catch (DataObjectNotFoundException e)
                        {
                            Log.getLog(getClass()).error(e);
                        } catch (NotesException e)
                        {
                            Log.getLog(getClass()).error(e);
                        }
                    }
                }
            }
        }


        return super.onValidate(evt);

    }

    public void onRequest(Event evt)
    {
        super.onRequest(evt);
        if(pages==null)
        {
            try
            {
                NotesModule nm = (NotesModule)Application.getInstance().getModule(NotesModule.class);
                Collection notes = nm.findNotes(getWidgetManager().getUser().getId(),"creationDate",false);
                int page=1;
                if(notes!=null&&notes.size()>0)
                {
                    pages = new ArrayList(notes.size());
                    Notes note = null;
                    for(Iterator i = notes.iterator();i.hasNext();)
                    {
                        note =(Notes)i.next();
                        page++;
                    }
                    if(note!=null)
                    {
                        currentNote = note;
                        notepad.setValue(currentNote.getContent());
                    }
                }
                else
                    newPage = true;
                currentPageNum = page>1?--page:1;
            }
            catch(Exception e)
            {
                Log.getLog(getClass()).error(e);
            }
        }
        //Retrieving preferences
        Entity entity = (Entity) evt.getRequest().getAttribute(ThemeManager.LABEL_ENTITY);
        PortletPreference preferenceRows = entity.getPreference(PREFERENCE_ROW);
        PortletPreference preferenceCols = entity.getPreference(PREFERENCE_COL);
        if(preferenceRows != null)
            notepad.setRows(preferenceRows.getPreferenceValue());
        else
            notepad.setRows(DEFAULT_ROW);
        if(preferenceCols != null)
            notepad.setCols(preferenceCols.getPreferenceValue());
        else
            notepad.setCols(DEFAULT_COL);
    }

    public String getDefaultTemplate()
    {
        return "notepad/notepadportlet";
    }

    public Button getSaveButton()
    {
        return saveButton;
    }

    public void setSaveButton(Button saveButton)
    {
        this.saveButton = saveButton;
    }

    public Button getClearButton()
    {
        return clearButton;
    }

    public void setClearButton(Button clearButton)
    {
        this.clearButton = clearButton;
    }

    public Button getNewPageButton()
    {
        return newPageButton;
    }

    public void setNewPageButton(Button newPageButton)
    {
        this.newPageButton = newPageButton;
    }

    public TextBox getNotepad()
    {
        return notepad;
    }

    public void setNotepad(TextBox notepad)
    {
        this.notepad = notepad;
    }

    public Notes getCurrentNote()
    {
        return currentNote;
    }

    public void setCurrentNote(Notes currentNote)
    {
        this.currentNote = currentNote;
    }

    public boolean isNewPage()
    {
        return newPage;
    }

    public void setNewPage(boolean newPage)
    {
        this.newPage = newPage;
    }

    public List getPages()
    {
        try{
            if(pages!=null&&pages.size()>0)
                for(Iterator i= pages.iterator();i.hasNext();){
                    Button b = (Button) i.next();
                    removeChild(b);
                }
            NotesModule nm = (NotesModule)Application.getInstance().getModule(NotesModule.class);
            Collection notes = nm.findNotes(getWidgetManager().getUser().getId(),"creationDate",false);
            if(pages == null)
                pages = new ArrayList(notes.size());

            while(pages.size()>notes.size()&&pages.size()>0)
                pages.remove(pages.size()-1);

            int page=1;
            int pagesIndex = 0;
            Notes note = null;
            Button pageButton;
            for(Iterator i = notes.iterator();i.hasNext();){
                note =(Notes)i.next();
                if(pages.size()<=pagesIndex){
                    pageButton = new Button("page"+page,""+page);
                    pages.add(pageButton);
                }
                else{
                    pageButton = (Button)pages.get(pagesIndex);
                }
                addChild(pageButton);
                // pageButton.set
                pageButton.setMessage(note.getNotesId());
                page++;
                pagesIndex++;
            }

        }catch(Exception e){
            Log.getLog(getClass()).error(e);
        }
        return pages;
    }

    public void setPages(List pages)
    {
        this.pages = pages;
    }

    public int getCurrentPageNum()
    {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum)
    {
        this.currentPageNum = currentPageNum;
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton)
    {
        this.deleteButton = deleteButton;
    }
}
