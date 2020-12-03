$(document).ready(function(){
	loadValidationHandlers();
});

function loadValidationHandlers(){
	$('.validate-form').submit(validateForm);
	$('.validate-blur').blur(function(){
		validateFormField($(this));
	});
}

function validateForm(e){
	return validateFormElement($(this));
}

function validateFormElement(form_element){
	if(form_element){
		var val_fields = form_element.find('input[class*="validate-"],select[class*="validate-"],textarea[class*="validate-"]');
		if(val_fields){
			var passes_validation = true;
			for (var i = 0; i != val_fields.length; i++) {
				passes_validation = validateFormField($(val_fields.get(i))) && passes_validation;
			}
		}
		return passes_validation;
	}
	return false;
}

function resetAllFields(){
	var val_fields = $('input[class*="validate-"],select[class*="validate-"],textarea[class*="validate-"]');
	if(val_fields){
		for (var i = 0; i != val_fields.length; i++) {
			resetField($(val_fields.get(i)));
		}
	}
}

function validateFormField(field){
	if(field){
		resetField(field);

		var field_passes_validation = true;
		if(field.hasClass('validate-not-empty')){
			if(!validateNotEmpty(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.cannot_be_empty"));
			}
		}

		if(field.hasClass('validate-number')){
			if(!validateIsNumber(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.must_be_number"));
			}
		}

		if(field.hasClass('validate-positive-number')){
			if(!validateIsPositiveNumber(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.must_be_positive_number"));
			}
		}

		if(field.hasClass('validate-integer')){
			if(!validateIsInteger(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.must_be_integer"));
			}
		}

		if(field.hasClass('validate-positive-integer')){
			if(!validateIsPositiveInteger(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.must_be_positive_integer"));
			}
		}

		if(field.hasClass('validate-regex')){
			if(!validateRegex(field)){
				field_passes_validation = false;
				markAsInvalid(field, messages("validation.text.fails_validation"));
			}
		}

		if(!field_passes_validation){
			return false;
		}

		markAsValid(field, messages("validation.text.valid_content"));
	}
	return true;
}

function resetField(field){
	field.next('.invalid-feedback').remove();
	field.next('.valid-feedback').remove();
	field.parent().find('.invalid-feedback').remove();
	field.parent().find('.valid-feedback').remove();
	field.removeClass('is-valid');
	field.removeClass('is-invalid');
}

function validateNotEmpty(field){
	if(field){
		var value = field.val();
		if(value){
			return value.trim() !== "";
		}
	}
	return false;
}

function validateIsNumber(field){
	if(field){
		var value = field.val();
		if(value){
			return !isNaN(value);
		}
	}
	return false;
}

function validateIsPositiveNumber(field){
	return (!field || !field.val()) || (validateIsNumber(field) && Number(field.val()) > 0);
}

function validateIsInteger(field){
	return (!field || !field.val()) || (validateIsNumber(field) && isInt(field.val()));
}

function validateIsPositiveInteger(field){
	return (!field || !field.val()) || (validateIsInteger(field) && Number(field.val()) > 0);
}

function validateRegex(field){
	if(field){
		var value = field.val();
		if(value){
			var regex_string = field.attr('pattern');
			if(regex_string){
				var regex = RegExp(regex_string);
				return regex.test(value);
			}
		}
	}
	return false;
}

function markAsInvalid(field, default_message){
	if(!field.hasClass('is-invalid')){
		field.addClass('is-invalid');
		if(!field.hasClass('validate-no-feedback') && !field.hasClass('validate-no-invalid-feedback')){
			var invalid_feedback_message = field.attr('data-invalid-feedback');
			if(invalid_feedback_message){
				addInvalidFeedback(field, invalid_feedback_message);
			}else{
				addInvalidFeedback(field, default_message);
			}
		}
	}
}

function markAsValid(field, default_message){
	if(!field.hasClass('is-valid')){
		field.addClass('is-valid');
		if(!field.hasClass('validate-no-feedback') && !field.hasClass('validate-no-valid-feedback')){
			var valid_feedback_message = field.attr('data-valid-feedback');
			if(valid_feedback_message){
				addValidFeedback(field, valid_feedback_message);
			}else{
				addValidFeedback(field, default_message);
			}
		}
	}
}

function addInvalidFeedback(field, message){
	addFeedback(field, message, 'invalid-feedback');
}

function addValidFeedback(field, message){
	addFeedback(field, message, 'valid-feedback');
}

function addFeedback(field, message, feedback_class){
	var el = $(document.createElement('div'));
	el.addClass(feedback_class);
	el.html(message);
	field.after(el);
}

function isInt(n) {
   return !isNaN(n) && parseFloat(n) == parseInt(n, 10);
}
