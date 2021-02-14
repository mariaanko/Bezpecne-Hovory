package com.wordpress.mariaanko.bezpecnehovory

import android.Manifest
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askForPermissions()

        val button: Button = findViewById(R.id.button)
        val webView: WebView = findViewById(R.id.webView)
        var phoneNumber: String? = null
        if(intent.extras != null) phoneNumber = intent.extras?.getString("number").toString()

        if(phoneNumber!=null){
            webView.loadUrl("https://www.vyhladavaniecisla.sk/cislo/" + phoneNumber)
        }else webView.loadUrl("https://www.vyhladavaniecisla.sk/")

        intent.removeExtra("number")
        phoneNumber = null

        button.setOnClickListener(){
            this.finish()
        }
    }

    fun askForPermissions() = runWithPermissions(Manifest.permission.READ_PHONE_STATE,
                                                    Manifest.permission.READ_CALL_LOG,
                                                    Manifest.permission.READ_CONTACTS) {

    }
}