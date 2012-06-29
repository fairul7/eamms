package com.tms.collab.vote.model;


import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.Log;

import java.util.Collection;
import java.util.Iterator;


public class PollDao extends DataSourceDao
{
    Log log = Log.getLog(getClass());

    public void init() throws DaoException
    {
        super.update("CREATE TABLE poll_question (id varchar(64) NOT NULL,question varchar(255) NOT NULL ,assigned varchar(150) default NULL,pending char(1) binary NOT NULL default '0',active char(1) binary NOT NULL default '0',ispublic char(1) binary NOT NULL default '0',total int(15) unsigned default '0',title varchar(200) NOT NULL default 'default title',PRIMARY KEY  (id),UNIQUE KEY id (id)) TYPE=MyISAM;",null);
        super.update("CREATE TABLE poll_answer (id int(15) unsigned NOT NULL auto_increment,answer varchar(200) NOT NULL default '0',q_id varchar(64) default '0',piority int(5) NOT NULL default '0' ,total int(20) unsigned default '0',PRIMARY KEY  (id),UNIQUE KEY id (id)) TYPE=MyISAM;",null);
    }

    public Question getQuestion(String id)  throws DataObjectNotFoundException, DaoException
    {
        try
        {
            String sql = " select id,title, question, total,assigned,pending,active,ispublic from poll_question where id = '" + id+"'";
            Collection col = super.select(sql,Question.class,null,0,-1);
            Question question = col.iterator().hasNext()?(Question)col.iterator().next() : null;
            if(question!=null)question.setAnswers(getAnswers(question));
            return question;
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public void updateQuestionTotal(Question question)
    {
        try{
            String sql = "UPDATE poll_question SET total=#total#" + " where id = #id#";
            super.update(sql,question);

        }catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }

    }

    public Answer[] getAnswers(String id) throws DataObjectNotFoundException, DaoException
    {
        try
        {
            String sql = "select id,answer,q_id,total,piority from poll_answer where q_id = '"+id+"' ORDER BY piority";
            Question question = getQuestion(id);
            Collection col = super.select(sql,Answer.class,null,0,-1);
            if(col!=null&&!col.isEmpty())
            {
                int index = 0;
                Answer[] answer = new Answer[col.size()];
                for(Iterator i = col.iterator();i.hasNext();index++)
                {
                   answer[index] = (Answer)i.next();
                    answer[index].setQuestion(question);
                }
                return answer;
            }
        /*    //Question question = getQuestion(id);
            if(question!=null)
            {
                Answer[] answers = getAnswers(question);
                question.setAnswers(answers);
                return answers;
            }*/
            return null;
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }


   public Answer[] getAnswers(Question question) throws DataObjectNotFoundException, DaoException
    {
        try
        {
            String sql = "select id,answer,q_id,total,piority from poll_answer where q_id = '"+question.getId()+"' ORDER BY piority";
            Collection col = super.select(sql,Answer.class,null,0,-1);
            if(col!=null&&!col.isEmpty())
            {
                int index = 0;
                Answer[] answer = new Answer[col.size()];
                for(Iterator i = col.iterator();i.hasNext();index++)
                {
                   answer[index] = (Answer)i.next();
                   answer[index].setQuestion(question);
                }
                return answer;
            }
            return null;
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }
    }

    public Answer getAnswer(String id) throws DataObjectNotFoundException, DaoException
    {
        try
        {
            String sql = "select id,answer,q_id,total,piority from poll_answer where id ="+id;
            Collection col = super.select(sql,Answer.class,null,0,-1);
            return col.isEmpty()? null : (Answer)col.iterator().next();
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
            throw new DaoException(e.toString());
        }

    }

    public void updateVote(String choice)
    {

    }

    public void updateAnswer(Answer answer)
    {
        try
        {
            String sql = "UPDATE poll_answer SET answer = #answer#,q_id = #q_id#, total=#total#,piority=#piority# " +
                             " WHERE id = #id#";
            super.update(sql,answer);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public void updateQuestion(Question question)
    {
        try
        {
            String sql = "UPDATE poll_question SET question = #question#,title=#title#, pending=#pending#,"+
                         "active=#active#,ispublic=#ispublic#,assigned=#assigned#,total=#total#" +
                         " where id = #id#";

            super.update(sql,question);

        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public Collection getQuestions(String filter, String sort, boolean desc, int startIndex, int maxRows)
    {
        try
        {
            if(filter==null) filter = "";
            String sql = " select id, question,title,total,assigned,pending,active,ispublic from poll_question"+
             " where question LIKE '%" + filter +"%'";
            if(sort!=null)
                sql += " ORDER BY "+sort;
            if(desc) sql+=" DESC"; ;


            return super.select(sql,Question.class,null,startIndex,maxRows);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public Question[] getQuestions()
    {
        try
        {
            String sql = " select id, question,title,total,assigned,pending,active,ispublic from poll_question";
            Collection col = super.select(sql,Question.class,null,0,-1);
            if(!col.isEmpty())
            {
                Question[] questions = new Question[col.size()];
                int i=0;
                for(Iterator iterator=col.iterator();iterator.hasNext();i++)
                {
                    questions[i] = (Question)iterator.next();
                    questions[i].setAnswers(getAnswers(questions[i]));
                }
                return questions;
            }

        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void deleteQuestion(String id)
    {
        try
        {
            Question question = getQuestion(id);
            Answer[] answers = getAnswers(id);
            if(answers!=null)
            {
                for(int i=0;i<answers.length;i++)
                    deleteAnswer(answers[i]);
            }
            String sql = "DELETE FROM poll_question where id=#id#";
            super.update(sql,question);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
    }

    public void deleteAnswer(Answer answer)
    {
        try
        {
            String sql = "DELETE FROM poll_answer where id=#id#";
            super.update(sql,answer);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
    }

    public Question[] getQuestionsByAssignment(String sectionID)
    {
      try
        {
            String sql =
                    "select id, question,title,total,assigned,pending,active,ispublic from poll_question where assigned = '"
                    + sectionID+"'";
            Collection col = super.select(sql,Question.class,null,0,-1);
            if(!col.isEmpty())
            {
                Question[] questions = new Question[col.size()];
                int i=0;
                for(Iterator iterator=col.iterator();iterator.hasNext();i++)
                {
                    questions[i] = (Question)iterator.next();
                    questions[i].setAnswers(getAnswers(questions[i]));
                }
                return questions;
            }

        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public String addQuestion(Question question)
    {
        try
        {
            String sql = "INSERT INTO poll_question(id,question,title,assigned,pending,active,ispublic,total) "+
                    "VALUES(#id#,#question#,#title#,#assigned#,#pending#,#active#,#ispublic#,#total#)";
            super.update(sql,question);
            return question.getId();
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
        return null;
    }


    public void addAnswer(String q_id,String answer,int order)
    {
        try
        {
            Answer newAnswer = new Answer();
            newAnswer.setQ_id(q_id);
            newAnswer.setAnswer(answer);
            newAnswer.setTotal(0);
            newAnswer.setPiority(order);
            String sql = "INSERT INTO poll_answer(answer,q_id,total,piority)"+
                         "VALUES(#answer#,#q_id#,#total#,#piority#)";
            super.update(sql,newAnswer);
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }

    }


}