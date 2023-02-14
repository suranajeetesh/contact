package com.kishan.ui.activity

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.newbasicstructure.R
import com.example.newbasicstructure.databinding.ActivityMainBinding
import com.kishan.ui.fragment.ContactsFragment
import com.kishan.ui.fragment.SMSFragment
import com.kishan.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : com.kishan.core.uI.BaseActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private var contactsFragment: ContactsFragment? = null
    private var smsFragment: SMSFragment? = null
    private var activateFragment: Fragment? = null
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        //        lifecycleScope.launch {
        //            homeViewModel.getData() //            sendSms("+91 9057516113","+19376321367",getSMSBody(),resources.getString(R.string.ACCOUNT_SID),resources.getString(R.string.AUTH_TOKEN))
        //        }
        initFragment()
        initObserver()
    }

    private fun initObserver() {
        homeViewModel.mError.observe(this) {
            if (it.message.isNotEmpty()) {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun initFragment() {
        binding.apply {
            bottomNavigation.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bn_contacts -> {
                        moveContacts()
                        return@setOnItemSelectedListener true
                    }
                    R.id.bn_sms -> {
                        moveSms()
                        return@setOnItemSelectedListener true
                    }
                }
                false
            }
            if (supportFragmentManager.findFragmentById(R.id.fl_container) == null) {
                binding.bottomNavigation.selectedItemId = R.id.bn_contacts
            }
        }
    }


    private fun moveContacts() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (contactsFragment == null) {
            contactsFragment = ContactsFragment()
            beginTransaction.add(R.id.fl_container, contactsFragment!!, ContactsFragment::class.java.simpleName)
        }
        contactsFragment?.let { beginTransaction.show(it) }
        smsFragment?.let { beginTransaction.hide(it) }
        beginTransaction.commit()
        activateFragment = contactsFragment
    }

    private fun moveSms() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (smsFragment == null) {
            smsFragment = SMSFragment()
            beginTransaction.add(R.id.fl_container, smsFragment!!, SMSFragment::class.java.simpleName)
        }
        smsFragment?.let { beginTransaction.show(it) }
        contactsFragment?.let { beginTransaction.hide(it) }
        beginTransaction.commit()
        activateFragment = smsFragment
    }

    override fun onPause() {
        homeViewModel.mError.value = com.kishan.data.remote.model.ErrorModel()
        super.onPause()
    }

    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressedTime > 2000) {
            backPressedTime = now
            backToast = Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT)
            backToast.show()
        } else {
            backToast.cancel()
            super.onBackPressed()
        }
    }
}