package com.tms.ekms.ekpmaildaemon.model;

import kacang.Application;

import kacang.services.scheduling.BaseJob;
import kacang.services.scheduling.JobTaskExecutionContext;
import kacang.services.scheduling.SchedulingException;

import kacang.util.Log;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;


public class EkpMailDaemon extends BaseJob {

    public static final String EKPMAILDAEMON_ACTIVATION="ekpmail_daemon.activation";
    public static final String MAIL_USERNAME = "ekpmaildaemon.mail.account.name";
    public static final String MAIL_PASSWORD = "ekpmaildaemon.mail.password";
    public static final String MAIL_POP = "ekpmaildaemon.mail.pop";
    EkpMailDaemonDataObject ekpmddo;


   static int countDownTime = 0;

    public void execute(JobTaskExecutionContext jobTaskExecutionContext)
        throws SchedulingException {




                       String activation = Application.getInstance().getProperty(EKPMAILDAEMON_ACTIVATION);

                    if(activation !=null && activation.equalsIgnoreCase("true")){


        long startTime = System.currentTimeMillis();


        try {
            getMail();
        } catch (Exception e) {
            Log.getLog(getClass()).info("error login into mail server" +
                e.toString(), e);
        }

        long currentTime = System.currentTimeMillis();
        long duration = currentTime - startTime;


        Log.getLog(getClass()).info("Duration for EKPMailModule task: "+duration+ "ms");





                    }












        /***************
          MessagingQueue queue;
          QueueItem item;
          MessagingModule module;

          queue = MessagingQueue.getInstance();
          module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

          // check for thread limit
          if (queue.getDownloadPop3Current() >= module.getDownloadPop3JobCount()) {
              Log.getLog(DownloadPop3Job.class).debug("Download POP3 skipped due to thread limit");
              return;
          }




          item = queue.popDownloadPop3();
          while (item != null) {
              try {
                  queue.setDownloadPop3Current(queue.getDownloadPop3Current() + 1);

                  // download emails
                  try {


                      module.downloadPop3MessagesNow(item.getUserId());



                  } catch (Throwable e) {
                      Log.getLog(DownloadPop3Job.class).error("Error downloading emails", e);
                  }
              } finally {
                  queue.setDownloadPop3Current(queue.getDownloadPop3Current() - 1);
              }

              item = queue.popDownloadPop3();
          }

          ******************/
    }

    public void do_testing_purpose() {
        //get mail each interval of time pre set
        try {
            getMail();
        } catch (Exception e) {
            Log.getLog(getClass()).info("error login into mail server" +
                e.toString(), e);
        }
    }

    public void getMail() throws MessagingException {
        Log.getLog(getClass()).info("EkpMailDaemon GETTING MAIL .............................");

        String token = "";
        String configured = "";
        EkpMailDaemonModule module = (EkpMailDaemonModule) Application.getInstance()
                                                                      .getModule(EkpMailDaemonModule.class);
        configured = Application.getInstance().getProperty(MAIL_POP);

        if (!((configured == null) || "".equals(configured))) {
            StringTokenizer tokenizer = new StringTokenizer(configured, ",");

            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
            }
        }

        String host = token;

        if ("".equals(host)) {
            host = "localhost";
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

        Store store = null;
        Folder folder = null;

        try {
            // Create empty properties
            Properties props = new Properties();

            // Get session
            Session session = Session.getDefaultInstance(props, null);

            // Get the store
            store = session.getStore("pop3");

            // Connect to store
            store.connect(host, username, password);

            // Get folder
            folder = store.getFolder("INBOX");

            // Open read-only
            folder.open(Folder.READ_WRITE);
            Log.getLog(getClass()).info("# of messages="+ folder.getMessageCount());
            // Get directory
            // Get message and save in Data Object
            ekpmddo = new EkpMailDaemonDataObject();
            ekpmddo.setMessage(folder.getMessages()); // message [] save in dataObject

            module.processMail(ekpmddo);
            //if communication between mail daemon and mail server ok, reset the down time
            countDownTime =0;
        } catch (javax.mail.MessagingException e) {

            Log.getLog(getClass()).info(e+"\n downTime="+countDownTime+" min");
                        countDownTime +=1;


        }
         finally {
            // make sure mailbox connection is closed
            //     if (store != null && store.isConnected()) {

            /*      try {
                      if ( folder !=null &&  folder.isOpen()) {
                          folder.close(true);
                      }
                      store.close();

                  }*/
          //  try {



     /*           if (folder != null) {
                    folder.close(true);
                }*/

                      /*  if(folder ==null)  System.out.println ("<<<<<folder null");
                        if(store == null)  System.out.println("<<<<<store nul");*/
/*
                if(folder.isOpen() || folder != null)
                {folder.close(true);
                 folder =null;
                }*/
          //      if(folder !=null )
           //     folder.close(true);




             /*   if (store != null) {
                    store.close();
                }*/


              /*  if (store.isConnected() || store != null )
                {   store.close();
                    store = null;
                }*/
             //  if(store != null)
             //  store.close();






              if (store != null) {
                try {
                    if (folder != null && folder.isOpen()) {
                        folder.close(true);
                    }
                    store.close();
                } catch (javax.mail.MessagingException e) {
                    throw new MessagingException(e.getMessage(), e);
                }
              }




        }

           /* catch (javax.mail.MessagingException e) {
                throw new MessagingException(e.getMessage(), e);

            }*/

            //  }
      //  }

        // Close connection
        //  folder.close(true);
        //  store.close();
    }
}
