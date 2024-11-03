/*
	Lester René López Gálvez
    IN5AV
    2022064
    
    Inicio: 28/03/2023
    Modificaciones: 28/03/2023, 01/04/2023
    
*/

drop database if exists DBTonysKinal2023; 
create database DBTonysKinal2023;

use DBTonysKinal2023;

create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa (codigoEmpresa)
);

create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

create table TipoPlatos(
	codigoTipoPlato int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidadProducto int not null,
    primary key PK_codigoProducto (codigoProducto)
);

create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado (codigoTipoEmpleado)
);

create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa)
);

create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa)
);

create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcion varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlatos(codigoTipoPlato)
);

create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos (codigoProducto)
);

create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Productos foreign key (codigoServicio)
		references Servicios (codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos (codigoPlato)
);

create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
    constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados (codigoEmpleado)
);

-- -------------------------------------------- PROCEDIMIENTOS  ALMACENADOS --------------------------------------------


-- --------------------------------------------------------------------------------------------------------------------
-- ------------------------------------------------ EMPRESAS ----------------------------------------------------------

-- Agregar Empresa
Delimiter $$
	create procedure sp_AgregarEmpresa (in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(8))
		begin
			insert into Empresas (nombreEmpresa, direccion, telefono) 
				values (nombreEmpresa, direccion, telefono);
		end$$
Delimiter ;

call sp_AgregarEmpresa('Tigo','9av. Zona 1 C. De Guatemala','44614876');
call sp_AgregarEmpresa('Banrural','10av. Zona 1, Villa Canales','45698521');
call sp_AgregarEmpresa('Banco BI','Zona 19 Col. La Florida','73000965');
call sp_AgregarEmpresa('Kinal','Zona 7, Col. Landivar','88923401');
call sp_AgregarEmpresa('BANTRAB','Zona 1, Villa Nueva','12094632');
call sp_AgregarEmpresa('Agencia WAY','Zona 9, C. De Guatemala','33775011');
call sp_AgregarEmpresa('Helados Sarita','Zona 12, Villa Nueva','44276390');
call sp_AgregarEmpresa('Pizza Hut','Ciudad Cayalá, Guatemala','77483948');
call sp_AgregarEmpresa('Regia','8av, zona 2 Villa Hermosa','10926384');
call sp_AgregarEmpresa('Coca Cola','Zona 12, Villa Nueva, Col. Las Margaritas','23456789');

-- select * from Empresas;


-- Editar Empresa
Delimiter $$
	create procedure sp_EditarEmpresa (in codEmpresa int, in nombreEmpre varchar(150), in dire varchar(150), in tel varchar(8))
		begin
			update Empresas E
				set E.nombreEmpresa = nombreEmpre,
					E.direccion = dire,
					E.telefono = tel
				where E.codigoEmpresa = codEmpresa;
		end$$
Delimiter ;

-- call sp_EditarEmpresa(1,'Claro','Ciudad De Guatemala','42066209');

-- select * from Empresas;


-- Eliminar Empresa
Delimiter $$
	create procedure sp_EliminarEmpresa (in codEmpresa int)
		begin
			delete from Empresas
				where codigoEmpresa = codEmpresa;
        end$$
Delimiter ;

-- call sp_EliminarEmpresa(6);

-- select * from Empresas;


-- Listar Empresa
Delimiter $$
	create procedure sp_ListarEmpresas()
		begin
			select
				E.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono
                from Empresas E;
        end$$
Delimiter ;

call sp_ListarEmpresas();


-- Buscar Empresa
Delimiter $$
	Create procedure sp_BuscarEmpresa (in codEmpresa int)
		begin
			select 
				E.nombreEmpresa,
                E.direccion,
                E.telefono,
                E.codigoEmpresa
                from Empresas E
					where E.codigoEmpresa = codEmpresa;
        end$$
Delimiter ;

call sp_BuscarEmpresa(2);


-- --------------------------------------------------------------------------------------------------------
-- -------------------------------------------- Tipo Empleado ---------------------------------------------
-- Agregar Tipo Empleado
Delimiter $$
	create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		begin
			insert into TipoEmpleado (descripcion)
				values (descripcion);
        end$$
Delimiter ;

call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Ayudante de cocina');
call sp_AgregarTipoEmpleado('Mesero');
call sp_AgregarTipoEmpleado('Cajero');
call sp_AgregarTipoEmpleado('Limpieza');
call sp_AgregarTipoEmpleado('Gerente');
call sp_AgregarTipoEmpleado('Subchef');
call sp_AgregarTipoEmpleado('Bartender');
call sp_AgregarTipoEmpleado('Pastelero');

-- select * from TipoEmpleado;


-- Editar Tipo Empleado
Delimiter $$
	create procedure sp_EditarTipoEmpleado(in codTipoEm int , in descrip varchar(50))
		begin
			update TipoEmpleado T
				set T.descripcion =  descrip
                where T.codigoTipoEmpleado = codTipoEm;
        end$$
