package com.lee.oneweekonebook.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.lee.oneweekonebook.BuildConfig
import com.lee.oneweekonebook.R
import com.lee.oneweekonebook.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)
            .apply {

                buttonOpenSource.setOnClickListener {
                    requireContext().startActivity(
                        Intent(
                            requireContext(),
                            OssLicensesMenuActivity::class.java
                        )
                    )
                    OssLicensesMenuActivity.setActivityTitle("ㅁㅁ")
                }

                val appVersion = getString(R.string.setting_app_version, BuildConfig.VERSION_NAME)

                buttonAppVersion.text = appVersion
                buttonAppVersion.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(
                            "https://play.google.com/store/apps/details?id=com.lee.oneweekonebook")
                        setPackage("com.android.vending")
                    }
                    startActivity(intent)
                }
            }

        return binding.root
    }
}