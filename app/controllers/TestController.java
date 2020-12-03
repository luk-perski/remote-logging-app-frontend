package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.IsAuthenticated;
import actions.IsAuthenticatedAs;
import models.db.user.Role;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.Utils;
import utils.app.page.BreadcrumbElement;
import utils.app.page.Breadcrumbs;
import utils.app.page.PageSettings;
import utils.excel.ExcelHelper;
import utils.excel.providers.BasicExcelProvider;
import utils.location.GeoIPHelper;
import utils.location.GeoIPRecord;
import utils.location.exception.GeoIPHelperException;
import utils.mail.MailHelper;
import utils.mail.exception.MailHelperException;
import utils.pdf.PDFHelper;
import utils.pdf.exception.PDFContentProviderException;
import utils.pdf.providers.BasicPDFProvider;
import utils.word.WordHelper;
import utils.word.providers.BasicWordProvider;
import views.html.base.basic_error_page;
import views.html.base.exception_error_page;
import views.html.base.tests;
import views.html.tests.form_generator;

public class TestController extends Controller {

	@Inject
	private Utils utils;

	@Inject
	private MailHelper mail_helper;

	@Inject
	private PDFHelper pdf_helper;

	@Inject
	private ExcelHelper excel_helper;

	@Inject
	private WordHelper word_helper;

	@Inject
	private GeoIPHelper geoip_helper;

	public Result test(Request r) {
		return ok("test");
	}

	public Result renderTests(Request r) {
		Breadcrumbs crumbs = this.utils.breacrumb_helper.getBreadcrumbsWithElements(r, this.utils.l, new BreadcrumbElement("Tests", r.uri(), true));
		return ok(tests.render(r, this.utils, new PageSettings(r, this.utils.l, null, crumbs)));
	}

	@IsAuthenticated
	public Result authenticatedPage() {
		return ok("This page can only be visited by an authenticated user");
	}

	@IsAuthenticatedAs({ Role.ADMIN })
	public Result authenticatedPageForAdmin() {
		return ok("This page can only be visited by an authenticated Admin user");
	}

	public Result hashManager() {
		return ok(BCrypt.hashpw("test", BCrypt.gensalt()));
	}

	public Result bufferMail() {
		try {
			this.mail_helper.bufferMail("Test Subject", "recipient@example.com", "source@example.com", "Test Message: " + new Date(), null, MailHelper.URGENCY_HIGH);
		} catch (MailHelperException e) {
			return internalServerError("Internal Server Error: " + e.getMessage());
		}
		return ok("Mail buffered");
	}

	public Result ajaxEndpoint() {
		String current_date_time = this.utils.date_utils.getCurrentDateAndTimeAsString(null);

		ObjectNode node = Json.newObject();
		node.put("current_date_time", current_date_time);

		return ok(Json.stringify(node)).as(Http.MimeTypes.JSON);
	}

	public Result generatePDFDocument(Request r) {
		try {
			String name = "test_pdf_file1.pdf";
			String filename = this.pdf_helper.generatePDFFile(new BasicPDFProvider(), "temp" + File.separator + "pdf", name);
			if (filename != null) {
				File temp_file = new File(filename);
				FileInputStream input_stream = new FileInputStream(temp_file);
				temp_file.delete();

				return this.utils.other_utils.renderResourceByContentType("application/pdf", input_stream, name, controllers.routes.TestController.generatePDFDocument().absoluteURL(r), false, false);
			}

			return internalServerError(basic_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.la(r, "error.text.unexpected_error", "null filename!"), true));
		} catch (PDFContentProviderException e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		} catch (FileNotFoundException e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		}
	}

	public Result generateExcelDocument(Request r) {
		try {
			String name = "test_excel_file1.xlsx";
			String filename = this.excel_helper.generateExcelFile(new BasicExcelProvider(), "temp" + File.separator + "excel", name);
			if (filename != null) {
				File temp_file = new File(filename);
				FileInputStream input_stream = new FileInputStream(temp_file);
				temp_file.delete();

				return this.utils.other_utils.renderResourceByContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", input_stream, name, controllers.routes.TestController.generateExcelDocument().absoluteURL(r), true, true);
			}

			return internalServerError(basic_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.la(r, "error.text.unexpected_error", "null filename!"), true));
		} catch (Exception e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		}
	}

	public Result generateWordDocument(Request r) {
		try {
			String name = "test_word_file1.docx";
			String filename = this.word_helper.generateWordFile(new BasicWordProvider(), "temp" + File.separator + "word", name);
			if (filename != null) {
				File temp_file = new File(filename);
				FileInputStream input_stream = new FileInputStream(temp_file);
				temp_file.delete();

				return this.utils.other_utils.renderResourceByContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document", input_stream, name, controllers.routes.TestController.generateWordDocument().absoluteURL(r), true, true);
			}

			return internalServerError(basic_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.la(r, "error.text.unexpected_error", "null filename!"), true));
		} catch (Exception e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		}
	}

	public Result getGeoIPData(Request r) {
		try {
			String out = "";

			InetAddress ip_address = InetAddress.getByName(new URL("https://www.iscte-iul.pt").getHost());

			GeoIPRecord record = this.geoip_helper.generateRecordFromIPAddress(ip_address.getHostAddress());
			if (record != null) {
				out += "ISO Code: " + record.getCountryISOCode() + "\n";
				out += "Country: " + record.getCountryNameEN() + "\n";
				out += "City: " + record.getCityNameEN() + "\n";
				out += "Postal Code: " + record.getPostalCode() + "\n";
				out += "Latitude: " + record.getLatitude() + "\n";
				out += "Longitude: " + record.getLongitude() + "\n";
			}

			return ok(out);

		} catch (GeoIPHelperException e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		} catch (UnknownHostException e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		} catch (MalformedURLException e) {
			return internalServerError(exception_error_page.render(r, this.utils, new PageSettings(r, this.utils.l, null, null), this.utils.l.l(r, "error.label.unexpected_error"), this.utils.l.l(r, "error.text.unexpected_error"), e, true));
		}
	}

	public Result formGeneratorExample(Request r) {
		return ok(form_generator.render(r, this.utils));
	}
}
