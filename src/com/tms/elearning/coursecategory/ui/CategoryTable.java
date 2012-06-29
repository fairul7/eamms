package com.tms.elearning.coursecategory.ui;

import com.tms.elearning.coursecategory.model.Category;
import com.tms.elearning.coursecategory.model.CategoryModule;
import com.tms.elearning.course.model.CourseModule;

import kacang.Application;

import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Feb 25, 2005
 * Time: 2:47:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTable extends Table implements Serializable{


    public void init() {
        setModel(new CategoryTable.CategoryTableModel());
    }

    public class CategoryTableModel extends TableModel {
        public CategoryTableModel() {
            SecurityService service = (SecurityService) Application.getInstance()
                                                                   .getService(SecurityService.class);
            User current = getWidgetManager().getUser();
            Application application = Application.getInstance();

            //  TableColumn naCol = new TableColumn("name", "Name");
            // addColumn(naCol);

            /*try
            {
                if (service.hasPermission(current.getId(), "com.tms.elearning.coursecategory.Edit", null, null))
                    naCol.setUrlParam("id");
            }
            catch (Exception e)
            {
                Log.getLog(Course.class).error(e.getMessage(), e);
            } */
            TableColumn naCol = new TableColumn("category",
                    application.getMessage("eLearning.course.label.category"));



            try { //if course can be edit , category also can be edit 

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.coursecategory.Edit", null, null)) {
                    naCol.setUrl("editCategory.jsp");
                }

                naCol.setUrlParam("id");
            } catch (Exception e) {
                Log.getLog(Category.class).error(e.getMessage(), e);
            }


              /* addColumn(new TableColumn("category", "Category"));*/
            addColumn(naCol);
            addFilter(new TableFilter("category"));


            try {
                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.coursecategory.Add", null, null)) {
                    addAction(new TableAction("add", "Add"));
                }

                if (service.hasPermission(current.getId(),
                            "com.tms.elearning.coursecategory.Delete", null,
                            null)) {
                    addAction(new TableAction("delete", "Delete", Application.getInstance().getMessage("eLearning.category.label.confirm")));
                }
            } catch (Exception e) {
                Log.getLog(Category.class).error("Error loading category: " +
                    e.toString(), e);
            }












        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("category");






            WidgetManager manager = getWidgetManager();
            User user = manager.getUser();
            String userId = user.getUsername();
            Application application = Application.getInstance();
            CategoryModule module = (CategoryModule) application.getModule(CategoryModule.class);

            return module.findCategory( name, userId, getSort(), isDesc(),
                getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("category");








            Application application = Application.getInstance();
            CategoryModule module = (CategoryModule) application.getModule(CategoryModule.class);

            return module.countCategory(name);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("add".equals(action)) {
                return new Forward("add");
            } else if ("delete".equals(action)) {
                Application application = Application.getInstance();
                CategoryModule module = (CategoryModule) application.getModule(CategoryModule.class);

                if(selectedKeys.length <=0) return new Forward("notSelected");

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteCourse(selectedKeys[i]);
                }


            return new Forward("deleted");
            }

            return null;
        }
    }
}
