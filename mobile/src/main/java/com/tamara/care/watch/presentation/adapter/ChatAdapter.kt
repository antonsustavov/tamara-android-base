package com.tamara.care.watch.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.entity.ChatEntity
import com.tamara.care.watch.data.entity.ChatType
import com.tamara.care.watch.databinding.ItemChatOpponentBinding
import com.tamara.care.watch.databinding.ItemChatUserBinding
import javax.inject.Inject

class ChatAdapter @Inject constructor() : RecyclerView.Adapter<ChatAdapter.BaseViewHolder>() {

    var items: List<ChatEntity> = listOf()

    companion object {
        private const val USER_TXT = 1
        private const val OPPONENT_TXT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            ChatType.USER_TEXT -> USER_TXT
            ChatType.OPPONENT_TEXT -> OPPONENT_TXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            USER_TXT -> UserTxtViewHolder(
                ItemChatUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            OPPONENT_TXT -> OpponentTxtViewHolder(
                ItemChatOpponentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> UserTxtViewHolder(
                ItemChatUserBinding.inflate(
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

    inner class UserTxtViewHolder(private val binding: ItemChatUserBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.userTxt.text = items[position].text
        }
    }

    inner class OpponentTxtViewHolder(private val binding: ItemChatOpponentBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.opponentTxt.text = items[position].text
        }
    }

}