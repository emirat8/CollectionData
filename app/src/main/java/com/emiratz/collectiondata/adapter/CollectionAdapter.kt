package com.emiratz.collectiondata.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emiratz.collectiondata.R
import com.emiratz.collectiondata.model.CollectionItem

class CollectionAdapter(var data : List<CollectionItem?>, private val clickListener: (CollectionItem) -> Unit, private val onlongclick : (CollectionItem) -> Unit ) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listcollection, parent, false))

    override fun onBindViewHolder(holder: CollectionAdapter.ViewHolder, position: Int) {
        holder.txtNama.text = data.get(position)?.nama
        holder.txtAlamat.text = data.get(position)?.alamat
        holder.txtJumlah.text = data.get(position)?.jumlah
        holder.itemView.setOnClickListener {
            clickListener(data.get(position)!!)
        }
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                val alertDialog = AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Hapus Data")
                    .setMessage("Apakah anda yakin ingin menghapus data ini ?")
                    .setPositiveButton("Ya"){dialog, which ->
                        onlongclick(data.get(position)!!)
                    }
                    .setNegativeButton("Tidak",null)
                    .create()
                alertDialog.show()
                return true
            }
        })
    }

    override fun getItemCount():Int = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNama = itemView.findViewById<TextView>(R.id.txtNama)
        val txtAlamat = itemView.findViewById<TextView>(R.id.txtAlamat)
        val txtJumlah = itemView.findViewById<TextView>(R.id.txtJumlah)
    }

}