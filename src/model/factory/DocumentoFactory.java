package model.factory;

public class DocumentoFactory {
    public static String getTipoDoc(boolean esFactura){
        return esFactura ? "FACTURA" : "BOLETA";
    }
}
