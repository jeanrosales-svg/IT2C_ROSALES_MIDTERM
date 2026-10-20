package com.rosales.dao;

import com.rosales.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object: every SQL statement in the application is here.
 */
public class ScenarioDao {

    private static final String INSERT_SQL =
            "INSERT INTO medicine (brand_name, category, price, stock) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, brand_name, category, price, stock FROM medicine ORDER BY id";

    /** Makes sure the table exists. Called once at startup. */
    public void initialize() throws SQLException {
        DatabaseConnection.initializeSchema();
    }

    /** Inserts a new record using a PreparedStatement. */
    public void insert(ScenarioEntity entity) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, entity.brandName());
            ps.setString(2, entity.category());
            ps.setDouble(3, entity.price());
            ps.setInt(4, entity.stock());
            ps.executeUpdate();
        }
    }

    /** Fetches every record. */
    public List<ScenarioEntity> findAll() throws SQLException {
        List<ScenarioEntity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ScenarioEntity(
                        rs.getInt("id"),
                        rs.getString("brand_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }
        return list;
    }
}
