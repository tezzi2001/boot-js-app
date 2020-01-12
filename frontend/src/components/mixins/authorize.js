/**
 * This mixin provides methods that authorizes user.
 */

export default {
  methods: {
    /**
     * Saves user data in local storage.
     * @param {object} userDataObj - object of user data.
     */
    setLocalStorageUserData(userDataObj) {
      for (let property in userDataObj) {
        localStorage[property] = userDataObj[property];
      }
    },
    /**
     * Saves user information in vuex storage.
     * @param {object} userDataObj - object of user data.
     */
    setUserParams(userDataObj) {
      const { login, name, role } = userDataObj;
      this.$store.dispatch("changeUserParams", {
        login,
        name,
        role
      });
    },
    /**
     * Gets user data from local storage and saves it in vuex storage.
     */
    setUserParamsFromLocalStorage() {
      if (localStorage.login) {
        const login = localStorage.login;
        const name = localStorage.name;
        const role = localStorage.role;
        this.setUserParams({
          login,
          name,
          role
        });
      }
    },
    /**
     * Moves user to the main page after with authorization button was clicked.
     */
    sendDataButtonClicked() {
      this.$router.push("/", () => {
        this.setUserParamsFromLocalStorage();
      });
    },
    /**
     * Authorizes user.
     * @param {string} login - User's login
     * @param {string} name - User's name
     * @param {string} role - User's role
     */
    authorize(login, name, role = "READER") {
      this.setLocalStorageUserData({
        login,
        name,
        role
      });
      this.sendDataButtonClicked();
    }
  }
};
