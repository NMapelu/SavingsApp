package com.example.semesterproject.bridget_apps.challenges_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semesterproject.ui.theme.Roboto

@Composable
fun SecondBar(
    title: String = "Pick your challenge",
    modifier: Modifier = Modifier
) {
    Column(  modifier = modifier.padding(horizontal = 22.dp, vertical = 3.dp)) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Roboto,
            color = MaterialTheme.colorScheme.onBackground

        )

    }
}
