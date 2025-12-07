package util;

import model.builder.Factura;
import model.dao.ProductoDAO;
import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDTO;
import model.dto.VentaDetalleDTO;

import java.io.File;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generador de Facturas en PDF (Versión Mejorada)
 * 
 * Esta versión intenta usar iText, pero si no está disponible,
 * genera un archivo HTML que puede convertirse a PDF o imprimirse.
 */
public class PdfFacturaGeneratorMejorado {
    
    // Datos de la empresa (configurables)
    private static final String EMPRESA_NOMBRE = "TIENDA WALON";
    private static final String EMPRESA_RUC = "20123456789";
    // private static final String EMPRESA_RAZON_SOCIAL = "TIENDA WALON S.A.C."; // Para uso futuro
    private static final String EMPRESA_DIRECCION = "Av. Principal 123, Lima, Perú";
    private static final String EMPRESA_TELEFONO = "01-1234567";
    private static final String EMPRESA_EMAIL = "ventas@tiendawalon.com";
    private static final String EMPRESA_WEB = "www.tiendawalon.com";
    
    private static ProductoDAO productoDAO = new ProductoDAO();
    
    /**
     * Genera un PDF de factura/boleta
     * Si iText no está disponible, genera un HTML que puede imprimirse
     */
    public static String generarFacturaPDF(Factura factura, int ventaId, ClienteDTO cliente) {
        try {
            // Intentar usar iText
            Class.forName("com.itextpdf.text.Document");
            return generarPDFConIText(factura, ventaId, cliente);
        } catch (ClassNotFoundException e) {
            // Si iText no está disponible, generar HTML
            System.out.println("iText no encontrado. Generando factura en formato HTML...");
            return generarHTMLFactura(factura, ventaId, cliente);
        } catch (Exception e) {
            System.err.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
            // Intentar generar HTML como respaldo
            try {
                return generarHTMLFactura(factura, ventaId, cliente);
            } catch (Exception ex) {
                System.err.println("Error al generar HTML: " + ex.getMessage());
                return null;
            }
        }
    }
    
    /**
     * Genera PDF usando iText (si está disponible)
     */
    private static String generarPDFConIText(Factura factura, int ventaId, ClienteDTO cliente) throws Exception {
        VentaDTO venta = factura.getVenta();
        List<VentaDetalleDTO> detalles = factura.getDetalles();
        
        // Crear directorio
        File directorioFacturas = new File("facturas");
        if (!directorioFacturas.exists()) {
            directorioFacturas.mkdirs();
        }
        
        // Nombre del archivo
        String tipoDoc = venta.getTipoDoc();
        String numeroDoc = String.format("%s-%06d", tipoDoc.substring(0, 1), ventaId);
        String nombreArchivo = numeroDoc + ".pdf";
        String rutaCompleta = directorioFacturas.getAbsolutePath() + File.separator + nombreArchivo;
        
        // Crear documento PDF
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
        java.io.FileOutputStream archivo = new java.io.FileOutputStream(rutaCompleta);
        com.itextpdf.text.pdf.PdfWriter.getInstance(documento, archivo);
        
        documento.open();
        
        // ========== ENCABEZADO ==========
        agregarEncabezadoPDF(documento, venta.getTipoDoc(), numeroDoc, venta.getFecha());
        
        // ========== DATOS DE LA EMPRESA ==========
        agregarDatosEmpresaPDF(documento);
        
        // ========== DATOS DEL CLIENTE ==========
        agregarDatosClientePDF(documento, cliente, venta.getTipoDoc());
        
        // ========== DETALLES DE PRODUCTOS ==========
        agregarTablaProductosPDF(documento, detalles);
        
        // ========== TOTALES ==========
        agregarTotalesPDF(documento, venta);
        
        // ========== PIE DE PÁGINA ==========
        agregarPiePaginaPDF(documento, venta);
        
        documento.close();
        archivo.close();
        
        return rutaCompleta;
    }
    
