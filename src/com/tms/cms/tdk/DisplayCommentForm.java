package com.tms.cms.tdk;

import com.tms.cms.comment.Comment;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentPublisher;
import com.tms.cms.core.model.ContentUtil;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.ui.Event;
import kacang.ui.LightWeightWidget;

import javax.servlet.http.HttpServletRequest;

public class DisplayCommentForm extends LightWeightWidget {

    private String id; // commentary id
    private String commentId; // parent comment id in thread, optional
    private String url;
    private String title;
    private String score;
    private String comment;
    private boolean restricted;
    private Comment commentary;
    private String scoreRequired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment getCommentary() {
        return commentary;
    }

    public void setCommentary(Comment commentary) {
        this.commentary = commentary;
    }

    public String getScoreRequired() {
        return scoreRequired;
    }

    public void setScoreRequired(String scoreRequired) {
        this.scoreRequired = scoreRequired;
    }

    public void onRequest(Event evt) {
        HttpServletRequest request = evt.getRequest();

        // get id
        String key = getId();
        if (key == null) {
            key = request.getParameter("id");
            setId(key);
        }

        if (commentId == null) {
            commentId = request.getParameter("commentId");
        }

        // check permission
        User user = ((SecurityService)Application.getInstance().getService(SecurityService.class)).getCurrentUser(request);
        ContentPublisher cp = (ContentPublisher)Application.getInstance().getModule(ContentPublisher.class);
        try {
            Comment co = (Comment)cp.view(key, user);

            // check permission
            if (!ContentUtil.hasPermission(co, user.getId(), ContentManager.USE_CASE_VIEW)) {
                restricted = true;
            }
            else {
                this.commentary = co;
            }
        }
        catch (DataObjectNotFoundException e) {
        }
        catch (ContentException e) {
            throw new RuntimeException(e.toString());
        }

        // set values
        setTitle(request.getParameter("title"));
        setScore(request.getParameter("score"));
        setComment(request.getParameter("comment"));
    }

    public String getDefaultTemplate() {
        if (restricted) {
            return "cms/tdk/displayContentNoPermission";
        }
        else {
            return "cms/tdk/displayCommentForm";
        }
    }

}
