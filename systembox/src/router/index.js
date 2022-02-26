import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/loginPage/Login'
import Header from '@/components/Header'
import HomePage from '@/components/HomePage'
import Sider from '@/components/Sider'
import UserManages from '@/pages/userManages/UserManages'
import CustomerManages from '@/pages/customerManages/CustomerManages'
import BoxManages from '@/pages/boxManages/BoxManages'
import BoxNumber from '@/pages/boxManages/BoxNumber'
import OrderManages from '@/pages/orderManages/OrderManages'
import AdvertManages from '@/pages/advertManages/AdvertManages'
import ViewManages from '@/pages/viewManages/ViewManages'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path:'/',
      name:'Login',
      component:Login
    },
    {
      path:'/sider',
      name:'Sider',
      component: Sider
    }, 
    {
      path:'/header',
      name:'Header',
      component: Header
    },


    {
      path:'/homepage',
      name:'HomePage',
      component: HomePage,
      children : [
            {
              path:"/usermanages",
              
              component: UserManages
            },
            {
              path:'/customermanages',
              name:'CustomerManages',
              component: CustomerManages
            },
            {
              path:'/boxmanages',
              name:'BoxManages',
              component: BoxManages
            },
            {
              path:'/boxnumber',
              name:'BoxNumber',
              component: BoxNumber
            },
            {
              path:'/ordermanages',
              name:'OrderManages',
              component: OrderManages
            },
            {
              path:'/advertmanages',
              name:'AdvertManages',
              component: AdvertManages
            },
            {
              path:'/viewmanages',
              name:'ViewManages',
              component: ViewManages
            },

        ],
    },
]
})
