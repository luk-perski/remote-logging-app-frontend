package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.ajax.AJAXControllerUtils;
import models.db.math.Sum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import pt.iscte_iul.gdsi.utils.TypeUtils;
import repository.math.MathRepository;
import utils.Utils;
import utils.app.page.PageSettings;
import views.html.math.math;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class MathController extends Controller {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Inject
    private Utils utils;

    @Inject
    private TypeUtils type_utils;

    @Inject
    private FormFactory ff;

    @Inject
    private AJAXControllerUtils ajax_controller_utils;

    @Inject
    private MathRepository mathRepository;

    public Result testMath() {
        return ok("Hello World");
    }

    //todo read about Breadcrumbs
    @AddCSRFToken
    public Result renderCalculator(Request request, String redirect) {
        return ok(math.render(request, this.utils, new PageSettings(request, this.utils.l, null, null), redirect, null));
    }

    public Result ajaxSubmitCompareNumber(Request request, String numberOne, String numberTwo) {
        Sum sum = getSumFromForm(numberOne, numberTwo);
        saveSumToDb(sum);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree("{\"status\":\"ok\", \"result\":\"" + sum.getResult() + "\"}");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return this.ajax_controller_utils.renderAJAXResponse(request, actualObj);
    }

    public Result ajaxGetSumData(Request request) {
        List<Sum> sumList = getAllSums();
        return this.ajax_controller_utils.renderAJAXResponse(request, Json.toJson(sumList));
    }

    private Sum getSumFromForm(String _numberOne, String _numberTwo) {
        Integer numberOne = Integer.parseInt(_numberOne);
        Integer numberTwo = Integer.parseInt(_numberTwo);
        Integer result = numberOne + numberTwo;

        log.info(String.format("Result: %d", result));

        return new Sum(numberOne, numberTwo, result, new Date());
    }

    private void saveSumToDb(Sum sum) {
        sum.save();
        sum.refresh();
    }

    private List<Sum> getAllSums(){
        return mathRepository.getAllSums();
    }
}
