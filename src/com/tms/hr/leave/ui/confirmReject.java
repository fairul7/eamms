package com.tms.hr.leave.ui;

import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.TextField;
import kacang.services.security.User;
import kacang.Application;
import kacang.ui.Event;
import kacang.model.DataObjectNotFoundException;

import java.io.Serializable;

import com.tms.hr.leave.model.LeaveModule;
import com.tms.hr.leave.model.LeaveException;

public class confirmReject extends Form implements Serializable{



    private String userId;
    private String id;


    public void init(){







    }




    public void onRequest(Event evt){


        Application application = Application.getInstance();
           LeaveModule lm = (LeaveModule)application.getModule(LeaveModule.class);

           if(userId !=null   && id !=null)
           {



               Label reasonReject = new Label("reasonReject","Reason Reject");
               addChild(reasonReject);

               TextField reasonRejectText = new TextField("reasonRejectText");

               addChild(reasonRejectText);




            /*   try {
                   lm.rejectLeave(getId(), "", getUser());
               } catch (DataObjectNotFoundException e) {
                   e.printStackTrace();
               } catch (LeaveException e) {
                   e.printStackTrace();
               }*/
           }



    }









    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
