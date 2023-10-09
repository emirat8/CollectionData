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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emiratz.collectiondata.R
import com.emiratz.collectiondata.adapter.CollectionAdapter
import com.emiratz.collectiondata.model.CollectionItem
import com.emiratz.collectiondata.model.ResponseGetAllData
import com.emiratz.collectiondata.model.ResponseSuccess
import com.emiratz.collectiondata.network.ApiConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
 * Use the [Collection.newInstance] factory method to
 * create an instance of this fragment.
 */
class Collection : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null

    lateinit var recyclerView: RecyclerView
    lateinit var collectionAdapter : CollectionAdapter
    lateinit var txtSearch : EditText
    lateinit var fabAddData : FloatingActionButton
    lateinit var progressBar : ProgressBar
    lateinit var btnSearch : Button
    lateinit var txtTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.listCollection)
        progressBar = view.findViewById(R.id.progressBar)
        txtSearch = view.findViewById(R.id.searchField)
        fabAddData = view.findViewById(R.id.fabAdd)
        btnSearch = view.findViewById(R.id.btnSearch)
        txtTitle = view.findViewById(R.id.txtTitle)

        txtTitle.text = param3

        fabAddData.setOnClickListener(View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack("add form")
                .replace(R.id.frmFragmentRoot, AddCollection.newInstance("add", CollectionItem(), "Tambah Data"))
                .commit()
        })
        btnSearch.setOnClickListener(View.OnClickListener {
            getAllDataByFilter(CollectionItem("", txtSearch.text.toString(), "", ""))
        })

        getAllCollection()
    }

    fun getAllDataByFilter(data : CollectionItem){
        val client = ApiConfig.getApiService().getAllDataByFilter("nama", "like", data.nama.toString())
        showProgressBar(true)

        client.enqueue(object : Callback<ResponseGetAllData> {
            override fun onResponse(
                call: Call<ResponseGetAllData>,
                response: Response<ResponseGetAllData>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgressBar(false)
                    collectionAdapter = CollectionAdapter(responseBody.data?.collection!!, {item ->
                        parentFragmentManager.beginTransaction()
                            .addToBackStack("add form")
                            .replace(R.id.frmFragmentRoot, AddCollection.newInstance("update", item, "Update Data"))
                            .commit()
                    } , { item ->
                        deleteCollection(item)
                    })
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = collectionAdapter
                }
            }

            override fun onFailure(call: Call<ResponseGetAllData>, t: Throwable) {
                showProgressBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllCollection()
    }

    fun getAllCollection(){
        val client = ApiConfig.getApiService().getAllData()
        showProgressBar(true)

        client.enqueue(object : Callback<ResponseGetAllData> {
            override fun onResponse(
                call: Call<ResponseGetAllData>,
                response: Response<ResponseGetAllData>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showProgressBar(false)
                    collectionAdapter = CollectionAdapter(responseBody.data?.collection!!, {item ->
                        parentFragmentManager.beginTransaction()
                            .addToBackStack("add form")
                            .replace(R.id.frmFragmentRoot, AddCollection.newInstance("update", item, "Update Data"))
                            .commit()
                    }, {item ->
                        deleteCollection(item)
                    })
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = collectionAdapter
                }
            }

            override fun onFailure(call: Call<ResponseGetAllData>, t: Throwable) {
                showProgressBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun deleteCollection(data : CollectionItem){
        val client = ApiConfig.getApiService()
            .deleteData(toRequestBody(data.id.toString()))
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
                    getAllCollection()
                }
            }

            override fun onFailure(call: Call<ResponseSuccess>, t: Throwable) {
                showProgressBar(false)
                Log.e("INFO", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun showProgressBar(flag:Boolean){
        if (flag){
            progressBar.visibility = View.VISIBLE
            progressBar.animate()
        }else{
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Collection.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
            Collection().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}