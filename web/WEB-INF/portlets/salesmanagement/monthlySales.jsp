<%@ page import="java.util.Calendar,
                 java.util.Date,
                 java.text.SimpleDateFormat,
                 kacang.Application,
                 com.tms.crm.sales.model.OpportunityModule,
                 com.tms.crm.sales.model.Opportunity,
                 java.util.Collection,
                 java.util.Iterator,
                 java.text.NumberFormat,
                 kacang.ui.WidgetManager,
                 kacang.services.security.SecurityService,
                 com.tms.crm.sales.misc.AccessUtil"%>
<%@ include file="/common/header.jsp" %>

<%
	WidgetManager wm     = WidgetManager.getWidgetManager(request);
	String        userID = wm.getUser().getId();
	
	if(AccessUtil.isSalesManager(userID)){
       Calendar cal = Calendar.getInstance();
       cal.setTime(new Date());
       String widgetName = request.getParameter("cn");
       String action = request.getParameter("act");
       int month,year;
       month = cal.get(Calendar.MONTH);
       year = cal.get(Calendar.YEAR);
       if(widgetName!=null&&action!=null&&widgetName.equals("monthlySales")&&action.equals("goto")){
           String sMonth = request.getParameter("month");
           String sYear = request.getParameter("year");
           if(sMonth!=null&&sMonth.trim().length()>0){
               month = Integer.parseInt(sMonth);
           }
           if(sYear!=null&&sYear.trim().length()>0){
               year = Integer.parseInt(sYear);
           }
       }
       cal.set(Calendar.MONTH, month);
       cal.set(Calendar.YEAR,year);
       SimpleDateFormat formatter = new SimpleDateFormat("MMMMMM");
       String sMonth = formatter.format(cal.getTime());
       formatter.applyPattern("yyyy");
       String sYear = formatter.format(cal.getTime());
       Date displayDate = cal.getTime();


       cal.add(Calendar.MONTH,-1);
       int pMonth = cal.get(Calendar.MONTH);
       int pYear = cal.get(Calendar.YEAR);
       cal.add(Calendar.MONTH,2);
       int nMonth = cal.get(Calendar.MONTH);
       int nYear = cal.get(Calendar.YEAR);
       //Date nextDate = cal.getTime();
   %>







<table width="100%">

    <tr>
        <td align="left">

        <a href="?cn=monthlySales&act=goto&month=<%=pMonth%>&year=<%=pYear%>"><<</a>
        <%=sMonth%>
        <a href="?cn=monthlySales&act=goto&month=<%=nMonth%>&year=<%=nYear%>">>></a>
        <%=sYear%>

        </td>
    </tr>

    <tr>
        <td align="left" >
            <table border="1" cellspacing=0 width="100%">
            <tr>
                <td>
                  <b> Opportunity</b>
                </td>
                <td>
                    <b>Company</b>
                </td>
                <td>
                    <b>Value</b>
                </td>

            </tr>

                <%
                    cal.setTime(displayDate);
                    cal.set(year,month,1,0,0,0);
                    Date startDate = cal.getTime();
                    cal.add(Calendar.MONTH,1);
                    cal.add(Calendar.SECOND,-1);
                    Date endDate = cal.getTime();
                    OpportunityModule module = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
                    Collection col = module.listOpportunities(null,null,null,null,Opportunity.STATUS_CLOSE.intValue(),false,false,startDate,endDate,"opportunityEnd",false,0,-1);
                    double total = 0;
                    int i = 0 ;
                    for (Iterator iterator = col.iterator(); iterator.hasNext();) {
                        Opportunity opportunity = (Opportunity) iterator.next();

                %>

                <tr>
                    <Td>
                    <a href="<c:url value="/ekms/sfa/closedsale_details.jsp"/>?opportunityID=<%=opportunity.getOpportunityID()%>">
                        <%=opportunity.getOpportunityName()%>
                        </a>
                    </td>

                    <Td>
                    <a href="<c:url value="/ekms/sfa/company_view_commain.jsp"/>?companyID=<%=opportunity.getCompanyID()%>">
                        <%=opportunity.getProperty("companyName")%>
                    </td>

                    <Td>
                        <%=NumberFormat.getInstance().format(opportunity.getOpportunityValue())%>
                    </td>

                </tr>

                <%
                        i++;
                        total += opportunity.getOpportunityValue();
                    }

                    if(i>0){
                %>
                <tr>
                <td colspan=2 align="right">
                    <b>Total</b>&nbsp;

                </td>
                <td>
                    <%=NumberFormat.getInstance().format(total)%>
                </td>

                </tr>

                <%}else{%>
                <tr>
                    <td colspan=3>
                        No sales for this month.
                    </td>
                </tr>

                <%}%>


            </table>
        </td>
    </tr>




</table>
<%}else{
        out.print("You don't have the access to view this portlet.");
    }
        %>
