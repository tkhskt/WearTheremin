export const state = () => ({
  windowSize: {
    width: 0,
    height: 0,
  },
  isMobile: false,
  soundEnabled: false,
  freq: 440,
})

export const actions = {
  onResize({ commit }, size) {
    commit('setIsMobile', size)
  },
  onChangeHoverTop({ commit }, hover) {
    commit('setHoverTop', hover)
  },
  onReceiveAcceleration({ commit }, acceleration) {
    commit('setFreq', acceleration)
  },
}

export const mutations = {
  setWindowSize(state, value) {
    state.windowSize = value
  },
  setIsMobile(state, value) {
    state.isMobile = value.width < 1025
  },
  setFreq(state, value) {
    state.freq = state.freq + value * 10
  },
}
