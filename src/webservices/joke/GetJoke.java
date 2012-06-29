/**
 * GetJoke.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.joke;

public interface GetJoke extends javax.xml.rpc.Service {

    // <a href='http://interpressfact.net'><img src='http://www.interpressfact.net/webpage.files/logo.gif'
    // border='0'></a><br><font color='#006600' size='3' face='Geneva, Arial,
    // Helvetica, san-serif'>Returns random joke every time this webservice
    // is invoked.</font>
    public java.lang.String getgetJokeSoapAddress();

    public webservices.joke.GetJokeSoap getgetJokeSoap() throws javax.xml.rpc.ServiceException;

    public webservices.joke.GetJokeSoap getgetJokeSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
