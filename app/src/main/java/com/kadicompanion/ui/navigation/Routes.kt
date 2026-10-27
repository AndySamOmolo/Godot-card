package com.kadicompanion.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector
import com.kadicompanion.R

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PHASE_LIST = "tutorials"
    const val BLUEPRINT = "blueprint"
    const val DOCS = "docs"
    const val BOOKMARKS = "bookmarks"
    const val INSIGHTS = "insights"
    const val SETTINGS = "settings"
    const val GLOSSARY = "glossary"

    const val LESSON_DETAIL = "lesson/{lessonId}"
    const val QUIZ = "quiz/{lessonId}"
    const val PRACTICE = "practice/{lessonId}"
    const val DEBUG_CHALLENGE = "debug/{phaseId}"
    const val DOC_DETAIL = "doc/{className}"
    const val GLOSSARY_DETAIL = "glossary_detail/{term}"
    const val PHASE_DETAIL = "phase/{phaseId}"

    fun lesson(lessonId: String) = "lesson/$lessonId"
    fun quiz(lessonId: String) = "quiz/$lessonId"
    fun practice(lessonId: String) = "practice/$lessonId"
    fun debugChallenge(phaseId: String) = "debug/$phaseId"
    fun docDetail(className: String) = "doc/$className"
    fun glossaryDetail(term: String) = "glossary_detail/$term"
    fun phaseDetail(phaseId: String) = "phase/$phaseId"

    const val ARG_LESSON_ID = "lessonId"
    const val ARG_PHASE_ID = "phaseId"
    const val ARG_CLASS_NAME = "className"
    const val ARG_TERM = "term"
}

enum class TopLevelDestination(val route: String, val labelRes: Int, val icon: ImageVector, val selectedIcon: ImageVector) {
    HOME(Routes.HOME, R.string.nav_home, Icons.Outlined.Home, Icons.Filled.Home),
    TUTORIALS(Routes.PHASE_LIST, R.string.nav_tutorials, Icons.AutoMirrored.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook),
    DOCS(Routes.DOCS, R.string.nav_docs, Icons.Outlined.School, Icons.Filled.School),
    BOOKMARKS(Routes.BOOKMARKS, R.string.nav_bookmarks, Icons.Outlined.BookmarkBorder, Icons.Filled.Bookmark)
}
