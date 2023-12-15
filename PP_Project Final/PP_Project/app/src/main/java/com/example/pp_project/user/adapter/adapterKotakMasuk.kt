package com.example.pp_project.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pp_project.R
import com.example.pp_project.user.data.dataKotakMasuk

class adapterKotakMasuk(val context : Context, val dataList : ArrayList<dataKotakMasuk>) : RecyclerView.Adapter<adapterKotakMasuk.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterKotakMasuk.MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fetch_kotak_masuk, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: adapterKotakMasuk.MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.namaDokumen.text = currentItem.namaDokument
        holder.namaPengguna.text = currentItem.namaPengirim
        holder.waktuDokument.text = currentItem.tanggalPengiriman
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class MyViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val namaPengguna :TextView = item.findViewById(R.id.namaPengirim)
        val namaDokumen : TextView = item.findViewById(R.id.namaDokumen)
        val waktuDokument : TextView = item.findViewById(R.id.waktuDokumen)
    }
}