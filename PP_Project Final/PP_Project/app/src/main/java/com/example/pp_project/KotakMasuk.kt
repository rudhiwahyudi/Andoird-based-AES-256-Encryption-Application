package com.example.pp_project

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.databinding.ActivityKotakMasukBinding
import com.example.pp_project.user.adapter.adapterKotakMasuk
import com.example.pp_project.user.data.dataKotakMasuk
import org.json.JSONException
import org.json.JSONObject

class KotakMasuk : AppCompatActivity() {
    private lateinit var binding : ActivityKotakMasukBinding
    private lateinit var dataList : ArrayList<dataKotakMasuk>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKotakMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataList = ArrayList<dataKotakMasuk>()
        getDataKotakMasuk()
        binding.backActivity.setOnClickListener {
            onBackPressed()
        }
    }
    private fun getDataKotakMasuk(){
        val url = urlAPI.endPoint.url
        val sharedPreference =  getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id","")
        AndroidNetworking.get("$url/other/kotakmasuk/$id")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        if (response.getString("success").equals("true")) {
                            val getDataArray = response.getJSONArray("newResult")
                            for (i in 0 until getDataArray.length()){
                                val item = getDataArray.getJSONObject(i)
                                dataList.add(
                                    dataKotakMasuk(
                                        item.getString("nama_pengirim"),
                                        item.getString("nama_file"),
                                        item.getString("created"),
                                    )
                                )
                                populateData()
                            }
                        }else{
                            Toast.makeText(this@KotakMasuk, response.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@KotakMasuk, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: ANError) {
                    Toast.makeText(this@KotakMasuk, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun populateData(){
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.recyclerView.layoutManager = layoutManager
        val adp = adapterKotakMasuk(this, dataList)
        binding.recyclerView.adapter = adp
    }

}