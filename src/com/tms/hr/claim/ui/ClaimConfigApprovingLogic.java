/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import kacang.Application;
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

public class ClaimConfigApprovingLogic extends Form 
{

	protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigApprovingLogic";
	protected SelectBox sb_NumberOfApproverRequired;
	protected Button bn_Submit;

	
	public void init() 
	{
      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
                  application.getModule(ClaimConfigModule.class);

		setColumns(2);
        setMethod("POST");

		addChild(new Label("property1", "<b>"+Application.getInstance().getMessage("claims.label.defaultApproveLogic","Default Approving Logic")+"</b>"));
      sb_NumberOfApproverRequired = new SelectBox("sb_NumberOfApproverRequired");
		sb_NumberOfApproverRequired.addOption("1",application.getMessage("claims.label.approver.one"));
		sb_NumberOfApproverRequired.addOption("2",application.getMessage("claims.label.allapprovers"));
		/// get all users, populate in select box
		Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
		Vector vecApprovingLogics = new Vector();
		if(col!=null) { vecApprovingLogics = new Vector(col); }
		else { vecApprovingLogics = new Vector(); }

		String selectedId[] = new String[vecApprovingLogics.size()];
      for(int cnt=0;cnt<vecApprovingLogics.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecApprovingLogics.get(cnt);
         selectedId[cnt]=theObj.getProperty1();
      }
		/// then select existing ApprovingLogics
		sb_NumberOfApproverRequired.setSelectedOptions(selectedId);
      addChild(sb_NumberOfApproverRequired);
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
		Vector vecApprovingLogics ;
		if(col!=null)
		{ vecApprovingLogics =  new Vector( col);}
		else
		{ vecApprovingLogics = new Vector();}
		/// need to remove existing rows given the namespace
		for(int cnt=0;cnt<vecApprovingLogics.size();cnt++)
		{
			ClaimConfig theObj = (ClaimConfig) vecApprovingLogics.get(cnt);
			module.deleteObject(theObj.getId());
		}

		Collection colKey = sb_NumberOfApproverRequired.getSelectedOptions().keySet();		
		String[] userIds = (String[]) colKey.toArray(new String[0]);

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

//		removeChildren();
//		init();

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
      Vector vecApprovingLogics = new Vector();
      if(col!=null) { vecApprovingLogics = new Vector(col); }
      else { vecApprovingLogics = new Vector(); }

      String selectedId[] = new String[vecApprovingLogics.size()];
      for(int cnt=0;cnt<vecApprovingLogics.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecApprovingLogics.get(cnt);
         selectedId[cnt]=theObj.getProperty1();
      }
      /// then select existing ApprovingLogics
      sb_NumberOfApproverRequired.setSelectedOptions(selectedId);

	}

	public static int getNumberOfApproverRequired(Application app)
	{
		
      ClaimConfigModule module = (ClaimConfigModule)
                  app.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecApprovingLogics = new Vector();
      if(col!=null) { vecApprovingLogics = new Vector(col); }
      else { vecApprovingLogics = new Vector(); }

      for(int cnt=0;cnt<vecApprovingLogics.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecApprovingLogics.get(cnt);
			Integer nApp = new Integer(theObj.getProperty1());
			return nApp.intValue();
      }
		return 0;
	}

    public SelectBox getSb_NumberOfApproverRequired() {
        return sb_NumberOfApproverRequired;
    }

    public void setSb_NumberOfApproverRequired(SelectBox sb_NumberOfApproverRequired) {
        this.sb_NumberOfApproverRequired = sb_NumberOfApproverRequired;
    }

    public Button getBn_Submit() {
        return bn_Submit;
    }

    public void setBn_Submit(Button bn_Submit) {
        this.bn_Submit = bn_Submit;
    }

    public String getDefaultTemplate() {
        return "claims/config_approval_logic";
    }
}
