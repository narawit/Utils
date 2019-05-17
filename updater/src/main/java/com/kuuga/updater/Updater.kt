package com.kuuga.android.updater

import android.os.AsyncTask
import org.jsoup.Jsoup

class Updater(private val packageName: String = "", private val currentVersion: String = "") :
    AsyncTask<Void, String, String>() {

    var mListener: OnUpdaterListener? = null

    fun check(listener: OnUpdaterListener) {
        mListener = listener
        execute()
    }

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
            mListener?.onFailed(VersionFormatException())
            newVersion
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null && result.isNotEmpty()) {
            val googleVersions = result.split(".")
            val myVersions = currentVersion.split(".")

            if (googleVersions.size == myVersions.size) {
                var more = false
                var start = 0
                repeat(myVersions.size) { i ->
                    val m: Int
                    val g: Int

                    try {
                        m = myVersions[i].toInt()
                        g = googleVersions[i].toInt()

                        if (start == 0) {
                            if (g > m) {
                                more = true
                            }
                        } else {
                            if (!more) {
                                if (g > m) {
                                    more = true
                                }
                            }
                        }

                    } catch (e: NumberFormatException) {
                        more = false
                        start = i
                    }
                }

                if (more) {
                    mListener?.onLessGooglePlay()
                } else {
                    mListener?.onEqualGooglePlay()
                }
            } else {
                mListener?.onFailed(VersionFormatException())
            }
        }
    }
}