package com.omar.service;

import com.omar.DAO.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;

@Getter
@Setter
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

    public AerolineaService getAerolineaService() throws SQLException {
        if (aerolineaService == null) {
            aerolineaService = new AerolineaService(daoFactory.getAerolineaDAO());
        }
        return aerolineaService;
    }

    public AeropuertoService getAeropuertoService() throws SQLException {
        if (aeropuertoService == null) {
            aeropuertoService = new AeropuertoService(daoFactory.getAeropuertoDAO());
        }
        return aeropuertoService;
    }

    public AsientoService getAsientoService() throws SQLException {
        if (asientoService == null) {
            asientoService = new AsientoService(daoFactory.getAsientoDAO());
        }
        return asientoService;
    }

    public ClienteService getClienteService() throws SQLException {
        if (clienteService == null) {
            clienteService = new ClienteService(daoFactory.getClienteDAO());
        }
        return clienteService;
    }

    public ReservacionService getReservacionService() throws Exception {
        if (reservacionService == null) {
            reservacionService = new ReservacionService(daoFactory.getReservacionDAO());
        }
        return reservacionService;
    }

    public VueloService getVueloService() throws SQLException {
        if (vueloService == null) {
            vueloService = new VueloService(daoFactory.getVueloDAO());
        }
        return vueloService;
    }

    public static void setInstance(ServiceFactory instance) {
        ServiceFactory.instance = instance;
    }


}
