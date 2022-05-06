package info.androidhive.glide.app

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class AppController : Application() {
    private var requestQueue: RequestQueue? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val reqQueue: RequestQueue
        get() {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(applicationContext)
            }
            return requestQueue!!
        }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        reqQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        reqQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any?) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        val TAG = AppController::class.java
            .simpleName

        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}