import Classes.ToolBox
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import java.time.LocalDate
import android.widget.TextView
import com.example.opsc_part2.R
import com.example.opsc_part2.custom_logs_cards
import com.example.opsc_part2.databinding.FragmentLogsBinding
import java.text.SimpleDateFormat
import java.util.*

class Logs : Fragment(R.layout.fragment_logs) {
    private lateinit var linView: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logs, container, false)

        linView = view.findViewById(R.id.linearProjectCards)

        val filterButton: ImageButton = view.findViewById(R.id.ibFilter)
        filterButton.setOnClickListener {
        }

        return view
    }





}
