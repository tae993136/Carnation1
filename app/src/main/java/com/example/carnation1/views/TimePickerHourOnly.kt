package com.example.carnation1.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.carnation1.R
import java.util.*

class TimePickerHourOnly(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
	private val infService = Context.LAYOUT_INFLATER_SERVICE
	private var numPickerAMPM: NumberPicker
	private var numPickerHour: NumberPicker

	enum class AMPM {
		AM,
		PM
	}

	init {
		val inflater = getContext().getSystemService(infService) as LayoutInflater
		val view = inflater.inflate(R.layout.timepicker_houronly, this, false)
		numPickerAMPM = view.findViewById(R.id.timePicker_AMPM)
		numPickerAMPM.minValue = 0
		numPickerAMPM.maxValue = 3
		numPickerAMPM.displayedValues = arrayOf("오전", "오후", "오전", "오후")
		numPickerAMPM.wrapSelectorWheel = true
		numPickerHour = view.findViewById(R.id.timePicker_Hour)
		numPickerHour.minValue = 0
		numPickerHour.maxValue = 11
		numPickerHour.displayedValues =
			arrayOf("12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11")
		numPickerHour.wrapSelectorWheel = true
		numPickerHour.setOnValueChangedListener { _, oldVal, newVal ->
			if ((oldVal == 11 && newVal == 0) || (oldVal == 0 && newVal == 11))
				numPickerAMPM.value = (numPickerAMPM.value + 1) % 2
		}
		addView(view)
	}

	fun getHour24(): Int {
		return getHour() + numPickerAMPM.value % 2 * 12
	}

	fun getHour(): Int {
		return numPickerHour.value
	}

	fun getAMPM(): AMPM {
		return if (numPickerAMPM.value % 2 == 0) AMPM.AM else AMPM.PM
	}

	fun getDate(): Date {
		return Calendar.Builder().setTimeOfDay(getHour24(), 0, 0).build().time
	}

	fun setHour24(hour: Int) {
		assert(hour in 0..23)
		numPickerHour.value = hour % 12
		numPickerAMPM.value = if (hour >= 12) 1 else 0
	}

	fun setHour(hour: Int) {
		assert(hour in 0..11)
		numPickerHour.value = hour
	}

	fun setAMPM(value: AMPM) {
		numPickerAMPM.value = if (value == AMPM.AM) 0 else 1
	}
}