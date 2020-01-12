import Vue from "vue";

export default {
  state: {
    userParams: {
      login: "",
      name: "",
      role: ""
    }
  },
  getters: {
    login(state) {
      return state.userParams.login;
    },
    name(state) {
      return state.userParams.name;
    },
    role(state) {
      return state.userParams.role;
    },
    isAuthorized(state) {
      return state.userParams.login ? true : false;
    },
    isAdmin(state) {
      return state.userParams.role === "ADMIN";
    }
  },
  mutations: {
    mutateUserParams(state, payLoad) {
      for (const property in state.userParams) {
        Vue.set(state.userParams, property, payLoad[property]);
      }
    }
  },
  actions: {
    changeUserParams(store, payLoad) {
      store.commit("mutateUserParams", payLoad);
    }
  }
};
