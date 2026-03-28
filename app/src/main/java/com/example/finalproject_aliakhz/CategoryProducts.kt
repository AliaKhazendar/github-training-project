package com.example.finalproject_aliakhz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// بيانات المنتجات حسب الفئة
val categoryProducts = mapOf(
    1 to listOf( // Skin creams
        Product("Face Cream", "Qmele", "$25", "$20", 80, R.drawable.skin1),
        Product("Night Cream", "Qmele", "$30", "$22", 60, R.drawable.skin2)
    ),
    2 to listOf( // Nail products
        Product("Nail Polish Set", "Qmele", "$15", "$10", 45, R.drawable.nail1),
        Product("Nail Care Kit", "Qmele", "$20", "$14", 30, R.drawable.nail2)
    ),
    3 to listOf( // Perfume
        Product("Rose Perfume", "Qmele", "$60", "$45", 90, R.drawable.perfume1),
        Product("Oud Spray", "Qmele", "$80", "$55", 70, R.drawable.perfume2)
    ),
    // باقي الفئات
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    category: Category,
    onBack: () -> Unit,
    favoriteProducts: List<FavoriteProduct> = emptyList(),
    onFavoriteToggle: (Product) -> Unit = {},
    onProductClick: (Product) -> Unit = {}
) {
    val products = categoryProducts[category.id] ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        category.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        if (products.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No products in this category", color = Color.Gray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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