<template>
  <div class="products-page">
    <!-- Header Section -->
    <div class="products-header">
      <h1>Our Products</h1>
      <p>Discover amazing products at great prices</p>
    </div>

    <!-- Search and Filter Section -->
    <div class="search-filter-section">
      <div class="search-container">
        <div class="search-input-wrapper">
          <input
            v-model="searchinput"
            type="text"
            placeholder="Search products..."
            class="search-input"
            @input="performSearch"
          />
          <button class="search-button" @click="performSearch">Search</button>
        </div>
      </div>

      <div class="filter-section">
        <div class="filter-group">
          <label>Sort by:</label>
          <select v-model="sortBy" @change="sortProducts" class="filter-select">
            <option value="name">Name</option>
            <option value="price-low">Price: Low to High</option>
            <option value="price-high">Price: High to Low</option>
            <option value="category">Category</option>
          </select>
        </div>
      </div>
    </div>

    <!-- Products Grid -->
    <div class="products-container">
      <div v-if="isLoading && !showBackendUnavailable" class="loading-state">
        <div class="loading-spinner"></div>
        <p>Loading products...</p>
      </div>

      <div v-else-if="showBackendUnavailable" class="backend-unavailable">
        <div class="backend-icon">!</div>
        <h3>Store Currently Unavailable</h3>
        <p>The product service is temporarily down.</p>
        <p>Please try again later or contact support if the issue persists.</p>
        <button @click="retryLoad" class="retry-button">Try Again</button>
      </div>

      <div v-else-if="getError" class="error-state">
        <div class="error-icon">!</div>
        <h3>Oops! Something went wrong</h3>
        <p>{{ getError }}</p>
        <button @click="retryLoad" class="retry-button">Try Again</button>
      </div>

      <div v-else-if="filteredProducts.length === 0" class="no-products">
        <div class="no-products-icon">?</div>
        <h3>No products found</h3>
        <p>Try adjusting your search or filters</p>
        <button @click="clearFilters" class="clear-filters-button">
          Clear Filters
        </button>
      </div>

      <div v-else class="products-grid">
        <div
          v-for="product in filteredProducts"
          :key="product.id"
          class="product-card"
          @click="productSelected(product)"
        >
          <div class="product-image-container">
            <img
              :src="product.image"
              :alt="product.productName"
              class="product-image"
            />
            <div class="product-overlay">
              <button
                class="quick-view-button"
                @click.stop="productSelected(product)"
              >
                Quick View
              </button>
            </div>
          </div>

          <div class="product-info">
            <h3 class="product-name">{{ product.productName }}</h3>
            <p class="product-category">{{ product.category }}</p>
            <p class="product-description">{{ product.description }}</p>

            <div class="product-footer">
              <div class="price-section">
                <span class="product-price">${{ product.price }}</span>
                <span
                  class="product-stock"
                  :class="{ 'low-stock': product.productQuantity < 10 }"
                >
                  {{ product.productQuantity }} in stock
                </span>
              </div>

              <div class="product-actions">
                <button
                  class="add-to-cart-button"
                  @click.stop="addToCart(product)"
                >
                  Add to Cart
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Pagination (if needed) -->
    <div v-if="showPagination" class="pagination">
      <button
        class="pagination-button"
        :disabled="currentPage === 1"
        @click="previousPage"
      >
        Previous
      </button>
      <span class="pagination-info">
        Page {{ currentPage }} of {{ totalPages }}
      </span>
      <button
        class="pagination-button"
        :disabled="currentPage === totalPages"
        @click="nextPage"
      >
        Next
      </button>
    </div>
  </div>
</template>
<script>
import { mapActions, mapGetters } from "vuex";

export default {
  name: "DemoProducts",
  computed: {
    ...mapGetters(["getProductsList", "isLoading", "getError"]),
    filteredProducts() {
      let products = [...this.productsList];

      // Apply search filter
      if (this.searchinput.trim()) {
        products = products.filter(
          (product) =>
            product.productName
              .toLowerCase()
              .includes(this.searchinput.toLowerCase().trim()) ||
            product.category
              .toLowerCase()
              .includes(this.searchinput.toLowerCase().trim()) ||
            product.description
              .toLowerCase()
              .includes(this.searchinput.toLowerCase().trim())
        );
      }

      // Apply sorting
      switch (this.sortBy) {
        case "name":
          products.sort((a, b) => a.productName.localeCompare(b.productName));
          break;
        case "price-low":
          products.sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
          break;
        case "price-high":
          products.sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
          break;
        case "category":
          products.sort((a, b) => a.category.localeCompare(b.category));
          break;
      }

      return products;
    },
    totalPages() {
      return Math.ceil(this.filteredProducts.length / this.itemsPerPage);
    },
    showPagination() {
      return this.filteredProducts.length > this.itemsPerPage;
    },
  },
  methods: {
    ...mapActions(["getProductsListApi"]),
    productSelected(product) {
      this.$router.push(`/product/${product.id}`);
    },
    performSearch() {
      // Search is handled by computed property filteredProducts
    },
    sortProducts() {
      // Sorting is handled by computed property filteredProducts
    },
    addToCart(product) {
      // TODO: Implement add to cart functionality
      console.log("Adding to cart:", product);
      // You can emit an event or call a Vuex action here
    },
    retryLoad() {
      this.showBackendUnavailable = false;

      // Set timeout again
      this.loadingTimeout = setTimeout(() => {
        if (this.isLoading) {
          this.showBackendUnavailable = true;
        }
      }, 5000);

      this.$store.dispatch("getProductsListApi", {
        success: () => {
          clearTimeout(this.loadingTimeout);
          this.productsList = this.getProductsList;
          this.showBackendUnavailable = false;
        },
        error: (err) => {
          clearTimeout(this.loadingTimeout);
          console.error("Error loading products:", err);
          this.showBackendUnavailable = true;
        },
      });
    },
    clearFilters() {
      this.searchinput = "";
      this.sortBy = "name";
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
  },
  created() {
    // Set a timeout to detect if backend is not responding
    this.loadingTimeout = setTimeout(() => {
      if (this.isLoading) {
        this.showBackendUnavailable = true;
      }
    }, 5000); // 5 seconds timeout

    this.$store.dispatch("getProductsListApi", {
      success: () => {
        clearTimeout(this.loadingTimeout);
        this.productsList = this.getProductsList;
        this.showBackendUnavailable = false;
      },
      error: (err) => {
        clearTimeout(this.loadingTimeout);
        console.error("Error loading products:", err);
        this.showBackendUnavailable = true;
      },
    });
  },
  beforeDestroy() {
    if (this.loadingTimeout) {
      clearTimeout(this.loadingTimeout);
    }
  },
  data() {
    return {
      searchinput: "",
      productsList: [],
      sortBy: "name",
      currentPage: 1,
      itemsPerPage: 12,
      showBackendUnavailable: false,
      loadingTimeout: null,
    };
  },
};
</script>
<style scoped>
/* Main Container */
.products-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

/* Header Section */
.products-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.products-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: 700;
}

