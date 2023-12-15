package com.example.pp_project.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.R
import com.example.pp_project.databinding.ActivityRegisterBinding
import com.example.pp_project.urlAPI
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnDaftar.setOnClickListener {
            if (binding.edUsername.text.toString().isEmpty() || binding.edEmail.text.toString().isEmpty() || binding.edPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
            }else{
                binding.btnDaftar.visibility = View.GONE
                register()
            }
        }
    }
    private fun register(){
        val url = urlAPI.endPoint.url
        val jsonObject = JSONObject()
        try {
            jsonObject.put("email", binding.edEmail.text.toString())
            jsonObject.put("password", binding.edPassword.text.toString())
            jsonObject.put("nama", binding.edUsername.text.toString())
        }catch ( e: JSONException){
            e.printStackTrace()
        }
        AndroidNetworking.post("$url/auth/register")
            .addJSONObjectBody(jsonObject)
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        if (response.getString("success").equals("true")){
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@RegisterActivity, response.getString("message"), Toast.LENGTH_SHORT).show()
                            binding.btnDaftar.visibility = View.VISIBLE
                        }

                    }catch (e : JSONException){
                        Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                        binding.btnDaftar.visibility = View.VISIBLE
                    }

                }
                override fun onError(error: ANError) {
                    // handle error
                    Toast.makeText(this@RegisterActivity,error.toString(), Toast.LENGTH_SHORT).show()
                    binding.btnDaftar.visibility = View.VISIBLE
                }
            })
    }
}