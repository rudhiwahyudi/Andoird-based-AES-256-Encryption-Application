package com.example.pp_project.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.pp_project.R
import com.example.pp_project.user.data.dataSpinnerDaftarRumahSakit
import kotlinx.android.synthetic.main.spinner_layout.view.textSpinner

class adapterSpinnerDaftarRumahSakit(context: Context, spinnerList : List<dataSpinnerDaftarRumahSakit>):ArrayAdapter<dataSpinnerDaftarRumahSakit>(context,0, spinnerList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val item = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false)

        view.textSpinner.text = item!!.nama


        return view
    }
}