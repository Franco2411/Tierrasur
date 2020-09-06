package com.example.tierrasurfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tierrasurfragments.databinding.ActivityInicioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioActivity : AppCompatActivity() {

    //Databinging
    private lateinit var binding: ActivityInicioBinding

    //Firebase Authenticacion
    //private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        //Splash Screen
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityInicioBinding>(this, R.layout.activity_inicio)

        //Inicializo la autenticacion por Firebase
        //auth = Firebase.auth

        //Botón de arriba
        val navController2 = this.findNavController(R.id.myNavHost2)
        //Para anclarlo a la appbar
        NavigationUI.setupActionBarWithNavController(this, navController2)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController2 = this.findNavController(R.id.myNavHost2)
        return navController2.navigateUp()
    }


}