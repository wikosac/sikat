package com.rstj.sikat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rstj.sikat.databinding.ActivityMainBinding
import com.rstj.sikat.src.ViewModelFactory
import com.rstj.sikat.src.auth.AuthActivity
import com.rstj.sikat.src.auth.AuthViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory by lazy { ViewModelFactory.getInstance(this) }
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        viewModel.getTokenPref().observe(this) {
            if (it == null) {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            } else {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                enableEdgeToEdge()
                ViewCompat.setOnApplyWindowInsetsListener(binding.container) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }
                window.statusBarColor = ContextCompat.getColor(this, R.color.md_theme_primaryContainer)
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                binding.navView.setupWithNavController(navController)
            }
        }
    }
}