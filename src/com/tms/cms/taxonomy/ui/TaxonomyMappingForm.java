package com.tms.cms.taxonomy.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentHelper;
import com.tms.cms.taxonomy.model.TaxonomyMap;
import com.tms.cms.taxonomy.model.TaxonomyModule;
import com.tms.cms.taxonomy.model.TaxonomyNode;

public class TaxonomyMappingForm extends Form {

    private String contentId;
    private Collection[] mappedList;
    private TaxonomyMap[] list;

    private CheckBox[] cbNodes;
    private Button btnRemove;

    public void init() {

    }

    public String getDefaultTemplate() {
        return "taxonomy/contentMappingForm";
    }

    public void onRequest(Event event) {
        ContentObject co = ContentHelper.getContentObject(event, getContentId());
        if (co != null) {
            setContentId(co.getId());
        }
        addContainerForm();
    }

    public void addContainerForm() {
        removeChildren();
        mappedList = null;
        list=null;

        TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
        list = mod.getMappingByContentId(contentId,getWidgetManager().getUser());

        if (list!=null && list.length>0) {
            mappedList = new Collection[list.length];
            cbNodes = new CheckBox[list.length];

            for (int i=0;i<list.length;i++) {
                mappedList[i]=new ArrayList();
                getParentNode(list[i].getTaxonomyId(),i,mod);
                cbNodes[i] = new CheckBox("cbNode"+i);
                cbNodes[i].setValue(""+list[i].getTaxonomyId());
                addChild(cbNodes[i]);
            }
        }

        btnRemove = new Button("btnRemove",Application.getInstance().getMessage("taxonomy.label.remove","Remove"));
        addChild(btnRemove);
    }

    public Forward onValidate(Event event) {
        if (btnRemove.getAbsoluteName().equals(findButtonClicked(event))) {
            Collection col = new ArrayList();
            for (int i=0; i<cbNodes.length;i++) {
                if (cbNodes[i].isChecked()) {
                    String sId = (String)cbNodes[i].getValue();
                    col.add(sId);
                }
            }
            if (col!=null && col.size()>0) {
                TaxonomyModule mod = (TaxonomyModule)Application.getInstance().getModule(TaxonomyModule.class);
                TaxonomyMap map;
                for (Iterator i=col.iterator();i.hasNext();) {
                    map = new TaxonomyMap();
                    map.setContentId(getContentId());
                    map.setTaxonomyId((String)i.next());
                    mod.deleteMapping(map);
                }
            }
            return new Forward("removed");
        }
        return null;
    }

    public void getParentNode(String taxonomyId, int iCurrent, TaxonomyModule mod) {
        TaxonomyNode node = mod.getNode(taxonomyId);
        if (!node.getParentId().equals("0")) {
            getParentNode(node.getParentId(),iCurrent,mod);
            mappedList[iCurrent].add(node);
        }
        else {
            mappedList[iCurrent].add(node);
        }
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public Collection[] getMappedList() {
        return mappedList;
    }

    public void setMappedList(Collection[] mappedList) {
        this.mappedList = mappedList;
    }

    public TaxonomyMap[] getList() {
        return list;
    }

    public void setList(TaxonomyMap[] list) {
        this.list = list;
    }

    public CheckBox[] getCbNodes() {
        return cbNodes;
    }

    public void setCbNodes(CheckBox[] cbSelected) {
        this.cbNodes = cbSelected;
    }

    public Button getBtnRemove() {
        return btnRemove;
    }

    public void setBtnRemove(Button btnRemove) {
        this.btnRemove = btnRemove;
    }
}

