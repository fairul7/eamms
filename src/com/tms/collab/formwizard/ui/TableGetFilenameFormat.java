package com.tms.collab.formwizard.ui;

import kacang.stdui.TableFormat;

import java.util.StringTokenizer;

public class TableGetFilenameFormat implements TableFormat {


    public String format(Object value) {
        String str = String.valueOf(value);


        if (str != null && str.trim().length() > 0) {
            StringTokenizer stk = new StringTokenizer(str,"/");
            while (stk.hasMoreTokens()) {
                str = stk.nextToken();
            }
        }
        else {
            str = "N/A";
        }
        return str;
    }
}
