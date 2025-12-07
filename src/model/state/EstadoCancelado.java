package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado cancelado: Venta cancelada
 */
public class EstadoCancelado implements EstadoVenta {
    
    public EstadoCancelado(ContextoVenta contexto) {
        // Estado cancelado no requiere contexto
    }
    
    @Override
    public void agregarProducto(ProductoDTO producto, int cantidad, double precio) {
        throw new IllegalStateException("No se pueden agregar productos a una venta cancelada");
    }
    
    @Override
    public void eliminarProducto(int indice) {
        throw new IllegalStateException("No se pueden eliminar productos de una venta cancelada");
    }
    
    @Override
    public void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery) {
        throw new IllegalStateException("No se puede finalizar una venta cancelada");
    }
    
    @Override
    public void cancelarVenta() {
        // Ya está cancelada, no hacer nada
    }
    
    @Override
    public String getNombreEstado() {
        return "CANCELADO";
    }
    
    @Override
    public List<VentaDetalleDTO> getDetalles() {
        return new ArrayList<>();
    }
}

