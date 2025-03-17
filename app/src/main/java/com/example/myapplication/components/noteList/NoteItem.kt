package com.example.myapplication.components.noteList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Note
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Composable
fun SwipeableNoteItem(
    note: Note,
    onDelete: (Note) -> Unit,
    onClick: () -> Unit
) {
    val animatableOffsetX = remember { Animatable(0f) }
    val deleteThreshold = -50f // Giảm ngưỡng để dễ hiển thị icon
    val scope = rememberCoroutineScope()
    var isSwiped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent) // Đặt nền trong suốt để tránh lấn màu
    ) {
        // Icon Delete (cố định bên phải)
        IconButton(
            onClick = {
                onDelete(note)
                scope.launch { animatableOffsetX.animateTo(0f) }
                isSwiped = false
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
//                .padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete icon",
                tint = Color.Red
            )
        }

        // NoteItem chỉ dịch sang trái một chút
        NoteItem(
            note = note,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        if (animatableOffsetX.value <= deleteThreshold) -120 else animatableOffsetX.value.toInt(),
                        0
                    )
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (animatableOffsetX.value <= deleteThreshold) {
                                    isSwiped = true
                                    animatableOffsetX.animateTo(-120f) // Chỉ dịch sang trái một chút
                                } else {
                                    isSwiped = false
                                    animatableOffsetX.animateTo(0f)
                                }
                            }
                        },
                        onDragCancel = {
                            scope.launch {
                                animatableOffsetX.animateTo(0f)
                                isSwiped = false
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (animatableOffsetX.value + dragAmount).coerceIn(-120f, 0f)
                                animatableOffsetX.snapTo(newOffset)
                                println("OffsetX: ${animatableOffsetX.value}")
                            }
                        }
                    )
                }
        )
    }
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(note.colorBackGround)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp)) // Áp dụng nền trực tiếp tại đây
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = note.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth()
            )
            Text(
                text = note.content ?: "Không có nội dung",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth()
            )
            Text(
                text = note.date.toString(),
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
