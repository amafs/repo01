package info.androidhive.glide.repository

import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import info.androidhive.glide.app.AppController
import info.androidhive.glide.model.Image
import org.json.JSONException

class Repository {
    private val TAG = Repository::class.java.simpleName
    private var images: ArrayList<Image>? = ArrayList()
    fun fetchImages(onDataGet: (ArrayList<Image>) -> Unit) {
        val req = JsonArrayRequest(
            endpoint,
            { response ->
                Log.d("Repo", response.length().toString())
                if (response != null) {
                    for (Index in 0 until response.length()) {
                        try {
                            val jsonObject = response.getJSONObject(Index)
                            val image = Image()
                            image.name = jsonObject.getString("name")
                            val jsonUrl = jsonObject.getJSONObject("url")
                            image.small = jsonUrl.getString("small")
                            image.medium = jsonUrl.getString("medium")
                            image.large = jsonUrl.getString("large")
                            image.timestamp = jsonObject.getString("timestamp")
                            images!!.add(image)
                            Log.d("Repo", "Sucessssss")
                        } catch (e: JSONException) {
                            Log.e(TAG, "Json parsing error: " + e.message)
                        }
                        onDataGet(images!!)
                        Log.d("Repo", images!!.size.toString())
                    }
                }
            }) { error ->
            Log.e(TAG, "Error: " + error.message)
        }

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(req)

    }

    companion object {
        private const val endpoint = "https://api.androidhive.info/json/glide.json"
    }

}