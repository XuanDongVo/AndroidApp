package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.components.noteList.NoteList
import com.example.myapplication.viewmodel.NoteViewModel

@Composable
fun NoteScreen(navController: NavController,  noteModel: NoteViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Thanh tìm kiếm
            SearchBar(query, onQueryChange = { query = it })

            // Danh sách ghi chú
            NoteList(noteModel = noteModel)
        }

        // Nút FAB đặt ở góc phải dưới cùng
        FloatingNewNoteButton(
             onClick = { navController.navigate("newNoteScreen") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(18.dp)
        )
    }
}

@Composable
fun FloatingNewNoteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm ghi chú")
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        placeholder = { Text(text = "Tìm kiếm...") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

// ✅ Preview với NavController giả lập
@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    val fakeNavController = rememberNavController()
    NoteScreen(navController = fakeNavController)
}
