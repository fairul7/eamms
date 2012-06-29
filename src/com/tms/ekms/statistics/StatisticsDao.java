package com.tms.ekms.statistics;

import kacang.model.*;
import java.util.*;

public class StatisticsDao extends DataSourceDao
{
	//TODO: this is temporary only. Remove when LogService.getUsage has been changed.
	protected Collection selectUsage_Temp(DaoQuery query) throws DaoException
	{
		return super.select("SELECT COUNT(entryLabel) AS \"count\", entryLabel, userId FROM log_entry " +
			"WHERE 1=1 " + query.getStatement() + " GROUP BY entryLabel, userId", HashMap.class, query.getArray(), 0, -1);
	}
}
