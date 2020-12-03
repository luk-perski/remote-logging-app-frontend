
/////// FORM ///////
const form_component_template = `
    <form :class="element.css_classes" :action="element.action" :method="element.method" :id="element.id">
        <input type="hidden" :name="element.csrf_token_name" :value="element.csrf_token_value"/>

        <component v-for="(child, index) in element.children" :is="child.component" :element="child" :lang="lang" :key="index"></component>
     </form>
`;
Vue.component('form-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(form_component_template).render.call(this, createElement); } });

/////// ROW ///////
const row_component_template = `
    <div :class="['form-row', element.css_classes].filter(Boolean).join(' ')" :id="element.id">
        <component v-for="(child, index) in element.children" :is="child.component" :element="child" :lang="lang" :key="index"></component>
    </div>
`;
Vue.component('row-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(row_component_template).render.call(this, createElement); } });

/////// COL ///////
const col_component_template = `
    <div :class="[((element.grid_css_classes) ? element.grid_css_classes : 'col'), element.extra_css_classes].filter(Boolean).join(' ')" :id="element.id">
        <component v-for="(child, index) in element.children" :is="child.component" :element="child" :lang="lang" :key="index"></component>
    </div>
`;
Vue.component('col-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(col_component_template).render.call(this, createElement); } });

/////// INPUT-TEXT ///////
const input_text_component_template = `
    <div class="form-group">
        <label :for="element.id">{{ element["label_" + lang] }}</label>
        <input :class="['form-control', element.css_classes].filter(Boolean).join(' ')" type="text" :name="element.name" :id="element.id" :placeholder="element['placeholder_' + lang]" />
    </div>
`;
Vue.component('input-text-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(input_text_component_template).render.call(this, createElement); } });

/////// INPUT-PASSWORD ///////
const input_password_component_template = `
    <div class="form-group">
        <label :for="element.id">{{ element["label_" + lang] }}</label>
        <input :class="['form-control', element.css_classes].filter(Boolean).join(' ')" type="password" :name="element.name" :id="element.id" />
    </div>
`;
Vue.component('input-password-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(input_password_component_template).render.call(this, createElement); } });

/////// SELECT ///////
const select_component_template = `
    <div class="form-group">
        <label :for="element.id">{{ element["label_" + lang] }}</label>
        <select :name="element.name" :id="element.id" :class="['form-control', element.css_classes].filter(Boolean).join(' ')">
            <component v-for="(child, index) in element.children" :is="child.component" :element="child" :lang="lang" :key="index"></component>
        </select>
    </div>
`;
Vue.component('select-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(select_component_template).render.call(this, createElement); } });

/////// OPTION ///////
const option_component_template = `
    <option :value="element.value" :id="element.id" :class="element.css_classes" :selected="element.is_selected">{{ element["label_" + lang] }}</option>
`;
Vue.component('option-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(option_component_template).render.call(this, createElement); } });

/////// BUTTON ///////
const button_component_template = `
    <button :class="'btn btn-' + element.theme + ' ' + element.extra_css_classes" :type="element.type" :id="element.id">
        <component v-for="(child, index) in element.children" :is="child.component" :element="child" :lang="lang" :key="index"></component>
    </button>
`;
Vue.component('button-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(button_component_template).render.call(this, createElement); } });

/////// FONT-AWESOME ///////
const fa_component_template = `
    <span>
        <span></span>
        <i :class="'fa fa-' + element.icon + ' ' + element.extra_css_classes" :id="element.id"></i>
        <span></span>
    </span>
`;
Vue.component('fa-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(fa_component_template).render.call(this, createElement); } });

/////// TEXT ///////
const text_component_template = `<span :id="element.id">{{ element["text_" + lang] }}</span>`;
Vue.component('text-component', { props: { element: Object, lang: String }, render(createElement){ return Vue.compile(text_component_template).render.call(this, createElement); } });
