package com.emiratz.collectiondata.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseGetAllData(
	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class Data(
	@field:SerializedName("collection")
	val collection: List<CollectionItem?>? = null
) : Parcelable

@Parcelize
data class CollectionItem(
	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("jumlah")
	val jumlah: String? = null,
) : Parcelable
