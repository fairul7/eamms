package com.tms.elearning.course.ui;

import kacang.stdui.TabbedPanel;
import kacang.stdui.Table;

import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;

import kacang.util.Log;


public class CoursePanel extends TabbedPanel {
    private CourseTable courseTable;

    public void init() {
        System.out.print("===>CoursePanel");
        removeChildren();

        addChild(courseTable);

        courseTable.init();

        courseTable.setHidden(false);

        courseTable.addEventListener(this);
    }

    public Forward actionPerformed(Event evt) {
        Widget widget = evt.getWidget();
        Log log = Log.getLog(this.getClass());

        try {
            log.debug("--- widget = " + widget.getName());

            if (widget.getName().equals(courseTable.getName())) {
                Table table = (Table) widget;
                log.debug("--- evt.getType() = " + evt.getType());

                if (evt.getType().equals(Table.PARAMETER_KEY_ACTION)) {
                    if (table.getSelectedAction().equalsIgnoreCase("add")) {
                        System.out.println("Add Form");
                    } else if (table.getSelectedAction().equalsIgnoreCase("delete")) {
                        courseTable.init();
                        courseTable.setHidden(false);
                    } else if (table.getSelectedAction().equalsIgnoreCase("archive")) {
                        courseTable.init();
                        courseTable.setHidden(false);
                    }
                } else if (evt.getType().equals(Table.PARAMETER_KEY_SELECTION)) {
                    System.out.println("Parameter Selected");
                }
            }
        } finally {
        }

        return super.actionPerformed(evt);
    }

    public CourseTable getCourseTable() {
        return courseTable;
    }

    public void setCourseTable(CourseTable courseTable) {
        this.courseTable = courseTable;
    }
}
