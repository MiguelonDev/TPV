package tpv.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import tpv.dao.ComandasDAO;
import tpv.dao.ProductosDAO;
import tpv.model.Comanda;
import tpv.model.Producto;

/**
 * Clase que genera la pantalla y gestiona todas las acciones que pueden generar sus componentes
 * 
 * @author miguel.aguirre
 *
 */
public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8655188175948638048L;
	
	private JButton btnAceptar = new JButton("Aceptar");
	private JButton btnLimpiar = new JButton("Limpiar");
	private JButton btnEliminar = new JButton("Eliminar");
	private JButton btnCerrar = new JButton("Cerrar");
	private JTextField txtSubtotal = new JTextField();
	private JTextField txtImpuestos = new JTextField();
	private JTextField txtTotal = new JTextField();
	
	private ProductosDAO productosDAO = new ProductosDAO();
	private ProductosListModel productos = new ProductosListModel();
	
	private static final double TIPO_IMPUESTO = 0.21;
	
	private static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("##0.00");
		
	/**
	 * 
	 * Método que inicializa un {@link} JFrame sobre el que inicializar todos los demas componentes
	 * 
	 */
	public Display() {
		super("TPV");
		inicializaComponentes();
	}
	
	/**
	 * Método que se encarga de inicializar los componentes de la 
	 * pantalla, asignarles propiedades y funcionalidades y 
	 * establecer sus posiciones relativas en el marco principal
	 * 
	 */
	private void inicializaComponentes() {
		setLayout(new BorderLayout());
		JLabel lblTitulo = new JLabel("TPV Miguel Aguirre Varela");
		lblTitulo.setHorizontalAlignment(JLabel.CENTER);
		add(lblTitulo, BorderLayout.NORTH);
		
		
		
		JPanel pnlCentro = new JPanel(new GridLayout(1, 2));
		
		JPanel pnlCuenta = new JPanel(new BorderLayout());
		JLabel lblCuenta = new JLabel("Comanda");
		lblCuenta.setHorizontalAlignment(JLabel.CENTER);
		pnlCuenta.add(lblCuenta, BorderLayout.NORTH);
				
		JList<Producto> lstComanda = new JList<>(productos);
		
		pnlCuenta.add(new JScrollPane(lstComanda), BorderLayout.CENTER);
		
		pnlCentro.add(pnlCuenta);
		
		JPanel pnlDerecha = new JPanel(new BorderLayout());
		
		JPanel pnlProductos = new JPanel(new GridLayout(0, 2));
		try {
			List<Producto> listaProductos = productosDAO.findAll();
			if(listaProductos != null) {
				for(Producto producto : listaProductos) {
					JButton btnProducto = new JButton(producto.getNombre());
					btnProducto.addActionListener(e -> {
						productos.addProducto(producto);
						recalcular();
					});
					
					pnlProductos.add(btnProducto);
				}
			}
		} catch(SQLException e) {
			// TODO Error buscando productos.  Mostrar alerta al usuario
		}
		
		
		pnlDerecha.add(pnlProductos, BorderLayout.CENTER);
		pnlDerecha.add(new JLabel(" "), BorderLayout.NORTH);
		pnlCentro.add(pnlDerecha);
		
		add(pnlCentro, BorderLayout.CENTER);
		
		JPanel pnlBotonera = new JPanel();
		JPanel pnlResumen = new JPanel(new GridLayout(3, 2));
		
		txtSubtotal.setHorizontalAlignment(JTextField.RIGHT);
		txtSubtotal.setEditable(false);
		pnlResumen.add(txtSubtotal);
		pnlResumen.add(new JLabel(" <= Subtotal"));
		
		txtImpuestos.setHorizontalAlignment(JTextField.RIGHT);
		txtImpuestos.setEditable(false);
		pnlResumen.add(txtImpuestos);
		pnlResumen.add(new JLabel(" <= Impuestos (21% IVA)"));
		
		txtTotal.setHorizontalAlignment(JTextField.RIGHT);
		txtTotal.setEditable(false);
		pnlResumen.add(txtTotal);
		pnlResumen.add(new JLabel(" <= Total"));
		
		
		
		JPanel pnlPie = new JPanel(new BorderLayout());
				
		pnlPie.add(pnlResumen, BorderLayout.WEST);
				
		btnAceptar.addActionListener(e -> {

			int respuesta = JOptionPane.showConfirmDialog(Display.this, 
					"¿Cerrar comanda e imprimir ticket?", 
					"Cerrar comanda", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			if(respuesta == JOptionPane.YES_OPTION) {
				// antes de resetear guardamos en BBDD la comanda generada
				
				Comanda comanda = new Comanda();
				ComandasDAO dao = new ComandasDAO();
				
				
				comanda.setProductos(productos.getProductos());
				
				
				try {
					// realizamos las inserciones de la comanda completa en las tablas correspondientes para llevar una auditoria del uso del tpv
					dao.save(comanda,txtSubtotal.getText(),txtImpuestos.getText(),txtTotal.getText(), TIPO_IMPUESTO); 
				}catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
				productos.reset();
				recalcular();
			}
		});
		
		btnLimpiar.addActionListener(e -> {
			productos.reset();
			recalcular();
		});
		btnEliminar.addActionListener(e -> {
			// Gestionamos TODA la selección aunque podría haberse limitado a un solo índice
			int[] seleccionados = lstComanda.getSelectedIndices();
			for(int sel : seleccionados) {
				productos.removeProducto(sel);
			}
			recalcular();
			
		});
		
		btnCerrar.addActionListener(e -> {
			
			int respuesta = JOptionPane.showConfirmDialog(Display.this, 
					"¿Quieres cerrar la aplicación TPV?", 
					"Cerrar TPV", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
			if(respuesta == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		});
		
		pnlBotonera.add(btnAceptar);
		pnlBotonera.add(btnEliminar);
		pnlBotonera.add(btnLimpiar);
		pnlBotonera.add(btnCerrar);
		
		
		pnlPie.add(pnlBotonera, BorderLayout.SOUTH);
		
		add(pnlPie, BorderLayout.SOUTH);
	}
	
	/**
	 * Método que recalcula el resultado de los impuestos sobre la lista de comanda, 
	 * el sumatorio esta última y la suma de ambos resultados
	 */
	private void recalcular() {
		double subtotal = 0;
		for(Producto producto : productos.getProductos()) {
			subtotal += producto.getPrecio();
		}
		double impuestos = subtotal * TIPO_IMPUESTO;
		
		double total = subtotal + impuestos;
		
		txtSubtotal.setText(DECIMAL_FORMAT.format(subtotal));
		txtImpuestos.setText(DECIMAL_FORMAT.format(impuestos));
		txtTotal.setText(DECIMAL_FORMAT.format(total));
	}
	
	/**
	 * Método principal en el que se inicia la ejecución de la aplicación
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display d = new Display();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		d.setAlwaysOnTop(true); // se fuerza que siempre esté por encima de las demás aplicaciones
		d.setUndecorated(true); // Quita el marco, ha sido necesario implementar el boton que permita cerrar la aplicacion
		d.setResizable(false);
		d.setBounds((screenSize.width - 600) / 2, (screenSize.height - 600) / 2, 600, 600);
		d.setVisible(true);
		d.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
