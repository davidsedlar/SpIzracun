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
};

// Save and restore form data
saveData = function() {
	inputObj = $('[id$=basic_info], [id$=workdays]').values();
};
restoreData = function() {
	$('[id$=basic_info], [id$=workdays]').values(inputObj);
};

/*
 * jQuery.values: get or set all of the name/value pairs from child
 * input controls @argument data {array} If included, will populate
 * all child controls. @returns element if data was provided, or
 * array of values if not
 */
$.fn.values = function(data) {
    var els = $(this).find(':input').get();

    if(typeof data != 'object') {
        // return all data
        data = {};

        $.each(els, function() {
            if (this.name && !this.disabled &&  (this.checked || /select|textarea/i.test(this.nodeName) || /text|hidden|password/i.test(this.type))) {
            	data[this.name] = $(this).val();
            }
        });
        return data;
    } else {
        $.each(els, function() {
            if (this.name && data[this.name]) {
                if(this.type == 'checkbox') {
                        this.checked = (data[this.name] == $(this).val());
                } else {
                        $(this).val(data[this.name]);
                }
            }
        });
        return $(this);
    }
};

// Ajax loader graphics
ajaxLoader = function(data) {
    var ajaxstatus = data.status;

    switch (ajaxstatus) {
        case "begin": // This is called right before ajax request
						// is been sent.
            $('#ajaxloader').show();
        	saveData();
            break;

        case "complete": // This is called right after ajax
							// response is received.
        	$('#ajaxloader').hide();
            break;
            
        case "success": // This is called if request successful.
        	restoreData();
        	numberFormat();
            break;
    }
};

// On load
$(document).ready(numberFormat);
$(document).ready(qtips);