<template>
  <div id="top" class="container">
    <h1>BLE Peripheral Test</h1>
    <label>connected</label> {{ ble_connected }}<br />
    <label>deviceName</label> {{ ble_devicename }}<br />
    <button class="btn btn-default" v-on:click="ble_connect">接続</button>

    <br />
    <br />
    <label>notify value</label>
    <textarea
      type="string"
      rows="5"
      class="form-control"
      v-model="ble_notify_value"
      readonly
    ></textarea>

    <theremin-sound />

    <div class="modal fade" id="progress">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">{{ progress_title }}</h4>
          </div>
          <div class="modal-body">
            <center><progress max="100" /></center>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
const UUID_ANDROID_SERVICE = 'a9d158bb-9007-4fe3-b5d2-d3696a3eb067'
const UUID_ANDROID_NOTIFY = '52dc2803-7e98-4fc2-908a-66161b5959b0'

const ANDROID_WAIT = 200

let bluetoothDevice = null
const characteristics = new Map()

export default {
  name: 'IndexPage',
  el: '#top',
  data() {
    return {
      progress_title: '',

      ble_connected: false,
      ble_devicename: '',
      ble_notify_value: '',
    }
  },
  computed: {
    ...mapState('main', ['frequency']),
  },
  mounted() {
    // window.onmousemove = (e) => {
    //   const pointerX = e.pageX
    //   this.$store.dispatch('main/onChangeFrequency', 440 + pointerX * 0.5)
    // }
  },
  methods: {
    ble_connect() {
      return this.requestDevice(UUID_ANDROID_SERVICE)
        .then((name) => {
          this.ble_devicename = name
          return bluetoothDevice.gatt
            .connect()
            .then((server) => {
              console.log('Execute : getPrimaryService')
              return waitAsync(ANDROID_WAIT).then(() => {
                return server.getPrimaryService(UUID_ANDROID_SERVICE)
              })
            })
            .then((service) => {
              console.log('Execute : getCharacteristic')
              characteristics.clear()
              return Promise.all([
                this.setCharacteristic(service, UUID_ANDROID_NOTIFY),
              ])
            })
            .then((values) => {
              return waitAsync(ANDROID_WAIT).then(() => {
                return this.startNotify(UUID_ANDROID_NOTIFY)
              })
            })
            .then(() => {
              this.ble_connected = true
              console.log('ble_connect done')
              return bluetoothDevice.name
            })
            .catch((error) => {
              alert(error)
            })
            .finally(() => {
              // this.progress_close()
            })
        })
        .catch((error) => {
          alert(error)
        })
    },
    requestDevice(serviceUUID) {
      console.log('Execute : requestDevice')

      return navigator.bluetooth
        .requestDevice({
          filters: [{ services: [serviceUUID] }],
        })
        .then((device) => {
          console.log('requestDevice OK')
          characteristics.clear()
          bluetoothDevice = device
          bluetoothDevice.addEventListener(
            'gattserverdisconnected',
            this.onDisconnect
          )
          return bluetoothDevice.name
        })
    },
    setCharacteristic(service, characteristicUuid) {
      console.log('Execute : setCharacteristic : ' + characteristicUuid)

      return waitAsync(ANDROID_WAIT)
        .then(() => {
          return service.getCharacteristic(characteristicUuid)
        })
        .then((characteristic) => {
          characteristics.set(characteristicUuid, characteristic)
          characteristic.addEventListener(
            'characteristicvaluechanged',
            this.onDataChanged
          )
          return service
        })
    },
    onDisconnect(event) {
      console.log('onDisconnect')
      characteristics.clear()
      this.ble_connected = false
    },
    onDataChanged(event) {
      const characteristic = event.target
      // const packet = uint8arrayToArray(characteristic.value)

      if (characteristic.uuid === UUID_ANDROID_NOTIFY) {
        const str = new TextDecoder().decode(characteristic.value)
        const newFreq = this.calcFrequency(parseFloat(str))
        this.$store.dispatch('main/onChangeFrequency', newFreq)
        this.ble_notify_value = str // bytes2hexs(packet, '')
      }
    },
    startNotify(uuid) {
      if (characteristics.get(uuid) === undefined) {
        console.log('Not Connected')
        return
      }

      console.log('Execute : startNotifications')
      return characteristics.get(uuid).startNotifications()
    },
    stopNotify(uuid) {
      if (characteristics.get(uuid) === undefined) {
        console.log('Not Connected')
        return
      }

      console.log('Execute : stopNotifications')
      return characteristics.get(uuid).stopNotifications()
    },
    calcFrequency(acceleration) {
      if (Number.isNaN(acceleration)) return this.frequency
      if (Math.abs(acceleration) < 1) return this.frequency
      if (this.frequency + acceleration < 0) return this.frequency
      return this.frequency + acceleration
    },
  },
}

function waitAsync(timeout) {
  return new Promise((resolve, reject) => {
    setTimeout(resolve, timeout)
  })
}
</script>
