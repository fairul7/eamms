/**
 * BabelFishService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.babelfish;

public interface BabelFishService extends javax.xml.rpc.Service {

    // Translates text of up to 5k in length, between a variety of languages.
    public java.lang.String getBabelFishPortAddress();

    public webservices.babelfish.BabelFishPortType getBabelFishPort() throws javax.xml.rpc.ServiceException;

    public webservices.babelfish.BabelFishPortType getBabelFishPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
