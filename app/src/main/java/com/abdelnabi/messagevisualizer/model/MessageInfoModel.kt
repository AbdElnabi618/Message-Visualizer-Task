package com.abdelnabi.messagevisualizer.model

import com.google.android.gms.maps.model.LatLng

class MessageInfoModel {
    var id: String? = null
    var message: String? = null
    var sentiment: String? = null
    var latLng: LatLng = LatLng(0.0,0.0)
}