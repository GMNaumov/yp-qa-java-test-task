package models;

/**
 * Класс, инкапсулирующий свойства доставляемого груза
 */
public class Cargo {
    CargoDimension dimension;
    boolean isFragile;

    public Cargo(CargoDimension dimension, boolean isFragile) {
        this.dimension = dimension;
        this.isFragile = isFragile;
    }

    public CargoDimension getDimension() {
        return dimension;
    }

    public boolean isFragile() {
        return isFragile;
    }
}
