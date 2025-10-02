<template>
  <div class="product-display">
    <h1>Product Display</h1>
    <div class="display-controls">
      <button @click="toggleDisplay">Toggle Display</button>
      <button @click="toggleVisibility">Toggle Visibility</button>
    </div>

    <div class="product-list">
      <h2>Available Products</h2>
      <ul class="products">
        <li
          v-for="(product, index) in productsList"
          :key="product.id"
          :class="['product-item', index % 2 === 0 ? 'even-row' : 'odd-row']"
        >
          {{ product.name }} - ${{ product.price }}
        </li>
      </ul>
      <button @click="refreshProducts">Refresh Products</button>
    </div>
  </div>
</template>
<script>
export default {
  name: "ProductDisplay",
  data() {
    return {
      showDisplay: true,
      showVisibility: true,
      productsList: [
        {
          name: "Wireless Headphones",
          price: 120,
          id: 1,
        },
        {
          name: "Smart Watch",
          price: 140,
          id: 2,
        },
        {
          name: "Bluetooth Speaker",
          price: 160,
          id: 3,
        },
        {
          name: "Phone Case",
          price: 25,
          id: 4,
        },
        {
          name: "USB Cable",
          price: 15,
          id: 5,
        },
        {
          name: "Power Bank",
          price: 45,
          id: 6,
        },
        {
          name: "Screen Protector",
          price: 12,
          id: 7,
        },
        {
          name: "Wireless Charger",
          price: 35,
          id: 8,
        },
        {
          name: "Laptop Stand",
          price: 55,
          id: 9,
        },
      ],
    };
  },
  methods: {
    toggleDisplay() {
      this.showDisplay = !this.showDisplay;
    },
    toggleVisibility() {
      this.showVisibility = !this.showVisibility;
    },
    refreshProducts() {
      // TODO: Implement product refresh logic
      console.log("Refreshing products...");
    },
  },
};
</script>
<style scoped>
.product-display {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 10px;
}

.display-controls {
  margin-bottom: 20px;
}

.display-controls button {
  margin-right: 10px;
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.display-controls button:hover {
  background-color: #369870;
}

.product-list {
  margin-top: 20px;
}

.products {
  list-style: none;
  padding: 0;
}

.product-item {
  padding: 10px;
  margin: 5px 0;
  border-radius: 4px;
}

.even-row {
  background-color: #f0f8ff;
}

.odd-row {
  background-color: #f5f5f5;
}

.product-item:hover {
  background-color: #e8f4fd;
}

button {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #369870;
}
</style>
