package com.example.noteapp

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.noteapp.data.NotesItem
import com.example.noteapp.ui.theme.NoteAppTheme
import kotlinx.coroutines.launch
import org.commonmark.node.Document
import org.commonmark.parser.Parser
import org.intellij.lang.annotations.Language
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen() {
    val state = rememberScaffoldState()
    val list = mutableListOf<NotesItem>()
    val scope = rememberCoroutineScope()
    for (i in 1..4) {
        list.add(NotesItem(noteDesc = "Description is $i", noteTitle = "$i", noteColorCode = i))
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Notes") }, navigationIcon = {
            IconButton(onClick = { scope.launch { state.drawerState.open() } }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        })
    }, scaffoldState = state, drawerContent = {
        Text(text = "Drawer opened")
    }, bottomBar = {

    }, modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
    ) { i ->
        NotesList(list = list)
    }
}


@Composable
fun NotesList(list: List<NotesItem>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(list) {
            NormalNoteItem(it)
        }
    }
}


@Composable
fun SwipeBackground() {
    IconButton(onClick = {

    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
            contentDescription = "delete"
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteItem(notesItem: NotesItem) {
    val squareSize = 100.dp
    val parser = Parser.builder().build()
    val state = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    Box(
        Modifier
            .fillMaxSize()
            .swipeable(
                state,
                anchors,
                orientation = Orientation.Horizontal,
                thresholds = { _, _ -> FractionalThreshold(0.3f) }),
    ) {
        Row(
            modifier = Modifier
                .width(120.dp)
                .height(200.dp)
                .background(NotesItem.colorList[notesItem.noteColorCode % 4])
        ) {
            SwipeBackground()
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(state.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
                .clip(
                    AbsoluteCutCornerShape(topRightPercent = 15)
                )
                .background(NotesItem.colorList[notesItem.noteColorCode % 4])
        ) {
            val root = parser.parse(MIXED_MD) as Document
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MDDocument(root)
            }
        }

    }
}


@Composable
fun NormalNoteItem(notesItem: NotesItem) {
    val parser = Parser.builder().build()
    val root = parser.parse(MIXED_MD) as Document
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(10))
            .background(Color.Red)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            MDDocument(document = root)
        }
    }
}


@Preview
@Composable
fun NormalItemPreview() {
    NoteAppTheme {
        Surface {
            NormalNoteItem(NotesItem(1, "", "", 1))
        }
    }
}

@Preview("Notes Screen", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreview() {
    NoteAppTheme {
        Surface {
            NotesScreen()
        }
    }
}

@Preview("Notes List", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NotesListPreview() {
    NoteAppTheme {
        Surface {
            NotesList(listOf())
        }
    }
}

@Preview("Notes Item", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NoteItemPreview() {
    NoteAppTheme {
        Surface {
            NoteItem(NotesItem(noteDesc = "Description is i", noteTitle = "i", noteColorCode = 1))
        }
    }
}

@Language("Markdown")
const val MIXED_MD = """
### Markdown Header

This is regular text without formatting in a single paragraph.

![Serious](file:///android_asset/serios.jpg)

Images can also be inline: ![Serious](file:///android_asset/serios.jpg). [Links](http://hellsoft.se) and `inline code` also work. This *is* text __with__ inline styles for *__bold and italic__*. Those can be nested.

Here is a code block:
```javascript
function codeBlock() {
    return true;
}
```

+ Bullet
+ __Lists__
+ Are
+ *Cool*

1. **First**
1. *Second*
1. Third
1. [Fourth is clickable](https://google.com)  
   1. And
   1. Sublists
1. Mixed
   - With
   - Bullet
   - Lists

100) Lists
100) Can
100) Have
100) *Custom*
100) __Start__
100) Numbers

- List
- Of
- Items
  - With
  - Sublist

> A blockquote is useful for quotes!

"""
