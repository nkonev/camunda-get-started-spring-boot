import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";

Vue.use(Vuex);

export const GET_USER = 'getUser';
export const SET_USER = 'setUser';
export const UNSET_USER = 'unsetUser';
export const FETCH_USER_PROFILE = 'fetchUserProfile';

const store = new Vuex.Store({
    state: {
        currentUser: null,
    },
    mutations: {
        [SET_USER](state, payload) {
            state.currentUser = payload;
        },
        [UNSET_USER](state) {
            state.currentUser = null;
        },
    },
    getters: {
        [GET_USER](state) {
            return state.currentUser;
        },
    },
    actions: {
        [FETCH_USER_PROFILE](context) {
            axios.get(`/api/aaa-backend/sessions/whoami`).then(( {data} ) => {
                console.debug("fetched profile =", data);
                context.commit(SET_USER, data.identity);
            });
        },
    }
});

export default store;