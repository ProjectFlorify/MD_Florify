package com.example.florify.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.florify.databinding.FragmentProfileBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.repository.Repository
import com.example.florify.ui.forum.History
import com.example.florify.ui.update.Update
import com.example.florify.ui.welcome.onboarding.Welcome
import com.example.florify.viewmodelfactory.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var sharedPreferencesHelper: PreferencesHelper
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferencesHelper = PreferencesHelper(requireContext())
        val apiService = ApiConfig.getApiClient()
        val repository = Repository(apiService)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, sharedPreferencesHelper)
        )[ProfileViewModel::class.java]

        binding.logout.setOnClickListener {
            showConfirmationDialog("Are you sure you want to logout?") {
                handleLogout()
            }
        }

        binding.deleteAccount.setOnClickListener {
            showConfirmationDialog("Are you sure you want to delete your account? This action cannot be undone.") {
                handleLogout()
                deleteAccount()
            }
        }

        binding.deleteAllHistory.setOnClickListener {
            showConfirmationDialog("Are you sure you want to delete all history?") {
                deleteAllHistory()
            }
        }

        binding.updateAccount.setOnClickListener {
            val intent = Intent(requireContext(), Update::class.java)
            startActivity(intent)
        }

        binding.history.setOnClickListener {
            val intent = Intent(requireContext(), History::class.java)
            startActivity(intent)
        }

        getUser()
        loading()
        return root
    }

    private fun getUser() {
        viewModel.getUser()
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.name.text = it.userData?.name
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showConfirmationDialog(message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteAllHistory() {
        viewModel.deleteAllHistory()
        viewModel.deleteAll.observe(viewLifecycleOwner) {
            it?.let { result ->
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Failed to delete all history",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteAccount() {
        viewModel.deleteAccount()
        viewModel.deleteAccount.observe(viewLifecycleOwner) { result ->
            result?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                if (it.error == null) {
                    navigateToWelcome()
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleLogout() {
        if (sharedPreferencesHelper.getSession().isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Session Doesn't Exist", Toast.LENGTH_SHORT).show()
        } else {
            sharedPreferencesHelper.removeSession()
            navigateToWelcome()
        }
    }

    private fun navigateToWelcome() {
        val intent = Intent(requireContext(), Welcome::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
