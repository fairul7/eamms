package com.tms.fms.facility.model;

import kacang.model.DaoException;
import kacang.model.DataSourceDao; 
import kacang.model.DefaultDataObject;

import java.util.ArrayList;
import java.util.Collection;
public class MonitorDao  extends DataSourceDao {

	public Collection showTop10SlowQuery() throws DaoException{ 
		String sql = "select top 20 "+ 
	    "sum(qs.total_worker_time) as total_cpu_time,  "+
	    "sum(qs.execution_count) as total_execution_count, "+
	    "count(*) as  number_of_statements,  "+
	    "qs.plan_handle  "+
		"from  "+
	    "sys.dm_exec_query_stats qs "+
		"group by qs.plan_handle "+
		"order by sum(qs.total_worker_time) desc " ;
		
		return super.select(sql, DefaultDataObject.class, null, 0, -1);
	}
	
	public Collection showHandlerQuery(DefaultDataObject sqlHandler) throws DaoException{ 
		Object lala =  sqlHandler.getProperty("plan_handle"); 
		 
		String sql = "select top 1 "+
		   " sql_text.text,  sql_handle, "+
		   " plan_generation_num,  execution_count,  dbid,  objectid  "+
		   " from  "+
		   " sys.dm_exec_query_stats a "+
		   " cross apply sys.dm_exec_sql_text(?) as sql_text "+
		   " where  "+
		   " plan_generation_num >1 "+
		   " order by plan_generation_num desc " ;
		
		return super.select(sql, DefaultDataObject.class, new Object[]{lala}, 0, -1); 
	}

}
