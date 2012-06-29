package com.tms.elearning.testware.model;

import com.tms.elearning.core.model.LearningContentModule;
import kacang.util.Log;
import kacang.model.DaoException;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 23, 2004
 * Time: 3:33:04 PM
 * To change this template use Options | File Templates.
 */
public class AssessmentModule extends LearningContentModule{
    Log log = Log.getLog(getClass());

    public Collection getAssessments(String keyword, String course,String folder, String sort, boolean desc, int start, int rows) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.getAssessments(keyword, course,folder, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }







        public Collection getAssessmentsByModule(String moduleId, String sort, boolean desc, int start, int rows) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.getAssessmentsByModule(moduleId, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }






        public Collection getAssessmentsByStudent(String student_id, String sort, boolean desc, int start, int rows) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.getAssessmentsByStudent(student_id, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }

    public int countAssessments(String keyword, String course, String folder) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.countAssessments(keyword, course, folder);
        } catch (DaoException e) {
            log.error("Error counting assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }


       public int countAssessmentsByModule(String moduleId) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.countAssessmentsByModule(moduleId);
        } catch (DaoException e) {
            log.error("Error counting assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }



    public int countAssessmentsByStudent(String student_id) {
     AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
     try {
         return dao.countAssessmentsByStudent(student_id);
     } catch (DaoException e) {
         log.error("Error counting assessments " + e.toString(),e);
         throw new AssessmentModuleException(e.toString());
     }
 }




    //---tiru
    public Collection getCourseAssessments(String keyword, String course, String sort, boolean desc, int start, int rows) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.getCourseAssessments(keyword, course, sort, desc, start, rows);
        } catch (DaoException e) {
            log.error("Error finding assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }

    public int countCourseAssessments(String keyword, String course) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            return dao.countCourseAssessments(keyword, course);
        } catch (DaoException e) {
            log.error("Error counting assessments " + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }
    //tiru----
    public boolean insertAssessment(Assessment assmt) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            dao.insertAssessment(assmt);
            return true;
        } catch(DaoException e) {
            log.error("Error inserting assessment" + e.toString(),e);
            throw new AssessmentModuleException(e.toString());
        }
    }

     public boolean deleteAssessment(String id) {
        AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
        try {
            dao.deleteAssessment(id);
            return true;
        } catch (DaoException e) {
            log.error("Error deleting assessment " + e.toString(), e);
            throw new AssessmentModuleException(e.toString());
        }
    }

    public Assessment getAssessment(String id) {
           AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
           try {
               return dao.getAssessment(id);
           } catch (DaoException e)  {
               log.error("Error finding assessment " + e.toString(), e);
               throw new AssessmentModuleException(e.toString());
           }
    }

     public boolean updateAssessment(Assessment asmt) {
            AssessmentModuleDao dao = (AssessmentModuleDao)getDao();
            try {
                dao.updateAssessment(asmt);
                return true;
            } catch (DaoException e) {
                log.error("Error updating assessment " + e.toString(), e);
                throw new AssessmentModuleException(e.toString());
            }
     }

    public void setActivationAssessment(String id){

        AssessmentModuleDao dao = (AssessmentModuleDao) getDao();
        try {
            dao.setActivationAssessment(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error set flag for activation in cel_content_assessment");
        }
    }

    public void setDeactivationAssessment(String id){

        AssessmentModuleDao dao = (AssessmentModuleDao) getDao();
        try {
            dao.setDeactivationAssessment(id);
        } catch (DaoException e) {
            Log.getLog(getClass()).warn("Error reverse flag for activation in cel_content_assessment");
        }
    }


    public Assessment checkActive(String id, String startDate, String endDate){

      AssessmentModuleDao dao = (AssessmentModuleDao) getDao();
       try{
           return dao.checkActive(id,startDate,endDate);

       }   catch(DaoException e){
           Log.getLog(getClass()).warn("Error checking whether assessment is activated or not");
       }

        return null;
    }


}
