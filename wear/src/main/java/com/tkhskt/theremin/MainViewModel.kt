package com.tkhskt.theremin

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer

class MainViewModel :
    ViewModel(),
    CapabilityClient.OnCapabilityChangedListener,
    DataClient.OnDataChangedListener {

    override fun onCapabilityChanged(info: CapabilityInfo) {

    }

    override fun onDataChanged(data: DataEventBuffer) {

    }
}
