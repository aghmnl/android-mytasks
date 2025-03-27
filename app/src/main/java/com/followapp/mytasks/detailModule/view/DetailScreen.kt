package com.followapp.mytasks.detailModule.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.followapp.mytasks.common.entities.Task
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailScreen(
    task: Task?,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var isDone by remember { mutableStateOf(task?.isDone == true) }
    var dueDate by remember { mutableStateOf(task?.dueDate) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isDone,
                onCheckedChange = { isDone = it },
                modifier = Modifier.padding(8.dp)
            )
            Text("Done")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = Date(selection)
                dueDate = selectedDate
                onDateSelected(selectedDate)
            }
            datePicker.show((context as androidx.fragment.app.FragmentActivity).supportFragmentManager, "DATE_PICKER")
        }) {
            Text(dueDate?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "Select Date")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onClose, modifier = Modifier.weight(1f)) {
                Text("Close")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val newTask = Task(
                    title = title,
                    description = description,
                    isDone = isDone,
                    dueDate = dueDate,
                    id = task?.id ?: 0L // Provide default id value
                )
                onSave(newTask)
            }, modifier = Modifier.weight(1f)) {
                Text("Save")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (task != null) {
            Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
                Text("Delete")
            }
        }
    }
}