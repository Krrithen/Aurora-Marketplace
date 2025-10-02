<template>
  <div>
    <input type="text" :value="value" @input="myInputFieldHandler" />
  </div>
</template>
<script>
export default {
  props: ["value"],
  name: "MyInputField",
  data() {
    return {};
  },
  methods: {
    myInputFieldHandler(event) {
      //console.log("from myInput File" + " " + event.target.value);
      this.$emit("input", event.target.value);
    },
  },
};
</script>
<style scoped>
input {
  width: 70vw;
  height: 5vh;
}
</style>
