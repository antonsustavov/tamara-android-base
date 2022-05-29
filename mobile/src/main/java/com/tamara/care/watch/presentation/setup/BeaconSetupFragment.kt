package com.tamara.care.watch.presentation.setup

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.minew.beaconplus.sdk.MTCentralManager
import com.minew.beaconplus.sdk.MTPeripheral
import com.tamara.care.watch.R
import com.tamara.care.watch.data.entity.BeaconRegisterEntity
import com.tamara.care.watch.data.entity.TransmitterEntity
import com.tamara.care.watch.data.model.ModelState
import com.tamara.care.watch.databinding.FragmentSetupBeaconBinding
import com.tamara.care.watch.manager.SharedPreferencesManager
import com.tamara.care.watch.presentation.MainActivityListener
import com.tamara.care.watch.presentation.adapter.BeaconSetupAdapter
import com.tamara.care.watch.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeaconSetupFragment : Fragment() {

    companion object {
        const val ROOMS_LIST = "ROOMS_LIST"
        const val EXTRA_UPDATE_BEACON = "EXTRA_UPDATE_BEACON"
        const val EXTRA_UPDATE_TRANSMITTER = "EXTRA_UPDATE_TRANSMITTER"
        const val EXTRA_TRANSMITTER = "EXTRA_TRANSMITTER"
        const val EXTRA_BEACON = "EXTRA_BEACON"
        const val BEACONS_MAX_COUNT = 6
    }

    private val binding by lazy {
        FragmentSetupBeaconBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var beaconSetupAdapter: BeaconSetupAdapter

    @Inject
    lateinit var permissionsUtils: PermissionsUtils

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Inject
    lateinit var bluetoothDialogFactory: BluetoothDialogFactory

    private lateinit var mainActivityListener: MainActivityListener

    private lateinit var mtCentralManager: MTCentralManager

    private val roomsList = arrayListOf<String>()

    private val viewModel: SetupViewModel by viewModels()

    private val beaconsBluetoothLiveData = MutableLiveData<List<MTPeripheral>>()

    //launchers
    private val enableBluetoothIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    checkPermissions()
                }

                Activity.RESULT_CANCELED -> {
                    activity?.let { bluetoothDialogFactory.createDialog(DialogType.NEED_BLUETOOTH_FOR_SCANNING, it).show() }
                }
            }
        }

    private val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantResults ->
        var allGranted = true
        for (result in grantResults) {
            if (!result.value) {
                allGranted = false
                break
            }
        }
        if (allGranted) {
            permissionsGranted()
        } else {
            activity?.let { bluetoothDialogFactory.createDialog(DialogType.LOCATION_PERMISSION_ERROR, it).show() }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = (context as MainActivityListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = this
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityListener.hideBottomNavBar()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<TransmitterEntity>(EXTRA_UPDATE_TRANSMITTER)
            ?.observe(viewLifecycleOwner) {
                setupTransmitter()
                checkPermissions()
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<BeaconRegisterEntity>(EXTRA_UPDATE_BEACON)
            ?.observe(viewLifecycleOwner) { item ->
                val adapterItem = beaconSetupAdapter.items.find { it.macAddress == item.macAddress }
                if (!adapterItem?.room.isNullOrEmpty()) {
                    roomsList.remove(adapterItem?.room)
                }
                adapterItem?.room = item.room
                adapterItem?._id = item._id
                roomsList.add(item.room)
                beaconSetupAdapter.notifyDataSetChanged()
            }
        if (sharedPreferencesManager.transmitterId.isNullOrEmpty()) {
            openTransmitterDialog()
        }

        setupRefreshLayout()
        observeViewModel()
        setupBeaconListener()
        setupTransmitter()
        setupClicks()
        setupRecycler()
    }

    private fun setupRefreshLayout() {
        binding.refresh.setOnRefreshListener {
            viewModel.getBeaconInfo()
            binding.refresh.isRefreshing = false
        }
    }

    private fun setupTransmitter() {
        if (sharedPreferencesManager.transmitterId.isNullOrEmpty()) {
            binding.transmitterIdTxt.text = requireContext().getString(R.string.enter_id_from_watch)
            binding.transmitterIdTxt.setTextColor(
                (ContextCompat.getColor(
                    binding.root.context,
                    R.color.red
                ))
            )
        } else {
            binding.transmitterIdTxt.setTextColor(
                (ContextCompat.getColor(
                    binding.root.context,
                    R.color.black
                ))
            )
            binding.transmitterIdTxt.text = sharedPreferencesManager.transmitterId
        }
    }

    private fun setupBeaconListener() {
        mtCentralManager = MTCentralManager.getInstance(requireContext())
    }

    private fun setupRecycler() {
        binding.beaconRecycler.adapter = beaconSetupAdapter

        beaconSetupAdapter.onItemClickListener = {
            if (!binding.progressBar.isVisible) {
                if (roomsList.size <= BEACONS_MAX_COUNT) {
                    val bundle = Bundle()
                    bundle.putParcelable(EXTRA_BEACON, it)
                    bundle.putStringArrayList(ROOMS_LIST, roomsList)
                    findNavController().navigate(R.id.beaconSetupDialogFragment, bundle)
                }
            } else {
                requireContext().showToast(getString(R.string.to_set_up_beacon_stop_scanning_process))
            }
        }
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = beaconSetupAdapter.items[viewHolder.bindingAdapterPosition]
                viewModel.deleteBeaconInfo(item._id ?: "-1")
                beaconSetupAdapter.items.removeAt(viewHolder.bindingAdapterPosition)
                roomsList.remove(item.room)
                beaconSetupAdapter.notifyDataSetChanged()
            }
        }
        val touchHelper = ItemTouchHelper(swipeHandler)
        touchHelper.attachToRecyclerView(binding.beaconRecycler)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.beaconsNetworkLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ModelState.Success -> {
                    val items = mutableListOf<BeaconRegisterEntity>()
                    beaconsBluetoothLiveData.value?.forEach { mtp ->
                        var roomName = requireContext().getString(R.string.link_the_room)
                        var id: String? = null
                        state.data.items.forEach {
                            if (it.macAddress == mtp.mMTFrameHandler.mac) {
                                roomName = it.room
                                id = it._id!!
                            }
                        }
                        items.add(
                            BeaconRegisterEntity(
                                _id = id,
                                room = roomName,
                                macAddress = mtp.mMTFrameHandler.mac,
                                transmitter = sharedPreferencesManager.transmitterId
                            )
                        )
                    }
                    beaconSetupAdapter.items = items
                    roomsList.clear()
                    items.forEach {
                        if (it.room.isNotEmpty()) {
                            roomsList.add(it.room)
                        }
                    }
                    beaconSetupAdapter.notifyDataSetChanged()
                }
                is ModelState.Error -> {
                    requireContext().showToast(state.error.errorMessage)
                }
                is ModelState.Loading -> {

                }
            }
        }
    }

    private fun trackBeacons() {
        mtCentralManager.setBluetoothChangedListener { }
        mtCentralManager.startScan()
        mtCentralManager.setMTCentralManagerListener { peripherals ->
            if (beaconsBluetoothLiveData.value?.size != 6) {
                beaconsBluetoothLiveData.value = peripherals
                viewModel.refreshValue()
            }
        }
    }

    private fun setupClicks() {
        binding.drawer.setOnClickListener {
            if (sharedPreferencesManager.transmitterId.isNullOrBlank()) {
                openTransmitterDialog()
            } else {
                mainActivityListener.openDrawer()
            }
        }

        binding.back.setOnClickListener {
            if (sharedPreferencesManager.transmitterId.isNullOrBlank()) {
                openTransmitterDialog()
            } else {
                activity?.onBackPressed()
            }
        }
        binding.transmitterCard.setOnClickListener {
            openTransmitterDialog()
        }

        binding.scanButton.setOnClickListener {
            if (binding.progressBar.isVisible) {
                binding.scanButtonTextView.text = requireContext().getString(R.string.scan_beacons)
                mtCentralManager.stopScan()
                binding.progressBar.visibility = View.GONE
            } else {
                when (viewModel.getBluetoothState()) {
                    BluetoothState.BluetoothEnabled -> {
                        if (!sharedPreferencesManager.transmitterId.isNullOrEmpty()) {
                            checkPermissions()
                        } else {
                            openTransmitterDialog()
                        }
                    }
                    BluetoothState.BluetoothDisabled -> {
                        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        enableBluetoothIntentLauncher.launch(enableBluetoothIntent)
                    }
                    BluetoothState.NoBluetoothAdapter -> {
                        activity?.let { checkedActivity ->
                            bluetoothDialogFactory.createDialog(DialogType.NO_BLUETOOTH_ADAPTER, checkedActivity).show()
                        }
                    }
                }
            }
        }
    }

    private fun checkPermissions() {
        val missingRequiredPermissions = permissionsUtils.missingRequiredBluetoothPermissions()
        if (missingRequiredPermissions.isEmpty()) {
            permissionsGranted()
        } else {
            requestPermissionsLauncher.launch(missingRequiredPermissions)
        }
    }

    private fun permissionsGranted() {
        if (locationServicesAreEnabled()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.scanButtonTextView.text = requireContext().getString(R.string.stop_scan_beacons)
            trackBeacons()
            viewModel.getBeaconInfo()
        } else {
            activity?.let { bluetoothDialogFactory.createDialog(DialogType.LOCATION_SERVICES_ARE_NOT_ENABLED, it).show() }
        }
    }

    private fun locationServicesAreEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isLocationEnabled
        } else {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            isGpsEnabled || isNetworkEnabled
        }
    }

    fun openTransmitterDialog() {
        val bundle = Bundle()
        bundle.putString(EXTRA_TRANSMITTER, sharedPreferencesManager.transmitterId)
        findNavController().navigate(R.id.transmitterSetupDialogFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mtCentralManager.stopScan()
    }
}