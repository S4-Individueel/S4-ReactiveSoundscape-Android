package com.example.reactivesoundscape_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.widget.Button
import android.widget.TextView

enum class LastPlayedSound {
    DEAFEN, UNDEAFEN, NONE
}

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var lastPlayedSound = LastPlayedSound.NONE

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var mediaPlayer: MediaPlayer? = null

    private var accelX: TextView? = null
    private var accelY: TextView? = null
    private var accelZ: TextView? = null

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accelX = findViewById(R.id.accelX)
        accelY = findViewById(R.id.accelY)
        accelZ = findViewById(R.id.accelZ)

        button = findViewById(R.id.button)

        button.setOnClickListener {navigateToOtherScreen()}

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun navigateToOtherScreen() {
        val intent = Intent(this, DragGestureActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            accelX!!.text = x.toString()
            accelY!!.text = y.toString()
            accelZ!!.text = z.toString()

            if (z > 4 && lastPlayedSound != LastPlayedSound.UNDEAFEN) {
                lastPlayedSound = LastPlayedSound.UNDEAFEN
                playSound(R.raw.undeafen)
            } else if (z < -4 && lastPlayedSound != LastPlayedSound.DEAFEN) {
                lastPlayedSound = LastPlayedSound.DEAFEN
                playSound(R.raw.deafen)
            }
        }
    }

    private fun playSound(soundResourceId: Int) {
        synchronized(this) {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                return
            }

            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, soundResourceId)
            mediaPlayer!!.setOnCompletionListener {
                mediaPlayer!!.release()
                mediaPlayer = null
            }

            mediaPlayer!!.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}
