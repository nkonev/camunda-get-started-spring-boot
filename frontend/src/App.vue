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
                    <v-list-item-avatar  v-if="currentUser.avatar">
                        <img :src="currentUserAvatar"/>
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title>{{currentUser.login}}</v-list-item-title>
                        <v-list-item-subtitle>Logged In</v-list-item-subtitle>
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

                <LoginModal/>

                <router-view :key="`routerView`+`${$route.params.id}`"/>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
    import 'typeface-roboto'
    import axios from 'axios';
    import LoginModal from "./LoginModal";
    import {mapGetters} from 'vuex'
    import {
        FETCH_USER_PROFILE,
        GET_USER,
        UNSET_USER
    } from "./store";
    import bus, {
        LOGGED_OUT,
    } from "./bus";
    import {profile_self_name, chat_list_name} from "./routes";
    import SimpleModal from "./SimpleModal";
    import ChooseAvatar from "./ChooseAvatar";
    import {getCorrectUserAvatar} from "./utils";

    export default {
        data () {
            return {
                appBarItems: [
                    { title: 'Form data', icon: 'mdi-home-city', clickFunction: this.goHome, requireAuthenticated: false },

                    { title: 'My Account', icon: 'mdi-account', clickFunction: this.goProfile, requireAuthenticated: true },

                    { title: 'Logout', icon: 'mdi-logout', clickFunction: this.logout, requireAuthenticated: true },
                ],
                drawer: this.$vuetify.breakpoint.lgAndUp,
            }
        },
        components:{
            LoginModal,
            SimpleModal,
            ChooseAvatar,
        },
        methods:{
            toggleLeftNavigation() {
                this.$data.drawer = !this.$data.drawer;
            },
            logout(){
                console.log("Logout");
                axios.post(`/api/logout`).then(({ data }) => {
                    this.$store.commit(UNSET_USER);
                    bus.$emit(LOGGED_OUT, null);
                });
            },
            goHome() {
                this.$router.push(({ name: chat_list_name}))
            },
            goProfile() {
                this.$router.push(({ name: profile_self_name}))
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
            currentUserAvatar() {
                return getCorrectUserAvatar(this.currentUser.avatar);
            },
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

<style scoped lang="stylus">
    .call-blink {
        animation: blink 0.5s;
        animation-iteration-count: 5;
    }

    @keyframes blink {
        50% { opacity: 10% }
    }
</style>