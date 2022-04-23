package com.test.ocbc.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.test.ocbc.data.source.prefs.UserPreferences
import com.test.ocbc.view.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {
    lateinit var progressDialog: ProgressDialog
    lateinit var prefs: UserPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressDialog = ProgressDialog(context)
        prefs = UserPreferences(context)
    }
}