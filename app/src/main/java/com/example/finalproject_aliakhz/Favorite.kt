package com.example.finalproject_aliakhz


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
private val CardBg     = Color(0xFFFFF0F3)

data class FavoriteProduct(
    val id: Int,
    val name: String,
    val brand: String,
    val price: Double,
    val soldCount: Int,
    @DrawableRes val imageRes: Int,
    val isFavorite: Boolean = true
)

val sampleFavorites = listOf(
    FavoriteProduct(1, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
    FavoriteProduct(2, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
    FavoriteProduct(3, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
    FavoriteProduct(4, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
    FavoriteProduct(5, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
    FavoriteProduct(6, "Device Laser Hair Rem...", "Qmele", 10.0, 50, R.drawable.device),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    initialProducts: androidx.compose.runtime.snapshots.SnapshotStateList<FavoriteProduct> =
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateListOf() },
    onProductClick: (FavoriteProduct) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
   // var products by remember { mutableStateOf(initialProducts) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Favorite", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
      //  bottomBar = { BottomNavBar(selectedIndex = 3) },
        containerColor = Color.White
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(initialProducts, key = { it.id }) { product ->
                FavoriteProductCard(
                    product = product,
                    onFavoriteToggle = {
                        initialProducts.remove(product)  // يحذف من الـ list مباشرة
                    },
                    onClick = { onProductClick(product) }
                )
            }
        }
    }
}

@Composable
private fun FavoriteProductCard(
    product: FavoriteProduct,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(bottom = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            // Heart button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onFavoriteToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (product.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (product.isFavorite) PrimaryRed else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(product.name, fontSize = 12.sp, maxLines = 1, color = Color.Black)
            Spacer(Modifier.height(2.dp))
            Text(product.brand, fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "$${String.format("%.2f", product.price)}",
                    color = PrimaryRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text("${product.soldCount} sold", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FavoritePreview() {
    MaterialTheme { FavoriteScreen() }
}