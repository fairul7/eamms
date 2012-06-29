package kacang.model.operator;

import java.util.Properties;

import kacang.Application;

/**
 * The OperatorLike operator simulates an SQL query of
 *
 * <code>WHERE fieldname LIKE (%value%)</code>.
 */
public class OperatorLike extends OperatorBase {
    public OperatorLike() {
    }

    public OperatorLike(String property, Object value, String operator) {
        super(property, value, operator);
    }

    public void setValue(Object value) {
    	Application app = Application.getInstance();
    	Properties properties = app.getProperties();
    	String db2 = properties.getProperty("kacang.model.operator.operatorLike.db2");
    	
        if (value instanceof String){
        	if(db2 != null && db2.equals("true"))
        		this.value = "%" + value.toString().toUpperCase() + "%";
        	else
        		this.value = "%" + value + "%";
        }
        else if (value == null)
            this.value = new String("%%");
        else
            this.value = value;
    }

    public String getQueryString() {
    	Application app = Application.getInstance();
    	Properties properties = app.getProperties();
    	String db2 = properties.getProperty("kacang.model.operator.operatorLike.db2");
    	
        if(db2 != null && db2.equals("true"))
        	return " " + getOperator() + " (UPPER(VARCHAR(" + getProperty() + ")) LIKE ?)";
        else
        	return " " + getOperator() + " (" + getProperty() + " LIKE ?)";
    }
}
