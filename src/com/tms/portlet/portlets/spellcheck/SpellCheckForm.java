package com.tms.portlet.portlets.spellcheck;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.Application;
import webservices.sc.*;

import java.rmi.RemoteException;

public class SpellCheckForm extends Form {
    private TextField word;
    private Button check;
    private Label outputConsole;

    public void init() {
        super.init();

        removeChildren();
        setInvalid(false);
        setMessage("post");

        word = new TextField("Word", "");
        word.setSize("20");
        word.addChild(new ValidatorNotEmpty("Word"));
        addChild(word);

        check = new Button("Check", Application.getInstance().getMessage("portlet.label.check","Check"));
        addChild(check);

        outputConsole = new Label(Application.getInstance().getMessage("portlet.label.outputConsole","Output Console"), "");
        addChild(outputConsole);
    }

    public Forward onValidate(Event event) {
        String s = doSpellCheck((String) word.getValue());
        outputConsole.setText(s);

        return super.onValidate(event);
    }

    public String doSpellCheck(String s) {
        StringBuffer sb = new StringBuffer();

        SpellCheckServiceLocator l = new SpellCheckServiceLocator();
        try {
            SpellCheckServiceSoap soap = l.getSpellCheckServiceSoap();

            Corrections c = soap.spellCheck("", s);
            CorrectionsCorrection[] cc = c.getCorrection();
            ArrayOfString a;

            if (cc != null) {
                for (int i = 0; i < cc.length; i++) {
                    sb.append(cc[i].getWord().toUpperCase() + ": \n");

                    a = cc[i].getSuggestions();
                    for (int j = 0; j < a.getSuggestion().length; j++) {
                        sb.append(a.getSuggestion(j));
                        if (j + 1 < a.getSuggestion().length) {
                            sb.append(", ");
                        } else {
                            sb.append("\n");
                        }
                    }
                }
            } else {
                // no result (no error?)
                sb.append(Application.getInstance().getMessage("portlet.label.checkResult","Spell check is complete. No mistake found."));
            }

            return sb.toString();

        } catch (javax.xml.rpc.ServiceException e) {
            return "Web service error: " + e.getMessage();
        } catch (RemoteException e) {
            return "Web service error: " + e.getMessage();
        }
    }

    public TextField getWord() {
        return word;
    }

    public void setWord(TextField word) {
        this.word = word;
    }

    public Button getCheck() {
        return check;
    }

    public void setCheck(Button check) {
        this.check = check;
    }

    public Label getOutputConsole() {
        return outputConsole;
    }

    public void setOutputConsole(Label outputConsole) {
        this.outputConsole = outputConsole;
    }
}
