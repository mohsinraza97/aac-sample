package com.mohsinsyed.aac_sample.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.mohsinsyed.aac_sample.R
import com.mohsinsyed.aac_sample.databinding.ActivityMainBinding
import com.mohsinsyed.aac_sample.ui.view_models.BaseViewModel
import com.mohsinsyed.aac_sample.ui.view_models.PostViewModel
import com.mohsinsyed.aac_sample.utils.extensions.hideKeyboard
import com.mohsinsyed.aac_sample.utils.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val postViewModel by viewModels<PostViewModel>()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpObservers() {
        postViewModel.isLoading.observe(this, {
            binding?.flLoader?.apply {
                visibility = if (it) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
        postViewModel.uiEvents.observe(this, { event ->
            when (event) {
                is BaseViewModel.UIEvents.ShowMessage -> binding?.root?.showSnackBar(event.message)
                is BaseViewModel.UIEvents.HideKeyboard -> hideKeyboard()
            }
        })
    }
}