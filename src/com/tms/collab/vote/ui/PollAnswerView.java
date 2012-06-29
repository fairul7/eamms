package com.tms.collab.vote.ui;

import com.tms.collab.vote.model.Answer;
import com.tms.collab.vote.model.PollModule;
import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;


/**
 * Created by IntelliJ IDEA.
 * User: Disen
 * Date: Apr 29, 2003
 * Time: 11:02:21 AM
 * To change this template use Options | File Templates.
 */
public class PollAnswerView extends Form
{
    public static final String POLL_ANSWER_ADD ="Add";
    public static final String POLL_ANSWER_UPDATE ="update";
    public static final String POLL_ANSWER_EDIT ="edit";
    public static final String POLL_ANSWER_DELETE ="delete";
    public static final String POLL_ANSWER_UP ="up";
    public static final String POLL_ANSWER_DOWN ="down";
    private Answer selectedAnswer;
    private Answer [] answers;
    private String questionID;
    private String action = POLL_ANSWER_ADD;
    private Button actionButton = new Button("action");
    private TextField answerTextField = new TextField("answer");
    private TextBox answerTextBox = new TextBox("answerTextBox");

    public void init()
    {
        try
        {
            actionButton.setText(Application.getInstance().getMessage("vote.label." + action, action));
            addChild(actionButton);
            if(selectedAnswer!=null)
            {
               answerTextField.setValue(selectedAnswer.getAnswer());
            }
            addChild(answerTextField);
/*            if(answers!=null)
            {
                for(int i=0;i<answers.length;i++)
                {
                    answerTextArea.append(answers[i].getAnswer()+"\n\n");
                }
            }*/
            answerTextBox.setRows(""+5);
            addChild(answerTextBox);
            setMethod("POST");
        }
        catch(Exception e)
        {}
    }

    public TextBox getAnswerTextBox()
    {
        return answerTextBox;
    }

    public void setAnswerTextBox(TextBox answerTextBox)
    {
        this.answerTextBox = answerTextBox;
    }

    public TextField getAnswerTextField()
    {
        return answerTextField;
    }

    public void setAnswerTextField(TextField answerTextField)
    {
        this.answerTextField = answerTextField;
    }

    public Button getActionButton()
    {
        return actionButton;
    }

