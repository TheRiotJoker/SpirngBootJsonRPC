package de.theriotjoker.datamanager.utils;

public enum OperationEnum {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, FACTORIAL, POWER;


    public static OperationEnum fromString(String operation) {
        return switch (operation.toUpperCase()) {
            case "ADDITION" -> ADDITION;
            case "SUBTRACTION" -> SUBTRACTION;
            case "MULTIPLICATION" -> MULTIPLICATION;
            case "DIVISION" -> DIVISION;
            case "FACTORIAL" -> FACTORIAL;
            case "POWER" -> POWER;
            default -> throw new IllegalArgumentException("No such operation: " + operation);
        };
    }
}
