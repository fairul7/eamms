/**
 * CorrectionsCorrection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class CorrectionsCorrection  implements java.io.Serializable {
    private java.lang.String word;
    private webservices.sc.ArrayOfString suggestions;

    public CorrectionsCorrection() {
    }

    public java.lang.String getWord() {
        return word;
    }

    public void setWord(java.lang.String word) {
        this.word = word;
    }

    public webservices.sc.ArrayOfString getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(webservices.sc.ArrayOfString suggestions) {
        this.suggestions = suggestions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CorrectionsCorrection)) return false;
        CorrectionsCorrection other = (CorrectionsCorrection) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.word==null && other.getWord()==null) || 
             (this.word!=null &&
              this.word.equals(other.getWord()))) &&
            ((this.suggestions==null && other.getSuggestions()==null) || 
             (this.suggestions!=null &&
              this.suggestions.equals(other.getSuggestions())));
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
        if (getWord() != null) {
            _hashCode += getWord().hashCode();
        }
        if (getSuggestions() != null) {
            _hashCode += getSuggestions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CorrectionsCorrection.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "correctionsCorrection"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("word");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "word"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suggestions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "suggestions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "ArrayOfString"));
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