    /**
     * Genera factura en formato HTML (alternativa si no hay iText)
     */
    private static String generarHTMLFactura(Factura factura, int ventaId, ClienteDTO cliente) {
        try {
            VentaDTO venta = factura.getVenta();
            
            // Crear directorio
            File directorioFacturas = new File("facturas");
            if (!directorioFacturas.exists()) {
                directorioFacturas.mkdirs();
            }
            
            // Nombre del archivo
            String tipoDoc = venta.getTipoDoc();
            String numeroDoc = String.format("%s-%06d", tipoDoc.substring(0, 1), ventaId);
            String nombreArchivo = numeroDoc + ".html";
            String rutaCompleta = directorioFacturas.getAbsolutePath() + File.separator + nombreArchivo;
            
            // Generar HTML
            try (FileWriter writer = new FileWriter(rutaCompleta)) {
                writer.write(generarContenidoHTML(factura, ventaId, cliente, numeroDoc));
            }
            
            return rutaCompleta;
        } catch (Exception e) {
            System.err.println("Error al generar HTML: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Genera el contenido HTML de la factura
     */
    private static String generarContenidoHTML(Factura factura, int ventaId, ClienteDTO cliente, String numeroDoc) {
        VentaDTO venta = factura.getVenta();
        List<VentaDetalleDTO> detalles = factura.getDetalles();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<title>").append(venta.getTipoDoc()).append(" ").append(numeroDoc).append("</title>\n");
        html.append("<style>\n");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append(".header { text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px; margin-bottom: 20px; }\n");
        html.append(".titulo { font-size: 24px; font-weight: bold; }\n");
        html.append(".numero { font-size: 18px; font-weight: bold; margin: 10px 0; }\n");
        html.append(".empresa, .cliente { margin: 15px 0; }\n");
        html.append(".empresa h3, .cliente h3 { margin-bottom: 5px; }\n");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("th { background-color: #f2f2f2; font-weight: bold; }\n");
        html.append(".totales { float: right; width: 300px; margin-top: 20px; }\n");
        html.append(".totales table { width: 100%; }\n");
        html.append(".total-final { font-size: 18px; font-weight: bold; background-color: #e0e0e0; }\n");
        html.append(".pie { margin-top: 30px; text-align: center; font-size: 12px; color: #666; }\n");
        html.append("@media print { body { margin: 0; } }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        
        // Encabezado
        html.append("<div class='header'>\n");
        html.append("<div class='titulo'>").append(venta.getTipoDoc()).append(" ELECTRÓNICA</div>\n");
        html.append("<div class='numero'>N° ").append(numeroDoc).append("</div>\n");
        html.append("<div>Fecha: ").append(venta.getFecha().format(formatter)).append("</div>\n");
        html.append("</div>\n");
        
        // Datos de la empresa
        html.append("<div class='empresa'>\n");
        html.append("<h3>EMPRESA:</h3>\n");
        html.append("<p><strong>").append(EMPRESA_NOMBRE).append("</strong><br>\n");
        html.append("RUC: ").append(EMPRESA_RUC).append("<br>\n");
        html.append(EMPRESA_DIRECCION).append("<br>\n");
        html.append("Tel: ").append(EMPRESA_TELEFONO).append(" | Email: ").append(EMPRESA_EMAIL).append("</p>\n");
        html.append("</div>\n");
        
        // Datos del cliente
        html.append("<div class='cliente'>\n");
        html.append("<h3>").append(venta.getTipoDoc().equals("FACTURA") ? "CLIENTE:" : "DATOS DEL CLIENTE:").append("</h3>\n");
        html.append("<p>Nombre: ").append(cliente.getNombre()).append("<br>\n");
        html.append("DNI: ").append(cliente.getDni()).append("<br>\n");
        html.append("Dirección: ").append(cliente.getDireccion() != null ? cliente.getDireccion() : "").append("</p>\n");
        html.append("</div>\n");
        
        // Tabla de productos
        html.append("<table>\n");
        html.append("<thead>\n<tr>\n");
        html.append("<th>Código</th>\n");
        html.append("<th>Descripción</th>\n");
        html.append("<th>Cant.</th>\n");
        html.append("<th>Precio Unit.</th>\n");
        html.append("<th>Importe</th>\n");
        html.append("</tr>\n</thead>\n<tbody>\n");
        
        for (VentaDetalleDTO detalle : detalles) {
            ProductoDTO producto = productoDAO.buscarPorId(detalle.getProductoId());
            String codigo = (producto != null) ? producto.getCodigo() : "PROD" + detalle.getProductoId();
            String descripcion = (producto != null) ? producto.getDescripcion() : "Producto " + detalle.getProductoId();
            
            html.append("<tr>\n");
            html.append("<td>").append(codigo).append("</td>\n");
            html.append("<td>").append(descripcion).append("</td>\n");
            html.append("<td>").append(detalle.getCantidad()).append("</td>\n");
            html.append("<td>S/ ").append(String.format("%.2f", detalle.getPrecio())).append("</td>\n");
            html.append("<td>S/ ").append(String.format("%.2f", detalle.getImporte())).append("</td>\n");
            html.append("</tr>\n");
        }
        
        html.append("</tbody>\n</table>\n");
        
        // Totales
        html.append("<div class='totales'>\n");
        html.append("<table>\n");
        html.append("<tr><td><strong>Subtotal:</strong></td><td>S/ ").append(String.format("%.2f", venta.getSubtotal())).append("</td></tr>\n");
        html.append("<tr><td><strong>IGV (18%):</strong></td><td>S/ ").append(String.format("%.2f", venta.getIgv())).append("</td></tr>\n");
        html.append("<tr class='total-final'><td><strong>TOTAL:</strong></td><td>S/ ").append(String.format("%.2f", venta.getTotal())).append("</td></tr>\n");
        html.append("</table>\n");
        html.append("</div>\n");
        
        // Pie de página
        html.append("<div style='clear: both;'></div>\n");
        html.append("<div class='pie'>\n");
        if (venta.isDelivery()) {
            html.append("<p><strong>✓ SERVICIO DE DELIVERY INCLUIDO</strong></p>\n");
        }
        html.append("<p>Tipo de Venta: ").append(venta.getTipoVenta()).append("</p>\n");
        html.append("<p><strong>Gracias por su compra!</strong></p>\n");
        html.append("<p>").append(EMPRESA_WEB).append(" | ").append(EMPRESA_EMAIL).append("</p>\n");
        html.append("</div>\n");
        
        html.append("</body>\n</html>");
        
        return html.toString();
    }
    
    // Métodos para generar PDF con iText
    private static void agregarEncabezadoPDF(com.itextpdf.text.Document doc, 
                                            String tipoDoc, String numeroDoc, 
                                            java.time.LocalDateTime fecha) throws Exception {
        com.itextpdf.text.Font fuenteTitulo = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fuenteSubtitulo = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
        
        com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph(
            tipoDoc + " ELECTRÓNICA", fuenteTitulo);
        titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        titulo.setSpacingAfter(5);
        doc.add(titulo);
        
        com.itextpdf.text.Paragraph numero = new com.itextpdf.text.Paragraph(
            "N° " + numeroDoc, fuenteSubtitulo);
        numero.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        numero.setSpacingAfter(15);
        doc.add(numero);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        com.itextpdf.text.Paragraph fechaDoc = new com.itextpdf.text.Paragraph(
            "Fecha: " + fecha.format(formatter));
        fechaDoc.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        fechaDoc.setSpacingAfter(20);
        doc.add(fechaDoc);
    }
    
    private static void agregarDatosEmpresaPDF(com.itextpdf.text.Document doc) throws Exception {
        com.itextpdf.text.Font fuenteNegrita = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        
        doc.add(new com.itextpdf.text.Paragraph("EMPRESA:", fuenteNegrita));
        doc.add(new com.itextpdf.text.Paragraph(EMPRESA_NOMBRE));
        doc.add(new com.itextpdf.text.Paragraph("RUC: " + EMPRESA_RUC));
        doc.add(new com.itextpdf.text.Paragraph(EMPRESA_DIRECCION));
        doc.add(new com.itextpdf.text.Paragraph("Tel: " + EMPRESA_TELEFONO));
        doc.add(new com.itextpdf.text.Paragraph("Email: " + EMPRESA_EMAIL));
        doc.add(new com.itextpdf.text.Paragraph(" "));
    }
    
    private static void agregarDatosClientePDF(com.itextpdf.text.Document doc, 
                                               ClienteDTO cliente, String tipoDoc) throws Exception {
        com.itextpdf.text.Font fuenteNegrita = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        
        String tituloCliente = tipoDoc.equals("FACTURA") ? "CLIENTE:" : "DATOS DEL CLIENTE:";
        doc.add(new com.itextpdf.text.Paragraph(tituloCliente, fuenteNegrita));
        doc.add(new com.itextpdf.text.Paragraph("Nombre: " + cliente.getNombre()));
        doc.add(new com.itextpdf.text.Paragraph("DNI: " + cliente.getDni()));
        doc.add(new com.itextpdf.text.Paragraph("Dirección: " + 
            (cliente.getDireccion() != null ? cliente.getDireccion() : "")));
        if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
            doc.add(new com.itextpdf.text.Paragraph("Teléfono: " + cliente.getTelefono()));
        }
        doc.add(new com.itextpdf.text.Paragraph(" "));
    }
    
    private static void agregarTablaProductosPDF(com.itextpdf.text.Document doc, 
                                                 List<VentaDetalleDTO> detalles) throws Exception {
        com.itextpdf.text.Font fuenteNegrita = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD);
        
        com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3, 4, 2, 2, 2});
        
        // Encabezados
        tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Código", fuenteNegrita)));
        tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Descripción", fuenteNegrita)));
        tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Cant.", fuenteNegrita)));
        tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Precio", fuenteNegrita)));
        tabla.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Importe", fuenteNegrita)));
        
        // Datos
        for (VentaDetalleDTO detalle : detalles) {
            ProductoDTO producto = productoDAO.buscarPorId(detalle.getProductoId());
            String codigo = (producto != null) ? producto.getCodigo() : "PROD" + detalle.getProductoId();
            String descripcion = (producto != null) ? producto.getDescripcion() : "Producto " + detalle.getProductoId();
            
            tabla.addCell(new com.itextpdf.text.Phrase(codigo));
            tabla.addCell(new com.itextpdf.text.Phrase(descripcion));
            tabla.addCell(new com.itextpdf.text.Phrase(String.valueOf(detalle.getCantidad())));
            tabla.addCell(new com.itextpdf.text.Phrase(
                String.format("S/ %.2f", detalle.getPrecio())));
            tabla.addCell(new com.itextpdf.text.Phrase(
                String.format("S/ %.2f", detalle.getImporte())));
        }
        
        doc.add(tabla);
        doc.add(new com.itextpdf.text.Paragraph(" "));
    }
    
