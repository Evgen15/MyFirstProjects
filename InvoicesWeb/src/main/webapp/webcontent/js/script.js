$(function() {
	$("#startDate").datepicker();
	$("#endDate").datepicker();
	$("#dateInvoice").datepicker();
	$.datepicker.regional["ru"] = {
		monthNames: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
			"Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
		dayNamesMin: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
		dateFormat: "dd.mm.yy",
		firstDay: 1
	};
	$.datepicker.setDefaults($.datepicker.regional["ru"]);
});

function init() {
    selectAddress();
    calculateSum();
    var dateInvoice = document.getElementsByName("dateInvoice")[0];
    dateInvoice.value = formatDate(dateInvoice.value);
}

function selectAddress() {
	var seller = document.querySelector(".seller > div > select");
	var index = seller.selectedIndex;
	var adress = seller.options[index].text;
	var addressSeller = document.querySelector(".seller > div > input");
	addressSeller.value = adress;
}

function calculateSum() {
	var totalSum = 0;
	var totalInput = document.getElementsByName("sumInvoice")[0];
	var amountArray = document.getElementsByName("amountLine");
	var priceArray = document.getElementsByName("priceLine");
	var sumArray = document.getElementsByName("sumLine");
	for (var i = 0; i < amountArray.length; i++) {
		var amount = amountArray[i].value;
		var price = priceArray[i].value;
		var sum = amount * price;
		if (sum != 0) {
			sumArray[i].value = formatDigit(String(sum));
			totalSum += sum;
		} else sumArray[i].value = "";
	}
	if (totalSum != 0) totalInput.value = formatDigit(String(totalSum));
	else totalInput.value = "";
}

function addNewLine() {
	var table = document.getElementsByTagName("table")[0];
	var tbody = document.createElement("tbody");
	table.appendChild(tbody);
	tbody.innerHTML =
		"<tr><td><input type='checkbox' ></td><td><input type='text' size='30' name='descriptionLine' required ></td><td><input type='text' size='10' name='amountLine' required onchange='calculateSum()' onkeypress='return isDigit(event)' ></td><td><input type='text' size='10' name='priceLine' required onchange='calculateSum()' onkeypress='return isDigit(event)' ></td><td><input type='text' size='10' name='sumLine' required onkeypress='return false' ></td></tr>";
}

function deleteLine() {
	var inputArray = document.querySelectorAll("td > input");
	for (var i = 0; i < inputArray.length; i++) {
		if (inputArray[i].checked) {
			var tr = inputArray[i].parentNode.parentNode;
			var parent = tr.parentNode;
			parent.removeChild(tr);
		}
	}
	calculateSum();
}

function isDigit(event) {
	e = event || window.event;
	var charCode = e.keyCode;
	var str = String.fromCharCode(charCode);
	var regexp = /(\d)/g;
	return regexp.test(str);
}

function formatDate(str) {
	var result;
	if (str) result = new Date(str).toLocaleDateString();
	else result = new Date().toLocaleDateString();
	return result;
}

function formatDigit(str) {
	var regexp = /(\d)(?=(\d{3})+(\D|$))/g;
	return str.replace(regexp, "$1 ");
}

function unformatDigit() {
    var totalSum = document.getElementsByName("sumInvoice")[0];
    totalSum.value = totalSum.value.replace(/ /g, "");
    var sumArray = document.getElementsByName("sumLine");
    for (var i = 0; i < sumArray.length; i++) {
        sumArray[i].value = sumArray[i].value.replace(/ /g, "");
    }
}
