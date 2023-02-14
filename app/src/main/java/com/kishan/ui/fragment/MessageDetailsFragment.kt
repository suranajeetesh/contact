package com.kishan.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.FragmentMessageDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kishan.core.uI.BaseFragment
import com.kishan.data.remote.model.response.MenuResponse
import com.kishan.data.remote.model.response.sms.Sms
import com.kishan.ui.activity.MainActivity
import com.kishan.util.extensionFunction.hide
import com.kishan.util.extensionFunction.show
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Jeetesh Surana.
 */

@AndroidEntryPoint
class MessageDetailsFragment: BaseFragment() {
    private var mBottomSheetDialog: BottomSheetDialog?=null
    private lateinit var binding: FragmentMessageDetailsBinding

    private var mList = ArrayList<MenuResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMessageDetailsBinding.inflate(layoutInflater, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("sms",Sms::class.java)?.let{
                binding.mData = it
            }
        }else{
            arguments?.getParcelable<Sms>("sms")?.let{
                binding.mData = it
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        mList.clear()
        mList.add(MenuResponse(getString(R.string.view_media),))
        mList.add(MenuResponse(getString(R.string.search_in_conversation),))
        mList.add(MenuResponse(getString(R.string.notifications),))
        binding.apply {
            imgBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
            imgOptions.setOnClickListener {
                upcomingToast()
            }
        }
    }

    private fun upcomingToast(){
        Toast.makeText(requireContext(), getString(R.string.in_development), Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.bottomNavigation.hide()
    }

    override fun onPause() {
        (activity as MainActivity).binding.bottomNavigation.show()
        super.onPause()
    }
}