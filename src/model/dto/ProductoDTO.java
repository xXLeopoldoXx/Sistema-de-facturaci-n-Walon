package model.dto;

public class ProductoDTO {
    private int id;
    private String codigo;
    private String descripcion;
    private int stock;
    private double precioUnit;
    private double precioMayor;
    private int proveedorId;

    public ProductoDTO() {
    }

    public ProductoDTO(int id, String codigo, String descripcion, int stock,
                       double precioUnit, double precioMayor, int proveedorId) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precioUnit = precioUnit;
        this.precioMayor = precioMayor;
        this.proveedorId = proveedorId;
    }

    public ProductoDTO(String codigo, String descripcion, int stock,
                       double precioUnit, double precioMayor, int proveedorId) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precioUnit = precioUnit;
        this.precioMayor = precioMayor;
        this.proveedorId = proveedorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public int getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", stock=" + stock +
                ", precioUnit=" + precioUnit +
                ", precioMayor=" + precioMayor +
                ", proveedorId=" + proveedorId +
                '}';
    }
}
