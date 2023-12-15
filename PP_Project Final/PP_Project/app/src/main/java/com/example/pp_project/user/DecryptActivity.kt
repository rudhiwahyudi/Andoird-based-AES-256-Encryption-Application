package com.example.pp_project.user

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.pp_project.R
import com.example.pp_project.RealPathUtil
import com.example.pp_project.databinding.ActivityDecryptBinding
import com.example.pp_project.urlAPI
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class DecryptActivity : AppCompatActivity() {
    var getStringFile :String = ""
    var getExtension : String = ""
    private lateinit var dialogLayout : View
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var dialog : Dialog
    private lateinit var binding: ActivityDecryptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDecryptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_alert_progress, null)
        alertDialog = AlertDialog.Builder(this).setView(dialogLayout)
        dialog = alertDialog.create()

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.btnEncrypt.setOnClickListener {


            if (binding.edNamaDokumen.text.toString().trim().isEmpty()) {
                binding.edNamaDokumen.error = "Nama dokumen wajib diisi"
            } else if (binding.edDeskripsiDokument.text.toString().trim().isEmpty()) {
                binding.edDeskripsiDokument.error = "Deskripsi dokumen wajib diisi"
            } else if (getStringFile.isEmpty()) {
                Toast.makeText(this, "Belum menambahka dokumen", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                enkripsi()
                binding.btnEncrypt.visibility = View.GONE
            }
        }

        binding.enkripsiFile.setOnClickListener {
            if (Build.VERSION.SDK_INT> Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, 1001)
                }else{
                    filePicker()
                }
            }else{
                filePicker()
            }

        }
    }

    private fun filePicker() {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val file = data!!.data
            val path = RealPathUtil.getRealPath(this, file!!)
            getExtension = path!!.substringAfterLast(".")
            val nameFile = path.substringAfterLast("/")
            binding.textFile.text = nameFile
            binding.textFile.visibility = View.VISIBLE
            binding.icUnduh.visibility = View.VISIBLE
            binding.parentUnduh.visibility = View.GONE

            //enkripsi
            val getUri = Uri.parse(file.toString())
            val result = uriToBase64(this, getUri)
            getStringFile = result!!

        }
    }

    private fun enkripsi(){
        val url = urlAPI.endPoint.url
        val sharedPreference =  getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPreference?.getString("id","")

        val namaDokumen = binding.edNamaDokumen.text.toString().trim()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id_user", id)
            jsonObject.put("description", binding.edDeskripsiDokument.text.toString())
            jsonObject.put("filename", "$namaDokumen.$getExtension")
            jsonObject.put("base64", getStringFile)

        }catch ( e: JSONException){
            e.printStackTrace()
        }
        AndroidNetworking.post("$url/proses/enkripsi")
            .addJSONObjectBody(jsonObject)
            .addHeaders("Content-Type", "application/json")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        if (response.getString("success").equals("true")){
                            dialog.dismiss()
                            val layoutDialog = LayoutInflater.from(this@DecryptActivity).inflate(R.layout.dialog_enkripsi_success, null)
                            val alertDialog = AlertDialog.Builder(this@DecryptActivity).setView(layoutDialog)
                            val dialog = alertDialog.create()
                            dialog.show()

                            val btnOke = layoutDialog.findViewById<Button>(R.id.btnOke)
                            btnOke.setOnClickListener {
                                val intent = Intent(this@DecryptActivity, MainActivity::class.java)
                                startActivity(intent)
                                dialog.dismiss()
                                finish()
                            }
                            binding.btnEncrypt.visibility = View.VISIBLE
                        }else{
                            Toast.makeText(this@DecryptActivity, response.getString("message"), Toast.LENGTH_SHORT).show()
                            binding.btnEncrypt.visibility = View.VISIBLE
                        }

                    }catch (e : JSONException){
                        binding.btnEncrypt.visibility = View.VISIBLE
                        Toast.makeText(this@DecryptActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onError(error: ANError) {
                    Toast.makeText(this@DecryptActivity, error.message, Toast.LENGTH_SHORT).show()
                    binding.btnEncrypt.visibility = View.VISIBLE
                }
            })
    }

    fun uriToBase64(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = ByteArrayOutputStream()

        try {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            val bytes = outputStream.toByteArray()
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                outputStream.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return null
    }
}