package utils.app.page;

import play.data.DynamicForm;
import play.mvc.Http.Request;
import utils.language.LanguageUtils;

public class FormSubmissionResult {

	public static final String FLASH_RESULT = "form_result";
	public static final String FLASH_IS_ERROR = "is_error";

	private static final String DEFAULT_SUCCESS_MESSAGE = "general.text.operation_success";
	private static final String DEFAULT_ERROR_MESSAGE = "general.text.operation_error";

	private boolean success;

	private String success_message;

	private String error_message;

	private DynamicForm submitted_form;

	public FormSubmissionResult(Request r, LanguageUtils l, boolean success, DynamicForm submitted_form) {
		this.success = success;
		if (this.success) {
			this.success_message = l.l(r, DEFAULT_SUCCESS_MESSAGE);
		} else {
			this.error_message = l.l(r, DEFAULT_ERROR_MESSAGE);
		}
		this.submitted_form = submitted_form;
	}

	public FormSubmissionResult(Request r, LanguageUtils l, boolean success, String message, boolean attach_to_default_message, DynamicForm submitted_form) {
		this.success = success;
		if (this.success) {
			this.success_message = ((attach_to_default_message) ? l.l(r, DEFAULT_SUCCESS_MESSAGE) + ": " : "") + l.l(r, message);
		} else {
			this.error_message = ((attach_to_default_message) ? l.l(r, DEFAULT_ERROR_MESSAGE) + ": " : "") + l.l(r, message);
		}
		this.submitted_form = submitted_form;
	}

	public boolean success() {
		return this.success;
	}

	public String getSuccessMessage() {
		return this.success_message;
	}

	public String getErrorMessage() {
		return this.error_message;
	}

	public String getFormValue(String key) {
		if (this.submitted_form != null) {
			return this.submitted_form.get(key);
		}
		return null;
	}
}
