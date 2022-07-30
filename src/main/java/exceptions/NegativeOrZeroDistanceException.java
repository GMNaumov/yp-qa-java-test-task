package exceptions;

/**
 * Исключение, выбрасываемое при использовании в расчёте нулевого или отрицательного расстояния доставки
 */
public class NegativeOrZeroDistanceException extends Exception{
    private static final String MESSAGE = "Расстояние доставки не может быть отрицательным или нулевым";

    public NegativeOrZeroDistanceException() {
        super(MESSAGE);
    }
}
