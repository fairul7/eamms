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
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

public class ClaimConfigMileage extends Form 
{

	protected static String namespace = "com.tms.hr.claim.ui.ClaimConfigMileage";
	protected TextField tf_DollarPerKM;
	protected Button bn_Submit;

	
	public void init() 
	{
      Application application = Application.getInstance();
      ClaimConfigModule module = (ClaimConfigModule)
        application.getModule(ClaimConfigModule.class);

		setColumns(2);
        setMethod("POST");


		addChild(new Label("property1", "<b>"+Application.getInstance().getMessage("claims.label.mileageOverKm","Mileage $/km")+"</b>"));
      tf_DollarPerKM = new TextField("dpkm");
		/// get all users, populate in select box
		Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
		Vector vecMileages = new Vector();
		if(col!=null) { vecMileages = new Vector(col); }
		else { vecMileages = new Vector(); }

      for(int cnt=0;cnt<vecMileages.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecMileages.get(cnt);
         tf_DollarPerKM.setValue(theObj.getProperty1());
      }
		/// then select existing Mileages
		ValidatorIsNumeric vnu = new ValidatorIsNumeric("number",Application.getInstance().getMessage("claims.message.mustBeNumber","Must be a number!"));
		tf_DollarPerKM.addChild(vnu);
		tf_DollarPerKM.setSize("4");
      addChild(tf_DollarPerKM);
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
		Vector vecMileages ;
		if(col!=null)
		{ vecMileages =  new Vector( col);}
		else
		{ vecMileages = new Vector();}
		/// need to remove existing rows given the namespace
		for(int cnt=0;cnt<vecMileages.size();cnt++)
		{
			ClaimConfig theObj = (ClaimConfig) vecMileages.get(cnt);
			module.deleteObject(theObj.getId());
		}

		ClaimConfig obj = new ClaimConfig();
		UuidGenerator uuid = UuidGenerator.getInstance();
		obj.setId(uuid.getUuid());
		obj.setNamespace(namespace);
		obj.setProperty1((String) tf_DollarPerKM.getValue());
		module.addObject(obj);

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
      Vector vecMileages = new Vector();
      if(col!=null) { vecMileages = new Vector(col); }
      else { vecMileages = new Vector(); }

      String selectedId[] = new String[vecMileages.size()];
      for(int cnt=0;cnt<vecMileages.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecMileages.get(cnt);
         tf_DollarPerKM.setValue(theObj.getProperty1());
      }
      /// then select existing Mileages

	}

	public static BigDecimal getDollarPerKM(Application app)
	{
		
      ClaimConfigModule module = (ClaimConfigModule)
                  app.getModule(ClaimConfigModule.class);

      /// get all users, populate in select box
      Collection col = module.findObjects(new String[]{" namespace='"+namespace+"' "},
                     (String) "id", false,0,-1);
      Vector vecMileages = new Vector();
      if(col!=null) { vecMileages = new Vector(col); }
      else { vecMileages = new Vector(); }

      for(int cnt=0;cnt<vecMileages.size();cnt++)
      {
         ClaimConfig theObj = (ClaimConfig) vecMileages.get(cnt);
			BigDecimal bdDollarPerKM = new BigDecimal(theObj.getProperty1());
			return bdDollarPerKM;
      }
		return new BigDecimal("0.00");
	}

    public TextField getTf_DollarPerKM() {
        return tf_DollarPerKM;
    }

    public void setTf_DollarPerKM(TextField tf_DollarPerKM) {
        this.tf_DollarPerKM = tf_DollarPerKM;
    }

    public Button getBn_Submit() {
        return bn_Submit;
    }

    public void setBn_Submit(Button bn_Submit) {
        this.bn_Submit = bn_Submit;
    }

    public String getDefaultTemplate() {
        return "claims/config_mileage";
    }
}
