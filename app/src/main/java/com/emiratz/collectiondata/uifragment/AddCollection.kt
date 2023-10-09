package com.emiratz.collectiondata.uifragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.emiratz.collectiondata.R
import com.emiratz.collectiondata.model.CollectionItem
import com.emiratz.collectiondata.model.ResponseSuccess
import com.emiratz.collectiondata.network.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCollection.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCollection : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: CollectionItem? = null
    private var param3: String? = null

    lateinit var progressBar : ProgressBar
    lateinit var txtNama : EditText
    lateinit var txtAlamat : EditText
    lateinit var txtJumlah : EditText
    lateinit var btnSend : Button
    lateinit var txtTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getParcelable(ARG_PARAM2, CollectionItem::class.java)
            param3 = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtNama = view.findViewById(R.id.editNama)
        txtAlamat = view.findViewById(R.id.editAlamat)
        txtJumlah = view.findViewById(R.id.editJumlah)
        btnSend = view.findViewById(R.id.btnSend)
        progressBar = view.findViewById(R.id.progressBar2)
        txtTitle = view.findViewById(R.id.txtTitle)
        txtTitle.text = param3

        if(param1 == "add"){
            btnSend.setOnClickListener(View.OnClickListener {
                addCollection(
                    CollectionItem(null,
                        txtNama.text.toString(),
                        txtAlamat.text.toString(),
                        txtJumlah.text.toString(),
                    )
                )
            })
        }else{
            txtNama.setText(param2?.nama)
            txtAlamat.setText(param2?.alamat)
            txtJumlah.setText(param2?.jumlah)
            btnSend.setOnClickListener {
                updateCollection(CollectionItem(param2?.id, txtNama.text.toString(), txtAlamat.text.toString(), txtJumlah.text.toString()))
            }
        }
    }

    fun addCollection(data : CollectionItem){
        val client = ApiConfig.getApiService()
            .addData(toRequestBody(data.nama.toString()),
                toRequestBody(data.alamat.toString()),
                toRequestBody(data.jumlah.toString()))
        showProgressBar(true)
        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgressBar(false)
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    parentFragmentManager.popBackStackImmediate()
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgressBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateCollection(data : CollectionItem){
        val client = ApiConfig.getApiService()
            .updateData(toRequestBody(data.id.toString()),
                toRequestBody(data.nama.toString()),
                toRequestBody(data.alamat.toString()),
                toRequestBody(data.jumlah.toString()))
        showProgressBar(true)
        client.enqueue(object : Callback<ResponseSuccess> {
            override fun onResponse(
                call: Call<ResponseSuccess>,
                response: Response<ResponseSuccess>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgressBar(false)
                    Log.e("INFO", "onSuccess: ${responseBody.message}")
                    parentFragmentManager.popBackStackImmediate()
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgressBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun showProgressBar(flag:Boolean){
        if (flag){
            progressBar.visibility = View.VISIBLE
            progressBar.animate()
        }else{
            progressBar.visibility = View.GONE
        }
    }

    fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCollection.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: CollectionItem, param3: String) =
            AddCollection().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putParcelable(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}