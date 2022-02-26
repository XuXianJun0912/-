// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from "./store"
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import qs from 'qs' 


//import httpService from './service/http-service'
import axios from "axios";
// import '../config/axios';
Vue.prototype.$axios = axios;

Vue.prototype.$qs = qs;

Vue.config.productionTip = false
Vue.use(ElementUI);

// 拦截请求
axios.defaults.baseURL = "http://localhost:8081/"
axios.interceptors.request.use(config => {
  if ("/common/login" != config.url) {
    if (localStorage.getItem("token")) {
      config.headers["Authorization"] =
        "Bearer " + localStorage.getItem("token");
      console.log(localStorage.getItem("token"))
    }
  }
  return config
}, err => {
  return Promise.reject(err);
}),

  /* eslint-disable no-new */
  new Vue({
    el: '#app',
    router,
    store,
    qs,
    components: { App },
    template: '<App/>'
  })
