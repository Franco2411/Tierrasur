package com.example.tierrasurfragments

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tierrasurfragments.databinding.FragmentHome2Binding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Home2Fragment : Fragment() {

    //Databinding
    private lateinit var binding: FragmentHome2Binding

    //Cloud Firestore
    val db = FirebaseFirestore.getInstance()

    //Para enviar los mails
    var session: Session? = null
    var pdialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHome2Binding>(inflater, R.layout.fragment_home2, container, false)

        //Cambio el nombre a la Action Bar
        (activity as AppCompatActivity).supportActionBar?.title ="Tierrasur S.A."




        spinComplete()
        cargaRegistro()



        return binding.root
    }

    /** Llenado spinners **/
    private fun spinComplete() {

        //Unidades Labores
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.unidades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.uniLab.adapter = adapter
        }

        //Unidades Semillas
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.unidades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.uniSem.adapter = adapter
        }

        //Unidades Agroquimicos
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.unidades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.uniAgro.adapter = adapter
        }

        //Unidades Fertilizantes
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.unidades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.uniFerti.adapter = adapter
        }

        //Unidades Varios
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.unidades,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.uniVarios.adapter = adapter
        }

        //Agroquímicos
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.agros,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinAgro.adapter = adapter
        }

        //Fertilizantes
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.fertis,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinFerti.adapter = adapter
        }

        //Semillas
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.semillas,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinSemillas.adapter = adapter
        }

        //Varios
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.varios,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinVarios.adapter = adapter
        }

        //Labores
        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.labores,
            R.layout.spinner_login
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_login)
            binding.spinLabores.adapter = adapter
        }
    }

    /** Subo los datos **/
    private fun cargaRegistro() {

        binding.btnRegisOperacion.setOnClickListener {vie: View ->

            //Recupero los datos del fragment home1
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val fecha = prefs?.getString("fecha", null)
            val camp = prefs?.getString("camp", null)
            val actividad = prefs?.getString("actividad", null)
            val numLote = prefs?.getString("numLote", null)
            val uniP = prefs?.getString("uniP", null)

            try {
                val datos = db.collection(fecha.toString())
                val op1 = hashMapOf(
                    "campaña" to "${camp}",
                    "unidad productiva" to "${uniP}",
                    "N° de lote" to "${numLote}",
                    "actividad" to "${actividad}",
                    "labores" to "${binding.spinLabores.selectedItem}, Cant: ${binding.txtCantLab.text.toString()} ${binding.uniLab.selectedItem}",
                    "semillas" to "${binding.spinSemillas.selectedItem}, Cant: ${binding.txtCantSem.text.toString()} ${binding.uniSem.selectedItem}",
                    "agroquimicos" to "${binding.spinAgro.selectedItem}, Cant: ${binding.txtCantAgros.text.toString()} ${binding.uniAgro.selectedItem}",
                    "fertilizantes" to "${binding.spinFerti.selectedItem}, Cant: ${binding.txtCantFertis.text.toString()} ${binding.uniFerti.selectedItem}",
                    "varios" to "${binding.spinVarios.selectedItem}, Cant: ${binding.txtCantVarios.text.toString()} ${binding.uniVarios.selectedItem}"


                )
                datos.document("UP: ${uniP}").collection("N° Lote: ${numLote}").document("Actividad: ${actividad}").set(op1)
                Toast.makeText(this.requireContext(), "Registros cargados con éxito.", Toast.LENGTH_SHORT).show()
                email()
            } catch (e: IOException) {
                Toast.makeText(this.requireContext(), "Error al cargar los registros.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    //Email
    private fun email(){

        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
        session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    "tierrasur.sa2020@gmail.com",
                    "TierraSA"
                )
            }
        })
        pdialog = ProgressDialog.show(this.requireContext(), "", "Sending Mail...", true)
        val task: RetreiveFeedTask = RetreiveFeedTask()
        task.execute()
    }

    internal inner class RetreiveFeedTask :
        AsyncTask<String?, Void?, String?>() {
        protected override fun doInBackground(vararg p0: String?): String? {

            //Recupero los datos del fragment home1
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val fecha = prefs?.getString("fecha", null)
            val camp = prefs?.getString("camp", null)
            val actividad = prefs?.getString("actividad", null)
            val numLote = prefs?.getString("numLote", null)
            val uniP = prefs?.getString("uniP", null)



            //Variables
            val labores = binding.spinLabores.selectedItem.toString()
            val semillas = binding.spinSemillas.selectedItem.toString()
            val agros = binding.spinAgro.selectedItem.toString()
            val fertis = binding.spinFerti.selectedItem.toString()
            val varios = binding.spinVarios.selectedItem.toString()
            val cantLab = binding.txtCantLab.text.toString()
            val cantSem = binding.txtCantSem.text.toString()
            val cantAgro = binding.txtCantAgros.text.toString()
            val cantFert = binding.txtCantFertis.text.toString()
            val cantVar = binding.txtCantVarios.text.toString()


            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress("testfrom354@gmail.com"))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("fra98nba@gmail.com")
                )
                message.subject = "${actividad} - ${camp}"
                message.setContent("Campaña: ${camp} " +
                        "/// UP: ${uniP} " +
                        "/// N° de Lote: ${numLote} " +
                        "/// Actividad: ${actividad} " +
                        "/// Labores: ${labores}, Cant: ${cantLab} ${binding.uniLab.selectedItem.toString()} " +
                        "/// Semillas: ${semillas}, Cant: ${cantSem} ${binding.uniSem.selectedItem.toString()} " +
                        "/// Agroquímicos: ${agros}, Cant: ${cantAgro} ${binding.uniAgro.selectedItem.toString()} " +
                        "/// Fertilizantes: ${fertis}, Cant: ${cantFert} ${binding.uniFerti.selectedItem.toString()} " +
                        "/// Varios: ${varios}, Cant: ${cantVar} ${binding.uniFerti.selectedItem.toString()} ", "text/html; charset=utf-8")

                Transport.send(message)
            } catch (e: MessagingException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            pdialog?.dismiss()
            showAlert("Exito!!", "La operación fue realizada con éxito.")
            Toast.makeText(activity, "Mensaje enviado.", Toast.LENGTH_LONG).show()

            //Borro los datos guardados
            val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()
            prefs?.remove("fecha")
            prefs?.remove("camp")
            prefs?.remove("actividad")
            prefs?.remove("numLote")
            prefs?.remove("uniP")
            prefs?.apply()
            view?.findNavController()?.navigate(Home2FragmentDirections.actionHome2FragmentToHome1Fragment2())
        }
    }

    /** Cartel **/
    private fun showAlert(titulo: String, msg: String) {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle(titulo)
        builder.setMessage(msg)
        builder.setPositiveButton("Ok", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }




}