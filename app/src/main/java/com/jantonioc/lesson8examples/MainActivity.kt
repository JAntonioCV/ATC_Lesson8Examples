package com.jantonioc.lesson8examples

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import java.lang.Thread.sleep
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var btnDialog: Button
    lateinit var btnDate: Button
    lateinit var progressBar: ProgressBar
    private var progressStaus = 0
    lateinit var seekBar: SeekBar
    lateinit var seekResult: TextView
    lateinit var tvDate: TextView
    lateinit var webView: WebView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDialog = findViewById<Button>(R.id.btnShowDialog)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        seekBar = findViewById(R.id.seekBar)
        seekResult = findViewById(R.id.seekbarResult)
        btnDate = findViewById(R.id.btnFecha)
        tvDate = findViewById(R.id.txtFecha)

        webView = findViewById(R.id.webView)

        //webView
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.settings.javaScriptEnabled = true
        webView.settings.loadsImagesAutomatically= true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://androidatc.com")



        btnDialog.setOnClickListener{
            //showMyDialog()
            //showProgressBar()
            showNotification()
        }

        btnDate.setOnClickListener{
            //showCalendar()
            showSnackbar()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                seekResult.text = "Tracking started..${seekBar?.progress}"
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekResult.text = "Selected: $progress"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekResult.text = "Tracking end..${seekBar?.progress}"
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        //variasbles para el canal
        val CHANNEL_ID = "myChannel_id_01"
        val CHANNEL_NAME = "Nombre del canal"
        val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
        val chanelDescription = "Descripcion del canal"

        val  channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,CHANNEL_IMPORTANCE)
        channel.description = chanelDescription
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = getColor(R.color.purple_500)
        //crear la noficacion
        //2.1 generar pending intent
        val stackBuilder = TaskStackBuilder.create(this)

        //2.2creanis un inten
        val intent = Intent(this,ActivityNotifications::class.java)

        //2.2 agregar inten al tope de la pila virtual
        stackBuilder.addNextIntent(intent)

        //2.3

        val pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
        notification.setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Titulo de la notificacion")
            .setContentText("Mensaje de la notificacion")
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(10,notification.build())

    }

    private fun showSnackbar() {
        val contextView = findViewById<View>(R.id.mainlayout)
        Snackbar.make(contextView, "No internet Conection", Snackbar.LENGTH_SHORT).setAction("Retry")
        {
            Toast.makeText(this,"XD",Toast.LENGTH_SHORT).show()
        }.setActionTextColor(Color.RED).show()
    }

    private fun showCalendar() {
/*        val datePicker = DatePickerDialog(this)
        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            tvDate.text = "$dayOfMonth / ${month+1} / $year"
        }
        datePicker.show()*/

        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR)
        val m = cal.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            tvDate.text = "Hora: $hourOfDay con $minute"}, h,m,true
        )

        timePicker.show()
    }

    private fun showProgressBar() {
        progressBar.progress = 1

        Thread(
            Runnable {
                try
                {
                    while (progressStaus < 100)
                    {
                        progressStaus +=10
                        sleep(500)
                        progressBar.progress = progressStaus
                    }

                }catch (e: InterruptedException)
                {
                    Log.v("ILesson8","${e.localizedMessage}")
                }
            }).start()
    }

    private fun showMyDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Save")
            .setMessage("¿Seguro que desea salir?")
            .setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(this,"Yes",Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(this,"No",Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Neutral"){ dialogInterface: DialogInterface, i: Int ->
                Toast.makeText(this,"Neutral",Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menuItem1 -> {
                Toast.makeText(this,"Menu 1",Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }

            R.id.menuItem2 -> {
                Toast.makeText(this,"Menu 2",Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }

            R.id.menuItem3 -> {
                Toast.makeText(this,"Menu 3",Toast.LENGTH_SHORT).show()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}