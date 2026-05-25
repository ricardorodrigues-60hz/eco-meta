package com.example.ecometa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecometa.ui.components.EcoCard
import com.example.ecometa.ui.theme.EcoTextSecondary

data class ActivityItem(
    val id: String,
    val type: String,
    val distance: String,
    val co2: String,
    val date: String,
    val icon: ImageVector
)

@Composable
fun HistoryScreen() {
    val activities = listOf(
        ActivityItem("1", "Bicicleta", "12 km", "1.44 kg", "Hoje", Icons.Default.DirectionsBike),
        ActivityItem("2", "Caminhada", "3 km", "0.36 kg", "Ontem", Icons.Default.DirectionsWalk),
        ActivityItem("3", "Ônibus", "15 km", "1.20 kg", "24 Mai", Icons.Default.DirectionsBus),
        ActivityItem("4", "Bicicleta", "8 km", "0.96 kg", "22 Mai", Icons.Default.DirectionsBike)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meu Histórico",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activities) { activity ->
                EcoCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = activity.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activity.type,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = activity.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoTextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = activity.co2,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF00B8D9),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = activity.distance,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}
