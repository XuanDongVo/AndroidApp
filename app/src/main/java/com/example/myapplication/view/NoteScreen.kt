package com.example.myapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.components.noteList.NoteList
import com.example.myapplication.viewmodel.NoteViewModel

@Composable
fun NoteScreen(navController: NavController, noteModel: NoteViewModel) {
    var query by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Tất cả") }
    Column(modifier = Modifier.fillMaxSize()) {
        // Thanh tìm kiếm
        SearchBar(query, onQueryChange = { query = it })

        // LazyRow cho các tab và biểu tượng thư mục
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các thành phần
        ) {
            // Các tab
            items(listOf("Tất cả", "Thư mục Ghi chú", "Chưa phân")) { tab: String ->
                TabItem(
                    text = tab,
                    isSelected = selectedTab == tab,
                    onClick = { selectedTab = tab }
                )
            }

            // Biểu tượng thư mục
            item {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Chọn thư mục",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
            }
        }

        // Danh sách ghi chú
        NoteList(
            noteModel = noteModel,
            navController = navController,
            onNewNoteClick = { navController.navigate("newNoteScreen") } // Truyền hành động FAB
        )
    }
}
@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (isSelected) Modifier else Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
    )
}
//@Composable
//fun FloatingNewNoteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
//    FloatingActionButton(
//        onClick = onClick,
//        modifier = modifier,
//        containerColor = MaterialTheme.colorScheme.primaryContainer
//    ) {
//        Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm ghi chú")
//    }
//}

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


