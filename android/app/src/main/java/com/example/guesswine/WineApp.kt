package com.example.guesswine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessWineApp(vm: WineViewModel = viewModel()) {
    val state = vm.uiState
    var helpDialogText by remember { mutableStateOf<String?>(null) }
    var showTableDialog by remember { mutableStateOf(false) }

    val warmLight = lightColorScheme(
        primary = Color(0xFF8A1E2A),
        secondary = Color(0xFF7A5134),
        tertiary = Color(0xFF3F6A5A),
        background = Color(0xFFF9F6F1),
        surface = Color(0xFFFFFBF5),
    )
    val warmDark = darkColorScheme(
        primary = Color(0xFFF2A7A7),
        secondary = Color(0xFFE5C3A3),
        tertiary = Color(0xFF9ED5C1),
    )

    MaterialTheme(colorScheme = if (androidx.compose.foundation.isSystemInDarkTheme()) warmDark else warmLight) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Identificador de vino español") },
                    actions = {
                        IconButton(onClick = { showTableDialog = true }) {
                            Icon(Icons.Default.Info, contentDescription = "Ver tabla base")
                        }
                        IconButton(onClick = { vm.reset() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reiniciar cata")
                        }
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                if (state.phase != AppPhase.START) {
                    val progress = when (state.phase) {
                        AppPhase.VISUAL -> 0.25f
                        AppPhase.OLFATIVA -> 0.50f
                        AppPhase.GUSTATIVA -> 0.75f
                        AppPhase.RESULTADOS -> 1.00f
                        AppPhase.START -> 0f
                    }
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = when (state.phase) {
                            AppPhase.VISUAL -> "Fase actual: Visual"
                            AppPhase.OLFATIVA -> "Fase actual: Olfativa"
                            AppPhase.GUSTATIVA -> "Fase actual: Gustativa"
                            AppPhase.RESULTADOS -> "Fase actual: Resultados"
                            AppPhase.START -> ""
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                when (state.phase) {
                    AppPhase.START -> StartScreen(
                        onStart = { vm.start() },
                        onLoadExample = { vm.loadExample(it) },
                        onShowTable = { showTableDialog = true },
                    )
                    AppPhase.VISUAL -> VisualScreen(
                        state = state,
                        onUpdate = vm::update,
                        onNext = { vm.submitVisual() },
                        onBack = { vm.goToPhase(AppPhase.START) },
                        onHelp = { helpDialogText = it },
                    )
                    AppPhase.OLFATIVA -> OlfativaScreen(
                        state = state,
                        onUpdate = vm::update,
                        onNext = { vm.submitOlfativa() },
                        onBack = { vm.goToPhase(AppPhase.VISUAL) },
                        onHelp = { helpDialogText = it },
                    )
                    AppPhase.GUSTATIVA -> GustativaScreen(
                        state = state,
                        onUpdate = vm::update,
                        onCalculate = { vm.submitGustativaAndCalculate() },
                        onBack = { vm.goToPhase(AppPhase.OLFATIVA) },
                        onHelp = { helpDialogText = it },
                    )
                    AppPhase.RESULTADOS -> ResultsScreen(
                        state = state,
                        onBack = { vm.goToPhase(AppPhase.GUSTATIVA) },
                        onRestart = { vm.reset() },
                        onShowTable = { showTableDialog = true },
                    )
                }
            }
        }
    }

    if (showTableDialog) {
        WineTableDialog(onDismiss = { showTableDialog = false })
    }
    helpDialogText?.let { helpText ->
        AlertDialog(
            onDismissRequest = { helpDialogText = null },
            title = { Text("Ayuda") },
            text = { Text(helpText) },
            confirmButton = {
                TextButton(onClick = { helpDialogText = null }) {
                    Text("Cerrar")
                }
            },
        )
    }
}

@Composable
private fun StartScreen(
    onStart: () -> Unit,
    onLoadExample: (String) -> Unit,
    onShowTable: () -> Unit,
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Esta app te guía en una cata para identificar el vino. Introduce observaciones en cada fase.",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text("Flujo: visual -> olfativa -> gustativa -> resultados (top 3).")
                Text("Tip general: usa una copa limpia, buena luz y anota sensaciones antes de decidir.")
            }
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Catas predefinidas para testing", fontWeight = FontWeight.Bold)
                Text("Cargan datos de ejemplo para validar rápidamente la clasificación.")
                WineCatalog.exampleTastings.keys.forEach { name ->
                    OutlinedButton(
                        onClick = { onLoadExample(name) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(name)
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onStart, modifier = Modifier.weight(1f)) {
                Text("Comenzar cata")
            }
            OutlinedButton(onClick = onShowTable, modifier = Modifier.weight(1f)) {
                Text("Ver tabla base")
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Consejos rápidos de cata", fontWeight = FontWeight.Bold)
                Text("• Visual: inclina la copa sobre fondo blanco.")
                Text("• Olfativa: gira suavemente y huele en intervalos cortos.")
                Text("• Gustativa: evalúa acidez, cuerpo, alcohol y persistencia.")
            }
        }
    }
}

