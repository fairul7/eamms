/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import kacang.Application;
import kacang.services.security.ui.UsersSelectBox;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import java.util.Collection;
import java.util.Vector;

public class ClaimConfigAssessor extends Form 
{

	protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigAssessor";
    protected UsersSelectBox assessors;
	protected Button bn_Submit;

	
	public void init() 
	{
      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

		setColumns(2);
        setMethod("post");
		addChild(new Label("property1", "<b>"+Application.getInstance().getMessage("claims.label.defaultassessor","Default Assessor")+"</b>"));
        assessors = new UsersSelectBox("users");
        addChild(assessors);
        assessors.init();
		setAssessors();

		/// get all users, populate in select box
		Collection col = 
					module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
		Vector vecAssessors = new Vector();
		if(col!=null) { vecAssessors = new Vector(col); }
		else { vecAssessors = new Vector(); }

		String selectedId[] = new String[vecAssessors.size()];
      for(int cnt=0;cnt<vecAssessors.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
         selectedId[cnt]=theObj.getProperty1();
      }
		/// then select existing Assessors
        addChild(new Label("20",""));
		bn_Submit = new Button("submit", Application.getInstance().getMessage("claims.category.submit","Submit"));
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
		Vector vecAssessors ;
		if(col!=null)
		{ vecAssessors =  new Vector( col);}
		else
		{ vecAssessors = new Vector();}
		/// need to remove existing rows given the namespace
		for(int cnt=0;cnt<vecAssessors.size();cnt++)
		{
			ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
			module.deleteObject(theObj.getId());
		}

        String[] userIds = assessors.getIds();

		Log.getLog(this.getClass()).debug(" =========================================");
		Log.getLog(this.getClass()).debug(" Size of selected IDs:" + userIds.length);
		for(int cnt2=0;cnt2<userIds.length;cnt2++)
		{
			Log.getLog(this.getClass()).debug(" Number :" + cnt2);
			ClaimConfig obj = new ClaimConfig();
			UuidGenerator uuid = UuidGenerator.getInstance();
			obj.setId(uuid.getUuid());
			obj.setNamespace(namespace);
			obj.setProperty1(userIds[cnt2]);
			module.addObject(obj);
		}

		setAssessors();

		return super.onValidate(evt);
    }

	public void onRequest(Event evt)
	{
      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecAssessors = new Vector();
      if(col!=null) { vecAssessors = new Vector(col); }
      else { vecAssessors = new Vector(); }

      String selectedId[] = new String[vecAssessors.size()];
      for(int cnt=0;cnt<vecAssessors.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
         selectedId[cnt]=theObj.getProperty1();
      }
      /// then select existing Assessors
		setAssessors();
	}

	public void setAssessors()
	{
      ClaimConfigModule module = (ClaimConfigModule)
                  Application.getInstance().getModule(ClaimConfigModule.class);

      Collection col = module.findObjects(
							new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecAssessors = new Vector();
      if(col!=null) { vecAssessors = new Vector(col); }
      else { vecAssessors = new Vector(); }
      String rightIds[] = new String[vecAssessors.size()];
      for(int cnt=0;cnt<vecAssessors.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
         rightIds[cnt]=theObj.getProperty1();
      }
      assessors.setIds(rightIds);
	}

	public static boolean fnIn(String key, String[] array)
	{
        for(int cnt=0;cnt<array.length;cnt++)
		{
			if(key.equals(array[cnt]))
			{
				return true;
			}
		}
		return false;
	}


	public static boolean isAssessor(String userId, Application app )
	{


      ClaimConfigModule module = (ClaimConfigModule)
                  app.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecAssessors = new Vector();
      if(col!=null) { vecAssessors = new Vector(col); }
      else { vecAssessors = new Vector(); }

      for(int cnt=0;cnt<vecAssessors.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecAssessors.get(cnt);
			if(theObj.getProperty1().equals(userId))
			{ return true;}
      }

		return false;
	}

    public UsersSelectBox getAssessors() {
        return assessors;
    }

    public void setAssessors(UsersSelectBox assessors) {
        this.assessors = assessors;
    }

    public Button getBn_Submit() {
        return bn_Submit;
    }

    public void setBn_Submit(Button bn_Submit) {
        this.bn_Submit = bn_Submit;
    }

    public String getDefaultTemplate() {
        return "claims/config_assessor";
    }

    
}
