package pl.wsei.pam.lab07.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.wsei.pam.lab07.data.TodoTask

@Composable
fun ListItem(item: TodoTask, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Priority: ${item.priority}")
                Text(text = "Deadline: ${item.deadline}")
            }
            if (item.isDone) {
                Text(text = "Status: Done", color = MaterialTheme.colorScheme.primary)
            } else {
                Text(text = "Status: Pending", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
