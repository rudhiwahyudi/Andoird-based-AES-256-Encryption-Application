package com.example.pp_project.user.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pp_project.R
import com.example.pp_project.user.data.dataRiwayatEnkripsi

class adapterRiwayatEnkripsi(val context : Context, val dataList : ArrayList<dataRiwayatEnkripsi>) : RecyclerView.Adapter<adapterRiwayatEnkripsi.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterRiwayatEnkripsi.MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fetch_riwayat_enkripsi, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: adapterRiwayatEnkripsi.MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        val typeFile = currentItem.jenis
        if (typeFile.equals("docx")){
            holder.jenis.setImageResource(R.drawable.word_logo)
        }else if (typeFile.equals("xlsx")){
            holder.jenis.setImageResource(R.drawable.excel_logo)
        }else if (typeFile.equals("pdf")){
            holder.jenis.setImageResource(R.drawable.pdf_logo)
        }else if (typeFile.equals("csv")){
            holder.jenis.setImageResource(R.drawable.csv)
        }else if (typeFile.equals("txt")){
            holder.jenis.setImageResource(R.drawable.txt)
        }else if (typeFile.equals("jpg")){
            holder.jenis.setImageResource(R.drawable.jpg)
        }else if (typeFile.equals("png")){
            holder.jenis.setImageResource(R.drawable.png)
        }

        if (currentItem.cek.equals("0")){
            holder.cek.text = "File Privat"
        }else{
            holder.cek.text = "Dikirim ke rumah sakit lain"
        }



        holder.tanggal.text = currentItem.tanggal

        val getName = currentItem.nama_dokumen
        val sliceName = getName.substringAfterLast("-")
        val sliceNameAgain = sliceName.substringBeforeLast(".")
        holder.namaDokumen.text = sliceNameAgain

//        holder.parent.setOnClickListener {
//            val intent = Intent(context, DetailFileActivity::class.java)
//            intent.putExtra("id_file", currentItem.id)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    class MyViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val namaDokumen : TextView = item.findViewById(R.id.namaDokumen)
        val jenis : ImageView = item.findViewById(R.id.jenisDokumen)
        val tanggal : TextView = item.findViewById(R.id.tanggalDokumen)
        val parent : LinearLayout = item.findViewById(R.id.parent)
        val cek : TextView = item.findViewById(R.id.cekDokumen)
    }
}