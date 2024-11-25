package com.omar.dao;

import com.omar.BD;
import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {
    private static DAOFactory instance;
    private final Connection connection;

    private AerolineaDAO aerolineaDAO;
    private AeropuertoDAO aeropuertoDAO;
    private AsientoDAO asientoDAO;
    private ClienteDAO clienteDAO;
    private ReservacionDAO reservacionDAO;
    private VueloDAO vueloDAO;

    private DAOFactory() throws SQLException {
        this.connection = BD.getInstance().getConnection();
    }

    public static DAOFactory getInstance() throws SQLException {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public AerolineaDAO getAerolineaDAO() {
        if (aerolineaDAO == null) {
            aerolineaDAO = new AerolineaDAO(connection);
        }
        return aerolineaDAO;
    }
}
