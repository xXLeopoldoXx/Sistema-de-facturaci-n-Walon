package controller;

import model.dao.ProveedorDAO;
import model.dto.ProveedorDTO;
import view.ProveedoresView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProveedoresController {

    private ProveedoresView view;
    private ProveedorDAO dao;
    private DefaultTableModel modelo;

    private int idSeleccionado = -1;

    public ProveedoresController(ProveedoresView v){
        view = v;
        dao = new ProveedorDAO();
        init();
        cargarTabla();
    }

    private void init(){
        view.btnNuevo.addActionListener(e -> limpiar());
        view.btnGuardar.addActionListener(e -> guardar());
        view.btnActualizar.addActionListener(e -> actualizar());
        view.btnEliminar.addActionListener(e -> eliminar());

        view.tablaProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarFila();
            }
        });
    }

    private void cargarTabla(){
        modelo = (DefaultTableModel) view.tablaProveedores.getModel();
        modelo.setRowCount(0);

        List<ProveedorDTO> lista = dao.listar();
        for(ProveedorDTO p: lista){
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getRuc(),
                    p.getNombre(),
                    p.getTelefono(),
                    p.getDireccion(),
                    p.getRazonSocial()
            });
        }

        // ocultar ID
        view.tablaProveedores.getColumnModel().getColumn(0).setMinWidth(0);
        view.tablaProveedores.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tablaProveedores.getColumnModel().getColumn(0).setWidth(0);
    }

    private void guardar(){
        String ruc = view.txtRuc.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String telefono = view.txtTelefono.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        String razonSocial = view.txtRazonSocial.getText().trim();

        if(ruc.isEmpty() || nombre.isEmpty()){
            JOptionPane.showMessageDialog(view, "RUC y Nombre son obligatorios");
            return;
        }
        if(ruc.length() != 11){
            JOptionPane.showMessageDialog(view, "RUC debe tener 11 dígitos");
            return;
        }

        ProveedorDTO p = new ProveedorDTO(ruc, nombre, telefono, direccion, razonSocial);

        if(dao.insertar(p)){
            JOptionPane.showMessageDialog(view, "Proveedor guardado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo guardar");
        }
    }

    private void actualizar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un proveedor de la tabla");
            return;
        }

        String ruc = view.txtRuc.getText().trim();
        String nombre = view.txtNombre.getText().trim();
        String telefono = view.txtTelefono.getText().trim();
        String direccion = view.txtDireccion.getText().trim();
        String razonSocial = view.txtRazonSocial.getText().trim();

        if(ruc.isEmpty() || nombre.isEmpty()){
            JOptionPane.showMessageDialog(view, "RUC y Nombre son obligatorios");
            return;
        }

        ProveedorDTO p = new ProveedorDTO(idSeleccionado, ruc, nombre, telefono, direccion, razonSocial);

        if(dao.actualizar(p)){
            JOptionPane.showMessageDialog(view, "Proveedor actualizado");
            cargarTabla();
            limpiar();
        }else{
            JOptionPane.showMessageDialog(view, "No se pudo actualizar");
        }
    }

    private void eliminar(){
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(view, "Selecciona un proveedor de la tabla");
            return;
        }

        int ok = JOptionPane.showConfirmDialog(
                view,
                "¿Seguro de eliminar este proveedor?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if(ok == JOptionPane.YES_OPTION){
            if(dao.eliminar(idSeleccionado)){
                JOptionPane.showMessageDialog(view, "Proveedor eliminado");
                cargarTabla();
                limpiar();
            }else{
                JOptionPane.showMessageDialog(view, "No se pudo eliminar");
            }
        }
    }

    private void seleccionarFila(){
        int fila = view.tablaProveedores.getSelectedRow();
        if(fila == -1) return;

        idSeleccionado = Integer.parseInt(view.tablaProveedores.getValueAt(fila, 0).toString());

        view.txtRuc.setText(view.tablaProveedores.getValueAt(fila, 1).toString());
        view.txtNombre.setText(view.tablaProveedores.getValueAt(fila, 2).toString());
        view.txtTelefono.setText(view.tablaProveedores.getValueAt(fila, 3).toString());
        view.txtDireccion.setText(view.tablaProveedores.getValueAt(fila, 4).toString());
        view.txtRazonSocial.setText(view.tablaProveedores.getValueAt(fila, 5).toString());
    }

    private void limpiar(){
        view.txtRuc.setText("");
        view.txtNombre.setText("");
        view.txtTelefono.setText("");
        view.txtDireccion.setText("");
        view.txtRazonSocial.setText("");
        idSeleccionado = -1;
        view.txtRuc.requestFocus();
    }
}


