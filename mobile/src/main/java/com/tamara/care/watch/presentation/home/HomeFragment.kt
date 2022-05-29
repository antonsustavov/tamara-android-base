package com.tamara.care.watch.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.HomeRoomEntity
import com.tamara.care.watch.databinding.FragmentHomeBinding
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.RoomAdapter
import com.tamara.care.watch.utils.hideKeyBoard
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var roomAdapter: RoomAdapter

    lateinit var mainActivityListener: MainActivityListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = (context as MainActivityListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideKeyBoard()
        setupViews()
        setupClicks()
    }

    override fun onResume() {
        super.onResume()
        try {
            mainActivityListener.showBottomNavBar()
            mainActivityListener.getTransmitterInfo()
        } catch (e: Exception) {
        }
    }

    private fun setupViews() {
        binding.roomsRecycler.adapter = roomAdapter
        roomAdapter.items = HomeRoomEntity.getRooms()
    }

    private fun setupClicks() {
        binding.apply {
            setting.setOnClickListener {

                findNavController().navigate(R.id.toSetupFragment)
            }
            drawer.setOnClickListener {
                mainActivityListener.openDrawer()
            }
        }
        roomAdapter.onItemClickListener = {
            when {
                it.type?.key != null -> {
                    val bundle = Bundle()
                    bundle.putString("EXTRA_ROOM", it.type.key)
                    findNavController().navigate(R.id.toRoomFragment, bundle)
                }
                it.name == R.string.indices -> {
                    findNavController().navigate(R.id.toPatientFragment)
                }
                else -> {
                    requireActivity().showToast("in development")
                }
            }
        }
    }
}