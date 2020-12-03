
$(document).ready(function(){
	console.log(messages("general.title.index"));
	jsRoutes.controllers.TestController.ajaxEndpoint().ajax().done(indexDone).fail(indexFail);
});

function indexDone(data){
	if(data){
		if(data.current_date_time){
			console.log(data.current_date_time);
		}
	}
}

function indexFail(data){
	console.error(data);
}