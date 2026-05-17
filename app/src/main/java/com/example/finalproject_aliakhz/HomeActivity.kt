package com.example.finalproject_aliakhz

import android.R.attr.onClick
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen( onProductClick = {})

    }
}
}

// بيانات المنتج
data class Product(
    val title: String,
    val subtitle: String,
    val oldPrice: String,
    val newPrice: String,
    val sold: Int,
    val imageRes: Int
)

fun Product.toDetail(): ProductDetail {
    return ProductDetail(
        id = title.hashCode(),
        name = title,
        price = newPrice.replace("$", "").toDouble(),
        rating = 4.5f,
        ordersCount = sold,
        mainImageRes = imageRes,
        description = listOf("Nice product", "High quality", "Best choice")
    )
}
//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    favoriteProducts: List<FavoriteProduct> = emptyList(),
    onFavoriteToggle: (Product) -> Unit = {}
) {

    val products = listOf(
        Product("Brushes...", "Qmele", "$20", "$18", 70, R.drawable.brushes),
        Product("Device Laser Hair Rem...", "Qmele", "$15", "$10", 50, R.drawable.device),
        Product("Cherry Darling", "Qmele", "$80", "$50", 50, R.drawable.sponges),
        Product("Makeup Brush Set", "Qmele", "$70", "$40", 40, R.drawable.brush_set)
    )

    Scaffold(
//        bottomBar = {
//            BottomNavBar(selectedIndex = 1)
//        }
        topBar = {
            TopAppBar(
                title = { Text("  Good Morning, Alia") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.padding(end = 16.dp))
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", modifier = Modifier.padding(end = 16.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE), // لون الخلفية
                    titleContentColor = Color.White,    // لون النص
                    actionIconContentColor = Color.White // لون الأيقونات
                )
            )
        }
    )
    { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Header
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Good morning",
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                                Icon(Icons.Default.Search, contentDescription = "Search")

                                Box {
                                    Icon(Icons.Default.Notifications, contentDescription = "Noti")

                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.Red, RoundedCornerShape(4.dp))
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Banner
                        Image(
                            painter = painterResource(R.drawable.banner),
                            contentDescription = "Banner",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                }


                // Products
                items(products) { product ->
                    val isFav = favoriteProducts.any { it.id == product.title.hashCode() }
                    ProductCard(
                        product = product,
                        isFavorite = isFav,
                        onFavoriteClick = { onFavoriteToggle(product) },
                        onClick = { onProductClick(it) }
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onProductClick = {})
}
@Composable
fun ProductCard(
    product: Product,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onClick: (Product) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Box(modifier = Modifier.height(180.dp)) {
            Image(
                painter = painterResource(product.imageRes),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // أيقونة القلب تتغير حسب الحالة
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color(0xFFB72727) else Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable { onFavoriteClick() }
            )
        }
        Column(modifier = Modifier.padding(8.dp)) {

            Text(
                text = product.title,
                fontSize = 14.sp,
                maxLines = 1
            )

            Text(
                text = product.subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Prices + Sold (نفس السطر)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = product.oldPrice,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Text(
                        text = product.newPrice,
                        fontSize = 16.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                if(product.sold > 0) Text("${product.sold} sold", fontSize = 14.sp, color = Color.DarkGray)

            }
        }
    }
}
