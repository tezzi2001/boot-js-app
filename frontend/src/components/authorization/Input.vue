<template>
  <v-text-field
    :label="field.title"
    :type="field.type"
    @input="onInput($event)"
    class="input-field-width"
    :error="errorStatus"
  >
  </v-text-field>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  props: {
    field: Object,
    index: Number
  },
  computed: {
    ...mapGetters(["errorStatus"])
  },
  methods: {
    onInput(e) {
      const value = e;
      this.$store.dispatch("changeFieldValue", {
        index: this.index,
        value
      });
      this.checkIsFilled();
    },
    checkIsFilled() {
      const status = this.field.value === "" ? false : true;
      this.$store.dispatch("changeIsFilledStatus", {
        index: this.index,
        status
      });
    }
  }
};
</script>
