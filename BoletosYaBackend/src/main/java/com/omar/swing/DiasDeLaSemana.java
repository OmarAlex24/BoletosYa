package com.omar.swing;

import java.time.DayOfWeek;

public enum DiasDeLaSemana {
    DOMINGO("SUNDAY"),
    LUNES("MONDAY"),
    MARTES("TUESDAY"),
    MIERCOLES("WEDNESDAY"),
    JUEVES("THURSDAY"),
    VIERNES("FRIDAY"),
    SABADO("SATURDAY");

    private final String englishDay;

    DiasDeLaSemana(String englishDay) {
        this.englishDay = englishDay;
    }

    public static DiasDeLaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case DayOfWeek.MONDAY -> LUNES;
            case DayOfWeek.TUESDAY -> MARTES;
            case DayOfWeek.WEDNESDAY -> MIERCOLES;
            case DayOfWeek.THURSDAY -> JUEVES;
            case DayOfWeek.FRIDAY -> VIERNES;
            case DayOfWeek.SATURDAY -> SABADO;
            case DayOfWeek.SUNDAY -> DOMINGO;
            default -> throw new IllegalArgumentException("Día no válido: " + dayOfWeek);
        };
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
