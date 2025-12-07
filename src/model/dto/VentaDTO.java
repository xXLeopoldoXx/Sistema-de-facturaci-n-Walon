package model.dto;

import java.time.LocalDateTime;

public class VentaDTO {
    private int id;
    private LocalDateTime fecha;
    private int clienteId;
    private String tipoDoc;     // BOLETA / FACTURA
    private String tipoVenta;   // MENOR / MAYOR
    private boolean delivery;
    private double subtotal;
    private double igv;
    private double total;

    public VentaDTO() {
        this.fecha = LocalDateTime.now();
    }

    public VentaDTO(int id, LocalDateTime fecha, int clienteId, String tipoDoc,
                    String tipoVenta, boolean delivery,
                    double subtotal, double igv, double total) {
        this.id = id;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.tipoDoc = tipoDoc;
        this.tipoVenta = tipoVenta;
        this.delivery = delivery;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    public VentaDTO(int clienteId, String tipoDoc, String tipoVenta, boolean delivery,
                    double subtotal, double igv, double total) {
        this.fecha = LocalDateTime.now();
        this.clienteId = clienteId;
        this.tipoDoc = tipoDoc;
        this.tipoVenta = tipoVenta;
        this.delivery = delivery;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "VentaDTO{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", clienteId=" + clienteId +
                ", tipoDoc='" + tipoDoc + '\'' +
                ", tipoVenta='" + tipoVenta + '\'' +
                ", delivery=" + delivery +
                ", subtotal=" + subtotal +
                ", igv=" + igv +
                ", total=" + total +
                '}';
    }
}

