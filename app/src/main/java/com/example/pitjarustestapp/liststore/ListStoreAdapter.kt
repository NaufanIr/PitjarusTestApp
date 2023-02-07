package com.example.pitjarustestapp.liststore

import android.content.Intent
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pitjarustestapp.data.local.StoreEntity
import com.example.pitjarustestapp.databinding.ItemStoreBinding
import com.example.pitjarustestapp.storedetail.StoreDetailActivity
import kotlin.math.roundToInt

class ListStoreAdapter(private val stores: List<StoreEntity>, private val myLocation: Location) :
    RecyclerView.Adapter<ListStoreAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = stores.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val subtitleVal = "${stores[position].channelName} • ${stores[position].areaName} • ${stores[position].regionName}"

        val destination = Location("Location A").also {
            it.latitude = stores[position].latitude.toDouble()
            it.longitude = stores[position].longitude.toDouble()
        }

        val distance = myLocation.distanceTo(destination).roundToInt()

        holder.binding.apply {
            val outletName = "${stores[position].storeName} ${stores[position].id}"
            title.text = outletName
            cluster.text = stores[position].address
            subtitle.text = subtitleVal
            tvDistance.text = StringBuilder("$distance m")

            if (stores[position].isVisited!!) {
                linearLayoutCompat2.visibility = View.VISIBLE
            }

            //CLICK ACTION
            itemUser.setOnClickListener {
                val intent = Intent(itemUser.context, StoreDetailActivity::class.java).also {
                    it.putExtra(StoreDetailActivity.EXTRA_ID, stores[position].id.toString())
                }
                itemUser.context.startActivity(intent)
            }
        }
    }
}