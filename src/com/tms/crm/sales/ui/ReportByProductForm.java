package com.tms.crm.sales.ui;

import com.tms.crm.sales.model.CategoryModule;
import com.tms.crm.sales.model.CategoryObject;
import com.tms.crm.sales.model.ProductModule;
import kacang.Application;
import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.*;

public class ReportByProductForm extends Form{

    private Button view;  
    protected SelectBox category;
    private ComboSelectBox sb_product;
    private Radio viewAll;
    private Radio viewSelected;
    private ButtonGroup viewGroup;
    private DateField from;
    private DateField to;
    private Date toDate,fromDate;
    private Collection productIdList; 
    private String categorySelected;

	protected DateField startFrom;
	protected DateField startTo;
	protected Date startFromDate, startToDate;
	protected CheckBox start;
	protected CheckBox close;
	protected boolean startBool;
	protected boolean closeBool;
	private ValidatorMessage vmsg_start;
	private ValidatorMessage vmsg_close;


    public ReportByProductForm() {
    }

    public ReportByProductForm(String name) {
        super(name);
    }


    public void init() {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        setWidth("100%");
        view = new Button("view",Application.getInstance().getMessage("sfa.label.view","View"));
        sb_product = new ComboSelectBox("products");
        viewAll = new Radio("viewall",Application.getInstance().getMessage("sfa.label.viewAll","View All"));
        viewAll.setChecked(true);
        viewSelected = new Radio("viewSelected",Application.getInstance().getMessage("sfa.label.viewSelected","View Selected"));
        viewGroup = new ButtonGroup("viewGroup",new Radio[]{viewAll,viewSelected});
        from = new DateField("from");
        to = new DateField("to");

        CategoryModule module = (CategoryModule) Application.getInstance().getModule(CategoryModule.class);
        category = new SelectBox("category");
		category.addOption("", Application.getInstance().getMessage("sfa.label.pleaseSelect","--Please Select--"));
        Collection cat = module.getCategory();
        for( Iterator i=cat.iterator(); i.hasNext(); ){
        	CategoryObject o = (CategoryObject) i.next();
        	category.addOption(o.getCategoryID(), o.getCategoryName());
        }
        category.setOnChange("submit() ");
        addChild(category);
        
		startFrom = new DateField("startFrom");
		startTo = new DateField("startTo");
		start = new CheckBox("start");
		close = new CheckBox("close");
		vmsg_start = new ValidatorMessage("vmsg_start");
		start.addChild(vmsg_start);
		vmsg_close = new ValidatorMessage("vmsg_close");
		close.addChild(vmsg_close);
		addChild(startFrom);
		addChild(startTo);
		addChild(start);
		addChild(close);
        addChild(view);
        addChild(viewAll);
        addChild(viewSelected);
        addChild(sb_product);
        sb_product.init();
        addChild(from);
        addChild(to);
        setMethod("POST");
    }


    public void onRequest(Event evt) {
        super.onRequest(evt);    //To change body of overridden methods use File | Settings | File Templates.
        init();
        sb_product.init();

    }

		public Forward onSubmit(Event evt) {
		Forward fwd = super.onSubmit(evt);
		if (!start.isChecked() && !close.isChecked()) {
			vmsg_start.showError("");
			vmsg_close.showError("");
			setInvalid(true);
		}
	  	
 		 String button = findButtonClicked(evt);
 		 button = (button == null)? "" : button;
 		 
 
 		 if (!view.getAbsoluteName().equals(button)) {
 			 categorySelected = (String) category.getSelectedOptions().keySet().iterator().next();
 			
 		    	if (categorySelected!=null || !categorySelected.equals(""))
 		    	{
 		        	init();
 		        	category.setSelectedOptions(new String[] {categorySelected});
 		        	populateProduct(categorySelected);
 		            setInvalid(true);
 		        }
 		 }
 		 
 	
		return fwd;
	}

