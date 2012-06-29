<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

 <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='vote.label.votes'/></span>
                <br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">
              <li><a href="pollAdmin.jsp?event=view" class="option"><fmt:message key='vote.label.questionListing'/></a></li>
              <br>
              <li><a href="pollAdmin.jsp?event=newVote" class="option"><fmt:message key='vote.label.newVote'/></a></li>
              <br> <br> <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>