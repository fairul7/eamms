<%@ include file="/common/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="images/default.css" media="all" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Preview Quotation</title>
  </head>
  <body>
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td>
          <img src="images/tms_lg.png"/>
<%--  
    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_LetterHead"/>
--%>
        </td>
      </tr>
      <tr>
        <td>
    
          <div class="document">
            <x:template name="preview" type="com.tms.quotation.generator.GenerateQuotation" properties="quotationId=${param.quotationId}"/>
          </div>
        </td>
      </tr>
    </table>
  </body>
</html>