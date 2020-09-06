package com.example.tierrasurfragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tierrasurfragments.databinding.FragmentConfiguracionBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ConfiguracionFragment : Fragment() {

    //Databinding
    private lateinit var binding: FragmentConfiguracionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentConfiguracionBinding>(
            inflater,
            R.layout.fragment_configuracion,
            container,
            false)

        //Cambio el nombre a la Action Bar
        (activity as AppCompatActivity).supportActionBar?.title ="Configuración"

        usuario()
        cerrarSesion()

        return binding.root
    }

    /** Obtengo los datos del usuario **/
    private fun usuario() {
        //Recupero los datos
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                //ID del proveedor
                val providerId = profile.providerId

                val uid = profile.uid

                //Obtengo el nombre, correo y foto
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl

                //Los incorporo al fragment
                binding.tvNombreUsuario.text = name
                binding.tvEmailUsuario.text = email
                Picasso.with(this.requireContext()).load(photoUrl).into(binding.profilePicture)
            }

        }


    }

    /**  Cierre de sesión **/
    private fun cerrarSesion() {

        binding.btnCerrarSesion.setOnClickListener {view: View ->

            //Via email
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            prefs?.clear()
            prefs?.apply()

            //Via Google
            FirebaseAuth.getInstance().signOut()
            val loginScreen = Intent(this.requireContext(), InicioActivity::class.java)
            startActivity(loginScreen)
        }

    }


}