package com.tms.collab.vote.ui;

import com.tms.collab.vote.model.Answer;
import com.tms.collab.vote.model.PollModule;
import com.tms.collab.vote.model.Question;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Panel;
import kacang.stdui.Table;
import kacang.stdui.event.FormEventListener;
import kacang.stdui.event.TableEventListener;
import kacang.ui.Event;
import kacang.ui.Forward;

/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Apr 25, 2003
 * Time: 10:52:23 AM
 * To change this template use Options | File Templates.
 */
public class PollAdminView extends Panel implements TableEventListener,FormEventListener
{
    public static final String POLL_ADMIN_VIEW = "view";
    public static final String POLL_ADMIN_EDIT = "edit";
    private PollQuestionTable questionTable;
    private String action;
    private PollEditQuestion editQuestionView;

    public PollEditQuestion getEditQuestionView()
    {
        return editQuestionView;
    }

    public void setEditQuestionView(PollEditQuestion editQuestionView)
    {
        this.editQuestionView = editQuestionView;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public PollQuestionTable getQuestionTable()
    {
        return questionTable;
    }

    public void setQuestionTable(PollQuestionTable questionTable)
    {
        this.questionTable = questionTable;
    }

    public Forward onSubmit(Event evt)
    {
        return null;
    }

    public Forward onValidate(Event evt)
    {
        String buttonName = editQuestionView.findButtonClicked(evt);
         if(buttonName!=null && buttonName.equals(editQuestionView.getCancelButton().getAbsoluteName()))
        {
            this.action = POLL_ADMIN_VIEW;
            editQuestionView.setQuestion(null,evt);
        }



        return null;
    }

    public void onRequest(Event evt)
    {
        if("view".equals(evt.getRequest().getParameter("event")))
        {
            try
            {
                String id = editQuestionView.getId();
                PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
                Question question = pm.getQuestion(id);
                if(question==null)
                {
                    StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
                    ss.delete(new StorageFile("/vote/"+id));
                    Answer[] answers ;
                    if((answers = pm.getAnswers(id))!=null)
                    {
                        for(int i=0;i<answers.length;i++)
                            pm.deleteAnswer(answers[i]);
                    }
                }
                this.action = POLL_ADMIN_VIEW;
                editQuestionView.setQuestion(null,evt);
            }
            catch(Exception e){}
           // return new Forward(null,evt.getRequest().getRequestURI(),true);
        }
        else if("newVote".equals(evt.getRequest().getParameter("event")))
        {
            editQuestionView.setQuestion(null,evt);
            this.action=POLL_ADMIN_EDIT;
        }
    }

    public Forward onValidationFailed(Event evt)
    {
        return null;
    }

    public Forward onSort(Event evt)
    {
        return null;
    }

    public Forward onFilter(Event evt)
    {
        return null;
    }

    public Forward onPage(Event evt)
    {
        return null;
    }

    public Forward onSelection(Event evt)
    {
        if(evt.getType().equals(Table.PARAMETER_KEY_SELECTION))
        {
            String id = evt.getRequest().getParameter("id");
            editQuestionView.setQuestion(id,evt);
            this.action=POLL_ADMIN_EDIT;
            return new Forward(null,evt.getRequest().getRequestURI(),true);
        }
        return null;
    }

    public Forward onAction(Event evt)
    {
        if(questionTable.getSelectedAction().equals("add"))
        {
             editQuestionView.setQuestion(null,evt);
             this.action=POLL_ADMIN_EDIT;
        }
        else if(questionTable.getSelectedAction().equals("delete"))
        {
             editQuestionView.setQuestion(null,evt);
             this.action= POLL_ADMIN_VIEW;
        }
        return null;
    }

    public Forward actionPerformed(Event evt)
    {
        return super.actionPerformed(evt);
    }


    public void init()
    {
       // PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
       // questions = pm.getQuestions();
       // selectedQuestion = questions[0];
        questionTable = new PollQuestionTable("Questions");
        questionTable.setWidgetManager(getWidgetManager());
        questionTable.init();
        questionTable.addTableEventListener(this);

        editQuestionView = new PollEditQuestion("Question");
        addChild(editQuestionView);
        editQuestionView.init();
        editQuestionView.addFormEventListener(this);
        addChild(questionTable);

        this.action = POLL_ADMIN_VIEW;
    }

    public String getTemplate()
    {
        return "vote/PollAdmin";
    }



}
