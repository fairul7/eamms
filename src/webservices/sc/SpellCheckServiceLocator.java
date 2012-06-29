/**
 * SpellCheckServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class SpellCheckServiceLocator extends org.apache.axis.client.Service implements webservices.sc.SpellCheckService {

    // Use to get a proxy class for SpellCheckServiceSoap
    private final java.lang.String SpellCheckServiceSoap_address = "http://www.worldwidedesktop.com/spellcheck/spellcheckservice.asmx";

    public java.lang.String getSpellCheckServiceSoapAddress() {
        return SpellCheckServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SpellCheckServiceSoapWSDDServiceName = "SpellCheckServiceSoap";

    public java.lang.String getSpellCheckServiceSoapWSDDServiceName() {
        return SpellCheckServiceSoapWSDDServiceName;
    }

    public void setSpellCheckServiceSoapWSDDServiceName(java.lang.String name) {
        SpellCheckServiceSoapWSDDServiceName = name;
    }

    public webservices.sc.SpellCheckServiceSoap getSpellCheckServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SpellCheckServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSpellCheckServiceSoap(endpoint);
    }

    public webservices.sc.SpellCheckServiceSoap getSpellCheckServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.sc.SpellCheckServiceSoapStub _stub = new webservices.sc.SpellCheckServiceSoapStub(portAddress, this);
            _stub.setPortName(getSpellCheckServiceSoapWSDDServiceName());
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
            if (webservices.sc.SpellCheckServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.sc.SpellCheckServiceSoapStub _stub = new webservices.sc.SpellCheckServiceSoapStub(new java.net.URL(SpellCheckServiceSoap_address), this);
                _stub.setPortName(getSpellCheckServiceSoapWSDDServiceName());
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
        if ("SpellCheckServiceSoap".equals(inputPortName)) {
            return getSpellCheckServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "SpellCheckService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SpellCheckServiceSoap"));
        }
        return ports.iterator();
    }

}
