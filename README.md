# Proyecto Final - Juego de Construcción de Rutas para Trenes

Sistema de juego donde los jugadores construyen rutas para trenes sobre mapas predefinidos con obstáculos. El objetivo es conectar la estación de inicio con la de salida usando rieles, optimizando tiempo de construcción y cantidad de rieles.

---

## Características principales

- **3 niveles de dificultad** (FACIL, MEDIO, DIFICIL) con mapas predefinidos cargados desde plantillas
- **Modo construcción con temporizador**: el jugador coloca rieles mientras corre el tiempo
- **Simulación automática del tren**: al presionar Play, el tren recorre la ruta construida
- **Sistema de puntaje con estrellas**: penaliza tiempo (primeros 10s gratis) y rieles extra (>5)
- **Validaciones en tiempo real**: impide colocar rieles sobre obstáculos, estaciones o fuera del mapa
- **Ranking de jugadores**: guarda el mejor puntaje de cada jugador

---

## Sistema de puntajes

- **Intento no exitoso** → puntaje = **0**
- **Intento exitoso** → puntaje = **100** menos penalizaciones:
  - **-1 punto / segundo** sobre los primeros 10s (primeros 10 segundos NO se penalizan)
  - **-2 puntos / riel extra** (solo si > 5 rieles)
  - **-10** si el nivel es **MEDIO**, **-20** si es **DIFÍCIL**
- Puntaje final acotado a **0–100**

### Estrellas — interpretación clara

- **3 ★ — Excelente**: puntaje >= 90 (ruta óptima)
- **2 ★ — Bueno**: 60 ≤ puntaje < 90 (buen desempeño)
- **1 ★ — Mejorable**: puntaje < 60 (completado pero con ineficiencias)

_Ejemplo_: DIFICIL, 12s construcción, 7 rieles → 100 − 2 (tiempo sobre 10s) − 4 (2 rieles extra) − 20 (ajuste dificultad) = **74** → **2 ★**

(Implementación: `util.ScoreCalculator.calcularPuntaje(Intento, Nivel)`.)

---

---

## Flujo del juego — Vista desde el jugador

### 1. Inicio de partida
- El jugador se registra: `GameManager.iniciarPartida(jugador)`
- Se cargan automáticamente 3 niveles desde plantillas en `resources/levels/`:
  - **Nivel 1 (FACIL)**: Mapa 5×5 con 3 obstáculos bloqueando el camino central
  - **Nivel 2 (MEDIO)**: Mapa 7×7 con 6 obstáculos en eje central requiriendo rodeos
  - **Nivel 3 (DIFICIL)**: Mapa 9×9 con 12 obstáculos distribuidos en cuadrícula

### 2. Selección de nivel
- El jugador elige nivel 1, 2 o 3
- El mapa ya contiene **estaciones** (inicio en columna izquierda, salida en columna derecha) y **obstáculos** predefinidos

### 3. Modo construcción (colocar rieles)
- El jugador presiona **Iniciar construcción**: `GameManager.iniciarConstruccion(nivel)`
  - Comienza a correr el **temporizador de construcción**
- El jugador coloca rieles en el mapa:
  - `nivelManager.agregarRiel(nivel, new RielRecto(x, y, orientacion))` o `new RielCurvo(...)`
  - Orientación: 0/180 (vertical), 90/270 (horizontal)
  - **Validaciones automáticas**: no se permite colocar sobre obstáculos, estaciones o donde ya existe otro riel
- El jugador **solo puede colocar rieles** (no puede modificar obstáculos ni estaciones)

### 4. Ejecutar simulación (Play)
- El jugador presiona **Play**: `GameManager.playNivel()`
  - Se detiene el temporizador de construcción (ese tiempo se usa para el score)
  - Se **simula automáticamente** el recorrido del tren:
    - Sale desde la estación inicio (columna izquierda) moviéndose hacia el ESTE
    - Sigue los rieles colocados, consultando `Rieles.obtenerSalida(entrada)` en cada celda
    - Si llega a la estación fin → éxito
    - Si choca con obstáculo, descarrila o sale del mapa → fracaso (puntaje 0)

### 5. Resultados
- Si exitoso: se calcula el puntaje (100 − penalizaciones) y las estrellas (1-3)
- El intento se registra en el historial del jugador
- El ranking se actualiza con el mejor puntaje del jugador

> Nota clave: los primeros **10 segundos** de construcción **no se penalizan**. Solo se penaliza el tiempo sobre esos 10s.

---

## Ejemplo de ejecución

Compilar y ejecutar:
```bash
javac -d out src\\negocio\\*.java src\\util\\*.java
java -cp out util.Main
```

Salida esperada:
```
Error al colocar riel (solapamiento): Imposible colocar riel: Ya existe un riel en la celda.
Error al colocar riel sobre estación: Imposible colocar riel: No se puede colocar riel sobre estación inicio.
Error al colocar riel sobre obstáculo: Imposible colocar riel: Celda ocupada por obstáculo.
Intento 1 (construcción): puntaje=100, estrellas=3
Intento 2: puntaje=14, estrellas=1
Intento 3: puntaje=56, estrellas=1
Top 10:
Jugador{id=1, nombre='Ana'} - Mejor puntaje: 100
Demo de lógica concluido.
```

---

## Flujo interno (implementación técnica)

### Componentes clave

