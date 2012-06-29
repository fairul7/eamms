/**
 * Chat.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.eliza;

public interface Chat extends javax.xml.rpc.Service {

    // Eliza - Chat with a bot!
    public java.lang.String getIBaseDataTypesAddress();

    public webservices.eliza.ChatPortType getIBaseDataTypes() throws javax.xml.rpc.ServiceException;

    public webservices.eliza.ChatPortType getIBaseDataTypes(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
