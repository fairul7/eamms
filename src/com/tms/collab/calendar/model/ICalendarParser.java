package com.tms.collab.calendar.model;

import com.tms.collab.messaging.model.Message;
import com.tms.collab.messaging.model.MessagingException;
import com.tms.collab.messaging.model.MessagingModule;
import com.tms.collab.util.ICalendarUtil;
import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageException;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;

import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class ICalendarParser {

    private String method;

    private String uid;

    private String version;

    private String calscale;

    private String tzId;

    private String tzOffsetFrom;

    private String tzOffsetTo;

    private String tzName;

    private Map EventProperties = new HashMap();

    public ICalendarParser() {
        EventProperties.put("UID", "externalEventId");
        EventProperties.put("DTSTART", "startDate");
        EventProperties.put("DTEND", "endDate");
        EventProperties.put("CREATED", "creationDate");
        EventProperties.put("SUMMARY", "title");
        EventProperties.put("DESCRIPTION", "description");
        EventProperties.put("ORGANIZER", "organizerEmail");
        EventProperties.put("LOCATION", "location");
        EventProperties.put("STATUS", "status");
        EventProperties.put("CLASS", "classification");
        EventProperties.put("LAST-MODIFIED", "lastModified");
        EventProperties.put("CN", "firstName");
        EventProperties.put("PARTSTAT", "status");
    }

    public void parseRequest(InputStream fin, CalendarEvent event, String userId)
            throws IOException {
        CalendarBuilder builder = new CalendarBuilder();
        try {
            Calendar calendar = builder.build(fin);
            String method = calendar.getMethod().toString();
            if (method != null) {
                String[] temp = method.split(":");
                setMethod(temp[1]);
            }
            for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
                Component component = (Component) i.next();
                for (Iterator j = component.getProperties().iterator(); j
                        .hasNext();) {
                    Property property = (Property) j.next();
                    String prop = property.getName();
                    String key = (String) EventProperties
                            .get(property.getName());
                    if (key != null) {
                        String value = "";
                        if (prop.equals("DTSTART") || prop.equals("DTEND")
                                || prop.equals("CREATED")
                                || prop.equals("LAST-MODIFIED")) {
                            Date localDate = ICalendarUtil.convertToMysqlDate(property
                                    .getValue().toString());
                            org.apache.commons.beanutils.PropertyUtils
                                    .setProperty(event, key, localDate);
                        } else if (prop.equals("CLASS")) {
                            value = CalendarModule.CLASSIFICATION_PRIVATE;
                            org.apache.commons.beanutils.PropertyUtils
                                    .setProperty(event, key, value);
                        } else if (prop.equals("ORGANIZER")) {
                            value = property.getValue().toString();
                            if (value != null) {
                                try {
                                    String[] email1 = value.split(":");
                                    value = email1[1];
                                } catch (Exception e) {
                                }
                                org.apache.commons.beanutils.PropertyUtils
                                        .setProperty(event, key, value);
                            }
                        } else {
                            value = property.getValue().toString();
                            org.apache.commons.beanutils.PropertyUtils
                                    .setProperty(event, key, value);
                        }
                    }
                }
                // To retrive Attendees
                Collection attendeeList = new TreeSet();
                Application app = Application.getInstance();
                SecurityService security = (SecurityService) app
                        .getService(SecurityService.class);
                User user = (User) security.getUser(userId);
                String userEmail = (String) user.getProperty("email1");
                for (Iterator j = component.getProperties("ATTENDEE")
                        .iterator(); j.hasNext();) {
                    Property property = (Property) j.next();
                    String tempEmail = property.getValue();
                    try {
                        Attendee att = new Attendee();
                        String email = "";
                        try {
                            String[] email1 = tempEmail.split(":");
                            email = email1[1];
                        } catch (Exception e) {
                        }

                        if (userEmail.equals(email)) {
                            att.setUserId(userId);
                            att.setProperty("username", user.getUsername());
                            att.setProperty("firstName", user
                                    .getProperty("firstName"));
                            att.setProperty("email", userEmail);
                            att.setEkpUser(true);
                        } else {
                            att.setProperty("username", email);
                            att.setProperty("firstName", email);
                            att.setProperty("email", email);
                            att.setUserId(email);
                            att.setEkpUser(false);
                        }
                         for (Iterator p = property.getParameters().iterator(); p.hasNext();) {
                            Parameter param = (Parameter) p.next();
                            if (param.getName().equalsIgnoreCase("CN")) {
                                att.setProperty("firstName", param.getValue());
                            } else if (param.getName().equalsIgnoreCase("PARTSTAT")) {
                                String status = "";
                                if (param.getValue().equalsIgnoreCase("ACCEPTED")) {
                                    status = CalendarModule.ATTENDEE_STATUS_CONFIRMED;
                                } else if (param.getValue().equalsIgnoreCase("DECLINED")) {
                                    status = CalendarModule.ATTENDEE_STATUS_REJECT;
                                } else {
                                    status = CalendarModule.ATTENDEE_STATUS_PENDING;
                                }
                                att.setStatus(status);
                            }
                        }
                        att.setCompulsory(false);
                        attendeeList.add(att);
                    } catch (Exception e) {
                    }
                }
                event.setAttendees(attendeeList);
                event.setEkpEvent(false);
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error("Error parsing iCalendar file", e);
        }
    }

    public CalendarEvent getICalendarFile(String messageId, String userId)
            throws MessagingException, IOException, SecurityException,
            ParserException {

        MessagingModule mm = (MessagingModule) Application.getInstance()
                .getModule(MessagingModule.class);
        Message message = mm.getMessageByMessageId(messageId);
        MimeMessage mimeMessage;
        StorageService ss;
        StorageFile sf;
        InputStream in = null;
        CalendarEvent event = new CalendarEvent();
        try {

            try {
                ss = (StorageService) Application.getInstance().getService(
                        StorageService.class);
                sf = new StorageFile(message.getStorageFilename());
                sf = ss.get(sf);

                in = sf.getInputStream();
                mimeMessage = new MimeMessage(Session
                        .getDefaultInstance(new Properties()), in);
                mimeMessage.getContentType();
                if (message.getAttachmentCount() > 0) {
                    Multipart multipart = (Multipart) mimeMessage.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        Part part = multipart.getBodyPart(i);
                        String fname = part.getFileName();
                        try {
                            if (fname != null)
                                parseRequest(message.getAttachmentInputStream(fname, 1), event, userId);
                        } catch (Exception e) {
                        }
                    }
                } else {
                    try {
                        //parseRequest(mimeMessage.getInputStream(), event, userId);
                        //trying to get msg body into stream
                        String input = message.getBody().trim();
                        //first replacement for Windows line break implementation. second for UNIX 
                        input = StringUtils.replace(input, "\r\n\t", "");
                        input = StringUtils.replace(input, "\n\t", "");
                        InputStream inp = new ByteArrayInputStream(input.getBytes());
                        parseRequest(inp, event, userId);
                    } catch (Exception e) {
                    }
                }
            } catch (FileNotFoundException e) {
                Log.getLog(getClass()).error(e);
            }
        } catch (javax.mail.MessagingException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (StorageException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (IOException e) {
            throw new MessagingException(e.getMessage(), e);
        } catch (Exception e) {
            throw new MessagingException(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return event;
    }

    public String getCalscale() {
        return calscale;
    }

    public void setCalscale(String calscale) {
        this.calscale = calscale;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTzId() {
        return tzId;
    }

    public void setTzId(String tzId) {
        this.tzId = tzId;
    }

    public String getTzName() {
        return tzName;
    }

    public void setTzName(String tzName) {
        this.tzName = tzName;
    }

    public String getTzOffsetFrom() {
        return tzOffsetFrom;
    }

    public void setTzOffsetFrom(String tzOffsetFrom) {
        this.tzOffsetFrom = tzOffsetFrom;
    }

    public String getTzOffsetTo() {
        return tzOffsetTo;
    }

    public void setTzOffsetTo(String tzOffsetTo) {
        this.tzOffsetTo = tzOffsetTo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}