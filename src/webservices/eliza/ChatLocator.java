/**
 * ChatLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.eliza;

public class ChatLocator extends org.apache.axis.client.Service implements webservices.eliza.Chat {

    // Eliza - Chat with a bot!

    // Use to get a proxy class for IBaseDataTypes
    private final java.lang.String IBaseDataTypes_address = "http://www.x-ws.de/cgi-bin/eliza/chat.wsdl";

    public java.lang.String getIBaseDataTypesAddress() {
        return IBaseDataTypes_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IBaseDataTypesWSDDServiceName = "IBaseDataTypes";

    public java.lang.String getIBaseDataTypesWSDDServiceName() {
        return IBaseDataTypesWSDDServiceName;
    }

    public void setIBaseDataTypesWSDDServiceName(java.lang.String name) {
        IBaseDataTypesWSDDServiceName = name;
    }

    public webservices.eliza.ChatPortType getIBaseDataTypes() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IBaseDataTypes_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIBaseDataTypes(endpoint);
    }

    public webservices.eliza.ChatPortType getIBaseDataTypes(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.eliza.ChatBindingStub _stub = new webservices.eliza.ChatBindingStub(portAddress, this);
            _stub.setPortName(getIBaseDataTypesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (webservices.eliza.ChatPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.eliza.ChatBindingStub _stub = new webservices.eliza.ChatBindingStub(new java.net.URL(IBaseDataTypes_address), this);
                _stub.setPortName(getIBaseDataTypesWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("IBaseDataTypes".equals(inputPortName)) {
            return getIBaseDataTypes();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("SoapInterop", "chat");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("IBaseDataTypes"));
        }
        return ports.iterator();
    }

}
