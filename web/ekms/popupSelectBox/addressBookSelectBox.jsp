<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>
<x:config>
    <page name="messagingContacts">
    <tabbedpanel name="tab1" width="95%">
    <panel name="panel1" text="Intranet Users">
        <com.tms.collab.directory.ui.UserAppointmentTable name="table2" sort="username" />
    </panel>
    <panel name="panel2" text="Business Directory">
        <com.tms.collab.directory.ui.DirectoryContactAppointmentTable name="table3" sort="firstName" />
    </panel>
    <panel name="panel3" text="Personal Contacts">
        <com.tms.collab.directory.ui.ContactAppointmentTable name="table1" sort="firstName" />
    </panel>
    </tabbedpanel>
    </page>
</x:config>


<c:if test="${!empty param.popupSelectBoxCn}">
    <c:set var="popupSelectBoxCn" value="${popupSelectBoxCn}" scope="session" />
</c:if>
<c:if test="${forward.name == 'select_intranet'}">
    <script>
    	<c:set var="keys" value="${widgets['messagingContacts.tab1.panel1.table2'].keys}"/>
    	<c:set var="values" value="${widgets['messagingContacts.tab1.panel1.table2'].values}"/>
       // alert("hi"+'<c:out value="${keys}"/>'+" - "+'<c:out value="${values}"/>');
		window.opener.popupSelectBoxPopulate('<c:out value="${keys}"/>','<c:out value="${values}"/>');
    </script>
</c:if>
<c:if test="${forward.name == 'select_biz'}">
    <script>
    	<c:set var="keys" value="${widgets['messagingContacts.tab1.panel2.table3'].keys}"/>
    	<c:set var="values" value="${widgets['messagingContacts.tab1.panel2.table3'].values}"/>
      //  alert("hi"+'<c:out value="${keys}"/>'+" - "+'<c:out value="${values}"/>');
		window.opener.popupSelectBoxPopulate('<c:out value="${keys}"/>','<c:out value="${values}"/>');
    </script>
</c:if>
<c:if test="${forward.name == 'select_personal'}">
    <script>
    	<c:set var="keys" value="${widgets['messagingContacts.tab1.panel3.table1'].keys}"/>
    	<c:set var="values" value="${widgets['messagingContacts.tab1.panel3.table1'].values}"/>
       // alert("hi"+'<c:out value="${keys}"/>'+" - "+'<c:out value="${values}"/>');
		window.opener.popupSelectBoxPopulate('<c:out value="${keys}"/>','<c:out value="${values}"/>');
    </script>
</c:if>
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='addressbook.label.selectRecipients'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
 
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor" align="center">
        <x:display name="messagingContacts.tab1" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
</body>
</html>
