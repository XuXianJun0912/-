'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  devServer: {
    open: true, //是否自动弹出浏览器页面
    port: '8080',
    disableHostCheck: true,
  }
})
