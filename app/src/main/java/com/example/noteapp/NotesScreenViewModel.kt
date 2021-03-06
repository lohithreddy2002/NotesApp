package com.example.noteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NotesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesScreenViewModel @Inject constructor(private val repository: NotesScreenRepository) :
    ViewModel() {
    private val _allNotes = MutableStateFlow(NotesScreenState())
    val allNotes = _allNotes.asStateFlow()


    init {
        viewModelScope.launch {
            delay(2000)
            repository.dao.getNotes().collectLatest {
                _allNotes.value = _allNotes.value.copy(isLoading = false, notesList = it)
            }

        }

    }
}

data class NotesScreenState(
    val isLoading: Boolean = true,
    val notesList: List<NotesItem> = listOf()
)