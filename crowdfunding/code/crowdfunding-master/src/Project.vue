<template>
    <div v-if="this.$route.fullPath.indexOf('pledge') != -1">
        <div v-if="this.detailedProject.open == false">
            {{this.changeToProjects()}}
        </div>
        <div v-else-if="this.$session.exists()">
            <h1 class="text-center">Pledge</h1>
            <form id="pledgeForm">
                <div class="input-group">
                    <span class="input-group-addon" style="width:140px">Pledge Amount</span>
                    <input name="pledgeField" type="number" v-model="pledgeAmount" class="form-control">
                </div>
                <br />
                <div class="row">
                    <div class="col-3"></div>
                    <div class="col">
                        <label class="btn btn-secondary btn-block">
                            <input type="checkbox" name="anonymous" value="true" v-model="pledgeAnonymous">
                            Anonymous
                        </label>
                    </div>
                    <div class="col-3"></div>
                </div>

            </form>
            <button v-on:click="makePledge" class="btn btn-outline-dark btn-block">Make Pledge</button>
            <br /><br /><br/>
            <div v-if="errorFlag" style="color: red;">
                {{error}}
            </div>
        </div>
        <div v-else>
            {{this.changeToLogon()}}
        </div>
    </div>
    <div v-else id="project">

        <h1 class="text-center">{{this.detailedProject.title}}</h1>
        <h3 class="text-center" v-if="this.detailedProject.open == false" style="color: red;">Project Closed</h3>
        <div v-if="this.detailedProject.id != null">
            <br />
            <button v-if="this.$session.exists() && this.$session.get('userID') != this.detailedProject.creators[0].id" v-on:click="changeToPledge" class="btn btn-outline-dark btn-block">Pledge To This Project</button>
            <button v-if="this.$session.exists() && this.$session.get('userID') == this.detailedProject.creators[0].id && this.detailedProject.open != false" v-on:click="closeProject" class="btn btn-outline-dark btn-block">Close This Project</button>
        </div>
        <br/><br/><br/>
        <h4 class="text-center">{{this.calculateAmountOfBackers()}} Backers So Far</h4>
        <br />
        <h5 class="text-center">{{this.detailedProject.subtitle}}</h5>
        <br />
        <h6 class="text-center">Progress {{this.detailedProject.progress.currentPledged }}/{{this.detailedProject.target}}</h6>
        <div v-if="this.detailedProject.progress != null" id="backerProgress">
            <div id="backerBar">
                {{this.bar(this.detailedProject.progress, this.detailedProject.target)}}
            </div>
        </div>
        <table align="center" class="table table-bordered table-inverse">
            <tr>
                <td></td>
            </tr>
        </table>
        <div class="row">
            <div class="col">
                <p class="text-center">{{this.detailedProject.description}}</p>
                <br />
                <p class="text-center" v-for="c in this.detailedProject.creators">Creator: {{c.username}}</p>
                <br />
                <p class="text-center">Target: {{this.detailedProject.target}}</p>
                <br />
                <p class="text-center">Created At: {{this.getTime(this.detailedProject.creationDate)}}</p>
            </div>
            <div class="col">
                <img :src="getImage(this.detailedProject.id)" style="width:300px" align="right">
            </div>
        </div>
        <br />
        <table align="center" class="table table-bordered table-inverse">
            <tr>
                <td></td>
            </tr>
        </table>
        <h5 class="text-center">Last Five Backers</h5>
        <table align="center" class="table">
            <thead>
            <tr>
                <th class="text-center">Username</th>
                <th class="text-center">Amount Backed</th>
            </tr>
            </thead>
            <tbody>
                <tr v-for="backer in this.lastFiveBackers()">
                    <td class="text-center">{{backer.username}}</td>
                    <td class="text-center">${{backer.amount}}</td>
                </tr>
            </tbody>
        </table>
        <table align="center" class="table table-bordered table-inverse">
            <tr>
                <td></td>
            </tr>
        </table>

        <h5 class="text-center">Rewards</h5>
        <table align="center" class="table">
            <thead>
            <tr>
                <th class="text-center">Reward Amount</th>
                <th class="text-center">Description</th>
            </tr>
            </thead>
            <tr v-for="reward in this.detailedProject.rewards">
                <td class="text-center">{{reward.amount}}</td>
                <td class="text-center">{{reward.description}}</td>
            </tr>
        </table>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                error: "",
                errorFlag: false,
                detailedProject:[],
                pledgeAmount: 0,
                pledgeAnonymous: false
            }
        },
        mounted: function () {
            this.getSingleProject();
        },
        methods: {
            getSingleProject: function () {
                this.$http.get('http://localhost:4941/api/v2/projects/'+this.$route.params.projectID)
                    .then(function (response) {
                        this.detailedProject = response.data;
                    }, function (error) {
                        this.error = error;
                        this.errorFlag = true;
                    });
            },
            getTime: function (long) {
                var date = new Date(long);
                return date.toString()
            },
            bar: function (progress, target) {
                let width = 0;
                let cp = JSON.parse(JSON.stringify(progress)).currentPledged;
                let id = setInterval(frame, 10);

                //todo Find a better way to fix the loading bar problem
                if (window.location.href.substr(-2) !== '?r') {
                    window.location = window.location.href + '?r';
                }


                function frame() {
                    if (width >= (cp/target)*100) {
                        clearInterval(id);
                    } else {
                        width++
                        if (width > 100){width = 100}
                        try{
                            document.getElementById("backerBar").style.width = width + '%';
                        }catch (err){}

                    }
                }
            },
            lastFiveBackers: function () {
                let outBackers = [];
                try {
                    outBackers.push(JSON.parse(JSON.stringify(this.detailedProject.backers[0])));
                    outBackers.push(JSON.parse(JSON.stringify(this.detailedProject.backers[1])));
                    outBackers.push(JSON.parse(JSON.stringify(this.detailedProject.backers[2])));
                    outBackers.push(JSON.parse(JSON.stringify(this.detailedProject.backers[3])));
                    outBackers.push(JSON.parse(JSON.stringify(this.detailedProject.backers[4])));
                }
                catch(err) {
                }
                return outBackers;
            },
            calculateAmountOfBackers: function () {
                let outSet = new Set();
                for (var backer in this.detailedProject.backers){
                    outSet.add(this.detailedProject.backers[backer].username)
//                    console.log(this.detailedProject.backers[backer].username)
                }
//                console.log(JSON.stringify(this.detailedProject.backers));
//                console.log(outSet);
                return outSet.size
            },
            getDefaultImage: function () {
                return 'https://i.imgur.com/41ZTRpP.png'
            },
            getImage: function (id) {
                return 'http://localhost:4941/api/v2/projects/'+id+'/image';
            },
            changeToLogon: function () {
                window.location.replace("/user/login")
            },
            makePledge: function () {
                if (this.pledgeAmount <= 0){
                    this.errorFlag = true;
                    this.error = "Please make enter a real $ value";
                    return;
                }

                var pledge = {
                    "id":  this.$session.get('userID'),
                    "amount": parseInt(this.pledgeAmount),
                    "anonymous": this.pledgeAnonymous,
                    "card": {
                        "authToken": this.$session.get('X-Authorization')
                    }
                };
                this.$http.post('http://localhost:4941/api/v2/projects/' + this.detailedProject.id + '/pledge', pledge,{headers: {'X-Authorization': this.$session.get('X-Authorization')}})
                    .then(function (response) {
                        window.location.replace('/projects/' + this.detailedProject.id)
                    }, function (error) {
                        this.error = error.bodyText;
                        this.errorFlag = true;
                    });
            },
            changeToPledge: function () {
                window.location.replace('/projects/' + this.detailedProject.id + '/pledge')
            },
            closeProject: function () {
                var close = {
                    "open": false
                };

                this.$http.put('http://localhost:4941/api/v2/projects/' + this.detailedProject.id, close,{headers: {'X-Authorization': this.$session.get('X-Authorization')}})
                    .then(function (response) {
                        window.location.replace('/projects/')
                    }, function (error) {
                        this.error = error.bodyText;
                        this.errorFlag = true;
                    });
            },
            changeToProjects: function () {
                window.location.replace('/projects/' + this.detailedProject.id)
            }
        }
    }
</script>

<style>
    #backerProgress {
        width: 100%;
        background-color: #ddd;
    }

    #backerBar {
        width: 0%;
        height: 30px;
        background-color: #4CAF50;
    }
</style>