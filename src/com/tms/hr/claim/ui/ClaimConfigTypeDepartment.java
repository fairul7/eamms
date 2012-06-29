package com.tms.hr.claim.ui;

import com.tms.hr.claim.model.ClaimConfigTypeModule;

import kacang.Application;

import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.stdui.TableFilter;

import java.util.Collection;


/**
 * Created by IntelliJ IDEA.
 * User: cheewei
 * Date: Dec 8, 2005
 * Time: 5:43:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClaimConfigTypeDepartment extends Table {
    public void init() {

        setModel(new ClaimConfigTypeDepartmentModel());
    }

    public class ClaimConfigTypeDepartmentModel extends TableModel {
        ClaimConfigTypeDepartmentModel() {
            Application app = Application.getInstance();

            addFilter(new TableFilter("keyword"));

            TableColumn naCol = new TableColumn("typeName",
                    app.getMessage("claims.type.name", "type"));

            naCol.setUrlParam("id");
            addColumn(naCol);

            TableColumn naCol2 = new TableColumn("dm_dept_desc",
                    app.getMessage("claims.label.department", "department"));

            addColumn(naCol2);



        }

        public Collection getTableRows() {
            Application app = Application.getInstance();

            String keyword = (String)getFilterValue("keyword");
            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

            return module.retrieveAllTypeDepartment(keyword, getSort(), isDesc(),
                getStart(), getRows());
        }

        public int getTotalRowCount() {
            Application app = Application.getInstance();
            String keyword = (String)getFilterValue("keyword");
            ClaimConfigTypeModule module = (ClaimConfigTypeModule) app.getModule(ClaimConfigTypeModule.class);

            return module.countAllTypeDepart(keyword);
        }
    }
}
