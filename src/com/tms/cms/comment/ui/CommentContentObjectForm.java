package com.tms.cms.comment.ui;

import com.tms.cms.comment.Comment;
import com.tms.cms.comment.Commentary;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.core.ui.ContentObjectForm;
import kacang.stdui.ButtonGroup;
import kacang.stdui.Radio;
import kacang.ui.Event;

import java.util.Iterator;

/**
 * Form for adding/editing a comment.
 */
public class CommentContentObjectForm extends ContentObjectForm {

    private ButtonGroup scoreGroup;

    public CommentContentObjectForm() {
    }

    public CommentContentObjectForm(String name) {
        super(name);
    }

    public String getDefaultTemplate() {
        if (Commentary.class.getName().equals(getContentObject().getClassName())) {
            return "cms/admin/commentaryContentObjectForm";
        }
        else {
            return "cms/admin/commentContentObjectForm";
        }
    }

    public void init(Event evt) {
        super.init(evt);

        scoreGroup = new ButtonGroup("scoreGroup");
        for (int i=1; i<=5; i++) {
            Radio radio = new Radio("score" + i);
            radio.setText("" + i);
            radio.setValue("" + i);
            if (i==3) {
                radio.setChecked(true);
            }
            scoreGroup.addButton(radio);
        }
        addChild(scoreGroup);
    }

    protected void populateFields(Event evt) {
        super.populateFields(evt);
        // populate values?
        Comment comment = (Comment)getContentObject();
        if (comment != null) {
            float score = comment.getScore();
            String selected = "score" + score;
            for (Iterator i=scoreGroup.getChildren().iterator(); i.hasNext();) {
                Radio radio = (Radio)i.next();
                if (selected.equals(radio.getName())) {
                    radio.setChecked(true);
                }
                else {
                    radio.setChecked(false);
                }
            }
        }
    }

    protected void populateContentObject(Event evt, ContentObject contentObject) {
        super.populateContentObject(evt, contentObject);

        // set score
        Comment comment = (Comment)getContentObject();
        try {
            String scoreStr = (String)scoreGroup.getValue();
            float score = Float.parseFloat(scoreStr);
            comment.setScore(score);
        }
        catch (NumberFormatException e) {
            ;
        }

        // set author
        comment.setAuthor(getWidgetManager().getUser().getUsername());
    }

}
