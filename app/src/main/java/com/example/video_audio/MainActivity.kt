package com.example.video_audio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())
        val btnPlay = findViewById<FloatingActionButton>(R.id.fabPlay)
        val btnPause = findViewById<FloatingActionButton>(R.id.fabPause)
        val btnStop = findViewById<FloatingActionButton>(R.id.fabStop)
        val endTime = findViewById<TextView>(R.id.endView)
        endTime.text = "0"
        val currentTime = findViewById<TextView>(R.id.currTimeView)
        currentTime.text = "0"
        btnPlay.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.clap)
                endTime.text = "${mediaPlayer!!.duration / 1000}"
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }
        btnPause.setOnClickListener {
            mediaPlayer?.pause()
        }
        btnStop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }

    private fun initializeSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    mediaPlayer?.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        seekBar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekBar.progress = mediaPlayer!!.currentPosition
            var playedTime = mediaPlayer!!.currentPosition / 1000
            val currentTime = findViewById<TextView>(R.id.currTimeView)
            currentTime.text = "$playedTime"
            handler.postDelayed(runnable, 100)
        }
        handler.postDelayed(runnable, 100)
    }
}