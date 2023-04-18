package com.example.reactivesoundscape_android

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button

class DragGestureActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var gestureDetector: GestureDetector
    private var currentVolume: Float = 1.0f
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_gesture)

        mediaPlayer = MediaPlayer.create(this, R.raw.scream)
        mediaPlayer.isLooping = true

        gestureDetector = GestureDetector(this, this)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        currentVolume += p3 / 1000.0f
        if (currentVolume < 0) {
            currentVolume = 0.0f
        } else if (currentVolume > 1) {
            currentVolume = 1.0f
        }
        mediaPlayer.setVolume(currentVolume, currentVolume)
        return true
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent) {}

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent) {}

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }
}
