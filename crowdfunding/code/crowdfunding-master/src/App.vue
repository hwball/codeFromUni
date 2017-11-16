<template>
    <div id="app" class="container">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand"><router-link :to="{name: 'home'}" class="btn btn-dark btn-block">SENG365</router-link></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="navbar-nav-scroll">
                <ul class="navbar-nav bd-navbar-nav flex-row">
                    <li class="nav-item">
                        <a class="nav-link"><router-link :to="{name: 'projects'}" class="btn btn-dark btn-block">Projects</router-link></a>
                    </li>
                </ul>
            </div>
            <ul class="navbar-nav flex-row ml-md-auto d-none d-md-flex">
                <li class="nav-item" v-if="!this.$session.exists()">
                    <a class="nav-link"><router-link :to="{name: 'user'}" class="btn btn-dark btn-block">Register User</router-link></a>
                </li>
                <li class="nav-item" v-if="!this.$session.exists()">
                    <a class="nav-link"><router-link :to="{name: 'login'}" class="btn btn-dark btn-block">Login</router-link></a>
                </li>
                <li class="nav-item" v-if="this.$session.exists()">
                    <a class="nav-link"><button v-on:click="logOut" class="btn btn-dark btn-block" >Log Out</button></a>
                </li>
                <!--<li class="nav-item">-->
                    <!--<a class="nav-link"><button v-on:click="resetDB" class="btn btn-dark btn-block" >RESET</button></a>-->
                <!--</li>-->
            </ul>
        </nav>
        <br /><br />
        <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods: {
      logOut: function () {
          this.$http.post('http://localhost:4941/api/v2/users/logout', {},{headers: {'X-Authorization': this.$session.get('X-Authorization')}})
              .then(function (response) {
                  this.$session.destroy();
                  window.location = "/";
              }, function (error) {
              });

      },
//      resetDB: function () {
//          this.$http.post('http://localhost:4941/api/v2/admin/reset')
//              .then(function (response) {
//              }, function (error) {
//              });
//
//      }
  }
}
</script>

