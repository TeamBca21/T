package com.example.chatgtp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatgtp.Classes.MessageRVModal
import com.example.chatgtp.R

class MessageRVAdapter(private val msgList : ArrayList<MessageRVModal>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserMessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val userMsgTv : TextView = itemView.findViewById(R.id.txtUser)
    }
    class BotMessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val botMsgTv : TextView = itemView.findViewById(R.id.txtBot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View
        return if(viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_msg,parent,false)
            UserMessageViewHolder(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_msg,parent,false)
            BotMessageViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sender = msgList.get(position).sender
        when(sender){
            "user" -> (holder as UserMessageViewHolder).userMsgTv.setText(msgList.get(position).message)
            "bot" -> (holder as BotMessageViewHolder).botMsgTv.setText(msgList.get(position).message)
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(msgList.get(position).sender){
            "user" -> 0
            "bot" -> 1
            else -> 1
        }
    }


}