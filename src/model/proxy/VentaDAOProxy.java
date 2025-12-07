package model.proxy;

import model.builder.Factura;
import model.dao.VentaDAO;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Patrón Proxy específico para VentaDAO con logging y validación adicional
 */
public class VentaDAOProxy {
    
    private static final Logger logger = Logger.getLogger(VentaDAOProxy.class.getName());
    private final VentaDAO ventaDAOReal;
    
    public VentaDAOProxy() {
        this.ventaDAOReal = new VentaDAO();
    }
    
    /**
     * Proxy para registrar venta con logging y validación
     */
    public int registrarVenta(Factura factura) {
        logger.log(Level.INFO, "Proxy: Iniciando registro de venta");
        
        // Validación previa
        if (factura == null || factura.getVenta() == null) {
            logger.log(Level.SEVERE, "Proxy: Factura inválida");
            return 0;
        }
        
        if (factura.getDetalles() == null || factura.getDetalles().isEmpty()) {
            logger.log(Level.SEVERE, "Proxy: Factura sin detalles");
            return 0;
        }
        
        logger.log(Level.INFO, "Proxy: Factura válida con {0} detalles", 
                   factura.getDetalles().size());
        
        long inicio = System.currentTimeMillis();
        int ventaId = ventaDAOReal.registrarVenta(factura);
        long tiempo = System.currentTimeMillis() - inicio;
        
        if (ventaId > 0) {
            logger.log(Level.INFO, "Proxy: Venta registrada exitosamente con ID: {0} en {1}ms", 
                       new Object[]{ventaId, tiempo});
        } else {
            logger.log(Level.SEVERE, "Proxy: Error al registrar venta");
        }
        
        return ventaId;
    }
}


