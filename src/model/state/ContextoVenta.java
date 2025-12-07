package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.List;

/**
 * Contexto que mantiene el estado actual de la venta
 */
public class ContextoVenta {
    
    private EstadoVenta estadoActual;
    
    public ContextoVenta() {
        this.estadoActual = new EstadoInicial(this);
    }
    
    public void cambiarEstado(EstadoVenta nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
    
    public EstadoVenta getEstadoActual() {
        return estadoActual;
    }
    
    public void agregarProducto(ProductoDTO producto, int cantidad, double precio) {
        estadoActual.agregarProducto(producto, cantidad, precio);
    }
    
    public void eliminarProducto(int indice) {
        estadoActual.eliminarProducto(indice);
    }
    
    public void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery) {
        estadoActual.finalizarVenta(cliente, tipoDoc, tipoVenta, delivery);
    }
    
    public void cancelarVenta() {
        estadoActual.cancelarVenta();
    }
    
    public String getNombreEstado() {
        return estadoActual.getNombreEstado();
    }
    
    public List<VentaDetalleDTO> getDetalles() {
        return estadoActual.getDetalles();
    }
}


