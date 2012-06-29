package com.tms.collab.directory.model;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

public class AddressBookCsvParser implements Serializable {

    public static final int STATE_NEW = 0;
    public static final int STATE_IN_TOKEN = 1;
    public static final int STATE_QUOTE_OPEN = 2;
    public static final int STATE_QUOTE_CLOSE = 3;

    char delimiter = ',';
    char quote = '"';
    char lf = '\r';
    char eol = '\n';

    Collection tokens;
    Collection lines;

    String csv;
    char c;
    int currentState;
    int tokensPerLine;
    StringBuffer token;

    public AddressBookCsvParser() {
        tokens = new ArrayList();
        lines = new ArrayList();
    }

    public AddressBookCsvParser(String csv) {
        tokens = new ArrayList();
        lines = new ArrayList();
        setCsv(csv);
        parseCsv();
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    /**
     *
     * @return A Collection of Collection of Strings
     */
    public Collection getLines() {
        return lines;
    }

    public String[][] parseCsv() {

        if (csv == null || csv.trim().length() == 0)
            return null;

        currentState = STATE_NEW;
        token = new StringBuffer();
        for (int i=0; i<csv.length(); i++) {
            c = csv.charAt(i);
            switch(currentState) {
                case STATE_NEW:
                    if (c == delimiter) {
                        addToken();
                        currentState = STATE_NEW;
                    }
                    else if (c == quote) {
                        currentState = STATE_QUOTE_OPEN;
                    }
                    else if (c == lf) {
                        ;
                    }
                    else if (c == eol) {
                        addToken();
                        addLine();
                        currentState = STATE_NEW;
                    }
                    else {
                        appendToken();
                        currentState = STATE_IN_TOKEN;
                    }
                    break;
                case STATE_IN_TOKEN:
                    if (c == delimiter) {
                        addToken();
                        currentState = STATE_NEW;
                    }
                    else if (c == lf) {
                        ;
                    }
                    else if (c == eol) {
                        addToken();
                        addLine();
                        currentState = STATE_NEW;
                    }
                    else {
                        appendToken();
                        currentState = STATE_IN_TOKEN;
                    }
                    break;
                case STATE_QUOTE_OPEN:
                    if (c == quote) {
                        appendToken();
                        currentState = STATE_QUOTE_CLOSE;
                    }
                    else {
                        appendToken();
                        currentState = STATE_QUOTE_OPEN;
                    }
                    break;
                case STATE_QUOTE_CLOSE:
                    if (c == delimiter) {
                        token.deleteCharAt(token.length()-1);
                        addToken();
                        currentState = STATE_NEW;
                    }
                    else if (c == quote) {
                        appendToken();
                        currentState = STATE_QUOTE_OPEN;
                    }
                    else if (c == lf) {
                        ;
                    }
                    else if (c == eol) {
                        token.deleteCharAt(token.length()-1);
//                        if (tokensPerLine > 0 && (tokens.size() < tokensPerLine)) {
                        if (false) {
                            appendToken();
                            currentState = STATE_QUOTE_OPEN;
                        }
                        else {
                            addToken();
                            addLine();
                            currentState = STATE_NEW;
                        }
                    }
                    else {
                        appendToken();
                        currentState = STATE_QUOTE_OPEN;
                    }
                    break;
            }
        }
        addToken();
        addLine();

        return null;
    }

    protected void appendToken() {
        token.append(c);
    }

    protected void addToken() {
        tokens.add(token.toString());
        token = new StringBuffer();
    }

    protected void addLine() {
        if (lines.size() == 0) {
            tokensPerLine = tokens.size();
        }
        if(!isEmpty(tokens))
            lines.add(tokens);
        tokens = new ArrayList();
    }


    private boolean isEmpty(Collection tokens){
        boolean empty = true;

        for (Iterator iterator = tokens.iterator(); iterator.hasNext();) {
            String token = (String) iterator.next();
            if(token!=null&&token.trim().length()>0){
                empty=false;
                break;
            }
        }

        return empty;
    }

    public static void main(String[] args) {
//        AddressBookModule.parseCsv("111, 222,\"333 \nTest\",444\n555,666,\"77\n7\"\n999,1010,1111,1212");
        try {
            BufferedReader r = new BufferedReader(new FileReader("c:\\Documents and Settings\\julian\\Desktop\\test2.CSV"));
            String csv = "";
            String s = r.readLine();
            while (s!=null) {
                csv += s + "\n";
                s = r.readLine();
            }
            r.close();
            AddressBookCsvParser parser = new AddressBookCsvParser(csv);
            System.out.println(parser.getLines());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
