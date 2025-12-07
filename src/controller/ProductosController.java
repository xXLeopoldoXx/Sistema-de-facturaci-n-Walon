package controller;

import model.dao.ProductoDAO;
import model.dao.ProveedorDAO;
import model.dto.ProductoDTO;
import model.dto.ProveedorDTO;
import view.ProductosView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductosController {

    private ProductosView view;
    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO;
    private DefaultTableModel modelo;

    private int idSeleccionado = -1;

    // Para mapear índice combo -> proveedorId
    private List<ProveedorDTO> listaProveedores;

    public ProductosController(ProductosView v){
        view = v;
        productoDAO = new ProductoDAO();
        proveedorDAO = new ProveedorDAO();

        init();
        cargarProveedoresCombo();
        cargarTabla();
    }

    private void init(){
        view.btnNuevo.addActionListener(e -> limpiar());
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());

        view.tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    private void cargarProveedoresCombo(){
        view.cbProveedor.removeAllItems();
        listaProveedores = proveedorDAO.listar();

        view.cbProveedor.addItem("-- Seleccione --");
        for(ProveedorDTO p : listaProveedores){
            view.cbProveedor.addItem(p.getNombre());
        }
    }

    private void cargarTabla(){
        modelo = (DefaultTableModel) view.tablaProductos.getModel();
        modelo.setRowCount(0);

        List<ProductoDTO> lista = productoDAO.listar();

        for(ProductoDTO p : lista){
            // Buscar nombre proveedor para mostrarlo
            ProveedorDTO prov = proveedorDAO.buscarPorId(p.getProveedorId());
            String provNombre = (prov != null) ? prov.getNombre() : "SIN PROVEEDOR";

            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getCodigo(),
                    p.getDescripcion(),
                    p.getStock(),
                    p.getPrecioUnit(),
                    p.getPrecioMayor(),
                    p.getProveedorId(),
                    provNombre
            });
        }

        // Ocultar ID y PROVEEDOR_ID
        view.tablaProductos.getColumnModel().getColumn(0).setMinWidth(0);
        view.tablaProductos.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tablaProductos.getColumnModel().getColumn(0).setWidth(0);

        view.tablaProductos.getColumnModel().getColumn(6).setMinWidth(0);
        view.tablaProductos.getColumnModel().getColumn(6).setMaxWidth(0);
        view.tablaProductos.getColumnModel().getColumn(6).setWidth(0);
    }

    private void guardar(){
        String codigo = view.txtCodigo.getText().trim();
        String desc = view.txtDescripcion.getText().trim();

        int stock;
        double precioU, precioM;

        try{
            stock = Integer.parseInt(view.txtStock.getText().trim());
            precioU = Double.parseDouble(view.txtPrecioUnit.getText().trim());
            precioM = Double.parseDouble(view.txtPrecioMayor.getText().trim());
        }catch(Exception e){
            JOptionPane.showMessageDialog(view, "Stock o precios inválidos");
            return;
        }

        int indexCombo = view.cbProveedor.getSelectedIndex();
        if(indexCombo <= 0){
            JOptionPane.showMessageDialog(view, "Selecciona un proveedor");
            return;
        }

        int proveedorId = listaProveedores.get(indexCombo - 1).getId();

        if(codigo.isEmpty() || desc.isEmpty()){
            JOptionPane.showMessageDialog(view, "Código y descripción son obligatorios");
            return;
        }

        ProductoDTO prod = new ProductoDTO(codigo, desc, stock, precioU, precioM, proveedorId);

        if(productoDAO.insertar(prod)){
            JOptionPane.showMessageDialog(view, "Producto guardado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo guardar");
        }
    }

    private void actualizar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un producto de la tabla");
            return;
        }

        String codigo = view.txtCodigo.getText().trim();
        String desc = view.txtDescripcion.getText().trim();

        int stock;
        double precioU, precioM;

        try{
            stock = Integer.parseInt(view.txtStock.getText().trim());
            precioU = Double.parseDouble(view.txtPrecioUnit.getText().trim());
            precioM = Double.parseDouble(view.txtPrecioMayor.getText().trim());
        }catch(Exception e){
            JOptionPane.showMessageDialog(view, "Stock o precios inválidos");
            return;
        }

        int indexCombo = view.cbProveedor.getSelectedIndex();
        if(indexCombo <= 0){
            JOptionPane.showMessageDialog(view, "Selecciona un proveedor");
            return;
        }

        int proveedorId = listaProveedores.get(indexCombo - 1).getId();

        ProductoDTO prod = new ProductoDTO(idSeleccionado, codigo, desc, stock, precioU, precioM, proveedorId);

        if(productoDAO.actualizar(prod)){
            JOptionPane.showMessageDialog(view, "Producto actualizado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo actualizar");
        }
    }

    private void eliminar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un producto de la tabla");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(
                view,
                "¿Seguro de eliminar este producto?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if(ok == JOptionPane.YES_OPTION){
            if(productoDAO.eliminar(idSeleccionado)){
                JOptionPane.showMessageDialog(view, "Producto eliminado");
                cargarTabla();
                limpiar();
            }else{
                JOptionPane.showMessageDialog(view, "No se pudo eliminar");
            }
        }
    }

    private void seleccionarFila(){
        int fila = view.tablaProductos.getSelectedRow();
        if(fila == -1) return;

        idSeleccionado = Integer.parseInt(view.tablaProductos.getValueAt(fila, 0).toString());

        view.txtCodigo.setText(view.tablaProductos.getValueAt(fila, 1).toString());
        view.txtDescripcion.setText(view.tablaProductos.getValueAt(fila, 2).toString());
        view.txtStock.setText(view.tablaProductos.getValueAt(fila, 3).toString());
        view.txtPrecioUnit.setText(view.tablaProductos.getValueAt(fila, 4).toString());
        view.txtPrecioMayor.setText(view.tablaProductos.getValueAt(fila, 5).toString());

        int proveedorIdTabla = Integer.parseInt(view.tablaProductos.getValueAt(fila, 6).toString());

        // Seleccionar proveedor en combo según proveedorId
        view.cbProveedor.setSelectedIndex(0);
        for(int i=0; i<listaProveedores.size(); i++){
            if(listaProveedores.get(i).getId() == proveedorIdTabla){
                view.cbProveedor.setSelectedIndex(i+1);
                break;
            }
        }
    }

    private void limpiar(){
        view.txtCodigo.setText("");
        view.txtDescripcion.setText("");
        view.txtStock.setText("");
        view.txtPrecioUnit.setText("");
        view.txtPrecioMayor.setText("");
        view.cbProveedor.setSelectedIndex(0);
        idSeleccionado = -1;
        view.txtCodigo.requestFocus();
    }
}
