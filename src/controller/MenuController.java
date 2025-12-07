package controller;

import view.*;

public class MenuController {
    private MenuView view;

    public MenuController(MenuView v){
        this.view = v;
        init();
    }

    private void init(){
        view.btnClientes.addActionListener(e -> abrirClientes());
        view.btnProductos.addActionListener(e -> abrirProductos());
        view.btnProveedores.addActionListener(e -> abrirProveedores());
        view.btnVentas.addActionListener(e -> abrirVentas());
        view.btnCerrar.addActionListener(e -> cerrarSesion());
    }

    private void abrirClientes(){
        ClientesView c = new ClientesView();
        c.setVisible(true);
        c.setLocationRelativeTo(null);
    }

    private void abrirProductos(){
        ProductosView p = new ProductosView();
        p.setVisible(true);
        p.setLocationRelativeTo(null);
    }

    private void abrirProveedores(){
        ProveedoresView pr = new ProveedoresView();
        pr.setVisible(true);
        pr.setLocationRelativeTo(null);
    }

    private void abrirVentas(){
        VentasView v = new VentasView();
        v.setVisible(true);
        v.setLocationRelativeTo(null);
        // VentasController ya está conectado en el constructor de VentasView
    }

    private void cerrarSesion(){
        view.dispose();
        LoginView l = new LoginView();
        l.setVisible(true);
        l.setLocationRelativeTo(null);
    }
}

