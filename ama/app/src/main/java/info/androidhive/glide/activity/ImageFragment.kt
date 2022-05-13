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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.androidhive.glide.adapter.GalleryAdapter
import info.androidhive.glide.databinding.FragmentRecylerviewBinding
import info.androidhive.glide.model.Image
import info.androidhive.glide.viewmodel.FragmentViewModel
import timber.log.Timber


class ImageFragment(private val showType: String) : Fragment() {
    private val TAG = ImageFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter: GalleryAdapter? = null
    private var _binding: FragmentRecylerviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        progressDialog = ProgressDialog(this.activity)
        images = ArrayList<Image>()
        _binding = FragmentRecylerviewBinding.inflate(inflater, container, false)

        if (showType.equals("List")) {
            showList()
        }
        if (showType.equals("Grid")) {
            showGrid()
        }

        return binding.root
    }

    private fun showProgressBar() {
        progressDialog!!.setMessage("Downloading json...")
        progressDialog!!.show()
    }

    private fun showList() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = GalleryAdapter(requireContext(), images!!, showType)
        binding.recyclerView.adapter = adapter
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
        viewModel = ViewModelProviders.of(this).get(FragmentViewModel::class.java)
        showProgressBar()
        viewModel.fetchImages()
        viewModel.imagesLiveData.observe(viewLifecycleOwner) { images ->
            this.images?.clear()
            this.images?.addAll(images!!)
            Timber.d(this.images?.size.toString())
            adapter!!.notifyDataSetChanged()
            progressDialog!!.hide()
        }
    }

    private fun showGrid() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        adapter = GalleryAdapter(requireContext(), images!!, showType)
        binding.recyclerView.adapter = adapter
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

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
        viewModel = ViewModelProviders.of(this).get(FragmentViewModel::class.java)
        showProgressBar()
        viewModel.fetchImages()
        viewModel.imagesLiveData.observe(viewLifecycleOwner) { images ->
            this.images?.clear()
            this.images?.addAll(images!!)
            Timber.d(this.images?.size.toString())
            adapter!!.notifyDataSetChanged()
            progressDialog!!.hide()
        }
    }

}