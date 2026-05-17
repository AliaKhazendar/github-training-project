package com.example.finalproject_aliakhz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// ── بيانات الفئات
data class Category(
    val id: Int,
    val name: String,
    val imageRes: Int
)

val sampleCategories = listOf(
    Category(1, "Skin creams", R.drawable.skincreams),
    Category(2, "Nail products", R.drawable.nailcreams),
    Category(3, "Perfume", R.drawable.perfume),
    Category(4, "Skin care Tools", R.drawable.skintools),
    Category(5, "Makeup", R.drawable.makeup),
    Category(6, "Hair care tools", R.drawable.hairtools),
    Category(7, "Dental care", R.drawable.prush),
    Category(8, "Shampoo", R.drawable.headcream)
)

//  Bottom Navigation items
private data class NavItem(
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int = 0
)

private val navItems = listOf(
    NavItem(Icons.Default.Home, "Home"),
    NavItem(Icons.Default.Menu, "Categories"),
    NavItem(Icons.Default.ShoppingCart, "Cart", badgeCount = 4),
    NavItem(Icons.Default.FavoriteBorder, "Favorites"),
    NavItem(Icons.Default.Person, "Profile")
)

private val ActiveColor = Color(0xFFB71C1C)
private val InactiveColor = Color(0xFFBDBDBD)

//  Bottom Navigation
//@Composable
//fun BottomNavBar(selectedIndex: Int = 1, onItemClick: (Int) -> Unit = {}) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.White)
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.SpaceAround,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        navItems.forEachIndexed { index, item ->
//            Box {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.label,
//                    tint = if (index == 0) ActiveColor else InactiveColor, // Home أحمر
//                    modifier = Modifier.clickable { onItemClick(index) }
//                )
//                if (item.badgeCount > 0) {
//                    // Badge للـ Cart
//                    Box(
//                        modifier = Modifier
//                            .size(16.dp)
//                            .background(Color.Red, shape = RoundedCornerShape(8.dp))
//                            .align(Alignment.TopEnd)
//                    ) {
//                        Text(
//                            text = item.badgeCount.toString(),
//                            color = Color.White,
//                            fontSize = 10.sp,
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                    }
//                }
//
//            }
//        }
//    }
//}

//  Categories Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    categories: List<Category> = sampleCategories,
    onCategoryClick: (Category) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Categories",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF6200EE) // تغيير لون العنوان

                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
       // bottomBar = { BottomNavBar(selectedIndex = 1) },
        containerColor = Color.White
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category) { onCategoryClick(category) }
            }
        }
    }
}

//  Category Card
@Composable
private fun CategoryCard(category: Category, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // تعديل: زيادة ارتفاع Card
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient أسفل الصورة للنص
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000)) // أغمق gradient
                    )
                )
        )

        Text(
            text = category.name,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp) // زيادة المسافة عن أسفل Card
        )
    }
}

@Preview
@Composable
fun CategoriesPreview() {
    MaterialTheme { CategoriesScreen() }
}