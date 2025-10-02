export default {
  state: {
    firstVariable: "default firstVariable",
  },
  getters: {
    getFirstVariable: (state) => state.firstVariable,
  },
  mutations: {
    setFirstVariable: (state, value) => (state.firstVariable = value),
  },
  actions: {
    setFirstVariable: ({ commit }, { firstData }) => {
      commit("setFirstVariable", firstData);
    },
  },
};
