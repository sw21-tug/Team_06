package com.team06.focuswork.ui.logout

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.team06.focuswork.R
import com.team06.focuswork.databinding.FragmentOverviewBinding
import com.team06.focuswork.ui.login.LoginActivity
import com.team06.focuswork.ui.login.LoginViewModel
import com.team06.focuswork.ui.login.LoginViewModelFactory

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
                setPositiveButton(
                        R.string.logout_dialog_confirm,
                        DialogInterface.OnClickListener { dialog, _ ->
                            onConfirmLogout()
                            dialog.dismiss()
                        })

                setNegativeButton(
                        R.string.logout_dialog_cancel,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })
            }

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.logout_dialog_description)
                    .setTitle(R.string.logout_dialog_title)

            // Create the AlertDialog
            builder.create()
        }

        deleteDialog?.show()


        return binding.root
    }

    private fun onConfirmLogout(){
        var logoutViewModel = ViewModelProvider(this, LogoutViewModelFactory())
                .get(LogoutViewModel::class.java)

        logoutViewModel.logout()

        val mainIntent = Intent(activity, LoginActivity::class.java)
        activity?.startActivity(mainIntent)
        activity?.finish()
    }
}