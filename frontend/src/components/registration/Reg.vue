<template>
  <v-form
    class="d-flex flex-column justify-center align-center full-height full-width"
  >
    <v-container
      class="d-flex flex-column justify-center align-center"
      v-if="!isAuthorized"
    >
      <v-text-field
        v-model="form.login"
        id="login"
        label="Логин"
        class="input-field-width"
        :error-messages="loginErrors"
        :success="!$v.form.login.$invalid"
        @input="delayTouch($v.form.login)"
      ></v-text-field>

      <v-text-field
        v-model="form.name"
        id="name"
        label="Имя"
        class="input-field-width"
        :error-messages="nameErrors"
        :success="!$v.form.name.$invalid"
        @input="delayTouch($v.form.name)"
      ></v-text-field>

      <v-text-field
        v-model="form.email"
        id="email"
        label="Почта"
        class="input-field-width"
        :error-messages="emailErrors"
        :success="!$v.form.email.$invalid"
        @input="delayTouch($v.form.email)"
      ></v-text-field>

      <v-text-field
        v-model="form.password"
        id="password"
        label="Пароль"
        type="password"
        class="input-field-width"
        :error-messages="passwordErrors"
        :success="!$v.form.password.$invalid"
        @input="delayTouch($v.form.password)"
      ></v-text-field>

      <v-text-field
        v-model="form.confirmPass"
        id="confirm-pass"
        label="Подтвердите пароль"
        type="password"
        class="input-field-width"
        :error-messages="confirmPassErrors"
        :success="!$v.form.confirmPass.$invalid"
        @input="delayTouch($v.form.confirmPass)"
      >
      </v-text-field>

      <v-btn
        color="indigo"
        :disabled="isDisabled"
        :dark="!isDisabled"
        @click="sendRegData()"
        :loading="isLoading"
        class="mt-2"
        >Зарегистрироваться</v-btn
      >
    </v-container>
    <div class="message d-flex flex-column align-center" v-if="isAuthorized">
      <p>Вы уже вошли в систему</p>
      <router-link to="/">Вернуться на главную</router-link>
    </div>
  </v-form>
</template>

<script>
import { required, sameAs, email } from "vuelidate/lib/validators";
import { validationMixin } from "vuelidate";
import validators from "../mixins/validators.js";
import { mapGetters } from "vuex";
import axios from "axios";
import authorize from "../mixins/authorize.js";
import storageHandler from "../mixins/storageHandler.js";
import formDataHandler from "../mixins/formDataHandler.js";
import {
  isLoginUnique,
  isLoginValid,
  isEmailUnique,
  isPassValid
} from "./regValidators.js";

const touchMap = new WeakMap();

export default {
  data() {
    return {
      isLoading: false,
      isSubmited: false,
      form: {
        login: null,
        name: null,
        email: null,
        password: null,
        confirmPass: null
      }
    };
  },
  validations: {
    form: {
      login: { required, isUnique: isLoginUnique, isValid: isLoginValid },
      name: { required },
      email: { required, isUnique: isEmailUnique, isValid: email },
      password: { required, isValid: isPassValid },
      confirmPass: { required, sameAsPassword: sameAs("password") }
    }
  },
  computed: {
    ...mapGetters(["regInfo", "isAuthorized"]),
    isDisabled() {
      return this.$v.$invalid || this.isLoading;
    },
    loginErrors() {
      let errors = [];
      if (!this.$v.form.login.$dirty) return errors;
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.login,
        errors,
        errorMessage: "Это поле обязательно!",
        errorType: "required"
      });
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.login,
        errors,
        errorMessage: "Такой логин уже есть",
        errorType: "isUnique"
      });
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.login,
        errors,
        errorMessage: "Логин невалиден",
        errorType: "isValid"
      });
      return errors;
    },
    nameErrors() {
      let errors = [];
      if (!this.$v.form.name.$dirty) return errors;
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.name,
        errors,
        errorMessage: "Это поле обязательно!",
        errorType: "required"
      });
      return errors;
    },
    emailErrors() {
      let errors = [];
      if (!this.$v.form.email.$dirty) return errors;
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.email,
        errors,
        errorMessage: "Это поле обязательно!",
        errorType: "required"
      });
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.email,
        errors,
        errorMessage: "Такой емейл уже есть!",
        errorType: "isUnique"
      });
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.email,
        errors,
        errorMessage: "Неверный формат почты",
        errorType: "isValid"
      });
      return errors;
    },
    passwordErrors() {
      let errors = [];
      if (!this.$v.form.password.$dirty) return errors;
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.password,
        errors,
        errorMessage: "Это поле обязательно!",
        errorType: "required"
      });
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.password,
        errors,
        errorMessage:
          "Пароль должен иметь минимум 8 символов, 1 заглавную букву и состоять из латинских букв и знаков",
        errorType: "isValid"
      });
      return errors;
    },
    confirmPassErrors() {
      let errors = [];
      if (!this.$v.form.confirmPass.$dirty) return errors;
      errors = this.checkIfErrorShouldAppend({
        field: this.$v.form.confirmPass,
        errors,
        errorMessage: "Пароли не совпадают!",
        errorType: "sameAsPassword"
      });
      return errors;
    }
  },
  methods: {
    async sendRegData() {
      this.$v.form.$touch();
      if (this.$v.form.$invalid) return true;
      const { login, name, email, password } = this.form;
      const formData = this.createAndFillFormData({
        login,
        name,
        email,
        password
      });
      this.buttonClicked(true);
      await axios.post("http://localhost:8080/register", formData);
      this.buttonClicked(false);
      this.authorize(login, name);
    },
    buttonClicked(status) {
      this.isLoading = status;
      this.isSubmited = status;
    },
    delayTouch($v) {
      $v.$reset();
      if (touchMap.has($v)) {
        clearTimeout(touchMap.get($v));
      }
      touchMap.set($v, setTimeout($v.$touch, 350));
    }
  },
  mixins: [
    authorize,
    storageHandler,
    formDataHandler,
    validationMixin,
    validators
  ]
};
</script>
