package com.tms.ekms.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

/**
 * This class is intended to simplify the use of JDBC database access
 * by using reflection. This implementation is customized for Oracle 10g.
 * To use this utility, call
 * <code> JdbcUtil.getInstance() </code>
 * and use
 * <li><code>update()</code> for INSERT, UPDATE, DELETE and other update statements,
 * <li><code>select()</code> for SELECT statements.
 */
public class JdbcUtilOracle extends kacang.util.JdbcUtil {

    /**
     * This method converts a ResultSet into a Collection of objects of type resultClass.
     * @param rs The ResultSet.
     * @param resultClass The required result class type.
     * @param attributeList A list of column names.
     * @param start The starting index of results to return (first row is 0)
     * @param rows The number of rows to return.
     * @return A Collection of objects of the type specified by resultClass.
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Collection convertResultSetToCollection(ResultSet rs, Class resultClass, Collection attributeList, int start, int rows) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int currentRow = 0;
        Collection resultList = new ArrayList();
        Object rowObj;
        int index;
        String name;
        Object value;
        while (rs.next()) {
            if (currentRow >= start && (rows < 0 || currentRow < (start + rows))) {
                // instantiate object
                rowObj = resultClass.newInstance();
                index = 1;
                for (Iterator it = attributeList.iterator(); it.hasNext(); index++) {
                    name = (String) it.next();
                    /*value = rs.getObject(index);*/

					//TODO: HACK JOB DONE HERE FOR ORACLE. FIGURE OUT A MORE ELEGANT SOLUTION
					value = rs.getObject(index);
					if(StringUtils.containsNone(name, new char[] { 'a', 'b', 'c', 'd', 'e', 'f',
																   'g', 'h', 'i', 'j', 'k', 'l',
																   'm', 'n', 'o', 'p', 'q', 'r',
																   's', 't', 'u', 'v', 'w', 'x',
																   'y', 'z' } )) {
						name = name.toLowerCase();
					}
					if(value instanceof oracle.sql.CLOB)
						setBeanProperty(rowObj, name, rs.getString(name));
					else if(value instanceof java.sql.Date)
						// TODO: Will break everything except ORACLE
						setBeanProperty(rowObj, name, rs.getTimestamp(name));
					else
						setBeanProperty(rowObj, name, value);
					//TODO: HACK JOB DONE HERE FOR ORACLE. FIGURE OUT A MORE ELEGANT SOLUTION

                }
                resultList.add(rowObj);
            }
            currentRow++;
        }
        return resultList;
    }

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
					else if(pd.getPropertyType().getName().equals("int"))
						PropertyUtils.setSimpleProperty(obj, name, new Integer(((Number)value).intValue()));
					else if(pd.getPropertyType().getName().equals("double"))
						PropertyUtils.setSimpleProperty(obj, name, new Double(((Number)value).doubleValue()));
					else if(pd.getPropertyType().getName().equals("float"))
						PropertyUtils.setSimpleProperty(obj, name, new Float(((Number)value).floatValue()));
					else if(pd.getPropertyType().getName().equals("long"))
						PropertyUtils.setSimpleProperty(obj, name, new Long(((Number)value).longValue()));
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

}
