package com.abdelnabi.messagevisualizer.model

import android.content.Context
import android.location.Geocoder
import android.util.Log.e
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

private const val TAG = "String Helper Tag"
/**
 * this method receive content from api and convert it to MessageInfoModel object
 * content : example -> "messageid: 4, message: Lovely shisa in Cairo, Egypt, sentiment: Positive"
 */
fun getMessageInfo(content: String, context: Context): MessageInfoModel? {
    val info = MessageInfoModel()
    val dataList = content.split(": ").toTypedArray()
    info.id = getMessageIdFromString(dataList[1])
    info.message = getMessageFromString(dataList[2])
    info.sentiment = dataList[3]
    val country = getCountry(info.message!!)
    val latlngList = mutableListOf<LatLng>()
    for(item in country!!){
        // please note this function return one result only
        latlngList.add(getCountryLocation(item, context)[0])
    }
    // we need first address if we faced more than one "other address for city or country but same place"
    info.latLng = latlngList[0]
    return info
}

/**
 * this method receive id as first part of word get from getMessageInfo function and give the id with itself
 * example :- 4, message
 * return 4
 */
private fun getMessageIdFromString(text: String): String? {
    val wordList = text.split(",").toTypedArray()
    return wordList[0]
}

/**
 * this method used to get message from content string from api
 * input example from getMessageInfo :- "Lovely shisa in Cairo, Egypt, sentiment"
 *          we need to remove last part after ',' to get message
 * return Lovely shisa in Cairo, Egypt,
 */
private fun getMessageFromString(text: String): String? {
    val wordList = text.split(" ").toTypedArray()
    var result = ""
    for (i in 0 until wordList.size - 1) {
        result += wordList[i] + " "
    }
    return result
}

/**
 * this function used to get country and cities name from message depend on capitalization
 * return list of country and cities name
 */
private fun getCountry(message: String): List<String>? {
    val country: MutableList<String> = ArrayList()
    val words = message.split(" ".toRegex()).toTypedArray()
    for (word in words) {
        if (Character.isUpperCase(word[0])) {
            country.add(word)
        }
    }
    return country
}

/**
 * this function used to get country latitude and longitude from location name
 * return list of Latlng with on item of location
 */
private fun getCountryLocation(location :String, context: Context) : MutableList<LatLng>{
    val ll: MutableList<LatLng> = ArrayList(1)
    if (Geocoder.isPresent()) {
        try {
            val addressesList = Geocoder(context).getFromLocationName(location, 1)
            for (address in addressesList) {
                if (address.hasLatitude() && address.hasLongitude()) {
                    ll.add(LatLng(address.latitude, address.longitude))
                    e(TAG, "getCountryLocation: ${address.latitude}, ${address.longitude}")
                }
            }
        } catch (e: IOException) {
            e(TAG, "getCountryLocation: ", e)
        }
    }
    return ll
}