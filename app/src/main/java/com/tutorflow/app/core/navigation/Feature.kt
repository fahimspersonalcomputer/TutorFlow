package com.tutorflow.app.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Grading
import androidx.compose.material.icons.rounded.HowToReg
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.ui.graphics.vector.ImageVector
import com.tutorflow.app.R

/**
 * Modules that are reachable from the dashboard / More hub but are not built yet.
 * Each opens an honest "not built yet" screen naming the phase that delivers it.
 */
enum class Feature(
    val key: String,
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val phase: Int,
) {
    ADD_STUDENT("add_student", R.string.qa_add_student, Icons.Rounded.PersonAdd, R.string.phase_3),
    TAKE_ATTENDANCE("take_attendance", R.string.qa_take_attendance, Icons.Rounded.HowToReg, R.string.phase_3),
    COLLECT_FEE("collect_fee", R.string.qa_collect_fee, Icons.Rounded.Payments, R.string.phase_3),
    ADD_EXAM("add_exam", R.string.qa_add_exam, Icons.Rounded.PostAdd, R.string.phase_3),
    ENTER_MARKS("enter_marks", R.string.qa_enter_marks, Icons.Rounded.Grading, R.string.phase_3),
    ID_CARD("id_card", R.string.qa_id_card, Icons.Rounded.Badge, R.string.phase_4),
    ADMIT_CARD("admit_card", R.string.qa_admit_card, Icons.Rounded.ConfirmationNumber, R.string.phase_4),
    SEND_MESSAGE("send_message", R.string.qa_send_message, Icons.Rounded.Sms, R.string.phase_5),
    BACKUP("backup", R.string.qa_backup, Icons.Rounded.CloudUpload, R.string.phase_6),

    BATCHES("batches", R.string.more_batches, Icons.Rounded.Layers, R.string.phase_3),
    CLASSES("classes", R.string.more_classes, Icons.Rounded.School, R.string.phase_3),
    EXAMS("exams", R.string.more_exams, Icons.Rounded.Quiz, R.string.phase_3),
    RESULTS("results", R.string.more_results, Icons.Rounded.EmojiEvents, R.string.phase_3),
    DOCUMENTS("documents", R.string.more_documents, Icons.Rounded.Description, R.string.phase_4),
    MESSAGES("messages", R.string.more_messages, Icons.Rounded.Chat, R.string.phase_5),
    CALENDAR("calendar", R.string.more_calendar, Icons.Rounded.CalendarMonth, R.string.phase_3),
    REPORTS("reports", R.string.more_reports, Icons.Rounded.BarChart, R.string.phase_3),
    EXPENSES("expenses", R.string.more_expenses, Icons.Rounded.Receipt, R.string.phase_3),
    BACKUP_RESTORE("backup_restore", R.string.more_backup, Icons.Rounded.Backup, R.string.phase_6),
    ;

    companion object {
        fun fromKey(key: String?): Feature? = entries.firstOrNull { it.key == key }

        val quickActions: List<Feature> = listOf(
            ADD_STUDENT, TAKE_ATTENDANCE, COLLECT_FEE,
            ADD_EXAM, ENTER_MARKS, ID_CARD,
            ADMIT_CARD, SEND_MESSAGE, BACKUP,
        )

        val moreHub: List<Feature> = listOf(
            BATCHES, CLASSES, EXAMS, RESULTS, DOCUMENTS, MESSAGES,
            CALENDAR, REPORTS, EXPENSES, BACKUP_RESTORE,
        )
    }
}
