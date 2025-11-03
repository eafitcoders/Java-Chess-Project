import java.util.*;
public class FenParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ingrese una cadena FEN:");
        String fen = scanner.nextLine();

        String[] partes = fen.split("\\s+");
        if (partes.length != 6) {
            System.out.println("String FEN inválido");
            System.out.println("La cadena FEN debe tener 6 campos separados por espacios");
            System.out.println("Campos encontrados: " + partes.length);
            return;
        }
//El nombre de cada String es cada uno de los 6 campos de la cadena FEN

        String piecePlacement = partes[0];
        String sideToMove = partes[1];
        String castling = partes[2];
        String enPassant = partes[3];
        String halfmove = partes[4];
        String fullmove = partes[5];

        String[] filasSeparadas = piecePlacement.split("/");
        if (filasSeparadas.length != 8) {
            System.out.println("String FEN inválido");
            System.out.println("La sección de piezas ebe contener 8 filas separadas por '/'");
            System.out.println("Filas encontradas: " + filasSeparadas.length);
            return;
        }

        for (int i = 0; i < filasSeparadas.length; i++) {
            String fila = filasSeparadas[i];
            String filaError = validarFila(fila);
            if (filaError != null) {
                System.out.println("String FEN inválido");
                int filaMala = 8 - i;
                System.out.println("Fila errónea: " + filaMala + ":" + filaError);
                return;
            }
        }

        String sideError = validarSideToMove(sideToMove);
        if (sideError != null) {
            System.out.println("String FEN inválido");
            System.out.println("Side to move inválido: " + sideError);
            return;
        }


        String castlingError = validarCastling(castling);
        if (castlingError != null) {
            System.out.println("String FEN inválido");
            System.out.println("Castling inválido: " + castlingError);
            return;
        }

        String epError = validarEnPassant(enPassant);
        if (epError != null) {
            System.out.println("String FEN inválido");
            System.out.println("En passant inválido: " + epError);
            return;
        }

        String halfMoveError = validarHalfmove(halfmove);
        if (halfMoveError != null) {
            System.out.println("String FEN inválido");
            System.out.println("Halfmove clock inválido: " + halfMoveError);
            return;
        }

        String fullMoveError = validarFullmove(fullmove);
        if (fullMoveError != null) {
            System.out.println("String FEN inválido");
            System.out.println("Fullmove counter inválido: " + fullMoveError);
            return;
        }

        //Lo que hicimos a partir de aqui es empezar a construir el tablero:)
        String[][] board = construirTablero(filasSeparadas);
        System.out.println("FEN válido. Tablero:");
        imprimirTablero(board, sideToMove, castling, enPassant, halfmove, fullmove);
    }

    private static String validarFila(String fila) {
        if (fila == null || fila.isEmpty()) {
            return "fila vacía";
        }

        // Esto es en caso de que la fila esté vacia, ent : 8
        if (fila.equals("8")) {
            return null;
        }

        int suma = 0;
        for (int idx = 0; idx < fila.length(); idx++) {
            char c = fila.charAt(idx);

            if (esPieza(c)) {
                suma += 1;
            } else if (c >= '1' && c <= '7') {
                suma += (c - '0');
            } else if (c == '8') {

                return "se encontró '8' dentro de una fila con más caracteres; use '8' solo para fila completamente vacía";
            } else {
                return "carácter inválido '" + c + "' en la posición " + (idx + 1);
            }

            if (suma > 8) {
                return "la suma de columnas excede 8 (actual: " + suma + ")";
            }
        }

        if (suma != 8) {
            return "la suma de columnas es " + suma + " y debe ser 8";
        }
        return null;
    }

    private static boolean esPieza(char c) {
        switch (c) {
            case 'P':
            case 'N':
            case 'B':
            case 'R':
            case 'Q':
            case 'K':
            case 'p':
            case 'n':
            case 'b':
            case 'r':
            case 'q':
            case 'k':
                return true;
            default:
                return false;
        }
    }

    private static String validarSideToMove(String s) {
        if (s.length() != 1 || !(s.equals("w") || s.equals("b"))) {
            return "debe ser 'w' o 'b'";
        }
        return null;
    }

    private static String validarCastling(String c) {
        if (c.equals("-")) return null;
        if (c.isEmpty() || c.length() > 4) {
            return "longitud inválida; use '-', o 1 a 4 letras entre KQkq";
        }
        boolean k = false, q = false, kk = false, qq = false;
        for (int i = 0; i < c.length(); i++) {
            char ch = c.charAt(i);
            switch (ch) {
                case 'K':
                    if (k) return "duplicado 'K'";
                    k = true;
                    break;
                case 'Q':
                    if (q) return "duplicado 'Q'";
                    q = true;
                    break;
                case 'k':
                    if (kk) return "duplicado 'k'";
                    kk = true;
                    break;
                case 'q':
                    if (qq) return "duplicado 'q'";
                    qq = true;
                    break;
                default:
                    return "carácter inválido '" + ch + "'; solo KQkq o '-'";
            }
        }
        return null;
    }

    private static String validarEnPassant(String ep) {
        if (ep.equals("-")) return null;
        if (ep.length() != 2) {
            return "formato inválido; use '-' o una casilla como 'e3'/'e6'";
        }
        char file = ep.charAt(0);
        char rank = ep.charAt(1);
        if (file < 'a' || file > 'h') {
            return "columna inválida '" + file + "'; debe estar en 'a'..'h'";
        }
        if (!(rank == '3' || rank == '6')) {
            return "fila inválida '" + rank + "'; solo '3' o '6'";
        }
        return null;
    }

    private static String validarHalfmove(String hm) {
        if (!hm.matches("\\d+")) {
            return "debe ser un entero decimal >= 0";
        }

        return null;
    }

    private static String validarFullmove(String fm) {
        if (!fm.matches("\\d+")) {
            return "debe ser un entero decimal >= 1";
        }
        try {
            long v = Long.parseLong(fm);
            if (v < 1) return "debe ser >= 1";
        } catch (NumberFormatException ex) {
            return "número demasiado grande";
        }
        return null;
    }


    private static String[][] construirTablero(String[] filas) {
        String[][] board = new String[8][8];

        for (int i = 0; i < 8; i++) {
            String fila = filas[i];
            int col = 0;

            if (fila.equals("8")) {
                Arrays.fill(board[i], ".");
                continue;
            }

            for (int j = 0; j < fila.length(); j++) {
                char c = fila.charAt(j);
                if (esPieza(c)) {
                    board[i][col++] = unicodePieza(c);
                } else if (Character.isDigit(c)) {
                    int vacias = c - '0'; // aquí solo 1..7
                    for (int k = 0; k < vacias; k++) {
                        board[i][col++] = ".";
                    }
                }
            }
        }
        return board;
    }

    private static String unicodePieza(char c) {
        // Negras minúscula, Blancas mayúscula (convención FEN)
        switch (c) {
            // Blancas
            case 'K':
                return "♔";
            case 'Q':
                return "♕";
            case 'R':
                return "♖";
            case 'B':
                return "♗";
            case 'N':
                return "♘";
            case 'P':
                return "♙";
            // Negras
            case 'k':
                return "♚";
            case 'q':
                return "♛";
            case 'r':
                return "♜";
            case 'b':
                return "♝";
            case 'n':
                return "♞";
            case 'p':
                return "♟";
        }
        return "?";
    }

    private static void imprimirTablero(String[][] board,
                                        String sideToMove,
                                        String castling,
                                        String enPassant,
                                        String halfmove,
                                        String fullmove) {
        // Encabezado
        System.out.println();
        System.out.println("  +---------------------+");
        for (int i = 0; i < 8; i++) {
            int rank = 8 - i;
            System.out.printf("%d |", rank);
            for (int j = 0; j < 8; j++) {
                String cell = board[i][j];
                // separador y celda (alineado a 2 espacios)
                System.out.print(" " + (cell.length() == 1 ? cell : "?"));
            }
            System.out.println(" |");
        }
        System.out.println("  +---------------------+");
        System.out.println("    a b c d e f g h");
        System.out.println();
        System.out.println("Turno: " + (sideToMove.equals("w") ? "blancas (w)" : "negras (b)"));
        System.out.println("Enroques: " + castling);
        System.out.println("En passant: " + enPassant);
        System.out.println("Halfmove: " + halfmove);
        System.out.println("Fullmove: " + fullmove);

    }
}