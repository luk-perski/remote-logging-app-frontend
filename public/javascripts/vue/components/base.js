
Vue.component('small-label', {
	props: {
		text: String
	},
	template: `
		<small><b>{{ text }}</b>:
			<slot></slot>
		</small>
	`
});

Vue.component('span-tooltip', {
	template: `
		<span data-toggle="tooltip" data-placement="top">
			<slot></slot>
		</span>
	`
});

Vue.component('value-group', {
	props: {
		label: String
	},
	template: `
		<div class="value-group">
			<div class="label">{{ label }}</div>
			<div class="value">
				<slot></slot>
			</div>
		</div>
	`
});
