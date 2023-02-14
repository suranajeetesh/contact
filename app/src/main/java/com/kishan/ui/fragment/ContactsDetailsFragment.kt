package com.kishan.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.FragmentContactsDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kishan.core.uI.BaseFragment
import com.kishan.data.remote.model.response.MenuResponse
import com.kishan.data.remote.model.response.contact.ContactResponse
import com.kishan.ui.activity.MainActivity
import com.kishan.util.extensionFunction.getSMSBody
import com.kishan.util.extensionFunction.hide
import com.kishan.util.extensionFunction.sendSms
import com.kishan.util.extensionFunction.show
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Jeetesh Surana.
 */

@AndroidEntryPoint
class ContactsDetailsFragment: BaseFragment() {
    private var mBottomSheetDialog: BottomSheetDialog?=null
    private lateinit var binding: FragmentContactsDetailsBinding

    private var mList = ArrayList<MenuResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentContactsDetailsBinding.inflate(layoutInflater, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("contact",ContactResponse::class.java)?.let{
                binding.mData = it
            }
        }else{
            arguments?.getParcelable<ContactResponse>("contact")?.let{
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
            imgCall.setOnClickListener {
                upcomingToast()
            }
            txtCall.setOnClickListener {
                upcomingToast()
            }
            imgNotification.setOnClickListener {
                upcomingToast()
            }
            txtNotification.setOnClickListener {
                upcomingToast()
            }
            txtMoreActions.setOnClickListener {
                upcomingToast()
            }
            txtMessage.setOnClickListener {
                bottomSide()
            }
            imgEdit.setOnClickListener {
                bottomSide()
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

    private fun bottomSide() {
        mBottomSheetDialog = BottomSheetDialog(requireActivity(),R.style.CustomBottomSheetDialogTheme)
        val sheetView = activity?.layoutInflater?.inflate(R.layout.confirmation_dialog, null)
        sheetView?.let { mBottomSheetDialog?.setContentView(it) }

        val yesBtn = sheetView?.findViewById<AppCompatTextView>(R.id.txt_yes)
        val noBtn = sheetView?.findViewById<AppCompatTextView>(R.id.txt_no)

        yesBtn?.setOnClickListener {
            sendSms(resources.getString(R.string.dummy_phone_number),"+19376321367",getSMSBody(),resources.getString(R.string.ACCOUNT_SID),resources.getString(R.string.AUTH_TOKEN))
            mBottomSheetDialog?.dismiss()
        }

        noBtn?.setOnClickListener {
            mBottomSheetDialog?.dismiss()
        }
        mBottomSheetDialog?.show()
    }
}