@Composable
private fun VisualScreen(
    state: WineUiState,
    onUpdate: ((WineUiState) -> WineUiState) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onHelp: (String) -> Unit,
) {
    FormScreenContainer(title = "Fase 1: Visual", tip = "Inclina la copa sobre un fondo blanco para apreciar tono y brillo.") {
        ErrorList(state.errors)

        DropdownSelector(
            label = "Color",
            value = state.visualColor,
            options = WineFormOptions.visualColors,
            helpText = "Ejemplos: amarillo pajizo, rojo rubí, rosado salmón.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(visualColor = value) } },
        )

        OutlinedTextField(
            value = state.visualColorOtro,
            onValueChange = { value -> onUpdate { current: WineUiState -> current.copy(visualColorOtro = value) } },
            label = { Text("Otro color (si aplica)") },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text("Solo si has seleccionado 'Otro (escribir abajo)'.")
            },
        )

        DropdownSelector(
            label = "Densidad",
            value = state.visualDensidad,
            options = WineFormOptions.density,
            helpText = "Mayor densidad suele indicar más cuerpo o alcohol.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(visualDensidad = value) } },
        )

        DropdownSelector(
            label = "Lágrima / glicerina",
            value = state.visualLagrima,
            options = WineFormOptions.tears,
            helpText = "Gotas que caen lento pueden sugerir mayor alcohol o glicerina.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(visualLagrima = value) } },
        )

        DropdownSelector(
            label = "Claridad",
            value = state.visualClaridad,
            options = WineFormOptions.clarity,
            helpText = "Limpio: sin velos ni partículas visibles. Turbio: opaco o con sedimentos.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(visualClaridad = value) } },
        )

        BooleanRow(
            label = "¿Observas burbujas?",
            checked = state.visualBurbujas,
            onCheckedChange = { checked -> onUpdate { current: WineUiState -> current.copy(visualBurbujas = checked) } },
            helpText = "Pista clave para espumosos.",
            onHelp = onHelp,
        )

        NavigationButtons(
            leftLabel = "Volver al inicio",
            rightLabel = "Siguiente: Fase olfativa",
            onLeft = onBack,
            onRight = onNext,
        )
    }
}

@Composable
private fun OlfativaScreen(
    state: WineUiState,
    onUpdate: ((WineUiState) -> WineUiState) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onHelp: (String) -> Unit,
) {
    FormScreenContainer(title = "Fase 2: Olfativa", tip = "Gira suavemente la copa y huele en intervalos cortos para evitar saturación.") {
        ErrorList(state.errors)

        DropdownSelector(
            label = "Intensidad olfativa",
            value = state.olfIntensidad,
            options = WineFormOptions.smellIntensity,
            helpText = "Valora cuánto aroma percibes al acercar la copa.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(olfIntensidad = value) } },
        )

        MultiChoiceGroup(
            label = "Aromas primarios",
            options = WineFormOptions.primaryAromas,
            selected = state.olfAromasPrimarios,
            helpText = "Ejemplo: cítricos en blancos jóvenes, fruta roja en rosados/tintos.",
            onHelp = onHelp,
            onToggle = { item ->
                onUpdate { ui: WineUiState -> ui.copy(olfAromasPrimarios = toggleSet(ui.olfAromasPrimarios, item)) }
            },
        )

        MultiChoiceGroup(
            label = "Aromas secundarios (fermentación)",
            options = WineFormOptions.secondaryAromas,
            selected = state.olfAromasSecundarios,
            helpText = "Levadura/panadería suelen aparecer en espumosos o vinos sobre lías.",
            onHelp = onHelp,
            onToggle = { item ->
                onUpdate { ui: WineUiState -> ui.copy(olfAromasSecundarios = toggleSet(ui.olfAromasSecundarios, item)) }
            },
        )

        MultiChoiceGroup(
            label = "Aromas terciarios (crianza)",
            options = WineFormOptions.tertiaryAromas,
            selected = state.olfAromasTerciarios,
            helpText = "Vainilla y tostado suelen indicar barrica o crianza.",
            onHelp = onHelp,
            onToggle = { item ->
                onUpdate { ui: WineUiState -> ui.copy(olfAromasTerciarios = toggleSet(ui.olfAromasTerciarios, item)) }
            },
        )

        NavigationButtons(
            leftLabel = "Volver a visual",
            rightLabel = "Siguiente: Fase gustativa",
            onLeft = onBack,
            onRight = onNext,
        )
    }
}

