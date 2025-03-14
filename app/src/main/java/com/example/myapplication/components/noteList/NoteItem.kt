package com.example.myapplication.components.noteList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.myapplication.model.Note


@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    val backgroundColor = Color(note.colorBackGround)
//    val hasImage = note.imageResId?.let { it > 0 } ?: false

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)

    ) {
        // Column chứa tiêu đề, nội dung và ngày tháng
        Column(
            modifier = Modifier.weight(1f) // Chiếm toàn bộ không gian còn lại nếu có ảnh
        ) {
            Text(
                text = note.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()


            )

            Text(
                text = note.content ?: "Không có nội dung",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Light,
                maxLines = 1, // Chỉ hiển thị 1 dòng
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
            )

            Text(
                text = note.date.toString(),
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 4.dp)


            )
        }

        // Nếu có ảnh, hiển thị nó với trọng số nhỏ hơn
//        if (hasImage) {
//            AsyncImage(
//                model = "https://fakeimg.pl/300/",
//                contentDescription = "Note Image",
//                modifier = Modifier
//                    .weight(0.3f) // Ảnh chiếm khoảng 30% chiều rộng
//                    .padding(start = 8.dp)
//            )
//        }
    }
}


@Composable
fun AsyncImage(model: String, contentDescription: String, modifier: Modifier) {
    TODO("Not yet implemented")
}
