package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado con productos: Venta en proceso con productos en el carrito
 */
public class EstadoConProductos implements EstadoVenta {
    
    private ContextoVenta contexto;
    private List<VentaDetalleDTO> detalles;
    
    public EstadoConProductos(ContextoVenta contexto, List<VentaDetalleDTO> detalles) {
        this.contexto = contexto;
        this.detalles = new ArrayList<>(detalles);
    }
    
    @Override
    public void agregarProducto(ProductoDTO producto, int cantidad, double precio) {
        if (producto == null) {
            throw new IllegalStateException("Producto no válido");
        }
        
        double importe = cantidad * precio;
        VentaDetalleDTO detalle = new VentaDetalleDTO(
            0, producto.getId(), cantidad, precio, importe
        );
        detalles.add(detalle);
    }
    
    @Override
    public void eliminarProducto(int indice) {
        if (indice < 0 || indice >= detalles.size()) {
            throw new IllegalArgumentException("Índice inválido");
        }
        
        detalles.remove(indice);
        
        // Si no quedan productos, volver a estado inicial
        if (detalles.isEmpty()) {
            contexto.cambiarEstado(new EstadoInicial(contexto));
        }
    }
    
    @Override
    public void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery) {
        if (cliente == null) {
            throw new IllegalStateException("Debe seleccionar un cliente");
        }
        
        if (detalles.isEmpty()) {
            throw new IllegalStateException("No hay productos en la venta");
        }
        
        // Cambiar a estado finalizado
        contexto.cambiarEstado(new EstadoFinalizado(contexto, detalles));
    }
    
    @Override
    public void cancelarVenta() {
        detalles.clear();
        contexto.cambiarEstado(new EstadoCancelado(contexto));
    }
    
    @Override
    public String getNombreEstado() {
        return "CON_PRODUCTOS";
    }
    
    @Override
    public List<VentaDetalleDTO> getDetalles() {
        return new ArrayList<>(detalles);
    }
}


