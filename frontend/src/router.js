import Vue from 'vue'
import Router from 'vue-router'
import {mortgage_application_name, root } from "./routes";
import Error404 from "./Error404";
import MortgageApplicationView from "./MortgageApplicationView";

// This installs <router-view> and <router-link>,
// and injects $router and $route to all router-enabled child components
// WARNING You shouldn't include it in tests, else avoriaz's globals won't works (https://github.com/eddyerburgh/avoriaz/issues/124)
Vue.use(Router);

const router = new Router({
    mode: 'history',
    // https://router.vuejs.org/en/api/options.html#routes
    routes: [
        { name: mortgage_application_name, path: root, component: MortgageApplicationView},
        { path: '*', component: Error404 },
    ]
});


export default router;