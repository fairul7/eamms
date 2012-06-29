<%@ page import="kacang.Application"%>
<%@ include file="/common/header.jsp" %>

<%
	int navSelected = 0;
	int navCutOff   = 4;
	String[] navArray = new String[] {
							Application.getInstance().getMessage("sfa.message.selectCompany","Select Company"),
							Application.getInstance().getMessage("sfa.message.selectContacts","Select Contacts"),
                            Application.getInstance().getMessage("sfa.message.selectOpportunity","Select Opportunity"),
							Application.getInstance().getMessage("sfa.message.defineProductDistribution","Define Product Distribution"),
							Application.getInstance().getMessage("sfa.message.defineAccountDistribution","Define Account Distribution"),
							Application.getInstance().getMessage("sfa.message.selectPartner","Select Partner"),
							Application.getInstance().getMessage("sfa.message.selectPartnerContacts","Select Partner Contacts")
						};
	
	try {
		String navSel = (String) request.getAttribute("navSelected");
		navSelected = Integer.parseInt(navSel);
	} catch (Exception e) {
	}
%>

<table border="0" cellspacing="0" cellpadding="0">
    <tr>



        <td>

            <c:if test="${navSelected==0||navSelected==1}" >
                <b>
            </c:if>
            &nbsp;<fmt:message key='sfa.message.customer'/>
            <c:if test="${navSelected==0||navSelected==1}" >
                </b>
                 <font color="#990000">
                &gt; <%= navArray[navSelected] %> </font>

            </c:if>
                |
            </td>

        <td>
            <c:if test="${navSelected>1&&navSelected<5}" >
               <b>
            </c:if>
            &nbsp;<fmt:message key='sfa.message.opportunity'/>
            <c:if test="${navSelected>1&&navSelected<5}" >
                </b>     <font color="#990000">
                &gt; <%= navArray[navSelected] %> </font>
            </c:if>
            |
        </td>
        <td >
            <c:if test="${navSelected>4}" >
             <b>
            </c:if>
            &nbsp;<fmt:message key='sfa.message.partner'/>
            <c:if test="${navSelected>4}" >
                </b>
                <font color="#990000">
                &gt; <%= navArray[navSelected] %> </font>
            </c:if>
        </td>

    </tr>

</table>

<table width="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="border-top:1px solid #676767;border-bottom:1px solid #676767;padding:4px">
<font color="#999999" size="1" face="Verdana, Arial, Helvetica, sans-serif"><% /*for (int i=0; i<navArray.length; i++) {*/ %>
<%--
		<% if (i == navCutOff) { %><br><% } %>
--%>
		<%--<% if (i == navSelected) { %>--%><font color="#FF0000"><strong><%--<% } %>--%>

	<%--	<% if (i == navSelected) { %>--%></strong></font><%--<% } %>--%>
	<%/* } */%>
</font>
</td></tr>
</table>
