package model.facade;

import model.builder.Factura;
import model.builder.FacturaBuilder;
import model.dao.ClienteDAO;
import model.dao.ProductoDAO;
import model.dao.VentaDAO;
import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDTO;
import model.dto.VentaDetalleDTO;

import java.util.List;

/**
 * Patrón Facade: Proporciona una interfaz simplificada para el subsistema de ventas.
 * Encapsula la complejidad de múltiples DAOs y operaciones relacionadas.
 */
public class VentaService {
    
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    
    public VentaService() {
        this.clienteDAO = new ClienteDAO();
        this.productoDAO = new ProductoDAO();
        this.ventaDAO = new VentaDAO();
    }
    
    /**
     * Busca un cliente por DNI
     */
    public ClienteDTO buscarCliente(String dni) {
        return clienteDAO.buscarPorDni(dni);
    }
    
    /**
     * Busca un producto por código
     */
    public ProductoDTO buscarProducto(String codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }
    
    /**
     * Calcula el precio según el tipo de venta (menor/mayor)
     */
    public double calcularPrecio(ProductoDTO producto, boolean esMayor) {
        return esMayor ? producto.getPrecioMayor() : producto.getPrecioUnit();
    }
    
    /**
     * Valida si hay stock suficiente
     */
    public boolean validarStock(ProductoDTO producto, int cantidad) {
        return producto != null && producto.getStock() >= cantidad && cantidad > 0;
    }
    
    /**
     * Calcula los totales de una venta
     */
    public TotalesVenta calcularTotales(List<VentaDetalleDTO> detalles) {
        double subtotal = 0.0;
        for (VentaDetalleDTO detalle : detalles) {
            subtotal += detalle.getImporte();
        }
        double igv = subtotal * 0.18;
        double total = subtotal + igv;
        return new TotalesVenta(subtotal, igv, total);
    }
    
    /**
     * Construye una factura completa usando el Builder
     */
    public Factura construirFactura(VentaDTO venta, List<VentaDetalleDTO> detalles) {
        FacturaBuilder builder = new FacturaBuilder();
        builder.setVenta(venta);
        for (VentaDetalleDTO detalle : detalles) {
            builder.addDetalle(detalle);
        }
        return builder.build();
    }
    
    /**
     * Registra una venta completa (Facade principal)
     */
    public int registrarVentaCompleta(
            int clienteId,
            String tipoDoc,
            String tipoVenta,
            boolean delivery,
            List<VentaDetalleDTO> detalles) {
        
        TotalesVenta totales = calcularTotales(detalles);
        
        VentaDTO venta = new VentaDTO(
            clienteId,
            tipoDoc,
            tipoVenta,
            delivery,
            totales.subtotal,
            totales.igv,
            totales.total
        );
        
        Factura factura = construirFactura(venta, detalles);
        
        return ventaDAO.registrarVenta(factura);
    }
    
    /**
     * Clase interna para encapsular los totales
     */
    public static class TotalesVenta {
        public final double subtotal;
        public final double igv;
        public final double total;
        
        public TotalesVenta(double subtotal, double igv, double total) {
            this.subtotal = subtotal;
            this.igv = igv;
            this.total = total;
        }
    }
}

