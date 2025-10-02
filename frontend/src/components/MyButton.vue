<template>
  <button @click="myButtonHandler">{{ buttonText }}</button>
</template>

<script>
export default {
  name: "MyButton",
  data() {
    return {};
  },
  emits: ["button-clicked"],
  props: {
    buttonText: {
      type: String,
      default: "Click Me",
      required: false,
    },
  },
  methods: {
    myButtonHandler() {
      this.$emit("button-clicked");
    },
  },
};
</script>

<style scoped>
button {
  width: 28vw;
  height: 5vh;
}
</style>
