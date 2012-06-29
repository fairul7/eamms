package com.tms.tmsPIMSync;

import com.tms.collab.directory.model.Contact;
import com.funambol.foundation.pdi.contact.SIFC;
import com.funambol.foundation.pdi.parser.XMLContactParser;
import com.funambol.foundation.pdi.converter.ConverterException;
import com.funambol.foundation.pdi.converter.Converter;
import com.funambol.foundation.pdi.utils.SourceUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import kacang.util.Log;
import kacang.stdui.CountrySelectBox;

/**
 * Convert EKP Contact to SIF-C XML format. SIF-C is the standard XML format which sync4j use.
 */
public class VCardUtils implements SIFC {
    public static final String EXTENSION_PREFIX = "--[ Phone Extension:";
    public static final String EXTENSION_SUFFIX = " ]--";
    public static final String LAST_MODIFIED = "date_modified";
    public static final String DATE_ENTERED = "date_entered";
    private static String[][] countries = CountrySelectBox.COUNTRIES;
    public static DateFormat format;
    static{
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Map ekpContactToHashMap(Contact con){
        Map map = new HashMap();
        map.put(SourceUtils.ROOT_NAME, ROOT_TAG);

        if(con.getTitle()!=null) map.put(TITLE, con.getTitle());
        if(con.getFirstName()!=null) map.put(FIRST_NAME, con.getFirstName());
        if(con.getMiddleName()!=null) map.put(MIDDLE_NAME, con.getMiddleName());
        if(con.getLastName()!=null) map.put(LAST_NAME, con.getLastName());
        if(con.getNickName()!=null) map.put(NICK_NAME, con.getNickName());
        if(con.getEmail()!=null) map.put(EMAIL1_ADDRESS, con.getEmail());
        if(con.getMobile()!=null){
            map.put(MOBILE_TELEPHONE_NUMBER, con.getMobile());
            /*map.put(MOBILE_BUSINESS_TELEPHONE_NUMBER, con.getMobile());
            map.put(MOBILE_HOME_TELEPHONE_NUMBER, con.getMobile());*/
        }
        if(con.getDesignation()!=null) map.put(JOB_TITLE, con.getDesignation());
        if(con.getCompany()!=null) map.put(COMPANY_NAME, con.getCompany());
        if(con.getAddress()!=null) map.put(HOME_ADDRESS_STREET, con.getAddress());
        if(con.getCity()!=null) map.put(HOME_ADDRESS_CITY, con.getCity());
        if(con.getState()!=null) map.put(HOME_ADDRESS_STATE, con.getState());
        if(con.getPostcode()!=null) map.put(HOME_ADDRESS_POSTAL_CODE, con.getPostcode());
        if(con.getCountry()!=null && !con.getCountry().equals("-1")) map.put(HOME_ADDRESS_COUNTRY, getCountryNameByCode(con.getCountry()));
        if(con.getPhone()!=null) map.put(HOME_TELEPHONE_NUMBER, con.getPhone());
        if(con.getFax()!=null){
            map.put(BUSINESS_FAX_NUMBER, con.getFax());
            /*map.put(HOME_FAX_NUMBER, con.getFax());
            map.put(OTHER_FAX_NUMBER, con.getFax());*/
        }
        if(con.getExtension()!=null && con.getExtension().length() > 0){
            con.setComments(notesExtensionGenerator(con.getComments(), con.getExtension()));
        }

        if(con.getComments()!=null) map.put(BODY, con.getComments());
        if(con.getId()!=null) map.put(UID, con.getId());
        return map;
    }

    private String hashMapToXml(Map map) throws Exception {
        String xml = SourceUtils.hashMapToXml(map);
        return xml;
    }

    private String XmlToVCard(String xmlString) throws IOException {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));

