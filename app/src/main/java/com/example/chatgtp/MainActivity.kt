package com.example.chatgtp

import android.app.DownloadManager.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.chatgtp.Adapters.MessageRVAdapter
import com.example.chatgtp.Classes.MessageRVModal
import com.example.chatgtp.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var queryEdt : EditText
    lateinit var recyclerView: RecyclerView
    lateinit var msgAdapter : MessageRVAdapter
    lateinit var msgList : ArrayList<MessageRVModal>
    var url = "https://api.openai.com/v1/completions"
    // var url = "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"


    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        msgList = ArrayList()
        msgAdapter = MessageRVAdapter(msgList)
        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = msgAdapter

        binding.idEtQuery.setOnEditorActionListener(TextView.OnEditorActionListener{ textView , i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEND){
                if(binding.idEtQuery.text.toString().length > 0){
                    msgList.add(MessageRVModal(binding.idEtQuery.text.toString(),"user"))
                    msgAdapter.notifyDataSetChanged()
                    getResponse(binding.idEtQuery.text.toString())
                }else{
                    Toast.makeText(this@MainActivity,"Pleas Enter Your Question First !",Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false

        })

    }

    private fun getResponse(query: String) {
        binding.idEtQuery.setText("")
        val queue : RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject : JSONObject?= JSONObject()
        jsonObject?.put("model","text-davinci-003")
        jsonObject?.put("prompt",query)
        jsonObject?.put("temperature",0)
        jsonObject?.put("max_tokens",100)
        jsonObject?.put("top_p",1)
        jsonObject?.put("frequency_penalty",0.0)
        jsonObject?.put("presence_penalty",0.0)

        val postRequest : JsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener {  response->
           val responseMsg : String = response.getJSONArray("choices").getJSONObject(0).getString("text")
            msgList.add(MessageRVModal(responseMsg,"bot"))
            msgAdapter.notifyDataSetChanged()

        },Response.ErrorListener {
            Toast.makeText(this@MainActivity,"Something Went Wrong !",Toast.LENGTH_SHORT).show()

        }){
            override fun getHeaders(): MutableMap<String, String> {
                val params : MutableMap<String,String> =HashMap()
                params["Content-Type"] = "application/json"
                params["Authorization"] = "Bearer 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"
                return params
            }
        }
        postRequest.setRetryPolicy(object : RetryPolicy{
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }
        })
        queue.add(postRequest)
    }
}