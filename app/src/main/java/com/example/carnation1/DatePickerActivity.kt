package com.example.carnation1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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
	private var contentsLayout: ViewAnimator? = null
	private var datePicker: DatePicker? = null
	private var timePicker: TimePickerHourOnly? = null
	private var okButton: Button? = null
	private var prevButton: Button? = null
	private var nextButton: Button? = null
	private var summaryStatus: TextView? = null
	private var summaryDate: TextView? = null
	private var summaryTime: TextView? = null
	private var summaryPosition: TextView? = null
	private var year = 0
	private var month = 0
	private var day = 0
	private var hour = 0

	@SuppressLint("DefaultLocale")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_date_picker)
		contentsLayout = findViewById(R.id.datePicker_ContentsLayout)
		val inAnim = AlphaAnimation(0f, 1f)
		inAnim.duration = 500
		val outAnim = AlphaAnimation(1f, 0f)
		outAnim.duration = 500
		contentsLayout?.inAnimation = inAnim
		contentsLayout?.outAnimation = outAnim
		datePicker = findViewById(R.id.datePicker_Date)
		datePicker?.setMinDate(Instant.now().toEpochMilli())
		datePicker?.setMaxDate(Instant.now().toEpochMilli() + 2592000000L)
		timePicker = findViewById(R.id.datePicker_Time)
		timePicker?.setHour24(GregorianCalendar()[Calendar.HOUR_OF_DAY] + 1)
		okButton = findViewById(R.id.datePicker_OKbutton)
		prevButton = findViewById(R.id.datePicker_PrevButton)
		nextButton = findViewById(R.id.datePicker_NextButton)
		summaryStatus = findViewById(R.id.parkingLotReservationView_Row0)
		summaryDate = findViewById(R.id.parkingLotReservationView_Date)
		summaryTime = findViewById(R.id.parkingLotReservationView_Time)
		summaryPosition = findViewById(R.id.parkingLotReservationView_Position)
		(findViewById<View>(R.id.parkingLotReservationView_thumbnail) as ImageView).setImageResource(
			R.drawable.parkingimage
		)
		summaryStatus?.text = "신청 검토 중"
		summaryPosition?.text = String.format("%s번", intent.getStringExtra("parkingSpot"))
		datePicker?.setOnDateChangedListener { _, _, _, _ -> goNext(null) }
	}

	override fun onBackPressed() {
		if (contentsLayout!!.displayedChild == 0) super.onBackPressed() else contentsLayout!!.showPrevious()
	}

	fun goPrev(view: View?) {
		onBackPressed()
		nextButton!!.visibility = View.VISIBLE
		prevButton!!.text = if (contentsLayout!!.displayedChild == 0) "취소" else "이전"
	}

	@SuppressLint("DefaultLocale")
	fun goNext(view: View?) {
		year = datePicker!!.year
		month = datePicker!!.month + 1
		day = datePicker!!.dayOfMonth
		summaryDate!!.text = String.format("%d년 %d월 %d일", year, month, day)
		hour = timePicker!!.getHour24()
		summaryTime!!.text = String.format(
			"%s %d시 ~ %s %d시",
			if (hour < 12) "오전" else "오후",
			if (hour == 0) 12 else hour,
			if ((hour + 1) % 24 < 12) "오전" else "오후",
			if ((hour + 1) % 24 == 0) 12 else (hour + 1) % 24
		)
		contentsLayout!!.showNext()
		if (contentsLayout!!.displayedChild == 2) nextButton!!.visibility =
			View.INVISIBLE else nextButton!!.visibility = View.VISIBLE
	}

	fun sendReservation(v: View?) {
		val timeForReservation =
			Calendar.Builder().setLocale(Locale.getDefault()).setDate(year, month, day)
				.setTimeOfDay(hour, 0, 0).build()
		if (Instant.now().toEpochMilli() - timeForReservation.timeInMillis >= 0) {
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
		val result = send(jsonObject)
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