package com.example.tierrasurfragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tierrasurfragments.databinding.FragmentHome1Binding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class Home1Fragment : Fragment() {

    //Databinding
    private lateinit var binding: FragmentHome1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHome1Binding>(
            inflater,
            R.layout.fragment_home1,
            container,
            false
        )

        //Recupero el email
        val bundle: Bundle? = activity?.intent?.extras
        val email = bundle?.getString("email")
        //Toast.makeText(activity, "${email} gg", Toast.LENGTH_SHORT).show()

        //Ahora guardo los datos
        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
        prefs?.putString("email", email)
        prefs?.apply()

        //Cambio el nombre a la Action Bar
        (activity as AppCompatActivity).supportActionBar?.title ="Tierrasur S.A."

        //Fecha acutal
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tvFecha.setText(date)

        //Funciones
        spincomplete()
        anioCampaña()


        //Boton siguiente
        binding.btnSiguiente.setOnClickListener { view: View ->
            validacion(view)
        }

        setHasOptionsMenu(true)
        return binding.root
    }



    //Llenado de spinners
    private fun spincomplete(){

        //Actividades
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.spin_actividades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinActividad.adapter = adapter
        }


        //N° de Lote
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.spinNum_lote,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinLote.adapter = adapter
        }


        //Unidad productiva
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.spinUni_P,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinUnidadP.adapter = adapter
        }


    }




    /** Validacion para pasar al siguiente fragment **/
    private fun validacion(view: View) {

        val activ = binding.spinActividad.selectedItem.toString()
        val lote = binding.spinLote.selectedItem.toString()
        val uni = binding.spinUnidadP.selectedItem.toString()

        if (activ.equals("null") || lote.equals("null") || uni.equals("null")) {
            Snackbar.make(view, "Debe de llenar todos los campos.", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        } else {

            //Guardo los datos seleccionados
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            prefs?.putString("fecha", binding.tvFecha.text.toString())
            prefs?.putString("camp", binding.txtCampania.text.toString())
            prefs?.putString("actividad", activ)
            prefs?.putString("numLote", lote)
            prefs?.putString("uniP", uni)
            prefs?.apply()



            view.findNavController().navigate(Home1FragmentDirections.actionHome1Fragment2ToHome2Fragment())
        }
    }

    /** Fecha campaña **/
    private fun anioCampaña() {
        val calendar = Calendar.getInstance()
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val mesBien = mes.toInt() + 1
        val anio = SimpleDateFormat("yy", Locale.getDefault()).format(Date())


        if(dia.toInt() <= 31  && mesBien <= 5){
            val anioAnt = anio.toInt() - 1
            val anioAnte = anioAnt.toString()
            binding.txtCampania.setText(anioAnte + "/" + anio.toString())
        } else{
            val anioSig = anio.toInt() +1
            binding.txtCampania.setText(anio + "/" + anioSig)
        }
    }





}