package com.tms.collab.formwizard.grid;



import org.apache.commons.lang.NumberUtils;
import org.apache.commons.collections.SequencedHashMap;

import java.io.Serializable;
import java.util.Map;

public class G2Column implements Serializable {
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_DROP_DOWN = "dropDown";
//    public static final String TYPE_DATE = "date";
    public static final String TYPE_FORMULA = "formula";


    public static final String VALIDATE_REQUIRED = "required";
    public static final String VALIDATE_NUMBER = "number";
//    public static final String VALIDATE_DATE = "date";

    private String name;
    private String header;
    private String type;
    private Map itemMap;    // used by drop down list
    private String formula;
    private boolean columnTotal;
    private String validation;

    public G2Column() {

    }

    public G2Column(String name, String header, String type) {
        this.name = name;
        this.header = header;
        this.type = type;
    }

    public boolean isValidValue(String value) {
        boolean valid = true;

        if(VALIDATE_REQUIRED.equals(validation)) {
            if(value==null || value.trim().length()==0) {
                valid = false;
            }

        } else if(VALIDATE_NUMBER.equals(validation)) {
            if(!NumberUtils.isNumber(value)) {
                valid = false;
            }
        }

        return valid;
    }
    

    // === [ getters/setters ] =================================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map itemMap) {
        this.itemMap = itemMap;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public boolean isColumnTotal() {
        return columnTotal;
    }

    public void setColumnTotal(boolean columnTotal) {
        this.columnTotal = columnTotal;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
}
