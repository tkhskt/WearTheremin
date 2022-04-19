<template>
  <div>
    <button class="sound-button-enable" @click="enable_sound">
      サウンド有効化
    </button>
    <button class="sound-button-disable" @click="disable_sound">
      サウンド無効化
    </button>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      enabled: false,
    }
  },
  computed: {
    ...mapState('main', ['freq']),
  },
  watch: {
    freq(newValue) {
      console.log('freqqq!' + newValue)
      if (!this.oscillator) return
      console.log(newValue)
      this.oscillator.frequency.value = newValue
    },
  },
  mounted() {
    const AudioContext = window.AudioContext || window.webkitAudioContext
    this.audioCtx = new AudioContext()
    // create Oscillator and gain node
    this.gainNode = this.audioCtx.createGain()
    this.gainNode.connect(this.audioCtx.destination)
  },
  methods: {
    enable_sound() {
      this.enabled = true
      if (this.oscillator) this.oscillator.stop()
      // connect oscillator to gain node to speakers
      this.oscillator = this.audioCtx.createOscillator()
      this.oscillator.connect(this.gainNode)
      this.oscillator.detune.value = 100
      const initialVol = 0.1

      this.oscillator.start(0)

      this.oscillator.onended = function () {
        console.log('Your tone has now stopped playing!')
      }

      this.gainNode.gain.value = initialVol
    },
    disable_sound() {
      this.enabled = false
      this.oscillator.stop()
    },
  },
}
</script>
