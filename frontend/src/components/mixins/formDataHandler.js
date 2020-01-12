/**
 * This mixin provides methods that works with FormData.
 */

export default {
  methods: {
    /**
     * Creates and fill form data object.
     * @param {object} paramsObj - Object of form data parameters.
     */
    createAndFillFormData(paramsObj) {
      const formData = new FormData();
      for (const param in paramsObj) {
        formData.append(param, paramsObj[param]);
      }
      return formData;
    }
  }
};
