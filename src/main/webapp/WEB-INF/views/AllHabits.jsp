<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<head>
    <title>List habits</title>
</head>
<body>
<h3 align="center">Habits</h3>
<table border="2" align="center" cellpadding="5">
    <tr>
        <td>Habit</td>
        <td>First day</td>
        <td>Details</td>
    </tr>

    <c:forEach items="${habits}" var="habit">
        <tr>
            <td>
                <c:out value="${habit.name}"/>
            </td>

            <td><c:out value="${habit.firstDay}"/></td>
            <td>
                <form action="${pageContext.request.contextPath}/habits/habit" method="get">
                    <input type="hidden" name="habitId" value="${habit.id}"/>
                    <input type="submit" value="More info">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<h3 align="center">Add habit</h3>
<form align="center" action="${pageContext.request.contextPath}/habits/all" method="post">
    <input type="text" name="habitName" required>
    <input type="date" name="firstDay" required>
    <input type="submit" value="Save">
</form>
</body>
</html>