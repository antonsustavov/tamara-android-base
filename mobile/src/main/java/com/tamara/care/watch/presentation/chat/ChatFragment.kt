package com.tamara.care.watch.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.ChatEntity
import com.tamara.care.watch.data.entity.ChatType
import com.tamara.care.watch.databinding.FragmentChatBinding
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.ChatAdapter
import com.tamara.care.watch.utils.hideKeyBoard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var chatAdapter: ChatAdapter

    var textFromUser = ""
    val items = mutableListOf<ChatEntity>(
        ChatEntity(
            "The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.",
            type = ChatType.USER_TEXT
        ),
        ChatEntity("the", "", ChatType.USER_TEXT),
        ChatEntity(
            text = "All the Lorem Ipsum generators on the Internet tend",
            type = ChatType.OPPONENT_TEXT
        ),
        ChatEntity(
            text = "Lorem Ipsum generators",
            type = ChatType.OPPONENT_TEXT
        ),
        ChatEntity(
            "All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as" +
                    " necessary, making this the first true generator on the Internet. It uses a dictionary " +
                    "of over 200 Latin words, combined with a handful of model sentence structures, " +
                    "to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is " +
                    "therefore always free from repetition, injected humour, or non-characteristic words etc.",
            "",
            ChatType.OPPONENT_TEXT
        ),
        ChatEntity(
            "All the Lorem Ipsum generators on theThe generated Lorem Ipsum is the",
            type = ChatType.OPPONENT_TEXT
        )
    )

    lateinit var mainActivityListener: MainActivityListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = (context as MainActivityListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityListener.hideBottomNavBar()
        setupClicks()
        setupViews()
    }

    private fun setupViews() {
        binding.chatRecycler.adapter = chatAdapter
        chatAdapter.items = items
        chatAdapter.notifyDataSetChanged()

        binding.sendMessageEditText.doOnTextChanged { text, start, before, count ->
            textFromUser = text.toString().trim()
        }
    }

    private fun setupClicks() {
        binding.apply {

            back.setOnClickListener {
                findNavController().popBackStack()
            }
            drawer.setOnClickListener {
                mainActivityListener.openDrawer()
            }

            sendMessageIcon.setOnClickListener {
                if (textFromUser.isNotEmpty()) {
                    items.add(ChatEntity(textFromUser, type = ChatType.USER_TEXT))
                    sendMessageEditText.text.clear()
                    sendMessageEditText.clearFocus()
                    chatAdapter.items = items
                    chatRecycler.scrollToPosition(items.size - 1)
                }
            }
        }
//        binding.setting.setOnClickListener {
//            findNavController().navigate(R.id.BeaconSetupFragment)
//        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().hideKeyBoard()
    }

}