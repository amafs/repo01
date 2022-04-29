package info.androidhive.glide.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import info.androidhive.glide.R
import info.androidhive.glide.adapter.GalleryAdapter
import info.androidhive.glide.app.AppController
import info.androidhive.glide.model.Image
import org.json.JSONException
import androidx.fragment.app.FragmentTransaction;

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var pDialog: ProgressDialog? = null
    private var mAdapter: GalleryAdapter? = null
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        pDialog = ProgressDialog(this)
        images = ArrayList<Image>()
        mAdapter = GalleryAdapter(applicationContext, images!!)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addOnItemTouchListener(
            GalleryAdapter.RecyclerTouchListener(
                applicationContext,
                recyclerView!!, object : GalleryAdapter.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable("images", images)
                        bundle.putInt("position", position)
                        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                        val newFragment: SlideshowDialogFragment =
                            SlideshowDialogFragment.newInstance()
                        newFragment.setArguments(bundle)
                        newFragment.show(ft, "slideshow")
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
        fetchImages()
    }

    private fun fetchImages() {
        pDialog!!.setMessage("Downloading json...")
        pDialog!!.show()
        val req = JsonArrayRequest(
            endpoint,
            { response ->
                Log.d(TAG, response.toString())
                pDialog!!.hide()
                images!!.clear()
                for (i in 0 until response.length()) {
                    try {
                        val `object` = response.getJSONObject(i)
                        val image = Image()
                        image.name=`object`.getString("name")
                        val url = `object`.getJSONObject("url")
                        image.small=url.getString("small")
                        image.medium=url.getString("medium")
                        image.large=url.getString("large")
                        image.timestamp=`object`.getString("timestamp")
                        images!!.add(image)
                    } catch (e: JSONException) {
                        Log.e(TAG, "Json parsing error: " + e.message)
                    }
                }
                mAdapter!!.notifyDataSetChanged()
            }) { error ->
            Log.e(TAG, "Error: " + error.message)
            pDialog!!.hide()
        }

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(req)
    }

    companion object {
        private const val endpoint = "https://api.androidhive.info/json/glide.json"
    }
}