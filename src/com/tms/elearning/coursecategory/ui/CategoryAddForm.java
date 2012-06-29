package com.tms.elearning.coursecategory.ui;

import kacang.stdui.Form;
import kacang.stdui.TextField;
import kacang.stdui.Button;
import kacang.stdui.validator.ValidatorMessage;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.Application;
import kacang.services.security.User;
import kacang.util.UuidGenerator;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import com.tms.elearning.course.model.CourseModule;
import com.tms.elearning.course.model.Course;
//<block>import com.tms.elearning.core.model.UserModule;
import com.tms.elearning.coursecategory.model.Category;
import com.tms.elearning.coursecategory.model.CategoryModule;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: Feb 25, 2005
 * Time: 2:46:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryAddForm extends Form  implements Serializable{

    protected TextField category;
    protected Button submit;
    protected Button cancel;

    private String id;
    protected ValidatorMessage vmsg_categoryExists;

    //<block>private UserModule usermodule = (UserModule)Application.getInstance().getModule(UserModule.class);
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultTemplate() {
        return "elearning/categoryForm";
    }

     public void init() {

         category = new TextField("category");
         ValidatorNotEmpty v_category = new ValidatorNotEmpty("v_category", "");
         category.addChild(v_category);
         category.addChild(vmsg_categoryExists = new ValidatorMessage("vmsg_categoryExists"));
         category.setSize("30");
         category.setMaxlength("50");

         submit = new Button("submit","Submit");
         cancel = new Button("cancel","Cancel");
         addChild(category);
         addChild(submit);
         addChild(cancel);
    }

    public Forward onValidate(Event evt) {

           CategoryModule categorymodule = (CategoryModule)Application.getInstance().getModule(CategoryModule.class);
           Category cat = new Category();
           UuidGenerator uuid = UuidGenerator.getInstance();
           String sid = uuid.getUuid();
           WidgetManager manager = getWidgetManager();
           User user = manager.getUser();

           SimpleDateFormat formatter= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
           Date today=new Date(System.currentTimeMillis());
           String createdDate = formatter.format(today);

           cat.setId(sid);
           cat.setCategory((String)category.getValue());
           cat.setCreatedByUser(user.getUsername());
           cat.setCreatedByUserId(user.getId());
           cat.setCreatedDate(createdDate);
           categorymodule.addCategory(cat);
        return new Forward("added");
    }

}
