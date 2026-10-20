package com.rosales;

import com.rosales.dao.ScenarioDao;
import com.rosales.ui.ScenarioMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ScenarioDao dao = new ScenarioDao();
        try {
            dao.initialize();
        } catch (SQLException e) {
            System.out.println("Could not initialize database: " + e.getMessage());
            return;
        }
        new ScenarioMenu(dao).run();
    }
}
