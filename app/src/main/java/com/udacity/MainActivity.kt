package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private val NOTIFICATION_ID = 0
    private val REQUEST_CODE = 0
    private val FLAGS = 0

    var title = ""
    var description = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
            if (!isChecked) {
                custom_button.updateButtonState(ButtonState.Clicked)
                Toast.makeText(this, "Please select the file to download", LENGTH_SHORT)
                    .show()
            } else {
                custom_button.updateButtonState(ButtonState.Loading)
                Handler().postDelayed({
                    startNotification()
                }, 3000)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download() {

        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton ->
                    if (checked) {
                        isChecked = true
                        title = getString(R.string.glideLibraryDownLoad)
                        description = getString(R.string.notification_descriptionGlide)
                    }
                R.id.radioButton2 ->
                    if (checked) {
                        isChecked = true
                        title = getString(R.string.notification_title)
                        description = getString(R.string.notification_description)
                    }
                R.id.radioButton3 ->
                    if (checked) {
                        isChecked = true
                        title = getString(R.string.retrofitLibrary)
                        description = getString(R.string.notification_descriptionRetrofit)
                    }

            }
        }

    }

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
        // Create the content intent for the notification, which launches
        // this activity
        // TODO: Step 1.11 create intent
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        // TODO: Step 1.12 create PendingIntent
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // TODO: Step 2.0 add style
        val eggImage = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.drawable.ic_assistant_black_24dp
        )

//        val bigPicStyle = NotificationCompat.BigPictureStyle()
//            .bigPicture(eggImage)
//            .bigLargeIcon(null)

        // TODO: Step 2.2 add snooze action
        val snoozeIntent = Intent(applicationContext, DetailActivity::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            REQUEST_CODE,
            snoozeIntent,
            FLAGS
        )

        // TODO: Step 1.2 get an instance of NotificationCompat.Builder
        // Build the notification
        val builder = NotificationCompat.Builder(
            applicationContext,


            // TODO: Step 1.8 use the new 'breakfast' notification channel
            applicationContext.getString(R.string.notification_channel_id)
        )

            // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(title)
            .setContentText(messageBody)
            // TODO: Step 1.13 set content intent
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

            // TODO: Step 2.1 add style to builder
//            .setStyle(bigPicStyle)
//            .setLargeIcon(eggImage)

            // TODO: Step 2.3 add snooze action
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                "Check the status",
                snoozePendingIntent
            )

            // TODO: Step 2.5 set priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // TODO: Step 1.4 call notify
        notify(NOTIFICATION_ID, builder.build())

    }

    // TODO: Step 1.14 Cancel all notifications
    fun NotificationManager.cancelNotifications() {
        cancelAll()
    }


    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )
                // TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
            // TODO: Step 1.6 END create a channel
        }

    }

    fun startNotification() {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(
            description,
            applicationContext
        )

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
    }

}


