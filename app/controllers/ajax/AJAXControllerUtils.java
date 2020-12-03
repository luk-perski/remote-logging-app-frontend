package controllers.ajax;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.language.LanguageUtils;

public class AJAXControllerUtils {

	@Inject
	private LanguageUtils language_utils;

	public Result renderAJAXResponse(Request request, JsonNode json_node) {
		return Controller.ok(Json.stringify(Json.toJson(new AJAXResponse(this.language_utils.getLanguage(request), json_node)))).as(Http.MimeTypes.JSON);
	}

	public Result rendeAJAXErrorResponse(Request request, int error_code, String error_message) {
		switch (error_code) {
		case Http.Status.FORBIDDEN:
			return Controller.forbidden(generateErrorResponse(request, error_code, error_message)).as(Http.MimeTypes.JSON);
		case Http.Status.BAD_REQUEST:
			return Controller.badRequest(generateErrorResponse(request, error_code, error_message)).as(Http.MimeTypes.JSON);
		default:
			return Controller.internalServerError(generateErrorResponse(request, error_code, error_message)).as(Http.MimeTypes.JSON);
		}
	}

	private String generateErrorResponse(Request request, int error_code, String error_message) {
		return Json.stringify(Json.toJson(new AJAXErrorResponse(error_code, this.language_utils.la(request, error_message, request.uri()), request.uri())));
	}
}

class AJAXResponse {

	private String language;

	private JsonNode data;

	public AJAXResponse(String language, JsonNode json_node) {
		this.language = language;
		this.data = json_node;
	}

	public String getLanguage() {
		return this.language;
	}

	public JsonNode getData() {
		return this.data;
	}
}

class AJAXErrorResponse {

	private int error_code;

	private String error_message;

	private String resource;

	public AJAXErrorResponse(int error_code, String error_message, String resource) {
		this.error_code = error_code;
		this.error_message = error_message;
		this.resource = resource;
	}

	@JsonProperty("error_code")
	public int getErrorCode() {
		return this.error_code;
	}

	@JsonProperty("error_message")
	public String getErrorMessage() {
		return this.error_message;
	}

	public String getResource() {
		return this.resource;
	}
}