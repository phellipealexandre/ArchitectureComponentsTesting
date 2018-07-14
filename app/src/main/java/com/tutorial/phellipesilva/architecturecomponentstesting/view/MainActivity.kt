package com.tutorial.phellipesilva.architecturecomponentstesting.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.tutorial.phellipesilva.architecturecomponentstesting.R
import com.tutorial.phellipesilva.architecturecomponentstesting.databinding.ActivityMainBinding
import com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel.UserViewModel
import com.tutorial.phellipesilva.architecturecomponentstesting.userViewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        userViewModel = ViewModelProviders.of(this, UserViewModelFactory(this))
            .get(UserViewModel::class.java)

        initClickListeners()
        startObservingUser()
    }

    private fun initClickListeners() {
        btnReplaceUser.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val userId = edtUserIdForm.text.toString()
            userViewModel.replaceAndShowUserFromFetchedFromService(userId)
        }

        btnGetFromDb.setOnClickListener {
            val userId = edtGetUserFromDBId.text.toString()
            userViewModel.showStoredUserById(userId)
        }

        btnUpdatePhone.setOnClickListener {
            userViewModel.updateCurrentUserPhone(edtUserPhone.text.toString())
        }

        btnClearUsers.setOnClickListener {
            userViewModel.removeAllUsers()
            clearScreen()
        }
    }

    private fun startObservingUser() {
        userViewModel.getUser().observe(this, Observer {
            progressBar.visibility = View.GONE
            it?.let { activityMainBinding.user = it }
        })
    }

    private fun clearScreen() {
        activityMainBinding.user = null
    }
}
