package com.abdelnabi.messagevisualizer.view.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_message.view.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, MessageAdapter.OnItemClick {

    private lateinit var mMap: GoogleMap
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var viewModel : MessageViewModel
    private lateinit var dialog : DialogUtil

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
            messageAdapter.list = it!!
            addMarksToMap(it)
        })

        viewModel.onErrorThrowableMutableLiveData.observe(this, {
            dialog.hideDialog()
            Toast.makeText(this, it!!, Toast.LENGTH_SHORT).show()
        })
    }

    private fun addMarksToMap(messageList :List<MessageInfoModel>){
        for(message in messageList){
            val markerOptions = MarkerOptions()
            markerOptions.title(message.message)
            markerOptions.position(message.latLng)
            val defaultMarker = when(message.sentiment){
                "Negative" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                "Neutral"-> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                "Positive"-> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                else -> null
            }
            markerOptions.icon(defaultMarker)
            mMap.addMarker(markerOptions)
        }
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

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun itemSelected(item: MessageInfoModel) {
        TODO("Not yet implemented")
    }
}