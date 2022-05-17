package info.androidhive.glide.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.androidhive.glide.adapter.GalleryAdapter
import info.androidhive.glide.databinding.FragmentRecylerviewBinding
import info.androidhive.glide.model.Image
import info.androidhive.glide.viewmodel.ImagesViewModel
import timber.log.Timber

class ImagesFragment(var position: Int) : Fragment() {
    private val TAG = ImagesFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter: GalleryAdapter? = null
    private var _binding: FragmentRecylerviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ImagesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        progressDialog = ProgressDialog(this.activity)
        images = ArrayList<Image>()
        _binding = FragmentRecylerviewBinding.inflate(inflater, container, false)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = GalleryAdapter(requireContext(), images!!, position)
        binding.recyclerView.adapter = adapter
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addOnItemTouchListener(
            GalleryAdapter.RecyclerTouchListener(
                context,
                binding.recyclerView, object : GalleryAdapter.ClickListener {
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
        viewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)
        showProgressBar()
        viewModel.fetchImages()
        viewModel.imagesLiveData.observe(viewLifecycleOwner) { images ->
            this.images?.clear()
            this.images?.addAll(images!!)
            Timber.d(this.images?.size.toString())
            adapter!!.notifyDataSetChanged()
            progressDialog!!.hide()
        }
        return binding.root
    }

    private fun showProgressBar() {
        progressDialog!!.setMessage("Downloading json...")
        progressDialog!!.show()
    }

    fun changeLayout() {
        when (position) {
            GRID -> {
                val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
                binding.recyclerView.layoutManager = layoutManager
            }
            LIST -> {
                val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
                binding.recyclerView.layoutManager = layoutManager
            }
        }
        adapter = GalleryAdapter(requireContext(), images!!, position)
        binding.recyclerView.adapter = adapter
    }
}