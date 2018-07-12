package tpv.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import tpv.model.Producto;

/**
 * ListModel para el JList que representa los productos de la comanda
 * 
 * @author miguel.aguirre
 */
public class ProductosListModel extends AbstractListModel<Producto> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4952927333188736527L;
	
	private List<Producto> productos = new ArrayList<>();

	@Override
	public Producto getElementAt(int index) {
		return productos.get(index);
	}

	@Override
	public int getSize() {
		return productos.size();
	}
	
	public void addProducto(Producto producto) {
		productos.add(producto);
		fireIntervalAdded(this, getSize(), getSize() + 1);
	}
	
	public void removeProducto(int index) {
		productos.remove(index);
		fireIntervalRemoved(this, getSize(), getSize() + 1);
	}
	
	public List<Producto> getProductos() {
		//TODO: Hacer una lista defensiva para no exponer la lista interna
		return productos;
	}
	
	public void reset() {
		productos.clear();
		fireIntervalRemoved(this, 0, getSize() + 1);
	}
	
}
