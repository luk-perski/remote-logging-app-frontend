var system_job_header_component = {
	props: {
		job: Object,
		lang: String
	},
	template: `
		<div :class="'card-header ' + ((job.is_active) ? ((!job.is_running) ? 'success-theme-lighter' : 'warning-theme-lighter') : 'danger-theme-lighter')">
			<div class="float-right">
				<small-label text="` + messages('system.label.last_run') + `">
					<span :title="job.last_run_end">
						{{ job.last_run_end_relative }}
					</span>
				</small-label>

				<small-label class="ml-3" text="` + messages('system.label.next_run') + `">
					<span :title="job.next_run">
						{{ job.next_run_relative }}
					</span>
				</small-label>
			</div>

			<span v-if="job.is_running" class="mr-3" title="` + messages('system.label.job_running') + `">
				<i class="fa fa-cog fa-spin fa-fw primary-fg-color"></i>
				<span class="sr-only">` + messages('system.label.job_running') + `...</span>
			</span>

			<span v-if="job.has_errors" class="mr-3 danger-fg-color" title="` + messages('system.label.job_with_errors') + `">
				<i class="fa fa-warning"></i>
				<span class="sr-only">` + messages('system.label.job_with_errors') + `...</span>
			</span>

			<span><b>{{ job["name_" + this.lang] }}</b></span>
		</div>
	`
};

