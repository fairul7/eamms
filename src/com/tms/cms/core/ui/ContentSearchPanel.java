package com.tms.cms.core.ui;

import kacang.stdui.Panel;
import kacang.stdui.event.FormEventAdapter;
import kacang.ui.Event;
import kacang.ui.Forward;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 9, 2003
 * Time: 12:04:51 PM
 * To change this template use Options | File Templates.
 */
public class ContentSearchPanel extends Panel {

    private ContentSearchForm contentSearchForm;
    private ContentSearchTable contentSearchTable;

    public ContentSearchPanel() {
    }

    public ContentSearchPanel(String name) {
        super(name);
    }

    public void init() {
        addContainerForm();
    }

    protected void addContainerForm() {
        // remove existing widgets
        removeChildren();

        // add container form
        contentSearchForm = new ContentSearchForm("contentSearchForm");
        addChild(contentSearchForm);
        contentSearchForm.init();

        // add form listener
        contentSearchForm.addFormEventListener(new FormEventAdapter() {
            public Forward onValidate(Event evt) {
                // get form values and set to table
                contentSearchTable.setQuery(contentSearchForm.getQuery());
                contentSearchTable.setSort(contentSearchForm.getSort());
                contentSearchTable.setPageSize(contentSearchForm.getPageSize());
                return new Forward("search");
            }
        });

        // add forwards
        for (Iterator it=getForwardMap().values().iterator(); it.hasNext();) {
            contentSearchForm.addForward((Forward)it.next());
        }

        // add content search table
        contentSearchTable = new ContentSearchTable("contentSearchTable");
        contentSearchTable.setHideFilters(true);
        contentSearchTable.setWidth("100%");
        addChild(contentSearchTable);
        contentSearchTable.init();

        // add forwards
        for (Iterator it=getForwardMap().values().iterator(); it.hasNext();) {
            contentSearchTable.addForward((Forward)it.next());
        }

    }

}
