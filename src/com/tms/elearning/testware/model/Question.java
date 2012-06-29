package com.tms.elearning.testware.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: mahe
 * Date: Nov 2, 2004
 * Time: 4:07:12 PM
 * To change this template use Options | File Templates.
 */
public class Question implements Serializable{
    String id;
    String module_id;
    String question;
    String course_id;
    String qbank_id;
    String module;
    String answer_a;
    String answer_b;
    String answer_c;
    String answer_d;
    String answer_e;
    String correct_answer;
    String lesson_id;
    String topic;
    String topic_id;
    String exam_id;
    String createdByUser;
    String createdByUserId;
    String createdDate;
    String parentId;
    String question_id;

    public String getQbank_id() {
        return qbank_id;
    }

    public void setQbank_id(String qbank_id) {
        this.qbank_id = qbank_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String examid) {
        this.exam_id = examid;
    }

    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getAnswer_a() {
        return answer_a;
    }

    public void setAnswer_a(String answer_a) {
        this.answer_a = answer_a;
    }

    public String getAnswer_b() {
        return answer_b;
    }

    public void setAnswer_b(String answer_b) {
        this.answer_b = answer_b;
    }

    public String getAnswer_c() {
        return answer_c;
    }

    public void setAnswer_c(String answer_c) {
        this.answer_c = answer_c;
    }

    public String getAnswer_d() {
        return answer_d;
    }

    public void setAnswer_d(String answer_d) {
        this.answer_d = answer_d;
    }

    public String getAnswer_e() {
        return answer_e;
    }

    public void setAnswer_e(String answer_e) {
        this.answer_e = answer_e;
    }

    public String getCorrect_answer() {
        return correct_answer.trim();
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreatedByUserId() {
         return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
         this.createdByUserId = createdByUserId;
    }

    public String getCreatedDate() {
         return createdDate;
    }

    public void setCreatedDate(String createdDate) {
         this.createdDate = createdDate;
    }


}
