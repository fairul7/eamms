package com.tms.elearning.core.ui;

import com.tms.elearning.course.model.Course;
import com.tms.elearning.core.model.*;
import com.tms.elearning.folder.model.Folder;
import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.testware.model.Assessment;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.User;
import kacang.stdui.Tree;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import java.util.*;

public class LearningTree extends Tree {

    private String rootId;
    private String courseId;

    private String path="Course Home";
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private boolean includeLearnings = false;
    private String[] learningObjectClasses = new String[] {
        Course.class.getName(),
        Folder.class.getName(),
        Lesson.class.getName(),
        Assessment.class.getName(),
    };

    public LearningTree() {
    }

    public LearningTree(String name) {
        super(name);
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public boolean isIncludeLearnings() {
        return includeLearnings;
    }

    public void setIncludeLearnings(boolean includeLearnings) {
        this.includeLearnings = includeLearnings;
    }

   /* public String[] getLearningObjectClasses() {
        return LearningObjectClasses;
    }

    public void setLearningObjectClasses(String[] LearningObjectClasses) {
        this.LearningObjectClasses = LearningObjectClasses;
    }*/

    public void init() {
       // setSelectedId(LearningManager.LEARNING_TREE_ROOT_ID);
/*
        LearningObject root = (LearningObject)getModel();
        if (root != null) {
            // check root permission
            User user = getWidgetManager().getUser();
            Application app = Application.getInstance();
            LearningManager cm = (LearningManager)app.getModule(LearningManager.class);
            if (cm.hasPermission(root, user.getId(), LearningManager.USE_CASE_PREVIEW)) {
                // select root
                setSelectedId(LearningManager.Learning_TREE_ROOT_ID);
            }
            else {
                // select first child
                Collection children = root.getChildren();
                if (children != null && children.size() > 0) {
                    LearningObject child = (LearningObject)children.iterator().next();
                    LearningHelper.setId(evt, getSelectedId());
                    selectLearningId(getSelectedId());

                }

            }
        }
*/
    }

    public void onRequest(Event evt) {
       // LearningObject co = LearningHelper.getLearningObject(evt, null);
       /* if (co != null) {
            selectLearningId(co.getId());
        }*/
        courseId = evt.getRequest().getParameter("cid");
    }

   /* protected void selectLearningId(String id) {
        try {
            // check to see if in tree
            String className = LearningUtil.getClassNameFromKey(id);
            Collection classList = Arrays.asList(learningObjectClasses);
            if (classList.contains(className)) {
                setSelectedId(id);
            }
            else {
                // select parent instead
                Application app = Application.getInstance();
                LearningManager LearningManager = (LearningManager)app.getModule(LearningManager.class);
                LearningObject tmp = LearningManager.viewPath(id, getWidgetManager().getUser());
                while(tmp.getParent() != null) {
                    tmp = tmp.getParent();
                    if (classList.contains(tmp.getClassName())) {
                        setSelectedId(tmp.getId());
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
    }*/

   public Forward actionPerformed(Event evt) {
        Forward forward = super.actionPerformed(evt);
        /*if (forward == null) {
            LearningHelper.setId(evt, getSelectedId());
            selectLearningId(getSelectedId());
            forward = new Forward("selection");
        } */
       System.out.println("get selectid "+getSelectedId());
        return forward;
    }


    public Object getModel(){


        Application app = Application.getInstance();
        LearningModule module = (LearningModule)app.getModule(LearningModule.class);
        LearningObject root = new LearningObject();
   //     root.setId("Course_Root");
   //     root.setName("Course Home");

        Collection results = module.getCourses(courseId);
        LearningObject courseRoot=null, moduleRoot=null, lessonRoot=null, examRoot=null;
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
             Course course = (Course)iterator.next();
             courseRoot = new LearningObject();
             String cid = course.getId();
             courseRoot.setId(cid);
             courseRoot.setName(course.getName());
             Collection modules = module.getModules(cid);

                 for (Iterator it = modules.iterator(); it.hasNext();) {
                        Folder folder = (Folder)it.next();
                        moduleRoot = new LearningObject();
                        String mid = folder.getId();
                        moduleRoot.setId(mid);
                        moduleRoot.setName(folder.getName());
                        Collection lessons = module.getLessons(mid);

                            for (Iterator it1 = lessons.iterator(); it1.hasNext();) {
                                  Lesson lesson = (Lesson)it1.next();
                                  lessonRoot = new LearningObject();
                                  String lid = lesson.getId();
                                  lessonRoot.setId(lid);
                                  lessonRoot.setName(lesson.getName());
                                  Collection exams = module.getExams(lesson.getId());
                                  for (Iterator it2 = exams.iterator(); it2.hasNext();) {

                                      Assessment exam = (Assessment)it2.next();
                                      examRoot = new LearningObject();
                                      examRoot.setId(exam.getId());
                                      examRoot.setName(exam.getName());
                                      lessonRoot.addChild(examRoot);
                                  }
                             moduleRoot.addChild(lessonRoot);
                            }
                     courseRoot.addChild(moduleRoot);
                 }
           // root.addChild(courseRoot);
        }

        return courseRoot;
    }
}
