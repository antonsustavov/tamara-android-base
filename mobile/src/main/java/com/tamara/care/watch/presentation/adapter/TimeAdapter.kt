package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.EmptyTimeEntity
import com.tamara.care.watch.databinding.ItemDividerBinding
import com.tamara.care.watch.databinding.ItemEmptyDateBinding
import com.tamara.care.watch.databinding.ItemEmptyTimeBinding
import javax.inject.Inject


class TimeAdapter @Inject constructor() :
    RecyclerView.Adapter<TimeAdapter.BaseViewHolder>() {

    var items: ArrayList<EmptyTimeEntity> = arrayListOf()

    private val TYPE_DATE = 0
    private val TYPE_TIME = 1
    private val TYPE_DIVIDER = 2
    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].isDate == true -> TYPE_DATE
            items[position].isTime == true -> TYPE_TIME
            else -> TYPE_DIVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_DATE -> DateViewHolder(
                ItemEmptyDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_TIME -> TimeViewHolder(
                ItemEmptyTimeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> DividerViewHolder(
                ItemDividerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    abstract class BaseViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(position: Int)
    }

    inner class DateViewHolder(private val binding: ItemEmptyDateBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val item = items[position]
            binding.date.text = item.date
        }
    }

    inner class TimeViewHolder(private val binding: ItemEmptyTimeBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.timeEntity = items[position]
        }
    }

    inner class DividerViewHolder(private val binding: ItemDividerBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {}
    }
}