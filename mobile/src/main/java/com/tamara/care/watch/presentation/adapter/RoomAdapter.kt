package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.HomeRoomEntity
import com.tamara.care.watch.databinding.ItemRoomBinding
import javax.inject.Inject


class RoomAdapter @Inject constructor() : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    var items: List<HomeRoomEntity> = listOf()
    lateinit var onItemClickListener: (HomeRoomEntity) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RoomViewHolder(private val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = items[position]
            binding.apply {
                icon.setBackgroundResource(item.icon)
                if (item.name != null) {
                    title.text = binding.root.context.resources.getString(item.name)
                    title.visibility = View.VISIBLE
                } else {
                    title.visibility = View.GONE
                }
                root.backgroundTintList =
                    binding.root.context.resources.getColorStateList(item.color, null)
                root.setOnClickListener {
                    onItemClickListener.invoke(item)
                }
            }
        }
    }
}