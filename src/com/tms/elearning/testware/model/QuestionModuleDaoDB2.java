package com.tms.elearning.testware.model;

import java.util.Collection;
import java.util.HashMap;

import kacang.Application;
import kacang.model.DaoException;

public class QuestionModuleDaoDB2 extends QuestionModuleDao{
	
		public Collection showWrongQuestionsByAssessmentId(String assessmentId,
	        boolean desc, int start, int rows) throws DaoException {

	        Application app = Application.getInstance();

	        Object[] args = { assessmentId ,app.getCurrentUser().getId()};

	        return super.select("SELECT user_id, question, wrong_answer, right_answer, assessment_id FROM cel_content_assessment_students_history WHERE assessment_id=? AND user_id=?",
	            StudentAssessmentHistory.class, args, start, rows);
	    }
	
		public Collection getQuestions(String keyword, String course, String sort,
	        boolean desc, int start, int rows) throws DaoException {
	        String condition = (keyword != null) ? ("%" + keyword.toUpperCase() + "%") : "%";
	        String orderBy = (sort != null) ? (sort.equals("question")?"cast(question AS varchar(255))":sort) : "cast(question AS varchar(255))";

	        if (desc) {
	            orderBy += " DESC";
	        }

	        Object[] args = { condition };

	        return super.select(
	            "SELECT cel_content_question.id, question FROM cel_content_question WHERE UPPER(VARCHAR(question)) LIKE ? ORDER BY " + orderBy, Question.class, args, start, rows);
	    }

	    public int countQuestions(String keyword, String course)
	        throws DaoException {
	        Collection list = null;
	        String condition = (keyword != null) ? ("%" + keyword.toUpperCase() + "%") : "%";

	        Object[] args = { condition };
	        list = super.select("SELECT COUNT(cel_content_question.id) AS total FROM cel_content_question WHERE UPPER(VARCHAR(question)) LIKE ? ",
	                HashMap.class, args, 0, 1);

	        HashMap map = (HashMap) list.iterator().next();

	        return Integer.parseInt(map.get("total").toString());
	    }

}
