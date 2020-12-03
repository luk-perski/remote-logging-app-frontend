Vue.component('math-history', {
	props: {
        caption: String,
	    items: Array
	},
	template:`
	    <div with: auto>
            <table class="table table-bordered scrollable-table">
                <caption>{{caption}}</caption>
                <tbody>
                    <thead class="thead-dark">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Number One</th>
                            <th scope="col">Number Two</th>
                            <th scope="col">Result</th>
                        </tr>
                    </thead>
                    <tr v-for="item in items">
                        <td>{{ item.id }}</td>
                        <td>{{ item.number_one }}</td>
                        <td>{{ item.number_two }}</td>
                        <td>{{ item.result }}</td>
                    </tr>
                </tbody>
            </table>
		</div>
	`
});