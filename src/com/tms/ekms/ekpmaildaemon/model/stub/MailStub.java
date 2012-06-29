package com.tms.ekms.ekpmaildaemon.model.stub;

import java.util.Map;


public interface MailStub {
    public Map processMail(String emailAddress, Map contentMap); //pass to mailmodule

    public String getSubjectPattern();

    public String[] getBodyPattern();

    public String getHeaderInfo();

    public String getInfo();
}
