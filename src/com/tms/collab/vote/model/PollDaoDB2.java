package com.tms.collab.vote.model;

import java.util.Collection;

public class PollDaoDB2 extends PollDao{
	
	public Collection getQuestions(String filter, String sort, boolean desc, int startIndex, int maxRows)
    {
        try
        {
            if(filter==null) filter = "";
            String sql = " select id, question,title,total,assigned,pending,active,ispublic from poll_question"+
             " where UPPER(question) LIKE '%" + filter.toUpperCase() +"%'";
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

}
