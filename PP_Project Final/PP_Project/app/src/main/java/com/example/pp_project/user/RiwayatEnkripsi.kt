package com.example.pp_project.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.R
import com.example.pp_project.databinding.ActivityRiwayatEnkripsiBinding
import com.example.pp_project.urlAPI
import com.example.pp_project.user.adapter.adapterRiwayatEnkripsi
import com.example.pp_project.user.data.dataRiwayatEnkripsi
import org.json.JSONException
import org.json.JSONObject

class RiwayatEnkripsi : AppCompatActivity() {
    private lateinit var binding : ActivityRiwayatEnkripsiBinding
    private lateinit var dataList : ArrayList<dataRiwayatEnkripsi>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatEnkripsiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataList = ArrayList<dataRiwayatEnkripsi>()


        val checkType = intent.getStringExtra("type")
        if (checkType.equals(null)){
            getDataRiwayat()
        }else{

            getDataRiwayatExtention(checkType.toString())
        }
        binding.backActivity.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDataRiwayat(){
        val url = urlAPI.endPoint.url
        val sharedPreference =  getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id","")
        AndroidNetworking.get("$url/other/riwayatprivate/$id")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("ini respon", response.toString())
                    try {
                        if (response.getString("success").equals("true")) {
                            binding.recyclerView.visibility = View.VISIBLE
                            val getDataArray = response.getJSONArray("newResult")
                            for (i in 0 until getDataArray.length()){
                                val item = getDataArray.getJSONObject(i)
                                dataList.add(
                                    dataRiwayatEnkripsi(
                                        item.getString("id_file"),
                                        item.getString("nama_file"),
                                        item.getString("created"),
                                        item.getString("extension"),
                                        item.getString("is_send")
                                    )
                                )
                                populate()
                            }
                        }else{
                            Toast.makeText(this@RiwayatEnkripsi, response.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@RiwayatEnkripsi, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: ANError) {
                    Toast.makeText(this@RiwayatEnkripsi, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun getDataRiwayatExtention(extension : String){
        val url = urlAPI.endPoint.url
        val sharedPreference =  getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id","")
        AndroidNetworking.get("$url/other/riwayatprivate/$extension/$id")
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("ini respon", response.toString())
                    try {
                        if (response.getString("success").equals("true")) {
                            binding.recyclerView.visibility = View.VISIBLE
                            val getDataArray = response.getJSONArray("newResult")
                            for (i in 0 until getDataArray.length()){
                                val item = getDataArray.getJSONObject(i)
                                dataList.add(
                                    dataRiwayatEnkripsi(
                                        item.getString("id_file"),
                                        item.getString("nama_file"),
                                        item.getString("created"),
                                        item.getString("extension"),
                                        item.getString("is_send")
                                    )
                                )
                                populate()
                            }
                        }else{
                            Toast.makeText(this@RiwayatEnkripsi, response.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this@RiwayatEnkripsi, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: ANError) {
                    Toast.makeText(this@RiwayatEnkripsi, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun populate(){
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.recyclerView.layoutManager = layoutManager
        val adp = adapterRiwayatEnkripsi(this,dataList)
        binding.recyclerView.adapter = adp

    }
}