Delimiter ;

-- call sp_EditarTipoEmpleado(1, 'Mesero');

-- select * from TipoEmpleado;


-- Eliminar Tipo Empleado
Delimiter $$
	create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		begin
			delete from TipoEmpleado
				where codigoTipoEmpleado = codTipoEmpleado;
        end$$
Delimiter ;

-- call sp_EliminarTipoEmpleado(1);

-- select * from TipoEmpleado;


-- Listar Tipo Empleado
Delimiter $$
	create procedure sp_ListarTipoEmpleados()
		begin
			select 
				T.codigoTipoEmpleado,
				T.descripcion
                from TipoEmpleado T;
        end$$
Delimiter ;

call sp_ListarTipoEmpleados();

-- Buscar Tipo Empleado
Delimiter $$
	create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
		begin
			select 
				T.codigoTipoEmpleado,
                T.descripcion
                from TipoEmpleado T
					where T.codigoTipoEmpleado = codTipoEmpleado;
        end$$
Delimiter ;

call sp_BuscarTipoEmpleado(2);



-- ---------------------------------------------------------------------------------------------------
-- ------------------------------------------- Tipo Plato --------------------------------------------
-- Agregar Tipo Plato
Delimiter $$
	create procedure sp_AgregarTipoPlato(in descripcion varchar(100))
		begin
			insert into TipoPlatos(descripcion)
				values (descripcion);
        end$$
Delimiter ;

call sp_AgregarTipoPlato('Europea');
call sp_AgregarTipoPlato('Asiatico');
call sp_AgregarTipoPlato('China');
call sp_AgregarTipoPlato('Italiana');
call sp_AgregarTipoPlato('Mexicana');
call sp_AgregarTipoPlato('Asiatica');
call sp_AgregarTipoPlato('Japonesa');
call sp_AgregarTipoPlato('Inglesa');
call sp_AgregarTipoPlato('Alemana');
call sp_AgregarTipoPlato('Guatemalteca');

 select * from TipoPlatos;


-- Editar Tipo Plato
Delimiter $$
	create procedure sp_EditarTipoPlato(in codTipoPla int , in descrip varchar(50))
		begin
			update TipoPlatos T
				set T.descripcion =  descrip
                where T.codigoTipoPlato = codTipoPla;
        end$$
Delimiter ;

-- call sp_EditarTipoPlato(1, 'Sopa de letras');

-- select * from TipoPlatos;


-- Eliminar Tipo Plato
Delimiter $$
	create procedure sp_EliminarTipoPlato(in codTipoPla int)
		begin
			delete from TipoPlatos
				where codigoTipoPlato = codTipoPla;
        end$$
Delimiter ;

-- call sp_EliminarTipoPlato(1);

-- select * from TipoPlatos;


-- Listar Tipo Platos
Delimiter $$
	create procedure sp_ListarTipoPlatos()
		begin
			select 
				T.codigoTipoPlato, 
				T.descripcion
				from TipoPlatos T;
        end$$
Delimiter ;

call sp_ListarTipoPlatos();


-- Buscar Tipo Plato
Delimiter $$
	create procedure sp_BuscarTipoPlato(in codTipoPla int)
		begin
			select 
				T.codigoTipoPlato,
                T.descripcion
                from TipoPlatos T
					where T.codigoTipoPlato = codTipoPla;
        end$$
Delimiter ;

call sp_BuscarTipoPlato(2);


-- --------------------------------------------------------------------------------------------------
-- ------------------------------------------- Productos --------------------------------------------
-- Agregar Producto
Delimiter $$
	create procedure sp_AgregarProducto(in nombreProducto varchar(150), in cantidadProducto int)
		begin
			insert into Productos (nombreProducto, cantidadProducto)
				values (nombreProducto, cantidadProducto);
        end$$
Delimiter ;

call sp_AgregarProducto('Coca-Cola',20);
call sp_AgregarProducto('Pepsi',15);
call sp_AgregarProducto('Crush',23);
call sp_AgregarProducto('Fanta',23);
call sp_AgregarProducto('Cucharas desechables',423);
call sp_AgregarProducto('Cuchillos desechables',20);
call sp_AgregarProducto('Big Cola',185);
call sp_AgregarProducto('Tenedores desechables',78);
call sp_AgregarProducto('Gatorade',776);
call sp_AgregarProducto('Seven Up',34);

-- select * from Productos;


-- Editar Producto
Delimiter $$
	create procedure sp_EditarProducto(in codPro int , in namePro varchar(150), in cant int)
		begin
			update Productos P
				set P.nombreProducto = namePro,
					P.cantidadProducto = cant
					where P.codigoProducto = codPro;
        end$$
Delimiter ;

-- call sp_EditarProducto(1, 'SevenUp',30);

-- select * from Productos;


