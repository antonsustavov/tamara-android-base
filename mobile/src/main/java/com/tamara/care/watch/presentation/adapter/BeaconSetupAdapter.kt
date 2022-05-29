package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.BeaconRegisterEntity
import com.tamara.care.watch.databinding.ItemSetupBeaconBinding
import javax.inject.Inject


class BeaconSetupAdapter @Inject constructor(): RecyclerView.Adapter<BeaconSetupAdapter.BeaconViewHolder>() {

    var items: MutableList<BeaconRegisterEntity> = mutableListOf()
    lateinit var onItemClickListener: (BeaconRegisterEntity) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        val binding = ItemSetupBeaconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeaconViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class BeaconViewHolder(private val binding: ItemSetupBeaconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item  = items.get(position)
            binding.item = item
            if (item._id.isNullOrEmpty()) {
                binding.room.setTextColor((ContextCompat.getColor(binding.root.context, R.color.red)))
                binding.swipeRoDelete.visibility = View.GONE
            }
            else {
                binding.room.setTextColor((ContextCompat.getColor(binding.root.context, R.color.black)))
                binding.swipeRoDelete.visibility = View.VISIBLE
            }
            binding.root.setOnClickListener {
                onItemClickListener.invoke(items.get(position))
            }
        }
    }
}