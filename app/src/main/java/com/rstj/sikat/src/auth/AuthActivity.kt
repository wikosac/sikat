package com.rstj.sikat.src.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.rstj.sikat.MainActivity
import com.rstj.sikat.R
import com.rstj.sikat.src.ViewModelFactory
import com.rstj.sikat.src.auth.ui.LoginFragment

class AuthActivity : AppCompatActivity() {

    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTokenPref().observe(this) {
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                setContentView(R.layout.activity_auth)
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            }
        }
    }
}