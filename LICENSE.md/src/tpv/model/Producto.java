package tpv.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 
 * Objeto de modelo que representa un producto
 * 
 * @author miguel.aguirre
 *
 */
public class Producto {
	
	private Long id;
	private String nombre;
	private Double precio;
	
	private NumberFormat DECIMAL_FORMAT = new DecimalFormat("#00.00");
	
	
	// se crea el método toString por si es necesario en un futuro para llevar un sistema de trazas
	@Override
	public String toString() {
		//return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + "]";
		return  "[" + DECIMAL_FORMAT.format(precio) + "]........." + nombre;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

}
