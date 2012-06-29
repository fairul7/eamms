package com.tms.collab.vote.ui;

import com.tms.collab.vote.model.PollModule;
import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: May 2, 2003
 * Time: 4:01:19 PM
 * To change this template use Options | File Templates.
 */
public class PollQuestionTable extends Table
{

    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public PollQuestionTable()
    {
        super();
    }

    public PollQuestionTable(String name)
    {
        super(name);
    }

    public void init()
    {
        super.init();
        setModel(new QuestionTableModel());
        setNumbering(true);
        setMultipleSelect(true);
        setWidth("100%");
    }

    public class QuestionTableModel extends TableModel
    {
        private Collection col = null;


        public QuestionTableModel()
        {
	        Application application = Application.getInstance();
            //addColumn(new TableColumn("question","Question"));
            TableColumn titleColumn = new TableColumn("title", application.getMessage("general.label.title", "Title"));
            titleColumn.setUrl("");
            titleColumn.setUrlParam("id");
            addColumn(titleColumn);
            /*TableColumn questionColumn = new TableColumn("question","Question");
            questionColumn.setUrl("");
            questionColumn.setUrlParam("id");
            addColumn(questionColumn);*/
            // addColumn(new TableColumn("id","ID"));
            addColumn(new TableColumn("total", application.getMessage("general.label.total", "Total")));
            addAction(new TableAction("add", application.getMessage("vote.label.newVote", "New Vote")));
            TableAction deleteAction = new TableAction("delete", application.getMessage("general.label.delete", "Delete"), application.getMessage("vote.message.delete", "Are you sure you want to delete the following question(s)?"));
            addAction(deleteAction);
            addFilter(new TableFilter("question",""));
            String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn pendingColumn = new TableColumn("pending", application.getMessage("vote.label.pending", "Pending"));
            pendingColumn.setFormat(booleanFormat);
            addColumn(pendingColumn);
            TableColumn activeColumn = new TableColumn("active", application.getMessage("general.label.active", "Active"));
            activeColumn.setFormat(booleanFormat );
            addColumn(activeColumn);
            TableColumn publicColumn = new TableColumn("ispublic", application.getMessage("forum.label.public", "Public"));
            publicColumn.setFormat(booleanFormat );
            addColumn(publicColumn);
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public Collection getTableRows()
        {

            PollModule pm =(PollModule)Application.getInstance().getModule(PollModule.class);
            col = pm.getQuestionsAsCollection((String)getFilterValue("question"),getSort(),isDesc(),getStartIndex(),getRows());
            return col;
        }

        public int getTotalRowCount()
        {
            PollModule pm =(PollModule)Application.getInstance().getModule(PollModule.class);
            return pm.getQuestions().length;
        }



        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
            try
            {
                if("add".equals(action))
                {
                   /* Question question = new Question();
                    question.setId(UuidGenerator.getInstance().getUuid());
                    PollModule pm =(PollModule) Application.getInstance().getModule(PollModule.class);
                    pm.addQuestion(question);
                    setId(question.getId());
                    setCurrentPage(getPageCount());
                    return new Forward(null,evt.getRequest().getRequestURI(),true);*/
                }
                else if("delete".equals(action))
                {
                    int pageRequired=0;

                    PollModule pm =(PollModule) Application.getInstance().getModule(PollModule.class);
                    for(int i=0;i<selectedKeys.length;i++)
                    {
                        pm.deleteQuestion(selectedKeys[i]);
                        StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
                        ss.delete(new StorageFile("/vote/"+selectedKeys[i]));
                    }
                    int totalRows = getTotalRowCount();
                    if(totalRows<=getPageSize()) setCurrentPage(1);
                    else
                    {
                        pageRequired = totalRows/getPageSize();
                        if(totalRows % getPageSize()!=0)
                                pageRequired+=1;
                        if(getCurrentPage()>pageRequired)
                            setCurrentPage(pageRequired);

                    }
                }
            }
            catch(Exception e)
            {            }
            return null;
        }


    }


}
