import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.ItineraryFragment
import java.util.Calendar

class DatePickerFragment : DialogFragment() {
    var itineraryFragment: ItineraryFragment? = null
    private lateinit var calendarView: CalendarView

    companion object {
        const val SELECTED_DATE_KEY = "selected_date"
        const val TAG = "DatePickerFragment"

        fun newInstance(itineraryFragment: ItineraryFragment): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.itineraryFragment = itineraryFragment
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.date_picker_fragment, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDateMillis = arguments?.getLong(SELECTED_DATE_KEY, 0) ?: 0
        if (selectedDateMillis != 0L) {
            setCalendarViewDate(selectedDateMillis)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val updatedDateMillis = calendar.timeInMillis

            itineraryFragment?.updateDate(updatedDateMillis)
            dismiss()
        }
    }

    private fun setCalendarViewDate(dateMillis: Long) {
        calendarView.date = dateMillis
    }
}
