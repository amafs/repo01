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
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import info.androidhive.glide.adapter.GalleryListAdapter
import info.androidhive.glide.app.AppController
import info.androidhive.glide.databinding.FragmentListBinding
import info.androidhive.glide.model.Image
import info.androidhive.glide.viewmodel.ListViewModel
import org.json.JSONException

class ListFragment : Fragment() {
    private val TAG = ListFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter: GalleryListAdapter? = null
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        progressDialog = ProgressDialog(this.activity)
        images = ArrayList<Image>()
        adapter = GalleryListAdapter(requireContext(), images!!)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context)//GridLayoutManager(context, 1)
        binding.listView.layoutManager = layoutManager
        binding.listView.itemAnimator = DefaultItemAnimator()
        binding.listView.adapter = adapter
        binding.listView.addOnItemTouchListener(
            GalleryListAdapter.RecyclerTouchListener(
                context,
                binding.listView, object : GalleryListAdapter.ClickListener {
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
                        val toast = Toast.makeText(
                            context,
                            "電影名稱：" + image.name + "\n日期：" + image.timestamp,
                            Toast.LENGTH_SHORT
                        )
                        toast.setGravity(Gravity.TOP, 0, 100)
                        //Android 11開始不支援
                        //在 targetSdkVersion 為 R 或更高時，呼叫 setGravity 和 setMargin 方法將不進行任何操作
                        toast.show()
                    }
                })
        )
        listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        showProgressBar()
        listViewModel.fetchImages()
        listViewModel.imagesLiveData.observe(viewLifecycleOwner) { imagesLiveData ->
            images!!.clear()
            images = imagesLiveData
            adapter!!.notifyDataSetChanged()
            progressDialog!!.hide()
        }
        return view
    }

    private fun fetchImages() {
        progressDialog!!.setMessage("Downloading json...")
        progressDialog!!.show()
        val req = JsonArrayRequest(
            endpoint,
            { response ->
                Log.d(TAG, response.toString())
                progressDialog!!.hide()
                images!!.clear()
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        val image = Image()
                        image.name = jsonObject.getString("name")
                        val jsonUrl = jsonObject.getJSONObject("url")
                        image.small = jsonUrl.getString("small")
                        image.medium = jsonUrl.getString("medium")
                        image.large = jsonUrl.getString("large")
                        image.timestamp = jsonObject.getString("timestamp")
                        images!!.add(image)
                    } catch (e: JSONException) {
                        Log.e(TAG, "Json parsing error: " + e.message)
                    }
                }
                adapter!!.notifyDataSetChanged()
            }) { error ->
            Log.e(TAG, "Error: " + error.message)
            progressDialog!!.hide()
        }

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(req)
    }

    private fun showProgressBar() {
        progressDialog!!.setMessage("Downloading json...")
        progressDialog!!.show()
    }

    companion object {
        private const val endpoint = "https://api.androidhive.info/json/glide.json"
    }

}