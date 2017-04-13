<div class="searchInvoices">
	<form action="search.do" method="POST">
		<p>Укажите период, за который необходимо отобразить счета:</p>
		<div class="period">
			<div>
				дата начала
				<input type="text" size="7" name="startDate" id="startDate" required>
			</div>
			<div>
				и дата окончания
				<input type="text" size="7" name="endDate" id="endDate" required>
			</div>
			<div>
				<button type="submit" name="command" value="search">Показать</button>
			</div>
		</div>
	</form>
</div>
<form action="add.do" method="POST">
	<button type="submit" name="command" value="edit">Добавить новый счет</button>
</form>
