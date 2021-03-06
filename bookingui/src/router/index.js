import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
// import Hotel from '../views/Hotel.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/hotel',
    name: 'hotel',
    component: () => import(/* webpackChunkName: "about" */ '../views/Hotel.vue')
  },{
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },{
    path: '/checkout/:step',
    name: 'checkout',
    component: () => import(/* webpackChunkName: "about" */ '../views/Checkout.vue')
  },{
    path: '/newUser',
    name: 'newUser',
    component: () => import(/* webpackChunkName: "about" */ '../views/NewUser.vue')
  },{
    path: '/Success',
    name: 'Success',
    component: () => import(/* webpackChunkName: "about" */ '../views/Success.vue')
  },{
    path: '/order',
    name: 'order',
    component: () => import(/* webpackChunkName: "about" */ '../views/Order.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router