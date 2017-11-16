<template>
    <div v-if="this.$session.exists()">{{window.location = "/"}}</div>

    <div v-else id="Login">
        <div class="input-group">
            <span class="input-group-addon" style="width:100px">Username</span>
            <input name="usernameField" v-model="userName" class="form-control">
        </div>
        <div class="input-group">
            <span class="input-group-addon" style="width:100px">password</span>
            <input name="passwordField" type="password" v-model="userPassword" class="form-control">
        </div>
        <br />
        <div class="container">
            <div class="row">
                <div class="col-5"></div>
                <div class="col-2">
                    <button v-on:click="userLogin" class="btn btn-outline-dark btn-block">Login</button>
                </div>
                <div class="col-5"></div>
            </div>
            <br />
            <div class="row">
                <div class="col-3"></div>
                <div class="col-6">
                    <div v-if="errorFlag" class="alert alert-danger" role="alert">
                        <p class="text-center">{{error}}</p>
                    </div>
                </div>
                <div class="col-3"></div>
            </div>

        </div>


    </div>
</template>

<script>
    export default {
        data(){
            return{
                error: "",
                errorFlag: false,
                userName: '',
                userEmail: '',
                userPassword: '',
                userLocation: '',
                user: {},
                userToken: {}
            }
        },
        methods: {
            userLogin: function () {
                var url
                if (this.userName.indexOf('@') != -1){
                    url = 'http://localhost:4941/api/v2/users/login?email='+this.userName+"&password="+this.userPassword;
                }else{
                    url = 'http://localhost:4941/api/v2/users/login?username='+this.userName+"&password="+this.userPassword;

                }
                this.$http.post(url)
                    .then(function (response) {
                        this.userToken = JSON.parse(JSON.stringify(response.data.token));
                        this.$session.start();
                        this.$session.set('X-Authorization', this.userToken);
                        this.$session.set('userID', JSON.parse(JSON.stringify(response.data.id)));
                        //todo change this to user page
                        window.location = "/";
                    }, function (error) {
                        this.error = error.bodyText;
                        this.errorFlag = true;
                    });
            }
        }
    }
</script>