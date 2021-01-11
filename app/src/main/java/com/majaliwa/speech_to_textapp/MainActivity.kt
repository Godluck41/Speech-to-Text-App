package com.majaliwa.speech_to_textapp

import android.app.Activity
import android.content.Intent
import com.google.android.material.snackbar.Snackbar
import com.hiteshsahu.stt_tts.translation_engine.ConversionCallback
import com.majaliwa.speech_to_textapp.translation_engine.TranslatorFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : BasePermissionActivity() {

    override fun getActivityLayout(): Int {
        return R.layout.activity_main
    }


    override fun setUpView() {



        //SPEECH TO TEXT DEMO
        speechToText.setOnClickListener { view ->

            Snackbar.make(view, "Speak now, App is listening", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            TranslatorFactory
                .instance
                .with(
                    TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT,
                    object : ConversionCallback {
                        override fun onSuccess(result: String) {
                            sttOutput.text = result
                        }

                        override fun onCompletion() {
                        }

                        override fun onErrorOccurred(errorMessage: String) {
                            erroConsole.text = "Speech2Text Error: $errorMessage"
                        }

                    }).initialize("Speak Now !!", this@MainActivity)

        }


        //TEXT TO SPEECH DEMO
        textToSpeech.setOnClickListener { view ->

            val stringToSpeak: String = ttsInput.text.toString()

            if (null != stringToSpeak && stringToSpeak.isNotEmpty()) {

                TranslatorFactory
                    .instance
                    .with(
                        TranslatorFactory.TRANSLATORS.TEXT_TO_SPEECH,
                        object : ConversionCallback {
                            override fun onSuccess(result: String) {
                            }

                            override fun onCompletion() {
                            }

                            override fun onErrorOccurred(errorMessage: String) {
                                erroConsole.text = "Text2Speech Error: $errorMessage"
                            }

                        })
                    .initialize(stringToSpeak, this)

            } else {
                ttsInput.setText("Invalid input")
                Snackbar.make(view, "Please enter some text to speak", Snackbar.LENGTH_LONG).show()
            }

        }

    }

    fun findString(listOfPossibleMatches: ArrayList<String>?, stringToMatch: String): Boolean {

        if (null != listOfPossibleMatches) {

            for (transaltion in listOfPossibleMatches) {

                if (transaltion.contains(stringToMatch)) {

                    return true
                }
            }
        }
        return false
    }

    /**
     * Share on social media
     *
     * @param messageToShare message To Share
     * @param activity       context
     */
    fun share(messageToShare: String, activity: Activity) {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageToShare)
        activity.startActivity(Intent.createChooser(shareIntent, "Share using"))
    }
}
