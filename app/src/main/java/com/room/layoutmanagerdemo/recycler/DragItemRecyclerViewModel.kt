package com.room.layoutmanagerdemo.recycler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DragItemRecyclerViewModel : ViewModel() {
    public val dragItemLiveData = MutableLiveData<List<DragItemData>>()
}