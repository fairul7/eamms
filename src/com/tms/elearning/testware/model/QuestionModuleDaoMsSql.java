package com.tms.elearning.testware.model;

import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.util.Log;


public class QuestionModuleDaoMsSql extends QuestionModuleDao{
	
	public void init() throws DaoException {
        
		try {
            super.update("ALTER TABLE cel_content_question ALTER COLUMN createdByUser varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cel_content_question");
        }
        try {
            super.update("ALTER TABLE cel_content_question ALTER COLUMN createdByUserId varchar(70)", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error editing table cel_content_question");
        }
		
		try {
            super.update("CREATE TABLE cel_content_assessment_statistic (" +
                "  assessment_id varchar(255) NOT NULL default '0'," +
                "  user_id varchar(255) NOT NULL default '0'," +
                "  dateTook datetime default NULL," +
                "  total_questions integer default '0'," +
                "  wrong_questions integer default '0'" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment_statistic");
        }

        try {
            super.update(
                "CREATE TABLE cel_content_assessment_students_history (\n" +
                "  user_id varchar(255) NOT NULL default '0',\n" +
                "  question varchar(255) NOT NULL default '0',\n" +
                "  wrong_answer varchar(255) NOT NULL default '0',\n" +
                "  right_answer varchar(255) NOT NULL default '0',\n" +
                "  assessment_id varchar(255) NOT NULL default '0',\n" +
                "  last_date datetime NOT NULL default '0000-00-00 00:00:00'," +
                "  frequency integer NOT NULL default '0'" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment_students_history");
        }

        try {
            super.update("CREATE TABLE cel_content_question (\n" +
                "  id varchar(255) NOT NULL default '',\n" +
                "  course_id varchar(255) NOT NULL default '',\n" +
                "  topic_id varchar(255) default NULL,\n" +
                "  module_id varchar(255) default NULL,\n" +
                "  lesson_id varchar(255) default NULL,\n" +
                "  question text NOT NULL,\n" + "  answer_a text,\n" +
                "  answer_b text,\n" + "  answer_c text,\n" +
                "  answer_d text,\n" + "  answer_e text,\n" +
                "  score_a integer default '0',\n" +
                "  score_b integer default '0',\n" +
                "  score_c integer default '0',\n" +
                "  score_d integer default '0',\n" +
                "  score_e integer default '0',\n" +
                "  correct_answer char(3) default '0',\n" +
                "  subjective_answer text,\n" +
                "  is_subjective char(3) default '0',\n" +
                "  createdDate datetime default NULL,\n" +
                "  createdByUser varchar(20) default NULL,\n" +
                "  createdByUserId varchar(20) default NULL,\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_question");
        }

        try {
            super.update("CREATE TABLE cel_content_questions (\n" +
                "  qid varchar(255) NOT NULL default '0',\n" +
                "  assessment_id varchar(255) NOT NULL default '0',\n" +
                "  question_id varchar(255) NOT NULL default '0',\n" +
                "  question text,\n" + "  PRIMARY KEY  (qid)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cms_content_questions");
        }

        try {
            super.update("CREATE TABLE cel_content_assessment (\n" +
                "  id varchar(255) NOT NULL default '',\n" +
                "  course_id varchar(255) NOT NULL default '',\n" +
                "  module_id varchar(255) default NULL,\n" +
                "  lesson_id varchar(255) default NULL,\n" +
                "  level integer default '1',\n" +
                "  name varchar(255) NOT NULL default '',\n" +
                "  start_date datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  end_date datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  time_limit decimal NOT NULL default '0',\n" +
                "\"public\" varchar(50)  DEFAULT '0' NOT NULL,\n" +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  createdByUser varchar(255) NOT NULL default '',\n" +
                "  createdByUserId varchar(255) NOT NULL default '',\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment");
        }
    }
	
	public Collection showWrongQuestionsByAssessmentId(String assessmentId,
	        boolean desc, int start, int rows) throws DaoException {

	        Application app = Application.getInstance();

	        Object[] args = { assessmentId ,app.getCurrentUser().getId()};

	        return super.select("SELECT user_id, question, wrong_answer, right_answer, assessment_id FROM cel_content_assessment_students_history WHERE assessment_id=? AND user_id=?",
	            StudentAssessmentHistory.class, args, start, rows);
	    }
	
	public Collection getQuestions(String keyword, String course, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";
	        String orderBy = (sort != null) ? (sort.equals("question")?"cast(question as varchar)":sort) : "cast(question as varchar)";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { condition };

	        return super.select(
	            "SELECT cel_content_question.id, question FROM cel_content_question WHERE question LIKE ?  ORDER BY " +
	            orderBy, Question.class, args, start, rows);
	    }

}
