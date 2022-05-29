package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.ProximityItemEntity
import com.tamara.care.watch.databinding.ItemProximityBinding
import javax.inject.Inject


class ProxAdapter @Inject constructor() : RecyclerView.Adapter<ProxAdapter.ProxViewHolder>() {

    var items = arrayListOf<ProximityItemEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProxViewHolder {
        val binding = ItemProximityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProxViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProxViewHolder, position: Int) {
        holder.bind()
    }

    inner class ProxViewHolder(private val binding: ItemProximityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                item = items[bindingAdapterPosition]
                if (!items[bindingAdapterPosition].veryClose.isNullOrEmpty()) {
                    veryCloseRoomTitleTextView.visibility = View.VISIBLE
                    veryClose.visibility = View.VISIBLE
                    veryClose.setProximityValue(items[bindingAdapterPosition].veryClose, items[bindingAdapterPosition].veryCloseSignal)
                } else {
                    veryCloseRoomTitleTextView.visibility = View.GONE
                    veryClose.visibility = View.GONE
                }
                executePendingBindings()
            }
        }
    }
}