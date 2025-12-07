package model.dao;

import model.builder.Factura;

/**
 * Interface Segregation Principle: Interfaz específica para operaciones de Venta
 */
public interface IVentaDAO {
    
    /**
     * Registra una venta completa con sus detalles
     */
    int registrarVenta(Factura factura);
}


