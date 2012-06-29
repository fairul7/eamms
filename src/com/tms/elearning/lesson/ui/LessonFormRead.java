package com.tms.elearning.lesson.ui;

import com.tms.cms.article.Article;
import com.tms.cms.core.model.ContentException;
import com.tms.cms.core.model.ContentManager;
import com.tms.cms.core.model.ContentObject;
import com.tms.cms.document.Document;

import com.tms.ekms.setup.model.SetupException;
import com.tms.ekms.setup.model.SetupModule;

import com.tms.elearning.lesson.model.Lesson;
import com.tms.elearning.lesson.model.LessonModule;
import com.tms.elearning.testware.model.Assessment;

import kacang.Application;

import kacang.model.DataObjectNotFoundException;

import kacang.services.security.User;

import kacang.stdui.Form;
import kacang.stdui.Label;

import kacang.ui.Event;

import kacang.util.Log;

import org.apache.commons.collections.SequencedHashMap;

import java.io.Serializable;

import java.util.*;



public class LessonFormRead extends Form implements Serializable {
    protected String id;
    protected String moduleId;

    public void init(Event evt) {
        setColumns(1);
    }

    public void onRequest(Event event) {

        Application app = Application.getInstance();

        removeChildren();



        LessonModule lessonmodule = (LessonModule) Application.getInstance()
                                                              .getModule(LessonModule.class);

        //check whether active or not

        if( !("".equals(lessonmodule.checkActive(getId()).getIs_public()))  ) {

             String is_public = lessonmodule.checkActive(getId()).getIs_public();

        if("1".equals(is_public)){

        SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);

        try {
            Lesson lesson = lessonmodule.loadLesson(getId());

            Label lessonBrief = new Label("lessonBrief",
                    lesson.getBrief() + "<br>");
            addChild(lessonBrief);

            if ((lesson.getFilePath() != null) &&
                    !("".equals(lesson.getFilePath()))) {
                String url = "<li><A HREF=\"" + "/storage" + lesson.getFilePath() +
                    "\"    target=\"_blank\" >" + lesson.getFileName() +
                    "</A><br>";

                Label linkattachment = new Label("link2Attachment", url);

                Label fileText = new Label("fileText",
                        "<br><b>" +
                        app.getMessage("eLearning.lesson.label.filetext") +
                        "</b><br>");
                addChild(fileText);
                addChild(linkattachment);
            }

            //get associate document
            if (lesson.getAssociatedoc() != null) {
                String associateDoc = lesson.getAssociatedoc();
                StringTokenizer st = new StringTokenizer(associateDoc, ",");

                int counting = 0;
                String[] eLearningFilesArray = new String[st.countTokens()];

                Label link = null;

                while (st.hasMoreTokens()) {
                    String tempToken = "";
                    tempToken = st.nextToken();

                    if ((tempToken != null) && !(tempToken.equals("")) &&
                            !(tempToken.equals(","))) {
                        eLearningFilesArray[counting] = tempToken;

                        counting++;
                    }
                }

                if (counting > 0) {
                    Label referenceText = new Label("referencetext",
                            "<br><b>" +
                            app.getMessage("eLearning.lesson.label.reference") +
                            "</b><br>");
                    addChild(referenceText);
                }

                Map filesMap = generateOptionMap(eLearningFilesArray);
                String url = "";
                Label link2;

                for (Iterator iterator = filesMap.entrySet().iterator();
                        iterator.hasNext();) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();

                    if ((key != null) && (value != null)) {
                        try {
                            url = "<li> <A HREF=\"" + sm.get("siteUrl") +
                                "/ekms/content/content.jsp?id=" + key +
                                "\"  onclick=\"window.open('" +
                                sm.get("siteUrl") +
                                "/ekms/content/content.jsp?id=" + key +
                                "');return false;\" target=\"newWin\" >" +
                                value + "</A><br>";
                        } catch (SetupException e) {
                        }

                        link2 = new Label("link2Attachment1" + counting, url);

                        counting++;
                        addChild(link2);
                    }
                }
            }

            //retrieve assessment assign to that lesson
            Collection assessmentCol = lessonmodule.getAssessmentByLesson(getId());

            if (assessmentCol != null && assessmentCol.size() >0) {
                Label listAssessmentTitle = new Label("listAssessmentTitle",
                        "<br><br><b>" +
                        app.getMessage(
                            "eLearning.general.assessment.forthislesson") +
                        ":</b><br>");
                addChild(listAssessmentTitle);

                int countAssessment = 0;

                for (Iterator iterator = assessmentCol.iterator();
                        iterator.hasNext(); countAssessment++) {
                    Assessment ass = (Assessment) iterator.next();

                    String url = "<li><A HREF=\"" + "questionDo.jsp?id=" +
                        ass.getId() + "\"   target=\"_blank\"  >" +
                        ass.getName() + "</A><br>";

                    Label listAssessment = new Label("listAssessment" +
                            countAssessment, url);
                    addChild(listAssessment);
                }
            }
        } catch (DataObjectNotFoundException e) {
            e.printStackTrace();
        }

        }//if activated
        }  //if
    }

    public Map generateOptionMap(String[] ids) {
        Map optionMap = new SequencedHashMap();

        try {
            ContentManager cm = (ContentManager) Application.getInstance()
                                                            .getModule(ContentManager.class);
            User user = getWidgetManager().getUser();
            Collection list = cm.viewListWithContents(ids,
                    new String[] {
                        Document.class.getName(), Article.class.getName()
                    }, null, null, null, null, null, null, null, Boolean.FALSE,
                    null, null, false, 0, -1, ContentManager.USE_CASE_VIEW, user);
            Map tmpMap = new HashMap();

            for (Iterator i = list.iterator(); i.hasNext();) {
                ContentObject co = (ContentObject) i.next();
                String name = co.getName();

                if ((name != null) && (name.length() > 20)) {
                    name = name.substring(0, 20) + "..";
                }

                tmpMap.put(co.getId(), name);
            }

            for (int j = 0; j < ids.length; j++)
                optionMap.put(ids[j], tmpMap.get(ids[j]));
        } catch (ContentException e) {
            Log.getLog(getClass()).error("Error retrieving content", e);
        }

        return optionMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
