package com.tms.elearning.course.ui;

import com.tms.elearning.core.model.StudentSelectBox;
import com.tms.elearning.course.model.Course;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.course.model.Student;

import kacang.Application;

import kacang.model.DaoQuery;
import kacang.model.DataObjectNotFoundException;

import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.ui.WidgetManager;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CourseViewDetail extends Form {
    private String studentId; // save instant selected by instructor and pass to studentDetail.jsp
    private String[] studentsId;
    private String[] studentsName;
    private String studentName;
    private String courseNameStr;
    private String courseId; //id of the course
    private Label courseName;
    private Label synopsis;
    private Label category;
    private Label author;
    private Label courseNameField;
    private Label synopsisField;
    private Label categoryField;
    private Label authorField;
    private Label totalStudent;
    private Label totalStudentField;
    private Label RegisteredStudentName;
    private SelectBox studentSelectBox;
    protected StudentSelectBox selectStudent;
    protected Button nextButton;

    public String[] getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(String[] studentsId) {
        this.studentsId = studentsId;
    }

    public String[] getStudentsName() {
        return studentsName;
    }

    public void setStudentsName(String[] studentsName) {
        this.studentsName = studentsName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseNameStr() {
        return courseNameStr;
    }

    public void setCourseNameStr(String courseNameStr) {
        this.courseNameStr = courseNameStr;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void init() {
        super.init();
    }

    public void onRequest(Event evt) {
        super.onRequest(evt);

        Application application = Application.getInstance();

        //get course info from storage
        CourseModule module = (CourseModule) Application.getInstance()
                                                        .getModule(CourseModule.class);
        Course cour = null;

        try {
            if (module.loadCourse(getCourseId()) != null) {
                cour = module.loadCourse(getCourseId());
            }
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        }

        //get count total students in each course
        int countTotalStudent = 0;

        try {
            countTotalStudent = module.countStudentsEachCourse(getCourseId());
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        }

        setColumns(2);

        if (cour != null) {
            courseName = new Label("courseName",
                    "<b>" +
                    application.getMessage("eLearning.viewDetail.courseName") +
                    "</b>");
            courseNameField = new Label("courseNameField", cour.getName());
            setCourseNameStr(cour.getName());
            synopsis = new Label("synopsis",
                    "<b>" +
                    application.getMessage("eLearning.viewDetail.synopsis") +
                    "</b>");
            synopsisField = new Label("synopsisField", cour.getSynopsis());
            category = new Label("category",
                    "<b>" +
                    application.getMessage("eLearning.viewDetail.category") +
                    "</b>");
            categoryField = new Label("categoryField", cour.getCategory());
            author = new Label("author",
                    "<b>" +
                    application.getMessage("eLearning.viewDetail.author") +
                    "</b>");
            authorField = new Label("authorField", cour.getAuthor());
        }

        RegisteredStudentName = new Label("registeredStudentName",
                "<b>" +
                application.getMessage(
                    "eLearning.viewDetail.registeredstudentname") + "</b>");
        studentSelectBox = new SelectBox("studentSelectBox");
        nextButton = new Button("nextButton",
                application.getMessage("eLearning.studentDeatil.next"));

        selectStudent = new StudentSelectBox("selectStudent");

        addChild(courseName);
        addChild(courseNameField);
        addChild(synopsis);
        addChild(synopsisField);
        addChild(category);
        addChild(categoryField);
        addChild(author);
        addChild(authorField);

        totalStudent = new Label("totalStudent",
                "<b>" +
                application.getMessage("eLearning.viewDetail.totalStudent") +
                "</b>");
        addChild(totalStudent);

        totalStudentField = new Label("totalStudentField",
                Integer.toString(countTotalStudent));
        addChild(totalStudentField);

        //display total students in selectbox
        try {
            Collection studentlist = module.displayStudentsEachCourse(getCourseId());

            Map hash = new SequencedHashMap();
            hash.put("", "");

            for (Iterator iterator = studentlist.iterator();
                    iterator.hasNext();) {
                Student tempStudent = (Student) iterator.next();

                //convert id into user name
                SecurityService ss = (SecurityService) Application.getInstance()
                                                                  .getService(SecurityService.class);
                DaoQuery query = new DaoQuery();

                query.addProperty(new OperatorEquals("id",
                        tempStudent.getStudent(), DaoOperator.OPERATOR_AND));

                Collection userCollection = null;

                try {
                    userCollection = ss.getUsers(query, 0, -1, "username", false);
                } catch (SecurityException e) {
                    e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
                }

                User user = null;

                for (Iterator icount = userCollection.iterator();
                        icount.hasNext();) {
                    user = (User) icount.next();
                    hash.put(user.getId(), user.getUsername());
                }
            }

            // studentSelectBox.setOptionMap(hash);
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        }

        //load students in each course
        Collection loadStudents = module.loadStudents(getCourseId());

        Map studentMap = new SequencedHashMap();

        try{
        if(loadStudents.size()>0 && loadStudents !=null)
        {
            for (Iterator icount = loadStudents.iterator(); icount.hasNext();) {
            User user = (User) icount.next();

            studentMap.put(user.getId(), user.getUsername());
            }
        }
        }
        catch(NullPointerException e){
            //ignore sometime no students are there
        }
        addChild(RegisteredStudentName);

        addChild(selectStudent);
        nextButton.setColspan(2);
        nextButton.setAlign(Form.ALIGN_RIGHT);
        addChild(nextButton);

        selectStudent.init();
        selectStudent.setCourse_id(getCourseId());
    }

    public Forward onSubmit(Event evt) {
        super.onSubmit(evt);

        WidgetManager wm = evt.getWidgetManager();
        String buttonClicked = findButtonClicked(evt);
        Widget button = wm.getWidget(buttonClicked);

        if (nextButton.equals(button)) {
            setStudentsId(selectStudent.getIds());

            if (getCourseId() != null) {
                return new Forward("studentsDetail");
            }
        }

        if (getCourseId() != null) {
            setStudentId((String) ((List) studentSelectBox.getValue()).get(0));

            return new Forward("studentDetail");
        } else {
            return null;
        }
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}


