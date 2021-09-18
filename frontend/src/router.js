import Vue from 'vue'
import Router from 'vue-router'
import {chat_list_name, root, chat_name, profile_self_name, videochat_name, profile_name} from "./routes";
import Error404 from "./Error404";
import MortgageApplicationView from "./MortgageApplicationView";
import UserSelfProfile from "./UserSelfProfile";

// This installs <router-view> and <router-link>,
// and injects $router and $route to all router-enabled child components
// WARNING You shouldn't include it in tests, else avoriaz's globals won't works (https://github.com/eddyerburgh/avoriaz/issues/124)
Vue.use(Router);

const router = new Router({
    mode: 'history',
    // https://router.vuejs.org/en/api/options.html#routes
    routes: [
        { name: chat_list_name, path: root, component: MortgageApplicationView},
        { name: profile_self_name, path: '/profile', component: UserSelfProfile},
        { path: '*', component: Error404 },
    ]
});


export default router;