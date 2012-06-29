/**
 * BabelFishServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.babelfish;

public class BabelFishServiceLocator extends org.apache.axis.client.Service implements webservices.babelfish.BabelFishService {

    // Translates text of up to 5k in length, between a variety of languages.

    // Use to get a proxy class for BabelFishPort
    private final java.lang.String BabelFishPort_address = "http://services.xmethods.net:80/perl/soaplite.cgi";

    public java.lang.String getBabelFishPortAddress() {
        return BabelFishPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BabelFishPortWSDDServiceName = "BabelFishPort";

    public java.lang.String getBabelFishPortWSDDServiceName() {
        return BabelFishPortWSDDServiceName;
    }

    public void setBabelFishPortWSDDServiceName(java.lang.String name) {
        BabelFishPortWSDDServiceName = name;
    }

    public webservices.babelfish.BabelFishPortType getBabelFishPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BabelFishPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBabelFishPort(endpoint);
    }

    public webservices.babelfish.BabelFishPortType getBabelFishPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.babelfish.BabelFishBindingStub _stub = new webservices.babelfish.BabelFishBindingStub(portAddress, this);
            _stub.setPortName(getBabelFishPortWSDDServiceName());
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
            if (webservices.babelfish.BabelFishPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.babelfish.BabelFishBindingStub _stub = new webservices.babelfish.BabelFishBindingStub(new java.net.URL(BabelFishPort_address), this);
                _stub.setPortName(getBabelFishPortWSDDServiceName());
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
        if ("BabelFishPort".equals(inputPortName)) {
            return getBabelFishPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.xmethods.net/sd/BabelFishService.wsdl", "BabelFishService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("BabelFishPort"));
        }
        return ports.iterator();
    }

}
