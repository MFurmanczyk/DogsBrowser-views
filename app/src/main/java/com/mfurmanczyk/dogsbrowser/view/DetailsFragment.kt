package com.mfurmanczyk.dogsbrowser.view

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mfurmanczyk.dogsbrowser.R
import com.mfurmanczyk.dogsbrowser.databinding.FragmentDetailsBinding
import com.mfurmanczyk.dogsbrowser.databinding.SendSmsDialogBinding
import com.mfurmanczyk.dogsbrowser.model.SMSInfo
import com.mfurmanczyk.dogsbrowser.viewmodel.DetailsViewModel
import kotlinx.coroutines.launch

private const val TAG = "DetailsFragment"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<DetailsViewModel>(R.id.dog_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val dogUuid = DetailsFragmentArgs.fromBundle(it).dogUuid
            Log.i(TAG, "onViewCreated: $dogUuid")
            viewModel.getDog(dogUuid)
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_send_sms -> {
                        viewModel.switchSendSms(true)
                        (activity as MainActivity).checkSmsPermission()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.dog = it.dog
                }
            }
        }
    }

    fun onPermissionResult(permissionGranted: Boolean) {

        if (viewModel.uiState.value.sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SMSInfo(
                    "",
                    "${viewModel.uiState.value.dog?.dogBreed} is bred for ${viewModel.uiState.value.dog?.bredFor}. Its lifespan is about ${viewModel.uiState.value.dog?.lifespan}.",
                    viewModel.uiState.value.dog?.imageUrl
                )
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                dialogBinding.smsInfo = smsInfo
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton(getString(R.string.send_sms)) { _, _ ->
                        if(!dialogBinding.smsDestination.text.isNullOrEmpty()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->

                    }
                    .show()
            }
        }
    }


    private fun sendSms(smsInfo: SMSInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val smsManager = ContextCompat.getSystemService((activity as FragmentActivity), SmsManager::class.java)
            val sms = smsManager?.createForSubscriptionId(SmsManager.getDefaultSmsSubscriptionId())
            sms?.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null)
            Toast.makeText(context, "SMS sent to: ${smsInfo.to}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Unable to send sms. Android version < ${Build.VERSION_CODES.S}", Toast.LENGTH_SHORT).show()
        }
    }
}