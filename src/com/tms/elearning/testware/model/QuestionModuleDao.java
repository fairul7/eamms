package com.tms.elearning.testware.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao;

import kacang.util.Log;
import kacang.Application;

import java.util.Collection;
import java.util.HashMap;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 4:19:49 PM
 * To change this template use Options | File Templates.
 */
public class QuestionModuleDao extends DataSourceDao {
    protected String getTableName() {
        return "cel_content_question";
    }

    protected Class getContentObjectClass() {
        return Question.class;
    }

    public void init() throws DaoException {
        try {
            super.update("CREATE TABLE cel_content_assessment_statistic (" +
                "  assessment_id varchar(255) NOT NULL default '0'," +
                "  user_id varchar(255) NOT NULL default '0'," +
                "  dateTook datetime default NULL," +
                "  total_questions int(11) unsigned default '0'," +
                "  wrong_questions int(11) unsigned default '0'" + ")", null);
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
                "  frequency int(11) NOT NULL default '0'" + ")", null);
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
                "  score_a int(11) unsigned default '0',\n" +
                "  score_b int(11) unsigned default '0',\n" +
                "  score_c int(11) unsigned default '0',\n" +
                "  score_d int(11) unsigned default '0',\n" +
                "  score_e int(11) unsigned default '0',\n" +
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
                "  level int(11) default '1',\n" +
                "  name varchar(255) NOT NULL default '',\n" +
                "  start_date datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  end_date datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  time_limit double NOT NULL default '0',\n" +
                
            // "  is_active char(1) NOT NULL default '0',\n" +
            "  public ENUM('0','1')  DEFAULT \"0\" NOT NULL,\n" +
                "  createdDate datetime NOT NULL default '0000-00-00 00:00:00',\n" +
                "  createdByUser varchar(255) NOT NULL default '',\n" +
                "  createdByUserId varchar(255) NOT NULL default '',\n" +
                "  PRIMARY KEY  (id)\n" + ")", null);
        } catch (DaoException e) {
            Log.getLog(getClass()).debug("Error creating table cel_content_assessment");
        }
    }

    public Collection getQuestions(String keyword, String course, String sort,
        boolean desc, int start, int rows) throws DaoException {
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";
        String orderBy = (sort != null) ? sort : "question";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { condition };

        return super.select(
            "SELECT cel_content_question.id, question FROM cel_content_question WHERE question LIKE ?  ORDER BY " +
            orderBy, Question.class, args, start, rows);
    }

    public int countQuestions(String keyword, String course)
        throws DaoException {
        Collection list = null;
        String condition = (keyword != null) ? ("%" + keyword + "%") : "%";

        Object[] args = { condition };
        list = super.select("SELECT COUNT(cel_content_question.id) AS total FROM cel_content_question WHERE question LIKE ? ",
                HashMap.class, args, 0, 1);

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }

    public int countQuestionsByAssessment(String assessmentId)
        throws DaoException {
        Collection list = null;

        if (("".equals(assessmentId)) || (assessmentId == null)) {
            /*list = super.select("SELECT COUNT(cel_content_question.id) AS total FROM cel_content_question ",
                    HashMap.class, "", 0, 1);
            */
            list = super.select("SELECT COUNT(q.id) AS total FROM cel_content_question q LEFT JOIN cel_content_questions qs on q.id = qs.question_id \n" +
                    "WHERE qs.question_id is null",
                                HashMap.class, "", 0, 1);

        } else {
            Object[] args = { assessmentId };

             list = super.select("select count(q.id) as total from cel_content_question as q LEFT JOIN cel_content_questions as qs ON q.id=qs.question_id and (qs.assessment_id =?  ) WHERE qs.question_id IS NULL",
                    HashMap.class, args, 0, 1);

            /*list = super.select("SELECT count(distinct question_id) FROM cel_content_questions WHERE assessment_id !=? OR assessment_id IS NULL",
                    HashMap.class, args, 0, 1);*/

        }

        HashMap map = (HashMap) list.iterator().next();

        return Integer.parseInt(map.get("total").toString());
    }


    public int countQuestionsByAssessmentSelected(String assessmentId)
           throws DaoException {
           Collection list = null;

           if (("".equals(assessmentId)) || (assessmentId == null)) {
               /*list = super.select("SELECT COUNT(cel_content_question.id) AS total FROM cel_content_question ",
                       HashMap.class, "", 0, 1);
               */
               list = super.select("SELECT COUNT(q.id) AS total FROM cel_content_question q LEFT JOIN cel_content_questions qs on q.id = qs.question_id \n" +
                       "WHERE qs.question_id is null",
                                   HashMap.class, "", 0, 1);

           } else {
               Object[] args = { assessmentId };

                list = super.select("select count(qs.assessment_id) as total from cel_content_question as q RIGHT JOIN cel_content_questions as qs ON q.id=qs.question_id WHERE qs.assessment_id =?",
                       HashMap.class, args, 0, 1);

               /*list = super.select("SELECT count(distinct question_id) FROM cel_content_questions WHERE assessment_id !=? OR assessment_id IS NULL",
                       HashMap.class, args, 0, 1);*/

           }

           HashMap map = (HashMap) list.iterator().next();

           return Integer.parseInt(map.get("total").toString());
       }


    public void deleteQuestion(String id) throws DaoException {
        Object[] args = new String[] { id };

        //erase question from question bank
        super.update("DELETE FROM cel_content_question WHERE id=?", args);

        //erase questions from asessment set (selected questions)
        deleteQuestions(id);
    }

    public void deleteQuestions(String id) throws DaoException {
        Object[] args = new String[] { id };
        super.update("DELETE FROM cel_content_questions WHERE question_id=?",
            args);
    }

    public void insertQuestion(Question q) throws DaoException {
        super.update("INSERT INTO cel_content_question (id, course_id, module_id, lesson_id, question,  answer_a, answer_b, answer_c, answer_d, correct_answer,  createdDate, createdByUser, createdByUserId) VALUES (#id#, #course_id#, #module_id#, #lesson_id#, #question#, #answer_a#, #answer_b#, #answer_c#, #answer_d#, #correct_answer#, #createdDate#, #createdByUser#, #createdByUserId#)",
            q);
    }

    public void insertExamQuestion(Question q) throws DaoException {
        super.update("INSERT INTO cel_content_question (id, course_id, module_id, lesson_id, question, createdDate, createdByUser, createdByUserId) VALUES (#id#, #course_id#, #module_id#, #lesson_id#, #question#, #createdDate#, #createdByUser#, #createdByUserId#)",
            q);
    }

    public void addToExam(Question q) throws DaoException {
        super.update("INSERT INTO cel_content_questions (qid, assessment_id,question_id,question) VALUES (#qbank_id#, #exam_id#, #question_id#,#question#)",
            q);
    }

    public Question getQuestion(String id) throws DaoException {
        Object[] args = { id };
        Collection results = super.select("SELECT id, course_id, topic_id, module_id, lesson_id, question, answer_a, answer_b, answer_c, answer_d, correct_answer FROM cel_content_question WHERE id = ?",
                Question.class, args, 0, 1);

        if (results.size() == 0) {
            return null;
        }

        return (Question) results.iterator().next();
    }

    public void updateQuestion(Question q) throws DaoException {
        super.update("UPDATE cel_content_question SET course_id=#course_id#, module_id=#module_id#, lesson_id=#lesson_id#, question=#question#, answer_a=#answer_a#, answer_b=#answer_b#, answer_c=#answer_c#, answer_d=#answer_d#, correct_answer=#correct_answer# WHERE id=#id#",
            q);
    }

    public Collection showExamQuestions(String keyword, String exam,
        String sort, boolean desc, int start, int rows)
        throws DaoException {
        String orderBy = (sort != null) ? sort : "question";

        if (desc) {
            orderBy += " DESC";
        }

        Object[] args = { exam };

        return super.select(
            "SELECT distinct question_id, question FROM cel_content_questions WHERE assessment_id = ? ORDER BY " +
            orderBy, Question.class, args, start, rows);
    }

    public Collection showExamQuestionsByAssessment(String assessmentId,
        boolean desc, int start, int rows) throws DaoException {
        if (("".equals(assessmentId)) || (assessmentId == null)) {
            /*return super.select("select id, course_id, module_id, lesson_id, question, answer_a, answer_b, answer_c, answer_d, answer_e, correct_answer from cel_content_question;",
                Question.class, "", start, rows);
            */
            return super.select("SELECT q.id, q.course_id, q.module_id, q.lesson_id, q.question, q.answer_a, q.answer_b, q.answer_c, q.answer_d, q.answer_e, q.correct_answer FROM cel_content_question q LEFT JOIN cel_content_questions qs on q.id = qs.question_id \n" +
                    "WHERE qs.question_id is null",
                            Question.class, "", start, rows);



        } else {
            Object[] args = { assessmentId };

            return super.select("select q.id, q.course_id, q.module_id, q.lesson_id, q.question, q.answer_a, q.answer_b, q.answer_c, q.answer_d, q.answer_e, q.correct_answer from cel_content_question as q LEFT JOIN cel_content_questions as qs ON q.id=qs.question_id and (qs.assessment_id =?  ) WHERE qs.question_id IS NULL",
                Question.class, args, start, rows);
        }
    }


    public Collection showExamQuestionsByAssessmentSelected(String assessmentId,
        boolean desc, int start, int rows) throws DaoException {
        if (("".equals(assessmentId)) || (assessmentId == null)) {
            /*return super.select("select id, course_id, module_id, lesson_id, question, answer_a, answer_b, answer_c, answer_d, answer_e, correct_answer from cel_content_question;",
                Question.class, "", start, rows);
            */
            return super.select("SELECT q.id, q.course_id, q.module_id, q.lesson_id, q.question, q.answer_a, q.answer_b, q.answer_c, q.answer_d, q.answer_e, q.correct_answer FROM cel_content_question q LEFT JOIN cel_content_questions qs on q.id = qs.question_id \n" +
                    "WHERE qs.question_id is null",
                            Question.class, "", start, rows);



        } else {
            Object[] args = { assessmentId };

            return super.select("select q.id, q.course_id, q.module_id, q.lesson_id, q.question, q.answer_a, q.answer_b, q.answer_c, q.answer_d, q.answer_e, q.correct_answer from cel_content_question as q RIGHT JOIN cel_content_questions as qs ON q.id=qs.question_id WHERE qs.assessment_id =?",
                Question.class, args, start, rows);
        }
    }


    public void deleteExamQuestionsByAssessment(String assessmentId)
        throws DaoException {
        Object[] args = { assessmentId };

        super.update("DELETE FROM cel_content_questions WHERE assessment_id=?",
            args);
    }

    public Collection showAssessmentHistoryByStudent(String user_id,
        boolean desc, int start, int rows) throws DaoException {
        Object[] args = { user_id };

        return super.select("SELECT statistic.dateTook, assessment.name, statistic.wrong_questions,statistic.total_questions,assessment.module_id  FROM  cel_content_assessment as assessment, cel_content_assessment_statistic as statistic WHERE statistic.assessment_id=assessment.id  AND statistic.user_id=?",
            StudentStatistic.class, args, start, rows);
    }

    public Collection getAssessmentFreqStudent(String user_id,
        String assessmentId) throws DaoException {
        Object[] args = { user_id, assessmentId };

        return super.select("SELECT frequency as numbTaken FROM cel_content_assessment_students_history WHERE user_id=? AND assessment_id=?",
            Assessment.class, args, 0, 1);
    }

    public Collection getAssessmentHistoryByUserAndModule(String user_id,
        String moduleId) throws DaoException {
        Object[] args = { moduleId, user_id };

        return super.select("SELECT cel_content_assessment.name AS name , cel_content_assessment_statistic.wrong_questions AS wrong_questions, cel_content_assessment_statistic.total_questions AS total_questions, cel_content_assessment_statistic.dateTook AS dateTook FROM cel_content_assessment, cel_content_assessment_statistic WHERE cel_content_assessment.id = cel_content_assessment_statistic.assessment_id AND cel_content_assessment.module_id=? AND cel_content_assessment_statistic.user_id=?",
            StudentStatistic.class, args, 0, -1);
    }

    public Collection showWrongQuestionsByAssessmentId(String assessmentId,
        boolean desc, int start, int rows) throws DaoException {

        Application app = Application.getInstance();

        Object[] args = { assessmentId ,app.getCurrentUser().getId()};

        return super.select("SELECT * FROM cel_content_assessment_students_history WHERE assessment_id=? AND user_id=?",
            StudentAssessmentHistory.class, args, start, rows);
    }

    public void recordStudentStatistic(StudentStatistic studentStatistic)
        throws DaoException {
        super.update("INSERT INTO cel_content_assessment_statistic (assessment_id, dateTook,total_questions, wrong_questions, user_id) VALUES (#assessment_id#, #dateTook#,#total_questions#, #wrong_questions#, #user_id#)",
            studentStatistic);
    }

    public void insertStudentAssessmentHistory(
        StudentAssessmentHistory studentAssessmentHistory)
        throws DaoException {
        super.update(
            "INSERT INTO cel_content_assessment_students_history (user_id, question, wrong_answer,\n" +
            "right_answer, assessment_id, frequency, last_date) VALUES (#user_id#, #question#, #wrong_answer#, #right_answer#, #assessment_id#, #numbTaken#, #lastTakenDate#)",
            studentAssessmentHistory);
    }

    public void deleteAssessmentStudent(String user_id, String assessment_id)
        throws DaoException {
        Object[] args = new String[] { user_id, assessment_id };
        super.update("DELETE FROM cel_content_assessment_students_history WHERE user_id=? and assessment_id=?",
            args);
    }
}
