package com.tms.ekms.util;

import java.sql.*;

public class JdbcUtilSybase extends JdbcUtilMsSql {
	public void populatePreparedStatement(PreparedStatement pstmt, int index, Object value) throws SQLException {
		if (value == null)
			pstmt.setString(index, null);
		else if (value instanceof String)
			pstmt.setString(index, (String) value);
		else if (value instanceof Boolean)
			pstmt.setString(index, ((Boolean) value).booleanValue() ? "1" : "0");
		else if (value instanceof Integer)
			pstmt.setInt(index, ((Integer) value).intValue());
		else if (value instanceof Long)
			pstmt.setLong(index, ((Long) value).longValue());
		else if (value instanceof Double)
			pstmt.setDouble(index, ((Double) value).doubleValue());
		else if (value instanceof Float)
			pstmt.setFloat(index, ((Float) value).floatValue());
		else if (value instanceof java.util.Date)
			pstmt.setTimestamp(index, new Timestamp(((java.util.Date) value).getTime()));
		else
			pstmt.setObject(index, value);
	}
}
