<template>
  <div class="user-profile">
    <h1>User Profile</h1>
    <div class="profile-info">
      <p>{{ userName }}</p>
      <p>{{ userEmail }}</p>
      <p>{{ statusMessage }}</p>
      <p>{{ lastUpdated }}</p>
      <button @click="toggleStatus">Toggle Status</button>
      <p v-if="showStatus">{{ statusText }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserProfile",
  data() {
    return {
      showStatus: true,
      userName: "John Doe",
      userEmail: "john.doe@example.com",
      statusMessage: "Welcome to your profile",
      statusText: "Profile is active",
    };
  },
  computed: {
    lastUpdated() {
      return `Last updated: ${new Date().toLocaleDateString()}`;
    },
  },
  methods: {
    toggleStatus() {
      this.showStatus = !this.showStatus;
    },
  },
};
</script>
<style scoped>
.user-profile {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 10px;
}

.profile-info {
  margin-top: 15px;
}

button {
  height: 40px;
  width: 200px;
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