.products-header p {
  font-size: 1.2rem;
  opacity: 0.9;
}

/* Search and Filter Section */
.search-filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  gap: 20px;
  flex-wrap: wrap;
}

.search-container {
  flex: 1;
  min-width: 300px;
}

.search-input-wrapper {
  display: flex;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  border: none;
  outline: none;
  font-size: 16px;
}

.search-input::placeholder {
  color: #999;
}

.search-button {
  padding: 12px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 600;
  transition: opacity 0.3s;
}

.search-button:hover {
  opacity: 0.9;
}

.filter-section {
  display: flex;
  gap: 20px;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-group label {
  font-weight: 600;
  color: #2c3e50;
}

.filter-select {
  padding: 8px 12px;
  border: 2px solid #e1e8ed;
  border-radius: 6px;
  background: white;
  cursor: pointer;
  outline: none;
  transition: border-color 0.3s;
}

.filter-select:focus {
  border-color: #667eea;
}

/* Products Container */
.products-container {
  margin-bottom: 40px;
}

/* Loading State */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-state p {
  color: #666;
  font-size: 18px;
}

/* Backend Unavailable State */
.backend-unavailable {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  background: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 12px;
}

.backend-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.backend-unavailable h3 {
  color: #0369a1;
  margin-bottom: 10px;
}

.backend-unavailable p {
  color: #666;
  margin-bottom: 10px;
}

/* Error State */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
  background: #fdf2f2;
  border: 1px solid #fecaca;
  border-radius: 12px;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.error-state h3 {
  color: #dc2626;
  margin-bottom: 10px;
}

.error-state p {
  color: #666;
  margin-bottom: 20px;
}

.retry-button {
  padding: 12px 24px;
  background: #dc2626;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.3s;
}

.retry-button:hover {
  background: #b91c1c;
}

/* No Products State */
.no-products {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.no-products-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.no-products h3 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.no-products p {
  color: #666;
  margin-bottom: 20px;
}

.clear-filters-button {
  padding: 12px 24px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: background-color 0.3s;
}

.clear-filters-button:hover {
  background: #5a67d8;
}

/* Products Grid */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

/* Product Card */
.product-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image-container {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.quick-view-button {
  padding: 10px 20px;
  background: white;
  color: #2c3e50;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.quick-view-button:hover {
  background: #667eea;
  color: white;
}

/* Product Info */
.product-info {
  padding: 20px;
}

.product-name {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 8px;
  line-height: 1.3;
}

.product-category {
  color: #667eea;
  font-size: 0.9rem;
  font-weight: 500;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.product-description {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price-section {
  display: flex;
  flex-direction: column;
}

.product-price {
  font-size: 1.3rem;
  font-weight: 700;
  color: #2c3e50;
}

.product-stock {
  font-size: 0.8rem;
  color: #666;
}

.product-stock.low-stock {
  color: #dc2626;
  font-weight: 600;
}

.product-actions {
  margin-left: 16px;
}

.add-to-cart-button {
  padding: 8px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.9rem;
}

.add-to-cart-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 40px;
}

.pagination-button {
  padding: 10px 20px;
  background: white;
  color: #667eea;
  border: 2px solid #667eea;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.pagination-button:hover:not(:disabled) {
  background: #667eea;
  color: white;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-info {
  color: #666;
  font-weight: 600;
}

/* Responsive Design */
@media (max-width: 768px) {
  .products-page {
    padding: 10px;
  }

  .products-header h1 {
    font-size: 2rem;
  }

  .search-filter-section {
    flex-direction: column;
    align-items: stretch;
  }

  .search-container {
    min-width: auto;
  }

  .filter-section {
    justify-content: center;
  }

  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }

  .product-footer {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .product-actions {
    margin-left: 0;
  }

  .add-to-cart-button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .products-grid {
    grid-template-columns: 1fr;
  }

  .pagination {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
