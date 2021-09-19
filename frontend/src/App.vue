<template>
    <v-app>
        <!-- https://vuetifyjs.com/en/components/application/ -->
        <v-navigation-drawer
                left
                app
                :clipped="true"
                v-model="drawer"
        >
            <template v-slot:prepend>
                <v-list-item two-line v-if="currentUser">
                    <v-list-item-content>
                        <v-list-item-title>{{currentUserLogin}}</v-list-item-title>
                        <v-list-item-subtitle>{{currentUserEmail}}</v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
            </template>

            <v-divider></v-divider>

            <v-list dense>
                <v-list-item
                        v-for="item in getAppBarItems()"
                        :key="item.title"
                        @click="item.clickFunction"
                >
                    <v-list-item-icon>
                        <v-icon :color="item.color">{{ item.icon }}</v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title>{{ item.title }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>
        </v-navigation-drawer>


        <v-app-bar
                :color="'indigo'"
                dark
                app
                id="myAppBar"
                :clipped-left="true"
        >
            <v-app-bar-nav-icon @click="toggleLeftNavigation"></v-app-bar-nav-icon>
        </v-app-bar>


        <v-main>
            <v-container fluid class="ma-0 pa-0">
                <router-view :key="`routerView`+`${$route.params.id}`"/>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
    import 'typeface-roboto'
    import axios from 'axios';
    import {mapGetters} from 'vuex'
    import {
        FETCH_USER_PROFILE,
        GET_USER,
    } from "./store";
    import {mortgage_application_name} from "./routes";
    import SimpleModal from "./SimpleModal";

    export default {
        data () {
            return {
                appBarItems: [
                    { title: 'Mortgage application', icon: 'mdi-home-city', clickFunction: this.goHome, requireAuthenticated: false },
                    { title: 'Participants', icon: 'mdi-human-queue', clickFunction: this.goHome, requireAuthenticated: false },
                    { title: 'My Account', icon: 'mdi-account', clickFunction: this.goProfile, requireAuthenticated: true },
                    { title: 'Logout', icon: 'mdi-logout', clickFunction: this.logout, requireAuthenticated: true },
                ],
                drawer: this.$vuetify.breakpoint.lgAndUp,
            }
        },
        components:{
            SimpleModal,
        },
        methods:{
            toggleLeftNavigation() {
                this.$data.drawer = !this.$data.drawer;
            },
            logout(){
                window.location.href = '/api/aaa-self-service';
            },
            goHome() {
                this.$router.push(({ name: mortgage_application_name}))
            },
            goProfile() {
                window.location.href = '/api/aaa-self-service/settings';
            },
            getAppBarItems(){
                return this.appBarItems.filter((value, index) => {
                    if (value.requireAuthenticated) {
                        return this.currentUser
                    } else {
                        return true
                    }
                }).filter((value, index) => {
                    if (value.displayCondition) {
                        return value.displayCondition();
                    } else {
                        return true
                    }
                })
            },
        },
        computed: {
            ...mapGetters({
                currentUser: GET_USER,
            }), // currentUser is here, 'getUser' -- in store.js
            currentUserLogin() {
                const name = this.$store.getters[GET_USER].traits.name;
                return name.first + ' ' + name.last;
            },
            currentUserEmail() {
                return this.$store.getters[GET_USER].traits.email;
            }
        },
        mounted() {
            this.$store.dispatch(FETCH_USER_PROFILE);
        },
        created() {
        },

    }
</script>

<style lang="stylus">
    html {
        overflow-y auto
    }

    .row {
      margin-top: 0px !important;
      margin-bottom: 0px !important;
    }

</style>
