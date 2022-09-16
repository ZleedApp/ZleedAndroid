package com.zleed.app

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView


class ChannelActivity : AppCompatActivity() {
    private var isFullScreenActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.BLACK

        val videoView = findViewById<StyledPlayerView>(R.id.videoView);

        val videoStateButton = findViewById<ImageButton>(R.id.buttonVideoState)
        val fullscreenButton = findViewById<ImageButton>(R.id.buttonFullscreen)
        val settingsButton   = findViewById<ImageButton>(R.id.buttonSettings)

        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
            .setLiveConfiguration(
                MediaItem.LiveConfiguration.Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .setMaxOffsetMs(10000.toLong())
                    .setTargetOffsetMs(5000.toLong())
                    .build()
            )
            .build()

        val player = ExoPlayer.Builder(this).build()

        videoView.player = player

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        videoStateButton.setOnClickListener { _ ->
            if(player.isPlaying) {
                videoStateButton.setImageDrawable(resources.getDrawable(R.drawable.ic_play))

                player.pause()
            } else {
                videoStateButton.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))

                player.play()
            }
        }

        fullscreenButton.setOnClickListener { _ ->
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            if(!isFullScreenActive) {
                isFullScreenActive = true

                findViewById<TextView>(R.id.textView).visibility = View.GONE

                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                isFullScreenActive = false

                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }

        settingsButton.setOnClickListener { _ ->
            Toast.makeText(this, "It works!", Toast.LENGTH_SHORT).show()
        }

        //videoView.setVideoURI(Uri.parse("https://strmd.eu1.zleed.ga/theclashfruit.m3u8"))
    }

    // TODO: needs fixing
    override fun onBackPressed() {
        Toast.makeText(this, "It works! $isFullScreenActive", Toast.LENGTH_SHORT).show()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if(isFullScreenActive) {
            windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            isFullScreenActive = false
        } else super.onBackPressed()
    }
}