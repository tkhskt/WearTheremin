export const state = () => ({
  windowSize: {
    width: 0,
    height: 0,
  },
  isMobile: false,
  soundEnabled: false,
  frequency: 440.0,
  volume: 0.5,
})

export const actions = {
  onResize({ commit }, size) {
    commit('setIsMobile', size)
  },
  onChangeHoverTop({ commit }, hover) {
    commit('setHoverTop', hover)
  },
  onChangeFrequency({ commit }, value) {
    commit('setFrequency', value)
  },
  onChangeVolume({ commit }, value) {
    commit('setVolume', value)
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
  setVolume(state, value) {
    state.volume = value
  },
}
