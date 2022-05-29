package com.tamara.care.watch.presentation.therapist

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
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentTherapistBinding
import com.tamara.care.watch.presentation.MainActivity
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.TherapistEmptyTimeAdapter
import com.tamara.care.watch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TherapistFragment : Fragment() {
    lateinit var binding: FragmentTherapistBinding

    private val viewModel: TherapistViewModel by viewModels()

    @Inject
    lateinit var therapistEmptyTimeAdapter: TherapistEmptyTimeAdapter

    lateinit var mainActivityListener: MainActivityListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_therapist, container, false)
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
        viewModel.getTherapistInfo()
        setupClicks()
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.emptyTimeRecycler.adapter = therapistEmptyTimeAdapter
    }

    private fun observeViewModel() {
        viewModel.therapistInfoLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    binding.apply {
                        if (state.data.items.isNotEmpty()) {
                            name.text = state.data.items.first().name
                            var languages = ""
                            state.data.items.first().languages.forEachIndexed { index, item ->
                                languages += if (index == state.data.items.first().languages.lastIndex) {
                                    item
                                } else {
                                    "$item, "
                                }
                            }
                            languagesList.text = languages
                            (activity as MainActivity).showIndicatorImage(state.data.items.first().activity?.first()?.status)
//                        Glide
//                            .with(requireActivity())
//                            .load(state.data.image)
//                            .centerCrop()
//                            .into(binding.photo)
                            val inactive =
                                state.data.items.first().activity?.filter { it.status == "INACTIVE" }
                            inactive?.let {
                                therapistEmptyTimeAdapter.items = (inactive.toMutableList())
                                therapistEmptyTimeAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                is ModelState.Error -> {
                    requireContext().showToast(state.error.errorMessage)

                }
                is ModelState.Loading -> {

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