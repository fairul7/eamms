/**
 * SpellCheckServiceSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class SpellCheckServiceSoapStub extends org.apache.axis.client.Stub implements webservices.sc.SpellCheckServiceSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SpellCheck");
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "LicenseText"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "TextToCheck"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "corrections"));
        oper.setReturnClass(webservices.sc.Corrections.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "SpellCheckResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("MSSpellCheck");
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "LicenseText"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "TextToCheck"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">MSSpellCheckResponse>MSSpellCheckResult"));
        oper.setReturnClass(webservices.sc._MSSpellCheckResponse_MSSpellCheckResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "MSSpellCheckResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("WSpellCheck");
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "LicenseText"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "TextToCheck"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">WSpellCheckResponse>WSpellCheckResult"));
        oper.setReturnClass(webservices.sc._WSpellCheckResponse_WSpellCheckResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "WSpellCheckResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

    }

    public SpellCheckServiceSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SpellCheckServiceSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SpellCheckServiceSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">MSSpellCheckResponse>MSSpellCheckResult");
            cachedSerQNames.add(qName);
            cls = webservices.sc._MSSpellCheckResponse_MSSpellCheckResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "corrections");
            cachedSerQNames.add(qName);
            cls = webservices.sc.Corrections.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = webservices.sc.ArrayOfString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "correctionsCorrection");
            cachedSerQNames.add(qName);
            cls = webservices.sc.CorrectionsCorrection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">WSpellCheck");
            cachedSerQNames.add(qName);
            cls = webservices.sc._WSpellCheck.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">WSpellCheckResponse");
            cachedSerQNames.add(qName);
            cls = webservices.sc._WSpellCheckResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">WSpellCheckResponse>WSpellCheckResult");
            cachedSerQNames.add(qName);
            cls = webservices.sc._WSpellCheckResponse_WSpellCheckResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public webservices.sc.Corrections spellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.worldwidedesktop.com/spellcheck/SpellCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "SpellCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {licenseText, textToCheck});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (webservices.sc.Corrections) _resp;
            } catch (java.lang.Exception _exception) {
                return (webservices.sc.Corrections) org.apache.axis.utils.JavaUtils.convert(_resp, webservices.sc.Corrections.class);
            }
        }
    }

    public webservices.sc._MSSpellCheckResponse_MSSpellCheckResult MSSpellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.worldwidedesktop.com/spellcheck/MSSpellCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "MSSpellCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {licenseText, textToCheck});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (webservices.sc._MSSpellCheckResponse_MSSpellCheckResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (webservices.sc._MSSpellCheckResponse_MSSpellCheckResult) org.apache.axis.utils.JavaUtils.convert(_resp, webservices.sc._MSSpellCheckResponse_MSSpellCheckResult.class);
            }
        }
    }

    public webservices.sc._WSpellCheckResponse_WSpellCheckResult WSpellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.worldwidedesktop.com/spellcheck/WSpellCheck");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "WSpellCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {licenseText, textToCheck});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (webservices.sc._WSpellCheckResponse_WSpellCheckResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (webservices.sc._WSpellCheckResponse_WSpellCheckResult) org.apache.axis.utils.JavaUtils.convert(_resp, webservices.sc._WSpellCheckResponse_WSpellCheckResult.class);
            }
        }
    }

}
