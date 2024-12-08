package com.omar.swing;

import java.time.Month;

public enum MesDelAño {
    ENERO("Enero"),
    FEBRERO("Febrero"),
    MARZO("Marzo"),
    ABRIL("Abril"),
    MAYO("Mayo"),
    JUNIO("Junio"),
    JULIO("Julio"),
    AGOSTO("Agosto"),
    SEPTIEMBRE("Septiembre"),
    OCTUBRE("Octubre"),
    NOVIEMBRE("Noviembre"),
    DICIEMBRE("Diciembre");

    private final String englishMonth;

    MesDelAño(String englishMonth) {
        this.englishMonth = englishMonth;
    }

    public static MesDelAño fromMonth(int month) {
        return switch (month) {
            case 1 -> ENERO;
            case 2 -> FEBRERO;
            case 3 -> MARZO;
            case 4 -> ABRIL;
            case 5 -> MAYO;
            case 6 -> JUNIO;
            case 7 -> JULIO;
            case 8 -> AGOSTO;
            case 9 -> SEPTIEMBRE;
            case 10 -> OCTUBRE;
            case 11 -> NOVIEMBRE;
            case 12 -> DICIEMBRE;
            default -> throw new IllegalArgumentException("Mes no válido: " + month);
        };
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
