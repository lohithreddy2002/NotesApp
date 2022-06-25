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
class EditorViewModel @Inject constructor(private val repository: EditorRepository) : ViewModel() {

    private val _editorStateFlow = MutableStateFlow(EditorState())
    val editorStateFlow = _editorStateFlow.asStateFlow()

    private val _itemFlow = MutableStateFlow(ItemState())
    val itemFlow = _itemFlow.asStateFlow()

    fun createNote() {
        _editorStateFlow.value = _editorStateFlow.value.copy(loading = true)
        viewModelScope.launch {
            val item = NotesItem(
                noteDesc = _editorStateFlow.value.noteText,
                noteTitle = "",
                noteColorCode = _editorStateFlow.value.selectedColor - 1
            )
            repository.addNote(item)
        }
        _editorStateFlow.value = _editorStateFlow.value.copy(noteAdded = true)
    }

    fun changeSelectedColor(id: Int) {
        _editorStateFlow.value = _editorStateFlow.value.copy(selectedColor = id)
    }

    fun changeEditState() {
        _editorStateFlow.value =
            _editorStateFlow.value.copy(isEditing = !_editorStateFlow.value.isEditing)

    }

    fun getSingleNoteItem(id: Int) {
        viewModelScope.launch {
            val item = repository.getSingleNote(id)
            _editorStateFlow.value = _editorStateFlow.value.copy(
                selectedColor = item.noteColorCode + 1,
                noteText = item.noteDesc
            )
        }
    }

    fun changeText(text: String) {
        _editorStateFlow.value = _editorStateFlow.value.copy(noteText = text)
    }

    fun updateNote(id: Int) {
        _editorStateFlow.value = _editorStateFlow.value.copy(loading = true)
        viewModelScope.launch {
            repository.updateNote(
                NotesItem(
                    id,
                    noteDesc = _editorStateFlow.value.noteText,
                    noteTitle = "",
                    noteColorCode = _editorStateFlow.value.selectedColor - 1
                )
            )
        }
        _editorStateFlow.value = _editorStateFlow.value.copy(noteAdded = true)
    }


}


data class EditorState(
    val isEditing: Boolean = true,
    val loading: Boolean = false,
    val selectedColor: Int = 0,
    val noteAdded: Boolean = false,
    val noteText: String = ""

)

data class ItemState(
    val item: NotesItem? = null
)