package model.builder;

import java.util.*;
import model.dto.*;

public class FacturaBuilder {
    private VentaDTO venta;
    private List<VentaDetalleDTO> detalles = new ArrayList<>();

    public FacturaBuilder setVenta(VentaDTO v){ this.venta=v; return this; }
    public FacturaBuilder addDetalle(VentaDetalleDTO d){ detalles.add(d); return this; }

    public Factura build(){
        return new Factura(venta, detalles);
    }
}

