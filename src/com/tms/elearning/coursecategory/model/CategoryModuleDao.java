package com.tms.elearning.coursecategory.model;

import com.tms.elearning.core.model.DefaultLearningContentDao;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.lesson.model.Lesson;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;

import java.util.Collection;
import java.util.HashMap;


public class CategoryModuleDao extends DefaultLearningContentDao {

    protected String getTableName() {
        return "cel_content_course_category";
    }

    protected Class getContentObjectClass() {
        return Category.class;
    }

    public void init() throws DaoException {
        super.update("CREATE TABLE cel_content_course_category (" +
                "  id varchar(50) NOT NULL default ''," +
                "  category varchar(50) unique default NULL,"+
                "  createdByUser varchar(50),"+
                "  createdByUserId varchar(50),"+
                "  createdDate datetime,"+
                "  PRIMARY KEY  (id)" +
                ")", null);
    }

    public void insert(Category category) throws DaoException {
        super.update("INSERT INTO cel_content_course_category (id,category,createdByUser,createdByUserId,createdDate) VALUES (#id#,#category#,#createdByUser#,#createdByUserId#,#createdDate#)", category);
     //   super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", category);
    }


    public void update(Category category) throws DaoException {
            super.update("UPDATE cel_content_course_category SET category=#category#,createdByUser=#createdByUser#,createdByUserId=#createdByUserId#,createdDate=#createdDate# where id=#id#", category);
         //   super.update("INSERT INTO cel_content_status (id,new) VALUES (#id#,1)", category);
        }











/*    public void update(Category category) throws DaoException {
        super.update("UPDATE cel_content_course SET
        =#name#, author=#author#, synopsis=#synopsis#, instructor=#instructor#, category=#category# WHERE id=#id#", Category);
        super.update("INSERT INTO cel_content_status (id,modified,modifiedDate,modifiedByUser,modifiedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)", category);
    }
    public void updateCourse(String id) throws DaoException {
        Object[] args = new String[] {
            id
        };
        super.update("UPDATE cel_content_course SET registered=1 WHERE id=?", args);
        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)",course);
    }*/
    public void delete(String id) throws DaoException {
        Object[] args = new String[] {
            id
        };

      
        super.update("DELETE cel_content_questions.* FROM cel_content_questions, cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_questions.assessment_id = cel_content_assessment.id AND cel_content_assessment.module_id = cel_content_module.id AND cel_content_module.courseid = cel_content_course.id AND cel_content_course.categoryid=?", args);
        super.update("DELETE cel_content_assessment.* FROM cel_content_assessment, cel_content_module, cel_content_course WHERE cel_content_assessment.module_id = cel_content_module.id AND cel_content_module.courseid = cel_content_course.id AND cel_content_course.categoryid=?", args);
        super.update("DELETE cel_content_lesson.* FROM cel_content_lesson, cel_content_module, cel_content_course WHERE cel_content_lesson.folderid= cel_content_module.id AND cel_content_module.courseid = cel_content_course.id AND cel_content_course.categoryid=? ",args);
        super.update("DELETE cel_content_module.* FROM cel_content_module, cel_content_course WHERE cel_content_module.courseid=cel_content_course.id AND cel_content_course.categoryid=?", args);
        super.update("DELETE FROM cel_content_course WHERE categoryid=?", args);
        super.update("DELETE FROM cel_content_course_category WHERE id=?", args);



        //super.update("INSERT INTO cel_content_status (id,deleted,deletedDate,deletedByUser,deletedByUserId) VALUES (#id#,'Y',#createdDate#,#createdByUser#,#createdByUserId#)",course);
    }

    public Category load(String id) throws DaoException, DataObjectNotFoundException {

        Object[] args = { id };
        Collection results = super.select("SELECT id,category,createdByUser,createdByUserId,createdDate FROM cel_content_course_category WHERE id=?", Category.class, args, 0, 1);

        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Category)results.iterator().next();
        }
    }
  /*  public Course loadSubjects(String category) throws DaoException, DataObjectNotFoundException {
        Collection results = super.select("SELECT distinct(category) FROM cel_content_course", Course.class, "", 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Course)results.iterator().next();
        }
    }*/
    /*public Course loadList(String category) throws DaoException, DataObjectNotFoundException {

        Object[] args = { category };
        Collection results = super.select("SELECT id,name,author,synopsis,instructor FROM cel_content_course WHERE category=?", Course.class, args, 0, 1);
        if (results.size() == 0) {
            throw new DataObjectNotFoundException();
        } else {
            return (Course)results.iterator().next();
        }
    } */

    public Collection query(String category, String userId, String sort, boolean desc, int start, int rows) throws DaoException {

        String condition = (category != null) ? "%" + category + "%" : "%";
        String orderBy = (sort != null) ? sort : "category";
      //  if (desc)
        //    orderBy += " category";

  if (desc) orderBy += " DESC";


        //System.out.println("condition "+condition+" "+userId);





           Object[] args = { condition };
           return super.select("SELECT id,category,createdByUser,createdByUserId,createdDate FROM cel_content_course_category WHERE category LIKE ? ORDER BY " + orderBy, Category.class, args, start, rows);



    }

    public int countList(String category) throws DaoException {

        String condition = (category != null) ? "%" + category + "%" : "%";
        Object[] args = { condition };

        Collection list;
         if(category !=null && !("".equals(category ))){
          list = super.select("SELECT count(*) AS total FROM cel_content_course_category WHERE cel_content_course_category.category LIKE ?", HashMap.class, args, 0, 1);
         }
        else
          list = super.select("SELECT count(*) AS total FROM cel_content_course_category", HashMap.class, "", 0, 1);




        //   Collection list = super.select("SELECT count(*) FROM cel_content_course_category WHERE category= ?", HashMap.class, args, 0, 1);

        HashMap map = (HashMap)list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }


    public int count(String name) throws DaoException {

        String condition = (name != null) ? "%" + name + "%" : "%";
        Object[] args = { condition };

        Collection list = super.select("SELECT COUNT(*) AS total FROM cel_content_course_category WHERE category LIKE?", HashMap.class, args, 0, 1);
        HashMap map = (HashMap)list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public Collection getCategories() throws DaoException{
        String sql = "SELECT distinct id,category,createdByUser,createdByUserId,createdDate FROM cel_content_course_category";
        return super.select(sql, Category.class, null, 0, -1);
    }

}
