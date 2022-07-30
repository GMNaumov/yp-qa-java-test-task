package estimate;

import exceptions.NegativeOrZeroDistanceException;
import exceptions.TooLongDistanceForFragileCargoException;
import models.Cargo;
import models.CargoDimension;
import models.DeliveryServiceLoad;

/**
 * Класс предоставляющий методы для расчёта стоимости доставки
 */
public class CargoDeliveryCostEstimate {
    // Базовая стоимость доставки
    private static final double BASE_DELIVERY_COST = 400.0;
    // Ранжирование расстояния доставки
    private static final double SHORT_DISTANCE = 2.0;
    private static final double MIDDLE_DISTANCE = 10.0;
    private static final double LONG_DISTANCE = 30.0;
    // Доп. плата за расстояние доставки
    private static final double SHORT_DISTANCE_FEE = 50.0;
    private static final double MIDDLE_DISTANCE_FEE = 100.0;
    private static final double LONG_DISTANCE_FEE = 200.0;
    private static final double VERY_LONG_DISTANCE_FEE = 300.0;
    // Доп. плата за размер груза
    private static final double BIG_DIMENSION_FEE = 200.0;
    private static final double SMALL_DIMENSION_FEE = 100.0;
    // Доп. плата за хрупкость груза
    private static final double CARGO_FRAGILE_FEE = 300.0;
    // Коэффициенты загруженности службы доставки
    private static final double DELIVERY_SERVICE_NORMAL_LOAD_FEE = 1.0;
    private static final double DELIVERY_SERVICE_INCREASED_LOAD_FEE = 1.2;
    private static final double DELIVERY_SERVICE_HIGH_LOAD_FEE = 1.4;
    private static final double DELIVERY_SERVICE_HEAVY_LOAD_FEE = 1.6;


    /**
     * Расчёт полной стомости доставки груза
     *
     * @param deliveryDistance    Расстояние доставки
     * @param cargoDimension      Размер груза
     * @param isCargoFragile      Признак хрупкости груза
     * @param deliveryServiceLoad Статус загрузки службы доставки
     * @return стоимость доставки груза
     * @throws TooLongDistanceForFragileCargoException если производится попытка расчёта доставки хрупкого груза
     *                                                 на расстояние более 30 км.
     * @throws NegativeOrZeroDistanceException         если производится попыта расчёта доставки при нулевом
     *                                                 или отрицательном расстоянии
     */
    public double getDeliveryFullCost(double deliveryDistance,
                                      CargoDimension cargoDimension,
                                      boolean isCargoFragile,
                                      DeliveryServiceLoad deliveryServiceLoad)
            throws TooLongDistanceForFragileCargoException, NegativeOrZeroDistanceException {
        Cargo cargo = new Cargo(cargoDimension, isCargoFragile);
        return getDeliveryFullCost(cargo, deliveryDistance, deliveryServiceLoad);
    }

    private double getDeliveryFullCost(Cargo cargo,
                                       double deliveryDistance,
                                       DeliveryServiceLoad deliveryServiceLoad)
            throws NegativeOrZeroDistanceException, TooLongDistanceForFragileCargoException {
        if (deliveryDistance <= 0.0) {
            throw new NegativeOrZeroDistanceException();
        } else if (cargo.isFragile() && deliveryDistance > LONG_DISTANCE) {
            throw new TooLongDistanceForFragileCargoException();
        } else {
            double cargoDimensionFee = getCargoDimensionFee(cargo);
            double distanceFee = getDistanceFee(deliveryDistance);
            double deliveryServiceLoadFee = getDeliveryServiceLoadMultiplier(deliveryServiceLoad);
            double fragileFee = getCargoFragileFee(cargo);
            double deliveryCost = (distanceFee + cargoDimensionFee + fragileFee) * deliveryServiceLoadFee;
            return Math.max(deliveryCost, BASE_DELIVERY_COST);
        }
    }

    /**
     * Получение добавочной стомости доставки в зависимости от расстояния
     *
     * @param distance расстояние доставки
     * @return добавочная стоимость доставки
     */
    private double getDistanceFee(double distance) {
        if (distance > LONG_DISTANCE) {
            return VERY_LONG_DISTANCE_FEE;
        } else if (distance > MIDDLE_DISTANCE) {
            return LONG_DISTANCE_FEE;
        } else if (distance > SHORT_DISTANCE) {
            return MIDDLE_DISTANCE_FEE;
        } else {
            return SHORT_DISTANCE_FEE;
        }
    }

    /**
     * Получение добавочной стомости доставки в зависимости от размера груза
     *
     * @return добавочная стомость доставки
     */
    private double getCargoDimensionFee(Cargo cargo) {
        if (cargo.getDimension() == CargoDimension.BIG) {
            return BIG_DIMENSION_FEE;
        } else {
            return SMALL_DIMENSION_FEE;
        }
    }

    /**
     * Получение добавочной стомости доставки в зависимости от хрупкости груза
     *
     * @return добавочная стомость доставки
     */
    private double getCargoFragileFee(Cargo cargo) {
        return cargo.isFragile() ? CARGO_FRAGILE_FEE : 0.0;
    }

    /**
     * Получение коэффициента увеличения стомости доставки в зависимости от загруженности службы доставки
     *
     * @return коэффициент увеличения стоимости доставки
     */
    private double getDeliveryServiceLoadMultiplier(DeliveryServiceLoad deliveryServiceLoad) {
        switch (deliveryServiceLoad) {
            case NORMAL:
                return DELIVERY_SERVICE_NORMAL_LOAD_FEE;
            case INCREASED:
                return DELIVERY_SERVICE_INCREASED_LOAD_FEE;
            case HIGH:
                return DELIVERY_SERVICE_HIGH_LOAD_FEE;
            case HEAVY:
                return DELIVERY_SERVICE_HEAVY_LOAD_FEE;
        }
        return DELIVERY_SERVICE_NORMAL_LOAD_FEE;
    }
}
