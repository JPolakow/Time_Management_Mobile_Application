package com.example.opsc_part2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

class TimerService : Service()
{
    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        Log.d("timer", "entered1")
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
        Log.d("timer", "entered2")
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
        Log.d("timer", "entered3")
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            Log.d("timer", "task1")
            val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
            Log.d("timer", "task2")
        }
    }

    companion object
    {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

}