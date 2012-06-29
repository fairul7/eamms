package com.tms.portlet.portlets.eliza;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import webservices.eliza.ChatLocator;
import webservices.eliza.ChatPortType;

import java.rmi.RemoteException;

public class ElizaForm extends Form
{
    private TextField input;
    private Button clear;
    private Button say;
    private Label outputConsole;
    private Panel buttonPanel;

    public void init()
    {
        super.init();
        setColumns(1);

        removeChildren();
        setInvalid(false);
        setMessage("post");

        input = new TextField("Input", "");
        input.setSize("30");
        input.addChild(new ValidatorNotEmpty("input"));
        addChild(input);

        buttonPanel = new Panel("buttonPanel");
        buttonPanel.setColumns(2);
        addChild(buttonPanel);

        say = new Button("Say", Application.getInstance().getMessage("portlet.label.say","Say"));
        buttonPanel.addChild(say);

        clear = new Button("Clear", Application.getInstance().getMessage("portlet.label.clear","Clear"));
        buttonPanel.addChild(clear);

        outputConsole = new Label(Application.getInstance().getMessage("portlet.label.outputConsole","Output Console"), "");
        outputConsole.setAttribute("align", "left");
        addChild(outputConsole);
    }

    public Forward onValidate(Event event)
    {
        if(clear.getAbsoluteName().equals(findButtonClicked(event)))
            outputConsole = new Label(Application.getInstance().getMessage("portlet.label.outputConsole","Output Console"), "");
        else
        {
            String e = doSay((String) input.getValue());
            outputConsole.setText(Application.getInstance().getMessage("portlet.label.you:","You:") + input.getValue() + "<br>" + outputConsole.getText());
            outputConsole.setText(Application.getInstance().getMessage("portlet.label.eliza:","Eliza:") + e + "<br>" + outputConsole.getText());
            input.setValue("");
        }
        return super.onValidate(event);
    }

    public String doSay(String s)
    {
        ChatLocator l = new ChatLocator();
        ChatPortType t = null;

        try {
            t = l.getIBaseDataTypes();
            return t.eliza(s);

        } catch (javax.xml.rpc.ServiceException e) {
            return "Web service error: " + e.getMessage();
        } catch (RemoteException e) {
            return "Web service error: " + e.getMessage();
        }
    }

    public TextField getInput()
    {
        return input;
    }

    public void setInput(TextField input)
    {
        this.input = input;
    }

    public Button getSay()
    {
        return say;
    }

    public void setSay(Button say)
    {
        this.say = say;
    }

    public Label getOutputConsole()
    {
        return outputConsole;
    }

    public void setOutputConsole(Label outputConsole)
    {
        this.outputConsole = outputConsole;
    }

    public Button getClear()
    {
        return clear;
    }

    public void setClear(Button clear)
    {
        this.clear = clear;
    }

    public Panel getButtonPanel()
    {
        return buttonPanel;
    }

    public void setButtonPanel(Panel buttonPanel)
    {
        this.buttonPanel = buttonPanel;
    }
}
