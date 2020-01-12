import Vue from "vue";
import "./plugins/axios";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import vuelidate from "./plugins/vuelidate";

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  vuelidate,
  render: h => h(App)
}).$mount("#app");
