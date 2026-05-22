# Auditoría de Integración AdMob - MyTasks

**Fecha**: 2026-05-22
**Rama**: `test_AdMob_ID`
**Resultado**: ⚠️ **4 PROBLEMAS CRÍTICOS ENCONTRADOS**

---

## 1. INICIALIZACIÓN DE MobileAds

✅ **CORRECTO**
- Se inicializa en `TaskApplication.onCreate()` (línea 21)
- Llamada a `MobileAds.initialize(this)` antes de cargar ads
- Momento adecuado (en el ciclo de vida de la aplicación)

```kotlin
// TaskApplication.kt:21
MobileAds.initialize(this)
```

---

## 2. CARGA DE ADS

✅ **Parcialmente correcto**
- **Tipo soportado**: Solo banners (AdSize.BANNER)
- **Tipos NO soportados**: Interstitials, Rewarded (no hay código)
- **Ubicación**: AdFragment en bottom bar de MainActivity

```kotlin
// GoogleAdService.kt:16
adView.setAdSize(AdSize.BANNER)
```

---

## 3. REFRESH RATE (⚠️ CRÍTICO)

❌ **PROBLEMA: Refresh sin control en onResume()**

```kotlin
// AdFragment.kt:49-52
override fun onResume() {
    super.onResume()
    adViewModel.loadAd()  // ← SE CARGA CADA VEZ QUE SE RESUME
}
```

**El problema**:
- AdView se recarga **cada vez** que el fragment entra en foreground
- Si el usuario navega a otra pantalla y vuelve = nuevo ad request
- **NO hay throttling ni debounce**
- **NO hay comprobación** de si ya hay un ad válido

**Riesgo de "invalid traffic"**: ALTO
- Usuario navega: Home → Detail → Home = 3 ad requests en segundos
- AdMob detecta patrón anómalo = marcar como invalid traffic

---

## 4. FRECUENCIA DE AD REQUESTS

❌ **PROBLEMA: Múltiples requests sin límite**

| Evento | Llamada | Frecuencia |
|--------|---------|-----------|
| onCreate Fragment | `setupViewModel()` + `loadAd()` | 1x |
| onResume Fragment | `loadAd()` | **Cada navegación** |
| onViewCreated | El ad se inicializa | 1x |
| onDestroyView | Se destruye | 1x |

**Sin control de frecuencia mínima**:
```kotlin
// AdViewModel.kt:24-28
fun loadAd() {
    _adView.value?.let { adView ->
        repository.loadAd(adView, ...)  // Sin verificar estado anterior
    }
}
```

---

## 5. MOMENTO DE AD REQUESTS

⚠️ **PROBLEMA: Triggered indirectamente por navegación del usuario**

```
User Action          → Fragment Lifecycle → Ad Request
─────────────────────────────────────────────────────
Click navigation    → onResume()         → loadAd()
Go back             → onResume()         → loadAd()
Rotate screen       → onResume()         → loadAd()  ✗
Minimize app        → onResume()         → loadAd()  ✗
```

**No hay ad request explícito por acción del usuario** (esto es bueno), pero cada cambio de visibility = new request (esto es malo).

---

## 6. TEST ADS EN RELEASE BUILDS

🚨 **PROBLEMA CRÍTICO: Test IDs hardcodeados**

### Situación Actual:

**Constants.kt** (línea 4):
```kotlin
const val AD_ID = "ca-app-pub-3940256099942544/9214589741"  // Test AdMob ID
//    const val AD_ID = "ca-app-pub-5163472824682213/1226263826"  // Real Banner ID
```

**strings.xml** (líneas 23-26):
```xml
<string name="ad_unit_id">ca-app-pub-5163472824682213~4420222935</string>  <!-- Real App ID -->
<string name="ad_id">ca-app-pub-3940256099942544/9214589741</string>  <!-- Test ID ← USADO -->
<!--<string name="ad_id">ca-app-pub-5163472824682213/1226263826</string>  Real Banner -->
```

**EL PROBLEMA**:
- ❌ El código usa `Constants.AD_ID` (Test), no `strings.xml`
- ❌ **Release builds usan Test IDs**
- ❌ Esto causa que AdMob marque como "invalid traffic"
- ❌ Los test IDs deben NUNCA estar en production

**Resultado**: Incluso en release, se está usando el Ad Unit de prueba. AdMob lo detecta y penaliza.

---

## 7. PATRONES SOSPECHOSOS (Invalid Traffic Risk)

### 🚨 Patrón 1: Rapid Ad Loading
```kotlin
// AdFragment.kt
override fun onViewCreated() {
    setupViewModel()
    adViewModel.loadAd()  // Carga 1
}

override fun onResume() {
    super.onResume()
    adViewModel.loadAd()  // Carga 2 (si ya estaba visible)
}
```

Si el fragment está visible y se llama `onResume()`:
- El ad se recarga sin necesidad
- AdMob ve múltiples requests desde el mismo device en segundos

### 🚨 Patrón 2: No hay validación de estado previo

```kotlin
// GoogleAdService.kt:20-22
fun loadAd(adView: AdView, ...) {
    val adRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)  // ← No verifica si ya hay uno cargado
    adView.adListener = object : AdListener() { ... }
}
```