-- Eliminar Producto
Delimiter $$
	create procedure sp_EliminarProducto(in codPro int)
		begin
			delete from Productos
				where codigoProducto = codPro;
        end$$
Delimiter ;

-- call sp_EliminarProducto(1);


-- Listar Productos
Delimiter $$
	create procedure sp_ListarProductos()
		begin
			select 
				P.codigoProducto,
                P.nombreProducto,
				P.cantidadProducto
				from Productos P;
        end$$
Delimiter ;

call sp_ListarProductos();


-- Buscar Producto
Delimiter $$
	create procedure sp_BuscarProducto(in codPro int)
		begin
			select 
				P.codigoProducto,
                P.nombreProducto,
                P.cantidadProducto
                from Productos P
					where P.codigoProducto = codPro;
        end$$
Delimiter ;

call sp_BuscarProducto(2);


-- --------------------------------------------------------------------------------------------------
-- ------------------------------------------- Empleados --------------------------------------------
-- Agregar Empleado
Delimiter $$
	create procedure sp_AgregarEmpleado(in numeroEmpleado int, in apellidosEmpleado varchar(150), in nombresEmpleado varchar(150), 
		in direccionEmpleado varchar(150), in telefonoContacto varchar(8), in gradoCocinero varchar(50), in codigoTipoEmpleado int)
		begin
			insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, 
				codigoTipoEmpleado)
				values (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, 
					codigoTipoEmpleado);
        end$$
Delimiter ;

call sp_AgregarEmpleado(777,'Delgado Paz','Lester Mateo','Ciudad De Guatemala','44614876','Master',1);
call sp_AgregarEmpleado(222,'Aragon Paz','Jimena Alejandra','Quetzaltenango','45821069','Principiante',2);
call sp_AgregarEmpleado(323,'Pu Chox','Pedro Daniel','Ciudad De Guatemala','77543290','Avanzado',3);
call sp_AgregarEmpleado(221,'Fuentes Caal','Alex Adrian','Villa Nueva','55439800','Intermedio',4);
call sp_AgregarEmpleado(209,'Matias Torres','Wilson Olivares','San Marcos','77392837','Avanzado',5);
call sp_AgregarEmpleado(283,'Solares Paz','Mario Alejandro','Villa Hermosa','88239485','Intermedio',6);
call sp_AgregarEmpleado(212,'López Hernádez','Ariel Josue','Villa Nueva','82938475','Principiante',7);
call sp_AgregarEmpleado(253,'Herrera Solares','Evelyn Esmeralda','Villa Canales','23234567','Avanzado',8);
call sp_AgregarEmpleado(286,'Sanchez Solares','Andrea Maribel','Villa Hermosa','99273648','Intermedio',9);
call sp_AgregarEmpleado(299,'Espinoza Herrera','Aaron Ezequiel','Ciudad De Guatemala','23456789','Intermedio',10);

-- select * from TipoEmpleado;
-- select * from Empleados;


-- Editar Empleado
Delimiter $$
	create procedure sp_EditarEmpleado(in codEmpleado int , in numEmpleado int, in apeEmpleado varchar(150), in nombEmpleado varchar(150), 
		in direEmpleado varchar(150), in telContacto varchar(8), in gradoCoci varchar(50))
		begin
			update Empleados E
				set E.numeroEmpleado = numEmpleado,
					E.apellidosEmpleado = apeEmpleado,
                    E.nombresEmpleado = nombEmpleado,
                    E.direccionEmpleado = direEmpleado,
                    E.telefonoContacto = telContacto,
                    E.gradoCocinero = gradoCoci
					where E.codigoEmpleado = codEmpleado;
        end$$
Delimiter ;

-- call sp_EditarEmpleado(1,1,'Gálvez Ramírez','Mario Andrés','Xela','96255326','Principiante');



-- Eliminar Empleado
Delimiter $$
	create procedure sp_EliminarEmpleado(in codEmpleado int)
		begin
			delete from Empleados
				where codigoEmpleado = codEmpleado;
        end$$
Delimiter ;

-- call sp_EliminarEmpleado(1);

-- select * from Empleados;


-- Listar Empleados
Delimiter $$
	create procedure sp_ListarEmpleados()
		begin
			select 
				E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
				from Empleados E;
        end$$
Delimiter ;

call sp_ListarEmpleados();


-- Buscar Empleado
Delimiter $$
	create procedure sp_BuscarEmpleado(in codEmpleado int)
		begin
			select 
				E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
				from Empleados E
					where E.codigoEmpleado = codEmpleado;
        end$$
Delimiter ;

call sp_BuscarEmpleado(2);

-- Buscar Empleado-TipoEmpleado
Delimiter $$
	create procedure sp_BuscarEmpleadoTipoEmpleado(in codTipoEmpleado int)
		begin
			select 
				E.codigoEmpleado,
                E.numeroEmpleado,
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.direccionEmpleado,
                E.telefonoContacto,
                E.gradoCocinero,
                E.codigoTipoEmpleado
				from Empleados E
					where E.codigoTipoEmpleado = codTipoEmpleado;
        end$$
