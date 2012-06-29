/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-07-08
 * Copyright of The Media Shoppe Berhad
 */


package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfig;
import com.tms.hr.claim.model.ClaimConfigModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

public class ClaimConfigForm extends Form 
{

    protected TextField tf_Namespace;
    protected SelectBox sb_Category;
    protected SelectBox sb_Property1;
    protected SelectBox sb_Property2;
    protected SelectBox sb_Property3;
    protected SelectBox sb_Property4;
    protected SelectBox sb_Property5;
	protected Button bn_Submit;

	public void init() 
	{
		setColumns(2);
        setMethod("POST");

		addChild(new Label("namespace", "Namespace"));
		tf_Namespace = new TextField("tf_Namespace");
		addChild(tf_Namespace);

		addChild(new Label("category", "Category"));
		sb_Category = new SelectBox("sb_Category");
		sb_Category.addOption("default","Default");
		addChild(sb_Category);

		addChild(new Label("property1", "Property1"));
		sb_Property1 = new SelectBox("sb_Property1");
		sb_Property1.addOption("property1","Property1");
		addChild(sb_Property1);

		bn_Submit = new Button("submit", "Submit");
		addChild(bn_Submit);
    }

	public Forward onValidate(Event evt) 
	{
		Application application = Application.getInstance();
		ClaimConfigModule module = (ClaimConfigModule)
						application.getModule(ClaimConfigModule.class);

		ClaimConfig obj = new ClaimConfig();

		UuidGenerator uuid = UuidGenerator.getInstance();
		obj.setId(uuid.getUuid());
		obj.setNamespace((String) tf_Namespace.getValue());
		obj.setCategory((String) sb_Category.getSelectedOptions().keySet().iterator().next());
		obj.setProperty1((String) sb_Property1.getSelectedOptions().keySet().iterator().next());

      module.addObject(obj);
		removeChildren();
		init();

		return super.onValidate(evt);
    }

}
