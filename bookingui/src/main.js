import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';

import axios from 'axios'
import VueAxios from 'vue-axios'

axios.interceptors.request.use(function (config) {
  // Do something before request is sent
  config.headers["Authorization"] = "Bearer " + store.getters.getUserInfo.token;
  config.headers["Access-Control-Allow-Origin"] = "*";
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});

Vue.use(VueAxios, axios)
// Vue.axios.defaults.baseURL = "http://localhost:8080/";
Vue.axios.defaults.baseURL = process.env.NODE_ENV === 'production' ? "http://ooaddemo.japaneast.cloudapp.azure.com:8080/" : "http://localhost:8080/";

Vue.use(require('vue-moment'));

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  const publicPages = ['/', '/hotel', '/checkout/1', '/login'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = store.getters.getUserInfo.token !== "";

  // try to access a restricted page + not logged in
  if (authRequired && !loggedIn) {
    return next('/login');
  }
  next();
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
