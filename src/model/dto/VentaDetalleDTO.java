package model.dto;

public class VentaDetalleDTO {
    private int id;
    private int ventaId;
    private int productoId;
    private int cantidad;
    private double precio;
    private double importe;

    public VentaDetalleDTO() {
    }

    public VentaDetalleDTO(int id, int ventaId, int productoId, int cantidad,
                           double precio, double importe) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.importe = importe;
    }

    public VentaDetalleDTO(int ventaId, int productoId, int cantidad,
                           double precio, double importe) {
        this.ventaId = ventaId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "VentaDetalleDTO{" +
                "id=" + id +
                ", ventaId=" + ventaId +
                ", productoId=" + productoId +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", importe=" + importe +
                '}';
    }
}
