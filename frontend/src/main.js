import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import axios from "axios";
import bus, {
  LOGGED_OUT,
} from './bus';
import store, {UNSET_USER} from './store'
import router from './router.js'

axios.interceptors.response.use((response) => {
  return response
}, (error) => {
  // https://github.com/axios/axios/issues/932#issuecomment-307390761
  console.log("Catch error", error, error.request, error.response, error.config);
  if (axios.isCancel(error)) {
      return Promise.reject(error)
  } else if (error && error.response && error.response.status == 401 && error.config.url != '/api/profile') {
    console.log("Catch 401 Unauthorized, emitting ", LOGGED_OUT);
    store.commit(UNSET_USER);
    bus.$emit(LOGGED_OUT, null);
    return Promise.reject(error)
  } else {
    const consoleErrorMessage  = "Request: " + JSON.stringify(error.config) + ", Response: " + JSON.stringify(error.response);
    console.error(consoleErrorMessage);
    const errorMessage  = "Http error. Check the console";
    vm.$refs.appRef.onError(errorMessage);
    return Promise.reject(error)
  }
});

const vm = new Vue({
  vuetify,
  store,
  router,
  // https://ru.vuejs.org/v2/guide/render-function.html
  render: h => h(App, {ref: 'appRef'})
}).$mount('#root');
