package tpv.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de modelo que representa a una comanda
 * 
 * @author miguel.aguirre
 *
 */
public class Comanda {
	private Long id;
	private List<Producto> productos = new ArrayList<>();
	
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("Comanda [id=").append(id).append(", productos=");
		
		if(productos != null) {
			for(Producto producto : productos) {
				builder.append(producto).append(", ");
			}
		}
		builder.append("]");
		return builder.toString();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
}
