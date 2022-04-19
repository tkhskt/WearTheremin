export const state = () => ({
  windowSize: {
    width: 0,
    height: 0,
  },
  isMobile: false,
  soundEnabled: false,
  frequency: 440.0,
})

export const actions = {
  onResize({ commit }, size) {
    commit('setIsMobile', size)
  },
  onChangeHoverTop({ commit }, hover) {
    commit('setHoverTop', hover)
  },
  onChangeFrequency({ commit }, acceleration) {
    commit('setFrequency', acceleration)
  },
}

export const mutations = {
  setWindowSize(state, value) {
    state.windowSize = value
  },
  setIsMobile(state, value) {
    state.isMobile = value.width < 1025
  },
  setFrequency(state, value) {
    state.frequency = value
  },
}