- **`Juego`** — estado global de la partida (jugador actual, niveles, nivel activo)
- **`Jugador`** — información del jugador, historial de intentos
- **`Nivel`** — representa la cuadrícula (filas × columnas):
  - `mapaMatriz` — matriz con códigos: 0=vacío, 1=estación inicio, 2=estación fin, 3=obstáculo
  - `obstaculos` — lista de objetos `Obstaculo` (ej: `Piedra`)
  - `rieles` — lista de objetos `Rieles` (recto/curvo) con posición y orientación
  - Estaciones: `entradaX/Y`, `entradaDireccion`, `salidaX/Y`
- **`NivelManager`** — gestión de niveles:
  - `crearNivelesPorDefecto()` — carga automáticamente las 3 plantillas desde `resources/levels/`
  - `cargarDesdePlantilla(path)` — parsea archivo de plantilla y construye `Nivel`
  - Helpers: `agregarRiel`, `setEstacionInicio`, `setEstacionFin`
- **`GameManager`** — orquesta el flujo de juego:
  - `iniciarConstruccion(nivel)` — arranca temporizador
  - `playNivel()` — detiene temporizador, simula tren, calcula y registra puntaje
  - `simularTren(nivel)` — motor de simulación paso a paso
- **`ScoreCalculator`** — cálculo de puntaje con reglas (tiempo penalizable = tiempo - 10s, penalización de rieles >5, ajuste por dificultad)
- **`RankingManager`** — almacena y consulta mejores puntajes por jugador
- **`Intento`** — representa una ejecución: tiempo, rieles usados, éxito, puntaje
- **`Puntaje`** — valor numérico (0-100), estrellas (1-3) y descripción

### Validaciones implementadas

- **`Nivel.puedeColocarRiel(x,y,riel)`** → devuelve `Optional<String>`:
  - Verifica: fuera de rango, celda con obstáculo, sobre estación, riel ya existente
  - Si válido → `Optional.empty()`; si no → mensaje con razón
- **`Nivel.agregarRiel(riel)`** — usa `puedeColocarRiel` y lanza `Exception` con mensaje claro
- Los obstáculos solo se pueden colocar por código/diseñador (`nivel.agregarObstaculo(...)`) — los jugadores **no** pueden colocar obstáculos

### Simulación del tren

1. Inicia en `(entradaX, entradaY)` con dirección `entradaDireccion` (forzado a `ESTE` para iniciar desde izquierda)
2. En cada paso:
   - Calcula siguiente celda según dirección actual (NORTE/SUR/ESTE/OESTE)
   - Verifica si alcanzó la estación fin → éxito (solo si entra con dirección `ESTE`)
   - Verifica colisión: fuera del mapa, obstáculo, sin riel → fracaso
   - Consulta `riel.obtenerSalida(entradaSide)` para determinar nueva dirección
   - Si respuesta es `DESCARRILAMIENTO` → fracaso
3. Repite hasta éxito, fracaso o límite de pasos (evita loops infinitos)

### Tipos de rieles y orientación

- **`RielRecto`** — conecta 2 lados opuestos:
  - Orientación 0/180: vertical (NORTE ↔ SUR)
  - Orientación 90/270: horizontal (ESTE ↔ OESTE)
- **`RielCurvo`** — cambia dirección (ej: NORTE → ESTE)
  - Actualmente implementación simple; expandible para más combinaciones

---

## Plantillas de niveles (mapas predefinidos)

El sistema carga automáticamente 3 niveles desde archivos de plantilla en `resources/levels/`:

### Niveles disponibles

| Nivel | Archivo | Dificultad | Tamaño | Obstáculos | Descripción |
|-------|---------|------------|--------|------------|-------------|
| 1 | `level1.lvl` | FACIL | 5×5 | 2 | Obstáculos en (0,2), (3,2) - ruta directa por fila 2 disponible |
| 2 | `level2.lvl` | MEDIO | 7×7 | 6 | Obstáculos distribuidos dispersos en diferentes filas y columnas |
| 3 | `level3.lvl` | DIFICIL | 9×9 | 12 | Obstáculos distribuidos en patrón regular, múltiples rutas posibles |

### Formato de plantilla

Archivo de texto simple con estructura `clave: valor`:

```
numero: 1
dificultad: FACIL
filas: 5
columnas: 5

entrada: 2,0,ESTE
salida: 2,4

obstaculos:
1,2
3,2
2,3
```

**Campos:**
- `numero` — número del nivel (entero positivo)
- `dificultad` — FACIL, MEDIO o DIFICIL
- `filas`, `columnas` — dimensiones del mapa
- `entrada` — posición de estación inicio: `fila,columna,direccion` (debe estar en columna 0, dirección siempre ESTE)
- `salida` — posición de estación fin: `fila,columna` (debe estar en última columna)
- `obstaculos:` — sección seguida de líneas con `x,y[,TIPO]` (TIPO opcional, por defecto `PIEDRA`)

**Comentarios:** líneas que comienzan con `#` son ignoradas

### Carga automática

- `NivelManager.crearNivelesPorDefecto()` carga las 3 plantillas automáticamente
- Si falla la carga, crea niveles vacíos como fallback
- Método manual: `Nivel nivel = nivelManager.cargarDesdePlantilla("resources/levels/level1.lvl");`

### Ventajas

- **Diseño manual**: control total sobre disposición de obstáculos
- **Reutilizables**: mismo mapa para múltiples partidas
- **Modificables**: editar archivos `.lvl` sin recompilar código
- **Jugabilidad garantizada**: diseñador asegura que existe al menos una ruta posible