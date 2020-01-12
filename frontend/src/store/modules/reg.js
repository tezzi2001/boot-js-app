export default {
  state: {
    regInfo: [
      {
        value: "",
        label: "Login",
        title: "Логин",
        commonError:
          "Логин должен начинаться с буквы и состоять из латинских букв.",
        pattern: /^[a-zA-Z][a-zA-Z0-9-.]{1,20}$/,
        requestName: "checkLogin",
        existsError: "Такой логин уже есть!",
        errorStatus: true,
        type: "text"
      },
      {
        value: "",
        label: "Name",
        title: "Имя",
        pattern: /^[a-zA-Z]+$/,
        commonError: "Имя должно состоять из латинских букв.",
        requestName: "",
        existsError: "",
        errorStatus: true,
        type: "text"
      },
      {
        value: "",
        label: "Email",
        title: "Почта",
        pattern: /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/,
        commonError: "Некорректная почта.",
        requestName: "checkEmail",
        existsError: "Такая почта уже есть!",
        errorStatus: true,
        type: "email"
      },
      {
        value: "",
        label: "Password",
        title: "Пароль",
        pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/,
        commonError:
          "Пароль должен иметь минимум 8 символов, 1 заглавную букву и состоять из латинских букв и знаков",
        requestName: "",
        existsError: "",
        errorStatus: true,
        type: "password"
      },
      {
        value: "",
        label: "Confirm password",
        title: "Подтвердите пароль",
        pattern: /(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/,
        requestName: "",
        existsError: "",
        errorStatus: true,
        type: "password"
      }
    ]
  },
  getters: {
    regInfo(state) {
      return state.regInfo;
    }
  },
  mutations: {
    mutateRegInfoValue(state, payLoad) {
      let index = payLoad.index;
      let value = payLoad.value;
      state.regInfo[index].value = value;
    },
    mutateErrorStatus(state, payLoad) {
      let status = payLoad.status;
      let index = payLoad.index;
      state.regInfo[index].errorStatus = status;
    }
  },
  actions: {
    changeRegInfoValue(store, payLoad) {
      store.commit("mutateRegInfoValue", payLoad);
    },
    changeErrorStatus(store, payLoad) {
      store.commit("mutateErrorStatus", payLoad);
    }
  }
};
