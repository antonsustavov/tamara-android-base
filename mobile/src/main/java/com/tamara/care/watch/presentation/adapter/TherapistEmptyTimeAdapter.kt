package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.RoomType
import com.tamara.care.watch.data.entity.TransmitterActivityEntity
import com.tamara.care.watch.databinding.ItemTherapistEmptyTimeBinding
import javax.inject.Inject


class TherapistEmptyTimeAdapter @Inject constructor() :
    RecyclerView.Adapter<TherapistEmptyTimeAdapter.EmptyTimeViewHolder>() {

    var items: MutableList<TransmitterActivityEntity> = mutableListOf()
    lateinit var onRoomClickListener: (RoomType) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyTimeViewHolder {
        val binding = ItemTherapistEmptyTimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmptyTimeViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EmptyTimeViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class EmptyTimeViewHolder(private val binding: ItemTherapistEmptyTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.timeEntity = items[position].time
        }
    }
}