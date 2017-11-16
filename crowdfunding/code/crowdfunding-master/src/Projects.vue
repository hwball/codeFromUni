<template>
    <div v-if="createFlag" class="container-fluid">
        <div id="projectForm">
            <div class="row">
                <div class="col">
                    <div class="row">
                            <div class="input-group">
                                <span class="input-group-addon" style="width:100px">Title</span>
                                <input name="titleField" type="text" v-model="newProjectTitle" class="form-control">
                            </div>
                            <div class="input-group">
                                <span class="input-group-addon" style="width:100px">Subtitle</span>
                                <input name="subtitleField" type="text" v-model="newProjectSubtitle"  class="form-control">
                            </div>
                            <div class="input-group">
                                <span class="input-group-addon" style="width:100px">Description</span>
                                <input name="descriptionField" type="text" v-model="newProjectDescription" class="form-control">
                            </div>
                            <div class="input-group">
                                <span class="input-group-addon" style="width:100px">Target</span>
                                <input name="targetField" type="number" v-model="newProjectTarget" class="form-control">
                            </div>
                    </div>
                    <br />
                    <div class="row">
                            <div class="input-group">
                                <span class="input-group-addon" style="width:140px">Reward Amount</span>
                                <input name="rAmountField" type="number" v-model="newRewardAmount" class="form-control">
                            </div>
                            <div class="input-group">
                                <span class="input-group-addon" style="width:140px">Description</span>
                                <input name="rDescriptionField" type="text" v-model="newRewardDescription" class="form-control">
                            </div>
                    </div>
                </div>
                <div class="col">
                    <img :src="this.projectImg" height="280" align="middle"/>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <button v-on:click="addReward" class="btn btn-dark btn-block">Add Reward</button>
                </div>
                <div class="col">
                    <div class="input-group" >
                        <input type="file" v-on:change="onFileChange" class="btn btn-dark btn-block">
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col"></div>
            <div class="col">
                <table align="center">
                    <thead>
                    <tr>
                        <th>Reward Amount</th>
                        <th>Reward Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="reward in newProjectRewards">
                        <td>{{reward.amount}}</td>
                        <td>{{reward.description}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col"></div>
        </div>
        <br />
        <div class="row">
            <div class="col"></div>
            <div class="col">
                <button v-on:click="postNewProject" class="btn btn-outline-dark btn-block">Create</button>
                <br /><br />
                <div class="row">
                    <div class="col-3"></div>
                    <div class="col-6">
                        <div v-if="errorFlag" class="alert alert-danger" role="alert">
                            <p class="text-center">{{error}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col"></div>
        </div>
    </div>

    <div v-else>
        <div v-if="errorFlag" style="color: red;">
            {{error}}
        </div>

        <div id="projects">
            <div id="search">
                <div class="navbar-form" role="search">
                    <input name="query" v-model="searchQuery" class="form-control" placeholder="Search" type="text">
                </div>

                <div class="row">
                    <div class="col"></div>
                    <div v-if="this.$session.exists()" class="btn-group">
                        <label class="btn btn-secondary active">
                            <input type="radio" name="projectFilter" value="0" v-model="searchType" autocomplete="off" checked>
                            View All Projects
                        </label>
                        <label class="btn btn-secondary">
                            <input type="radio" name="projectFilter" value="1" v-model="searchType" autocomplete="off">
                            View My Backings
                        </label>
                        <label class="btn btn-secondary">
                            <input type="radio" name="projectFilter" value="2" v-model="searchType" autocomplete="off">
                            View My Projects
                        </label>
                    </div>
                    <div class="col"></div>
                </div>
                <br />
                <div class="row">
                    <div class="col"></div>
                    <div class="col">
                        <button v-if="this.$session.exists()" v-on:click="createNewProject"  class="btn btn-outline-dark btn-block">Make New Project</button>
                    </div>
                    <div class="col"></div>
                </div>

            </div>


            <br />
            <div class="card-deck">
                <div v-for="rowArray in filteredProjects" class="row">
                    <div v-if="project != null" class="card text-white bg-dark" v-for="project in rowArray">
                        <img class="card-img" :src="getImage(project.id)">
                        <div class="card-body">
                            <h4 class="card-title">{{project.title}}</h4>
                            <h5 class="card-subtitle">{{project.subtitle}}</h5>
                        </div>
                        <div class="card-footer">
                            <router-link :to="{name: 'project', params: {projectID: project.id}}" class="btn btn-primary btn-block">View</router-link>
                        </div>
                    </div>
                </div>
            </div>
            <br />
            <button v-if="showMoreCheck" v-on:click="increaseRow"  class="btn btn-outline-dark btn-block">Show More</button>
            <br />
        </div>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                error: "",
                errorFlag: false,
                projects:Array,
                searchQuery: "",
                createFlag: false,
                newProjectTitle: '',
                newProjectSubtitle: '',
                newProjectDescription: '',
                newProjectTarget: '',
                newProjectRewards: [],
                newRewardAmount: '',
                newRewardDescription: '',
                newProjectID: 0,
                searchType: '0',
                tempBackings: [],
                tempMyProjects: [],
                filteredProj: [],
                arrayLists: [],
                projectImg: '',
                binImage:'',
                showAdditionalRowCount: 1,
                showMoreCheck: true
            }
        },
        computed: {
            filteredProjects: function () {
                var outData = [];
                if (this.searchType === '0') {
                    var filterKey = this.searchQuery;
                    var data = this.projects;
                    if (filterKey) {
                        for (let proj in data) {
                            if (String(data[proj].title).toLowerCase().indexOf(filterKey.toLowerCase()) != -1 ||
                                String(data[proj].subtitle).toLowerCase().indexOf(filterKey.toLowerCase()) != -1) {
                                outData.push(data[proj])
                            }
                        }
                        console.log(outData);
                        return this.arrayOfArrays(outData, 3)
                    } else {
//                        console.log(this.arrayOfArrays(outData, 3));
                        return this.arrayOfArrays(data, 3)
                    }
                } else if (this.searchType === '1') {//my backings
//                    console.log(this.projects)
                    for (var proj in this.projects) {
                        this.checkProjectBackers(this.projects[proj], this.$session.get("userID"))
                    }
                    return this.arrayOfArrays(this.tempBackings, 3)
                } else if (this.searchType === '2') {//my projects
                    for (let proj in this.projects) {
                        this.checkProjectCreatorID(this.projects[proj], this.$session.get("userID"))
                    }
                    return this.arrayOfArrays(this.tempMyProjects, 3)
                }
            }
        },
        mounted: function () {
            this.getProjects(this.filteredProjects)
        },
        methods: {
            getProjects: function (filteredProj) {
                this.$http.get('http://localhost:4941/api/v2/projects')
                    .then(function (response) {
                        this.projects = response.data;
                        filteredProj
                    }, function (error) {
                        this.error = error;
                        this.errorFlag = true;
                    });
            },
            getDefaultImage: function () {
               return 'https://i.imgur.com/41ZTRpP.png'
            },
            getImage: function (id) {
                return 'http://localhost:4941/api/v2/projects/'+id+'/image';
            },
            createNewProject: function () {
                this.createFlag = true;
            },
            addReward: function () {
                if (this.newRewardDescription.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a reward description";
                    return;
                }
                if (parseInt(this.newRewardAmount) > 2147483647){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a number lower than  2,147,483,647 for the reward amount";
                    return;
                }

                var temp = {
                    "amount": parseInt(this.newRewardAmount),
                    "description": this.newRewardDescription
                };
                this.newProjectRewards.push(temp)
            },
            postNewProject: function () {
                this.errorFlag = false;
                this.error = '';

                if (this.newProjectTitle.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a title";
                    return;
                }
                if (this.newProjectSubtitle.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a subtitle";
                    return;
                }
                if (this.newProjectDescription.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a description";
                    return;
                }
                if (parseInt(this.newProjectTarget) > 2147483647){
                    this.errorFlag = true;
                    this.error = "Please make sure you have entered a number lower than  2,147,483,647 for the target";
                    return;
                }
                if ( this.newProjectRewards.length < 1){
                    this.errorFlag = true;
                    this.error = "Please make sure you have at least one reward";
                    return;
                }

                var newProject = {
                    "title": this.newProjectTitle,
                    "subtitle": this.newProjectSubtitle,
                    "description": this.newProjectDescription,
                    "target": parseInt(this.newProjectTarget),
                    "creators": [
                        {
                            "id": this.$session.get('userID')
                        }
                    ],
                    "rewards": this.newProjectRewards
                };

                this.$http.post('http://localhost:4941/api/v2/projects', newProject,{headers: {'X-Authorization': this.$session.get('X-Authorization')}})
                    .then(function (response) {
                        this.newProjectID = response.data.id;
                        this.updateProjectImage(response.data.id)
                    }, function (error) {
                        this.error = error;
                        this.errorFlag = true;
                    });
            },
            updateProjectImage: function (id) {
                let uploadImage = '';

                if (this.projectImg === ''){
                    window.location.replace('/projects/' + id)
                    return;
                }else{
                    uploadImage = this.binImage
                }
                console.log(this.binImage)
                this.$http.put('http://localhost:4941/api/v2/projects/' + id + '/image', uploadImage,{headers:
                    {'X-Authorization': this.$session.get('X-Authorization'),
                        "Content-Type": "image/png"}})
                    .then(function (response) {
                        window.location.replace('/projects/' + id)
                    }, function (error) {
                        this.error = error.bodyText;
                        this.errorFlag = true;
                    });
            },
            checkProjectCreatorID: function (proj, thisCreator) {
                this.$http.get('http://localhost:4941/api/v2/projects/'+proj.id)
                    .then(function (response) {
                        if (response.data.creators[0].id === thisCreator && !this.tempMyProjects.includes(proj)){
//                            console.log(response.data.creators[0].id === thisCreator && !this.tempMyProjects.includes(proj))
                            this.tempMyProjects.push(proj)
//                            console.log(response.data.creators[0].id === thisCreator && !this.tempMyProjects.includes(proj))
                        }
                    }, function (error) {
                        this.error = error;
                        this.errorFlag = true;
                    })
                    .then(
                    );
            },
            checkProjectBackers:function (proj, thisCreator) {
                this.$http.get('http://localhost:4941/api/v2/projects/'+proj.id)
                    .then(function (response) {
                        let idList = []
                        for (let backer in response.data.backers){
//                            console.log(response.data.backers[backer].id)
                            idList.push(response.data.backers[backer].id)
                        }
                        if (idList.includes(thisCreator) && !this.tempBackings.includes(proj)){
//                            console.log('here')
                            this.tempBackings.push(proj)
                        }
                    }, function (error) {
                        this.error = error;
                        this.errorFlag = true;
                    });
            },
            arrayOfArrays: function (startData, rowSize) {
                let outData = [];
                let rowCount = 0;
                let colCount = 0;
//                console.log(startData.length)
                for (let i = 0; i < (1 +this.showAdditionalRowCount); i++){//(startData.length/rowSize); i++){
                    outData.push([])
                }

                for (let i = 0; i < (1 +this.showAdditionalRowCount)*rowSize;i++){//startData.length; i++){
//                    console.log(i)
                    outData[rowCount].push(startData[i]);
                    colCount += 1;
                    if (colCount >= rowSize){
                        rowCount += 1;
                        colCount = 0;
                    }
                }
//                console.log(this.showAdditionalRowCount);
//                console.log((startData.length/rowSize));
                if (this.showAdditionalRowCount +1 >= (startData.length/rowSize)){
                    this.showMoreCheck = false;
                }else{
                    this.showMoreCheck = true;
                }
                return outData
            },
            onFileChange: function(e) {
                var files = e.target.files || e.dataTransfer.files;
                if (!files.length)
                    return;
                this.createImage(files[0]);
            },
            createImage: function(file) {
                var reader = new FileReader();
                reader.onload = (e) => {
                    this.projectImg = e.target.result;
                };
                reader.readAsDataURL(file);
                this.binImage = file;
            },
            increaseRow: function () {
                this.showAdditionalRowCount += 2;
            }
        }
    }
</script>

<style>
</style>