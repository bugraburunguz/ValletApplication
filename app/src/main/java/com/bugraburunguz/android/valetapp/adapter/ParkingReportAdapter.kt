package com.bugraburunguz.android.valetapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.model.ReceiptEntity

class ParkingReportAdapter(receiptEntities: ArrayList<ReceiptEntity>, listener: ListItemClickListener) :
    RecyclerView.Adapter<ParkingReportAdapter.ParkingReportViewHolder>() {
    private var listOfReceipts = ArrayList<ReceiptEntity>()
    private val mOnClickListener: ListItemClickListener

    interface ListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingReportViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.parking_report_list_layout, parent, false)
        return ParkingReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParkingReportViewHolder, position: Int) {
        val carNumber = listOfReceipts[position].carNo
        val dateTime = listOfReceipts[position].dateTime
        val noOfHours = listOfReceipts[position].noOfHours
        holder.noOfHours.text = noOfHours
        holder.dateTime.text = dateTime
        holder.carNumber.text = carNumber
    }

    override fun getItemCount(): Int {
        return listOfReceipts.size
    }

    inner class ParkingReportViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var carNumber: TextView = itemView.findViewById(R.id.list_car_number)
        var dateTime: TextView = itemView.findViewById(R.id.list_date_time)
        var noOfHours: TextView = itemView.findViewById(R.id.list_no_of_hours)
        override fun onClick(v: View) {
            val clickedPosition = adapterPosition
            mOnClickListener.onListItemClick(clickedPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    init {
        listOfReceipts = receiptEntities
        mOnClickListener = listener
    }
}
