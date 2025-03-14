package com.example.myapplication.components.noteDetail

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    onBackClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        },
        actions = {
            IconButton(onClick = onMoreClick) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Submit"
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Title(title: String, color: Color, onQueryChange: (String) -> Unit) {
    val currentDateTime by remember {
        mutableStateOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // 🟢 Giới hạn chiều cao, tránh lấn chiếm Content
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
    ) {
        Column {
            TextField(
                value = title,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Tiêu đề",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = color,
                    unfocusedContainerColor = color.copy(alpha = 0.9f),
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = currentDateTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }
    }
}




@Composable
fun Content(text: String, color: Color, onQueryChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Bắt đầu soạn...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        },

        colors = TextFieldDefaults.colors(
            focusedContainerColor = color, // 🟢 Sử dụng color làm màu nền khi focus
            unfocusedContainerColor = color.copy(alpha = 0.9f), // 🔵 Giảm độ trong suốt khi không focus
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent, // 🟠 Ẩn gạch chân khi focus
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteContent(
    titleText: String,
    onTitleChange: (String) -> Unit,
    contentText: String,
    onContentChange: (String) -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier // Thêm mặc định để tránh lỗi
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp) // Padding được xử lý bởi Scaffold
            .background(backgroundColor)
    ) {
        Title(titleText, backgroundColor) { onTitleChange(it) }
        Content(contentText, backgroundColor) { onContentChange(it) }
    }
}


// Hàm ColorPicker (danh sách màu)
@Composable
fun ColorPicker(onColorSelected: (Color) -> Unit) {
    val context = LocalContext.current
    val resources: Resources = context.resources

    val colors = remember {
        val typedArray = resources.obtainTypedArray(R.array.color_picker)
        val colorList = List(typedArray.length()) { index ->
            val colorResId = typedArray.getResourceId(index, 0) // 🟢 Lấy ID màu
            if (colorResId != 0) {
                Color(ResourcesCompat.getColor(resources, colorResId, context.theme)) // 🔥 Chuyển ID -> Color
            } else {
                Color.White
            }
        }
        typedArray.recycle() // 🔄 Giải phóng bộ nhớ sau khi sử dụng
        colorList
    }

    var selectedColor: Color = colors.firstOrNull() ?: Color.White

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(colors.size) { index ->  // Truyền số lượng items thay vì danh sách
            val color = colors[index]  // Lấy màu từ danh sách

            val size by animateDpAsState(
                if (color == selectedColor) 50.dp else 40.dp,
                label = "colorSize"
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (color == selectedColor) 2.dp else 0.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
                    .clickable {
                        selectedColor = color
                        onColorSelected(color)
                    }
            )
        }
    }
}



