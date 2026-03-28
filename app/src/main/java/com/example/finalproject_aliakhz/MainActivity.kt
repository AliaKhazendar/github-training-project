package com.example.finalproject_aliakhz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppRoot() }
    }
}

// ===== Root Composable =====
//@Composable
//fun AppRoot() {
//    // هذا المتغير يحدد أي شاشة نشوفها
//    var currentScreen by remember { mutableStateOf("splash") }
//    var selectedProduct by remember { mutableStateOf<ProductDetail?>(null) }
//    //var selectedProduct7 by remember { mutableStateOf<Product?>(null) }
//
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        // الشاشة الرئيسية حسب currentScreen
//        Box(modifier = Modifier.weight(1f)) {
//            when (currentScreen) {
//                "splash" -> SplashScreen { currentScreen = "login" }
//                "login" -> LoginScreen { currentScreen = "home" }
//                "home" -> HomeScreen { selectedProduct = it.toDetail() }
//                "categories" -> CategoriesScreen()
//                "cart" -> CartScreen()
//                "favorites" -> FavoriteScreen()
//                "account" -> AccountScreen()
//            }
//            // إذا تم اختيار منتج، نعرض شاشة التفاصيل فوق كل شيء
//            selectedProduct?.let { product ->
//                ProductDetailScreen(
//                    product = product,
//                    onBack = { selectedProduct = null }
//                )
//            }
//        }
////        selectedProduct7?.let { product ->
////            ProductDetailScreen(product = product, onClose = { selectedProduct7 = null })
////        }
//
//        // البار السفلي يظهر فقط للشاشات الرئيسية وعندما لا تظهر تفاصيل المنتج
//        if(currentScreen !in listOf("splash", "login") && selectedProduct == null) {
//            BottomNavBar(selectedScreen = currentScreen) { selected ->
//                currentScreen = selected
//            }
//        }
//    }
//}
@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf("splash") }
    var selectedProduct by remember { mutableStateOf<ProductDetail?>(null) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val favoriteProducts = remember { mutableStateListOf<FavoriteProduct>() }

    // السلة والطلبات المشتركة
    val cartItems = remember { mutableStateListOf<CartItem>() }
    val purchasedOrders = remember { mutableStateListOf<OrderItem>() }
    var showMyOrders by remember { mutableStateOf(false) }

    // دالة مشتركة لإضافة للسلة
    fun addToCart(product: ProductDetail) {
        val existing = cartItems.find { it.id == product.id }
        if (existing != null) {
            val index = cartItems.indexOf(existing)
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(
                CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1,
                    imageRes = product.mainImageRes
                )
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (currentScreen) {
                "splash" -> SplashScreen { currentScreen = "login" }
                "login" -> LoginScreen { currentScreen = "home" }
                "home" -> HomeScreen(
                    onProductClick = { selectedProduct = it.toDetail() },
                    favoriteProducts = favoriteProducts,
                    onFavoriteToggle = { product ->
                        val existing = favoriteProducts.find { it.id == product.title.hashCode() }
                        if (existing != null) favoriteProducts.remove(existing)
                        else favoriteProducts.add(
                            FavoriteProduct(
                                id = product.title.hashCode(),
                                name = product.title,
                                brand = product.subtitle,
                                price = product.newPrice.replace("$", "").toDouble(),
                                soldCount = product.sold,
                                imageRes = product.imageRes
                            )
                        )
                    }
                )
                "categories" -> CategoriesScreen(
                    onCategoryClick = { selectedCategory = it }
                )
                "cart" -> CartScreen(
                    cartItems = cartItems,
                    onBuyNowClick = {
                        cartItems.forEach { item ->
                            purchasedOrders.add(
                                OrderItem(id = item.id, name = item.name, price = item.price,
                                    quantity = item.quantity, imageRes = item.imageRes)
                            )
                        }
                        cartItems.clear()
                    },
                    onItemBuyNow = { item ->
                        purchasedOrders.add(
                            OrderItem(id = item.id, name = item.name, price = item.price,
                                quantity = item.quantity, imageRes = item.imageRes)
                        )
                    }
                )
                "favorites" -> FavoriteScreen(initialProducts = favoriteProducts)
                "account" -> AccountScreen(
                    orderCount = purchasedOrders.size,
                    onMyOrder = { showMyOrders = true }
                )
            }

            // شاشة تفاصيل المنتج
            selectedProduct?.let { product ->
                ProductDetailScreen(
                    product = product,
                    onBack = { selectedProduct = null },
                    onAddToCart = { addToCart(product) },
                    onBuyNow = {
                        addToCart(product)
                        selectedProduct = null
                        currentScreen = "cart"
                    }
                )
            }

            // شاشة منتجات الفئة
            selectedCategory?.let { category ->
                CategoryProductsScreen(
                    category = category,
                    onBack = { selectedCategory = null },
                    favoriteProducts = favoriteProducts,
                    onFavoriteToggle = { product ->
                        val existing = favoriteProducts.find { it.id == product.title.hashCode() }
                        if (existing != null) favoriteProducts.remove(existing)
                        else favoriteProducts.add(
                            FavoriteProduct(
                                id = product.title.hashCode(),
                                name = product.title,
                                brand = product.subtitle,
                                price = product.newPrice.replace("$", "").toDouble(),
                                soldCount = product.sold,
                                imageRes = product.imageRes
                            )
                        )
                    },
                    onProductClick = { selectedProduct = it.toDetail() }
                )
            }

            // شاشة My Orders فوق كل شيء
            if (showMyOrders) {
                MyOrdersScreen(
                    orders = purchasedOrders,
                    onBack = { showMyOrders = false }
                )
            }
        }

        if (currentScreen !in listOf("splash", "login")
            && selectedProduct == null
            && selectedCategory == null
            && !showMyOrders
        ) {
            BottomNavBar(selectedScreen = currentScreen,cartCount = cartItems.size) { currentScreen = it }
        }
    }
}

// ===== BottomNavBar =====
@Composable
fun BottomNavBar(selectedScreen: String,cartCount: Int = 0,onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = { onSelect("home") }) { Icon(Icons.Default.Home, contentDescription = "Home", tint = if(selectedScreen=="home") Color.Red else Color.Gray) }
        IconButton(onClick = { onSelect("categories") }) { Icon(Icons.Default.Menu, contentDescription = "Categories", tint = if(selectedScreen=="categories") Color.Red else Color.Gray) }
        IconButton(onClick = { onSelect("cart") }) {
            BadgedBox(
                badge = {
                    if (cartCount > 0) {  // يظهر فقط لو في منتجات
                        Badge { Text("$cartCount") }
                    }
                }
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = if (selectedScreen == "cart") Color.Red else Color.Gray
                )
            }
        }
        IconButton(onClick = { onSelect("favorites") }) { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites", tint = if(selectedScreen=="favorites") Color.Red else Color.Gray) }
        IconButton(onClick = { onSelect("account") }) { Icon(Icons.Default.Person, contentDescription = "Account", tint = if(selectedScreen=="account") Color.Red else Color.Gray) }
    }
}