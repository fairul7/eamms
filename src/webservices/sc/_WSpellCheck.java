/**
 * _WSpellCheck.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class _WSpellCheck  implements java.io.Serializable {
    private java.lang.String licenseText;
    private java.lang.String textToCheck;

    public _WSpellCheck() {
    }

    public java.lang.String getLicenseText() {
        return licenseText;
    }

    public void setLicenseText(java.lang.String licenseText) {
        this.licenseText = licenseText;
    }

    public java.lang.String getTextToCheck() {
        return textToCheck;
    }

    public void setTextToCheck(java.lang.String textToCheck) {
        this.textToCheck = textToCheck;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _WSpellCheck)) return false;
        _WSpellCheck other = (_WSpellCheck) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.licenseText==null && other.getLicenseText()==null) || 
             (this.licenseText!=null &&
              this.licenseText.equals(other.getLicenseText()))) &&
            ((this.textToCheck==null && other.getTextToCheck()==null) || 
             (this.textToCheck!=null &&
              this.textToCheck.equals(other.getTextToCheck())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLicenseText() != null) {
            _hashCode += getLicenseText().hashCode();
        }
        if (getTextToCheck() != null) {
            _hashCode += getTextToCheck().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_WSpellCheck.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", ">WSpellCheck"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("licenseText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "LicenseText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textToCheck");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck", "TextToCheck"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
