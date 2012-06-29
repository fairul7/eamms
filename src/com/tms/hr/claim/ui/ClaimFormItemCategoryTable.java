/**
 * @author Vincent Lee, Wavelet Solutions Sdn Bhd (632468-W)
 * Created On: 2004-06-18
 * Copyright of The Media Shoppe Berhad
 */
package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;
import com.tms.hr.claim.model.ClaimFormItemCategoryModule;
import com.tms.hr.claim.model.ClaimTypeObject;

import com.tms.util.FormatUtil;

import kacang.Application;

import kacang.stdui.*;

import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class ClaimFormItemCategoryTable extends Table {
    public void init() {
        setMethod("POST");
        setModel(new ClaimFormItemCategoryTableModel());
    }

    public class ClaimFormItemCategoryTableModel extends TableModel {
        public ClaimFormItemCategoryTableModel() {
            TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("claims.label.categoryName","Category Name"));
            tcName.setUrl("config_category_edit.jsp");
            tcName.setUrlParam("id");
            addColumn(tcName);
            addColumn(new TableColumn("code", Application.getInstance().getMessage("claims.label.code","Code")));
            addColumn(new TableColumn("description", Application.getInstance().getMessage("claims.category.description","Description")));

            TableColumn tcTimeEdit = new TableColumn("timeEdit",
                    Application.getInstance().getMessage("claims.label.lastEditTime","Last Edit Time"));
            tcTimeEdit.setFormat(new TableDateFormat(FormatUtil.getInstance()
                                                               .getLongDateTimeFormat()));
            addColumn(tcTimeEdit);

            //			TableColumn statusImage = new TableColumn("statusImage","Status");
            //			statusImage.setAlign("center");
            //			addColumn(statusImage);

            /*
            //feature dissabled
            TableColumn editCol = new TableColumn(null, "Dependency", false);
            editCol.setLabel("View");
            editCol.setUrl("categoryDependencyForm.jsp");
            editCol.setUrlParam("id");
            addColumn(editCol);
            */
            TableFilter typeFilter = new TableFilter("typeFilter");
            SelectBox sbType = new SelectBox("sbType");

            //retrieve type from database
            Collection allTypeCol = null;
            Map allTypeMap = new SequencedHashMap();
            Application app = Application.getInstance();
            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);
            allTypeCol = module.retrieveAllType();

            allTypeMap.put("default", Application.getInstance().getMessage("claims.label.default","Default"));
            allTypeMap.put("travel", Application.getInstance().getMessage("claims.label.travel","Travel"));

            for (Iterator iterator = allTypeCol.iterator(); iterator.hasNext();) {
                ClaimTypeObject object = (ClaimTypeObject) iterator.next();
                allTypeMap.put(object.getId(), object.getTypeName());
            }

            if (allTypeMap != null) {
                sbType.setOptionMap(allTypeMap);
            }

            /*            sbType.addOption("default","Default");
            sbType.addOption("travel","Travel");
                        sbType.setSelectedOption("default");  */
            sbType.setSelectedOption("default");
            typeFilter.setWidget(sbType);

            // addFilter(typeFilter);
            addAction(new TableAction("delete", Application.getInstance().getMessage("claims.label.delete","Delete"),
                    Application.getInstance().getMessage("claims.message.criteriaDelete","Only not in used category in selected list will be deleted!")));

            addFilter(new TableFilter("keyword"));
        }

        public Collection getTableRows() {
            String type = null;
            Collection selected = (Collection) getFilterValue("typeFilter");

            if ((selected != null) && (selected.size() > 0)) {
                type = (String) selected.iterator().next();
            }

            type = "default";

            String keyword = (String) getFilterValue("keyword");

            Application application = Application.getInstance();
            ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);

            return module.selectObjects(keyword,"status", "act", type, getStart(),
                getRows(), getSort(), isDesc());
        }

        public int getTotalRowCount() {
            String type = null;
            Collection selected = (Collection) getFilterValue("typeFilter");

            if ((selected != null) && (selected.size() > 0)) {
                type = (String) selected.iterator().next();
            }

            type = "default";

            String keyword = (String) getFilterValue("keyword");

            Application application = Application.getInstance();
            ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);

            return module.countObjects(keyword,"act", type);
        }

        public String getTableRowKey() {
            return "id";
        }

        public Forward processAction(Event evt, String action,
            String[] selectedKeys) {
            if ("delete".equals(action)) {
                Application application = Application.getInstance();
                ClaimFormItemCategoryModule module = (ClaimFormItemCategoryModule) application.getModule(ClaimFormItemCategoryModule.class);

                for (int i = 0; i < selectedKeys.length; i++) {
                    module.deleteObject(selectedKeys[i]);
                }
            }

            return null;
        }
    }
}
