<%-- 2011-06-10: (for FMS) remove body tag from Description --%>
<%-- 2011-06-10: modified to clean up fms_tran_request.remarks --%>
<%@ page import="kacang.*, kacang.model.*, com.tms.fms.transport.model.*, java.util.*, java.io.*" %>

<%!
	public String escapeTag(String s) {
		String result = "";
		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i);        
		    if (c == '<') {
		    	result += "&lt;";
		    } else if (c == '>') {
		    	result += "&gt;";
		    } else {
		    	result += c;
		    }
		}
		return result;
	}

	public void fixDesc(String tag, int rows, JspWriter out) throws IOException, DaoException {
		Application app = Application.getInstance();
		TransportDao dao = (TransportDao) app.getModule(TransportModule.class).getDao();
		
		String sqlFind = 
			"SELECT id, remarks " +
			"FROM fms_tran_request " + 
			"WHERE remarks LIKE ? ";
		Collection col = dao.select(sqlFind, TransportRequest.class, new String[] {tag + "%"}, 0, rows);
		out.println(escapeTag(tag) + " found " + col.size() + " times<br>");
		
		String sqlFix = 
			"UPDATE fms_tran_request " +
			"SET remarks = ? " +
			"WHERE id = ? ";
		
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			TransportRequest tr = (TransportRequest) iterator.next();
			String requestId = tr.getId();
			String remarks = tr.getRemarks().toString().replaceFirst(tag, "");
			
			dao.update(sqlFix, new String[] {remarks, requestId});
			//out.println("requestId = " + requestId + "<br>remarks = " + escapeTag(remarks) + "<br><br>");
		}
	}
%>

<%
	fixDesc("<body>", -1, out);
	fixDesc("<body />", -1, out);
%>

<br><br>
Script Run Successfully