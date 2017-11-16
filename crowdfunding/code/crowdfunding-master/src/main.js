import Vue from 'vue'
import App from './App.vue'
import Home from './Home.vue'
import Projects from './Projects.vue'
import Project from './Project.vue'
import User from './User.vue'
import Login from './Login.vue'

import VueRouter from 'vue-router';
Vue.use(VueRouter);
import VueResource from 'vue-resource';
Vue.use(VueResource);
import VueSession from 'vue-session'
Vue.use(VueSession);

const routes = [
    {
        path: "/",
        name: "home",
        component: Home
    },
    {
        path:"/projects/:projectID",
        name: "project",
        component: Project
    },
    {
        path: "/projects/:projectID/pledge",
        name: "pledge",
        component: Project
    },
    {
        path: "/projects",
        name: "projects",
        component: Projects
    },
    {
        path: "/user",
        name: "user",
        component: User
    },
    {
        path: "/user/login",
        name: "login",
        component:Login
    }
];

const router = new VueRouter({
    routes: routes,
    mode: 'history'
});

new Vue({
  el: '#app',
  router: router,
  render: h => h(App)
});
