package com.crioprecipitati.androidpervasive1718.model

enum class LifeParameters(val longName: String, val acronym: String, val id: Int) {
    SYSTOLIC_BLOOD_PRESSURE("Pressione Arteriosa Sistolica", "SYS", 1),
    DIASTOLIC_BLOOD_PRESSURE("Pressione Arteriosa Diastolica", "DIA", 2),
    HEART_RATE("Frequenza Cardiaca", "HR", 3),
    TEMPERATURE("Temperatura", "T", 4),
    OXYGEN_SATURATION("Saturazione Ossigeno", "SpO2", 5),
    END_TIDAL_CARBON_DIOXIDE("End Tidal Anidride Carbonica", "EtCO2", 6);

    object Utils {
        fun getByAcronym(acr: String) = LifeParameters.values().firstOrNull { it.acronym == acr }
        fun getByID(id: Int) = LifeParameters.values().firstOrNull { it.id == id }
        fun getByEnumName(enumName: String) = LifeParameters.values().firstOrNull { it.toString() == enumName }
    }
}