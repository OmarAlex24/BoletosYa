package com.omar.DAO;

import com.omar.BD;
import com.omar.DAO.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
@Setter
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

    public AerolineaDAO getAerolineaDAO() throws SQLException {
        if (aerolineaDAO == null) {
            aerolineaDAO = new AerolineaDAO();
        }
        return aerolineaDAO;
    }

    public AeropuertoDAO getAeropuertoDAO() throws SQLException {
        if (aeropuertoDAO == null) {
            aeropuertoDAO = new AeropuertoDAO();
        }
        return aeropuertoDAO;
    }

    public AsientoDAO getAsientoDAO() throws SQLException {
        if (asientoDAO == null) {
            asientoDAO = new AsientoDAO();
        }
        return asientoDAO;
    }

    public ReservacionDAO getReservacionDAO() throws SQLException {
        if (reservacionDAO == null) {
            reservacionDAO = new ReservacionDAO();
        }
        return reservacionDAO;
    }

    public VueloDAO getVueloDAO() throws SQLException {
        if (vueloDAO == null) {
            vueloDAO = new VueloDAO();
        }
        return vueloDAO;
    }

    public ClienteDAO getClienteDAO() throws SQLException {
        if (clienteDAO == null) {
            clienteDAO = new ClienteDAO();
        }
        return clienteDAO;
    }

    public static void setInstance(DAOFactory instance) {
        DAOFactory.instance = instance;
    }

}
