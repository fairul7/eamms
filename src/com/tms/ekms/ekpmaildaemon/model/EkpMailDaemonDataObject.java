package com.tms.ekms.ekpmaildaemon.model;

import javax.mail.Message;
import java.io.Serializable;



public class EkpMailDaemonDataObject implements Serializable{
    private Message[] message;


    public Message[] getMessage() {
        return message;
    }

    public void setMessage(Message[] message) {
        this.message = message;
    }

  
}