var system_job_body_component = {
	props: {
		job: Object,
		lang: String
	},
	data: function(){
		return {
			editable: false
		};
	},
	updated: loadNecessaryHandlers,
	methods: {
		getJobLog(){
			var self = this;
			jsRoutes.controllers.ajax.AJAXSystemController.renderSystemJobLog(self.job.id).ajax().done(function(data){
				if(data && data.data && data.data.job_log){
					showDialog('primary', self.job["name_" + self.lang], '<pre class="log-container">' + data.data.job_log + '</pre>');
				}else{
					showDialog('warning', self.job["name_" + self.lang], messages("system.text.no_log_to_view"));
				}
			}).fail(function(){
				showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
			});
		},
		activateJob(){
			var self = this;
			var r = jsRoutes.controllers.ajax.AJAXSystemController.activateSystemJob(self.job.id);
			$.ajax({
				beforeSend: function(request) {
	    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
	  			},
				url: r.url,
				type: r.type,
				success: function(data){
					if(data && data.data){
						self.$emit('updated-event');
					}else{
						showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
					}
				},
				error: function(){
					showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
				}
			});
		},
		deactivateJob(){
			var self = this;
			var r = jsRoutes.controllers.ajax.AJAXSystemController.deactivateSystemJob(self.job.id);
			$.ajax({
				beforeSend: function(request) {
	    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
	  			},
				url: r.url,
				type: r.type,
				success: function(data){
					if(data && data.data){
						self.$emit('updated-event');
					}else{
						showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
					}
				},
				error: function(){
					showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
				}
			});
		},
		setEditable(value){
			var self = this;
			self.editable = value;
			if(value){
				self.$nextTick(() => {
					$('#next_run_' + self.job.id).val(self.job.next_run);
					$('#periodicity_' + self.job.id).val(self.job.periodicity);
				});
			}
		},
		saveJob(){
			var self = this;

			var r = jsRoutes.controllers.ajax.AJAXSystemController.editSystemJob(self.job.id);
			$.ajax({
				beforeSend: function(request) {
	    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
	  			},
				url: r.url,
				type: r.type,
				data: {
					periodicity: $('#periodicity_' + self.job.id).val(),
					next_run: $('#next_run_' + self.job.id).val()
				},
				success: function(data){
					if(data && data.data){
						self.$emit('updated-event');
					}else{
						showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
					}
				},
				error: function(){
					showDialog('danger', self.job["name_" + self.lang], messages("general.text.operation_error"));
				}
			});

			self.setEditable(false);
		}
	},
	template: `
		<div class="card-body">
			<div class="row">
				<div class="col">
					<value-group label="` + messages("system.label.last_run_start") + `">
						{{ job.last_run_start }}
					</value-group>
				</div>
				<div class="col">
					<value-group label="` + messages("system.label.last_run_end") + `">
						{{ job.last_run_end }}
					</value-group>
				</div>
				<div class="col">
					<value-group label="` + messages("system.label.execution_time") + `">
						{{ job.last_run_execution_time }}
					</value-group>
				</div>
			</div>
			<div class="row">
				<div class="col">
					<value-group v-if="!editable" label="` + messages("system.label.periodicity") + `">
						{{ job.periodicity }}m
					</value-group>
			  		<div v-if="editable" class="form-group">
			  			<label for="periodicity">` + messages("system.label.periodicity") + `</label>
			  			<input type="text" class="form-control validate-not-empty validate-positive-integer validate-blur" name="periodicity" :id="'periodicity_' + job.id" maxlength="5" />
			  		</div>
				</div>
				<div class="col">
					<value-group v-if="!editable" label="` + messages("system.label.next_run") + `">
						{{ job.next_run }}
					</value-group>
			  		<div v-if="editable" class="form-group">
			  			<label :for="'next_run_' + job.id">` + messages("system.label.next_run") + `</label>
			  			<input type="text" class="form-control date-time-picker validate-not-empty validate-blur" name="next_run" :id="'next_run_' + job.id" />
                	</div>
				</div>
			</div>

			<div class="row mt-4">
				<div class="col">
					<div v-if="!job.is_running">
						<button v-if="!editable" class="btn btn-outline-primary" :disabled="job.is_active" @click="setEditable(true)"><i class="fa fa-edit"></i> ` + messages("general.label.edit") + `</button>
						<button v-if="editable" class="btn btn-outline-success" :disabled="job.is_active" @click="saveJob"><i class="fa fa-check-circle"></i> ` + messages("general.label.save") + `</button>
						<button v-if="editable" class="btn btn-outline-danger" :disabled="job.is_active" @click="setEditable(false)"><i class="fa fa-check-circle"></i> ` + messages("general.label.cancel") + `</button>
						<a v-if="!editable" href="#job_log" @click="getJobLog" class="btn btn-outline-secondary"><i class="fa fa-search-plus"></i> ` + messages("system.label.view_log") + `</a>
						<a v-if="job.is_active && !editable" href="#deactivate" @click="deactivateJob" class="btn btn-outline-danger"><i class="fa fa-stop-circle"></i> ` + messages("system.label.deactivate") + `</a>
						<a v-if="!job.is_active && !editable" href="#activate" @click="activateJob" class="btn btn-outline-success"><i class="fa fa-play-circle"></i> ` + messages("system.label.activate") + `</a>
					</div>
				</div>
			</div>
		</div>
	`
};

var system_job_component = {
  	props: {
  		job: Object,
  		lang: String
  	},
  	components: {
  		'system-job-header': system_job_header_component,
  		'system-job-body': system_job_body_component
  	},
  	methods: {
		updatedEventReceived: function () {
			// Sending this event up the chain
			this.$emit('updated-event');
		}
	},
  	template: `
  		<div class="card">
			<a class="dark-fg-color no-decoration" :href="'#job' + job.id + '_body'" data-toggle="collapse" role="button" aria-expanded="false" :aria-controls="'job' + job.id + '_body'">
				<system-job-header v-bind:job="job" v-bind:lang="lang"></system-job-header>
			</a>

			<div class="collapse" :id="'job' + job.id + '_body'">
				<system-job-body v-bind:job="job" v-bind:lang="lang" @updated-event="updatedEventReceived"></system-job-body>
			</div>
  		</div>
  	`
};

function loadNecessaryHandlers(){
	var self = this;
	self.$nextTick(() => {
		loadValidationHandlers();

		$('.date-time-picker').datetimepicker({
			locale: self.language,
			format: 'YYYY-MM-DD HH:mm:ss'
		});
    });
}

function showDialog(_theme, _title, _message){
	bootbox.setLocale(this.lang).alert({
		message: _message,
		title: _title,
		className: _theme
	});
}
