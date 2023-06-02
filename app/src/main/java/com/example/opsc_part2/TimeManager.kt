import android.os.Handler
import android.widget.TextView
import com.example.opsc_part2.custom_dashboard_cards

object TimerManager {
    private var handler: Handler? = null
    private var seconds: Int = 0
    private var startTime: Long = 0

    fun startTimer(timerText: TextView) {
        if (handler != null) {
            stopTimer()
        }

        startTime = System.currentTimeMillis()

        handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secondsText = (seconds % 60).toString().padStart(2, '0')
                val timerTextValue = "$hours:$minutes:$secondsText"

                timerText.text = timerTextValue

                seconds++
                handler?.postDelayed(this, 1000)
            }
        }

        handler?.post(runnable)
    }

    fun getCurrentTimerValue(): Long {
        val currentTime = System.currentTimeMillis()
        return currentTime - startTime
    }

    fun stopTimer() {
        handler?.removeCallbacksAndMessages(null)
        handler = null
        seconds = 0
    }

    fun startTimer(customCard: custom_dashboard_cards, timerText: TextView) {
        val handler = Handler()
        var seconds = 0

        val runnable = object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secondsText = (seconds % 60).toString().padStart(2, '0')
                val timerValue = "$hours:$minutes:$secondsText"

                timerText.text = timerValue

                seconds++
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)
    }

}


