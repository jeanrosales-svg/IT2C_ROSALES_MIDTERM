package com.rosales.model;

/**
 * Pharmacy medicine record (5 attributes).
 * For a new record that has not been saved yet, pass id = 0;
 * the database assigns the real ID automatically (starting from 1).
 */
public record ScenarioEntity(
        int id,
        String brandName,
        String category,
        double price,
        int stock
) {
}
