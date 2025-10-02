<template>
  <div class="home">
    <div class="hero-section">
      <h1>Welcome to Aurora Marketplace</h1>
      <p>Discover amazing products at great prices</p>
      <router-link to="/products" class="cta-button">Shop Now</router-link>
    </div>

    <div class="features">
      <h2>Why Choose Aurora?</h2>
      <div class="feature-grid">
        <div class="feature">
          <h3>Fast Delivery</h3>
          <p>Quick and reliable shipping to your doorstep</p>
        </div>
        <div class="feature">
          <h3>Secure Payment</h3>
          <p>Safe and encrypted payment processing</p>
        </div>
        <div class="feature">
          <h3>Easy Returns</h3>
          <p>Hassle-free return policy</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeView",
};
</script>

<style scoped>
.home {
  padding: 20px;
}

.hero-section {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 10px;
  margin-bottom: 40px;
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  font-weight: bold;
}

.hero-section p {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

.cta-button {
  display: inline-block;
  padding: 15px 30px;
  background-color: #42b983;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  font-weight: bold;
  font-size: 1.1rem;
  transition: background-color 0.3s;
}

.cta-button:hover {
  background-color: #369870;
}

.features {
  text-align: center;
  padding: 40px 20px;
}

.features h2 {
  font-size: 2.5rem;
  margin-bottom: 40px;
  color: #2c3e50;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1000px;
  margin: 0 auto;
}

.feature {
  padding: 30px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.feature:hover {
  transform: translateY(-5px);
}

.feature h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #42b983;
}

.feature p {
  color: #666;
  line-height: 1.6;
}
</style>