@Composable
private fun GustativaScreen(
    state: WineUiState,
    onUpdate: ((WineUiState) -> WineUiState) -> Unit,
    onCalculate: () -> Unit,
    onBack: () -> Unit,
    onHelp: (String) -> Unit,
) {
    FormScreenContainer(title = "Fase 3: Gustativa", tip = "Toma un sorbo pequeño y evalúa ataque, medio de boca y final.") {
        ErrorList(state.errors)

        MultiChoiceGroup(
            label = "Sensaciones de sabor",
            options = WineFormOptions.flavors,
            selected = state.gusSabor,
            helpText = "Puedes seleccionar varias si conviven (p. ej. seco y ácido).",
            onHelp = onHelp,
            onToggle = { item ->
                onUpdate { ui: WineUiState -> ui.copy(gusSabor = toggleSet(ui.gusSabor, item)) }
            },
        )

        DropdownSelector(
            label = "Cuerpo",
            value = state.gusCuerpo,
            options = WineFormOptions.body,
            helpText = "Ligero = paso fácil; pleno = sensación más densa y envolvente.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(gusCuerpo = value) } },
        )

        DropdownSelector(
            label = "Acidez",
            value = state.gusAcidez,
            options = WineFormOptions.acidity,
            helpText = "Acidez alta aporta frescura y mayor salivación.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(gusAcidez = value) } },
        )

        DropdownSelector(
            label = "Alcohol (sensación de calor)",
            value = state.gusAlcohol,
            options = WineFormOptions.alcohol,
            helpText = "El calor en garganta y boca ayuda a estimar graduación.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(gusAlcohol = value) } },
        )

        DropdownSelector(
            label = "Persistencia",
            value = state.gusPersistencia,
            options = WineFormOptions.persistence,
            helpText = "Tiempo que dura el sabor después de tragar o escupir.",
            onHelp = onHelp,
            onValueSelected = { value -> onUpdate { current: WineUiState -> current.copy(gusPersistencia = value) } },
        )

        BooleanRow(
            label = "Efervescencia en boca",
            checked = state.gusEfervescencia,
            onCheckedChange = { checked -> onUpdate { current: WineUiState -> current.copy(gusEfervescencia = checked) } },
            helpText = "Ayuda a detectar espumosos o vinos de aguja.",
            onHelp = onHelp,
        )

        BooleanRow(
            label = "Sensación salina",
            checked = state.gusSalinidad,
            onCheckedChange = { checked -> onUpdate { current: WineUiState -> current.copy(gusSalinidad = checked) } },
            helpText = "Puede aparecer en perfiles de generosos biológicos.",
            onHelp = onHelp,
        )

        OutlinedTextField(
            value = state.gusNotas,
            onValueChange = { value -> onUpdate { current: WineUiState -> current.copy(gusNotas = value) } },
            label = { Text("Notas libres (opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            supportingText = { Text("Anota matices no cubiertos por los campos anteriores.") },
        )

        NavigationButtons(
            leftLabel = "Volver a olfativa",
            rightLabel = "Calcular resultados",
            onLeft = onBack,
            onRight = onCalculate,
        )
    }
}

@Composable
private fun ResultsScreen(
    state: WineUiState,
    onBack: () -> Unit,
    onRestart: () -> Unit,
    onShowTable: () -> Unit,
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Resultados de clasificación", style = MaterialTheme.typography.headlineSmall)
        if (state.vinoAtipico) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1D6))) {
                Text(
                    "No hay una coincidencia clara. Posible vino atípico. Vuelve atrás y añade más detalles.",
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        if (state.resultados.isEmpty()) {
            Card {
                Text("No hay resultados calculados todavía.", modifier = Modifier.padding(16.dp))
            }
        } else {
            state.resultados.forEachIndexed { index, result ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (index == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            "${index + 1}. ${result.tipo} - ${result.elaboracion} (${result.confianzaRelativa}%)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text("Uva probable: ${result.uvaProbable}")
                        Text("DO sugerida: ${result.doSugerida}")
                        Text("Zona sugerida: ${result.zona}")
                        Text("Elaboración: ${result.elaboracion}")
                        Text("Graduación estimada: ${result.graduacionEstimada}")
                        Text("Explicación: Basado en ${reasonText(result.reasons)}.")
                        Text("Score interno: ${result.score}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Resumen comparativo", fontWeight = FontWeight.Bold)
                state.resultados.forEach { item ->
                    Text("• ${item.tipo} / ${item.elaboracion} - ${item.confianzaRelativa}% - ${item.graduacionEstimada}")
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text("Volver a gustativa")
            }
            OutlinedButton(onClick = onShowTable, modifier = Modifier.weight(1f)) {
                Text("Tabla base")
            }
        }
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text("Reiniciar cata completa")
        }
    }
}

@Composable
private fun FormScreenContainer(
    title: String,
    tip: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scroll = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            Text(title, style = MaterialTheme.typography.headlineSmall)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f))) {
                Text("Tip: $tip", modifier = Modifier.padding(16.dp))
            }
            content()
        },
    )
}

