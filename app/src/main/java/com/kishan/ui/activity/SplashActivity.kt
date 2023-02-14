package com.kishan.ui.activity

import android.content.Intent
import android.os.Bundle

class SplashActivity : com.kishan.core.uI.BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent = Intent(
            this,
            MainActivity::class.java
        ).apply {
            intent.extras?.let { putExtras(it) }
        }
        startActivity(intent)
        finish()
    }
}
