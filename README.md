# FEN Parser – Proyecto Final Lenguajes y Paradigmas de computación

** Presentado por: Simón Arias Cuartas y Sofía Rojo Salazar
**Universidad EAFIT — School of Applied Sciences and Engineering**  
**Curso:** Lenguajes y Paradigmas de computación 
**Profesor:** Alexander Narváez Berrío  
**Fecha: 3 de noviembre 2025  

---

## Descripción del Proyecto

Este proyecto consiste en un **analizador sintáctico (parser)** de cadenas FEN (Forsyth–Edwards Notation) que representan posiciones de partidas de ajedrez.

El programa:
Recibe una cadena FEN por consola  
Valida que cumpla con la gramática oficial  
Muestra la posición en un tablero gráfico  
Si tiene errores, indica el tipo de error de parsing  

---

## Tecnologías utilizadas

| Tecnología | Uso |
|-----------|-----|
| **Java** | Lógica principal y validaciones |
| **Swing / GUI** | Renderizado del tablero |
| **Expresiones Regulares** | Validación léxica |

---

## Funcionalidad

- Procesa las 6 secciones de un string FEN:
  1 Piece Placement  
  2 Side to Move  
  3 Castling Ability  
  4 En Passant  
  5 Halfmove Clock  
  6 Fullmove Counter  

- Dibuja las piezas con caracteres **Unicode**
- Detecta errores como:
  - Más/menos de 8 filas
  - Número incorrecto de casillas por fila
  - Caracteres ilegales
  - Formato de movimiento inválido

---

## Requisitos Mínimos

- **Java 17+**
- Sistema operativo: Windows, macOS o Linux
- IDE recomendado: IntelliJ IDEA / Eclipse / VS Code

---

## Ejecución

```bash
javac FenParser.java
java FenParser
