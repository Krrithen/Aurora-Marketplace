<template>
  <div class="cart-view">
    <h1>Shopping Cart</h1>

    <!-- Show sign-in message when user is not logged in -->
    <div v-if="!isLoggedIn" class="signin-message">
      <div class="signin-card">
        <h2>Sign In to View Cart</h2>
        <p>Please sign in to access your shopping cart and saved items.</p>
        <div class="signin-actions">
          <router-link to="/login" class="btn btn-primary">Sign In</router-link>
          <router-link to="/register" class="btn btn-outline">
            Create Account
          </router-link>
        </div>
      </div>
    </div>

    <!-- Show cart content when user is logged in -->
    <div v-else class="cart-content">
      <p>Your cart items will be displayed here.</p>
      <p>This will integrate with the cart-service backend.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "CartView",
  data() {
    return {
      isLoggedIn: false, // TODO: Connect to authentication state
    };
  },
};
</script>

<style scoped>
.cart-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.signin-message {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.signin-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 40px;
  text-align: center;
  max-width: 400px;
  width: 100%;
}

.signin-card h2 {
  color: #2c3e50;
  margin-bottom: 15px;
  font-size: 1.8rem;
}

.signin-card p {
  color: #666;
  margin-bottom: 30px;
  font-size: 1.1rem;
}

.signin-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 12px 24px;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-outline {
  color: #667eea;
  border-color: #667eea;
  background: transparent;
}

.btn-outline:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.cart-content {
  margin-top: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}
</style>
