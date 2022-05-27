package com.cloudcoding.api

import android.content.Context
import com.cloudcoding.MainActivity
import com.cloudcoding.api.request.GetConversationsRequest
import com.cloudcoding.api.request.GetMessagesRequest
import com.cloudcoding.api.request.MessageRequest
import com.cloudcoding.models.Conversation
import com.cloudcoding.models.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.lang.reflect.Type


object SocketManager {
    private val gson = Gson()
    private val connection = socket()

    private fun socket(): Socket {
        val preference = MainActivity.getContext().getSharedPreferences(
            "token",
            Context.MODE_PRIVATE
        )!!
        val token = preference.getString("token", "")!!
        val opts = IO.Options()
        opts.extraHeaders = hashMapOf<String, List<String>>("authorization" to listOf(token))
        val socket = IO.socket("http://10.0.2.2:3000/messaging", opts)
        socket.connect()
        return socket
    }

    fun onMessageDeleted(a: () -> Unit) {
        connection.on("messageDeleted") { a() }
    }

    fun onMessageCreated(a: (Message) -> Unit) {
        connection.on("messageCreated") { args ->
            println(args[0].toString())
            val type: Type = object : TypeToken<Message>() {}.type
            val message: Message = gson.fromJson(args[0].toString(), type)
            a(message)
        }
    }

    fun onConversations(a: (List<Conversation>) -> Unit) {
        connection.on("conversations") { args ->
            val listType: Type = object : TypeToken<List<Conversation>?>() {}.type
            val conversations: List<Conversation> = gson.fromJson(args[0].toString(), listType)
            a(conversations)
        }
    }

    fun onMessages(a: (MutableList<Message>) -> Unit) {
        connection.on("messages") { args ->
            val listType: Type = object : TypeToken<MutableList<Message>?>() {}.type
            val messages: MutableList<Message> = gson.fromJson(args[0].toString(), listType)
            a(messages)
        }
    }

    fun clear() {
        connection.off()
    }

    fun getConversations(getConversationsRequest: GetConversationsRequest) {
        connection.emit("getConversations",  JSONObject(gson.toJson(getConversationsRequest)))
    }

    fun getMessages(getMessagesRequest: GetMessagesRequest) {
        connection.emit("getMessages", JSONObject(gson.toJson(getMessagesRequest)))
    }

    fun createMessage(createMessage: MessageRequest) {
        connection.emit("sendMessage", JSONObject(gson.toJson(createMessage)))
    }
}