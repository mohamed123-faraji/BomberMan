package fr.malibu.assistants.jws.utils.types;

public enum PositionENum {
    WOOD("W"),
    BOMB("B"), METAL("M"), FLOOR("F");

    private String value;

    PositionENum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
