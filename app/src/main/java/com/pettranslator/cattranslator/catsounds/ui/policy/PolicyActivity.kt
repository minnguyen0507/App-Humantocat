package com.pettranslator.cattranslator.catsounds.ui.policy

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pettranslator.cattranslator.catsounds.R
import com.pettranslator.cattranslator.catsounds.bases.BaseActivity
import com.pettranslator.cattranslator.catsounds.databinding.ActivityPolicyBinding
import com.pettranslator.cattranslator.catsounds.utils.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyActivity : BaseActivity<ActivityPolicyBinding>() {

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPolicyBinding =
        ActivityPolicyBinding.inflate(inflater)

    override fun initialize() {
        enableEdgeToEdge()
        viewBinding.btnBack.setSafeOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}