    public void setActionButton(Button actionButton)
    {
        this.actionButton = actionButton;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public void onRequest(Event evt)
    {
        try
        {
          //  super.onRequest(evt);
            if(questionID!=null&&questionID.trim().length()>0)
            {
                PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
                setAnswers(pm.getAnswers(questionID));
                repaint();
            }
        }
        catch(Exception e)
        {
        }
     }


    public void repaint()
    {
         actionButton.setText(Application.getInstance().getMessage("vote.label." + action, action));
        if(selectedAnswer!=null)
        {
           answerTextField.setValue(selectedAnswer.getAnswer());
        }

    }

    public void deleteAnswer(Event evt)
    {
        try
        {
            if(answers==null) return;
            PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
            HttpServletRequest req = evt.getRequest();
            String id = req.getParameter("id");
            for(int i=0;i<answers.length;i++)
            {
                if(answers[i].getId().equals(id))
                {
                    pm.deleteAnswer(answers[i]);
                    resetOrder(answers[i]);
                    answers = pm.getAnswers(questionID);
                    selectedAnswer = null;
                    this.action = POLL_ANSWER_ADD;
                    repaint();
                    break;
                }
            }
        }
        catch(Exception e)
        {    }
    }

    public void resetOrder(Answer answer)
    {
        int p;
        PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
        for(int i=0;i<answers.length;i++)
        {
            if((p = answers[i].getPiority())>answer.getPiority())
            {
                answers[i].setPiority(--p);
                pm.updateAnswer(answers[i]);
            }
        }
    }

    public void editAnswer(Event evt)
    {
        HttpServletRequest req = evt.getRequest();
        String id = req.getParameter("id");
        for(int i=0;i<answers.length;i++)
        {
            if(answers[i].getId().equals(id))
            {
                selectedAnswer = answers[i];
                this.action = POLL_ANSWER_UPDATE;
                repaint();
                break;
            }
        }
    }

    private void moveUp(Event evt)
    {
        PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
        HttpServletRequest req = evt.getRequest();
        int pior = Integer.parseInt(req.getParameter("pior"));
        answers[pior-1].setPiority(pior-1);
        pm.updateAnswer(answers[pior-1]);
        answers[pior-2].setPiority(pior);
        pm.updateAnswer(answers[pior-2]);
        answers = pm.getAnswers(questionID);
    }

    private void moveDown(Event evt)
    {
        PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
        HttpServletRequest req = evt.getRequest();
        int pior = Integer.parseInt(req.getParameter("pior"));
        answers[pior-1].setPiority(pior+1);
        pm.updateAnswer(answers[pior-1]);
        answers[pior].setPiority(pior);
        pm.updateAnswer(answers[pior]);
        answers = pm.getAnswers(questionID);
    }

    public Forward onValidate(Event evt)
    {
        //super.onValidate(evt);
        if(answers!=null||questionID!=null)
        {
            String buttonName = findButtonClicked(evt);
            PollModule pm = (PollModule)Application.getInstance().getModule(PollModule.class);
            if(buttonName!=null)
            {
                if(action.equals(POLL_ANSWER_ADD))
                {
                    int order;
                    if(answers!=null)
                        order = answers.length +1;
                    else
                        order = 1;
                    String answer = (String)answerTextBox.getValue();
                    if(answer.equals(""))return null;
                    StringTokenizer tokenizer = new StringTokenizer(answer,"\n");
                    while(tokenizer.hasMoreTokens())
                    {
                        String token = tokenizer.nextToken();
                        if(Character.isWhitespace(token.charAt(token.length()-1)))
                            token = token.substring(0,token.length()-1);
                        if(token.trim().length()>0)
                        {
                            answers =  answers = pm.addAnswer(questionID,token,order);
                            order++;
                        }
                    }
                    selectedAnswer=null;
                    answerTextBox.setValue("");
                    this.action= POLL_ANSWER_ADD;
                    return new Forward(null,evt.getRequest().getRequestURI(),true);
                }
                else if(action.equals(POLL_ANSWER_UPDATE))
                {
                    String answer = (String)answerTextField.getValue();
                    if(answer.equals(""))return null;
                    selectedAnswer.setAnswer(answer);
                    pm.updateAnswer(selectedAnswer);
                    selectedAnswer= null;
                    answerTextField.setValue("");
                    this.action = POLL_ANSWER_ADD;
                    return new Forward(null,evt.getRequest().getRequestURI(),true);
                }
            }
            else
            {
                String type = evt.getType();
                if(POLL_ANSWER_EDIT.equals(type))
                {
                    editAnswer(evt);
                }
                else if(POLL_ANSWER_DELETE.equals(type))
                {
                    deleteAnswer(evt);
                }
                else if(POLL_ANSWER_UP.equals(type))
                {
                       moveUp(evt);
                }
                else if(POLL_ANSWER_DOWN.equals(type))
                {
                       moveDown(evt);
                }
            }
        }
        return new Forward(null,evt.getRequest().getRequestURI(),true);
    }


    public Answer getSelectedAnswer()
    {
        return selectedAnswer;
    }


    public void setSelectedAnswer(Answer selectedAnswer)
    {
        this.selectedAnswer = selectedAnswer;
    }

    public Answer[] getAnswers()
    {
        return answers;
    }

    public void setAnswers(Answer[] answers)
    {
        this.answers = answers;
    }

    public String getQuestionID()
    {
        return questionID;
    }

    public void setQuestionID(String questionID)
    {
        this.questionID = questionID;
    }



    public String getTemplate()
    {
        return "vote/PollAnswer";
    }

    public String getDefaultTemplate()
    {
       return "vote/PollAnswer";
    }
}
//  String extractedAnswer = "";
/* if(!extractedAnswer.equals(""))
                        answers = pm.addAnswer(questionID,extractedAnswer);*/

/*if(token.equals("")||token==null)
                        {
                            if(!extractedAnswer.equals(""))
                            {
                                answers = pm.addAnswer(questionID,extractedAnswer);
                                extractedAnswer = "";
                            }

                        }
                        else
                        {
                            extractedAnswer += token;
                        }*/
