// // auth.js为设置token和删除token及判断用户是否登录封装主入口 

// import axios from 'axios';
// import { getToken } from './auth';

// const instance = axios.create({
//     timeout: 5000,
//     baseURL: 'https://xxxxxxxxx/xxxx/',
//   });

// export function isLogin() {
//     if (localStorage.getItem('token')) {
//      return true;
//     } else {
//      return false;
//     }
//    }
//    export function getToken() {
//     return localStorage.getItem('token');
//    }
//    export function setToken(token) {
//     localStorage.setItem('token', token);
//    }
   
//    export function removeToken() {
//     localStorage.removeItem('token');
//    }

//     /**
//   * 获取数据 get请求
//   * @param {*} url
//   * @param {*} config
//   */
//  export const get = (url, config) => instance.get(url, config);
 
//  /**
//   * post请求
//   * @param {*} url
//   * @param {*} data
//   * @param {*} config
//   */
//  export const post = (url, data) => instance.post(url, data);
//  /**
//   * put
//   * @param {*} url
//   * @param {*} data
//   * @param {*} config
//   */
//  export const put = (url, data, config) => instance.put(url, data, config);
 
//  /**
//   * delete
//   * @param {*} url
//   * @param {*} config
//   */
//  export const remove = (url, config) => instance.delete(url, config);