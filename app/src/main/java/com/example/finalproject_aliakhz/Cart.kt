package com.example.finalproject_aliakhz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val PrimaryRed = Color(0xFFB71C1C)
private val CardBg     = Color(0xFFFFF0F3)

// بيانات السلة
data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int
)

val sampleCartItems = listOf(
    CartItem(1, "Device Laser Hair Rem...", 10.0, 1, R.drawable.device),
    CartItem(2, "Device Laser Hair Rem...", 10.0, 1, R.drawable.device),
    CartItem(3, "Device Laser Hair Rem...", 10.0, 1, R.drawable.device),
    CartItem(4, "Device Laser Hair Rem...", 10.0, 1, R.drawable.device),
)

// ── Screen ─────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: androidx.compose.runtime.snapshots.SnapshotStateList<CartItem> =
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateListOf() },
    onBuyNowClick: () -> Unit = {},
    onItemBuyNow: (CartItem) -> Unit = {}
) {
    val subTotal = cartItems.sumOf { it.price * it.quantity }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Cart(${cartItems.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems, key = { it.id }) { item ->
                    CartItemCard(
                        item = item,
                        onRemove = { cartItems.remove(item) },
                        onIncrease = {
                            val index = cartItems.indexOf(item)
                            if (index >= 0) cartItems[index] = item.copy(quantity = item.quantity + 1)
                        },
                        onDecrease = {
                            val index = cartItems.indexOf(item)
                            if (index >= 0 && item.quantity > 1)
                                cartItems[index] = item.copy(quantity = item.quantity - 1)
                        },
                        onBuyNow = {
                            onItemBuyNow(item)
                            cartItems.remove(item)
                            scope.launch {
                                snackbarHostState.showSnackbar("Purchase Complete!")
                            }
                        }
                    )
                }
            }
            CartFooter(
                subTotal = subTotal,
                onBuyNow = {
                    onBuyNowClick()
                    scope.launch {
                        snackbarHostState.showSnackbar("Purchase Complete!")
                    }
                }
            )
        }
    }
}


@Composable
private fun CartItemCard(
    item: CartItem,
    onRemove: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onBuyNow: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBg)
            .padding(8.dp)
    ) {
        Column {
            // Image + remove button
            Box(Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Remove",
                        tint = PrimaryRed, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(Modifier.height(6.dp))
            Text(item.name, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$${String.format("%.2f", item.price)}",
                    color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                QuantitySelector(item.quantity, onIncrease, onDecrease)
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Buy now",
                    color = PrimaryRed,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onBuyNow() }  // كان فارغاً
                )
                Icon(Icons.Default.Info, contentDescription = "Info",
                    tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun QuantitySelector(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, PrimaryRed, CircleShape)
                .clickable { onDecrease() },
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(id = R.drawable.minus), contentDescription = "Decrease",
                tint = PrimaryRed, modifier = Modifier.size(10.dp))
        }
        Text(String.format("%02d", quantity), fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 4.dp))
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(PrimaryRed)
                .clickable { onIncrease() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Increase",
                tint = Color.White, modifier = Modifier.size(10.dp))
        }
    }
}


@Composable
private fun CartFooter(subTotal: Double, onBuyNow: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("SubTotal", fontSize = 14.sp)
            Text("$${String.format("%.2f", subTotal)}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onBuyNow,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
        ) {
            Text("Buy now", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun CartPreview() {
    MaterialTheme { CartScreen() }
}