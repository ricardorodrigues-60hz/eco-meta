package com.example.ecometa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecometa.ui.components.EcoBadge
import com.example.ecometa.ui.components.EcoCard
import com.example.ecometa.ui.theme.EcoTextSecondary

data class RankUser(
    val name: String,
    val points: String,
    val level: String
)

@Composable
fun RankingScreen() {
    val rankingList = listOf(
        RankUser("Ana Silva", "4,250", "Guardiã"),
        RankUser("João Santos", "3,890", "Protetor"),
        RankUser("Você", "1,250", "Brotinho"),
        RankUser("Carla Dias", "980", "Semente"),
        RankUser("Marcos Oliveira", "750", "Semente")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ranking Global",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Competição saudável por um planeta melhor",
            style = MaterialTheme.typography.bodyMedium,
            color = EcoTextSecondary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(rankingList) { index, user ->
                val isMe = user.name == "Você"
                
                EcoCard(
                    backgroundColor = if (isMe) MaterialTheme.colorScheme.primaryContainer else Color.White
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        EcoBadge(
                            text = (index + 1).toString(),
                            containerColor = when (index) {
                                0 -> Color(0xFFFFD700) // Ouro
                                1 -> Color(0xFFC0C0C0) // Prata
                                2 -> Color(0xFFCD7F32) // Bronze
                                else -> MaterialTheme.colorScheme.secondary
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isMe) FontWeight.Bold else FontWeight.SemiBold
                            )
                            Text(
                                text = user.level,
                                style = MaterialTheme.typography.labelSmall,
                                color = EcoTextSecondary
                            )
                        }

                        Text(
                            text = "${user.points} pts",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
