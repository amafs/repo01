package info.androidhive.glide.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import info.androidhive.glide.adapter.GalleryAdapter_list
import info.androidhive.glide.app.AppController
import info.androidhive.glide.model.Image
import org.json.JSONException
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager
import info.androidhive.glide.databinding.FragmentListBinding

class ListFragment :Fragment(){
    private val TAG = ListFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var pDialog: ProgressDialog? = null
    private var mAdapter: GalleryAdapter_list? = null
    //private var recyclerView: RecyclerView? = null
    private var _binding:FragmentListBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{
        _binding= FragmentListBinding.inflate(inflater,container,false)
        val view =binding.root
        //al v : View= inflater.inflate(R.layout.fragment_list, container, false)
        //recyclerView = v.findViewById<View>(R.id.list_view) as RecyclerView
        pDialog = ProgressDialog(this.activity)
        images = ArrayList<Image>()
        mAdapter = GalleryAdapter_list(requireContext(), images!!)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)//GridLayoutManager(context, 1)
        //recyclerView!!.layoutManager = mLayoutManager
        binding.listView!!.layoutManager=mLayoutManager
        binding.listView!!.itemAnimator = DefaultItemAnimator()
        binding.listView!!.adapter = mAdapter
        binding.listView!!.addOnItemTouchListener(
            GalleryAdapter_list.RecyclerTouchListener(
                context,
                binding.listView!!, object : GalleryAdapter_list.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable("images", images)
                        bundle.putInt("position", position)
                        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                        val newFragment: SlideshowDialogFragment =
                            SlideshowDialogFragment.newInstance()
                        newFragment.setArguments(bundle)
                        newFragment.show(ft, "slideshow")
                    }

                    override fun onLongClick(view: View?, position: Int) {
                        val image = images!![position]
                        val toast= Toast.makeText(context,
                            "電影名稱："+image.name+"\n日期："+image.timestamp,
                            Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.TOP,0,100)
                        //Android 11開始不支援
                        //在 targetSdkVersion 為 R 或更高時，呼叫 setGravity 和 setMargin 方法將不進行任何操作
                        toast.show()
                    }
                })
        )
        fetchImages()
        return view
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