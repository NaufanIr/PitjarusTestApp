package com.example.pitjarustestapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store")
class StoreEntity(

    @field:ColumnInfo("id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @field:ColumnInfo("visited")
    val isVisited: Boolean? = false,

    @field:ColumnInfo("visit_photo")
    val visitPhoto: String? = null,

    @field:ColumnInfo("visit_date")
    val visitDate: String? = null,

    @field:ColumnInfo("store_id")
    val storeId: String,

    @field:ColumnInfo("store_code")
    val storeCode: String,

    @field:ColumnInfo("channel_name")
    val channelName: String,

    @field:ColumnInfo("area_name")
    val areaName: String,

    @field:ColumnInfo("address")
    val address: String,

    @field:ColumnInfo("dc_name")
    val dcName: String,

    @field:ColumnInfo("latitude")
    val latitude: String,

    @field:ColumnInfo("region_id")
    val regionId: String,

    @field:ColumnInfo("area_id")
    val areaId: String,

    @field:ColumnInfo("account_id")
    val accountId: String,

    @field:ColumnInfo("dc_id")
    val dcId: String,

    @field:ColumnInfo("subchannel_id")
    val subchannelId: String,

    @field:ColumnInfo("account_name")
    val accountName: String,

    @field:ColumnInfo("store_name")
    val storeName: String,

    @field:ColumnInfo("subchannel_name")
    val subchannelName: String,

    @field:ColumnInfo("region_name")
    val regionName: String,

    @field:ColumnInfo("channel_id")
    val channelId: String,

    @field:ColumnInfo("longitude")
    val longitude: String,
)


