package com.kadicompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kadicompanion.ui.bookmarks.BookmarksScreen
import com.kadicompanion.ui.common.KadiBackground
import com.kadicompanion.ui.docs.docdetail.DocDetailScreen
import com.kadicompanion.ui.docs.doclist.DocsListScreen
import com.kadicompanion.ui.glossary.GlossaryDetailScreen
import com.kadicompanion.ui.glossary.GlossaryListScreen
import com.kadicompanion.ui.home.HomeScreen
import com.kadicompanion.ui.insights.InsightsScreen
import com.kadicompanion.ui.navigation.KadiBottomBar
import com.kadicompanion.ui.navigation.Routes
import com.kadicompanion.ui.navigation.TopLevelDestination
import com.kadicompanion.ui.onboarding.OnboardingScreen
import com.kadicompanion.ui.settings.SettingsScreen
import com.kadicompanion.ui.theme.KadiTheme
import com.kadicompanion.ui.tutorial.blueprint.ProjectBlueprintScreen
import com.kadicompanion.ui.tutorial.debugchallenge.DebugChallengeScreen
import com.kadicompanion.ui.tutorial.lessondetail.LessonDetailScreen
import com.kadicompanion.ui.tutorial.phaselist.PhaseDetailScreen
import com.kadicompanion.ui.tutorial.phaselist.PhaseListScreen
import com.kadicompanion.ui.tutorial.practice.PracticeScreen
import com.kadicompanion.ui.tutorial.quiz.QuizScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
        )
        setContent {
            val rootVm: RootViewModel = hiltViewModel()
            val state by rootVm.uiState.collectAsStateWithLifecycle()
            KadiTheme(darkTheme = state.darkTheme, dynamicColor = state.dynamicColor, reduceMotion = state.reduceMotion, dyslexiaFont = state.dyslexiaFont) {
                KadiBackground {
                    AppContent(state = state, onOnboarded = rootVm::onOnboarded)
                }
            }
        }
    }
}

@Composable
private fun AppContent(state: RootUiState, onOnboarded: () -> Unit) {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in TopLevelDestination.entries.map { it.route }.toSet()
    Scaffold(
        bottomBar = { if (showBottomBar) KadiBottomBar(navController = navController) },
        containerColor = Color.Transparent
    ) { padding ->
        NavHost(navController = navController, startDestination = if (state.isOnboarded) Routes.HOME else Routes.ONBOARDING, modifier = Modifier.fillMaxSize().padding(padding)) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(onFinished = { onOnboarded(); navController.navigate(Routes.HOME) { popUpTo(Routes.ONBOARDING) { inclusive = true } } })
            }
            composable(Routes.HOME) {
                HomeScreen(
                    onContinueLesson = { lessonId -> navController.navigate(Routes.lesson(lessonId)) },
                    onReview = { lessonId -> navController.navigate(Routes.lesson(lessonId)) },
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                    onOpenInsights = { navController.navigate(Routes.INSIGHTS) },
                    onOpenPhases = { navController.navigate(Routes.PHASE_LIST) },
                    onOpenBlueprint = { navController.navigate(Routes.BLUEPRINT) },
                    onOpenPhase = { phaseId -> navController.navigate(Routes.phaseDetail(phaseId)) }
                )
            }
            composable(Routes.PHASE_LIST) {
                PhaseListScreen(
                    onPhaseClick = { phaseId -> navController.navigate(Routes.phaseDetail(phaseId)) },
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                    onOpenBlueprint = { navController.navigate(Routes.BLUEPRINT) }
                )
            }
            composable(Routes.BLUEPRINT) {
                ProjectBlueprintScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToPhase = { phaseId -> navController.navigate(Routes.phaseDetail(phaseId)) }
                )
            }
            composable(Routes.PHASE_DETAIL, arguments = listOf(navArgument(Routes.ARG_PHASE_ID) { type = NavType.StringType })) { entry ->
                val phaseId = entry.arguments?.getString(Routes.ARG_PHASE_ID).orEmpty()
                PhaseDetailScreen(phaseId = phaseId, onLessonClick = { lessonId -> navController.navigate(Routes.lesson(lessonId)) }, onDebugChallenge = { navController.navigate(Routes.debugChallenge(phaseId)) }, onBack = { navController.popBackStack() })
            }
            composable(Routes.LESSON_DETAIL, arguments = listOf(navArgument(Routes.ARG_LESSON_ID) { type = NavType.StringType })) { entry ->
                val lessonId = entry.arguments?.getString(Routes.ARG_LESSON_ID).orEmpty()
                LessonDetailScreen(onBack = { navController.popBackStack() }, onQuiz = { navController.navigate(Routes.quiz(lessonId)) }, onPractice = { navController.navigate(Routes.practice(lessonId)) }, onNextLesson = { nextId -> navController.navigate(Routes.lesson(nextId)) { popUpTo(Routes.LESSON_DETAIL) { inclusive = true } } }, onDebugChallenge = { phaseId -> navController.navigate(Routes.debugChallenge(phaseId)) }, onTermClick = { term -> navController.navigate(Routes.glossaryDetail(term)) })
            }
            composable(Routes.QUIZ, arguments = listOf(navArgument(Routes.ARG_LESSON_ID) { type = NavType.StringType })) {
                QuizScreen(onBack = { navController.popBackStack() }, onComplete = { navController.popBackStack() })
            }
            composable(Routes.PRACTICE, arguments = listOf(navArgument(Routes.ARG_LESSON_ID) { type = NavType.StringType })) {
                PracticeScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.DEBUG_CHALLENGE, arguments = listOf(navArgument(Routes.ARG_PHASE_ID) { type = NavType.StringType })) {
                DebugChallengeScreen(onBack = { navController.popBackStack() }, onComplete = { navController.popBackStack() })
            }
            composable(Routes.DOCS) {
                DocsListScreen(
                    onDocClick = { className -> navController.navigate(Routes.docDetail(className)) },
                    onOpenGlossary = { term -> navController.navigate(Routes.glossaryDetail(term)) }
                )
            }
            composable(Routes.DOC_DETAIL, arguments = listOf(navArgument(Routes.ARG_CLASS_NAME) { type = NavType.StringType })) { entry ->
                val className = entry.arguments?.getString(Routes.ARG_CLASS_NAME).orEmpty()
                DocDetailScreen(className = className, onBack = { navController.popBackStack() }, onGlossaryTermClick = { term -> navController.navigate(Routes.glossaryDetail(term)) })
            }
            composable(Routes.GLOSSARY) {
                GlossaryListScreen(onTermClick = { term -> navController.navigate(Routes.glossaryDetail(term)) })
            }
            composable(Routes.GLOSSARY_DETAIL, arguments = listOf(navArgument(Routes.ARG_TERM) { type = NavType.StringType })) { entry ->
                val term = entry.arguments?.getString(Routes.ARG_TERM).orEmpty()
                GlossaryDetailScreen(term = term, onBack = { navController.popBackStack() }, onLessonClick = { lessonId -> navController.navigate(Routes.lesson(lessonId)) }, onDocClick = { className -> navController.navigate(Routes.docDetail(className)) })
            }
            composable(Routes.BOOKMARKS) {
                BookmarksScreen(onLessonClick = { lessonId -> navController.navigate(Routes.lesson(lessonId)) }, onDocClick = { className -> navController.navigate(Routes.docDetail(className)) }, onGlossaryClick = { term -> navController.navigate(Routes.glossaryDetail(term)) })
            }
            composable(Routes.INSIGHTS) { InsightsScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.SETTINGS) { SettingsScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
