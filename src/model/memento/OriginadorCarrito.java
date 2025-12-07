package model.memento;

import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Memento: Originador que crea y restaura mementos
 */
public class OriginadorCarrito {
    
    private List<VentaDetalleDTO> detalles;
    
    public OriginadorCarrito() {
        this.detalles = new ArrayList<>();
    }
    
    /**
     * Agrega un detalle al carrito
     */
    public void agregarDetalle(VentaDetalleDTO detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        detalles.add(detalle);
    }
    
    /**
     * Elimina un detalle del carrito
     */
    public void eliminarDetalle(int indice) {
        if (detalles != null && indice >= 0 && indice < detalles.size()) {
            detalles.remove(indice);
        }
    }
    
    /**
     * Limpia todos los detalles
     */
    public void limpiar() {
        if (detalles != null) {
            detalles.clear();
        }
    }
    
    /**
     * Crea un memento con el estado actual
     */
    public MementoCarrito crearMemento() {
        return new MementoCarrito(detalles);
    }
    
    /**
     * Restaura el estado desde un memento
     */
    public void restaurarDesdeMemento(MementoCarrito memento) {
        if (memento != null) {
            this.detalles = memento.getEstado();
        }
    }
    
    /**
     * Obtiene el estado actual
     */
    public List<VentaDetalleDTO> getDetalles() {
        return new ArrayList<>(detalles);
    }
    
    /**
     * Establece los detalles directamente
     */
    public void setDetalles(List<VentaDetalleDTO> detalles) {
        this.detalles = new ArrayList<>(detalles);
    }
}


