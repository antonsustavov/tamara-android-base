package com.tamara.care.watch.presentation.proximity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.ProximityItemEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentProximityBinding
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.ProxAdapter
import com.tamara.care.watch.utils.getDateFromServerTime
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProximityFragment : Fragment() {

    lateinit var binding: FragmentProximityBinding

    @Inject
    lateinit var proxAdapter: ProxAdapter

    private val viewModel: ProximityViewModel by viewModels()
    private lateinit var mainActivityListener: MainActivityListener

    private lateinit var previousClosestBeaconDate: Date

    //paging part
    private var offset = 0
    private var isLoadingNow = true
    private var isAllLoaded = false
    private var countItems = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_proximity, container, false)
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

        setupProximityRecyclerView()
        setupRefreshListener()
        getProximityInfo()
        setupViewModelCallbacks()
        setupClicks()
    }

    private fun setupProximityRecyclerView() {
        binding.proximityRecycler.apply {
            adapter = proxAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = binding.proximityRecycler.layoutManager as LinearLayoutManager?
                    if (proxAdapter.itemCount > 1 &&
                        (proxAdapter.itemCount - (layoutManager?.findLastCompletelyVisibleItemPosition() ?: 0) < 7)
                        && proxAdapter.itemCount < countItems && !isLoadingNow && !isAllLoaded
                    ) {
                        isLoadingNow = true
                        offset += 20
                        getProximityInfo()
                    }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupRefreshListener() {
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            isLoadingNow = true
            isAllLoaded = false
            offset = 0

            proxAdapter.items.clear()
            proxAdapter.notifyDataSetChanged()

            getProximityInfo()
        }
    }

    private fun getProximityInfo() {
        viewModel.getProximityInfo(offset = offset)
    }

    @SuppressLint("SetTextI18n")
    private fun setupViewModelCallbacks() {
        viewModel.proximityInfoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ModelState.Success -> {
                    isLoadingNow = false

                    if (it.data.items.isEmpty()) {
                        isAllLoaded = true
                    }

                    if (it.data.items.isNotEmpty()) {
                        if (countItems == -1) {
                            countItems = it.data.count
                            it.data.items.first().dateTime.getDateFromServerTime()?.let { date ->
                                previousClosestBeaconDate = date
                            }
                        }

                        val date = it.data.items.first().dateTime.getDateFromServerTime()

                        if (::previousClosestBeaconDate.isInitialized) {
                            if (previousClosestBeaconDate <= date) {
                                if (!it.data.items.first().closestRoom.isNullOrEmpty()) {
                                    binding.latestBeacon.text =
                                        "${it.data.items.first().closestRoom} (${it.data.items.first().closestRoomSignal})"
                                }
                                date?.let {
                                    binding.latestBeaconTime.text = SimpleDateFormat(
                                        "EEE, MMM d, HH:mm",
                                        Locale.ENGLISH
                                    ).format(date)
                                }
                            }
                        }

                        val items: MutableList<ProximityItemEntity> = it.data.items.toMutableList()
                        items.removeAt(0)

                        val previousListSize = proxAdapter.items.size

                        proxAdapter.items.addAll(items)
                        proxAdapter.notifyItemRangeInserted(previousListSize, items.size)

                    }
                }
                is ModelState.Error -> {
                    //binding.loaderVisibility = false
                    requireContext().showToast(it.error.errorMessage)
                }
                is ModelState.Loading -> {
                    //binding.loaderVisibility = true
                }
            }
        }
    }

    private fun setupClicks() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.drawer.setOnClickListener {
            mainActivityListener.openDrawer()
        }
    }
}