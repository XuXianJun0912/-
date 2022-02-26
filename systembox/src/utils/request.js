// // request.js为请求拦截、请求数据封装主入口
// import axios from 'axios';
// import { getToken } from './auth';

// const instance = axios.create({
//     timeout: 5000,
//     baseURL: 'https://xxxxxxxxx/xxxx/',
//   });

//   // 请求拦截
//  instance.interceptors.request.use(
//     function(config) {
//      // eslint-disable-next-line prettier/prettier
//      config.headers.authorization = 'Bearer ' + getToken();
//      return config;
//     },
//     function(error) {
//      // Do something with request error
//      return Promise.reject(error);
//     }
//    );
   
//    instance.interceptors.response.use(
//     response => {
//      return response;
//     },
//     error => {
//      if (error.response.status == 401) {
//       window.location.href = '/#/login';
//      }
//      if (error.response.status == 404) {
//       window.location.href = '/404.html';
//      }
//      return Promise.reject(error.response.data);
//     }
//    );