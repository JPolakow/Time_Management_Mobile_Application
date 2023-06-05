import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.widget.TextView
import com.example.opsc_part2.custom_dashboard_cards

object TimerManager {
    private val handlerMap = mutableMapOf<custom_dashboard_cards, Handler>()
    private val startTimeMap = mutableMapOf<custom_dashboard_cards, Long>()
    private val accumulatedTimeMap = mutableMapOf<custom_dashboard_cards, Long>()
    private const val PREF_NAME = "TimerPrefs"
    private const val KEY_START_TIME = "startTime"
    private const val KEY_ACCUMULATED_TIME = "accumulatedTime"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveTimerData(context: Context, customCard: custom_dashboard_cards, startTime: Long, accumulatedTime: Long) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putLong("${customCard.hashCode()}_$KEY_START_TIME", startTime)
        editor.putLong("${customCard.hashCode()}_$KEY_ACCUMULATED_TIME", accumulatedTime)
        editor.apply()
    }

    fun getStartTime(context: Context, customCard: custom_dashboard_cards): Long {
        val prefs = getSharedPreferences(context)
        return prefs.getLong("${customCard.hashCode()}_$KEY_START_TIME", 0)
    }

    fun updateTimerText(timerText: TextView, elapsedTime: Long) {
        val seconds = (elapsedTime / 1000) % 60
        val minutes = (elapsedTime / (1000 * 60)) % 60
        val hours = (elapsedTime / (1000 * 60 * 60)) % 24

        val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerText.text = formattedTime
    }

    fun getAccumulatedTime(context: Context, customCard: custom_dashboard_cards): Long {
        val prefs = getSharedPreferences(context)
        return prefs.getLong("${customCard.hashCode()}_$KEY_ACCUMULATED_TIME", 0)
    }

    fun pauseTimer(context: Context, customCard: custom_dashboard_cards) {
        val handler = handlerMap[customCard]
        handler?.removeCallbacksAndMessages(null)

        val startTime = startTimeMap[customCard] ?: 0
        val currentTime = System.currentTimeMillis()
        val accumulatedTime = currentTime - startTime

        accumulatedTimeMap[customCard] = accumulatedTime
        customCard.isTimerRunning = false

        // Save the timer data to shared preferences
        saveTimerData(context, customCard, startTime, accumulatedTime)
    }

    private fun formatTime(timeInMillis: Long): String {
        val seconds = (timeInMillis / 1000) % 60
        val minutes = (timeInMillis / (1000 * 60)) % 60
        val hours = (timeInMillis / (1000 * 60 * 60)) % 24

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun stopTimer(customCard: custom_dashboard_cards) {
        val handler = handlerMap[customCard]
        handler?.removeCallbacksAndMessages(null)

        handlerMap.remove(customCard)
        startTimeMap.remove(customCard)
        accumulatedTimeMap.remove(customCard)
        customCard.isTimerRunning = false
    }

    fun startTimer(context: Context, customCard: custom_dashboard_cards, timerText: TextView) {
        if (customCard.isTimerRunning) return

        val handler = Handler()
        val startTime = System.currentTimeMillis() - getAccumulatedTime(context, customCard)

        val runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime

                updateTimerText(timerText, elapsedTime)

                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)

        handlerMap[customCard] = handler
        startTimeMap[customCard] = startTime
        accumulatedTimeMap[customCard] = 0
        customCard.isTimerRunning = true

        // Save the timer data to shared preferences
        saveTimerData(context, customCard, startTime, 0)
    }

}
