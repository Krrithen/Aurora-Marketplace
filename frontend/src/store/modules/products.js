import API_CONFIG from "@/config/api";
import axios from "axios";

// Create axios instance with base configuration
const apiClient = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  state: {
    productsList: [],
    loading: false,
    error: null,
  },
  getters: {
    getProductsList: (state) => state.productsList,
    isLoading: (state) => state.loading,
    getError: (state) => state.error,
  },
  mutations: {
    setProductsList: (state, value) => (state.productsList = value),
    setLoading: (state, value) => (state.loading = value),
    setError: (state, value) => (state.error = value),
  },
  actions: {
    getProductsListApi: ({ commit }, { success, error }) => {
      commit("setLoading", true);
      commit("setError", null);

      apiClient
        .get(API_CONFIG.ENDPOINTS.PRODUCTS)
        .then((response) => {
          commit("setProductsList", response.data);
          commit("setLoading", false);
          success && success(response.data);
        })
        .catch((err) => {
          const errorMessage =
            err.response?.data?.message || err.message || "An error occurred";
          commit("setError", errorMessage);
          commit("setLoading", false);
          error && error(err);
        });
    },
  },
};
