package com.example.finalproject_aliakhz


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryRed = Color(0xFFB71C1C)
private val BlueEdit   = Color(0xFF1565C0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    userName: String = "Mona Fadl Al-Harthy",
    userPhone: String = "009665211043",
    userEmail: String = "Mona Fadl@gmail.com",
    orderCount: Int = 4,
    @DrawableRes profileImageRes: Int = R.drawable.profile_photo,
    onEditProfile: () -> Unit = {},
    onMyOrder: () -> Unit = {},
    onPaymentMethod: () -> Unit = {},
    onShippingAddress: () -> Unit = {},
    onFqa: () -> Unit = {},
    onInviteFriends: () -> Unit = {},
    onSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showEdit by remember { mutableStateOf(false) }
    var displayName  by remember { mutableStateOf(userName) }
    var displayPhone by remember { mutableStateOf(userPhone) }
    var displayEmail by remember { mutableStateOf(userEmail) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Account", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F5F5))
            )
        },
        //bottomBar = { BottomNavBar(selectedIndex = 4) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(
                userName = displayName,
                userPhone = displayPhone,
                userEmail = displayEmail,
                profileImageRes = profileImageRes,
                onEditClick = { showEdit = true }
            )

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            // Section 1: Orders, Payment, Shipping
            AccountMenuItem(iconPainter = painterResource(id = R.drawable.order), label = "My order",
                trailing = { Text("$orderCount", color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 15.sp) },
                onClick = onMyOrder)
            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(start = 56.dp))

            AccountMenuItem(iconPainter =painterResource(id = R.drawable.pay), label = "payment method", onClick = onPaymentMethod)
            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(start = 56.dp))

            AccountMenuItem(iconVector = Icons.Default.LocationOn, label = "shipping address", onClick = onShippingAddress)

            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 6.dp)

            // Section 2: Support
            AccountMenuItem(iconPainter = painterResource(id = R.drawable.help), label = "FQA", onClick = onFqa)
            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(start = 56.dp))

            AccountMenuItem(iconPainter = painterResource(id = R.drawable.inv), label = "invite friends", onClick = onInviteFriends)
            HorizontalDivider(color = Color(0xFFEEEEEE), modifier = Modifier.padding(start = 56.dp))

            AccountMenuItem(iconVector = Icons.Default.Settings, label = "settings", onClick = onSettings)

            HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 6.dp)

            // Section 3: Logout
            AccountMenuItem(iconPainter = painterResource(id = R.drawable.logout), label = "Logout", onClick = onLogout)
        }
    }
    if (showEdit) {
        EditProfileBottomSheet(
            // مرّر القيم الحالية للـ Bottom Sheet
            currentName = displayName,
            currentPhone = displayPhone,
            currentEmail = displayEmail,
            onDismiss = { showEdit = false },
            onDone = { name, phone, email ->
                // احفظ التعديلات
                displayName  = name
                displayPhone = phone
                displayEmail = email
                showEdit = false
            }
        )
    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    userPhone: String,
    userEmail: String,
    @DrawableRes profileImageRes: Int,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile image + camera icon
        Box(modifier = Modifier.size(64.dp)) {
            Image(
                painter = painterResource(id = profileImageRes),
                contentDescription = "Profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(PrimaryRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(id = R.drawable.camera), contentDescription = "Change photo",
                    tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(userName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            Spacer(Modifier.height(2.dp))
            Text(userPhone, fontSize = 13.sp, color = Color.Gray)
            Spacer(Modifier.height(2.dp))
            Text(userEmail, fontSize = 13.sp, color = Color.Gray)
        }

        // Edit
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit profile",
            tint = BlueEdit,
            modifier = Modifier.size(20.dp).clickable { onEditClick() }
        )
    }
}

@Composable
private fun AccountMenuItem(
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    label: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // اختيار نوع الأيقونة
        when {
            iconVector != null -> {
                Icon(
                    imageVector = iconVector,
                    contentDescription = label,
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }

            iconPainter != null -> {
                Icon(
                    painter = iconPainter,
                    contentDescription = label,
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.width(18.dp))

        Text(
            label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        trailing?.invoke()
    }
}

@Preview
@Composable
fun AccountPreview() {
    AccountScreen()
}