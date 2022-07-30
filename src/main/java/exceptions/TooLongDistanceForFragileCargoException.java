package exceptions;

/**
 * Исключение, выбрасываемое попытке расчёта стоимости доставки хрупкого груза на расстояние более 30 км.
 */
public class TooLongDistanceForFragileCargoException extends Exception {
    private static final String MESSAGE = "К сожалению, хрупкие грузы не перевозятся на большие расстояния";

    public TooLongDistanceForFragileCargoException() {
        super(MESSAGE);
    }
}
