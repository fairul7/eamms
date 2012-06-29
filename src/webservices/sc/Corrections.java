/**
 * Corrections.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package webservices.sc;

public class Corrections  implements java.io.Serializable {
    private webservices.sc.CorrectionsCorrection[] correction;

    public Corrections() {
    }

    public webservices.sc.CorrectionsCorrection[] getCorrection() {
        return correction;
    }

    public void setCorrection(webservices.sc.CorrectionsCorrection[] correction) {
        this.correction = correction;
    }

    public webservices.sc.CorrectionsCorrection getCorrection(int i) {
        return correction[i];
    }

    public void setCorrection(int i, webservices.sc.CorrectionsCorrection value) {
        this.correction[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Corrections)) return false;
        Corrections other = (Corrections) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.correction==null && other.getCorrection()==null) || 
             (this.correction!=null &&
              java.util.Arrays.equals(this.correction, other.getCorrection())));
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
        if (getCorrection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCorrection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCorrection(), i);
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
        new org.apache.axis.description.TypeDesc(Corrections.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "corrections"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "correction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.worldwidedesktop.com/spellcheck/SpellCheckResult.xsd", "correctionsCorrection"));
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
