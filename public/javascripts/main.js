
$(document).ready(function(){
  	$('[data-toggle="popover"]').popover();
  	$('[data-toggle="tooltip"]').tooltip();
});

function setPageAndSubmit(page_number){
	var form = $('.form-paginate-results');
	if(form){
		var page = $('input[name="page"]');
		if(page){
			page.val(page_number);
			form.submit();
		}
	}
}