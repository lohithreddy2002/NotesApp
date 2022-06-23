package com.example.noteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NotesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(val repository: EditorRepository) : ViewModel() {

    private val _editorStateFlow = MutableStateFlow(EditorState())
    val editorStateFlow = _editorStateFlow.asStateFlow()

    fun createNote(text: String) {
        _editorStateFlow.value = _editorStateFlow.value.copy(loading = true)
        viewModelScope.launch {
            val item = NotesItem(
                noteDesc = text,
                noteTitle = "",
                noteColorCode = _editorStateFlow.value.selectedColor - 1
            )
            repository.addNote(item)
        }
    }

    fun changeSelectedColor(id: Int) {
        _editorStateFlow.value = _editorStateFlow.value.copy(selectedColor = id)
    }

    fun changeEditState() {
        _editorStateFlow.value =
            _editorStateFlow.value.copy(isEditing = !_editorStateFlow.value.isEditing)

    }


}


data class EditorState(
    val isEditing: Boolean = true,
    val loading: Boolean = false,
    val selectedColor: Int = 0,
    val noteAdded: Boolean = false

)