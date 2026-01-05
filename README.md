## Sistema de puntajes

- **Intento no exitoso** → puntaje = **0**
- **Intento exitoso** → puntaje = **100** menos penalizaciones:
  - **-1 punto / segundo** (tiempo)
  - **-2 puntos / riel extra** (solo si > 5 rieles)
  - **-10** si el nivel es **MEDIO**, **-20** si es **DIFÍCIL**
- Puntaje final acotado a **0–100**

### Estrellas — interpretación clara

- **3 ★ — Excelente**: puntaje >= 90 (ruta óptima)
- **2 ★ — Bueno**: 60 ≤ puntaje < 90 (buen desempeño)
- **1 ★ — Mejorable**: puntaje < 60 (completado pero con ineficiencias)

_Ejemplo_: DIFICIL, 30s, 7 rieles → 100 − 30 − 4 − 20 = **46** → **1 ★**

(Implementación: `util.ScoreCalculator.calcularPuntaje(Intento, Nivel)`.)
## Ejemplo de ejecución (vista del jugador)

Breve paso a paso:
1. El jugador inicia la partida y se registra (p. ej. `Jugador(1, "Ana")`).
2. Se crean los 3 niveles por defecto (`nivelManager.crearNivelesPorDefecto()`): 1=FÁCIL, 2=MEDIO, 3=DIFÍCIL.
3. El jugador selecciona un nivel y realiza un intento indicando rieles y el tiempo.
4. Al finalizar, el sistema muestra el puntaje y las estrellas, y actualiza el ranking.

Ejemplo (salida de la demo incluida):

Intento 1: puntaje=55, estrellas=1
Intento 2: puntaje=4, estrellas=1
Intento 3: puntaje=46, estrellas=1
Top 10:
Jugador{id=1, nombre='Ana'} - Mejor puntaje: 55

- Cómo ejecutar la demo:
  - Compilar: `javac -d out src\\negocio\\*.java src\\util\\*.java`
  - Ejecutar: `java -cp out util.Main`

(La demo muestra exactamente la experiencia de un jugador que completa 3 niveles y cómo se calculan los puntajes.)