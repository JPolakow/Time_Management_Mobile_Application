import android.os.Handler
import android.widget.TextView
import com.example.opsc_part2.custom_dashboard_cards

object TimerManager {
    private val handler = Handler()
    private var startTime: Long = 0
    private var accumulatedTime: Long = 0
    private lateinit var runnable: Runnable
    private val handlerMap = mutableMapOf<custom_dashboard_cards, Handler>()
    private val startTimeMap = mutableMapOf<custom_dashboard_cards, Long>()
    private val accumulatedTimeMap = mutableMapOf<custom_dashboard_cards, Long>()

   /* fun startTimer(timerText: TextView) {
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
    }*/

    fun pauseTimer(customCard: custom_dashboard_cards) {
        if (!customCard.isTimerRunning) return

        val handler = handlerMap[customCard]
        handler?.removeCallbacksAndMessages(null)

        val startTime = startTimeMap[customCard] ?: 0
        val currentTime = System.currentTimeMillis()
        val accumulatedTime = currentTime - startTime

        accumulatedTimeMap[customCard] = accumulatedTime
        customCard.isTimerRunning = false
    }

    private fun formatTime(timeInMillis: Long): String {
        val seconds = (timeInMillis / 1000) % 60
        val minutes = (timeInMillis / (1000 * 60)) % 60
        val hours = (timeInMillis / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getCurrentTimerValue(): Long {
        val currentTime = System.currentTimeMillis()
        return currentTime - startTime
    }

    fun stopTimer(customCard: custom_dashboard_cards) {
        val handler = handlerMap[customCard]
        handler?.removeCallbacksAndMessages(null)

        handlerMap.remove(customCard)
        startTimeMap.remove(customCard)
        accumulatedTimeMap.remove(customCard)
        customCard.isTimerRunning = false
    }

    fun startTimer(customCard: custom_dashboard_cards, timerText: TextView) {
        if (customCard.isTimerRunning) return

        val handler = Handler()
        val startTime = System.currentTimeMillis() - (accumulatedTimeMap[customCard] ?: 0)

        val runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime

                val hours = (elapsedTime / 3600000).toInt()
                val minutes = ((elapsedTime % 3600000) / 60000).toInt()
                val seconds = ((elapsedTime % 60000) / 1000).toInt()
                val timerValue = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                timerText.text = timerValue

                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)

        handlerMap[customCard] = handler
        startTimeMap[customCard] = startTime
        customCard.isTimerRunning = true
    }


}


