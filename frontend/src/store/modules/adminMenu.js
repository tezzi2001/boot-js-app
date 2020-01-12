export default {
  state: {
    adminMenuOptions: [
      {
        title: "Добавить новость",
        icon: "mdi-plus-box",
        link: "/create"
      }
    ]
  },
  getters: {
    adminMenuOptions(state) {
      return state.adminMenuOptions;
    }
  }
};