Delimiter ;


-- --------------------------------------------------------------------------------------------------
-- ------------------------------------------- Servicios --------------------------------------------

-- Agregar servicio
Delimiter $$
	create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(150), in horaServicio time, in lugarServicio varchar(150),
		in telefonoContacto varchar(8), in codigoEmpresa int)
		begin
			insert into Servicios (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values (fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        end$$
Delimiter ;

call sp_AgregarServicio('2024-09-02','Buffet','12:00','Zona 1, C. Guatemala','95115945',1);
call sp_AgregarServicio('2024-10-22','Banquete','10:00','Zona 5, Santa Rosa','12332159',2);
call sp_AgregarServicio('2024-09-18','AutoServicio','20:00','Zona 19, Col. La Florida','58249630',3);
call sp_AgregarServicio('2024-07-07','Brunch','12:10','Ciudad Capital','98765432',4);
call sp_AgregarServicio('2024-08-08','Banquete','17:00','Villa Nueva','12340987',5);
call sp_AgregarServicio('2024-04-23','Barra de ensaladas','14:00','Villa Hermosa','12345678',6);
call sp_AgregarServicio('2024-05-22','Catering','15:00','Villa Canales','58249630',7);
call sp_AgregarServicio('2024-06-10','Entrega a Domicilio','20:00','Santa Catarina Pinula','09889076',8);
call sp_AgregarServicio('2024-03-02','Brunch','18:00','Villa Nueva','87876543',9);
call sp_AgregarServicio('2024-10-09','Buffet','14:00','Zona 4, Linda Vista','00775532',10);

-- select * from Servicios;


-- Editar Servicio
Delimiter $$
	create procedure sp_EditarServicio(in codSer int , in fechaSer date, in tipoSer varchar(150), in horaSer time, in lugarSer varchar(150),
		in telefonoConta varchar(8))
		begin
			update Servicios S
				set S.fechaServicio = fechaSer,
					S.tipoServicio = tipoSer,
                    S.horaServicio = horaSer,
                    S.lugarServicio = lugarSer,
                    S.telefonoContacto = telefonoConta
					where S.codigoServicio = codSer;
        end$$
Delimiter ;

-- call sp_EditarServicio(1,'2023-05-09','Autoservicio','23:00','Chimaltenango','44614876',1);


-- Eliminar Servicio
Delimiter $$
	create procedure sp_EliminarServicio(in codSer int)
		begin
			delete from Servicios
				where codigoServicio = codSer;
        end$$
Delimiter ;

-- call sp_EliminarServicio(1);


-- Listar Servicios
Delimiter $$
	create procedure sp_ListarServicios()
		begin
			select 
				S.codigoServicio,
				S.fechaServicio,
				S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
				from Servicios S;
        end$$
Delimiter ;

call sp_ListarServicios();

-- Buscar Servicio
Delimiter $$
	create procedure sp_BuscarServicio(in codSer int)
		begin
			select 
				S.codigoServicio,
				S.fechaServicio,
				S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
				from Servicios S
					where S.codigoServicio = codSer;
        end$$
Delimiter ;

call sp_BuscarServicio(2);

-- Buscar Servicio-Empresa
Delimiter $$
	create procedure sp_BuscarServicioEmpresa(in codEmp int)
		begin
			select 
				S.codigoServicio,
				S.fechaServicio,
				S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
				from Servicios S
					where S.codigoEmpresa = codEmp;
        end$$
Delimiter ;



-- ----------------------------------------------------------------------------------------------------
-- ------------------------------------------- Presupuesto --------------------------------------------
-- Agregar Presupuesto
Delimiter $$
	create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), codigoEmpresa int)
		begin
			insert into Presupuesto (fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values (fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        end$$
Delimiter ;

call sp_AgregarPresupuesto('2022-04-01',155.21,1);
call sp_AgregarPresupuesto('2022-06-06',205.35,2);
call sp_AgregarPresupuesto('2022-10-11',789.89,3);
call sp_AgregarPresupuesto('2022-12-12',3452.98,4);
call sp_AgregarPresupuesto('2022-08-25',12345.23,5);
call sp_AgregarPresupuesto('2022-07-29',234.09,6);
call sp_AgregarPresupuesto('2022-03-27',23421.23,7);
call sp_AgregarPresupuesto('2022-06-09',23423.92,8);
call sp_AgregarPresupuesto('2022-05-22',239.43,9);
call sp_AgregarPresupuesto('2022-08-12',986.88,10);

-- select * from Presupuesto;


-- Editar Presupuesto
Delimiter $$
	create procedure sp_EditarPresupuesto(in codPres int , in fechaSoli date, in cantPresupuesto decimal(10,2))
		begin
			update Presupuesto P
				set P.fechaSolicitud = fechaSoli,
					P.cantidadPresupuesto = cantPresupuesto
					where P.codigoPresupuesto = codPres;
        end$$
Delimiter ;

-- call sp_EditarPresupuesto(1,'2021-11-07',777.77,1);


-- Eliminar Presupuesto
Delimiter $$
	create procedure sp_EliminarPresupuesto(in codPre int)
		begin
			delete from Presupuesto
				where codigoPresupuesto = codPre;
        end$$
Delimiter ;

-- call sp_EliminarPresupuesto(2);


-- Listar Presupuestos
Delimiter $$
	create procedure sp_ListarPresupuestos()
		begin
			select 
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
				P.codigoEmpresa
				from Presupuesto P;
        end$$
Delimiter ;

call sp_ListarPresupuestos();


-- Buscar Presupuesto
Delimiter $$
	create procedure sp_BuscarPresupuesto(in codPresupuesto int)
		begin
			select 
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
				P.codigoEmpresa
				from Presupuesto P
					where P.codigoPresupuesto = codPresupuesto;
        end$$
Delimiter ;

-- Busar Presupuesto-Empresa
Delimiter $$
	create procedure sp_BuscarPresupuestoEmpresa(in codEmpresa int)
		begin
			select 
				P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
				P.codigoEmpresa
				from Presupuesto P
					where P.codigoEmpresa = codEmpresa;
        end$$
Delimiter ;

call sp_BuscarPresupuestoEmpresa(5);

-- -----------------------------------------------------------------------------------------------------
-- ---------------------------------------------- Platos -----------------------------------------------
-- Agregar Plato 
Delimiter $$
	create procedure sp_AgregarPlato(in cantidad int, in nombrePlato varchar(50), in descripcion varchar(150), 
		in precioPlato decimal(10,2), in codigoTipoPlato int)
		begin
			insert into Platos (cantidad, nombrePlato, descripcion, precioPlato, codigoTipoPlato)
				values (cantidad, nombrePlato, descripcion, precioPlato, codigoTipoPlato);
        end$$
Delimiter ;

call sp_AgregarPlato(5,'Pizza mediana','Pizza con salsa de tomate y peperoni',150.20,1);
call sp_AgregarPlato(30,'Pizza hawaiana','Pizza con piña',30.20,2);
call sp_AgregarPlato(30,'Sopa de camarones','Camarones cocidos con tomate y cebolla',45.45,3);
call sp_AgregarPlato(30,'Risotto de champiñones','Arroz cremoso cocinado con caldo',623.98,4);
call sp_AgregarPlato(30,'Enchiladas de pollo','Tortillas de maíz',23.43,5);
call sp_AgregarPlato(30,'Salmón a la parrilla','Filete de salmón sazonado',123.20,6);
call sp_AgregarPlato(30,'Spaghetti carbonara','spaghetti cocida al dente',234.20,7);
call sp_AgregarPlato(30,'Tacos al pastor','Tortillas de maíz rellenas de carne',123.20,8);
call sp_AgregarPlato(30,'Ensalada César',' Lechuga romana fresca',987.20,9);
call sp_AgregarPlato(30,'Paella de mariscos','plato de arroz español ',89.20,10);


-- select * from Platos;


-- Editar Plato
Delimiter $$
	create procedure sp_EditarPlato(in codPlato int, in cant int, in nomPlato varchar(50), in descrip varchar(150), in prePlato decimal(10,2))
		begin
			update Platos P
				set P.cantidad = cant,
					P.nombrePlato = nomPlato,
                    P.descripcion = descrip,
                    P.precioPlato = prePlato
					where P.codigoPlato = codPlato;
        end$$
Delimiter ;

-- call sp_EditarPlato(1,1,'Pizza Familiar','Pizza 5 estaciones',200.20,1);


-- Eliminar Plato
Delimiter $$
	create procedure sp_EliminarPlato(in codPla int)
		begin
			delete from Platos
				where codigoPlato = codPla;
        end$$
Delimiter ;

-- call sp_EliminarPlato(1);


-- Listar Platos
Delimiter $$
	create procedure sp_ListarPlatos()
		begin
			select 
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato,
                P.descripcion,
                P.precioPlato,
                P.codigoTipoPlato
				from Platos P;
        end$$
Delimiter ;

call sp_ListarPlatos();


-- Buscar Plato
Delimiter $$
	create procedure sp_BuscarPlato(in codPlato int)
		begin
			select 
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato,
                P.descripcion,
                P.precioPlato,
                P.codigoTipoPlato
				from Platos P
					where P.codigoPlato = codPlato;
        end$$
Delimiter ;

call sp_BuscarPlato(2);


-- Buscar Plato-TipoPlato
Delimiter $$
	create procedure sp_BuscarPlatoTipoPlato(in codTipoPlato int)
		begin
			select 
				P.codigoPlato,
				P.cantidad,
                P.nombrePlato,
                P.descripcion,
                P.precioPlato,
                P.codigoTipoPlato
				from Platos P
					where P.codigoTipoPlato = codTipoPlato;
        end$$
Delimiter ;

-- -------------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------- Productos_has_Platos -----------------------------------------------
-- Agregar Productos_has_Platos 
Delimiter $$
	create procedure sp_AgregarProductoHasPlato(in Productos_codigoProducto int , in codigoPlato int, in codigoProducto int)
		begin
			insert into Productos_has_Platos (Productos_codigoProducto, codigoPlato, codigoProducto)
				values (Productos_codigoProducto, codigoPlato, codigoProducto);
        end$$
Delimiter ;

call sp_AgregarProductoHasPlato(1,1,1);
call sp_AgregarProductoHasPlato(2,2,2);
call sp_AgregarProductoHasPlato(3,3,3);
call sp_AgregarProductoHasPlato(4,4,4);
call sp_AgregarProductoHasPlato(5,5,5);
call sp_AgregarProductoHasPlato(6,6,6);
call sp_AgregarProductoHasPlato(7,7,7);
call sp_AgregarProductoHasPlato(8,8,8);
call sp_AgregarProductoHasPlato(9,9,9);
call sp_AgregarProductoHasPlato(10,10,10);

-- select * from Productos_has_Platos;


-- Editar Productos_has_Platos
Delimiter $$
	create procedure sp_EditarProductoHasPlato(in Productos_codProducto int, in codPlato int , in codProducto int)
		begin
			update Productos_has_Platos P
				set P.codigoPlato = codPlato,
                    P.codigoProducto = codProducto
					where P.Productos_codigoProducto = Productos_codProducto;
        end$$
Delimiter ;

-- call sp_EditarProductoHasPlato(1,1,1);


-- Eliminar Productos_has_Platos
Delimiter $$
	create procedure sp_EliminarProductoHasPlato(in codProductoHP int)
		begin
			delete from Productos_has_Platos
				where Productos_codigoProducto = codProductoHP;
        end$$
Delimiter ;

-- call sp_EliminarProductoHasPlato(1);


-- Listar Productos_has_Platos
Delimiter $$
	create procedure sp_ListarProductosHasPlatos()
		begin
			select 
				P.Productos_codigoProducto,
                P.codigoPlato, 
                P.codigoProducto
				from Productos_has_Platos P;
        end$$
Delimiter ;

call sp_ListarProductosHasPlatos();


-- Buscar Productos_has_Platos
Delimiter $$
	create procedure sp_BuscarProductoHasPlato(in codProducto int)
		begin
			select 
				P.Productos_codigoProducto,
                P.codigoPlato, 
                P.codigoProducto
				from Productos_has_Platos P
					where P.Productos_codigoProducto = codProducto;
        end$$
Delimiter ;

call sp_BuscarProductoHasPlato(1);

-- -------------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------- Servicios_has_Platos -----------------------------------------------
-- Agregar Servicios_has_Platos 
Delimiter $$
	create procedure sp_AgregarServicioHasPlato(in Servicios_codigoServicio int, in codigoPlato int, in codigoServicio int)
		begin
			insert into Servicios_has_Platos (Servicios_codigoServicio, codigoPlato, codigoServicio)
				values (Servicios_codigoServicio, codigoPlato, codigoServicio);
        end$$
Delimiter ;

call sp_AgregarServicioHasPlato(1,1,1);
call sp_AgregarServicioHasPlato(2,2,2);
call sp_AgregarServicioHasPlato(3,3,3);
call sp_AgregarServicioHasPlato(4,4,4);
call sp_AgregarServicioHasPlato(5,5,5);
call sp_AgregarServicioHasPlato(6,6,6);
call sp_AgregarServicioHasPlato(7,7,7);
call sp_AgregarServicioHasPlato(8,8,8);
call sp_AgregarServicioHasPlato(9,9,9);
call sp_AgregarServicioHasPlato(10,10,10);


-- select * from Servicios_has_Platos;


-- Editar Servicios_has_Platos
Delimiter $$
	create procedure sp_EditarServicioHasPlato(in Ser_codServicio int, in codPlato int , in codServicio int)
		begin
			update Servicios_has_Platos S
				set S.Servicios_codigoServicio = Ser_codServicio,
					S.codigoPlato = codPlato,
                    S.codigoServicio = codServicio
					where S.Servicios_codigoServicio = Ser_codServicio;
        end$$
Delimiter ;

-- call sp_EditarServicioHasPlato(1,1,1);
-- call sp_EditarServicioHasPlato(2,2,2);
-- call sp_EditarServicioHasPlato(3,3,3);
-- call sp_EditarServicioHasPlato(4,4,4);
-- call sp_EditarServicioHasPlato(5,5,5);
-- call sp_EditarServicioHasPlato(6,6,6);
-- call sp_EditarServicioHasPlato(7,7,7);
-- call sp_EditarServicioHasPlato(8,8,8);
-- call sp_EditarServicioHasPlato(9,9,9);
-- call sp_EditarServicioHasPlato(10,10,10);


-- Eliminar Servicios_has_Platos
Delimiter $$
	create procedure sp_EliminarServicioHasPlato(in codServicioHP int)
		begin
			delete from Servicios_has_Platos
				where Servicios_codigoServicio = codServicioHP;
        end$$
Delimiter ;

-- call sp_EliminarServicioHasPlato(1);


-- Listar Servicios_has_Platos
Delimiter $$
	create procedure sp_ListarServiciosHasPlatos()
		begin
			select 
				S.Servicios_codigoServicio, 
                S.codigoPlato, 
                S.codigoServicio
				from Servicios_has_Platos S;
        end$$
Delimiter ;

call sp_ListarServiciosHasPlatos();


-- Buscar Servicios_has_Platos
Delimiter $$
	create procedure sp_BuscarServicioHasPlato(in codServicio int)
		begin
			select 
				S.Servicios_codigoServicio, 
                S.codigoPlato, 
                S.codigoServicio
				from Servicios_has_Platos S
					where S.Servicios_codigoServicio = codServicio;
        end$$
Delimiter ;

call sp_BuscarServicioHasPlato(2);


-- -------------------------------------------------------------------------------------------------------------------
-- ---------------------------------------------- Servicios_has_Empleados -----------------------------------------------
-- Agregar Servicios_has_Empleados 
Delimiter $$
	create procedure sp_AgregarServicioHasEmpleado(in Servicios_codigoServicio int, in codigoServicio int, in codigoEmpleado int, 
		in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
		begin
			insert into Servicios_has_Empleados (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        end$$
Delimiter ;

call sp_AgregarServicioHasEmpleado(1,1,1,'2024-05-07','18:00','Ciudad De Guatemala');
call sp_AgregarServicioHasEmpleado(2,2,2,'2024-12-09','15:00','Chimaltenango');
call sp_AgregarServicioHasEmpleado(3,3,3,'2024-09-09','10:00','Peten');
call sp_AgregarServicioHasEmpleado(4,4,4,'2024-10-11','12:00','Xela');
call sp_AgregarServicioHasEmpleado(5,5,5,'2024-09-01','20:00','Patzún');
call sp_AgregarServicioHasEmpleado(6,6,6,'2024-12-08','22:00','Santa Rosa');
call sp_AgregarServicioHasEmpleado(7,7,7,'2024-02-02','23:00','Villa Nueva');
call sp_AgregarServicioHasEmpleado(8,8,8,'2024-03-03','15:00','Chiquimula');
call sp_AgregarServicioHasEmpleado(9,9,9,'2024-11-11','14:00','El progreso');
call sp_AgregarServicioHasEmpleado(10,10,10,'2024-01-15','11:00','Villa Hermosa');

-- select * from Servicios_has_Empleados;


-- Editar Servicios_has_Empleados
Delimiter $$
	create procedure sp_EditarServicioHasEmpleado(in Ser_codServicio int, in fechaEve date, in horaEve time, in lugarEve varchar(150))
		begin
			update Servicios_has_Empleados S
				set S.fechaEvento = fechaEve,
                    S.horaEvento = horaEve,
                    S.lugarEvento = lugarEve
					where S.Servicios_codigoServicio = Ser_codServicio;
        end$$
Delimiter ;

-- call sp_EditarServicioHasEmpleado(1,'2010-08-25','10:05','San Marcos');


-- Eliminar Servicios_has_Empleados
Delimiter $$
	create procedure sp_EliminarServicioHasEmpleado(in codServ int)
		begin
			delete from Servicios_has_Empleados
				where Servicios_codigoServicio = codServ;
        end$$
Delimiter ;

-- call sp_EliminarServicioHasEmpleado(2);


-- Listar Servicios_has_Empleados
Delimiter $$
	create procedure sp_ListarServiciosHasEmpleados()
		begin
			select 
				S.Servicios_codigoServicio, 
                S.codigoServicio, 
                S.codigoEmpleado, 
                S.fechaEvento, 
                S.horaEvento, 
                S.lugarEvento
				from Servicios_has_Empleados S;
        end$$
Delimiter ;

call sp_ListarServiciosHasEmpleados();


-- Buscar Servicios_has_Empleados
Delimiter $$
	create procedure sp_BuscarServicioHasEmpleado(in codServicioEmpleado int)
		begin
			select 
				S.Servicios_codigoServicio, 
                S.codigoServicio, 
                S.codigoEmpleado, 
                S.fechaEvento, 
                S.horaEvento, 
                S.lugarEvento
				from Servicios_has_Empleados S
					where S.Servicios_codigoServicio = codServicioEmpleado;
        end$$
Delimiter ;

call sp_BuscarServicioHasEmpleado(1);

-- ------------------------------------------------------------ Login -----------------------------------------------------------------------------
-- ------------------------------------------------------------------------------------------------------------------------------------------------
create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(100) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)
);


create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

-- agregar
Delimiter $$
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), 
		in usuarioLogin varchar(50), in contrasena varchar(50))
			begin
				insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
					values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
            end$$
