package com.tamara.care.watch.presentation.room

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.EmptyTimeEntity
import com.tamara.care.watch.data.entity.RoomPeriod
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentRoomBinding
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.TimeAdapter
import com.tamara.care.watch.presentation.dialog.FilterBottomDialogFragment.Companion.EXTRA_ROOM_DATE
import com.tamara.care.watch.presentation.dialog.FilterBottomDialogFragment.Companion.EXTRA_ROOM_PERIOD
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RoomFragment : Fragment() {

    lateinit var binding: FragmentRoomBinding

    @Inject
    lateinit var timeAdapter: TimeAdapter

    private val viewModel: RoomViewModel by viewModels()

    private lateinit var mainActivityListener: MainActivityListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room, container, false)
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = (context as MainActivityListener)
        mainActivityListener.hideBottomNavBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val room = arguments?.getString("EXTRA_ROOM")
        binding.title.text = room

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<RoomPeriod>(
            EXTRA_ROOM_PERIOD
        )?.observe(viewLifecycleOwner) { period ->
            viewModel.getRoomInfo(roomName = room ?: "-1", period = period.key)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<String>>(
            EXTRA_ROOM_DATE
        )?.observe(viewLifecycleOwner) { date ->
            viewModel.getRoomInfo(roomName = room ?: "-1", dateFrom = date[0], dateTo = date[1])
        }

        viewModel.getRoomInfo(room ?: "-1")
        setupClicks()
        setupViews()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.roomInfoLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    binding.infoProgressBar.visibility = View.GONE
                    val items = mutableListOf<EmptyTimeEntity>()
                    state.data.forEach { roomEntity ->
                        items.add(EmptyTimeEntity(date = roomEntity.date, isDate = true))
                        roomEntity.time?.forEach { timeEntity ->
                            items.add(EmptyTimeEntity(from = timeEntity.from, to = timeEntity.to, isTime = true))
                        }
                        items.add(EmptyTimeEntity(isDivider = true))
                    }
                    if (!items.isNullOrEmpty()) items.removeLast()

                    val previousContentSize =timeAdapter.items.size

                    timeAdapter.items.clear()
                    timeAdapter.notifyItemRangeRemoved(0, previousContentSize)
                    timeAdapter.items.addAll(items)
                    timeAdapter.notifyItemRangeInserted(0, items.size)
                }
                is ModelState.Error -> {
                    binding.infoProgressBar.visibility = View.GONE
                    requireContext().showToast(state.error.errorMessage)
                }
                is ModelState.Loading -> {
                    binding.infoProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupViews() {
        binding.roomRecycler.adapter = timeAdapter
    }

    private fun setupClicks() {
        binding.apply {
            btnFilter.setOnClickListener {
                findNavController().navigate(R.id.filterBottomDialogFragment)
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }
            drawer.setOnClickListener {
                mainActivityListener.openDrawer()
            }
        }
    }
}