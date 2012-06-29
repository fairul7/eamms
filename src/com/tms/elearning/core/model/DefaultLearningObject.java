package com.tms.elearning.core.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 20, 2003
 * Time: 2:55:45 PM
 * To change this template use Options | File Templates.
 */
public class DefaultLearningObject extends LearningObject implements Serializable{

    public Class getLearningModuleClass() {
        return DefaultLearningModule.class;
    }



}