Delimiter ;

-- Listar
Delimiter $$
	create procedure sp_ListarUsuarios()
		begin
			select 
				U.codigoUsuario, 
                U.nombreUsuario, 
                U.apellidoUsuario, 
                U.usuarioLogin, 
                U.contrasena
			from Usuario U;
        end$$
Delimiter ;

call sp_AgregarUsuario('Lester','Lopez','Less','1234567');
call sp_ListarUsuarios();


-- -- Consulta General
/*
Delimiter $$
    Create procedure sp_ReporteGeneral(in codEmpresa int)
        Begin
            select
                E.nombreEmpresa,
                E.direccion,
                E.telefono,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                S.FechaServicio, 
                S.tipoServicio, 
                S.horaServicio,
                SE.fechaEvento,
                SE.horaEvento,
                SE.lugarEvento,
                EM.nombresEmpleado,
                EM.apellidosEmpleado,
                EM.direccionEmpleado,
                EM.telefonoContacto,
                TE.descripcion,
                PL.cantidad,
                PL.nombrePlato,
                PL.descripcion,
                PL.precioPlato,
                TP.descripcion,
                PD.nombreProducto,
                PD.cantidadProducto
                from Empresas E
                inner join Presupuesto P
                on E.codigoEmpresa = P.codigoEmpresa
                inner join Servicios S
                on S.codigoEmpresa = E.codigoEmpresa
                inner join Servicios_has_Empleados SE
                on SE.codigoServicio = S.codigoServicio
                inner join Empleados EM
                on EM.codigoEmpleado = SE.codigoEmpleado
                inner join TipoEmpleado TE
                on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado
                inner join Servicios_has_Platos SP
                on SP.codigoServicio = S.codigoServicio
                inner join Platos PL
                on PL.codigoPlato = SP.codigoPlato
                inner join TipoPlatos TP
                on TP.codigoTipoPlato = PL.codigoTipoPlato
                inner join Productos_has_Platos PP
                on PP.codigoPlato = PL.codigoPlato
                inner join Productos PD
                on PD.codigoProducto = PP.codigoProducto
                where E.codigoEmpresa = codEmpresa;
        End$$
Delimiter ;*/

