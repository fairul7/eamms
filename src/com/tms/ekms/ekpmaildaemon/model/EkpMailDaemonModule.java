package com.tms.ekms.ekpmaildaemon.model;

import com.tms.ekms.ekpmaildaemon.model.stub.MailStub;

import kacang.Application;

import kacang.model.DaoQuery;
import kacang.model.DefaultModule;

import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;

import kacang.services.scheduling.JobSchedule;
import kacang.services.scheduling.JobTask;
import kacang.services.scheduling.SchedulingException;
import kacang.services.scheduling.SchedulingService;

import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;

import kacang.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EkpMailDaemonModule extends DefaultModule {
    public static final String EKPMAILDAEMON_NOTIFICATION = "ekpmail_daemon";
    public static final String STUB_PROPERTY = "ekpmaildaemon.allowed.classes";
    public static final String MAIL_USERNAME = "ekpmaildaemon.mail.account.name";
    public static final String MAIL_PASSWORD = "ekpmaildaemon.mail.password";
    public static final String MAIL_STMP = "ekpmaildaemon.mail.smtp";
    private Map stubMap;
    public MailStub stub;
    private boolean INVALID_CONTENT = false;
    String entireRawContent = "";
    Object o = null;
    Object o2 = null;
    boolean globalishtml;

    public void init() {
        //job start
        JobSchedule schedule = new JobSchedule(getClass().getName(),
                JobSchedule.MINUTELY);
        schedule.setGroup(EKPMAILDAEMON_NOTIFICATION);
        schedule.setRepeatCount(JobSchedule.REPEAT_INDEFINITELY);

        JobTask task = new EkpMailDaemon();
        task.setName(getClass().getName());
        task.setGroup(EKPMAILDAEMON_NOTIFICATION);
        task.setDescription("EKPMailModule Runner");

        SchedulingService service = (SchedulingService) Application.getInstance()
                                                                   .getService(SchedulingService.class);

        try {
            service.deleteJobTask(task);
            service.scheduleJob(task, schedule);
        } catch (SchedulingException e) {
            Log.getLog(getClass()).error("Error removing task:" +
                task.getName(), e);
        }

        //job end
        // pre define stub classes  into Map
        stubMap = new HashMap();

        String configured = Application.getInstance().getProperty(STUB_PROPERTY);

        if (!((configured == null) || "".equals(configured))) {
            StringTokenizer tokenizer = new StringTokenizer(configured, ",");

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                try {
                    stubMap.put(token, Class.forName(token).newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    Log.getLog(getClass()).info("can't find the stub files" +
                        e.toString(), e);
                }
            }
        }

        // for testing purpose start
        //  EkpMailDaemon ekpmd = new EkpMailDaemon(); //this line not important cause already declared in above job
        //  ekpmd.do_testing_purpose();
        //for testing purpose end
    }

    public void processMail(EkpMailDaemonDataObject ekpmddo)
        throws MessagingException {
        //process mail, iterate thru stubs and do whatever
        Application app = Application.getInstance();
        boolean ishtml;
        
        String configured = Application.getInstance().getProperty(MAIL_USERNAME);

        for (int i = 0, n = ekpmddo.getMessage().length; i < n; i++) {
            String tempBodyContent = "";

            String email = ekpmddo.getMessage()[i].getFrom()[0].toString();

            int checkhtml = ekpmddo.getMessage()[i].getContentType().toString()
                                                   .indexOf("text/html");

            if (checkhtml < 0) {
                ishtml = false;
            } else {
                ishtml = true;
            }

            globalishtml = ishtml;

            boolean invalidSubject = false;

            //filter email
            if ((email != null) && !("".equals(email))) {
                int startPoint = email.indexOf("<");
                int endPoint = email.indexOf(">", startPoint + 1);

                if (!((startPoint < 0) && (endPoint < 0))) {
                    email = email.substring(startPoint + 1, endPoint)
                                 .replaceAll(" ", "").trim();
                }
            }

            if (!email.equalsIgnoreCase("")) {
                SecurityService ss = (SecurityService) Application.getInstance()
                                                                  .getService(SecurityService.class);
                User user = null;
                DaoQuery query = new DaoQuery();

                query.addProperty(new OperatorEquals("email1", email,
                        DaoOperator.OPERATOR_AND));

                Collection userCollection = null;

                try {
                    userCollection = ss.getUsers(query, 0, -1, "username", false);

                    for (Iterator icount = userCollection.iterator();
                            icount.hasNext();) {
                        user = (User) icount.next();
                    }

                    if (user == null) {
                        //user cant be found in database
                        
                    /*
                     * prevent self sending to self,  for instance test2@tmsasia.com send to test2@tmsasisa.com
                     */	 
                    	
                    	 if (!((configured == null) || "".equals(configured))) {
                             StringTokenizer tokenizer = new StringTokenizer(configured, ",");

                             while (tokenizer.hasMoreTokens()) {
                            	 String email_from="";
                                 email_from = tokenizer.nextToken();
                                 
                                 
                                 if(!email.equalsIgnoreCase(email_from)){	
                                 	if (!ishtml) {
                                         sendMail(email,
                                             Application.getInstance().getMessage("maildaemon.label.error.invalidSubject"),
                                             "\n" +
                                             Application.getInstance().getMessage("maildaemon.label.error.invalidBody"),
                                             ishtml);
                                     } else {
                                         sendMail(email,
                                             Application.getInstance().getMessage("maildaemon.label.error.invalidSubject"),
                                             "<br>" +
                                             Application.getInstance().getMessage("maildaemon.label.error.invalidBody"),
                                             ishtml);
                                     }
                                 }  
                                 
                             }
                         }	
                    	
                    	
                    	
                 
                        
                        
                        
                    }

                    if (user != null) {
                        String tempSubject = ekpmddo.getMessage()[i].getSubject();

                        if (app.getMessage("maildaemon.label.request")
                                   .equalsIgnoreCase(tempSubject)) {
                            //for each blank email, send all the templates from stub
                            replyMessage(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                                true, "", ishtml);
                        } else if (!("".equals(tempSubject)) &&
                                (ekpmddo.getMessage()[i].getSubject() != null) &&
                                !(app.getMessage("maildaemon.label.request")
                                         .equalsIgnoreCase(tempSubject))) {
                            int startPoint = tempSubject.indexOf("[");
                            int endPoint = tempSubject.indexOf("]",
                                    startPoint + 1);

                            try {
                                //compare by trim, ignore case, and cancel out all space
                                tempSubject = tempSubject.substring(startPoint +
                                        1, endPoint).replaceAll(" ", "").trim();
                            } catch (StringIndexOutOfBoundsException e) {
                                // no subject match found here
                            }

                            for (Iterator it = stubMap.keySet().iterator();
                                    it.hasNext();) {
                                //assign stubmap <Map> to stub <MailStub> object
                                String className = (String) it.next();

                                stub = (MailStub) stubMap.get(className);

                                //do proper filtering of subject from email first
                                //TODO: Maybe we can use regular expressions
                                if (tempSubject.equalsIgnoreCase(
                                            stub.getSubjectPattern()
                                                    .replaceAll(" ", "").trim())) {
                                    invalidSubject = true;

                                    try {
                                        o = ekpmddo.getMessage()[i].getContent();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (o instanceof String) {
                                        //where the content of email is plain text only, without attachment
                                        doProcessing(ekpmddo, i, user, null,
                                            null, null, null, null, 0);
                                    } //check content
                                    else if (o instanceof Multipart) {
                                        //debug//System.out.print("**This is a Multipart Message.  ");
                                        Multipart mp = (Multipart) o;
                                        int count3 = mp.getCount(); //when count3 >=2 mean got attachment

                                        for (int j = 0; j < count3; j++) {
                                            String fileName = "";
                                            String fileType = "";
                                            long fileSize = 0;

                                            // Part are numbered starting at 0
                                            BodyPart b = mp.getBodyPart(j);
                                            fileSize = mp.getBodyPart(j)
                                                         .getSize();

                                            String mimeType2 = b.getContentType();

                                            //debug//System.out.println("BodyPart " + (j + 1) +
                                            //debug// " is of MimeType " + mimeType2);
                                            //check the header for attachment , pattern "name=file.txt"
                                            if (!("".equals(mimeType2))) {
                                                int point = mimeType2.indexOf(";",
                                                        0);

                                                fileType = mimeType2.substring(0,
                                                        point);

                                                int point1 = mimeType2.indexOf("\"",
                                                        point + 1);
                                                int point2 = mimeType2.indexOf("\"",
                                                        point1 + 1);

                                                if (!(point1 < 0) &&
                                                        !(point2 < 0)) {
                                                    fileName = mimeType2.substring(point1 +
                                                            1, point2);
                                                }

                                                if (fileName.equals("")) {
                                                    int point1Tmp = mimeType2.indexOf(
                                                            "name=");
                                                    int point2Tmp = mimeType2.indexOf(";",
                                                            point1Tmp + 1);

                                                    if (!(point1Tmp < 0) &&
                                                            !(point2Tmp < 0)) {
                                                        fileName = mimeType2.substring(point1Tmp +
                                                                1, point2Tmp);
                                                    }
                                                }
                                            }

                                            try {
                                                o2 = b.getContent();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            if (o2 instanceof String) {
                                                // for plain text attachment, for instance,txt
                                                //debug//System.out.println(
                                                //debug//    "**This is a String BodyPart**");
                                                if ((j + 1) >= count3) {
                                                    //o2 is the plain text attachment
                                                    doProcessing(ekpmddo, i,
                                                        user, fileName,
                                                        tempBodyContent,
                                                        (String) o2, null,
                                                        fileType, fileSize);

                                                    /*   backup old version
                                                      doProcessing(ekpmddo, i,
                                                                   user, fileName,
                                                                   tempBodyContent,
                                                                   (String) o2, null,
                                                                    null, 0);*/
                                                }

                                                if (j == 0) {
                                                    tempBodyContent += (String) o2;
                                                }
                                            } else if (o2 instanceof Multipart) {
                                                //debug//System.out.print(
                                                //debug// "**This BodyPart is a nested Multipart.  ");
                                                Multipart mp2 = (Multipart) o2;

                                                //for HTML email
                                                if ((j + 1) >= count3) {
                                                    //o2 is the plain text attachment
                                                    doProcessing(ekpmddo, i,
                                                        user, fileName,
                                                        tempBodyContent,
                                                        (String) o2, null,
                                                        fileType, fileSize);

                                                    //process binary attachment
                                                }

                                                if (j == 0) {
                                                    ByteArrayOutputStream pout = new ByteArrayOutputStream();

                                                    try {
                                                        mp2.writeTo(pout);

                                                        tempBodyContent += pout.toString();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else if (o2 instanceof InputStream) {
                                                //debug//System.out.println(
                                                //debug// "**This is an InputStream BodyPart**");
                                                //this is for binary attachment
                                                if ((j + 1) >= count3) {
                                                    InputStream is = (InputStream) o2;

                                                    doProcessing(ekpmddo, i,
                                                        user, fileName,
                                                        tempBodyContent, null,
                                                        is, fileType, fileSize); //pout is the binary attachment
                                                }
                                            }
                                        }

                                        //End of for
                                    } else if (o instanceof InputStream) {
                                        //debug//System.out.println(
                                        //debug//"**This is an InputStream message**");
                                        InputStream is = (InputStream) o;

                                        // Assumes character content (not binary images)
                                        int c;

                                        try {
                                            while ((c = is.read()) != -1) {
                                                System.out.write(c);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    //subject not match  for each stub,  so this place will be loop many times
                                    //workaround to only send 1 error msg.
                                    if (!it.hasNext() && !invalidSubject) {
                                        try {
                                            o = ekpmddo.getMessage()[i].getContent();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (o instanceof String) {
                                            if (!ishtml) {
                                                sendMail(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                                                    "Invalid Request",
                                                    "Invalid Content  \n>>>>>>>>>>\n" +
                                                    (String) o.toString(),
                                                    ishtml);
                                            } else {
                                                sendMail(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                                                    "Invalid Request",
                                                    "Invalid Content  <br> >>>>>>>>>><br>" +
                                                    (String) o.toString(),
                                                    ishtml);
                                            }
                                        } else if (o instanceof Multipart) {
                                            Multipart mp = (Multipart) o;
                                            BodyPart b = mp.getBodyPart(0);

                                            try {
                                                o2 = b.getContent();
                                            } catch (IOException e) {
                                                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
                                            }

                                            tempBodyContent += (String) o2;

                                            if (!ishtml) {
                                                sendMail(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                                                    "Invalid Request",
                                                    "Invalid Content  \n>>>>>>>>>>\n" +
                                                    tempBodyContent, ishtml);
                                            } else {
                                                sendMail(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                                                    "Invalid Request",
                                                    "Invalid Content  <br> >>>>>>>>>>  <br>" +
                                                    tempBodyContent, ishtml);
                                            }
                                        }
                                    }

                                    INVALID_CONTENT = true;
                                }
                            }

                            //check subject
                        }
                         //subject match pattern
                    }

                    // user found
                } catch (SecurityException e) {
                    Log.getLog(getClass()).error("ekpmaildaemon error query securityservice, shouldn't happen",
                        e);

                    //error in query database, shouldn't happen
                }
            }

            // check for email from
            ekpmddo.getMessage()[i].setFlag(Flags.Flag.DELETED, true);
        }

        //recur each email in inbox
    }

    public void replyMessage(String email, boolean sendBlank,
        String tempStatus, boolean ishtml) {
        //either pass in map of processed template"email" or pass in blank template
        // tempStatus has value only if mail is already process, return success or fail
        Application app = Application.getInstance();
        String tempTextBody = "";

        //filter email
        if ((email != null) && !("".equals(email))) {
            int startPoint = email.indexOf("<");
            int endPoint = email.indexOf(">", startPoint + 1);

            if (!((startPoint < 0) && (endPoint < 0))) {
                email = email.substring(startPoint + 1, endPoint)
                             .replaceAll(" ", "").trim();
            }
        }

        if (sendBlank) { //if need to send BLANK TEMPLATE

            for (Iterator it = stubMap.keySet().iterator(); it.hasNext();) {
                String className = (String) it.next();
                stub = (MailStub) stubMap.get(className);

                //generate nice template from bodyPattern
                if (!ishtml) {
                    tempTextBody = "\n" +
                        app.getMessage("maildaemon.label.info.header") ;
                } else {
                    tempTextBody = "<br>" +
                        app.getMessage("maildaemon.label.info.header");

                }

                tempTextBody += stub.getHeaderInfo();

                if (!ishtml) {
                    tempTextBody += "\n";
                } else {
                    tempTextBody += "<br>";
                }

                for (int j = 0; j < stub.getBodyPattern().length; j++) {
                    if (!("".equals(stub.getBodyPattern()[j]))) {
                        if (!ishtml) {
                            tempTextBody += ("\n" +
                            (stub.getBodyPattern()[j] + " [      ]"));
                        } else {
                            tempTextBody += ("<br>" +
                            (stub.getBodyPattern()[j] + " [      ]"));
                        }
                    }
                }

                if (!ishtml) {
                    tempTextBody += "\n"+stub.getInfo();
                } else {
                    tempTextBody += ("<br>" + stub.getInfo());
                }

                sendMail(email, stub.getSubjectPattern(), tempTextBody, ishtml);
            }

            // itto through generateTemplateMap
        } else {
            //either success or fail as long as not blank message
            if (!ishtml) {
                tempTextBody = "\n" + tempStatus;
            } else {
                tempTextBody = "<br>" + tempStatus;
            }

            String tempCheckCondition = "";
            int lengthCondition = Application.getInstance()
                                             .getMessage("maildaemon.label.successful")
                                             .toString().length();

            try {
                tempCheckCondition = tempStatus.substring(0, lengthCondition +
                        1);
            } catch (IndexOutOfBoundsException e) {
                tempCheckCondition = "";
            }

            if (tempCheckCondition.replaceAll(" ", "").trim().equalsIgnoreCase(Application.getInstance()
                                                                                              .getMessage("maildaemon.label.successful"))) {
                sendMail(email,
                    stub.getSubjectPattern() + " " +
                    Application.getInstance().getMessage("maildaemon.label.successful"),
                    tempTextBody, ishtml);
            } else {
                sendMail(email, stub.getSubjectPattern(), tempTextBody, ishtml);
            }
        }
    }

    public void sendMail(String email, String subject, String bodyText,
        boolean ishtml) {
        //if html mail , put <p> </p> if not ekp mail client will consider it is plain text
        //  if(ishtml)
        // bodyText = "<P>"+bodyText+"</P>";

        /*bodyText = bodyText.replaceAll("<br />","\n");
        bodyText = bodyText.replaceAll("<br>","");
        bodyText = bodyText.replaceAll("<b>","");
        bodyText = bodyText.replaceAll("</b>"," ");
        bodyText = bodyText.replaceAll("</div>","");
        // bodyText = bodyText.replaceAll("<blockquote style=\"border-left: 2px solid rgb(16, 16, 255); padding-left: 5px; margin-left: 5px;\"><div>","");
        bodyText = bodyText.replaceAll("<hr />","");
        */

        //send mail to user start
        // Local reference to the javax.mail.Session
        //delay for 500ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        javax.mail.Session mailSession = null;
        String token = "";
        String configured = "";

        configured = Application.getInstance().getProperty(MAIL_STMP);

        if (!((configured == null) || "".equals(configured))) {
            StringTokenizer tokenizer = new StringTokenizer(configured, ",");

            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
            }
        }

        String smtpHost = token;

        if ("".equals(smtpHost)) {
            token = "localhost";
        }

        token = "";
        configured = "";

        configured = Application.getInstance().getProperty(MAIL_USERNAME);

        if (!((configured == null) || "".equals(configured))) {
            StringTokenizer tokenizer = new StringTokenizer(configured, ",");

            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
            }
        }

        String username = token;

        if ("".equals(username)) {
            username = "admin";
        }

        token = "";
        configured = "";

        configured = Application.getInstance().getProperty(MAIL_PASSWORD);

        if (!((configured == null) || "".equals(configured))) {
            StringTokenizer tokenizer = new StringTokenizer(configured, ",");

            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
            }
        }

        String password = token;

        if ("".equals(password)) {
            password = "admin";
        }

        token = "";
        configured = "";

        Transport transport = null;

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", "true");

            // Get a mail session. You would normally get this from
            // JNDI, but some servers have a problem with this.
            // Each Message Driven bean instance is responsible for
            //getting its own unshared javax.mail.Session.
            mailSession = javax.mail.Session.getDefaultInstance(props, null);

            // javax.mail.Message msg = new MimeMessage(mailSession);
            //           com.tms.collab.messaging.model.Message message = new com.tms.collab.messaging.model.Message();
            MimeMessage msg = new MimeMessage(mailSession);

            //  message.setMessageId(UuidGenerator.getInstance().getUuid());
            //retrieve sent from from config file
            String email_from = "";
            configured = "";
            configured = Application.getInstance().getProperty(MAIL_USERNAME);

            if (!((configured == null) || "".equals(configured))) {
                StringTokenizer tokenizer = new StringTokenizer(configured, ",");

                while (tokenizer.hasMoreTokens()) {
                    email_from = tokenizer.nextToken();
                }
            }

            if ("".equals(email_from)) {
                email_from = "root@localhost";
            }

            // Set the mail properties
            msg.setFrom(new javax.mail.internet.InternetAddress(email_from));

            InternetAddress[] addresses = { new InternetAddress(email) };
            msg.setRecipients(javax.mail.Message.RecipientType.TO, addresses);

            msg.setSubject("RE: " + "[" + subject + "]");
            msg.setSentDate(new Date());

            msg.addHeader("Content-Type", "multipart/mixed");

            // Create the body text
            Multipart parts = new MimeMultipart();

            MimeBodyPart mainBody = new MimeBodyPart();

            //  msg.addHeader( "Content-Type", "multipart/alternative" );
            //mainBody.setContent(bodyText, "text/html");
            if (!ishtml) {
                mainBody.setContent(bodyText, "text/plain");
            } else {
                mainBody.setContent(bodyText, "text/html");
            }

            // mainBody.setText(bodyText); //text in body
            parts.addBodyPart(mainBody);

            //attachment
            //  MimeBodyPart attachmentBody = new MimeBodyPart();
            //  attachmentBody.setText("This is text in the attachment");
            //attachmentBody.addBodyPart( p2 );
            // Set some header fields
            //  msg.setHeader("X-Priority", "High");
            //   msg.setHeader("Sensitivity", "Company-Confidential");
            msg.setContent(parts);

            transport = mailSession.getTransport("smtp");
            transport.connect(smtpHost, username, password);

            //transport.send(msg);
            transport.sendMessage(msg, msg.getAllRecipients());

            transport.close();
        } catch (AuthenticationFailedException afe) {
            afe.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }

                if (transport.isConnected()) {
                    transport.close();
                }

                mailSession = null;
            } catch (MessagingException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }
        }

        //send mail to user end
    }

    public void processMessageFromStub(Map contentMap, String entireRawContent,
        boolean ishtml) {
        String tempStatus = "";
        String email = "";
        Set set = contentMap.keySet();
        Iterator iter = set.iterator();

        while (iter.hasNext()) {
            String key = iter.next().toString();
            String value = contentMap.get(key).toString();

            if ("EMAIL".equals(key)) {
                email = value;
            }

            if ("ERROR".equals(key)) {
                if (!ishtml) {
                    tempStatus = value;
                } else {
                    tempStatus = value.replaceAll("\n", "<br>");
                }
            }
        }

        if (!ishtml) {
            tempStatus += ("\n\n>>>>>>>>>>>>>>>>>>>>\n" + entireRawContent);
        } else {
            tempStatus += ("<br><br>>>>>>>>>>>>>>>>>>>>><br>" +
            entireRawContent);
        }

        replyMessage(email, false, tempStatus, ishtml);
    }

    public void doProcessing(EkpMailDaemonDataObject ekpmddo, int i, User user,
        String fileName, String emailBody, String attachmentContent,
        InputStream is, String fileType, long fileSize) {
        String tempBody = "";
        String mailProcessStatus = "";
        boolean ishtml;

        // boolean ishtml;
        if ((fileName != null) && (emailBody != null) &&
                (attachmentContent != null)) {
            //if email body is html and attachment is plain
            tempBody = emailBody;
        } else if (is != null) {
            //if email body is html and attachment is binary
            tempBody = emailBody;
        } else {
            tempBody = (String) o; //for plain text,no attachment at all
        }

        ishtml = globalishtml;

        entireRawContent = tempBody;

        Map contentMap = new HashMap();

        INVALID_CONTENT = false; //reinitialize to false before content is checked

        for (int count = 0; count < stub.getBodyPattern().length; count++) {
            //retrieve keyword from email
            int key = tempBody.indexOf(stub.getBodyPattern()[count].toUpperCase(),
                    0);
            String keywordFromEmail = "";

            if (key > 0) {
                keywordFromEmail = tempBody.substring(key,
                        stub.getBodyPattern()[count].length() + key).trim();
            }
            else {
                keywordFromEmail = "";
            }

            if (keywordFromEmail.equalsIgnoreCase(stub.getBodyPattern()[count])) {
                String temptoken = "";
                int retrieveContentStart = -1;
                int retrieveContentEnd = -1;

                if (INVALID_CONTENT == false) {
                    retrieveContentStart = tempBody.indexOf("[", key + 1);
                    retrieveContentEnd = tempBody.indexOf("]", key + 1);

                    if (retrieveContentEnd > retrieveContentStart) {
                        temptoken = tempBody.substring(retrieveContentStart +
                                1, retrieveContentEnd);
                    } else {
                        temptoken = "";
                    }
                }

                if ("".equals(temptoken.replaceAll(" ", "").trim()) &&
                        (INVALID_CONTENT == false) &&
                        !(stub.getBodyPattern()[count].equals(""))) {
                    if (!ishtml) {
                        mailProcessStatus += ("\n " +
                        Application.getInstance().getMessage("maildaemon.label.error.entry") +
                        " " + stub.getBodyPattern()[count].toUpperCase() + " " +
                        Application.getInstance().getMessage("maildaemon.label.error.cantfound"));
                    }
                    else {
                        mailProcessStatus += ("<br> " +
                        Application.getInstance().getMessage("maildaemon.label.error.entry") +
                        " " + stub.getBodyPattern()[count].toUpperCase() + " " +
                        Application.getInstance().getMessage("maildaemon.label.error.cantfound"));
                    }

                    INVALID_CONTENT = true;
                }

                /***

                 else {
                 INVALID_CONTENT = false; //content is ok

                 //if key inside mail is USERNAME or PASSWORD, do trim and cancel out all space and put insde MAP

                 if (stub.getBodyPattern()[count].toUpperCase().equals(app.getMessage("maildaemon.label.username")) ||
                 stub.getBodyPattern()[count].toUpperCase().equals(app.getMessage("maildaemon.label.password"))) {
                 contentMap.put(stub.getBodyPattern()[count].toUpperCase(),
                 tempBody.substring(retrieveContentStart + 1,
                 retrieveContentEnd).replaceAll(" ", "").trim());
                 } else {


                 contentMap.put(stub.getBodyPattern()[count].toUpperCase(),
                 tempBody.substring(retrieveContentStart + 1,
                 retrieveContentEnd));
                 }

                 contentMap.put(stub.getBodyPattern()[count].toUpperCase(),
                 tempBody.substring(retrieveContentStart + 1,
                 retrieveContentEnd));
                 }   ***/
                if (INVALID_CONTENT == false) {
                    if (retrieveContentEnd > retrieveContentStart) {
                        contentMap.put(stub.getBodyPattern()[count].toUpperCase(),
                            tempBody.substring(retrieveContentStart + 1,
                                retrieveContentEnd));
                    } else {
                        contentMap.put(stub.getBodyPattern()[count].toUpperCase(),
                            "");
                    }
                }
            } //if keyword from email and from stub match

            else {
                // keyword in email is changed by user ; shouldn't be changed by user
                if (!ishtml) {
                    mailProcessStatus += "Email content was altered by user\n";

                    mailProcessStatus += ("\n " +
                    Application.getInstance().getMessage("maildaemon.label.error.entry") +
                    " " + stub.getBodyPattern()[count].toUpperCase() + " " +
                    Application.getInstance().getMessage("maildaemon.label.error.cantfound"));
                } else {
                    mailProcessStatus += "Email content was altered by user<br>";

                    mailProcessStatus += ("<br> " +
                    Application.getInstance().getMessage("maildaemon.label.error.entry") +
                    " " + stub.getBodyPattern()[count].toUpperCase() + " " +
                    Application.getInstance().getMessage("maildaemon.label.error.cantfound"));
                }

                INVALID_CONTENT = true;
            }
        }

        if ((attachmentContent != null) && !("".equals(attachmentContent))) {
            contentMap.put("ATTACHMENT", attachmentContent);
        }

        if ((fileName != null) && !("".equals(fileName))) {
            contentMap.put("FILENAME", fileName);
        }

        if (is != null) {
            contentMap.put("BINARY_ATTACHMENT", (InputStream) is);
        }

        if ((fileSize >= 0) && (fileType != null)) {
            contentMap.put("FILE_SIZE", (String) Long.toString(fileSize));
            contentMap.put("FILE_TYPE", (String) fileType);
        }

        // if (!INVALID_CONTENT) {
        //if body content is ok
        //  SecurityService ss = (SecurityService) Application.getInstance()
        //                                                   .getService(SecurityService.class);
        if (!INVALID_CONTENT) {
            contentMap.put("USERID", user.getId());

            try {
                processMessageFromStub(stub.processMail(
                        ekpmddo.getMessage()[i].getFrom()[0].toString(),
                        contentMap), entireRawContent, ishtml);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        } else {
            //got invalid content of body retrieve from email
            try {
                if (!ishtml) {
                    replyMessage(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                        false,
                        mailProcessStatus + "\n>>>>>>>>>>>>>>\n" +
                        entireRawContent, ishtml);
                } else {
                    replyMessage(ekpmddo.getMessage()[i].getFrom()[0].toString(),
                        false,
                        mailProcessStatus + "<br> >>>>>>>>>>>>>> <br>" +
                        entireRawContent, ishtml);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        /******************

         boolean invalidPassword = false;

         try {




         Collection userCollection = ss.getUsersByUsername((String) contentMap.get(
         app.getMessage("maildaemon.label.username")));

         //iterate through collection to get information
         for (Iterator itc = userCollection.iterator(); itc.hasNext();) {
         //Object element = itc.next();
         User element = (User) itc.next();
         //element.getProperty("email1")

         if (element.getPassword().equals(Encryption.encrypt(
         (String) contentMap.get(app.getMessage("maildaemon.label.username"))))) {
         //add userID to Map
         contentMap.put("USERID", element.getId());

         // password is correct, start calling stub  and return result to processMessageFromStub for processing into string
         try {
         processMessageFromStub(stub.processMail(
         ekpmddo.getMessage()[i].getFrom()[0].toString(),
         contentMap), entireRawContent);
         } catch (MessagingException e) {
         Log.getLog(getClass()).info("Username not found in EKP",
         e);
         }

         invalidPassword = false;
         } else {
         mailProcessStatus = "\n" +
         app.getMessage(
         "maildaemon.label.error.usernamepassword") +
         "\n>>>>>>>>>>>>>>\n" + entireRawContent;

         //tell user entered wrong password
         try {
         replyMessage(ekpmddo.getMessage()[i].getFrom()[0].toString(),
         false, mailProcessStatus);
         invalidPassword = true;
         } catch (MessagingException e) {
         Log.getLog(getClass()).info("wrong password entered",
         e);
         }
         }
         }
         } catch (SecurityException e) {
         //invalid user name
         //if password check already wrong, so skip this
         if (!invalidPassword) {
         mailProcessStatus = "\n" +
         app.getMessage(
         "maildaemon.label.error.usernamepassword") +
         "\n>>>>>>>>>>>>>>\n" + entireRawContent;

         try {
         replyMessage(ekpmddo.getMessage()[i].getFrom()[0].toString(),
         false, mailProcessStatus);
         } catch (MessagingException e1) {
         Log.getLog(getClass()).info("email cannot be replied, might due to mail server problem",
         e1);
         }
         }

         Log.getLog(getClass()).info("Username not found", e);
         }
         *******************/

        //  }
    }
}
