package com.example.florify.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.florify.databinding.FragmentProfileBinding
import com.example.florify.preferences.PreferencesHelper
import com.example.florify.ui.welcome.Welcome

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferencesHelper: PreferencesHelper by lazy {
            PreferencesHelper(requireContext())
        }

        binding.logout.setOnClickListener {
            if(sharedPreferencesHelper.getSession().isNullOrEmpty()){
                Toast.makeText(requireContext(), "Session Does'nt Exist", Toast.LENGTH_SHORT).show()
            }else{
                sharedPreferencesHelper.removeSession()
                startActivity(Intent(requireContext(), Welcome::class.java))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}