package com.tamara.care.watch.presentation.dialog

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.RoomPeriod
import com.tamara.care.watch.databinding.FragmentDialogBottomFilterBinding
import java.text.SimpleDateFormat
import java.util.*


class FilterBottomDialogFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentDialogBottomFilterBinding

    companion object {
        const val EXTRA_ROOM_PERIOD = "EXTRA_ROOM_PERIOD"
        const val EXTRA_ROOM_DATE = "EXTRA_ROOM_DATE"
    }

    val PATTERN_DISPLAY = "MM/dd/yyyy"
    val PATTERN_SERVER = "yyyy-MM-dd"

    private val fromCalendar: Calendar = Calendar.getInstance()
    private val untilCalendar: Calendar = Calendar.getInstance()

    private val displayFormat = SimpleDateFormat(PATTERN_DISPLAY, Locale.getDefault())
    private val serverFormat = SimpleDateFormat(PATTERN_SERVER, Locale.getDefault())

    private var fromValue = ""
    private var untilValue = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_bottom_filter,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnFilter.setOnClickListener {
                if (fromValue < untilValue) {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        EXTRA_ROOM_DATE,
                        listOf(fromValue, untilValue)
                    )
                }
                dismiss()
            }
            last20Days.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    EXTRA_ROOM_PERIOD,
                    RoomPeriod.LAST_20_DAYS
                )
                dismiss()
            }
            previousMonth.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    EXTRA_ROOM_PERIOD,
                    RoomPeriod.PREVIOUS_MONTH
                )
                dismiss()
            }
            thisMonth.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    EXTRA_ROOM_PERIOD,
                    RoomPeriod.CURRENT_MONTH
                )
                dismiss()
            }
            from.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    fromDate,
                    fromCalendar[Calendar.YEAR],
                    fromCalendar[Calendar.MONTH],
                    fromCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
            until.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    untilDate,
                    untilCalendar[Calendar.YEAR],
                    untilCalendar[Calendar.MONTH],
                    untilCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        }
    }

    var fromDate =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            fromCalendar[Calendar.YEAR] = year
            fromCalendar[Calendar.MONTH] = monthOfYear
            fromCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            fromValue = serverFormat.format(fromCalendar.time)
            if (untilValue > fromValue) {
                binding.from.text = displayFormat.format(fromCalendar.time)
            } else if (untilValue.isEmpty()) {
                binding.from.text = displayFormat.format(fromCalendar.time)
            }
        }

    var untilDate =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            untilCalendar[Calendar.YEAR] = year
            untilCalendar[Calendar.MONTH] = monthOfYear
            untilCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            untilValue = serverFormat.format(untilCalendar.time)
            if (untilValue > fromValue) {
                binding.until.text = displayFormat.format(untilCalendar.time)
            } else if (fromValue.isEmpty()) {
                binding.until.text = displayFormat.format(untilCalendar.time)
            }
        }
}