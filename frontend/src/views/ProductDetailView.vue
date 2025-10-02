<template>
  <div class="ProductDetailView">
    <h1>Product Details</h1>
    <nav>
      <router-link :to="`/product/${productId}/info`">Product Info</router-link>
      |
      <router-link :to="`/product/${productId}/reviews`">Reviews</router-link>
    </nav>
    <router-view />
  </div>
</template>
<script>
export default {
  data() {
    return {
      productId: null,
    };
  },
  mounted() {
    // Get product details based on route params
    this.productId = this.$route.params.productId;
    // TODO: Fetch product details using productId
  },
};
</script>
