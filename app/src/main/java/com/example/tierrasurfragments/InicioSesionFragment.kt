package com.example.tierrasurfragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.navigation.findNavController
import com.example.tierrasurfragments.databinding.FragmentInicioSesionBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class InicioSesionFragment : Fragment() {

    private lateinit var binding: FragmentInicioSesionBinding

    //Constante para google
    private val GOOGLE_SIGN_IN = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentInicioSesionBinding>(
            inflater,
            R.layout.fragment_inicio_sesion,
            container,
            false
        )
        //Cambio el nombre de la ActionBar
        (activity as AppCompatActivity).supportActionBar?.title = "Inicio de Sesión"

        botones()
        session()
        return binding.root

    }

    /** Metodos botones **/
    @SuppressLint("CommitPrefEdits")
    private fun botones() {

        //Botón de registro
        binding.btnRegistro.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_inicioSesionFragment_to_registroFragment)
        }

        //Botón iniciar sesión
        binding.btnInicio.setOnClickListener { view: View ->
            if (binding.txtUsuario.text.isEmpty() || binding.txtPassword.text.isEmpty()) {
                Toast.makeText(
                    this.requireActivity(),
                    "Debe de llenar todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.txtUsuario.text.toString(),
                        binding.txtPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val home1 =
                                Intent(this.requireContext(), MainActivity::class.java).apply {
                                    putExtra("email", binding.txtUsuario.text.toString())
                                }

                            startActivity(home1)
                        } else {
                            showAlert("Error!!", "Usuario y/o contraseña incorrectos.")
                        }
                    }
            }
        }

        //Inicio con Google
        binding.btnGoogle.setOnClickListener { view: View ->

            //Configuracion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this.requireContext(), googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    /** Función cartel **/
    private fun showAlert(titulo: String, msg: String) {
        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setTitle(titulo)
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val home1 =
                                    Intent(this.requireContext(), MainActivity::class.java).apply {
                                        putExtra("email", account.email ?: "")
                                    }

                                startActivity(home1)

                            } else {
                                showAlert("Error!!", "No se pudo iniciar sesión.")
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert(
                    "Error!!",
                    "No se pudo conectar con la base, revise su conexión y vuelva a intentar."
                )
            }


        }
    }

    /** Mantener sesion activa **/
    private fun session() {
        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs?.getString("email", null)

        if (email != null) {
            binding.authLayout.visibility = View.INVISIBLE
            val home1 = Intent(activity, MainActivity::class.java).apply {
                putExtra("email", email)
            }
            startActivity(home1)
        }

    }


}