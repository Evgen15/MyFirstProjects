<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="templates/header.jsp" />
<body>
	<c:import url="templates/search.jsp" />
	<h3>
         Счета за период с
         <c:out value="${sessionScope.startDate}" /> по
         <c:out value="${sessionScope.endDate}" /> :
      </h3>
	<form action="edit.do" method="POST">
		<table class="resultTable">
			<tr>
				<th></th>
				<th>Номер</th>
				<th>Дата</th>
				<th>Продавец</th>
				<th>Сумма</th>
			</tr>
			<c:forEach var="invoice" items="${invoicesList}">
				<tr>
					<td>
						<input type="radio" name="idInvoice" value="${invoice.id}" required>
					</td>
					<td>${invoice.number}</td>
					<td>
						<script>
							document.write(formatDate("${invoice.date}"));
						</script>
					</td>
					<td>${invoice.seller.name}</td>
					<td>
						<script>
							document.write(formatDigit("${invoice.sum}"));
						</script>
					</td>
					<td class="hideEditDelButtons">
						<button type="submit" name="command" value="edit">редактировать</button>
						<button type="submit" name="command" value="delete">удалить</button>
					</td>
				</tr>
			</c:forEach>
		</table>
	</form>
</body>

</html>
