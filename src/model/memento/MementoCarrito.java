package model.memento;

import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Memento: Almacena el estado del carrito de compras
 */
public class MementoCarrito {
    
    private final List<VentaDetalleDTO> detalles;
    private final String fechaCreacion;
    
    public MementoCarrito(List<VentaDetalleDTO> detalles) {
        // Crear copia profunda para evitar modificaciones externas
        this.detalles = new ArrayList<>();
        for (VentaDetalleDTO detalle : detalles) {
            VentaDetalleDTO copia = new VentaDetalleDTO(
                detalle.getId(),
                detalle.getVentaId(),
                detalle.getProductoId(),
                detalle.getCantidad(),
                detalle.getPrecio(),
                detalle.getImporte()
            );
            this.detalles.add(copia);
        }
        this.fechaCreacion = java.time.LocalDateTime.now().toString();
    }
    
    /**
     * Restaura el estado guardado
     */
    public List<VentaDetalleDTO> getEstado() {
        // Retornar copia para mantener inmutabilidad
        List<VentaDetalleDTO> copia = new ArrayList<>();
        for (VentaDetalleDTO detalle : detalles) {
            VentaDetalleDTO nuevo = new VentaDetalleDTO(
                detalle.getId(),
                detalle.getVentaId(),
                detalle.getProductoId(),
                detalle.getCantidad(),
                detalle.getPrecio(),
                detalle.getImporte()
            );
            copia.add(nuevo);
        }
        return copia;
    }
    
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    
    public int getCantidadItems() {
        return detalles.size();
    }
}


