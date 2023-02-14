package com.kishan.ui.fragment

/**
 * Created by Jeetesh Surana.
 */
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.FragmentContactsBinding
import com.kishan.core.uI.BaseFragment
import com.kishan.data.remote.model.response.contact.ContactResponse
import com.kishan.ui.adapter.ContactAdapter
import com.kishan.util.extensionFunction.addReplaceFragment
import com.kishan.util.extensionFunction.show
import com.kishan.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentContactsBinding

    var mList = ArrayList<ContactResponse>()
    private var mAdapter: ContactAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initRecyclerView()
        init()
    }

    private fun init() {
        lifecycleScope.launch {
            homeViewModel.getData()
        }
        binding.apply {
            edtSearch.doAfterTextChanged {
                Log.e("TAG","init() --> ${it.toString()}")
                mAdapter?.filter(it.toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        homeViewModel.contactList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mAdapter?.updateList(it)
            }
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvContact.layoutManager = layoutManager
        mAdapter = ContactAdapter(mList, object : ContactAdapter.ItemClickListener {
                override fun itemClick(position: Int, contact: ContactResponse) {
                    activity?.addReplaceFragment(
                        R.id.fl_container, ContactsDetailsFragment().apply {
                            arguments = getArgument(contact)
                        }, addFragment = true, addToBackStack = true
                    )
                }

            override fun noData(show: Boolean) {
                binding.layoutNoData.show(show)
            }
        })
        binding.rvContact.adapter = mAdapter
    }

    private fun getArgument(contact: ContactResponse): Bundle {
        return Bundle().apply {
            putParcelable("contact", contact)
        }
    }
}