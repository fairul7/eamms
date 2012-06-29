/**
 * SpellCheckServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public interface SpellCheckServiceSoap extends java.rmi.Remote {

    // Spell check a word or multiple words
    public webservices.sc.Corrections spellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException;

    // Spell check a word or multiple words (Works best with MSSOAP 2.0)
    public webservices.sc._MSSpellCheckResponse_MSSpellCheckResult MSSpellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException;
    public webservices.sc._WSpellCheckResponse_WSpellCheckResult WSpellCheck(java.lang.String licenseText, java.lang.String textToCheck) throws java.rmi.RemoteException;
}
