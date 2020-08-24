package com.abdelnabi.messagevisualizer.view_model;

import android.util.Log
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abdelnabi.messagevisualizer.data.ApiClient
import com.abdelnabi.messagevisualizer.model.MessageInfoModel
import com.abdelnabi.messagevisualizer.model.MessageModel
import com.abdelnabi.messagevisualizer.uitl.getMessageInfoFromString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageViewModel : ViewModel() {

    val mutableLiveData =
        MutableLiveData<MutableList<MessageInfoModel>>()
    val onErrorThrowableMutableLiveData =
        MutableLiveData<String>()

    fun getMessage() {
        val itemCall = ApiClient.instance!!.getMessage()
        itemCall.enqueue(object : Callback<MessageModel> {
            override fun onResponse(
                call: Call<MessageModel>,
                response: Response<MessageModel>
            ) {
                if(response.code()/100 == 2){
                    val body = response.body()!!
                    mutableLiveData.value = getMessageInfo(body.feed!!.entry!!)
                }else{
                    onErrorThrowableMutableLiveData.postValue("We faced a problem, please try in another time")
                }

            }

            override fun onFailure(
                call: Call<MessageModel>,
                t: Throwable
            ) {
                Log.e(
                    Constraints.TAG,
                    "onFailure: ",
                    t
                )
                onErrorThrowableMutableLiveData.value = "We faced a problem, please try in another time"
            }
        })
    }

    fun getMessageInfo(dataList : List<MessageModel.EntryEntity>): MutableList<MessageInfoModel>{
        val messageList = mutableListOf<MessageInfoModel>()
        for(item in dataList){
            messageList.add(getMessageInfoFromString(item.content!!.messageBody!!)!!)
        }
        return messageList
    }


}