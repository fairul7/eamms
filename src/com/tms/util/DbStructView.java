package com.tms.util;

import kacang.util.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;

public class DbStructView {
	private static final boolean ALLOW_GENERIC_CHARACTER = true;
	
	private DataSource dataSource;
	private HashMap cachedPrimaryKeys;
	
	/*
		//Used to compare database structure
		
		<%	
			DataSourceFactory dsf = DataSourceFactory.getInstance();
		
			DataSource defaultdb = dsf.getDataSource("defaultdb");
			pageContext.setAttribute("defaultdb", defaultdb);
		%>
		
		<%
			Date start = new Date();
			DbStructView dbStructView = new DbStructView(defaultdb);
			ArrayList tableList = dbStructView.tableList();
			dbStructView.loadPrimaryKey(tableList);
			
			out.println("<pre>");
			for (Iterator iterator = tableList.iterator(); iterator.hasNext();) {
				String tableName = (String) iterator.next();
				String struct = dbStructView.tableStruct(tableName, "\n");
				out.println(struct);
			}
			Date end = new Date();
			out.println("Total elapsed: " + (end.getTime() - start.getTime()) + " ms");
			out.println("</pre>");
		%>
	*/
	
	public DbStructView(DataSource dataSource) {
		this.dataSource = dataSource;
		cachedPrimaryKeys = new HashMap();
	}
	
	public ArrayList tableList() {
		Connection conn = null;
		ArrayList result = new ArrayList();
		
		try {
			ResultSet rs;
			conn = dataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			
			rs = dbmd.getTables(null, null, "%", new String[] {"TABLE"});
			while(rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				result.add(tableName);
			}
			rs.close();
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
			}
		}
		return result;
	}
	
	public void loadPrimaryKey(ArrayList tableList) {
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			
			for (int i=0; i < tableList.size(); i++) {
				String tableName = (String) tableList.get(i);
				
				ResultSet rs = dbmd.getPrimaryKeys(null, null, tableName);
				while(rs.next()) {
					String pkName = rs.getString("PK_NAME");
					String columnName = rs.getString("COLUMN_NAME");
					
					HashMap entry = (HashMap) cachedPrimaryKeys.get(tableName);
					if (entry == null) {
						entry = new HashMap();
						entry.put("pkColumns", new ArrayList());
						cachedPrimaryKeys.put(tableName, entry);
					}
					ArrayList pkColumns = (ArrayList) entry.get("pkColumns");
					pkColumns.add(columnName);
					if (pkName != null) {
						entry.put("pkName", pkName);
					}
				}
				rs.close();
			}
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
			}
		}
	}
	
	public HashMap getCachedPrimaryKey(String tableName) {
		return (HashMap) cachedPrimaryKeys.get(tableName);
	}
	
	public ResultSetMetaData tableDef(String tableName) {
		Connection conn = null;
		ResultSetMetaData rsmd = null;
		
		try {
			ResultSet rs;
			conn = dataSource.getConnection();
			
			Statement stmt = conn.createStatement();
			stmt.setMaxRows(1);
			rs = stmt.executeQuery("SELECT * FROM " + tableName);
			rsmd = rs.getMetaData();
			
			rs.close();
			
		} catch (Exception e) {
			Log.getLog(getClass()).error(e.toString(), e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Log.getLog(getClass()).error(e.toString(), e);
				}
			}
		}
		return rsmd;
	}
	
	public String tableStruct(String tableName, String newline) {
		return tableStruct(tableName, true, newline, "\t");
	}
	
	public String tableStruct(String tableName, boolean generic, String newline, String spacing) {
		ResultSetMetaData rsmd = tableDef(tableName);
		HashMap pkInfo = getCachedPrimaryKey(tableName);
		
		StringBuffer sb = new StringBuffer();
		sb.append("table: " + tableName + " {" + newline);
		try {
			for (int i=1; i<=rsmd.getColumnCount(); i++) {
				String field = formatField(rsmd, i, generic, spacing);
				sb.append(field + newline);
			}
			if (pkInfo != null) {
				sb.append(formatPrimaryKey(pkInfo, generic, spacing) + newline);
			}
		} catch (SQLException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
		sb.append("}" + newline);
		
		return sb.toString();
	}
	
	protected String formatField(ResultSetMetaData rsmd, int columnNum, boolean generic, String spacing) throws SQLException {
		String field = spacing + rsmd.getColumnName(columnNum);
		
		String columnTypeName = rsmd.getColumnTypeName(columnNum);
		int precision =  rsmd.getPrecision(columnNum);
		int scale =  rsmd.getScale(columnNum);
		field = field + spacing + getTypeName(columnTypeName, precision, scale, generic);
		
		if (rsmd.isNullable(columnNum) == ResultSetMetaData.columnNullable) {
			field = field + spacing + "NULL";
		} else if (rsmd.isNullable(columnNum) == ResultSetMetaData.columnNoNulls) {
			field = field + spacing + "NOT NULL";
		}
		
		return field;
	}
	
	protected String getTypeName(String columnTypeName, int precision, int scale, boolean generic) {
		columnTypeName = columnTypeName.toLowerCase();
		if (precision != 0) {
			if (ALLOW_GENERIC_CHARACTER) {
				if (generic && columnTypeName.equals("varchar")) {
					columnTypeName = "character";
				}
				if (generic && columnTypeName.equals("char")) {
					columnTypeName = "character";
				}
			}
			
			if (generic && columnTypeName.equals("datetime")) {
				// ignore precision & scale
			} else if (generic && columnTypeName.equals("text")) {
				// ignore precision & scale
			} else if (generic && columnTypeName.startsWith("int")) {
				columnTypeName = "integer";
			} else {
				String extra = "(" + precision + ")";
				if (scale != 0) {
					extra = "(" + precision + ", " + scale + ")";
				}
				columnTypeName = columnTypeName + extra;
			}
		}
		return columnTypeName;
	}
	
	protected String formatPrimaryKey(HashMap pkInfo, boolean generic, String spacing) {
		String pkName = "";
		if (!generic && pkInfo.get("pkName") != null) {
			pkName = pkInfo.get("pkName") + " ";
		}
		
		String cols = "";
		ArrayList pkColumns = (ArrayList) pkInfo.get("pkColumns");
		for (int i=0; i< pkColumns.size(); i++) {
			String column = (String) pkColumns.get(i);
			if (i != 0) {
				cols = cols + ", ";
			}
			cols = cols + column;
		}
		
		return spacing + "PRIMARY KEY: " + pkName + "(" + cols + ")";
	}
}