-- call sp_ReporteGeneral(1);

 select
                E.nombreEmpresa,
                E.direccion,
                E.telefono,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                S.fechaServicio, 
                S.tipoServicio, 
                S.horaServicio,
                S.telefonoContacto,
                SE.fechaEvento,
                SE.horaEvento,
                SE.lugarEvento,
                EM.nombresEmpleado,
                EM.apellidosEmpleado,
                EM.direccionEmpleado,
                TE.descripcion as TipoEmpleado,
                PL.cantidad,
                PL.nombrePlato,
                PL.descripcion as descripcionPlato,
                PL.precioPlato,
                TP.descripcion as descripcionTipoPlato,
                PD.nombreProducto,
                PD.cantidadProducto
                from Empresas E
                inner join Presupuesto P
                on E.codigoEmpresa = P.codigoEmpresa
                inner join Servicios S
                on S.codigoEmpresa = E.codigoEmpresa
                inner join Servicios_has_Empleados SE
                on SE.codigoServicio = S.codigoServicio
                inner join Empleados EM
                on EM.codigoEmpleado = SE.codigoEmpleado
                inner join TipoEmpleado TE
                on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado
                inner join Servicios_has_Platos SP
                on SP.codigoServicio = S.codigoServicio
                inner join Platos PL
                on PL.codigoPlato = SP.codigoPlato
                inner join TipoPlatos TP
                on TP.codigoTipoPlato = PL.codigoTipoPlato
                inner join Productos_has_Platos PP
                on PP.codigoPlato = PL.codigoPlato
                inner join Productos PD
                on PD.codigoProducto = PP.codigoProducto
                where E.codigoEmpresa = 10;
                
#ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password by 'kinalless';

-- ----------------------------------------- Consulta de Presupuesto ------------------------------------------------------------------

Delimiter $$
	create procedure sp_ReportePresupuesto(in codEmpresa int)
		begin
			select E.nombreEmpresa, 
				E.direccion, 	
                E.telefono,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio
                from Empresas E	
                inner join Presupuesto P
					on E.codigoEmpresa = P.codigoEmpresa
				inner join Servicios S
					on E.codigoEmpresa = S.codigoEmpresa
						where E.codigoEmpresa = codEmpresa;
				
        end$$
Delimiter ;

call sp_ReportePresupuesto(1);

-- ------------------ Consulta Reporte Servicio --------------------------------
select * , E.nombreEmpresa from Servicios S
	inner join Empresas E
		on S.codigoEmpresa = E.codigoEmpresa;