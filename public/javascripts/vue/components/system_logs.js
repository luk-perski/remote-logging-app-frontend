var system_logs_box_component = {
  	props: {
        user: Object,
  		system_logs: Array,
        has_more_data: Boolean,
  		lang: String
  	},
  	methods: {
        loadMoreData: function(){
            console.log("Loading more data");
            this.$emit('load-more-data');
        }
	},
  	template: `
  		<div id="system_log_box" class="mt-3 mb-3">
            <div v-if="system_logs.length" class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead class="thead-dark">
                        <tr>
                            <th>` + messages("system.label.instant") + `</th>
                            <th>` + messages("system.label.type") + `</th>
                            <th>` + messages("system.label.context") + `</th>
                            <th>` + messages("system.label.description") + `</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(system_log, index) in system_logs" :key="index">
                            <td><b><span :title="system_log.log_instant">{{ system_log.log_instant_relative }}</span></b></td>
                            <td class="small">{{ system_log.log_type }}</td>
                            <td class="small">{{ system_log.log_context }}</a></td>
                            <td class="small"><span v-html="system_log.log_description"></span></td>
                        </tr>
                    </tbody>
                </table>
                <div v-if="has_more_data" class="text-center mt-3 mb-3">
                    <button class="btn btn-outline-primary" @click="loadMoreData"><i class="fa fa-ellipsis-h"></i> ` + messages("system.label.load_more") + `</button>
                </div>
            </div>
            <div v-else class="mt-3 mb-3">
                <p class="alert alert-warning">` + messages("system.text.no_log_to_view") + `</p>
            </div>
  		</div>
  	`
};

function showDialog(_theme, _title, _message){
    bootbox.setLocale(this.lang).alert({
        message: _message,
        title: _title,
        className: _theme
    });
}
