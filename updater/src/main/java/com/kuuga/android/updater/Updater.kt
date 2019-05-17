package com.kuuga.android.updater

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup

class Updater(private val packageName: String = "", private val currentVersion: String = "") :
    AsyncTask<Void, String, String>() {

    var update: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<Throwable> = MutableLiveData()

    override fun doInBackground(vararg params: Void?): String? {
        var newVersion: String? = null
        return try {
            val document = Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName")
                .timeout(30000)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get()
            if (document != null) {
                val element = document.getElementsContainingOwnText("Current Version")
                for (ele in element) {
                    if (ele.siblingElements() != null) {
                        val sibElements = ele.siblingElements()
                        for (sibElement in sibElements) {
                            newVersion = sibElement.text()
                        }
                    }
                }
            }
            newVersion
        } catch (e: Exception) {
            error.postValue(e)
            newVersion
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null && result.isNotEmpty()) {
            update.postValue(currentVersion.toFloat() >= result.toFloat())
        }
    }
}