            XMLContactParser parser = new XMLContactParser(in);
            com.funambol.foundation.pdi.contact.Contact contact = parser.parse();
            Converter converter = new Converter("GMT", "UTF-8");
            return converter.contactToVcard(contact);
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ConverterException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }finally{
            if(in!=null){
                in.close();
            }
        }
        return null;
    }

    public String ekpContactToVCard(Contact con) throws Exception {
        Map map = ekpContactToHashMap(con);
        String xml = hashMapToXml(map);
        return XmlToVCard(xml);
    }

    public String ekpContactToXml(Contact con) throws Exception {
        Log.getLog(VCardUtils.class).info("ekpContactToXml: " + con.getFirstName());
        Map map = ekpContactToHashMap(con);
        String xml = hashMapToXml(map);
        return xml;
    }

    /**
     * Get Data from XML message
     * converting the xml item into a Contact object
     *
     * the contact object is a Contact
     *
     * @param content String
     * @return Contact
     */
    public com.funambol.foundation.pdi.contact.Contact getFoundationContactFromXML(String content) {

        ByteArrayInputStream buffer = null;
        XMLContactParser parser     = null;
        com.funambol.foundation.pdi.contact.Contact contact = null;
        try {
            contact = null;
            buffer  = new ByteArrayInputStream(content.getBytes());
            if ((content.getBytes()).length > 0) {
                parser = new XMLContactParser(buffer);
                contact = parser.parse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    public com.funambol.foundation.pdi.contact.Contact ekpContactToFoundationContact(Contact ekpCon) throws Exception {
        Map map = ekpContactToHashMap(ekpCon);
        String xml = hashMapToXml(map);
        return getFoundationContactFromXML(xml);
    }

    public String[] ekpContactToXml(Collection contacts) throws Exception {
        String[] xmls = new String[contacts.size()];
        int i = 0;
        for(Iterator itr = contacts.iterator(); itr.hasNext();i++){
            Map map = ekpContactToHashMap((Contact) itr.next());
            String xml = hashMapToXml(map);
            xmls[i] = xml;
        }
        return xmls;
    }

    public Contact foundationContactToEkpContact(com.funambol.foundation.pdi.contact.Contact contact) throws IOException, ParserConfigurationException, ConverterException, SAXException {
        Contact ekpCon = null;
        Map map = foundationContactsToHashMap(contact);
        ekpCon = hashMapToEkpContact(map);
        return ekpCon;
    }

    private Map foundationContactsToHashMap(com.funambol.foundation.pdi.contact.Contact contact) throws ConverterException, IOException, ParserConfigurationException, SAXException {
        Converter converter = new Converter("GMT", "UTF-8");
        String xml = converter.contactToXML(contact);
        return SourceUtils.xmlToHashMap(xml);
    }

     public String foundationContactsToXml(com.funambol.foundation.pdi.contact.Contact contact) throws ConverterException, IOException, ParserConfigurationException, SAXException {
        Converter converter = new Converter("GMT", "UTF-8");
        String xml = converter.contactToXML(contact);
        return xml;
    }

    private Contact hashMapToEkpContact(Map map){
        Contact contact = new Contact();
        if(map.containsKey(TITLE) && map.get(TITLE).toString().length() != 0) contact.setTitle((String) map.get(TITLE));
        if(map.containsKey(FIRST_NAME) && map.get(FIRST_NAME).toString().length() != 0) contact.setFirstName((String) map.get(FIRST_NAME));
        if(map.containsKey(MIDDLE_NAME) && map.get(MIDDLE_NAME).toString().length() != 0) contact.setMiddleName((String) map.get(MIDDLE_NAME));
        if(map.containsKey(LAST_NAME) && map.get(LAST_NAME).toString().length() != 0) contact.setLastName((String) map.get(LAST_NAME));
        if(map.containsKey(NICK_NAME) && map.get(NICK_NAME).toString().length() != 0) contact.setNickName((String) map.get(NICK_NAME));
        if(map.containsKey(EMAIL1_ADDRESS) && map.get(EMAIL1_ADDRESS).toString().length() != 0) contact.setEmail((String) map.get(EMAIL1_ADDRESS));

        if(map.containsKey(MOBILE_BUSINESS_TELEPHONE_NUMBER) && map.get(MOBILE_BUSINESS_TELEPHONE_NUMBER).toString().length() != 0) contact.setMobile((String) map.get(MOBILE_BUSINESS_TELEPHONE_NUMBER));
        else if(map.containsKey(MOBILE_TELEPHONE_NUMBER) && map.get(MOBILE_TELEPHONE_NUMBER).toString().length() != 0) contact.setMobile((String) map.get(MOBILE_TELEPHONE_NUMBER));
        else if(map.containsKey(MOBILE_HOME_TELEPHONE_NUMBER) && map.get(MOBILE_HOME_TELEPHONE_NUMBER).toString().length() != 0) contact.setMobile((String) map.get(MOBILE_HOME_TELEPHONE_NUMBER));

        if(map.containsKey(JOB_TITLE) && map.get(JOB_TITLE).toString().length() != 0) contact.setDesignation((String) map.get(JOB_TITLE));
        if(map.containsKey(COMPANY_NAME) && map.get(COMPANY_NAME).toString().length() != 0) contact.setCompany((String) map.get(COMPANY_NAME));
        if(map.containsKey(HOME_ADDRESS_STREET) && map.get(HOME_ADDRESS_STREET).toString().length() != 0) contact.setAddress((String) map.get(HOME_ADDRESS_STREET));
        if(map.containsKey(HOME_ADDRESS_CITY) && map.get(HOME_ADDRESS_CITY).toString().length() != 0) contact.setCity((String) map.get(HOME_ADDRESS_CITY));
        if(map.containsKey(HOME_ADDRESS_STATE) && map.get(HOME_ADDRESS_STATE).toString().length() != 0) contact.setState((String) map.get(HOME_ADDRESS_STATE));
        if(map.containsKey(HOME_ADDRESS_POSTAL_CODE) && map.get(HOME_ADDRESS_POSTAL_CODE).toString().length() != 0) contact.setPostcode((String) map.get(HOME_ADDRESS_POSTAL_CODE));
        if(map.containsKey(HOME_ADDRESS_COUNTRY) && map.get(HOME_ADDRESS_COUNTRY).toString().length() != 0) contact.setCountry(getCountryCodeByName((String) map.get(HOME_ADDRESS_COUNTRY)));
        if(map.containsKey(BODY) && map.get(BODY).toString().length() != 0) contact.setComments((String) map.get(BODY));
        if(map.containsKey(HOME_TELEPHONE_NUMBER) && map.get(HOME_TELEPHONE_NUMBER).toString().length() != 0) contact.setPhone((String) map.get(HOME_TELEPHONE_NUMBER));
        else if(map.containsKey(BUSINESS_TELEPHONE_NUMBER) && map.get(BUSINESS_TELEPHONE_NUMBER).toString().length() != 0) contact.setPhone((String) map.get(BUSINESS_TELEPHONE_NUMBER));

        //check for ext...
        if(contact.getPhone() != null && contact.getPhone().length() > 0 && contact.getPhone().indexOf("x") != -1){
            String number = contact.getPhone();
            String ext = number.substring(number.indexOf("x")+1, number.length()).trim();
            contact.setExtension(ext);

            //remove "x XXX" cause mobile phone will not be able to call
            number = number.substring(0, number.indexOf("x")-1).trim();
            contact.setPhone(number);

            contact.setComments(notesExtensionGenerator(contact.getComments(), contact.getExtension()));
            if(contact.getComments()!=null){
                if(contact.getComments().startsWith("Extension: " + contact.getExtension())){
                    contact.setComments("Extension: " + contact.getExtension() + contact.getComments());
                }
            }else{
                contact.setComments("Extension: " + contact.getExtension());
            }
        }else{
            if(getExtensionFromNotes(contact.getComments())!=null) contact.setExtension(getExtensionFromNotes(contact.getComments()));
        }


        if(map.containsKey(HOME_FAX_NUMBER) && map.get(HOME_FAX_NUMBER).toString().length() != 0) contact.setFax((String) map.get(HOME_FAX_NUMBER));
        else if(map.containsKey(BUSINESS_FAX_NUMBER) && map.get(BUSINESS_FAX_NUMBER).toString().length() != 0) contact.setFax((String) map.get(BUSINESS_FAX_NUMBER));

        if(map.containsKey(UID) && map.get(UID).toString().length() != 0) contact.setId((String) map.get(UID));
        return contact;
    }

    /*
    * Converts time from UTC/GMT TimeZone to default (server) timezone.
    *
    */
    public String toServerTime(String time)
    {
        final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

        Date specifiedTime;
        try {
            specifiedTime = format.parse(time);
        }
        catch (Exception e1) {
            return time;
        }

        // switch timezone
        sdf.setTimeZone(TimeZone.getDefault()); // default to server's timezone

        return sdf.format(specifiedTime);
    }

    private String getCountryNameByCode(String code){
        for(int i=0; i < countries.length; i++){
            if(countries[i][0].equals(code)){
                return countries[i][1];
            }
        }
        // if code cant be found, return code
        return code;
    }

    private String getCountryCodeByName(String name){
        for(int i=0; i < countries.length; i++){
            if(countries[i][1].equals(name)){
                return countries[i][0];
            }
        }
        // if name cant be found return name
        return name;
    }


    /**
     * Check notes for existing extension
     * Replace if needed
     *
     * @param notes
     * @param extension
     * @return
     */
    public String notesExtensionGenerator(String notes, String extension){
        notes = (notes == null) ? "" : notes;
        String newNotes;
        int start = notes.indexOf(EXTENSION_PREFIX);
        int end = notes.indexOf(EXTENSION_SUFFIX);

        // if extension exist
        // replace
        if(start != -1 && end != -1){
            newNotes = notes.substring(end + EXTENSION_SUFFIX.length(), notes.length());
            newNotes = EXTENSION_PREFIX + extension + EXTENSION_SUFFIX + newNotes;
        }else {
            newNotes = EXTENSION_PREFIX + extension + EXTENSION_SUFFIX + "\n" + notes;
        }
        return newNotes;
    }

    public String getExtensionFromNotes(String notes){
        if(notes == null  || "".equals(notes)){
            return null;
        }
        String extension;
        int start = notes.indexOf(EXTENSION_PREFIX);
        int end = notes.indexOf(EXTENSION_SUFFIX);

        if(start != -1 && end != -1){
            extension = notes.substring(start + EXTENSION_PREFIX.length(), end);
        }else return null;
        return extension;
    }



}
