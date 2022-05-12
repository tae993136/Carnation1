package com.example.carnation1.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.carnation1.R

class ParkingLotView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
	private val infService = Context.LAYOUT_INFLATER_SERVICE

	enum class Direction {
		LEFT, RIGHT
	}

	private class ItemData(number: Int, position: String, onClickListener: OnClickListener?) {
		var num: Int
		var row: Int
		var direction: Direction?
		lateinit var onClickListener: OnClickListener

		init {
			num = number
			direction = if (position[0] == 'L') Direction.LEFT else Direction.RIGHT
			row = position.substring(1).toInt()
			if (onClickListener != null) {
				this.onClickListener = onClickListener
			}
		}
	}

	class ListAdapter : BaseAdapter {
		private val TAG = javaClass.simpleName
		private var activity: Activity
		private var direction: Direction? = null
		private var arrayList: ArrayList<ItemData> = ArrayList()

		constructor(activity: Activity) {
			this.activity = activity
		}

		constructor(activity: Activity, direction: Direction) {
			this.activity = activity
			this.direction = direction
		}

		override fun getCount(): Int {
			return arrayList.size
		}

		override fun getItem(position: Int): Any {
			return arrayList[position]
		}

		override fun getItemId(position: Int): Long {
			return position.toLong()
		}

		override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
			var view = convertView
			val holder: ViewHolder
			if (view == null) {
				holder = ViewHolder()
				view = LayoutInflater.from(parent!!.context).inflate(
					if (direction == Direction.LEFT)
						R.layout.parkinglot_list_item_left
					else
						R.layout.parkinglot_list_item_right, parent, false
				)
				holder.imageButton = view.findViewById(R.id.parkingLot_Item_Image)
				holder.textView = view.findViewById(R.id.parkingLot_Item_Text)
				view.tag = holder
			} else
				holder = view.tag as ViewHolder

			val itemData: ItemData = arrayList[position]
			holder.imageButton.setImageResource(
				if (itemData.direction == Direction.LEFT)
					R.drawable.parking_lot_left
				else
					R.drawable.parking_lot_right
			)
			holder.textView.text = itemData.row.toString()
			holder.imageButton.setOnClickListener(itemData.onClickListener)
			return view!!
		}

		class ViewHolder {
			lateinit var imageButton: ImageButton
			lateinit var textView: TextView
		}

		fun addItem(number: Int, position: String, onClickListener: OnClickListener?) {
			val item = ItemData(number, position, onClickListener)
			if (item.direction == direction)
				arrayList.add(item)
		}
	}

	init {
		val inflater = getContext().getSystemService(infService) as LayoutInflater
		val view = inflater.inflate(R.layout.parkinglot_view, this, false)
		addView(view)
	}
}