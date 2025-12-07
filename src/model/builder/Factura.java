package model.builder;

import java.util.List;
import model.dto.VentaDTO;
import model.dto.VentaDetalleDTO;

public class Factura {
    private VentaDTO venta;
    private List<VentaDetalleDTO> detalles;

    public Factura(VentaDTO venta, List<VentaDetalleDTO> detalles){
        this.venta = venta;
        this.detalles = detalles;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public List<VentaDetalleDTO> getDetalles() {
        return detalles;
    }
}


