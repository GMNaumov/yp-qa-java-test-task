import estimate.CargoDeliveryCostEstimate;
import exceptions.NegativeOrZeroDistanceException;
import exceptions.TooLongDistanceForFragileCargoException;
import models.CargoDimension;
import models.DeliveryServiceLoad;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testdata.TestUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class TestCargoDeliveryCostEstimate {
    private static final double ALLOWED_COST_DELTA = 0.0001; // Допустимое отклонение цены при расчётах стоимости
    private static final double LONG_DISTANCE = 30.00; // Максимально допустимое расстояние перевозки хрупкого груза
    private CargoDeliveryCostEstimate estimate;

    @BeforeEach
    void init() {
        estimate = new CargoDeliveryCostEstimate();
    }

    @ParameterizedTest
    @MethodSource("testDeliveryDataListForPositiveTest")
    @DisplayName("Позитивный сценарий тестирования. " +
            "Тестирование граничных значений расстояния доставки; " +
            "изменения стоимости доставки в зависимости от размера груза, хрупкости и загруженности службы доставки")
    void TestPositiveGetDeliveryFullCost(double deliveryDistance, CargoDimension cargoDimension, boolean isCargoFragile,
                                         DeliveryServiceLoad deliveryServiceLoad, double expectedDeliveryFullCost)
            throws TooLongDistanceForFragileCargoException, NegativeOrZeroDistanceException {
        double fullDeliveryCost = estimate.getDeliveryFullCost(deliveryDistance, cargoDimension,
                isCargoFragile, deliveryServiceLoad);
        Assertions.assertEquals(expectedDeliveryFullCost, fullDeliveryCost, ALLOWED_COST_DELTA);
    }

    @Test
    @DisplayName("Негативный сценарий тестирования. " +
            "Выброс исключения при передаче в расчёт значений null для размера груза и статуса службы доставки.")
    void TestNegativeGetDeliveryFullCost() {
        double distance = TestUtils.getRandomDistanceFromRange(0.00, 30.00);
        Assertions.assertThrows(
                NullPointerException.class,
                () -> estimate.getDeliveryFullCost(distance, null, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("testDeliveryDataListForNegativeTestTrowsDistanceException")
    @DisplayName("Негативный сценарий тестирования. " +
            "Выброс исключения для сценария перевозки груза на нулевое или отрицательное расстояние.")
    void TestNegativeGetDeliveryFullCostWithIncorrectDistanceThrowsException(double deliveryDistance,
                                                                             CargoDimension cargoDimension,
                                                                             boolean isCargoFragile,
                                                                             DeliveryServiceLoad deliveryServiceLoad) {
        String exceptionMessage = "Расстояние доставки не может быть отрицательным или нулевым";
        NegativeOrZeroDistanceException exception = Assertions.assertThrows(
                NegativeOrZeroDistanceException.class,
                () -> estimate.getDeliveryFullCost(deliveryDistance, cargoDimension,
                        isCargoFragile, deliveryServiceLoad)
        );
        Assertions.assertTrue(exception.getMessage().contains(exceptionMessage));
    }

    @ParameterizedTest
    @MethodSource("testDeliveryDataListForNegativeTestTrowsFragileCargoException")
    @DisplayName("Негативный сценарий тестирования. " +
            "Выброс исключения для сценария перевозки хрупкого груза на расстояние более 30 км.")
    void TestNegativeGetDeliveryFullCostWithFragileCargoAndLongDistanceThrowsException(double deliveryDistance,
                                                                           CargoDimension cargoDimension,
                                                                           boolean isCargoFragile,
                                                                           DeliveryServiceLoad deliveryServiceLoad) {
        String exceptionMessage = "К сожалению, хрупкие грузы не перевозятся на большие расстояния";
        TooLongDistanceForFragileCargoException exception = Assertions.assertThrows(
                TooLongDistanceForFragileCargoException.class,
                () -> estimate.getDeliveryFullCost(deliveryDistance, cargoDimension,
                        isCargoFragile, deliveryServiceLoad)
        );
        Assertions.assertTrue(exception.getMessage().contains(exceptionMessage));
    }

    static Stream<Arguments> testDeliveryDataListForPositiveTest() {
        return Stream.of(
                arguments(0.01, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(TestUtils.getRandomDistanceFromRange(0.00, 2.00), CargoDimension.SMALL,
                        false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(2.00, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(2.01, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(TestUtils.getRandomDistanceFromRange(2.00, 10.00), CargoDimension.SMALL,
                        false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(9.99, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(10.00, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(10.01, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(TestUtils.getRandomDistanceFromRange(10.00, 30.00), CargoDimension.SMALL,
                        false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(29.99, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(30.00, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(30.01, CargoDimension.SMALL, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(TestUtils.getRandomDistanceFromRange(30.00, Double.MAX_EXPONENT), CargoDimension.SMALL,
                        false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(1.00, CargoDimension.BIG, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(5.00, CargoDimension.BIG, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(15.00, CargoDimension.BIG, false, DeliveryServiceLoad.NORMAL, 400.0000),
                arguments(5.00, CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL, 600.0000),
                arguments(29.99, CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL, 700.0000),
                arguments(15.00, CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL, 700.0000),
                arguments(15.00, CargoDimension.BIG, true, DeliveryServiceLoad.INCREASED, 840.0000),
                arguments(15.00, CargoDimension.BIG, true, DeliveryServiceLoad.HIGH, 980.0000),
                arguments(15.00, CargoDimension.BIG, true, DeliveryServiceLoad.HEAVY, 1120.0000)
        );
    }

    static Stream<Arguments> testDeliveryDataListForNegativeTestTrowsDistanceException() {
        return Stream.of(
                arguments(0.0, CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL),
                arguments(TestUtils.getRandomDistanceFromRange(Double.MIN_EXPONENT, 0.0), CargoDimension.BIG, true,
                        DeliveryServiceLoad.NORMAL)
        );
    }

    static Stream<Arguments> testDeliveryDataListForNegativeTestTrowsFragileCargoException() {
        return Stream.of(
                arguments(30.01, CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL),
                arguments(TestUtils.getIncreaseDistance(LONG_DISTANCE),
                        CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL),
                arguments(TestUtils.getRandomDistanceFromRange(LONG_DISTANCE, Double.MAX_EXPONENT),
                        CargoDimension.BIG, true, DeliveryServiceLoad.NORMAL)
        );
    }
}