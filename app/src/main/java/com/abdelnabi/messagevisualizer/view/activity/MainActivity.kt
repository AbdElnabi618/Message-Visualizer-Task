package com.abdelnabi.messagevisualizer.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdelnabi.messagevisualizer.R
import com.abdelnabi.messagevisualizer.model.MessageInfoModel
import com.abdelnabi.messagevisualizer.uitl.DialogUtil
import com.abdelnabi.messagevisualizer.view.adapter.MessageAdapter
import com.abdelnabi.messagevisualizer.view_model.MessageViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, MessageAdapter.OnItemClick {

    private lateinit var mMap: GoogleMap
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var viewModel : MessageViewModel
    private lateinit var dialog : DialogUtil
    private lateinit var messageList: List<MessageInfoModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        observeData()
    }

    private fun initView() {
        dialog = DialogUtil(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        dialog.showDialog()
        viewModel.getMessage()

        messageAdapter = MessageAdapter(this, this)
        rv_message_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_message_list.adapter = messageAdapter

    }



    private fun observeData() {
        viewModel.mutableLiveData.observe(this, {
            dialog.hideDialog()
            messageList = it
            addAllMarksToMap()
            // add item to show all item
            val messageInfoModel = MessageInfoModel()
            messageInfoModel.message = "Show All Message"
            messageInfoModel.sentiment = ""
            messageInfoModel.id = "abdelnabi618"
            it.add(0, messageInfoModel)
            messageAdapter.list = it!!
        })

        viewModel.onErrorThrowableMutableLiveData.observe(this, {
            dialog.hideDialog()
            Toast.makeText(this, it!!, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addAllMarksToMap() {
        for (message in messageList)
            addMarkToMap(message)
    }

    private fun addMarkToMap(message : MessageInfoModel) {
        val markerOptions = MarkerOptions()
        markerOptions.title(message.message)
        markerOptions.position(message.latLng)
        val defaultMarker = when (message.sentiment) {
            "Negative" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            "Neutral" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            "Positive" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            else -> null
        }
        markerOptions.icon(defaultMarker)
        mMap.addMarker(markerOptions)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // move map to center
        mMap.cameraPosition.target
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun itemSelected(item: MessageInfoModel) {
        mMap.clear()
        if(item.id == "abdelnabi618"){
           addAllMarksToMap()
        }else{
            addMarkToMap(item)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(item.latLng))
        }
    }
}