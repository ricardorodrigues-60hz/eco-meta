package com.example.ecometa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecometa.ui.components.EcoCard
import com.example.ecometa.ui.components.EcoProgressBar
import com.example.ecometa.ui.theme.EcoTextSecondary

data class Challenge(
    val title: String,
    val description: String,
    val progress: Float,
    val valueText: String,
    val reward: String
)

@Composable
fun ChallengesScreen() {
    val challenges = listOf(
        Challenge("Pedalando pelo Clima", "Percorra 5km de bike hoje", 0.6f, "3/5 km", "+50 pts"),
        Challenge("Caminhada Matinal", "Realize 3 caminhadas nesta semana", 1.0f, "3/3", "Concluído"),
        Challenge("Transporte Público", "Use ônibus ou metrô 5 vezes", 0.4f, "2/5", "+100 pts"),
        Challenge("Mestre da Eco-Mobilidade", "Poupe 10kg de CO2 no mês", 0.25f, "2.5/10 kg", "+500 pts")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Desafios",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(challenges) { challenge ->
                EcoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = if (challenge.progress >= 1f) Color(0xFFFFD700) else EcoTextSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = challenge.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = challenge.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoTextSecondary
                            )
                        }
                        Text(
                            text = challenge.reward,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (challenge.progress >= 1f) MaterialTheme.colorScheme.primary else Color(0xFF00B8D9)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    EcoProgressBar(
                        progress = challenge.progress,
                        label = "Progresso",
                        valueText = challenge.valueText
                    )
                }
            }
        }
    }
}
