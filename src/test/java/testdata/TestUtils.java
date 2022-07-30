package testdata;

import java.util.Random;

public class TestUtils {
    private static Random random;

    private TestUtils() {

    }

    /**
     * Получение увеличенного расстояния доставки
     *
     * @param distance базовое расстояние доставки
     * @return расстояние доставки, увеличенное на случайное значение от 1.01 до 1.99
     */
    public static double getIncreaseDistance(double distance) {
        random = new Random();
        double multiplier = random.nextDouble();
        return multiplier != 0.0 ? distance * (multiplier + 1) : distance * 1.01;
    }

    /**
     * Получение уменьшенного расстояния доставки
     *
     * @param distance базовое расстояние доставки
     * @return расстояние доставки, уменьшенное на случайное значение от 0.01 до 0.99
     */
    public static double getDecreaseDistance(double distance) {
        random = new Random();
        double multiplier = random.nextDouble();
        return multiplier != 0.0 ? distance * multiplier : distance * 0.01;
    }

    /**
     * Получение случайного значения расстояния доставки
     *
     * @param minDistance минимальное расстояние доставки (исключая значение)
     * @param maxDistance максимальное расстояние доставки (включая значение)
     * @return случайное значение расстояния доставки в выбранном диапазоне
     */
    public static double getRandomDistanceFromRange(double minDistance, double maxDistance) {
        random = new Random();
        return random.nextDouble() * (maxDistance - minDistance) + minDistance;
    }
}
