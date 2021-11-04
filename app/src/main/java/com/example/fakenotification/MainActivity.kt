package com.example.fakenotification

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.databinding.DataBindingUtil
import com.example.fakenotification.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val channel_id = "xchannel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnNotify.setOnClickListener {
            clickedNotify()
        }

        binding.btnNotifyBigText.setOnClickListener {
            clickedNotify(isBigText = true)
        }

        binding.btnNotifyLargeIcon.setOnClickListener {
            clickedNotify(useBigPicture = true, isBigText = true)
        }

        binding.btnNotifyInbox.setOnClickListener {
            clickedNotify(useInboxStyle = true)
        }

        binding.btnNotifyMessage.setOnClickListener {
            clickedNotify(messageStyle = true)
        }

        binding.btnNotifyContentView.setOnClickListener {
            clickedNotify(useContentView = true)
        }

        binding.notifyBigContentView.setOnClickListener {
            clickedNotify(useBigContentView = true)
        }

        binding.btnNotifyHeadup.setOnClickListener {
            clickedNotify(useHeadsupContentView = true)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("abba", "action: ${intent?.action}")
    }

    private fun clickedNotify(
        isBigText: Boolean = false,
        useBigPicture: Boolean = false,
        useInboxStyle: Boolean = false,
        messageStyle: Boolean = false,
        useContentView: Boolean = false,
        useBigContentView: Boolean = false,
        useHeadsupContentView: Boolean = false
    ) =
        with(binding) {
            val title = edtTitle.text.toString()
            val content = edtContent.text.toString()
            val notifyId =
                if (edtNotifyId.text.isEmpty()) Random.nextInt() else edtNotifyId.text.toString()
                    .toInt()

            val notification =
                createNotification(
                    title,
                    content,
                    isBigText,
                    useBigPicture,
                    useInboxStyle,
                    messageStyle,
                    useContentView,
                    useBigContentView,
                    useHeadsupContentView
                )

            notify(notifyId, notification)
        }

    private fun notify(id: Int, notification: Notification) {
        val manager = NotificationManagerCompat.from(this@MainActivity)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(manager)


        manager.notify(id, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManagerCompat) {
        val channel =
            NotificationChannel(channel_id, "name", NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(
        title: String,
        content: String,
        isBigText: Boolean,
        useBigPicture: Boolean,
        useInboxStyle: Boolean,
        useMessageStyle: Boolean,
        useContentView: Boolean,
        useBigContentView: Boolean,
        useHeadsupContentView: Boolean
    ): Notification {
        val builder = NotificationCompat.Builder(this, channel_id)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_24)
//            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createContentIntent(content))

        if (isBigText) {
            builder.setStyle(
                NotificationCompat.BigTextStyle().setSummaryText("summary text")
                    .setBigContentTitle("xxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
                    .bigText("bccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbccccbcccc")
                    .setSummaryText("summary text")
            )
        }


        if (useBigPicture) {
            val bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.bitmap
            )
            builder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(bitmap)
                    .setSummaryText("summary text").setBigContentTitle("big content title")
            )
        }

        if (useMessageStyle) {
            val person1 = Person.Builder()
                .setKey("person1 key")
                .setIcon(IconCompat.createWithResource(this, R.drawable.person1))
                .setName("person1")
                .setUri("person1 uri")
                .setBot(true)
                .setImportant(true)
                .build()

            val person2 = Person.Builder()
                .setKey("person2 key")
                .setIcon(IconCompat.createWithResource(this, R.drawable.person2))
                .setName("person2")
                .setUri("person2 uri")
                .build()


            val messageStyle =
                NotificationCompat.MessagingStyle(person1)
                    .setConversationTitle("Message title")
                    .addMessage("message 1", System.currentTimeMillis(), person1)
                    .addMessage("message 2", System.currentTimeMillis(), person2)
                    .addMessage("message 3", System.currentTimeMillis(), person1)
                    .addMessage("message 4", System.currentTimeMillis(), person2)

            builder.setStyle(messageStyle)


        }

        if (useInboxStyle) {
            builder.setStyle(
                NotificationCompat.InboxStyle().addLine("abx").addLine("xyz").addLine("bcd")
                    .addLine("333").setBigContentTitle("big content title")
                    .setSummaryText("summary text")
            )
        }

        val remoteView = RemoteViews(packageName, R.layout.content_view_layout)
        remoteView.setTextViewText(R.id.title,"custom title")
        remoteView.setTextViewText(R.id.content,"custom content")

        if (useContentView) {
            builder.setCustomContentView(remoteView)
        }

        if (useBigContentView) {
            builder.setCustomBigContentView(remoteView)
        }

        if (useHeadsupContentView) {
            builder.setCustomHeadsUpContentView(remoteView)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        }

        return builder.build()
    }

    private fun createContentIntent(content: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = content
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getActivity(this, 0, intent, flags)
    }
}