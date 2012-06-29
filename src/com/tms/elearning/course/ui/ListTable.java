package com.tms.elearning.course.ui;

import com.tms.elearning.course.model.CourseModule;

import kacang.Application;

import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;

import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Collection;


public class ListTable extends Table {


    private String categoryid;
 


    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;

    }

    public void onRequest(Event event) {
        initTable();
    }

    public void initTable() {
        setModel(new ListTableModel());
    }

    public class ListTableModel extends TableModel {
        public ListTableModel() {
            TableColumn naCol = new TableColumn("name", "Course");

            naCol.setUrlParam("id");

            addColumn(naCol);

            addColumn(new TableColumn("author", "Author"));
            addColumn(new TableColumn("instructor", "Instructor"));

            addAction(new TableAction("register", "Register"));
        }

        public Collection getTableRows() {
            Application application = Application.getInstance();
            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            return module.findList(categoryid, getSort(), isDesc(), getStart(),
                getRows());
        }

        public int getTotalRowCount() {
            Application application = Application.getInstance();
            CourseModule module = (CourseModule) application.getModule(CourseModule.class);

            return module.countList(categoryid);
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("register".equals(action)) {
                Application application = Application.getInstance();
                CourseModule module = (CourseModule) application.getModule(CourseModule.class);


                if(selectedKeys.length <=0) return new Forward("notSelected");



                for (int i = 0; i < selectedKeys.length; i++) {
                    module.registerCourse(selectedKeys[i]);


                }




                return new Forward("register");
            }

            return null;
        }

        public String getTableRowKey() {
            return "id";
        }









    }



}
