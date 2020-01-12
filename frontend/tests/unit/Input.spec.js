import Vue from "vue";
import Input from "../../src/components/registration/Input";

describe("Input", () => {
  describe("checkPassword method test", () => {
    it(`Returns "true" when passwords are the same`, () => {
      expect(Input.methods.checkPassword("Palma_2001", "Palma_2001")).toBe(
        true
      );
    });
    it(`Returns "false" when passwords are not the same`, () => {
      expect(Input.methods.checkPassword("Palma_2001", "Palma_2000")).toBe(
        false
      );
    });
  });

  describe("changeValidateResult method test", () => {
    it("Changes Vue data object isSuccess property to true and errorMessage to ''", () => {
      const vm = new Vue(Input);
      vm.changeValidateResult(true, "");
      expect(vm.validateResult.isSuccess).toBe(true);
      expect(vm.validateResult.errorMessage).toBe("");
    });

    it("Changes Vue data object isSuccess property to false and errorMessage to 'error'", () => {
      const vm = new Vue(Input);
      vm.changeValidateResult(false, "error");
      expect(vm.validateResult.isSuccess).toBe(false);
      expect(vm.validateResult.errorMessage).toBe("error");
    });
  });
});
