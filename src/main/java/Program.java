
import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final int WIN_COUNT = 3;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;


    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация объектов игры
     */
    static void initialize() {
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");


        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.printf("Введите координаты хода X и Y\n(от 1 до %s) через пробел: \n", Math.max(fieldSizeX, fieldSizeY));
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }


    /**
     * Проверка, является ли ячейка игрового поля пустой
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности координат хода
     *
     * @param x
     * @param y
     * @return
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {

        int x = 0;
        int y = 0;
        if (playerWinNextTurn()) {
            aiIntellect();
        } else {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            } while (!isCellEmpty(x, y));
            field[x][y] = DOT_AI;
        }


    }

    static boolean playerWinNextTurn() {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN)) {
                        field[i][j] = DOT_EMPTY;
                        return true;
                    } else {
                        field[i][j] = DOT_EMPTY;
                    }
                }
            }
        }
        return false;
    }

    static void aiIntellect() {
        outerLoop:
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN)) {
                        field[i][j] = DOT_AI;
                        break outerLoop;
                    } else {
                        field[i][j] = DOT_EMPTY;
                    }
                }
            }
        }
    }


    /**
     * Проверка на ничью
     *
     * @return
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {

            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * Метод проверки победы
     *
     * @param dot фишка игрока
     * @return
     */
    static boolean checkWin(char dot) {
        int counter = 0;

        //Проверка по горизонталям
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {

                if (field[i][j] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            counter = 0;
        }

        //Проверка по вертикали
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {

                if (field[j][i] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }

                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            counter = 0;
        }


        int countCircles = 0;
        //проверка диагоналей, разбитых на четверти
        for (int k = Math.min(fieldSizeX, fieldSizeY) - 1; k > 0; k--) {
            for (int f = k, l = 0; l < Math.min(fieldSizeX, fieldSizeY) - countCircles; f--, l++) {
                if (field[f][l] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            countCircles++;
            counter = 0;
        }

        for (int k = Math.min(fieldSizeX, fieldSizeY) - 1, u = 1; u < Math.min(fieldSizeX, fieldSizeY); u++) {
            for (int f = k, l = u; l < Math.min(fieldSizeX, fieldSizeY); f--, l++) {
                if (field[f][l] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            counter = 0;
        }

        for (int k = (Math.min(fieldSizeX, fieldSizeY) - 1); k >= 0; k--) {
            for (int l = ((Math.min(fieldSizeX, fieldSizeY) - 1)), i = k; i >= 0; i--, l--) {
                if (field[l][i] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            counter = 0;
        }
        //и проверить верх второй части
        for (int i = Math.min(fieldSizeX, fieldSizeY) - 2; i >= 0; i--) {
            for (int j = i, k = Math.min(fieldSizeX, fieldSizeY) - 1; j >= 0; j--, k--) {
                if (field[j][k] == dot) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == WIN_COUNT) {
                    return true;
                }
            }
            counter = 0;
        }


        return false;
    }

    /**
     * Проверка состояния игры
     *
     * @param dot фишка игрока
     * @param s   победный слоган
     * @return
     */
    static boolean checkState(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

}