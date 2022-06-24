package com.example.noteapp

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.theme.NoteAppTheme
import org.commonmark.node.Document
import org.commonmark.parser.Parser

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditorScreen(viewModel: EditorViewModel, navController: NavController, id: Int? = null) {
    val editorState = viewModel.editorStateFlow.collectAsState()
    val parser = Parser.builder().build()
    var text by remember {
        mutableStateOf("")
    }
    if (id != null) {
        viewModel.getSingleNoteItem(id)
        val item = viewModel.itemFlow.collectAsState()
        text = item.value.item?.noteDesc.toString()
        viewModel.changeSelectedColor(item.value.item?.noteColorCode?.plus(1) ?: 0)
    }
    val context = LocalContext.current
    if (editorState.value.noteAdded) {
        navController.navigateUp()
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val state = rememberScaffoldState()
    Column {
        Row(modifier = Modifier.padding(4.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (editorState.value.selectedColor != 0) {
                    viewModel.createNote(text)
                } else {
                    showToast(context, "Select any one of the Colors")
                }

            }, enabled = !editorState.value.loading) {
                Text(text = "Submit")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.changeEditState()
                keyboard?.hide()
            }, enabled = !editorState.value.loading) {
                if (editorState.value.isEditing) {
                    Text(text = "Check")
                } else {
                    Text(text = "Edit")

                }

            }
            ColorItem(selected = editorState.value.selectedColor == 1, color = Color.Yellow) {
                viewModel.changeSelectedColor(1)
            }
            ColorItem(selected = editorState.value.selectedColor == 2, color = Color.Red) {
                viewModel.changeSelectedColor(2)
            }
            ColorItem(selected = editorState.value.selectedColor == 3, color = Color.Magenta) {
                viewModel.changeSelectedColor(3)
            }
            ColorItem(selected = editorState.value.selectedColor == 4, color = Color.Green) {
                viewModel.changeSelectedColor(4)
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .border(
                    1.dp, Color.Gray,
                    RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (editorState.value.isEditing) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text
                    ),
                    decorationBox = {
                        if (text == "") {
                            Text(text = "Your Note")
                        }
                        it()
                    }
                )
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (text == "") {
                        val root = parser.parse("### EMPTY") as Document
                        MDDocument(root)
                    } else {
                        val root = parser.parse(text) as Document
                        MDDocument(root)
                    }
                }

            }
        }
    }
}

fun showToast(context: Context, message: String) {
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.BOTTOM, 0, 0)
    toast.show()
}

@Preview
@Composable
fun EditorPreview() {
    NoteAppTheme() {
        Surface {
            EditorScreen(navController = rememberNavController(), viewModel = viewModel())
        }
    }
}