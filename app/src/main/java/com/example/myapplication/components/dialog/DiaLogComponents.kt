package com.example.myapplication.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import android.app.DatePickerDialog as AndroidDatePickerDialog
import android.app.TimePickerDialog as AndroidTimePickerDialog
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun MinimalDialog(
    onDismissRequest: () -> Unit,
    onRemindMe:  () -> Unit = {},
    onNavigateTo: () -> Unit = {},
    onDelete: () -> Unit = {},
    xOffset: Dp = 200.dp,
    yOffset: Dp = 10.dp
) {
    Popup(
        onDismissRequest = { onDismissRequest() },
        offset = IntOffset(xOffset.value.toInt(), yOffset.value.toInt()) // Định vị tuyệt đối
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRemindMe() }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Nhắc nhỏ",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateTo() }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Chuyển tới",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDelete() }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Xóa",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (Long, String) -> Unit // Callback trả về thời gian đã chọn
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var selectedDateTime by remember { mutableStateOf(calendar.timeInMillis) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Chọn ngày và giờ") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nút chọn ngày
                Button(
                    onClick = {
                        // Gọi AndroidDatePickerDialog trong sự kiện onClick
                        AndroidDatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDateTime = calendar.timeInMillis
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                ) {
                    Text("Chọn ngày")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nút chọn giờ
                Button(
                    onClick = {
                        // Gọi AndroidTimePickerDialog trong sự kiện onClick
                        AndroidTimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)
                                selectedDateTime = calendar.timeInMillis
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true // Định dạng 24 giờ
                        ).show()
                    }
                ) {
                    Text("Chọn giờ")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hiển thị thời gian đã chọn
                Text(
                    text = "Thời gian: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDateTime)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Kiểm tra thời gian không nằm trong quá khứ
                        val dateFormatted = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedDateTime)
                        onDateTimeSelected(selectedDateTime, dateFormatted)// Dùng thời gian hiện tại nếu quá khứ
                    onDismissRequest()
                }
            ) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Hủy")
            }
        }
    )
}