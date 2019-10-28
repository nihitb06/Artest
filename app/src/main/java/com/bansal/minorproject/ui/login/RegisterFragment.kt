package com.bansal.minorproject.ui.login


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.bansal.minorproject.R
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.textSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_register_fragment_to_login_fragment)
        }
        return view
    }


}
