Documentación - Sistema de Facturación TIENDA WALON

Requisitos
- PostgreSQL instalado
- Base de datos `facturacion_tienda` creada
- Driver PostgreSQL agregado al proyecto
- Driver iText para crear pdfs

Ejecutar
1. Ejecutar `Main.java`
2. Ingresar usuario y contraseña
3. Acceder al menú principal

---

Funcionalidades

**Clientes**
- **Nuevo**: Limpiar campos para nuevo cliente
- **Guardar**: Registrar nuevo cliente (DNI, Nombre, Dirección, Teléfono)
- **Actualizar**: Modificar cliente seleccionado en tabla
- **Eliminar**: Eliminar cliente seleccionado
- **Volver**: Regresar al menú principal

2. **Productos**
- **Nuevo**: Limpiar campos para nuevo producto
- **Guardar**: Registrar producto (Código, Descripción, Stock, Precios, Proveedor)
- **Actualizar**: Modificar producto seleccionado
- **Eliminar**: Eliminar producto seleccionado
- **Volver**: Regresar al menú principal

3. **Proveedores**
- **Nuevo**: Limpiar campos para nuevo proveedor
- **Guardar**: Registrar proveedor (RUC, Nombre, Teléfono, Dirección, Razón Social)
- **Actualizar**: Modificar proveedor seleccionado
- **Eliminar**: Eliminar proveedor seleccionado
- **Volver**: Regresar al menú principal

4. **Ventas**
- **Buscar Cliente**: Ingresar DNI para cargar datos del cliente
- **Buscar Producto**: Ingresar código para cargar producto
- **Agregar**: Agregar producto al carrito de venta
- **Actualizar/Eliminar Item**: Modificar o quitar productos del carrito
- **Generar Comprobante**: Crear factura o boleta en PDF/HTML
- **Volver**: Regresar al menú principal

5. **Menú Principal**
- **Clientes**: Gestionar clientes
- **Productos**: Gestionar productos
- **Proveedores**: Gestionar proveedores
- **Ventas**: Realizar ventas y generar comprobantes
- **Cerrar Sesión**: Salir y volver al login

---

Base de Datos

### Configuración
- **Host**: localhost
- **Puerto**: 5432
- **Base de datos**: facturacion_tienda
- **Usuario**: postgres
- **Contraseña**: (configurar en `DBConnection.java`)

### Script SQL
Ejecutar `database/facturacion_tienda_postgresql.sql` para crear las tablas.

---

Generación de Comprobantes

- Los comprobantes se guardan en la carpeta `facturas/`
- Formato: `B-000001.pdf` (Boleta) o `F-000002.pdf` (Factura)
- Si no está instalado iText, se genera un archivo HTML que puede imprimirse como PDF

---

Notas Importantes

- Todos los campos obligatorios deben estar completos antes de guardar
- Los precios en ventas se ajustan según "Por menor" o "Por mayor"
- El stock se actualiza automáticamente al generar una venta
- Usar el botón "Volver" en cada módulo para regresar al menú sin cerrar la aplicación

