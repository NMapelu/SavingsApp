package com.example.semesterproject.bridget_apps.challenges_ui

import android.view.SurfaceControl
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semesterproject.ui.theme.Roboto
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape

// ------------------------- DATA MODEL -------------------------
//data class Transaction(
//    val date: String,
//    val amount: Double,
//    val total: Double
//)

// ------------------------- MAIN SCREEN -------------------------
@Composable
fun Progress(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8FF))
            .padding(top = 90.dp, start = 25.dp, end = 16.dp, bottom = 16.dp)

    ) {
        Text(
            text = "Saving Progress",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        ProgressCard()

        Spacer(modifier = Modifier.height(20.dp))

//        RecentTransactionsList(
//            transactions = listOf(
//                Transaction("Nov 1", 200.0, 200.0),
//                Transaction("Nov 2", 150.0, 350.0),
//                Transaction("Nov 3", 500.0, 850.0)
//            )
//        )
    }
}


@Composable
fun ProgressCard(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(size = 20.dp)
    ) {
        CardContent(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun CardContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFF9C27B0)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Current Balance",
                color = Color.White,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Kshs 5,000",
                color = Color.White,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Saved so far", color = Color.White.copy(alpha = 0.8f))
                Text(text = "Target Goal", color = Color.White.copy(alpha = 0.8f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Kshs 0", color = Color.White)
                Text(text = "Kshs 10,000", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            val progress = animateFloatAsState(targetValue = 0.5f, label = "progress")
            LinearProgressIndicator(
                progress = { progress.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

//// ------------------------- RECENT TRANSACTIONS -------------------------
//@Composable
//fun RecentTransactionsList(transactions: List<Transaction>) {
//    Text(
//        text = "Recent Transactions",
//        style = MaterialTheme.typography.titleMedium,
//        fontWeight = FontWeight.Bold,
//        modifier = Modifier.padding(bottom = 8.dp)
//    )
//
//    LazyColumn {
//        items(transactions) { transaction ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = transaction.date)
//                Text(
//                    text = "+Ksh ${transaction.amount}",
//                    color = Color(0xFF4CAF50),
//                    fontWeight = FontWeight.Bold
//                )
//                Text(text = "Total: ${transaction.total}")
//            }
//            Divider()
//        }
//    }
//}
//
//

