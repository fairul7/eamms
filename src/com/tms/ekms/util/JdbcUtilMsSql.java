package com.tms.ekms.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * This class is intended to simplify the use of JDBC database access
 * by using reflection. This implementation is customized for Oracle 10g.
 * To use this utility, call
 * <code> JdbcUtil.getInstance() </code>
 * and use
 * <li><code>update()</code> for INSERT, UPDATE, DELETE and other update statements,
 * <li><code>select()</code> for SELECT statements.
 */
public class JdbcUtilMsSql extends kacang.util.JdbcUtil {
	
	public void setBeanProperty(Object obj, String name, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (obj instanceof Map) {
            ((Map) obj).put(name, value);
        } else {
            try {
                PropertyUtils.setSimpleProperty(obj, name, value);
            } catch (IllegalAccessException e) {
                throw e;
            } catch (IllegalArgumentException ie) {
                try {
                    PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(obj, name);
                    
                    if (pd.getPropertyType().getName().equals("boolean") && !Boolean.getBoolean(value.toString())) {
                        // handle boolean value
                        value = ("1".equals(value)) ? Boolean.TRUE : Boolean.FALSE;
                        PropertyUtils.setSimpleProperty(obj, name, value);
                    } 
                    /**
					 * Support for non standard numeric types. The BeanUtils class will not be able to introspect into
					 * numeric classes outside of the java.lang package so customized handling has to be implemented
					 * here.
					 * */
                    else if(pd.getPropertyType().getName().equals("double"))
						PropertyUtils.setSimpleProperty(obj, name, new Double(((Number)value).doubleValue()));
					else {
                        // handle as String
                        PropertyUtils.setSimpleProperty(obj, name, value.toString());
                    }
                } catch (Exception e) {
                    // try to invoke mapped property in DefaultDataObject
                    setMappedProperty(obj, name, value);
                }
            } catch (InvocationTargetException e) {
                // try to invoke mapped property in DefaultDataObject
                setMappedProperty(obj, name, value);
            } catch (NoSuchMethodException e) {
                // try to invoke mapped property in DefaultDataObject
                setMappedProperty(obj, name, value);
            }
        }
    }
	
	public Collection convertResultSetToCollection(ResultSet rs, Class resultClass, Collection attributeList, int start, int rows) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int currentRow = 0;
        Collection resultList = new ArrayList();
        Object rowObj;
        int index;
        int end = start + rows;
        String name;
        Object value;
        while (rs.next()) {
            if (currentRow >= start && (rows < 0 || currentRow < end)) {
                // instantiate object
                rowObj = resultClass.newInstance();
                index = 1;
                for (Iterator it = attributeList.iterator(); it.hasNext(); index++) {
                    name = (String) it.next();
                    value = rs.getObject(index);
                    
//                    if(value != null && value.getClass().getName().equals("java.math.BigDecimal")){
//                    	value = new Double(((Number)value).doubleValue());
//                    }
                    
                    setBeanProperty(rowObj, name, value);
                }
                resultList.add(rowObj);
            }
            currentRow++;
        }
        return resultList;
    }

}
