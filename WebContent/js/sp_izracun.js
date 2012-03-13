// Shared Qtip properties
var shared = {
   position: {
      my: 'top center', 
      at: 'bottom center'
   },
   style: {
      tip: true,
      classes: 'ui-tooltip-rounded ui-tooltip-shadow'
   }
};
// Store form parameters
var inputObj = {};


//Qtips tooltips
qtips = function() {
	$('#type_select td:nth-child(1)').qtip($.extend({}, shared, { 
		content: {
			text: 'Izračun po urah na dan (npr. 8 ur/dan)'
		}
	}));
	$('#type_select td:nth-child(2)').qtip($.extend({}, shared, { 
		content: {
			text: 'Izračun po urah na mesec (npr. 168 ur/mesec)'
		}
	}));
	$('#type_select td:nth-child(3)').qtip($.extend({}, shared, { 
		content: {
			text: 'Izračun glede na pavšalni mesečni znesek (npr. 3000 EUR/mesec). ' + 
					'Predvideva se, da so prazniki, dopust ter bolniška že všteti.'
		}
	}));
};

// Number formatter
numberFormat = function() {
	$("input[type='text']").blur(function(){
		$(this).parseNumber({format:"#,##0.00", locale:"de"});
		$(this).formatNumber({format:"#,##0.00", locale:"de"});
	});
	$("[id$='urDnevno']").blur(function(){
		var number = $(this).parseNumber({format:"#,##0.00", locale:"de"});
		$(this).formatNumber({format:"#,##0.00", locale:"de"});
		
		$("[id$='urMesecno']").val(number * 21);
		$("[id$='urMesecno']").formatNumber({format:"#,##0.00", locale:"de"});
	});
	$("[id$='urMesecno']").blur(function(){
		var number = $(this).parseNumber({format:"#,##0.00", locale:"de"});
		$(this).formatNumber({format:"#,##0.00", locale:"de"});
		
		$("[id$='urDnevno']").val(number / 21);
		$("[id$='urDnevno']").formatNumber({format:"#,##0.00", locale:"de"});
	});
};

// Section toggle
sectionToggle = function() {
	// Selectors
	var selected_radio = $('input:radio[name$=tipVnosa]:checked').val();
	
	var div_ure = $('div[id$=basic_info], div[id$=workdays]');
	var div_pavsal = $('div[id$=pavsal_info]');
	var input_ure = $('div[id$=basic_info] input, div[id$=workdays] input');
	var input_pavsal = $('div[id$=pavsal_info] input');
	
	var input_ure_mesecno = $('input[id$=urMesecno]');
	var input_ure_dnevno = $('input[id$=urDnevno]');
	
	// If pavsal_mesecno
	if(selected_radio == 'pavsal_mesecno') {
		input_ure.attr('disabled', 'disabled'); 
		div_ure.hide('slow');

		input_pavsal.removeAttr('disabled');
		div_pavsal.show('slow');	
	}
	
	// If ure_dnevno or 
	else {
		input_pavsal.attr('disabled', 'disabled');
		div_pavsal.hide('slow');

		input_ure.removeAttr('disabled');
		div_ure.show('slow');

		if(selected_radio == 'ure_dnevno')
			input_ure_mesecno.attr('disabled', 'disabled');
		else if (selected_radio == 'ure_mesecno')
			input_ure_dnevno.attr('disabled', 'disabled');
	}
};

// On submit
handleOnSubmit = function() {
	var selected_radio = $('input:radio[name$=tipVnosa]:checked').val();
	var input_ure = $('input[id$=urMesecno], input[id$=urDnevno]');
	
	if(selected_radio != 'pavsal_mesecno')
		input_ure.removeAttr('disabled');
};

// On load
$(document).ready(function() {
	$('input:radio[name$=tipVnosa]').change(sectionToggle);
	$('#izracunForm').submit(handleOnSubmit);
	sectionToggle();
});
$(document).ready(numberFormat);
$(document).ready(qtips);