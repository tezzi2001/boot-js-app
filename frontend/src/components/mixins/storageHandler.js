/**
 * This mixin provides methods that work with vuex storage.
 */
export default {
  methods: {
    /**
     * Gets data of any collection in storage.
     * @param {string} itemName - Name of object in collection.
     * @param {*} fieldName - Field name in object.
     */
    getCollectionData(itemName, fieldName) {
      return this[itemName].map(el => {
        return el[fieldName];
      });
    },
    /**
     * Checks if any collection data equals to any value.
     * @param {*} itemName -  Name of object in collection.
     * @param {*} fieldName - Field name in object.
     * @param {*} checkedValue - Checked value.
     */
    checkCollectionData(itemName, fieldName, checkedValue) {
      return this[itemName].some(el => {
        return el[fieldName] === checkedValue;
      });
    }
  }
};
