package com.tamara.care.watch.presentation.patient

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentPatientBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.MainActivity
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.PhysicalParametersAdapter
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PatientFragment : Fragment() {

    private val binding by lazy {
        FragmentPatientBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var physicalParametersAdapter: PhysicalParametersAdapter

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val patientViewModel: PatientViewModel by viewModels()

    private lateinit var mainActivityListener: MainActivityListener

    //paging part
    private var offset = 0
    private var isLoadingNow = true
    private var countItems = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = (context as MainActivityListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityListener.showBottomNavBar()

        getPatientInfo()
        getPhysicalParameters()
        setupRecyclerView()
        setupViewModelCallbacks()
        setupClicks()
    }

    private fun getPhysicalParameters() {
        patientViewModel.getPhysicalParameters(
            offset = offset,
            transmitterId = sharedPreferencesManager.transmitterId
        )
    }

    private fun getPatientInfo() {
        patientViewModel.getPatientInfo()
    }

    private fun setupRecyclerView() {
        binding.physicalParametersRecyclerView.apply {
            adapter = physicalParametersAdapter
            layoutManager = LinearLayoutManager(requireContext())

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = binding.physicalParametersRecyclerView.layoutManager as LinearLayoutManager?
                    if (physicalParametersAdapter.itemCount > 1 &&
                        (physicalParametersAdapter.itemCount - (layoutManager?.findLastCompletelyVisibleItemPosition() ?: 0) < 7)
                        && physicalParametersAdapter.itemCount < countItems && !isLoadingNow
                    ) {
                        isLoadingNow = true
                        offset += 20
                        getPhysicalParameters()
                    }
                }
            })
        }
    }

    private fun setupViewModelCallbacks() {
        patientViewModel.apply {
            physicalParameterLiveData.observe(viewLifecycleOwner) { modelState ->
                when (modelState) {
                    is ModelState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        isLoadingNow = false

                        if (countItems == -1) {
                            countItems = modelState.data.count

                        }

                        if (modelState.data.items.isNotEmpty()) {
                            val previousListSize = physicalParametersAdapter.physicalParametersList.size
                            physicalParametersAdapter.physicalParametersList.addAll(modelState.data.items)
                            physicalParametersAdapter.notifyItemRangeInserted(previousListSize, modelState.data.items.size)
                        }
                    }
                    is ModelState.Error -> {
                        isLoadingNow = false

                        binding.progressBar.visibility = View.GONE
                        requireContext().showToast(modelState.error.errorMessage)
                    }
                    is ModelState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
            therapistInfoLiveData.observe(viewLifecycleOwner) { modelState ->
                when (modelState) {
                    is ModelState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (modelState.data.items.isNotEmpty()) {
                            binding.name.text = modelState.data.items.first().name

                            (activity as MainActivity).showIndicatorImage(modelState.data.items.first().activity?.first()?.status)
                        }
                    }
                    is ModelState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        requireContext().showToast(modelState.error.errorMessage)
                    }
                    is ModelState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
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
        }
    }
}