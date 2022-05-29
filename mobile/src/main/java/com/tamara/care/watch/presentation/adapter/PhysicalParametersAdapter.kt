package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.ParametersEntity
import com.tamara.care.watch.databinding.ItemPhysicalParameterBinding

import javax.inject.Inject

/**
 * Created by ArtemLampa on 12.10.2021.
 */
class PhysicalParametersAdapter @Inject constructor() : RecyclerView.Adapter<PhysicalParametersAdapter.PhysicalParametersViewHolder>() {

    var physicalParametersList = arrayListOf<ParametersEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhysicalParametersViewHolder {
        return PhysicalParametersViewHolder(ItemPhysicalParameterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PhysicalParametersViewHolder, position: Int) {
        holder.bindView()
    }

    override fun getItemCount(): Int = physicalParametersList.size

    inner class PhysicalParametersViewHolder(val binding: ItemPhysicalParameterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            binding.apply {
                physicalParameter = physicalParametersList[bindingAdapterPosition]
                executePendingBindings()
            }
        }
    }
}