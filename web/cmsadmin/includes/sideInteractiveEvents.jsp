<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='calendar.label.events'/></span>
                <br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">
                <li><a href="eventList.jsp" class="option"><fmt:message key='calendar.label.eventsListing'/></a></li>
                <br>
                <li><a href="eventMonthlyView.jsp" class="option"><fmt:message key='calendar.label.monthlyView'/></a></li>
                <br>
                <li><a href="eventAdd.jsp" class="option"><fmt:message key='calendar.label.newEvent'/></a></li>
                <br>

                <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>
        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>
