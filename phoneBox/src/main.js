import Vue from "vue";
import router from "./router";
import store from "./store"
import "vant/lib/index.css";
import Vant from "vant";
import App from "./App";

import {
  Toast
} from "vant";
import axios from "axios";

import {
  Lazyload
} from "vant";
import {
  Image as VanImage
} from 'vant';

import Router from 'vue-router'
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VanImage);
Vue.use(Lazyload);

Vue.prototype.$axios = axios;
Vue.use(Vant);
Vue.use(Toast);

Vue.config.productionTip = false;
// 请求拦截
axios.defaults.baseURL = "http://localhost:8081/"
axios.interceptors.request.use(
  config => {
    if ("/common/login" != config.url) {
      if (localStorage.getItem("token")) {
        config.headers["Authorization"] =
          " Bearer " + localStorage.getItem("token");
        // console.log(localStorage.getItem("token"));
        // 让每个请求携带自定义token 根据实际情况自行修改
      }
    }
    return config;
  },
  err => {
    todo待完善接口拦截;
    return Promise.reject(err);
  }
);
/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  store,
  components: {
    App
  },
  template: "<App/>"
});
