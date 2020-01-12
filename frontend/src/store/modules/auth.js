export default {
  state: {
    authInfo: [
      {
        label: "Login",
        title: "Логин",
        value: "",
        type: "text",
        isFilled: false
      },
      {
        label: "Password",
        title: "Пароль",
        value: "",
        type: "password",
        isFilled: false
      }
    ],
    errorStatus: false
  },
  getters: {
    authInfo(state) {
      return state.authInfo;
    },
    errorStatus(state) {
      return state.errorStatus;
    }
  },
  mutations: {
    mutateFieldValue(state, payLoad) {
      const index = payLoad.index;
      const value = payLoad.value;
      state.authInfo[index].value = value;
    },
    mutateIsFilledStatus(state, payLoad) {
      const index = payLoad.index;
      const status = payLoad.status;
      state.authInfo[index].isFilled = status;
    },
    mutateAuthErrorStatus(state, payLoad) {
      const status = payLoad;
      state.errorStatus = status;
    }
  },
  actions: {
    changeFieldValue(store, payLoad) {
      store.commit("mutateFieldValue", payLoad);
    },
    changeIsFilledStatus(store, payLoad) {
      store.commit("mutateIsFilledStatus", payLoad);
    },
    changeAuthErrorStatus(store, payLoad) {
      store.commit("mutateAuthErrorStatus", payLoad);
    }
  }
};
