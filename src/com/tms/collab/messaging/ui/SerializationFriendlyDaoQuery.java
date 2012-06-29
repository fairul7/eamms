package com.tms.collab.messaging.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorBase;
import kacang.util.Log;

/**
 * To make {@link kacang.model.DaoQuery} serializable by extending it
 * and doing the serialization in this class itself.
 *
 * NOTE: TO BE USED ONLY IN {@link com.tms.collab.messaging.ui.MessageTable} NOT
 * ELSE WHERE.
 */
public class SerializationFriendlyDaoQuery extends DaoQuery implements Serializable {

    private static final long serialVersionUID = 4668577025069712798L;


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        List daoOperators = new ArrayList();
        int count = ois.readInt();
        for (int a=0; a< count; a++) {
            String classType = (String) ois.readObject();
            Object value = ois.readObject(); // value
            String property = (String) ois.readObject(); // property

            try {
                DaoOperator daoOperator = instantiateDaoOperator(classType);
                daoOperator.setProperty(property);
                daoOperator.setValue(value);
                if (daoOperator instanceof OperatorBase) {
                    String operator = (String) ois.readObject(); // operator
                    ((OperatorBase)daoOperator).setOperator(operator);
                }
                daoOperators.add(daoOperator);
            }
            catch(Exception e) {
                Log.getLog(getClass()).error("Error serializing DaoOperator", e);
            }
        }
        setProperties(daoOperators);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();

        Collection properties = getProperties();
        int size = properties.size();
        oos.writeInt(size);
        for (Iterator i = properties.iterator(); i.hasNext(); ) {
            DaoOperator daoOperator = (DaoOperator) i.next();

            oos.writeObject(daoOperator.getClass().getName());
            oos.writeObject(daoOperator.getValue());
            oos.writeObject(daoOperator.getProperty());
            if (daoOperator instanceof OperatorBase) {
                oos.writeObject(((OperatorBase)daoOperator).getOperator());
            }
        }
    }


    private static final DaoOperator instantiateDaoOperator(String object)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class objectClass = Class.forName(object);
        // let it throws ClassCastException if something went wrong
        return (DaoOperator) objectClass.newInstance();
    }
}
