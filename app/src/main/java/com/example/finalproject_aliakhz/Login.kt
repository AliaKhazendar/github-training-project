package com.example.finalproject_aliakhz

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(onLoginClick = {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            })

        }
    }
}
@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)) // تعديل: لون خلفية أخف
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.loginphoto),
            contentDescription = "Header Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp), // تعديل: ارتفاع أكبر للصورة
                    contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Phone number",
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.all { c -> c.isDigit() }) phoneNumber = it
                errorMessage = ""
            },
            placeholder = { Text("Enter Your Phone Number") },
            leadingIcon = { Text("+972") },
            shape = RoundedCornerShape(12.dp), // تعديل: زوايا أكثر نعومة,
            isError = errorMessage.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
        )
        // رسالة الخطأ
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in with Email",
            color = Color(0xFFB71C1C),
            modifier = Modifier.clickable { /* TODO */ },
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when {
                    phoneNumber.isEmpty() -> errorMessage = "Please enter your phone number"
                    phoneNumber.length != 8 -> errorMessage = "Phone number must be 8 digits"
                    else -> onLoginClick()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("login", color = Color.White, fontSize = 16.sp)
        }


        Spacer(modifier = Modifier.height(12.dp))

        val annotatedText = buildAnnotatedString {
            append("By clicking login you agree to our ")

            pushStringAnnotation(tag = "TERMS", annotation = "terms")
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("terms & conditions")
            }
            append(" and ")
            pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
            withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                append("privacy policy")
            }
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                    .firstOrNull()?.let { /* navigate to terms */ }
                annotatedText.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                    .firstOrNull()?.let { /* navigate to privacy */ }
            },
            style = LocalTextStyle.current.copy(fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f), color = Color.White)
            Text("  OR  ", fontSize = 12.sp, color = Color.Black)
            Divider(modifier = Modifier.weight(1f), color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CircleImageButton(imageResId = R.drawable.google) { /* TODO: Google login */ }
            CircleImageButton(imageResId = R.drawable.facebook) { /* TODO: Facebook login */ }
            CircleImageButton(imageResId = R.drawable.twitter) { /* TODO: Twitter login */ }
        }
    }
}

@Composable
fun CircleImageButton(imageResId: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp) // حجم الدائرة
            .background(Color.White, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp) // حجم الصورة داخل الدائرة
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginClick = {})
}