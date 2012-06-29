package com.tms.collab.vote.model;

import kacang.model.DefaultModule;

import java.util.Collection;


public class PollModule extends DefaultModule
{
    public Question getQuestion(String id) throws Exception
    {
        PollDao pollDao = (PollDao) getDao();
        Question question = pollDao.getQuestion(id);
        return question;
    }


    public Answer getAnswer(String id) throws Exception
    {
        PollDao pollDao = (PollDao) getDao();
        Answer answer = pollDao.getAnswer(id);
        Question question = getQuestion(answer.getQ_id());
        answer.setQuestion(question);
        return answer;
    }

    public synchronized void vote(Question question, String choice)
    {
        try{
            PollDao pollDao = (PollDao)getDao();
            Question q = pollDao.getQuestion(question.getId());
            q.vote();
            pollDao.updateQuestionTotal(q);
            Answer[] answers = q.getAnswers();
            for(int i=0;i<answers.length;i++)
            {
                if(answers[i].getId().equals(choice))
                {
                    answers[i].vote();
                    pollDao.updateAnswer(answers[i]);
                }
        }}catch(Exception e){}
    }


    public Answer[] getAnswers(String id)
    {
        try{
        PollDao pollDao = (PollDao) getDao();
        return pollDao.getAnswers(id);
        }catch(Exception e){}
        return null;

    }

    public void calculatePercentage(Answer answers[])
    {

        for(int i=0; i<answers.length;i++)
        {
            if(answers[i].getQuestion().getTotal()<=0)
            {
                answers[i].setPercentage(0.0f);
            }
            else
            {
                float Percentage = ((float)answers[i].getTotal()
                        /(float)answers[i].getQuestion().getTotal())*100;
                answers[i].setPercentage(Percentage);
            }
        }
    }

    public Question[] getQuestions()
    {
           PollDao pollDao = (PollDao) getDao();
           return pollDao.getQuestions();
    }

    public void updateAnswer(Answer answer)
    {
        PollDao pollDao = (PollDao) getDao();
        pollDao.updateAnswer(answer);
    }

    public void deleteQuestion(String id)
    {
        PollDao pollDao = (PollDao) getDao();
        pollDao.deleteQuestion(id);
    }
    public void deleteAnswer(Answer answer)
    {
        PollDao pollDao = (PollDao) getDao();
        Question question = answer.getQuestion();
        if(question!=null)
        {
            long question_total,answer_total;
            question_total = question.getTotal();
            answer_total = answer.getTotal();
            question.setTotal(question_total-answer_total);
            pollDao.updateQuestion(question);
        }
        pollDao.deleteAnswer(answer);
    }

    public String addQuestion(Question question)
    {
        try
        {
            PollDao pollDao = (PollDao) getDao();
            return pollDao.addQuestion(question);
        }
        catch(Exception e)
        {        }
        return null;
    }

    public Answer[] addAnswer(String q_id,String answer,int order)
    {
        try{
        PollDao pollDao = (PollDao) getDao();
        pollDao.addAnswer(q_id,answer,order);
        return pollDao.getAnswers(q_id);
        }
        catch(Exception e)
        {        }
        return null;
    }

    public void updateQuestion(Question question)
    {
        try
        {
            PollDao pollDao = (PollDao) getDao();
            pollDao.updateQuestion(question);

        }
        catch(Exception e)
        {        }
    }

    public void removePreviousAssignment(Question question)
    {
        try
        {
            if(!question.getAssigned().equals("0"))
            {
                PollDao pollDao = (PollDao) getDao();
                Question[] questions = pollDao.getQuestionsByAssignment(question.getAssigned());
                for(int i=0;i<questions.length;i++)
                {
                    questions[i].setAssigned("0");
                    updateQuestion(questions[i]);
                }
            }
        }
        catch(Exception e)
        {        }
    }

    public Question getQuestionByAssignment(String sectionID)
    {
        try
        {
            PollDao pollDao = (PollDao) getDao();
            Question[] question = pollDao.getQuestionsByAssignment(sectionID);
            if(question!=null&&question.length!=0)
                return question[0];
        }
        catch(Exception e)
        {        }
        return null;
    }

    public Collection getQuestionsAsCollection(String filter, String sort, boolean desc, int startIndex, int maxRows)
    {
        try
        {
            PollDao pollDao = (PollDao) getDao();
            return pollDao.getQuestions(filter, sort, desc, startIndex,maxRows);
        }
        catch(Exception e)
        {        }
        return null;
    }

    public void clearVoteResult(String id)
    {
        try
        {
            Question question = getQuestion(id);
            question.setTotal(0);
            updateQuestion(question);
            Answer[] answers = question.getAnswers();
            if(answers!=null)
            {
                for(int i=0;i<answers.length;i++)
                {
                    answers[i].setTotal(0);
                    updateAnswer(answers[i]);
                }
            }

        }
        catch(Exception e)
        {        }
    }


}