	public void populateProduct(String categorySelected){
	    	sb_product.init();
	    	ProductModule proModule = (ProductModule) Application.getInstance().getModule(ProductModule.class);
	        if(sb_product!=null){
	            Map rightMap = sb_product.getRightValues();
	            
	            Map leftMap = proModule.getProductMapping(categorySelected);
	            sb_product.setLeftValues(leftMap);
	            for (Iterator iterator = rightMap.keySet().iterator(); iterator.hasNext();) {
	                String productId = (String) iterator.next();
	                leftMap.remove(productId);
	            }
	           
	        }
		   
	}
		
    public Forward onValidate(Event evt) {
        if(view.getAbsoluteName().equals(findButtonClicked(evt))){
            toDate = to.getDate();
            fromDate = from.getDate();
			startFromDate = startFrom.getDate();
			startToDate = startTo.getDate();
			startBool = start.isChecked();
			closeBool = close.isChecked();

            //if(companiesIdList==null )
            productIdList = new ArrayList();
            if(viewAll.isChecked()){
            	ProductModule proModule = (ProductModule) Application.getInstance().getModule(ProductModule.class);
                Map map = proModule.getProductMap();
                for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                    String productId = (String) iterator.next();
                    productIdList.add(productId);
                }

            }else{
                Map map= sb_product.getRightValues();
                for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                    String productId = (String) iterator.next();
                    productIdList.add(productId);
                } 
            }
            return new Forward("view");
        }
        return super.onValidate(evt);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Radio getViewAll() {
        return viewAll;
    }

    public void setViewAll(Radio viewAll) {
        this.viewAll = viewAll;
    }

    public Radio getViewSelected() {
        return viewSelected;
    }

    public void setViewSelected(Radio viewSelected) {
        this.viewSelected = viewSelected;
    }

    public ButtonGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ButtonGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public DateField getFrom() {
        return from;
    }

    public void setFrom(DateField from) {
        this.from = from;
    }

    public DateField getTo() {
        return to;
    }

    public void setTo(DateField to) {
        this.to = to;
    }

    public String getDefaultTemplate() {
        return "sfa/reportbyproduct";
    }


    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

	public Collection getProductIdList() {
		return productIdList;
	}

	public void setProductIdList(Collection productIdList) {
		this.productIdList = productIdList;
	}

	public ComboSelectBox getSb_product() {
		return sb_product;
	}

	public void setSb_product(ComboSelectBox sb_product) {
		this.sb_product = sb_product;
	}

	public DateField getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(DateField startFrom) {
		this.startFrom = startFrom;
	}

	public DateField getStartTo() {
		return startTo;
	}

	public void setStartTo(DateField startTo) {
		this.startTo = startTo;
	}

	public Date getStartFromDate() {
		return startFromDate;
	}

	public void setStartFromDate(Date startFromDate) {
		this.startFromDate = startFromDate;
	}

	public Date getStartToDate() {
		return startToDate;
	}

	public void setStartToDate(Date startToDate) {
		this.startToDate = startToDate;
	}

	public CheckBox getStart() {
		return start;
	}

	public void setStart(CheckBox start) {
		this.start = start;
	}

	public CheckBox getClose() {
		return close;
	}

	public void setClose(CheckBox close) {
		this.close = close;
	}

	public boolean isStartBool() {
		return startBool;
	}

	public void setStartBool(boolean startBool) {
		this.startBool = startBool;
	}

	public boolean isCloseBool() {
		return closeBool;
	}

	public void setCloseBool(boolean closeBool) {
		this.closeBool = closeBool;
	}

	public ValidatorMessage getVmsg_start() {
		return vmsg_start;
	}

	public void setVmsg_start(ValidatorMessage vmsg_start) {
		this.vmsg_start = vmsg_start;
	}

	public ValidatorMessage getVmsg_close() {
		return vmsg_close;
	}

	public void setVmsg_close(ValidatorMessage vmsg_close) {
		this.vmsg_close = vmsg_close;
	}

	public SelectBox getCategory() {
		return category;
	}

	public void setCategory(SelectBox category) {
		this.category = category;
	}

	public String getCategorySelected() {
		return categorySelected;
	}

	public void setCategorySelected(String categorySelected) {
		this.categorySelected = categorySelected;
	}


}

