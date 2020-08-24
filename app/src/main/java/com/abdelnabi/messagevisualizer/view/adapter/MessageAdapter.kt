package com.abdelnabi.messagevisualizer.view.adapter;

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.abdelnabi.messagevisualizer.R
import com.abdelnabi.messagevisualizer.model.MessageInfoModel
import com.abdelnabi.messagevisualizer.model.MessageModel
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter(private var context: Context, private var itemClicked: OnItemClick) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var list: List<MessageInfoModel> = arrayListOf()
        set(newlist) {
            field = newlist
            notifyDataSetChanged()
        }
    private var selected: CheckBox? = null

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.tv_message.text = model.message
        holder.itemView.tv_sentiment.text = model.sentiment
        when(model.sentiment){
            "Negative" ->  holder.itemView.tv_sentiment.setTextColor(Color.RED)
            "Neutral"-> holder.itemView.tv_sentiment.setTextColor(Color.GREEN)
            "Positive"-> holder.itemView.tv_sentiment.setTextColor(Color.BLUE)
        }
        holder.itemView.rootView.setOnClickListener {
            itemClicked.itemSelected(model)
        }
    }

    interface OnItemClick {
        fun itemSelected(item: MessageInfoModel)
    }
}