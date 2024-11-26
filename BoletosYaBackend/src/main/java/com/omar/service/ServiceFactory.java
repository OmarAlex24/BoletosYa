package com.omar.service;

import com.omar.dao.DAOFactory;
import java.sql.SQLException;

public class ServiceFactory {
    private static ServiceFactory instance;
    private final DAOFactory daoFactory;

    private AerolineaService aerolineaService;
    private AeropuertoService aeropuertoService;
    private AsientoService asientoService;
    private ClienteService clienteService;
    private ReservacionService reservacionService;
    private VueloService vueloService;

    private ServiceFactory() throws SQLException {
        this.daoFactory = DAOFactory.getInstance();
    }

    public static ServiceFactory getInstance() throws SQLException {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public AerolineaService getAerolineaService() {
        if (aerolineaService == null) {
            aerolineaService = new AerolineaService(daoFactory.getAerolineaDAO());
        }
        return aerolineaService;
    }
}
