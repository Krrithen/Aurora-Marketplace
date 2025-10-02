import DemoProducts from "@/components/DemoProducts.vue";
import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";
import Login from "../views/MyLogin.vue";
import Register from "../views/MyRegister.vue";
import ProductDetailView from "../views/ProductDetailView.vue";
import ProductInfoSection from "../views/ProductInfoSection.vue";
import ProductReviewsSection from "../views/ProductReviewsSection.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/cart",
    name: "cart",
    component: () => import("../views/CartView.vue"),
  },
  {
    path: "/account",
    name: "account",
    component: () => import("../views/AccountView.vue"),
  },
  {
    path: "/product/:productId",
    name: "productDetail",
    component: ProductDetailView,
    children: [
      {
        path: "info",
        name: "productInfo",
        component: ProductInfoSection,
      },
      {
        path: "reviews",
        name: "productReviews",
        component: ProductReviewsSection,
      },
    ],
  },
  {
    path: "/products",
    name: "products",
    component: DemoProducts,
  },
  {
    path: "/login",
    name: "login",
    component: Login,
  },
  {
    path: "/register",
    name: "register",
    component: Register,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
