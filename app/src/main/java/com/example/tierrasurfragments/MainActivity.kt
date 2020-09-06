package com.example.tierrasurfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tierrasurfragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Databinding
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        //Botón atras de arriba
        val navController = this.findNavController(R.id.myNavHostFragment)
        //Para anclar el navegador a la appbar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //Para el Navigation Drawer
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


}