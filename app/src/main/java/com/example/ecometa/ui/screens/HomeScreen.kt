package com.example.ecometa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecometa.ui.components.EcoButton
import com.example.ecometa.ui.components.EcoCard
import com.example.ecometa.ui.components.EcoProgressBar
import com.example.ecometa.ui.theme.EcoGradientPrimary
import com.example.ecometa.ui.theme.EcoTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.extraLarge,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Novo Trajeto") }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header com Gradiente (Fidelidade ao .tsx)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = EcoGradientPrimary)
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Olá, Eco-Guardião!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Seu impacto ambiental hoje",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .offset(y = (-20).dp) // Efeito de sobreposição
            ) {
                // Card de Nível
                EcoCard {
                    Text(
                        text = "Nível Atual",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "Brotinho",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    EcoProgressBar(
                        progress = 0.65f,
                        label = "Próximo nível: Arbusto",
                        valueText = "650/1000 XP"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Métricas Rápidas
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        EcoCard {
                            Text("EcoPoints", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "1,250",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        EcoCard {
                            Text("CO2 Poupatdo", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "12.5 kg",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFF00B8D9)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Desafios Ativos (Prévia)
                Text(
                    text = "Desafios em Destaque",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                EcoCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pedalando pelo Clima", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Percorra 5km de bike hoje",
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoTextSecondary
                            )
                        }
                        Text("+50 pts", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // BottomSheet para Novo Trajeto
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                shape = MaterialTheme.shapes.extraLarge,
                containerColor = Color.White
            ) {
                RegistrationFormContent(onDismiss = { showBottomSheet = false })
            }
        }
    }
}

@Composable
fun RegistrationFormContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Registrar Trajeto",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Distância (km)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Simulação de Dropdown
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Meio de Transporte",
                modifier = Modifier.padding(16.dp),
                color = EcoTextSecondary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        EcoButton(
            text = "Confirmar Trajeto",
            onClick = { onDismiss() }
        )
    }
}
