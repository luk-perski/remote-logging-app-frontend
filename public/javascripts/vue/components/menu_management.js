
Vue.component('menu-manager', {
	props: {
		root_menus: Array,
		lang: String
	},
	data: function() {
		return {
			current_menu: {},
			add_new_menu: false
		};
	},
  	updated: loadValidationHandlers,
  	methods: {
		updatedEventReceived: function () {
			// Sending this event up the chain
			this.$emit('updated-event');
		},
		setMenuToConfig: function(menu_to_set) {
			if(menu_to_set){
				this.current_menu = menu_to_set;
			}else{
				this.add_new_menu = false;
				this.current_menu = {};
			}
			resetAllFields();
		},
		addNewMenu: function(parent_id){
			console.log(parent_id);
			this.add_new_menu = true;
			this.current_menu = {
				parent_id: parent_id
			};
			resetAllFields();
		}
	},
	template: `
		<div class="row">
			<div class="col-md-4 small">
 				<menu-component v-for="menu in root_menus" :menu="menu" :selected_menu="current_menu" :lang="lang" :key="menu.id" @updated-event="updatedEventReceived" @menu-to-config="setMenuToConfig" @add-new-menu="addNewMenu"></menu-component>
				<add-menu-component @add-new-menu="addNewMenu"></add-menu-component>
			</div>

			<div class="col-md-8">
				<menu-component-config :menu="current_menu" :add_new_menu="add_new_menu" :lang="lang" @updated-event="updatedEventReceived" @menu-to-config="setMenuToConfig"></menu-component-config>
			</div>
		</div>
	`
});

Vue.component('menu-component', {
	props: {
		menu: Object,
		selected_menu: Object,
		lang: String
	},
  	methods: {
		updatedEventReceived: function () {
			// Sending this event up the chain
			this.$emit('updated-event');
		},
		sendToConfig: function() {
			this.$emit('menu-to-config', this.menu);
		},
		setMenuToConfig: function(menu_to_set){
			// Sending this event up the chain
			this.$emit('menu-to-config', menu_to_set);
		},
		addNewMenu: function(parent_id){
			// Sending this event up the chain
			if(parent_id){
				this.$emit('add-new-menu', parent_id);
			}else{
				this.$emit('add-new-menu', this.menu.id);
			}
		},
		confirmDelete: function(){
			var self = this;
			showConfirmDialog('danger', self.menu["label_" + self.lang], messages("system.text.confirm_delete_menu"), function(result){
				if(result){
					// Delete this menu
					var r = jsRoutes.controllers.ajax.AJAXSystemController.deleteMenuData(self.menu.id);
					$.ajax({
						beforeSend: function(request) {
			    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
			  			},
						url: r.url,
						type: r.type,
						success: function(data){
							if(data && data.data){
								self.$emit('updated-event');
								showDialog('success', self.menu["label_" + self.lang], messages("general.text.operation_success"));
							}else{
								showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
							}
						},
						error: function(){
							showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
						}
					});
				}
			});
		}
	},
	computed: {
		is_currently_selected_menu: function(){
			if(this.selected_menu){
				return this.menu.id == this.selected_menu.id;
			}
		}
	},
	template: `
		<div class="card mb-2">
			<div :class="'card-header' + (is_currently_selected_menu ? ' primary-theme-lighter': '')">
				<div class="float-right">
					<a href="#edit" class="small" @click="sendToConfig"><i class="fa fa-edit"></i></a>
					<a href="#delete" class="small danger-fg-color" @click="confirmDelete"><i class="fa fa-trash"></i></a>
				</div>

				<a class="no-decoration" :href="'#menu' + menu.id" data-toggle="collapse" role="button" aria-expanded="false" :aria-controls="'menu' + menu.id">
					<i :class="menu.icon_css_class"></i> <b>{{ menu["label_" + lang] }}</b>
				</a>
			</div>

			<div class="collapse" :id="'menu' + menu.id">
				<div class="card-body">
					<menu-component v-for="child in menu.children" :menu="child" :selected_menu="selected_menu" :lang="lang" :key="child.id" @updated-event="updatedEventReceived" @menu-to-config="setMenuToConfig" @add-new-menu="addNewMenu"></menu-component>
					<add-menu-component @add-new-menu="addNewMenu"></add-menu-component>
				</div>
			</div>
		</div>
	`
});

Vue.component('add-menu-component', {
	methods: {
		addNewMenu: function(){
			this.$emit('add-new-menu');
		}
	},
	template: `
		<div class="mt-2 mb-2">
			<a href="#add_menu" class="no-decoration btn btn-outline-primary w-100 small text-left py-2 px-3" @click="addNewMenu">
				<i class="fa fa-plus-square"></i> ` + messages("system.label.add_menu") + `
			</a>
		</div>
	`
});

