package com.example.carnation1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carnation1.Client.ServerConnection
import com.example.carnation1.Client.ServerConnection.send
import com.example.carnation1.views.TimePickerHourOnly
import org.json.simple.JSONObject
import java.time.Instant
import java.util.*

class DatePickerActivity : AppCompatActivity() {
	private lateinit var contentsLayout: ViewAnimator
	private lateinit var datePicker: DatePicker
	private lateinit var timePicker: TimePickerHourOnly
	private lateinit var prevButton: Button
	private lateinit var nextButton: Button
	private lateinit var okButton: Button
	private lateinit var summaryStatus: TextView
	private lateinit var summaryDate: TextView
	private lateinit var summaryTime: TextView
	private lateinit var summaryPosition: TextView
	private var year = 0
	private var month = 0
	private var day = 0
	private var hour = 0

	@SuppressLint("DefaultLocale")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_date_picker)

		contentsLayout = findViewById(R.id.datePicker_ContentsLayout)
		datePicker = findViewById(R.id.datePicker_Date)
		timePicker = findViewById(R.id.datePicker_Time)
		prevButton = findViewById(R.id.datePicker_PrevButton)
		nextButton = findViewById(R.id.datePicker_NextButton)
		okButton = findViewById(R.id.datePicker_OkButton)
		summaryStatus = findViewById(R.id.parkingLotReservationView_Row0)
		summaryDate = findViewById(R.id.parkingLotReservationView_Date)
		summaryTime = findViewById(R.id.parkingLotReservationView_Time)
		summaryPosition = findViewById(R.id.parkingLotReservationView_Position)

		val inAnim = AlphaAnimation(0f, 1f)
		inAnim.duration = 500
		val outAnim = AlphaAnimation(1f, 0f)
		outAnim.duration = 500
		contentsLayout.inAnimation = inAnim
		contentsLayout.outAnimation = outAnim
		prevButton.setOnClickListener { goPrev() }
		nextButton.setOnClickListener { goNext() }
		okButton.setOnClickListener { sendReservation() }
		datePicker.minDate = Instant.now().toEpochMilli()
		datePicker.maxDate = Instant.now().toEpochMilli() + 2592000000L
		datePicker.setOnDateChangedListener { _, _, _, _ -> goNext() }
		timePicker.setHour24(GregorianCalendar()[Calendar.HOUR_OF_DAY] + 1)
		(findViewById<View>(R.id.parkingLotReservationView_thumbnail) as ImageView).setImageResource(
			R.drawable.parkingimage
		)
		summaryStatus.text = "신청 검토 중"
		summaryPosition.text = String.format("%s번", intent.getStringExtra("parkingSpot"))

	}

	override fun onBackPressed() {
		if (contentsLayout.displayedChild == 0) super.onBackPressed() else contentsLayout.showPrevious()
	}

	private fun goPrev() {
		onBackPressed()
		onPageChanged()
	}

	private fun goNext() {
		year = datePicker.year
		month = datePicker.month + 1
		day = datePicker.dayOfMonth
		summaryDate.text = String.format("%d년 %d월 %d일", year, month, day)
		hour = timePicker.getHour24()
		summaryTime.text = String.format(
			"%s %d시 ~ %s %d시",
			if (hour < 12) "오전" else "오후",
			if (hour == 0) 12 else hour,
			if ((hour + 1) % 24 < 12) "오전" else "오후",
			if ((hour + 1) % 24 == 0) 12 else (hour + 1) % 24
		)
		contentsLayout.showNext()
		onPageChanged()
	}

	private fun onPageChanged() {
		when (contentsLayout.displayedChild) {
			0 -> {
				prevButton.text = "취소"
			}
			contentsLayout.childCount - 1 -> {
				nextButton.visibility = GONE
			}
			else -> {
				prevButton.text = "이전"
				nextButton.visibility = VISIBLE
			}
		}
	}

	private fun sendReservation() {
		val timeForReservation =
			Calendar.Builder().setTimeZone(TimeZone.getTimeZone("GMT+9")).setDate(year, month, day)
				.setTimeOfDay(hour, 0, 0).build()
		if (timeForReservation.before(Calendar.Builder().setInstant(Instant.now().toEpochMilli()))) {
			Toast.makeText(this, "예약 시각이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
			return
		}
		val jsonObject = JSONObject()
		jsonObject["parkingSpot"] = intent.getStringExtra("parkingSpot")
		jsonObject["type"] = "reservation"
		jsonObject["year"] = year
		jsonObject["month"] = month
		jsonObject["day"] = day
		jsonObject["hour"] = hour
		jsonObject["sessionNumber"] = ServerConnection.sessionNumber
		jsonObject["userNumber"] = ServerConnection.userNumber
		val result: JSONObject? = send(jsonObject)
		if (result!!["result"].toString() == "OK") {
			Toast.makeText(this, "예약 완료", Toast.LENGTH_SHORT).show()
			setResult(RESULT_OK)
			finish()
		} else if (result["data"].toString() == "Another active reservation exists") {
			Toast.makeText(this, "이미 예약된 기록이 있습니다.", Toast.LENGTH_SHORT).show()
		} else {
			Toast.makeText(this, "서버와의 연결이 원활하지 않습니다. 나중에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
		}
	}
}