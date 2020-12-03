package models.helpers.base;

import javax.inject.Inject;

import models.helpers.exception.FailsValidationException;
import play.mvc.Http.Request;
import pt.iscte_iul.gdsi.utils.ValidationUtils;
import utils.language.LanguageUtils;

public class ValidationHelper {

	@Inject
	private ValidationUtils validation_utils;

	@Inject
	private LanguageUtils l;

	public void validateNotEmpty(String value, String label, Request r) throws FailsValidationException {
		if (value == null || value.trim().isEmpty()) {
			throw new FailsValidationException(l.l(r, "validation.text.cannot_be_empty") + ": " + l.l(r, label));
		}
	}

	public void validateMaxLength(int max_length, String value, String label, Request r) throws FailsValidationException {
		if (value.length() > max_length) {
			throw new FailsValidationException(l.l(r, "validation.text.is_larger_than_max_length") + ": " + l.l(r, label));
		}
	}

	public void validatePositiveInteger(String value, String label, Request r) throws FailsValidationException {
		try {
			if (value != null && !value.trim().isEmpty()) {
				int int_value = Integer.parseInt(value.trim());
				if (int_value > 0) {
					return;
				}
			}
		} catch (NumberFormatException e) {
		}
		throw new FailsValidationException(l.l(r, "validation.text.must_be_positive_integer") + ": " + l.l(r, label));
	}

	public void validateEmail(String email, String label, Request r) throws FailsValidationException {
		if (email == null || email.trim().isEmpty() || !this.validation_utils.isValidEmail(email)) {
			throw new FailsValidationException(l.l(r, "validation.text.fails_validation") + ": " + l.l(r, label) + " [" + email + "]");
		}
	}
}
