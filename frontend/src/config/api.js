// API Configuration
const API_CONFIG = {
  BASE_URL: process.env.VUE_APP_API_BASE_URL || "http://localhost:8081",
  ENDPOINTS: {
    PRODUCTS: "/products/GetAllProducts",
  },
};

export default API_CONFIG;