Vue.component('menu-component-config', {
	props: {
		menu: Object,
		add_new_menu: Boolean,
		lang: String
	},
  	methods: {
		updatedEventReceived: function () {
			// Sending this event up the chain
			this.$emit('updated-event');
		},
		resetConfig: function() {
			this.$emit('menu-to-config', undefined);
		},
		saveConfig: function() {
			var self = this;
			var form = $('#menu_config_form');
			if(form){
				if(validateFormElement(form)){
					if(self.menu.id){
						// This is an existing menu, will edit it (by sending data to server)
						var r = jsRoutes.controllers.ajax.AJAXSystemController.saveMenuData(self.menu.id);
						$.ajax({
							beforeSend: function(request) {
				    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
				  			},
							url: r.url,
							type: r.type,
							data: form.serialize(),
							success: function(data){
								if(data && data.data){
									self.$emit('updated-event');
									showDialog('success', self.menu["label_" + self.lang], messages("general.text.save_success"));
								}else{
									showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
								}
							},
							error: function(){
								showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
							}
						});
					}else{
						// This is a new menu, will create it (by sending data to server)
						var r = jsRoutes.controllers.ajax.AJAXSystemController.addMenuData();
						$.ajax({
							beforeSend: function(request) {
				    			request.setRequestHeader("Csrf-Token", $('input[name="csrfToken"]').attr('value'));
				  			},
							url: r.url,
							type: r.type,
							data: form.serialize(),
							success: function(data){
								if(data && data.data){
									self.$emit('updated-event');
									showDialog('success', self.menu["label_" + self.lang], messages("general.text.save_success"));
								}else{
									showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
								}
							},
							error: function(){
								showDialog('danger', self.menu["label_" + self.lang], messages("general.text.operation_error"));
							}
						});
					}
				}
			}
		}
	},
	computed: {
		show_form: function(){
			return this.menu.id || this.add_new_menu;
		}
	},
	template: `
		<div>
			<div v-if="show_form">
				<h4 v-if="menu.id">{{ menu["label_" + lang] }}</h4>
				<h4 v-else>` + messages("system.label.add_menu") + `</h4>

				<form id="menu_config_form" class="validate-form">
					<input type="hidden" name="parent_id" v-model="menu.parent_id" readonly />

					<div class="form-row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="label_pt">` + messages("general.label.label_pt") + `</label>
								<input type="text" name="label_pt" id="label_pt" class="form-control validate-not-empty validate-blur" v-model="menu.label_pt"/>
							</div>
						</div>

						<div class="col-md-6">
							<div class="form-group">
								<label for="label_en">` + messages("general.label.label_en") + `</label>
								<input type="text" name="label_en" id="label_en" class="form-control validate-not-empty validate-blur" v-model="menu.label_en"/>
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="short_label_pt">` + messages("general.label.short_label_pt") + `</label>
								<input type="text" name="short_label_pt" id="short_label_pt" class="form-control validate-not-empty validate-blur" v-model="menu.short_label_pt"/>
							</div>
						</div>

						<div class="col-md-6">
							<div class="form-group">
								<label for="short_label_en">` + messages("general.label.short_label_en") + `</label>
								<input type="text" name="short_label_en" id="short_label_en" class="form-control validate-not-empty validate-blur" v-model="menu.short_label_en"/>
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="col-md-8">
							<div class="form-group">
								<label for="url">URL</label>
								<input type="text" name="url" id="url" class="form-control" v-model="menu.url" />
								<a :href="menu.url" target="_blank" class="small">` + messages("general.label.test") + `</a>
							</div>
						</div>

						<div class="col-md-4">
							<div class="form-group">
								<label for="">` + messages("system.label.order_within_parent") + `</label>
								<input type="text" name="order_within_parent" id="order_within_parent" class="form-control validate-not-empty validate-positive-integer validate-blur" v-model="menu.order_within_parent" />
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="col-md-8">
							<div class="form-group">
								<label for="custom_validation_class">` + messages("system.label.custom_validation_class") + `</label>
								<input type="text" name="custom_validation_class" id="custom_validation_class" class="form-control" v-model="menu.custom_validation_class" />
							</div>
						</div>

						<div class="col-md-4">
							<div class="form-group">
								<label for="icon_css_class">` + messages("system.label.icon_css_class") + `</label>

								<div class="input-group mb-3">
									<input type="text" name="icon_css_class" id="icon_css_class" class="form-control" v-model="menu.icon_css_class" />
									<div class="input-group-append">
								    	<span class="input-group-text"><i :class="menu.icon_css_class"></i></span>
								  	</div>
								</div>
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="col">
							<div class="mt-3 mb-3">
								<div><b>` + messages("system.label.misc_settings") + `</b></div>
								<div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="menu.is_active" name="is_active" id="is_active">
										<label class="form-check-label" for="is_active">` + messages("system.label.is_active") + `</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="menu.is_public" name="is_public" id="is_public">
										<label class="form-check-label" for="is_public">` + messages("system.label.is_public") + `</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="menu.show_when_authenticated" name="show_when_authenticated" id="show_when_authenticated">
										<label class="form-check-label" for="show_when_authenticated">` + messages("system.label.show_when_authenticated") + `</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="menu.has_divider_before" name="has_divider_before" id="has_divider_before">
										<label class="form-check-label" for="has_divider_before">` + messages("system.label.has_divider_before") + `</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="menu.opened_by_default" name="opened_by_default" id="opened_by_default">
										<label class="form-check-label" for="opened_by_default">` + messages("system.label.opened_by_default") + `</label>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="col">
							<div v-if="menu.id" class="mt-3 mb-3">
								<div><b>` + messages("system.label.roles_with_access") + `</b></div>
								<div>
									<div v-for="role in menu.roles" class="form-check form-check-inline">
										<input class="form-check-input" type="checkbox" :checked="role.has_access" name="role[]" :id="'role' + role.role.id" :value="role.role.id"/>
										<label class="form-check-label" :for="'role' + role.role.id">{{ role.role["label_" + lang] }}</label>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="form-row mt-3 mb-3">
						<div class="col">
							<button type="button" class="btn btn-success" @click="saveConfig"><i class="fa fa-check-circle"></i> ` + messages("general.label.save") + `</button>
							<button type="button" class="btn btn-danger" @click="resetConfig"><i class="fa fa-times-circle"></i> ` + messages("general.label.cancel") + `</button>
						</div>
					</div>
				</form>

			</div>
			<div v-else>
				<p class="alert alert-info">` + messages("system.text.choose_menu_to_config") + `</p>
			</div>
		</div>
	`
});
