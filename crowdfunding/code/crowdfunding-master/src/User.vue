<template>
    <div id="User">
        <div class="container">
            <div class="input-group">
                <span class="input-group-addon" style="width:100px">Username</span>
                <input name="usernameField" v-model="userName" class="form-control">
            </div>
            <div class="input-group">
                <span class="input-group-addon" style="width:100px">Email</span>
                <input name="emailField" type="email" v-model="userEmail" class="form-control">
            </div>
            <div class="input-group">
                <span class="input-group-addon" style="width:100px">Password</span>
                <input name="passwordField" type="password" v-model="userPassword" class="form-control">
            </div>
            <div class="input-group">
                <span class="input-group-addon" style="width:100px">Location</span>
                <input name="locationField" v-model="userLocation" class="form-control">
            </div>
        </div>

        <div class="container">
            <div class="row">
                <div class="col-5"></div>
                <div class="col-2">
                    <button v-on:click="createUser" class="btn btn-outline-dark btn-block">Create User</button>
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
            createUser: function () {
                this.errorFlag = false;
                this.error = '';

                if (this.userName.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a username";
                    return;
                }
                if (this.validateEmail(this.userEmail)){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a correct email";
                    return;
                }
                if (this.userPassword.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a password";
                    return;
                }

                var formData = {
                    'username': this.userName,
                    'email': this.userEmail,
                    'password': this.userPassword,
                    'location': this.userLocation
                }
                this.$http.post('http://localhost:4941/api/v2/users/', formData, {headers:{'Content-Type': 'application/json'}})
                    .then(function (response) {
                        this.user = response.data;
                    }, function (error) {
                        this.error = error.bodyText;
                        this.errorFlag = true;
                    })
                    .then(this.userLogin)
            },
            userLogin: function () {
                if (!this.errorFlag){
                    this.$http.post('http://localhost:4941/api/v2/users/login?username='+this.userName+"&password="+this.userPassword)
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
            },
            validateEmail: function(email) {
                var re = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
                console.log(email.length)
                if (email.length < 1){return true;}
                return !re.test(email);
            }
        }
    }
</script>