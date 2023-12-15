package com.example.pp_project.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.KotakMasuk
import com.example.pp_project.R
import com.example.pp_project.urlAPI
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject


class ProfileFragment : Fragment() {
    private lateinit var btnKotakMasuk :RelativeLayout
    private lateinit var btnKirimFile :RelativeLayout
    private lateinit var btnRiwayatEnkripsi : RelativeLayout
    private lateinit var namaPengguna : TextView
    private lateinit var emailPengguna : TextView
    private lateinit var btnKeluar : RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        btnKotakMasuk = view.findViewById(R.id.kotakMasuk)
        btnKirimFile = view.findViewById(R.id.sendFile)
        btnRiwayatEnkripsi = view.findViewById(R.id.riwayatEnkripsi)
        namaPengguna = view.findViewById(R.id.namaPengguna)
        emailPengguna = view.findViewById(R.id.emailPengguna)
        btnKeluar = view.findViewById(R.id.keluarAplikasi)

        getData()
        btnKotakMasuk.setOnClickListener {
            val intent = Intent(activity, KotakMasuk::class.java)
            startActivity(intent)
        }
        btnKirimFile.setOnClickListener {
            val intent = Intent(activity, KirimFile::class.java)
            startActivity(intent)
        }
        btnRiwayatEnkripsi.setOnClickListener {
            val intent = Intent(activity, RiwayatEnkripsi::class.java)
            startActivity(intent)
        }
        btnKeluar.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }
    private fun getData() {
        val url = urlAPI.endPoint.url
        val sharedPreference = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id", "")
        AndroidNetworking.get("$url/other/profil/${id}")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.d("ini respon", response.toString())
                    try {
                        if (response?.getString("success").equals("true")) {
                            val getJSONArray = response?.getJSONArray("result")
                            namaPengguna.text= getJSONArray?.getJSONObject(0)!!.getString("nama")
                            emailPengguna.text =getJSONArray?.getJSONObject(0)!!.getString("email")


                        }else{
                            Toast.makeText(activity, response!!.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(activity, anError!!.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

}