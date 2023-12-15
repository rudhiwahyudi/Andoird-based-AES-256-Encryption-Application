package com.example.pp_project.user

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.R
import com.example.pp_project.urlAPI
import com.example.pp_project.user.adapter.adapterRiwayatEnkripsi
import com.example.pp_project.user.data.dataRiwayatEnkripsi
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {
    private lateinit var btnDekripsi : Button
    private lateinit var edDekripsi : EditText
    private lateinit var dialogLayout : View
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var dialog : Dialog
    private lateinit var btnPDF : ImageView
    private lateinit var btnTXT : ImageView
    private lateinit var btnDoc : ImageView
    private lateinit var btnImage : ImageView
    private lateinit var recyclerView: RecyclerView
    private var dataList = ArrayList<dataRiwayatEnkripsi>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dialogLayout = LayoutInflater.from(activity).inflate(R.layout.dialog_alert_progress, null)
        alertDialog = activity?.let { AlertDialog.Builder(it).setView(dialogLayout) }!!
        dialog = alertDialog.create()


        btnDekripsi = view.findViewById(R.id.btnDcrypt)
        edDekripsi = view.findViewById(R.id.edDekripsi)
        btnTXT = view.findViewById(R.id.txtFile)
        btnImage = view.findViewById(R.id.imageFile)
        btnPDF = view.findViewById(R.id.pdfFile)
        btnDoc = view.findViewById(R.id.docFile)
        recyclerView = view.findViewById(R.id.recyclerView)
        dataList = ArrayList<dataRiwayatEnkripsi>()
        getDataRiwayat()
        btnTXT.setOnClickListener {
            val intent = Intent(activity, RiwayatEnkripsi::class.java)
            intent.putExtra("type", "txt")
            startActivity(intent)
        }
        btnPDF.setOnClickListener {
            val intent = Intent(activity, RiwayatEnkripsi::class.java)
            intent.putExtra("type", "pdf")
            startActivity(intent)
        }
        btnDoc.setOnClickListener {
            val intent = Intent(activity, RiwayatEnkripsi::class.java)
            intent.putExtra("type", "docx")
            startActivity(intent)
        }
        btnTXT.setOnClickListener {
            val intent = Intent(activity, RiwayatEnkripsi::class.java)
            intent.putExtra("type", "png")
            startActivity(intent)
        }


        btnDekripsi.setOnClickListener {
            if (edDekripsi.text.toString().isEmpty()){
                Toast.makeText(activity, "Token wajib diisi", Toast.LENGTH_SHORT).show()
            }else{
                dekripsi()
            }
        }

        return view
    }
    private fun getDataRiwayat(){
        val url = urlAPI.endPoint.url
        val sharedPreference =  activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
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
                            recyclerView.visibility = View.VISIBLE
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
                            Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: ANError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun dekripsi(){
        val url = urlAPI.endPoint.url
        val sharedPreference =  activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id","")

        val jsonObject = JSONObject()
        try {
            jsonObject.put("id_user", id)
            jsonObject.put("token", edDekripsi.text.toString())
        }catch ( e: JSONException){
            e.printStackTrace()
        }
        AndroidNetworking.post("$url/proses/dekripsi")
            .addJSONObjectBody(jsonObject)
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        if (response.getString("success").equals("true")){
                            Log.d("ini respon", response.toString())
                            dialog.dismiss()
                            val getURL = "${urlAPI.endPoint.url}${response.getString("url")}"
                            val layoutDialog = LayoutInflater.from(activity)
                                .inflate(R.layout.dialog_dekripsi_success, null)
                            val alertDialog =
                                activity?.let { AlertDialog.Builder(it).setView(layoutDialog) }
                            val dialog = alertDialog?.create()
                            dialog?.show()

                            val btnOpen = layoutDialog.findViewById<Button>(R.id.btnOpen)
                            btnOpen.setOnClickListener {
                                val uri = Uri.parse(getURL)
                                val intent = Intent(Intent.ACTION_VIEW,uri)
                                startActivity(intent)
                                dialog?.dismiss()
                            }
                        }else{
                            Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    }catch (e : JSONException){
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onError(error: ANError) {
                    // handle error
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun populate(){
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        recyclerView.layoutManager = layoutManager
        val adp = activity?.let { adapterRiwayatEnkripsi(it, dataList) }
        recyclerView.adapter = adp

    }


}