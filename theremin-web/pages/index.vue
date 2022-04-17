<template>
  <div id="top" class="container">
    <h1>BLE Peripheral Test</h1>
    <label>connected</label> {{ ble_connected }}<br />
    <label>deviceName</label> {{ ble_devicename }}<br />
    <button class="btn btn-default" v-on:click="ble_connect">接続</button>
    <br /><br />
    <label>write value</label>
    <textarea
      type="string"
      rows="5"
      class="form-control"
      v-model="ble_write_value"
    ></textarea>
    <button class="btn btn-default" v-on:click="ble_write">Write</button>
    <br /><br />
    <button class="btn btn-default" v-on:click="ble_read">Read</button>
    <br />
    <label>read value</label>
    <textarea
      type="string"
      rows="5"
      class="form-control"
      v-model="ble_read_value"
      readonly
    ></textarea>
    <br />
    <label>notify value</label>
    <textarea
      type="string"
      rows="5"
      class="form-control"
      v-model="ble_notify_value"
      readonly
    ></textarea>

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
const UUID_ANDROID_SERVICE = 'a9d158bb-9007-4fe3-b5d2-d3696a3eb067'
const UUID_ANDROID_WRITE = '52dc2801-7e98-4fc2-908a-66161b5959b0'
const UUID_ANDROID_READ = '52dc2802-7e98-4fc2-908a-66161b5959b0'
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
      ble_write_value: '',
      ble_read_value: '',
      ble_notify_value: '',
    }
  },
  created() {
    // proc_load()

    const ary = []
    for (let i = 0; i < 500; i++) ary[i] = i & 0xff
    this.ble_write_value = bytes2hexs(ary, '')
  },
  computed: {},
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
                this.setCharacteristic(service, UUID_ANDROID_WRITE),
                this.setCharacteristic(service, UUID_ANDROID_READ),
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
              this.progress_close()
            })
        })
        .catch((error) => {
          alert(error)
        })
    },
    ble_read() {
      return this.readChar(UUID_ANDROID_READ)
    },
    ble_write() {
      return this.writeChar(
        UUID_ANDROID_WRITE,
        hexs2bytes(this.ble_write_value, '')
      )
    },
    requestDevice(serviceUUID) {
      console.log('Execute : requestDevice')

      return navigator.bluetooth
        .requestDevice({
          filters: [{ services: [serviceUUID] }],
          //      acceptAllDevices: true,
          //      optionalServices: [service_uuid]
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
      console.log('onDataChanged')

      const characteristic = event.target
      const packet = uint8arrayToArray(characteristic.value)
      if (characteristic.uuid === UUID_ANDROID_READ) {
        this.ble_read_value = bytes2hexs(packet, '')
      } else if (characteristic.uuid === UUID_ANDROID_NOTIFY) {
        this.ble_notify_value = bytes2hexs(packet, '')
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
    writeChar(uuid, arrayValue) {
      if (characteristics.get(uuid) === undefined) {
        console.log('Not Connected')
        return
      }

      console.log('Execute : writeValue')
      const data = Uint8Array.from(arrayValue)
      return characteristics.get(uuid).writeValue(data)
    },
    readChar(uuid) {
      if (characteristics.get(uuid) === undefined) {
        console.log('Not Connected')
        return
      }

      console.log('Execute : readValue')
      return characteristics.get(uuid).readValue((dataView) => {
        console.log(dataView)
      })
    },
  },
}
function hexs2bytes(hexs, sep = ' ') {
  hexs = hexs.trim(hexs)
  if (sep === '') {
    hexs = hexs.replace(/ /g, '')
    const array = []
    for (let i = 0; i < hexs.length / 2; i++)
      array[i] = parseInt(hexs.substr(i * 2, 2), 16)
    return array
  } else {
    return hexs.split(sep).map(function (h) {
      return parseInt(h, 16)
    })
  }
}

function bytes2hexs(bytes, sep = ' ') {
  return bytes
    .map(function (b) {
      const s = b.toString(16)
      return b < 0x10 ? '0' + s : s
    })
    .join(sep)
    .toUpperCase()
}

function uint8arrayToArray(array) {
  const result = new Array(array.byteLength)
  for (let i = 0; i < array.byteLength; i++) result[i] = array.getUint8(i)

  return result
}

function waitAsync(timeout) {
  return new Promise((resolve, reject) => {
    setTimeout(resolve, timeout)
  })
}
</script>
