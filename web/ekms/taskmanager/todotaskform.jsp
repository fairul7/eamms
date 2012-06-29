<%@include file="/common/header.jsp"%>


<!-- Page/Widget Definition -->
<x:config reloadable="${param.reload}">
    <page name="taskformpage">
         <com.tms.collab.taskmanager.ui.TaskForm name="taskform"/>
    </page>
</x:config>

<c:if test="${forward.name=='conflict exception'}">
    <%    session.setAttribute("edit",Boolean.FALSE);%>

    <c:redirect url="/ekms/calendar/conflicts.jsp" ></c:redirect>
</c:if>

<%  String id = request.getParameter("id");
    boolean edit = false;
    if(id!=null&&id.trim().length()>0){
        edit = true;

%>
    <x:set name="taskformpage.taskform" property="taskId" value="<%=id%>" />
<%}%>

<HTML>
<HEAD>
<TITLE><fmt:message key='taskmanager.label.tmsINTRANETMessaging'/></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">


<SCRIPT LANGUAGE="JavaScript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
//-->
</SCRIPT>
</HEAD>

<BODY BGCOLOR="#C0C0C0" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">

<TABLE WIDTH="100%" BORDER="0" CELLPADDING="0" CELLSPACING="0">
  <TR>
    <TD ALIGN="LEFT" VALIGN="MIDDLE" BGCOLOR="#003399" COLSPAN="2"><IMG SRC="images/blank.gif" WIDTH="5" HEIGHT="2"></TD>
  </TR>
<FORM>
<TR>
    <TD VALIGN="MIDDLE"><FONT FACE="Verdana, Arial, Helvetica, sans-serif" SIZE="2" COLOR="#FFFFFF"></FONT>&nbsp;<FONT FACE="Verdana, Arial, Helvetica, sans-serif" SIZE="2" COLOR="#FFFFFF"></FONT>&nbsp;<FONT FACE="Verdana, Arial, Helvetica, sans-serif" SIZE="2" COLOR="#FFFFFF"></FONT><IMG SRC="images/logo2.gif" WIDTH="75" HEIGHT="25" ALIGN="ABSMIDDLE"></TD>
      <TD VALIGN="MIDDLE" ALIGN="RIGHT">
        <SELECT NAME="select12">


          <OPTION><fmt:message key='taskmanager.label.appointment'/></OPTION>


          <OPTION><fmt:message key='taskmanager.label.toDoTask'/></OPTION>


          <OPTION><fmt:message key='taskmanager.label.event'/></OPTION>


        </SELECT>
                        <IMG SRC="images/ic_add.gif" WIDTH="30" HEIGHT="18" ALT="Click here to add an entry" BORDER="0" ALIGN="ABSMIDDLE" onMouseDown="MM_openBrWindow('addtodo.htm','addtodo','scrollbars=yes,resizable=yes,width=450,height=380')">&nbsp;</TD>
  </TR></FORM>
  <TR>
    <TD COLSPAN="2" BGCOLOR="#0066CC"><IMG SRC="images/blank.gif" WIDTH="5" HEIGHT="2"></TD>
  </TR>
  <TR>
    <TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#003399"><FONT SIZE="2" FACE="Verdana, Arial, Helvetica, sans-serif" COLOR="#FFFFFF"><B><%
        if(edit){
%><fmt:message key='taskmanager.label.editingToDoTask'/> <%}else{%><fmt:message key='taskmanager.label.addingANewToDoTask'/><%}%></B></FONT>
</TD>
  </TR>
  <TR>
    <TD COLSPAN="2" BGCOLOR="#000037"><IMG SRC="images/blank.gif" WIDTH="5" HEIGHT="3"></TD>
  </TR>
</TABLE>

<TABLE WIDTH="100%" BORDER="0" CELLSPACING="0" CELLPADDING="0">







  <TR>

    <TD BGCOLOR="#EFEFEF" VALIGN="TOP" CLASS="contentBgColor" COLSPAN="2"><IMG SRC="images/blank.gif" WIDTH="83" HEIGHT="10"></TD>
 </tr><tr><td  BGCOLOR="#EFEFEF">
 <x:display name="taskformpage.taskform" ></x:display>
 </td>

 </TR>
       </table>
