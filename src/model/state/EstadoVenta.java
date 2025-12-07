package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.List;

/**
 * Patrón State: Interfaz para los diferentes estados del proceso de venta
 */
public interface EstadoVenta {
    
    /**
     * Intenta agregar un producto al carrito
     */
    void agregarProducto(ProductoDTO producto, int cantidad, double precio);
    
    /**
     * Intenta eliminar un producto del carrito
     */
    void eliminarProducto(int indice);
    
    /**
     * Intenta finalizar la venta
     */
    void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery);
    
    /**
     * Intenta cancelar la venta
     */
    void cancelarVenta();
    
    /**
     * Obtiene el nombre del estado actual
     */
    String getNombreEstado();
    
    /**
     * Obtiene los detalles actuales del carrito
     */
    List<VentaDetalleDTO> getDetalles();
}


