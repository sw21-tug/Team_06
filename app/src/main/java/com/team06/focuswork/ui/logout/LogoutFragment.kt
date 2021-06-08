package com.team06.focuswork.ui.logout

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.team06.focuswork.R
import com.team06.focuswork.databinding.FragmentOverviewBinding
import com.team06.focuswork.ui.login.LoginActivity

class LogoutFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater, container, false)

        findNavController().navigate(R.id.nav_overview)

        val deleteDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.logout_dialog_confirm) { dialog, _ ->
                    onConfirmLogout()
                    dialog.dismiss()
                }
                setNegativeButton(R.string.logout_dialog_cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }

            builder.setMessage(R.string.logout_dialog_description)
                .setTitle(R.string.logout_dialog_title)

            builder.create()
        }

        deleteDialog?.show()

        return binding.root
    }

    private fun onConfirmLogout() {
        val logoutViewModel = ViewModelProvider(this, LogoutViewModelFactory())
            .get(LogoutViewModel::class.java)

        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("PASS").apply()
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove("USER").apply()

        logoutViewModel.logout()

        val mainIntent = Intent(activity, LoginActivity::class.java)
        activity?.startActivity(mainIntent)
        activity?.finish()
    }
}