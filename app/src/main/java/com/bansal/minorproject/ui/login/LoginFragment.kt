package com.bansal.minorproject.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bansal.minorproject.R
import com.bansal.minorproject.data.FireBaseAccess
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseAccess = FireBaseAccess().fireBaseAccess
    private val authStateListener = firebaseAccess.authStateListener

    init {
        firebaseAccess.navigate = FireBaseAccess.Navigate {
            findNavController().navigate(com.bansal.minorproject.R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.bansal.minorproject.R.layout.fragment_login, container, false)

        view.btnSignIn.setOnClickListener {
            firebaseAccess.loginUser(context, arrayOf(view.etEmail.text.toString(), view.etPassword.text.toString()))
        }

        val client = firebaseAccess.getGoogleApiClient(view)
        client.connect()

        view.fabGoogle.setOnClickListener {
            activity?.startActivityForResult(
                Auth.GoogleSignInApi.getSignInIntent(client),
                RC_SIGN_IN
            )
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        if(authStateListener != null)
            FirebaseAuth.getInstance().signOut()
        mAuth.addAuthStateListener { authStateListener }
    }

    override fun onStop() {
        if(authStateListener  != null)
            mAuth.removeAuthStateListener { authStateListener }

        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            firebaseAccess.handleSignInResult(
                Auth.GoogleSignInApi.getSignInResultFromIntent(data),
                context
            )
        }
    }

    override fun onPause() {
        super.onPause()
        val client = firebaseAccess.getGoogleApiClient(view)
        client.stopAutoManage(context as FragmentActivity)
        client.disconnect()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()

        private const val RC_SIGN_IN = 1
    }
}
