package com.kishan.ui.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.ContactItemBinding
import com.kishan.data.remote.model.response.contact.ContactResponse
import java.util.*

/**
 * Created by Jeetesh Surana.
 */
class ContactAdapter(
    var mList: ArrayList<ContactResponse>, var mItemClickListener: ItemClickListener
) : RecyclerView.Adapter<ContactAdapter.CommonAdapterViewHolder>() {
    var binding: ContactItemBinding? = null
    var filterList = ArrayList<ContactResponse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonAdapterViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_contacts, parent, false
        )
        return CommonAdapterViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: CommonAdapterViewHolder, position: Int) {
        val list = filterList[position]
        holder.bindData(list)
        holder.itemView.setOnClickListener {
            mItemClickListener.itemClick(position, list)
        }
    }

    class CommonAdapterViewHolder(var binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(list: ContactResponse) = binding.apply {
            mData = list
            executePendingBindings()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<ContactResponse>) {
        mList.clear()
        mList.addAll(list)
        filterList.clear()
        filterList.addAll(mList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        mItemClickListener.noData(false)
        filterList.clear()
        if (TextUtils.isEmpty(query)) {
            filterList.addAll(mList)
        } else {
            for ((i, user) in mList.withIndex()) {
                if (user.first_name?.lowercase(Locale.getDefault())
                        ?.contains(query.lowercase(Locale.getDefault())) == true || user.last_name?.lowercase(
                        Locale.getDefault()
                    )?.contains(query.lowercase(Locale.getDefault())) == true
                ) {
                    filterList.add(user)
                }
                if (i == mList.size - 1) {
                    mItemClickListener.noData(filterList.size < 1)
                    notifyDataSetChanged()
                }
            }
        }
        notifyDataSetChanged()
    }


    interface ItemClickListener {
        fun itemClick(position: Int, contact: ContactResponse)
        fun noData(show: Boolean)
    }
}
