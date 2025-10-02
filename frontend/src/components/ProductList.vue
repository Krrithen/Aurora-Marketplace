<template>
  <div>
    <p
      v-for="product in productsList"
      :key="product.id"
      :class="['singleProduct']"
      @click="selectProduct(product)"
    >
      <img :src="product.image" alt="Not Found" />
      {{ product.productId }}, {{ product.productName }},
      {{ product.category }}, {{ product.description }}, {{ product.price }},
      {{ product.productQuantity }}
      <slot></slot>
    </p>
  </div>
</template>
<script>
export default {
  name: "ProductList",
  emits: ["product-selected"],
  props: {
    productsList: {
      type: Array,
      default: () => [],
      required: true,
    },
  },
  data() {
    return {};
  },
  methods: {
    selectProduct(product) {
      this.$emit("product-selected", product);
    },
  },
};
</script>
<style scoped>
img {
  width: 20vw;
  height: 20vh;
}
.singleProduct {
  border: 2px solid black;
  width: 20vw;
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: 10px;
}
img {
  margin: 10px;
}
</style>
