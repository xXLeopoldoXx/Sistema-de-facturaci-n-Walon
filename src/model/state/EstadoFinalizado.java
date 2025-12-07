package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado finalizado: Venta completada y registrada
 */
public class EstadoFinalizado implements EstadoVenta {
    
    private List<VentaDetalleDTO> detalles;
    
    public EstadoFinalizado(ContextoVenta contexto, List<VentaDetalleDTO> detalles) {
        this.detalles = new ArrayList<>(detalles);
    }
    
    @Override
    public void agregarProducto(ProductoDTO producto, int cantidad, double precio) {
        throw new IllegalStateException("No se pueden agregar productos a una venta finalizada");
    }
    
    @Override
    public void eliminarProducto(int indice) {
        throw new IllegalStateException("No se pueden eliminar productos de una venta finalizada");
    }
    
    @Override
    public void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery) {
        throw new IllegalStateException("La venta ya está finalizada");
    }
    
    @Override
    public void cancelarVenta() {
        throw new IllegalStateException("No se puede cancelar una venta finalizada");
    }
    
    @Override
    public String getNombreEstado() {
        return "FINALIZADO";
    }
    
    @Override
    public List<VentaDetalleDTO> getDetalles() {
        return new ArrayList<>(detalles);
    }
}

