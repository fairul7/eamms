/**
 * ArrayOfString.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class ArrayOfString  implements java.io.Serializable {
    private java.lang.String[] suggestion;

    public ArrayOfString() {
    }

    public java.lang.String[] getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(java.lang.String[] suggestion) {
        this.suggestion = suggestion;
    }

    public java.lang.String getSuggestion(int i) {
        return suggestion[i];
    }

    public void setSuggestion(int i, java.lang.String value) {
        this.suggestion[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfString)) return false;
        ArrayOfString other = (ArrayOfString) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.suggestion==null && other.getSuggestion()==null) || 
             (this.suggestion!=null &&
              java.util.Arrays.equals(this.suggestion, other.getSuggestion())));
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
        if (getSuggestion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSuggestion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSuggestion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfString.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "ArrayOfString"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suggestion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "suggestion"));
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
