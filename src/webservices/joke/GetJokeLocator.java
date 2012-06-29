/**
 * GetJokeLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.joke;

public class GetJokeLocator extends org.apache.axis.client.Service implements webservices.joke.GetJoke {

    // <a href='http://interpressfact.net'><img src='http://www.interpressfact.net/webpage.files/logo.gif'
    // border='0'></a><br><font color='#006600' size='3' face='Geneva, Arial,
    // Helvetica, san-serif'>Returns random joke every time this webservice
    // is invoked.</font>

    // Use to get a proxy class for getJokeSoap
    private final java.lang.String getJokeSoap_address = "http://www.interpressfact.net/webservices/getJoke.asmx";

    public java.lang.String getgetJokeSoapAddress() {
        return getJokeSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String getJokeSoapWSDDServiceName = "getJokeSoap";

    public java.lang.String getgetJokeSoapWSDDServiceName() {
        return getJokeSoapWSDDServiceName;
    }

    public void setgetJokeSoapWSDDServiceName(java.lang.String name) {
        getJokeSoapWSDDServiceName = name;
    }

    public webservices.joke.GetJokeSoap getgetJokeSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getJokeSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getgetJokeSoap(endpoint);
    }

    public webservices.joke.GetJokeSoap getgetJokeSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            webservices.joke.GetJokeSoapStub _stub = new webservices.joke.GetJokeSoapStub(portAddress, this);
            _stub.setPortName(getgetJokeSoapWSDDServiceName());
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
            if (webservices.joke.GetJokeSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                webservices.joke.GetJokeSoapStub _stub = new webservices.joke.GetJokeSoapStub(new java.net.URL(getJokeSoap_address), this);
                _stub.setPortName(getgetJokeSoapWSDDServiceName());
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
        if ("getJokeSoap".equals(inputPortName)) {
            return getgetJokeSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://interpressfact.net/webservices/", "getJoke");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("getJokeSoap"));
        }
        return ports.iterator();
    }

}
