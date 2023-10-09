package com.emiratz.collectiondata.network

import com.emiratz.collectiondata.model.ResponseSuccess
import com.emiratz.collectiondata.model.ResponseGetAllData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("collection/all")
    fun getAllData() : Call<ResponseGetAllData>

    @Multipart
    @POST("collection/add")
    fun addData(@Part("nama") nama:RequestBody, @Part("alamat") alamat:RequestBody, @Part("jumlah") jumlah:RequestBody) : Call<ResponseSuccess>

    @Multipart
    @POST("collection/update")
    fun updateData(@Part("id") id:RequestBody, @Part("nama") nama:RequestBody, @Part("alamat") alamat:RequestBody, @Part("jumlah") jumlah:RequestBody) : Call<ResponseSuccess>

    @Multipart
    @POST("collection/delete")
    fun deleteData(@Part("id") id:RequestBody) : Call<ResponseSuccess>

    @GET("collection/all")
    fun getAllDataByFilter(@Query("filters[0][co][0][fl]") filterField : String,
                           @Query("filters[0][co][0][op]") filterOperator : String,
                           @Query("filters[0][co][0][vl]") filterValue : String
    ) : Call<ResponseGetAllData>

}