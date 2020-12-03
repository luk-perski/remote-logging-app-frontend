package controllers.ajax;

import javax.inject.Inject;

import models.db.app.form.FormElement;
import play.filters.csrf.CSRF;
import play.filters.csrf.CSRF.Token;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.app.form.models.Form;
import utils.app.form.models.FormComponent;
import utils.app.form.models.FormDataComponent;

public class AJAXTestController extends Controller {

	@Inject
	private AJAXControllerUtils ajax_controller_utils;

	public Result getFormData(Request request, Long form_id) {

		FormDataComponent component_instance = FormDataComponent.getComponentInstance(FormElement.getByID(form_id));
		if (component_instance instanceof FormComponent) {
			FormComponent form_component = (FormComponent) component_instance;
			Token token = CSRF.getToken(request.asScala()).get();
			form_component.setCSRFTokenName(token.name());
			form_component.setCSRFTokenValue(token.value());
			return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(new Form(form_component)));
		}

		return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(component_instance));
	}
}
