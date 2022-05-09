package info.androidhive.glide.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.androidhive.glide.adapter.GalleryGridAdapter
import info.androidhive.glide.databinding.FragmentGridBinding
import info.androidhive.glide.model.Image
import info.androidhive.glide.viewmodel.GridViewModel

class GridFragment : Fragment() {
    private val TAG = GridFragment::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var progressDialog: ProgressDialog? = null
    private var adapter: GalleryGridAdapter? = null
    private var _binding: FragmentGridBinding? = null//view binding
    private val binding get() = _binding!!
    private lateinit var gridViewModel: GridViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGridBinding.inflate(inflater, container, false)
        val view = binding.root
        progressDialog = ProgressDialog(this.activity)
        images = ArrayList<Image>()
        adapter = GalleryGridAdapter(requireContext(), images!!)//call GalleryGridAdapter
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnItemTouchListener(
            GalleryGridAdapter.RecyclerTouchListener(
                context,
                binding.recyclerView, object : GalleryGridAdapter.ClickListener {
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
        gridViewModel = ViewModelProviders.of(this).get(GridViewModel::class.java)
        showProgressBar()

        gridViewModel.fetchImages()
        gridViewModel.imagesLiveData.observe(viewLifecycleOwner) { images ->
            this.images?.clear()
            this.images?.addAll(images!!)
            Log.d("grid",this.images?.size.toString())
            adapter!!.notifyDataSetChanged()
            progressDialog!!.hide()
        }
        return view
    }

    private fun showProgressBar() {
        progressDialog!!.setMessage("Downloading json...")
        progressDialog!!.show()
    }

}