Cada llamada a `loadAd()` = nueva request a AdMob servers, aunque el anterior aún sea válido.

### 🚨 Patrón 3: AdView no se destruye en onPause()

```kotlin
// AdFragment.kt
override fun onDestroyView() {
    adViewModel.adView.value?.destroy()  // ← Solo aquí
}
// NO hay onPause() con destroy()
```

El view permanece en memoria y activo entre onResume/onPause, permitiendo múltiples cargas.

### 🚨 Patrón 4: Error handling inefectivo

```kotlin
// GoogleAdService.kt:28-30
override fun onAdFailedToLoad(error: LoadAdError) {
    onAdFailed()  // ← Solo llama callback, no hay retry logic
}
```

Si falla un ad:
- El usuario ve un hueco vacío
- No hay retry exponencial
- No hay manejo de cuota agotada

---

## RESUMEN EJECUTIVO

### ✅ QUÉ ESTÁ BIEN
1. **Inicialización**: MobileAds.initialize() en el lugar correcto
2. **Arquitectura**: DI pattern con Koin está bien estructurado (Service → Repository → ViewModel)
3. **Cleanup**: AdView se destruye en onDestroyView()
4. **UI**: Banners se ocultan correctamente si no cargan (via LiveData observer)

### ❌ QUÉ ESTÁ MAL
1. **Test IDs en Production**: 🚨 **CRÍTICO** - usar test ads en release builds
2. **Refresh sin control**: 🚨 **CRÍTICO** - loadAd() en onResume() sin throttling
3. **No hay debounce**: ⚠️ **ALTO** - múltiples requests sin espacio mínimo
4. **Error handling minimal**: ⚠️ **MEDIO** - no hay retry logic

### 📊 IMPACTO EN INVALID TRAFFIC
**Probabilidad de penalización AdMob**: 95%

Razones:
- Test IDs en builds públicos → bandera de actividad sospechosa
- onResume reload → múltiples requests por navegación
- Sin throttling → patrón anómalo detectable
- Mismo device, multiple requests, corto timeframe → invalid traffic

---

## LECCIONES PARA EL MONOREPO

### 1. **Build Variants para Ad IDs**
```gradle
// buildTypes
buildTypes {
    debug {
        buildConfigField "String", "AD_UNIT_ID",
            "\"ca-app-pub-3940256099942544/9214589741\""  // Test
    }
    release {
        buildConfigField "String", "AD_UNIT_ID",
            "\"ca-app-pub-5163472824682213/1226263826\""  // Real
    }
}
```

### 2. **Ad Load State Management**
```kotlin
// Implementar flag para evitar reloads innecesarios
class AdViewModel {
    private var lastLoadTime = 0L
    private val MIN_RELOAD_INTERVAL = 60_000L  // 1 minuto

    fun loadAdIfNeeded() {
        val now = System.currentTimeMillis()
        if (now - lastLoadTime >= MIN_RELOAD_INTERVAL) {
            loadAd()
            lastLoadTime = now
        }
    }
}
```

### 3. **Lifecycle Management**
```kotlin
override fun onPause() {
    super.onPause()
    // Opcional: pausar ads
    adViewModel.pauseAd()
}

override fun onResume() {
    super.onResume()
    // Solo recargar si es primera vez, no cada resume
    if (!adViewModel.isAdLoaded()) {
        adViewModel.loadAd()
    }
}
```

### 4. **Separate Ad Modules**
```
adModule/
├── debug/        # Test ad config
│   └── AdConfig.kt
├── release/      # Real ad config
│   └── AdConfig.kt
└── common/       # Shared logic
    └── AdService.kt
```

### 5. **Monitoring y Logging**
```kotlin
private fun logAdEvent(event: String, details: String) {
    // Para debugging y detección de patrones anómalos
    analytics.log("ad_event", mapOf(
        "event" to event,
        "timestamp" to System.currentTimeMillis(),
        "details" to details
    ))
}
```

---

## RECOMENDACIONES INMEDIATAS

| Prioridad | Acción | Impacto |
|-----------|--------|--------|
| 🚨 1 | Cambiar Constants.AD_ID al ID real O usar BuildConfig | ⬆️⬆️⬆️ Critical |
| 🚨 2 | Agregar throttling a onResume() (no recargar cada vez) | ⬆️⬆️⬆️ Critical |
| ⚠️ 3 | Implementar build variants para debug/release | ⬆️⬆️ High |
| ⚠️ 4 | Destruir AdView en onPause(), no solo onDestroyView() | ⬆️ Medium |

---

## ARCHIVOS AUDITADOS

```
✓ TaskApplication.kt               - Inicialización
✓ GoogleAdService.kt               - Servicio base
✓ AdRepository.kt                  - Capa de datos
✓ AdViewModel.kt                   - Lógica de presentación
✓ AdFragment.kt                    - Ciclo de vida (PROBLEMA)
✓ AdScreen.kt                      - UI Compose
✓ Constants.kt                     - Config (PROBLEMA)
✓ strings.xml                      - Recursos
✓ build.gradle.kts                 - Build config
```
