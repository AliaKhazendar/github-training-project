package com.example.finalproject_aliakhz


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryRed = Color(0xFFB71C1C)
private val StarYellow = Color(0xFFFFC107)


data class ProductDetail(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Float,
    val ordersCount: Int,
    val description: List<String>,
    @DrawableRes val mainImageRes: Int,
)

val sampleProduct = ProductDetail(
    id = 1,
    name = "Device Laser Hair Removal",
    price = 10.0,
    rating = 4.8f,
    ordersCount = 50,
    mainImageRes = R.drawable.leaser,
    description = listOf(
        "Applicable : 100-240V working voltage, suitable for all the countries in the world.",
        "Painless: Adjustable optimal energy level according to the skin tolerance. Providing the gentle treatment painlessly and easily, without hurting the skin. The LED light clear shows the energy level.",
        "Fast and big treatment area : wavelength >510um, 3 cm² spot size not too big or neither too small for any area."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductDetail = sampleProduct,
    onBack: () -> Unit = {},
    onAddToCart: () -> Unit = {},
    onBuyNow: () -> Unit = {}
) {
    var quantity      by remember { mutableStateOf(1) }
    var isFavorite    by remember { mutableStateOf(false) }
    var selectedThumb by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            ProductBottomBar(
                onAddToCart = onAddToCart,
                onBuyNow = onBuyNow
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Product Image Section ───────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFFF8E8EE))
            ) {
                // Main image
                Image(
                    painter = painterResource(id = product.mainImageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite button (top right)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { isFavorite = !isFavorite },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) PrimaryRed else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                // ── Price + Quantity ────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Minus button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .clickable { if (quantity > 1) quantity-- },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(id = R.drawable.minus), null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = String.format("%02d", quantity),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        // Plus button
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PrimaryRed)
                                .clickable { quantity++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add, null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(Modifier.height(12.dp))

                // ── Rating Row ──────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star, null,
                        tint = StarYellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("${product.rating}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(" | ", fontSize = 14.sp, color = Color.LightGray)
                    Text("${product.ordersCount} Orders", fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.weight(1f))
                    Icon(
                        Icons.Default.ArrowDropDown, null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(Modifier.height(16.dp))

                // ── Description List ────────
                product.description.forEachIndexed { index, text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            "${index + 1}. ",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            color = Color.Black,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ProductBottomBar(
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cart icon
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF5F5F5))
                .clickable { onAddToCart() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "Add to cart",
                tint = PrimaryRed,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        // Buy now button
        Button(
            onClick = onBuyNow,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
        ) {
            Text(
                "Buy now",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
//@Composable
//fun ProductDetailScreen(product: Product, onClose: () -> Unit) {
//    // تغطية الشاشة كاملة
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.White)) {
//
//        Column {
//            // هنا تفاصيل المنتج: صورة، وصف، السعر، كمية، إلخ
//            Image(
//                painter = painterResource(product.imageRes),
//                contentDescription = product.title,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//            )
//            Text(product.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(16.dp))
//            Text(product.subtitle, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp))
//        }
//
//        // زر إغلاق أعلى الشاشة
//        IconButton(
//            onClick = onClose,
//            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
//        ) {
//            Icon(Icons.Default.Close, contentDescription = "Close")
//        }
//    }
//}

@Preview(showSystemUi = true)
@Composable
fun ProductDetailPreview() {
    MaterialTheme { ProductDetailScreen() }
}