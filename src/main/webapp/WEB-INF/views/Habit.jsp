<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <title><c:out value="${habit.name}"/> habit</title>
</head>
<body>
<h3 align="center"><c:out value="${habit.name}"/> Habit</h3>
<table border="2" align="center" cellpadding="5">
    <tr>
        <td>Day number</td>
        <td>Date</td>
        <td>Done?</td>
    </tr>

    <c:forEach items="${habit.acts}" var="act">
        <tr>
            <td>
                <c:out value="${act.dayNumber}"/>
            </td>
            <td>
                <c:out value="${act.completionDate}"/>
            </td>
            <td>
                <c:choose>
                    <c:when test="${act.executed == 'U'}">
                        <form action="${pageContext.request.contextPath}/habits/habit" method="post">
                            <input name="done" type="radio" value="Y"> Yes
                            <input name="done" type="radio" value="N"> No <br>
                            <input type="hidden" name="habitId" value="${habit.id}"/>
                            <input type="hidden" name="dayNumber" value="${act.dayNumber}"/>
                            <input type="submit" value="reply">
                        </form>
                    </c:when>
                    <c:when test="${act.executed == 'Y'}">
                        Done
                    </c:when>
                    <c:when test="${act.executed == 'N'}">
                        Not done
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </c:forEach>


</table>
</body>
</html>