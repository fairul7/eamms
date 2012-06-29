package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.AssessmentModule;

import kacang.Application;

import kacang.stdui.*;

import java.io.Serializable;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 3:25:14 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentTableByModule extends Table implements Serializable {

    protected String moduleId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void init() {
        setModel(new AssessmentTableModel());
        setPageSize(10);
    }

    public class AssessmentTableModel extends TableModel {
        public AssessmentTableModel() {
            Application application = Application.getInstance();
            TableColumn assCol = new TableColumn("name",
                    application.getMessage("eLearning.assessment.label"));
            assCol.setUrl("questionDo.jsp");
            assCol.setUrlParam("id");
            addColumn(assCol);


            TableColumn difficulty = new TableColumn("level",
            application.getMessage("eLearning.assessment.difficulty"));

            addColumn(difficulty);


            TableColumn lastTookCol = new TableColumn("lastTakenDateStr",
                    application.getMessage("eLearning.assessment.lastDate"));
            addColumn(lastTookCol);

            /*            TableColumn frequencyCol = new TableColumn("numbTakenStr",
                                application.getMessage("eLearning.assessment.numbtimetaken"));
                        addColumn(frequencyCol);*/
            TableColumn assCol2 = new TableColumn("startDate",
                    application.getMessage("eLearning.assessment.startdate"));
            assCol2.setFormat(new TableDateFormat("yyyy-MM-dd 00:00:00"));

            addColumn(assCol2);

            TableColumn assCol3 = new TableColumn("endDate",
                    application.getMessage("eLearning.assessment.enddate"));
            assCol3.setFormat(new TableDateFormat("yyyy-MM-dd 00:00:00"));
            addColumn(assCol3);


        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.getAssessmentsByModule(getModuleId(), getSort(),
                isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.countAssessmentsByModule(getModuleId());
        }
    }
}
