<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="templates/header.jsp" />
<h3>Добавление / Редактирование счета</h3>

<body onload="init()">
	<form action="search.do" method="POST">
		<input type="hidden" name="idInvoice" value="${invoice.id}" />
		<div class="numberDateInvoice">
			<div>
				Номер счета
				<input type="text" size="10" name="numberInvoice" value="${invoice.number}" required onkeypress="return isDigit(event)">
			</div>
			<div>
				Дата счета
				<input type="text" size="10" name="dateInvoice" id="dateInvoice" value="${invoice.date}" required>
			</div>
		</div>

		<div class="seller">
			<div>
				Наименование продавца:
				<select name="idSeller" onchange="selectAddress()">
					<c:forEach var="seller" items="${listSellers}">
						<c:choose>
							<c:when test="${seller.id==invoice.seller.id}">
								<option label='${seller.name}' value="${seller.id}" selected>${seller.address}</option>
							</c:when>
							<c:otherwise>
								<option label='${seller.name}' value="${seller.id}">${seller.address}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</div>
			<div>
				Адрес продавца:
				<input type="text" size="75" readonly>
			</div>
		</div>

		<table class="tableLines">
			<caption>Строки в счете:</caption>
			<tr>
				<th></th>
				<th>Описание</th>
				<th>Количество</th>
				<th>Цена</th>
				<th>Сумма</th>
			</tr>
			<c:choose>
				<c:when test="${invoice == null}">
					<tr>
						<td>
							<input type="checkbox">
						</td>
						<td>
							<input type="text" size="30" name="descriptionLine" required>
						</td>
						<td>
							<input type="text" size="10" name="amountLine" required onchange="calculateSum()" onkeypress="return isDigit(event)">
						</td>
						<td>
							<input type="text" size="10" name="priceLine" required onchange="calculateSum()" onkeypress="return isDigit(event)">
						</td>
						<td>
							<input type="text" size="10" name="sumLine" required onkeypress="return false">
						</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="line" items="${invoice.listLines}">
						<tr>
							<td>
								<input type="checkbox">
							</td>
							<td>
								<input type="text" size="30" name="descriptionLine" value="${line.description}" required>
							</td>
							<td>
								<input type="text" size="10" name="amountLine" value="${line.amount}" required onchange="calculateSum()" onkeypress="return isDigit(event)">
							</td>
							<td>
								<input type="text" size="10" name="priceLine" value="${line.price}" required onchange="calculateSum()" onkeypress="return isDigit(event)">
							</td>
							<td>
								<input type="text" size="10" name="sumLine" value="${line.sum}" required onkeypress="return false">
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
				</c:choose>
		</table>

		<div class="totalSum">
			Итого по счету:
			<input type="text" size="10" name="sumInvoice" value="${invoice.sum}" required onkeypress="return false">
		</div>

		<div class="iconsPlusMinus">
			<div><img src="webcontent/images/plus.png" onclick="addNewLine()">Добавить позицию</div>
			<div><img src="webcontent/images/minus.png" onclick="deleteLine()">Удалить позицию</div>
		</div>

		<div class="buttonsSaveCancel">
			<button type="submit" name="command" value="save" onclick="unformatDigit()">Сохранить изменения</button>
			<a href="/invoices"><button type="button">Отмена</button></a>
		</div>
	</form>
</body>

</html>
