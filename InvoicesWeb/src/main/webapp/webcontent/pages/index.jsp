<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="templates/header.jsp" />
<body>
	<c:choose>
		<c:when test="${param.add != null}">
			<h3>Счет добавлен в базу.</h3>
		</c:when>
		<c:when test="${param.edit != null}">
			<h3>Счет отредактирован.</h3>
		</c:when>
		<c:otherwise>
			<h3>Добро пожаловать!</h3>
		</c:otherwise>
	</c:choose>
	<c:import url="templates/search.jsp" />
</body>

</html>
