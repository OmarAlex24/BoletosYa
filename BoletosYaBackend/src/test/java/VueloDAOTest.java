import com.omar.DAO.AerolineaDAO;
import com.omar.DAO.AeropuertoDAO;
import com.omar.DAO.VueloDAO;
import com.omar.entity.*;
import com.omar.service.AerolineaService;
import com.omar.service.AeropuertoService;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VueloDAOTest {
    private static VueloDAO vueloDAO;
    private static Vuelo vueloTest;
    private static Aerolinea aerolineaTest;
    private static Aeropuerto aeropuertoOrigenTest;
    private static Aeropuerto aeropuertoDestinoTest;
    private static AeropuertoService aeropuertoService;
    private static AerolineaService aerolineaService;

    @BeforeAll
    public static void setUp() throws Exception {
        aeropuertoService = new AeropuertoService(new AeropuertoDAO());
        aerolineaService = new AerolineaService(new AerolineaDAO());
        vueloDAO = new VueloDAO();

        // Configurar datos de prueba
        aerolineaTest = new Aerolinea();
        aerolineaTest.setNombre("Aerolínea Test");
        aerolineaTest.setCodigo("TEST");

        Aerolinea aerolineaAgregada = aerolineaService.crear(aerolineaTest);
        aerolineaTest.setId(aerolineaAgregada.getId());

        aeropuertoOrigenTest = new Aeropuerto();
        aeropuertoOrigenTest.setCodigo("TSO");
        aeropuertoOrigenTest.setNombre("TEST AEROPUERTO ORIGEN");
        aeropuertoOrigenTest.setCiudad("TEST CIUDAD ORIGEN");
        aeropuertoOrigenTest.setPais("TEST PAIS ORIGEN");

        aeropuertoDestinoTest = new Aeropuerto();
        aeropuertoDestinoTest.setCodigo("TSD");
        aeropuertoDestinoTest.setNombre("TEST AEROPUERTO DESTINO");
        aeropuertoDestinoTest.setCiudad("TEST CIUDAD DESTINO");
        aeropuertoDestinoTest.setPais("TEST PAIS DESTINO");

        Aeropuerto aeropuertoOrigenAgregado = aeropuertoService.crear(aeropuertoOrigenTest);
        aeropuertoOrigenTest.setId(aeropuertoOrigenAgregado.getId());

        Aeropuerto aeropuertoDestinoAgregado = aeropuertoService.crear(aeropuertoDestinoTest);
        aeropuertoDestinoTest.setId(aeropuertoDestinoAgregado.getId());

        vueloTest = new Vuelo();
        vueloTest.setAerolinea(aerolineaTest);
        vueloTest.setOrigen(aeropuertoOrigenTest);
        vueloTest.setDestino(aeropuertoDestinoTest);
        vueloTest.setFechaSalida(LocalDateTime.now());
        vueloTest.setFechaLlegada(LocalDateTime.now().plusHours(2));
        vueloTest.setPrecio(1500.0);
    }

    @BeforeEach
    public void initialize() {
        Vuelo vueloAgregado = vueloDAO.agregar(vueloTest);
        vueloTest.setId(vueloAgregado.getId());
    }

    @Test
    public void testAgregar() {
        Vuelo vueloAgregado = vueloDAO.obtenerPorId(vueloTest.getId());

        assertNotNull(vueloAgregado);
        assertNotNull(vueloAgregado.getId());
    }

    @Test
    public void testObtenerPorId() {
        Vuelo vueloObtenido = vueloDAO.obtenerPorId(vueloTest.getId());

        assertNotNull(vueloObtenido);
        assertEquals(vueloTest.getId(), vueloObtenido.getId());
    }

    @Test
    public void testModificar() {
        Vuelo vueloAgregado = vueloDAO.obtenerPorId(vueloTest.getId());
        double nuevoPrecio = 2000.0;
        vueloAgregado.setPrecio(nuevoPrecio);

        vueloDAO.modificar(vueloAgregado);

        Vuelo vueloModificado = vueloDAO.obtenerPorId(vueloAgregado.getId());
        assertEquals(nuevoPrecio, vueloModificado.getPrecio());
    }

    @Test
    public void testEliminar() {
        Vuelo vueloAgregado = vueloDAO.obtenerPorId(vueloTest.getId());
        vueloDAO.eliminar(vueloAgregado);

        Vuelo vueloEliminado = vueloDAO.obtenerPorId(vueloAgregado.getId());
        assertNull(vueloEliminado);
    }

    @Test
    public void testEliminarPorId() {
        Vuelo vueloAgregado = vueloDAO.obtenerPorId(vueloTest.getId());
        vueloDAO.eliminarPorId(vueloAgregado.getId());

        Vuelo vueloEliminado = vueloDAO.obtenerPorId(vueloAgregado.getId());
        assertNull(vueloEliminado);
    }

    @Test
    public void testBuscarVuelosDirectos() {
        LocalDateTime fecha = vueloTest.getFechaSalida();

        List<Vuelo> vuelosEncontrados = vueloDAO.buscarVuelosDirectos(
                aeropuertoOrigenTest.getCodigo(),
                aeropuertoDestinoTest.getCodigo(),
                fecha
        );

        assertNotNull(vuelosEncontrados);
        assertFalse(vuelosEncontrados.isEmpty());
    }

    @Test
    public void testBuscarVuelosDirectosSinResultados() {
        List<Vuelo> vuelosEncontrados = vueloDAO.buscarVuelosDirectos(
                "XXX",
                "YYY",
                LocalDateTime.now()
        );

        assertNotNull(vuelosEncontrados);
        assertTrue(vuelosEncontrados.isEmpty());
    }

    @AfterEach
    public void limpiarDespuesDeCadaTest() {
        vueloDAO.eliminarPorId(vueloTest.getId());
    }

    @AfterAll
    public static void limpiar() throws Exception {
        aerolineaService.eliminarPorId(aerolineaTest.getId());
        aeropuertoService.eliminarPorId(aeropuertoOrigenTest.getId());
        aeropuertoService.eliminarPorId(aeropuertoDestinoTest.getId());
    }
}