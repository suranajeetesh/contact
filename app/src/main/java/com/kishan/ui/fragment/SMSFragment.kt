package com.kishan.ui.fragment

/**
 * Created by Jeetesh Surana.
 */
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.FragmentSmsBinding
import com.kishan.data.remote.model.response.sms.Sms
import com.kishan.ui.adapter.SMSAdapter
import com.kishan.util.RequestCodeUtil
import com.kishan.util.extensionFunction.*
import com.kishan.util.helper.MarshMellowHelper
import com.kishan.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SMSFragment : com.kishan.core.uI.BaseFragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentSmsBinding
    private var marshMellowHelper: MarshMellowHelper? = null

    var mList = ArrayList<Sms>()
    private var mAdapter: SMSAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSmsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        init()
        initRecyclerView()
    }

    private fun init(){
        if (!checkPermission(requireActivity(), CheckPermission.SMS)) {
            initSMSPermission()
        } else {
            readSms()
        }
        binding.apply {
            edtSmsSearch.doAfterTextChanged {
                mAdapter?.filter(it.toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        homeViewModel.sms.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mAdapter?.updateList(it)
            }
        }
    }

    private fun initSMSPermission() {
        marshMellowHelper = MarshMellowHelper(
            this, smsPermission,
            RequestCodeUtil.PERMISSIONS_SMS_REQUEST_CODE
        )
        marshMellowHelper?.request(object : MarshMellowHelper.PermissionCallback {
            override fun onPermissionGranted() {
                readSms()
            }

            override fun onPermissionDenied(permissionDeniedError: String) {
                Toast.makeText(requireContext(), permissionDeniedError, Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDeniedBySystem(permissionDeniedBySystem: String) {
                Toast.makeText(requireContext(), permissionDeniedBySystem, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun readSms() {
        lifecycleScope.launch {
            activity?.application?.let { homeViewModel.getSms(it) }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvSms.layoutManager = layoutManager
        mAdapter = SMSAdapter(mList, object : SMSAdapter.ItemClickListener {
            override fun itemClick(position: Int, sms: Sms) {
                activity?.addReplaceFragment(
                    R.id.fl_container, MessageDetailsFragment().apply {
                        arguments = getArgument(sms)
                    }, addFragment = true, addToBackStack = true
                )
            }

            override fun noData(show: Boolean) {
                binding.layoutNoData.show(show)
            }
        })
        binding.rvSms.adapter = mAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        marshMellowHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getArgument(sms: Sms): Bundle {
        return Bundle().apply {
            putParcelable("sms", sms)
        }
    }
}