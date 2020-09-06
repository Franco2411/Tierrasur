package com.example.tierrasurfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tierrasurfragments.databinding.FragmentRegistroBinding
import com.google.firebase.auth.FirebaseAuth


class RegistroFragment : Fragment() {

    private lateinit var binding: FragmentRegistroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentRegistroBinding>(inflater, R.layout.fragment_registro, container, false)

        //Cambio el nombre de la ActionBar
        (activity as AppCompatActivity).supportActionBar?.title ="Registrarse"

        botones()
        return binding.root
    }

    /**
     * Metodo botones
     */
    private fun botones() {

        //Boton confirmar regstro
        binding.btnConfirmRegistro.setOnClickListener {view: View ->

            //Guardo las contras
            val pass = binding.txtContra.text.toString()
            val pass2 = binding.txtContra2.text.toString()

            if(binding.txtNombre.text.isEmpty() || binding.txtApellido.text.isEmpty() || binding.txtMail.text.isEmpty() || binding.txtContra.text.isEmpty() || binding.txtContra2.text.isEmpty()){
                Toast.makeText(this.requireActivity(), "Debe de llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else if(pass.equals(pass2)){
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(binding.txtMail.text.toString(), binding.txtContra.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this.requireActivity(), "Usted se ha registrado exitosamente", Toast.LENGTH_SHORT).show()

                            //Vuelvo al inicio de sesión
                            view.findNavController().navigate(RegistroFragmentDirections.actionRegistroFragmentToInicioSesionFragment())

                        } else{
                            showAlert("Error", "No se pudo registrar en la base, revise su conexión e intente nuevamente.")
                        }
                    }

            } else{
                Toast.makeText(this.requireActivity(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()

            }


        }
    }

    /**
     * Cartel de alerta
     */
    private fun showAlert(titulo: String, msg: String) {
        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setTitle(titulo)
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}