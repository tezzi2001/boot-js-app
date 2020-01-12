export default {
  methods: {
    /**
     * Checks if any error should append.
     * @param {object} field - Input vuelidate object (f.e - $v.form.login)
     * @param {array} errors - Array of field errors.
     * @param {string} errorMessage - Error message that appends.
     * @param {string} errorType - Type of error (f.e - isUnique)
     */
    checkIfErrorShouldAppend({ field, errors, errorMessage, errorType }) {
      !field[errorType] && field.$error && errors.push(errorMessage);
      return errors;
    }
  }
};