    private static void agregarTotalesPDF(com.itextpdf.text.Document doc, VentaDTO venta) throws Exception {
        com.itextpdf.text.Font fuenteNegrita = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD);
        
        com.itextpdf.text.pdf.PdfPTable tablaTotales = new com.itextpdf.text.pdf.PdfPTable(2);
        tablaTotales.setWidthPercentage(40);
        tablaTotales.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
        tablaTotales.setWidths(new float[]{2, 2});
        
        tablaTotales.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("Subtotal:", fuenteNegrita)));
        tablaTotales.addCell(new com.itextpdf.text.Phrase(
            String.format("S/ %.2f", venta.getSubtotal())));
        
        tablaTotales.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("IGV (18%):", fuenteNegrita)));
        tablaTotales.addCell(new com.itextpdf.text.Phrase(
            String.format("S/ %.2f", venta.getIgv())));
        
        com.itextpdf.text.Font fuenteTotal = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        tablaTotales.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase("TOTAL:", fuenteTotal)));
        tablaTotales.addCell(new com.itextpdf.text.pdf.PdfPCell(
            new com.itextpdf.text.Phrase(
                String.format("S/ %.2f", venta.getTotal()), fuenteTotal)));
        
        doc.add(tablaTotales);
        doc.add(new com.itextpdf.text.Paragraph(" "));
    }
    
    private static void agregarPiePaginaPDF(com.itextpdf.text.Document doc, VentaDTO venta) throws Exception {
        com.itextpdf.text.Font fuentePie = new com.itextpdf.text.Font(
            com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.ITALIC);
        
        if (venta.isDelivery()) {
            doc.add(new com.itextpdf.text.Paragraph(
                "✓ SERVICIO DE DELIVERY INCLUIDO", fuentePie));
        }
        
        doc.add(new com.itextpdf.text.Paragraph(" "));
        doc.add(new com.itextpdf.text.Paragraph(
            "Tipo de Venta: " + venta.getTipoVenta(), fuentePie));
        doc.add(new com.itextpdf.text.Paragraph(" "));
        doc.add(new com.itextpdf.text.Paragraph(
            "Gracias por su compra!", 
            new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD)));
    }
}

