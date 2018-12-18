DROP DATABASE IF EXISTS `tpv`;
CREATE DATABASE IF NOT EXISTS `tpv`;
USE `tpv`;

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `nombre` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `usuarios` (`nombre`, `password`) VALUES
	('admin', 'admin'),	('camarero1', 'camarero1');

DROP TABLE IF EXISTS `identifcomanda`;
CREATE TABLE IF NOT EXISTS `identifcomanda` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `subtotal` double NOT NULL,
  `impuestos` double NOT NULL,
  `total` double NOT NULL,
  `tasa` double NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_identifcomanda_usuarios` (`usuario`),
  CONSTRAINT `FK_identifcomanda_usuarios` FOREIGN KEY (`usuario`) REFERENCES `usuarios` (`nombre`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(15) NOT NULL,
  `precio` double NOT NULL,
  UNIQUE KEY `Index 2` (`nombre`),
  KEY `Índice 1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

INSERT INTO `productos` (`id`, `nombre`, `precio`) VALUES
	(19, 'Agua 1,5L', 1.5),	(18, 'Agua 50cl', 0.75),	(23, 'Bitter Kas', 2),	(10, 'Bollito', 0.6),	(3, 'Café/desc', 1.2),	(1, 'Café/leche', 1.2),	(2, 'Café/solo', 1.2),
	(11, 'Churros', 0.8),	(6, 'ColaCao', 1.2),	(5, 'Cortado', 1.2),	(25, 'Cubata I', 5.5),	(24, 'Cubata N', 4.5),	(22, 'Kas Lim.', 2),	(21, 'Kas Nar.', 2),	(8, 'Leche', 1.2),
	(4, 'Manchado', 1.2),	(13, 'Menú dia', 9),	(14, 'Menú FdS', 12),	(30, 'Moscatel', 2),	(29, 'Mosto', 2),	(7, 'Nesquik', 1.2),	(20, 'Pepsi', 2),	(12, 'Porras', 0.8),
	(15, 'Ración A', 4),	(16, 'Ración B', 5),	(17, 'Ración C', 6),	(9, 'Tostadas', 0.5),	(27, 'Vino Blanco', 2.5),	(28, 'Vino Rosado', 2.5),	(26, 'Vino Tinto', 2.5);

DROP TABLE IF EXISTS `productoscomanda`;
CREATE TABLE IF NOT EXISTS `productoscomanda` (
  `comanda_id` int(10) NOT NULL,
  `producto_nombre` varchar(255) NOT NULL,
  `producto_precio` double NOT NULL,
  KEY `FK_productoscomanda_identifcomanda` (`comanda_id`),
  CONSTRAINT `FK_productoscomanda_identifcomanda` FOREIGN KEY (`comanda_id`) REFERENCES `identifcomanda` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