@Composable
private fun ErrorList(errors: List<String>) {
    if (errors.isEmpty()) return
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE5E5))) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Revisa estos campos:", fontWeight = FontWeight.Bold)
            errors.forEach { err ->
                Text("• $err")
            }
        }
    }
}

@Composable
private fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    helpText: String,
    onHelp: (String) -> Unit,
    onValueSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        FieldLabel(label = label, helpText = helpText, onHelp = onHelp)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(value)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiChoiceGroup(
    label: String,
    options: List<String>,
    selected: Set<String>,
    helpText: String,
    onHelp: (String) -> Unit,
    onToggle: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        FieldLabel(label = label, helpText = helpText, onHelp = onHelp)
        Card {
            Column(Modifier.padding(8.dp)) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(option) }
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = selected.contains(option),
                            onCheckedChange = { onToggle(option) },
                        )
                        Text(option)
                    }
                }
            }
        }
        if (selected.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Seleccionado:", style = MaterialTheme.typography.bodySmall)
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                selected.sorted().forEach { item ->
                    FilterChip(
                        selected = true,
                        onClick = { onToggle(item) },
                        label = { Text(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BooleanRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    helpText: String,
    onHelp: (String) -> Unit,
) {
    Card {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            FieldLabel(label = label, helpText = helpText, onHelp = onHelp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(if (checked) "Sí" else "No")
                Switch(checked = checked, onCheckedChange = onCheckedChange)
            }
        }
    }
}

@Composable
private fun FieldLabel(label: String, helpText: String, onHelp: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        AssistChip(
            onClick = { onHelp(helpText) },
            label = { Text("Ayuda") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            },
        )
    }
}

@Composable
private fun NavigationButtons(
    leftLabel: String,
    rightLabel: String,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedButton(onClick = onLeft, modifier = Modifier.weight(1f)) {
            Text(leftLabel)
        }
        Button(onClick = onRight, modifier = Modifier.weight(1f)) {
            Text(rightLabel)
        }
    }
}

@Composable
private fun WineTableDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tabla base de vinos (referencia)") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Se usa esta tabla exacta como base interna de clasificación.")
                WineCatalog.wineTableRows.forEach { row ->
                    Card {
                        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("TIPO: ${row.tipo}", fontWeight = FontWeight.Bold)
                            Text("UVA: ${row.uva}")
                            Text("Subtipo/Elaboración principal: ${row.subtipoElaboracionPrincipal}")
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
    )
}

private fun <T> toggleSet(current: Set<T>, item: T): Set<T> {
    return if (current.contains(item)) current - item else current + item
}
