package info.androidhive.glide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.androidhive.glide.model.Image
import info.androidhive.glide.repository.Repository

public class GridViewModel(private val repository: Repository) : ViewModel() {
    var imagesLiveData: MutableLiveData<ArrayList<Image>?> = MutableLiveData()
    public fun fetchImages() {
        val images = repository.fetchImages()
        imagesLiveData.postValue(images)
    }
}