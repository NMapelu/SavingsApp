package com.example.semesterproject.bridget_apps.challenges_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.semesterproject.ui.theme.Roboto
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.navigation.NavController

data class ChallengeInput(
    val dailySave: Double,
    val startDate: String,
    val targetGoal: Double,
    val challengeType: String
)
@Composable
fun SavingForm(
    navController: NavController,
    challengeType: String,
    modifier: Modifier = Modifier
) {

    var dailysave by remember{
        mutableStateOf("")
    }

    var startdate by remember{
        mutableStateOf("")
    }

    var targetgoal by remember{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Welcome ",
            fontSize = 22.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Starting the $challengeType",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Fill in the Savings Form",
            fontSize = 22.sp,
            fontFamily = Roboto,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dailysave,
            onValueChange= { dailysave = it},
            label = {
                Text(
                    text = "Daily Save (Kshs)",
                    fontSize = 16.sp
                )}
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startdate,
            onValueChange= { startdate = it},
            label = {
                Text(
                    text = "Start Date",
                    fontSize = 16.sp
                )}
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = targetgoal,
            onValueChange= { targetgoal = it},
            label = {
                Text(
                    text = "Targeted Goal",
                    fontSize = 16.sp
                )}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            val inputData = ChallengeInput(
                dailySave = dailysave.toDoubleOrNull() ?: 0.0,
                startDate = startdate,
                targetGoal = targetgoal.toDoubleOrNull() ?: 0.0,
                challengeType = challengeType
            )

            navController.navigate("progress")


        })
        {
            Text(text = "Apply")
        }
    }
}