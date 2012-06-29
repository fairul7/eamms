package com.tms.elearning.testware.model;

import kacang.model.DaoException;
import kacang.model.DefaultModule;

import kacang.util.Log;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Oct 26, 2004
 * Time: 5:34:20 PM
 * To change this template use Options | File Templates.
 */
public class QuestionModule extends DefaultModule {
    Log log = Log.getLog(getClass());

    public Collection getQuestions(String keyword, String course, String sort,
        boolean desc, int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.getQuestions(keyword, course, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public int countQuestions(String keyword, String course) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.countQuestions(keyword, course);
        } catch (DaoException e) {
            log.error("Error counting questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public int countQuestionsByAssessment(String assessmentId) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.countQuestionsByAssessment(assessmentId);
        } catch (DaoException e) {
            log.error("Error counting questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

        public int countQuestionsByAssessmentSelected(String assessmentId) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.countQuestionsByAssessmentSelected(assessmentId);
        } catch (DaoException e) {
            log.error("Error counting questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }


    public boolean deleteQuestion(String id) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.deleteQuestion(id);

            return true;
        } catch (DaoException e) {
            log.error("Error deleting question " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean deleteQuestions(String id) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.deleteQuestions(id);

            return true;
        } catch (DaoException e) {
            log.error("Error deleting question " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean deleteExamQuestionsByAssessment(String assessmentId) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.deleteExamQuestionsByAssessment(assessmentId);

            return true;
        } catch (DaoException e) {
            log.error("Error deleting question in assessment" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean deleteAssessmentStudent(String user_id, String assessment_id) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.deleteAssessmentStudent(user_id, assessment_id);

            return true;
        } catch (DaoException e) {
            log.error("Error deleting assessment history " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean insertQuestion(Question q) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.insertQuestion(q);

            return true;
        } catch (DaoException e) {
            log.error("Error inserting question" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean insertStudentAssessmentHistory(
        StudentAssessmentHistory studentAssessmentHistory) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.insertStudentAssessmentHistory(studentAssessmentHistory);

            return true;
        } catch (DaoException e) {
            log.error("Error inserting question" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean insertExamQuestion(Question q) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.insertExamQuestion(q);

            return true;
        } catch (DaoException e) {
            log.error("Error inserting question" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean insertrecordStudentStatistic(
        StudentStatistic studentStatistic) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.recordStudentStatistic(studentStatistic);

            return true;
        } catch (DaoException e) {
            log.error("Error inserting statistic" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Question getQuestion(String id) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.getQuestion(id);
        } catch (DaoException e) {
            log.error("Error finding question " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean updateQuestion(Question q) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.updateQuestion(q);

            return true;
        } catch (DaoException e) {
            log.error("Error updating question " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public boolean addToExam(Question q) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            dao.addToExam(q);

            return true;
        } catch (DaoException e) {
            log.error("Error adding to exam" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection showExamQuestions(String exam, String sort, boolean desc,
        int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.showExamQuestions(null, exam, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding exam questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection showAssessmentHistoryByStudent(String user_id,
        boolean desc, int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.showAssessmentHistoryByStudent(user_id, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error retrieve student assessment history " +
                e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection getAssessmentHistoryByUserAndModule(String studentId,
        String moduleId) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.getAssessmentHistoryByUserAndModule(studentId, moduleId);
        } catch (DaoException e) {
            log.error(
                "Error retrieve student assessment history for instructor" +
                e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection getAssessmentFreqStudent(String user_id,
        String assessmentId) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.getAssessmentFreqStudent(user_id, assessmentId);
        } catch (DaoException e) {
            log.error("Error retrieve frequency of assessment" + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection showWrongQuestionsByAssessmentId(String assessmentId,
        boolean desc, int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.showWrongQuestionsByAssessmentId(assessmentId, desc,
                start, rows);
        } catch (DaoException e) {
            log.error("Error retrieve student assessment wrong answer history " +
                e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }

    public Collection showExamQuestionsByAssessment(String assessmentId,
        boolean desc, int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.showExamQuestionsByAssessment(assessmentId, desc, start,
                rows);
        } catch (DaoException e) {
            log.error("Error finding exam questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }


        public Collection showExamQuestionsByAssessmentSelected(String assessmentId,
        boolean desc, int start, int rows) {
        QuestionModuleDao dao = (QuestionModuleDao) getDao();

        try {
            return dao.showExamQuestionsByAssessmentSelected(assessmentId, desc, start,
                rows);
        } catch (DaoException e) {
            log.error("Error finding exam questions " + e.toString(), e);
            throw new QuestionModuleException(e.toString());
        }
    }


}
