/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import com.tms.hr.employee.ui.EmployeeSelectBox;
import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;
import java.util.Vector;

public class ClaimConfigAssistant extends Form 
{

	public static String namespace = "com.tms.hr.claim.ui.ClaimConfigAssistant";

	/// note:
	/// property one stores the owner's id
	/// property two stores the originator's ids

	protected SelectBox sb_Assistant;
	protected Button bn_Submit;

	
	public String getDefaultTemplate() {
		return "claims/user_options";
	}
	
	public void init() 
	{
      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

		setColumns(2);
        setMethod("POST");

		addChild(new Label("property1", "<b>"+ Application.getInstance().getMessage("claims.label.assistant", "Assistant")+"</b>"));
        sb_Assistant = new EmployeeSelectBox("sb_Assistant", EmployeeSelectBox.SHOW_ALL);
		sb_Assistant.setMultiple(true);
		sb_Assistant.setRows(4);

		setSelectedOptions();

        addChild(sb_Assistant);
        addChild(new Label("p2",""));
		bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.category.submit", "Submit"));
		addChild(bn_Submit);
    }

	public Forward onValidate(Event evt) 
	{
		Application application = Application.getInstance();
		ClaimConfigModule module = (ClaimConfigModule)
						application.getModule(ClaimConfigModule.class);

		/// first find all existing object with this namespace
		Collection col = 	module.findObjects(new String[]{" namespace='"+namespace+"' "},
							(String)null, false,0,-1);	
		Vector vecAssistants ;
		if(col!=null)
		{ vecAssistants =  new Vector( col);}
		else
		{ vecAssistants = new Vector();}
		/// need to remove existing rows given the namespace
		for(int cnt=0;cnt<vecAssistants.size();cnt++)
		{
			ClaimConfig theObj = (ClaimConfig) vecAssistants.get(cnt);
			module.deleteObject(theObj.getId());
		}

		Collection colKey = sb_Assistant.getSelectedOptions().keySet();		
		String[] userIds = (String[]) colKey.toArray(new String[0]);

		Log.getLog(this.getClass()).debug(" =========================================");
		Log.getLog(this.getClass()).debug(" Size of selected IDs:" + userIds.length);
		for(int cnt2=0;cnt2<userIds.length;cnt2++)
		{
			Log.getLog(this.getClass()).debug(" Number : " + cnt2);
			ClaimConfig obj = new ClaimConfig();
			UuidGenerator uuid = UuidGenerator.getInstance();
			obj.setId(uuid.getUuid());
			obj.setNamespace(namespace);
			obj.setProperty1(getWidgetManager().getUser().getId());
			obj.setProperty2(userIds[cnt2]);
			module.addObject(obj);
		}

//		removeChildren();
//		init();

		//return super.onValidate(evt);
        return new Forward("success");
    }

	public void onRequest(Event evt)
	{
		setSelectedOptions();
	}



	public void setSelectedOptions()
	{

      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]
								{
									" namespace='"+namespace+"' ",
									" property1='"+getWidgetManager().getUser().getId()+"' "
								},
                     (String) "id", false,0,-1);
      Vector vecAssistants = new Vector();
      if(col!=null) { vecAssistants = new Vector(col); }
      else { vecAssistants = new Vector(); }

      String selectedId[] = new String[vecAssistants.size()];
      for(int cnt=0;cnt<vecAssistants.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssistants.get(cnt);
         selectedId[cnt]=theObj.getProperty2();
      }
      /// then select existing Assistants
      sb_Assistant.setSelectedOptions(selectedId);

	}

	public static int countPossibleOptions(String originator, String namespace)
	{
      SecurityService service = (SecurityService) 
						Application.getInstance().getService(SecurityService.class);

      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]
                        {
                           " namespace='"+namespace+"' ",
                           " property2='"+originator+"' "
                        },
                     (String) "id", false,0,-1);
      Vector vecAssistants = new Vector();
      if(col!=null) { vecAssistants = new Vector(col); }
      else { vecAssistants = new Vector(); }
		return vecAssistants.size();
	}

	public static void addPossibleOptions(SelectBox selectBox, 
														String originator,
														String namespace)
	{
      SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);

      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]
								{
									" namespace='"+namespace+"' ",
									" property2='"+originator+"' "
								},
                     (String) "id", false,0,-1);
      Vector vecAssistants = new Vector();
      if(col!=null) { vecAssistants = new Vector(col); }
      else { vecAssistants = new Vector(); }

      String selectedId[] = new String[vecAssistants.size()];
      for(int cnt=0;cnt<vecAssistants.size();cnt++)
      {
			try
			{
         	ClaimConfig theObj = (ClaimConfig) vecAssistants.get(cnt);
         	selectedId[cnt]=theObj.getProperty1();
				User theUser = service.getUser(theObj.getProperty1());
      		selectBox.addOption(theObj.getProperty1(),theUser.getName());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
      }

	}



}
