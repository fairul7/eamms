package com.tms.elearning.testware.ui;

import com.tms.elearning.testware.model.AssessmentModule;

import kacang.Application;

import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import java.util.Collection;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 3:25:14 PM
 * To change this template use Options | File Templates.
 */
public class resultAssessmentsTable extends Table implements Serializable{


    public resultAssessmentsTable(String name) {
        setName(name);
    }

    public void init() {
        setModel(new resultAssessmentsFormModel());
        setPageSize(10);
    }

    public class resultAssessmentsFormModel extends TableModel {
        public resultAssessmentsFormModel() {
            Application application = Application.getInstance();
            TableColumn assCol = new TableColumn("name",
                    application.getMessage("eLearning.assessment.label"));
            assCol.setUrl("eachAssessmentstat.jsp");
            assCol.setUrlParam("assessment_id"); //pass the assessment id
            addColumn(assCol);
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.getAssessmentsByStudent(application.getCurrentUser()
                                                             .getId(),
                getSort(), isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            AssessmentModule module = (AssessmentModule) application.getModule(AssessmentModule.class);

            return module.countAssessmentsByStudent(application.getCurrentUser()
                                                               .getId());
        }
    }
}
