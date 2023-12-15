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
import com.example.pp_project.databinding.ActivityLoginBinding
import com.example.pp_project.urlAPI
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            if (binding.edEmail.text.toString().isEmpty() || binding.edPassword.text.toString().isEmpty()){
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show()
            }else{
                binding.btnLogin.visibility = View.GONE
                login()
            }

        }

        binding.txtDaftar.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun login(){
        val url = urlAPI.endPoint.url
        val jsonObject = JSONObject()
        try {
            jsonObject.put("email", binding.edEmail.text.toString())
            jsonObject.put("password", binding.edPassword.text.toString())
        }catch ( e: JSONException){
            e.printStackTrace()
        }
        AndroidNetworking.post("$url/auth/login")
            .addJSONObjectBody(jsonObject)
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        Log.d("respon login", response.toString())
                        if (response.getString("success").equals("true")){
                            val getId = response.getString("id")

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            binding.btnLogin.visibility = View.VISIBLE
                            val sharedPreference =  getSharedPreferences("user", Context.MODE_PRIVATE)
                            val editorId = sharedPreference.edit()
                            editorId.putString("id",getId)
                            editorId.commit()

                        }else{
                            Toast.makeText(this@LoginActivity, response.getString("message"), Toast.LENGTH_SHORT).show()
                            binding.btnLogin.visibility = View.VISIBLE
                        }

                    }catch (e : JSONException){
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        binding.btnLogin.visibility = View.VISIBLE
                    }

                }
                override fun onError(error: ANError) {
                    // handle error
                    Toast.makeText(this@LoginActivity,error.toString(), Toast.LENGTH_SHORT).show()
                    binding.btnLogin.visibility = View.VISIBLE
                }
            })
    }

}