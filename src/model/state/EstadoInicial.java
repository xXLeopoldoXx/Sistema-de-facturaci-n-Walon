package model.state;

import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDetalleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Estado inicial: Venta recién iniciada, sin productos
 */
public class EstadoInicial implements EstadoVenta {
    
    private ContextoVenta contexto;
    private List<VentaDetalleDTO> detalles;
    
    public EstadoInicial(ContextoVenta contexto) {
        this.contexto = contexto;
        this.detalles = new ArrayList<>();
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
        
        // Cambiar a estado con productos
        contexto.cambiarEstado(new EstadoConProductos(contexto, detalles));
    }
    
    @Override
    public void eliminarProducto(int indice) {
        throw new IllegalStateException("No hay productos para eliminar");
    }
    
    @Override
    public void finalizarVenta(ClienteDTO cliente, String tipoDoc, String tipoVenta, boolean delivery) {
        throw new IllegalStateException("No se puede finalizar una venta sin productos");
    }
    
    @Override
    public void cancelarVenta() {
        detalles.clear();
        contexto.cambiarEstado(new EstadoCancelado(contexto));
    }
    
    @Override
    public String getNombreEstado() {
        return "INICIAL";
    }
    
    @Override
    public List<VentaDetalleDTO> getDetalles() {
        return new ArrayList<>(detalles);
    }
}


