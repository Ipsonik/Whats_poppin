package com.example.whatspopin.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.whatspopin.models.PopItem
import com.example.whatspopin.repository.FunkoRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FunkoViewModel(private val funkoRepository: FunkoRepository) : ViewModel() {

    var pops: MutableLiveData<MutableList<PopItem>> = MutableLiveData()
    val _pop = MutableLiveData<PopItem>()
    var filterText: MutableLiveData<String> = MutableLiveData()
    val isListFiltered: MutableLiveData<Boolean> = MutableLiveData(false)


    fun fetchData(): MutableLiveData<MutableList<PopItem>> {
        viewModelScope.launch(IO) {
            pops.postValue(funkoRepository.getFunkoPops())
        }
        return pops
    }

    fun filter() {
        isListFiltered.value = filterText.value?.length!! > 1
    }


    fun getFilteredList(text: String): List<PopItem> {
        val filteredList = arrayListOf<PopItem>()
        for (pop: PopItem in pops.value!!) {
            if (pop.name.lowercase().contains(text.lowercase()) || pop.series.lowercase()
                    .contains(text.lowercase())
            ) {
                filteredList.add(pop)
            }
        }
        return filteredList
    }
    fun onPopClicked(pop: PopItem){
        _pop.value = pop
    }

    class FunkoViewModelFactory(private val funkoRepository: FunkoRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FunkoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FunkoViewModel(funkoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

