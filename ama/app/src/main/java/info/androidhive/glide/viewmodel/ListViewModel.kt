package info.androidhive.glide.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.androidhive.glide.model.Image
import info.androidhive.glide.repository.Repository

class ListViewModel(private val repository:Repository):ViewModel() {
    var imagesLiveData: MutableLiveData<ArrayList<Image>?> = MutableLiveData()
    fun fetchImages(){
        val images = repository.fetchImages()
        imagesLiveData.postValue(images)
    }
}