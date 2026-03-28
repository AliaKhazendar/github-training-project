package com.example.finalproject_aliakhz


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryRed = Color(0xFFB71C1C)
private val FieldBg    = Color(0xFFF5F0EB)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditProfileBottomSheet(
    currentName: String = "Mona Fadl Al-Harthy",
    currentPhone: String = "00966 5211043",
    currentEmail: String = "Mona Fadl@gmail.com",
    onDismiss: () -> Unit = {},
    onDone: (name: String, phone: String, email: String) -> Unit = { _, _, _ -> }
) {
    var name  by remember { mutableStateOf(currentName) }
    var phone by remember { mutableStateOf(currentPhone) }
    var email by remember { mutableStateOf(currentEmail) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, end = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close",
                        tint = PrimaryRed, modifier = Modifier.size(22.dp))
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            EditField(label = "Your Name", value = name, onValueChange = { name = it })
            Spacer(Modifier.height(16.dp))
            EditField(label = "Your Phone number", value = phone, onValueChange = { phone = it })
            Spacer(Modifier.height(16.dp))
            EditField(label = "Your Email Addres", value = email, onValueChange = { email = it })
            Spacer(Modifier.height(28.dp))

            Button(
                onClick = { onDone(name, phone, email) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
            ) {
                Text("Done", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun EditField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Normal)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor   = FieldBg,
                unfocusedContainerColor = FieldBg
            )
        )
    }
}

