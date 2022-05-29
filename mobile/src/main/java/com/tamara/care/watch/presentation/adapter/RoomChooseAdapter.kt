package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.AdapterRoomEntity
import com.tamara.care.watch.databinding.ItemRoomChooseBinding
import javax.inject.Inject

class RoomChooseAdapter @Inject constructor() : RecyclerView.Adapter<RoomChooseAdapter.BeaconViewHolder>() {

    private var selectedItem = -1
    lateinit var onRoomClickListener: (AdapterRoomEntity) -> Unit

    private val diffCallback = object : DiffUtil.ItemCallback<AdapterRoomEntity>() {
        override fun areItemsTheSame(oldItem: AdapterRoomEntity, newItem: AdapterRoomEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdapterRoomEntity, newItem: AdapterRoomEntity): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<AdapterRoomEntity>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        val binding = ItemRoomChooseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeaconViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(position)

        if (differ.currentList[position].isOccupied) {
            holder.binding.room.alpha = 0.4f
            holder.binding.roomCard.isClickable = false
        } else {
            holder.binding.room.alpha = 1f
            holder.binding.roomCard.isClickable = true
        }
    }

    inner class BeaconViewHolder(val binding: ItemRoomChooseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                room.text = differ.currentList[position].name
                roomCard.setOnClickListener {

                    differ.currentList[position].isOccupied = selectedItem == position

                    val previousItem: Int = selectedItem
                    selectedItem = position

                    onRoomClickListener.invoke(differ.currentList[position])

                    if (!differ.currentList[position].isOccupied) {
                        differ.currentList[position].isOccupied = true
                    }
                    if (previousItem != -1) { // we don`t have start item, that`s why we should make this additional check
                        if (differ.currentList[previousItem].isOccupied) {
                            differ.currentList[previousItem].isOccupied = false
                        }
                    }

                    if (previousItem != -1) {
                        notifyItemChanged(previousItem)
                    }
                    notifyItemChanged(selectedItem)
                }
                executePendingBindings()
            }
        }
    }
}