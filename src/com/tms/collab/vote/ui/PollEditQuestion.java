package com.tms.collab.vote.ui;

import com.tms.cms.section.ui.SectionSelectBox;
import com.tms.collab.vote.model.PollModule;
import com.tms.collab.vote.model.Question;
import kacang.Application;
import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;
import kacang.util.UuidGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PollEditQuestion extends Form {
    private Question selectedQuestion;
    private String id = null;
    private Button updateButton, clearResultButton, cancelButton;
    private PollAnswerView answerView;
    private CheckBox pendingCB, activeCB, publicCB;
    private RichTextBox questionTextField;
    private TextField titleTextField;
    private boolean added = false;
    private SelectBox assignedSelectBox;
    private SortedMap contentObjectMap;

    public PollEditQuestion() {
    }

    public PollEditQuestion(String name) {
        super(name);
    }

    public SortedMap getContentObjectMap() {
        return contentObjectMap;
    }

    public void setContentObjectMap(SortedMap contentObjectMap) {
        this.contentObjectMap = contentObjectMap;
    }

    public Button getClearResultButton() {
        return clearResultButton;
    }

    public void setClearResultButton(Button clearResultButton) {
        this.clearResultButton = clearResultButton;
    }

    public SelectBox getAssignedSelectBox() {
        return assignedSelectBox;
    }

    public void setAssignedSelectBox(SelectBox assignedSelectBox) {
        this.assignedSelectBox = assignedSelectBox;
    }

    public void setQuestionTextField(RichTextBox questionTextField) {
        this.questionTextField = questionTextField;
    }

    public RichTextBox getQuestionTextField() {
        return questionTextField;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public void setTitleTextField(TextField titleTextField) {
        this.titleTextField = titleTextField;
    }

    public CheckBox getPendingCB() {
        return pendingCB;
    }

    public void setPendingCB(CheckBox pendingCB) {
        this.pendingCB = pendingCB;
    }

    public CheckBox getActiveCB() {
        return activeCB;
    }

    public void setActiveCB(CheckBox activeCB) {
        this.activeCB = activeCB;
    }

    public CheckBox getPublicCB() {
        return publicCB;
    }

    public void setPublicCB(CheckBox publicCB) {
        this.publicCB = publicCB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Question getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public String getTemplate() {
        return "vote/PollQuestion";
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public Forward onValidate(Event evt) {
        ///
        // updateQuestion();
        if (selectedQuestion != null) {
            String buttonName = findButtonClicked(evt);
            if (buttonName != null && updateButton.getAbsoluteName().equals(buttonName)) {
                updateQuestion();
                if (selectedQuestion.getTitle().trim().length() > 0 && selectedQuestion.getQuestion().trim().length() > 0) {
                    try {

//                        selectedQuestion.setQuestion(extract(selectedQuestion.getQuestion(),evt));
                        selectedQuestion.setQuestion(makeRelative(evt.getRequest(), selectedQuestion.getQuestion()));
                        PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
                        Question question = pm.getQuestion(selectedQuestion.getId());
                        if (question == null)
                            pm.addQuestion(selectedQuestion);
                        else {
                            pm.removePreviousAssignment(selectedQuestion);
                            pm.updateQuestion(selectedQuestion);
                        }
                    }
                    catch (Exception e) {
                    }
                }
                return new Forward(null, evt.getRequest().getRequestURI(), true);
            }
            else if (clearResultButton.getAbsoluteName().equals(buttonName)) {
                PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
                pm.clearVoteResult(id);
            }
        }
        return super.onValidate(evt);//null;
    }

    public int imagesCount(String question) {
        int count = 0;

        return count;
    }

    public boolean hasImage(String question) {
        return question.indexOf("<IMG") != -1;
    }

    public void repaint() {
        questionTextField.setValue(selectedQuestion.getQuestion());
        questionTextField.setSize("" + selectedQuestion.getQuestion().length());
//        questionTextField.setImageUrl(getParent().getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) + "/cmsadmin/interactive/vote/frame.jsp?id=" + id);
        titleTextField.setValue(selectedQuestion.getTitle());
        if (selectedQuestion.getTitle().length() <= 40)
            titleTextField.setSize("50");
        else
            titleTextField.setSize("" + selectedQuestion.getTitle().length());
        pendingCB.setChecked(selectedQuestion.isPending());
        activeCB.setChecked(selectedQuestion.isActive());
        publicCB.setChecked(selectedQuestion.getIspublic());
        answerView.setAnswers(selectedQuestion.getAnswers());
        answerView.setQuestionID(selectedQuestion.getId());
        String sectionID = selectedQuestion.getAssigned();
        if (sectionID != null && !sectionID.equals("")) {
            List list = new LinkedList();
            list.add(sectionID);
            assignedSelectBox.setValue(list);
        }
    }

    private void updateQuestion() {
        selectedQuestion.setTitle((String) titleTextField.getValue());
        selectedQuestion.setQuestion((String) questionTextField.getValue());
        selectedQuestion.setPending(pendingCB.isChecked());
        selectedQuestion.setActive(activeCB.isChecked());
        selectedQuestion.setIspublic(publicCB.isChecked());
        Map map = assignedSelectBox.getSelectedOptions();
        String key = (String) map.keySet().iterator().next();
        selectedQuestion.setAssigned(key);
    }

    public void init() {
        try {
            setMethod("POST");
            Application application = Application.getInstance();
            contentObjectMap = new TreeMap();
            answerView = new PollAnswerView();
            answerView.setName("answerView");
            answerView.init();
            clearResultButton = new Button(("ClearResult"));
            clearResultButton.setText(application.getMessage("vote.label.clearResult", "Clear Result"));
            updateButton = new Button("Update");
            updateButton.setText(application.getMessage("vote.label.saveQuestion", "Save Question"));
            cancelButton = new Button("Cancel");
            cancelButton.setText(application.getMessage("vote.label.back", "Back"));
            pendingCB = new CheckBox("pending", application.getMessage("vote.label.pending", "Pending"));
            activeCB = new CheckBox("active", application.getMessage("general.label.active", "Active"));
            publicCB = new CheckBox("public", application.getMessage("forum.label.public", "Public"));
            questionTextField = new RichTextBox("question");
//            questionTextField.setImageUrl(getParent().getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH) + "/vote/InsertImage.jsp?id=" + id);
            questionTextField.addChild(new ValidatorNotEmpty("question"));
            titleTextField = new TextField("title");
            titleTextField.addChild(new ValidatorNotEmpty("title"));
            assignedSelectBox = new SectionSelectBox("Assigned");
            assignedSelectBox.setMultiple(false);
            addChild(assignedSelectBox);
            addChild(clearResultButton);
            addChild(titleTextField);
            addChild(questionTextField);
            addChild(answerView);
            addChild(cancelButton);
            addChild(updateButton);
            addChild(pendingCB);
            addChild(activeCB);
            addChild(publicCB);

        }
        catch (Exception e) {
        }
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public PollAnswerView getAnswerView() {
        return answerView;
    }

    public void setAnswerView(PollAnswerView answerView) {
        this.answerView = answerView;
    }


    public void setQuestion(String id, Event evt) {
        try {
            if (id != null) {
                PollModule pm = (PollModule) Application.getInstance().getModule(PollModule.class);
                selectedQuestion = pm.getQuestion(id);
//                selectedQuestion.setQuestion(add(selectedQuestion.getQuestion(),evt));
                selectedQuestion.setQuestion(makeAbsolute(evt.getRequest(), selectedQuestion.getQuestion()));
                this.id = id;
                answerView.setQuestionID(id);
            }
            else {
                Question question = new Question();
                question.setId(UuidGenerator.getInstance().getUuid());
                selectedQuestion = question;
                this.id = question.getId();
                answerView.setQuestionID(null);
            }
            repaint();
        }
        catch (Exception e) {
        }
    }

    /**
     * Converts absolute URLs into relative URLs.
     */
    public static String makeRelative(HttpServletRequest request, String content) {
        if (content == null)
            return null;

        String url = (request.getServerPort() == 80) ?
                "http://" + request.getServerName() + request.getContextPath() + "/" :
                "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        StringBuffer buffer = new StringBuffer(content);
        int j = buffer.toString().indexOf(url);
        while (j >= 0) {
            buffer.replace(j, j + url.length(), "");
            j = buffer.toString().indexOf(url);
        }
        return buffer.toString();
    }

    /**
     * Converts relative URLs into absolute URLs.
     */
    public static String makeAbsolute(HttpServletRequest request, String content) {
        if (content == null)
            return null;

        String url = (request.getServerPort() == 80) ?
                "http://" + request.getServerName() + request.getContextPath() + "/" :
                "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        StringBuffer buffer = new StringBuffer(content);
        int j = buffer.toString().indexOf("storage/");
        while (j >= 0) {
            if (j < url.length() || !buffer.substring(j - url.length(), j).equals(url)) {
                buffer.insert(j, url);
                j = buffer.toString().indexOf("storage/", j + url.length() + 2);
            }
            else {
                j = buffer.toString().indexOf("storage/", j + 2);
            }
        }
        return buffer.toString();
    }

}
