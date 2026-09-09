 package com.example.eventhub.ui.common

 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.fillMaxHeight
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.ArrowBack
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.Icon
 import androidx.compose.material3.IconButton
 import androidx.compose.material3.Surface
 import androidx.compose.material3.Text
 import androidx.compose.material3.TopAppBar
 import androidx.compose.material3.TopAppBarDefaults
 import androidx.compose.runtime.Composable
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.text.font.FontWeight
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
 import com.example.eventhub.ui.theme.Purple

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun EventHubTopBar(
     title: String,
     showBackButton: Boolean = false,
     onBackClick: () -> Unit = {},
     searchBar: @Composable (() -> Unit)? = null
 ) {
     Surface(
         color = Purple,
         modifier = Modifier.clip(RoundedCornerShape(
             bottomStart = 24.dp,bottomEnd = 24.dp
         )).background(Purple)
     ) {
         Column {
             TopAppBar(
                 title = {
                     Text(
                         text = title,
                         color = Color.White,
                         fontSize = 26.sp,
                         fontWeight = FontWeight.SemiBold
                     )
                 },
                 navigationIcon = {
                     if (showBackButton) {
                         IconButton(
                             onClick = onBackClick
                         ) {
                             Icon(
                                 imageVector = Icons.Default.ArrowBack,
                                 contentDescription = "Back",
                                 tint = Color.White
                             )
                         }
                     }
                 },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = Color.Transparent,
                     titleContentColor = Color.White,
                     navigationIconContentColor = Color.White
                 )
             )
             if (searchBar != null) {
                 Box(
                     modifier = Modifier.padding(
                         start = 16.dp,
                         end = 16.dp,
                         bottom = 16.dp
                     )
                 ) {
                     searchBar()
                 }
             }
         }
     }
 }