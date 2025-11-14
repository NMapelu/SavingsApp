package com.example.semesterproject

import com.example.semesterproject.bridget_apps.challenges_ui.TopBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.semesterproject.ui.theme.SemesterProjectTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.height
import com.example.semesterproject.bridget_apps.challenges_ui.CardSection
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.semesterproject.bridget_apps.challenges_ui.CardSection2
import com.example.semesterproject.bridget_apps.challenges_ui.CardSection3
import com.example.semesterproject.bridget_apps.challenges_ui.Progress
import com.example.semesterproject.bridget_apps.challenges_ui.SavingForm
import com.example.semesterproject.bridget_apps.challenges_ui.SecondBar



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SemesterProjectTheme {

                val navController = rememberNavController()


                AppNavigation(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ){
            SecondBar(
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            CardSection(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )

            CardSection2(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )

            CardSection3(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )

        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,

        startDestination = "mainScreen",
        modifier = modifier
    ){
        composable("mainScreen") {
            MainScreen(navController = navController, modifier = modifier)
        }

        composable(
            route = "savingForm/{challengeType}",
            arguments = listOf(navArgument("challengeType") { type = NavType.StringType })
        ) { backStackEntry ->

            val challengeType = backStackEntry.arguments?.getString("challengeType")

            SavingForm(
                navController = navController,
                challengeType = challengeType!!
            )
        }

        composable("progress") {
            Progress()
        }
    }
}

