package info.androidhive.glide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.androidhive.glide.model.Image
import info.androidhive.glide.repository.Repository

class ListViewModel : ViewModel() {
    var imagesLiveData: MutableLiveData<ArrayList<Image>?> = MutableLiveData()
    private var repository = Repository()
    fun fetchImages() {
        repository.fetchImages {images->
            imagesLiveData.postValue(images)
        }
    }
}