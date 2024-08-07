/*
Nombre: Jorge Abraham Revolorio Mazariegos
Código Técnico: IN5BM
Carné: 2019058
Fecha de Creación: 28-03-2023
Fecha de Modificación: 04-04-2023
Fecha de Modificación: 09-04-2023
Fecha de Modificación: 10-04-2023
*/

Drop database if exists DBTonysKinal2023_2019058;
Create database DBTonysKinal2023_2019058;

Use DBTonysKinal2023_2019058;

Create table Empresas(

	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null,
	direccion varchar(150) not null,
    telefono varchar(150) not null,
    primary key PK_codigoEmpresa (codigoEmpresa)

);

Create table TipoEmpleado(

	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)

);

Create table Empleados(

	codigoEmpleado int auto_increment not null,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(10) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade

);

Create table TipoPlato(

	codigoTipoPlato int auto_increment not null,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)

);

Create table Productos(

	codigoProducto int not null,
    nombreProducto varchar(150) not null,
    cantidad int not null,
    primary key PK_codigoProducto (codigoProducto)

);

Create table Servicios(

	codigoServicio int auto_increment not null,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(10) not null,
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa) on delete cascade

);

Create table Presupuestos(

	codigoPresupuesto int auto_increment not null,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal (10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresa foreign key (codigoEmpresa)
		references Empresas (codigoEmpresa) on delete cascade

);

Create table Platos(

	codigoPlato int auto_increment not null,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal (10,2) not null,
	codigoTipoPlato int not null,
    -- TipoPlato codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato1 foreign key (codigoTipoPlato)
		references TipoPlato (codigoTipoPlato) on delete cascade

);

Create table Productos_has_Platos(

	Producto_CodigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_CodigoProducto(Producto_CodigoProducto),
    constraint FK_Productos_has_Platos_Productos1 foreign key(codigoProducto)
		references Productos(codigoProducto) on delete cascade,
	constraint FK_Productos_has_Platos_Platos1 foreign key (codigoPlato)
		references Platos(codigoPlato) on delete cascade

);

Create table Servicios_has_Platos(

	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios1 foreign key (codigoServicio)
		references Servicios(codigoServicio) on delete cascade,
	constraint FK_Servicios_has_Platos1 foreign key (codigoPlato)
		references Platos(codigoPlato) on delete cascade

);

Create table Servicios_has_Empleados(

	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicio1 foreign key (codigoServicio)
		references Servicios(codigoServicio) on delete cascade,
	constraint FK_Servicios_has_Empleados_Empleados1 foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado) on delete cascade

);

Create table Roles(
	codigoRol int not null auto_increment,
    nombreRol varchar(25),
    primary key PK_codigoRol (codigoRol)
);

Delimiter $$
	Create procedure sp_AgregarRole(in nombreRol varchar(25))
		Begin
			Insert into Roles(nombreRol)
				values(nombreRol);
        End$$
Delimiter ;

Delimiter $$
	Create procedure sp_ListarRoles()
		Begin
			Select
				R.codigoRol,
                R.nombreRol
                from Roles R;
        End$$
Delimiter ;

call sp_AgregarRole("Jefe");
call sp_AgregarRole("Soporte");
call sp_AgregarRole("Empleados");

call sp_ListarRoles();

Delimiter $$
	Create procedure sp_buscarRol(in codigo_Rol int)
		Begin
			Select
				R.codigoRol,
                R.nombreRol
                from Roles R
					where R.codigoRol = codigo_Rol;
        End$$
Delimiter ;

call sp_buscarRol(3);

Delimiter $$
	Create procedure sp_EditarRol(in codigo_Rol int, in nombre_Rol varchar(25))
		Begin
			Update Roles R
				set R.nombreRol = nombre_Rol
					where R.codigoRol = codigo_Rol;
        End$$
Delimiter ;

call sp_EditarRol(3, "Empleado");

Delimiter $$
	Create procedure sp_EliminarRol(in codigo_Rol int)
		Begin
			Delete from Roles
				where codigoRol = codigo_Rol;
        End$$
Delimiter ;

-- call sp_EliminarRol(3);

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    clave varchar(50) not null,
    codigoRol int,
    primary key PK_codigoUsuario (codigoUsuario),
    constraint FK_Usuarios_Roles foreign key (codigoRol)
		references Roles(codigoRol) on delete cascade
);

Delimiter $$
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in clave varchar(50), in codigoRol int)
		Begin
			Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, clave, codigoRol)
				values(nombreUsuario, apellidoUsuario, usuarioLogin, clave, codigoRol);
        End$$
Delimiter ;

Delimiter $$
	Create procedure sp_ListarUsuarios()
		Begin
			Select
				U.codigoUsuario,
                U.nombreUsuario,
                U.apellidoUsuario,
                U.usuarioLogin,
                U.clave,
                U.codigoRol,
                R.nombreRol
                from Usuario U
					Inner Join Roles R on U.codigoRol = R.codigoRol;
        End$$
Delimiter ;

Delimiter $$
	Create procedure sp_EditarUsuario(in codigo_Usuario int, in nombre_Usuario varchar(100), in apellido_Usuario varchar(100), in usuario_Login varchar(50), 
										in clave_ varchar(50), in codigo_Rol int)
		Begin
			Update Usuario U
				set U.nombreUsuario = nombre_Usuario, U.apellidoUsuario = apellido_Usuario, U.usuarioLogin = usuario_Login, U.clave = clave_, U.codigoRol = codigo_Rol
					where U.codigoUsuario = codigo_Usuario;
        End$$
Delimiter ;

Delimiter $$
	Create procedure sp_EliminarUsuario(in codigo_Usuario int)
		Begin
			Delete from Usuario 
				where codigoUsuario = codigo_Usuario;
        End$$
Delimiter ;

call sp_AgregarUsuario('Fernando', 'Lopez', 'flopez', 'jefe', 1);
call sp_AgregarUsuario('Jorge', 'Revolorio', 'jrevolorio', 'admin', 2);
call sp_ListarUsuarios();

call sp_EditarUsuario(1, 'Pedro', 'Armas', 'parmas', 'jefe', 1);

-- call sp_EliminarUsuario(1);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);

-- -------------------- Stored Procedures ------------------------

-- -------------------- Entidad Empresas -------------------------

-- --------------------     Agregar      -------------------------

Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), in direccion varchar(150), in telefono varchar(150))
		Begin
			Insert into Empresas (nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono);
        End$$
Delimiter ;

call sp_AgregarEmpresa('McDonalds', 'Puerto Barrios', '89574125');
call sp_AgregarEmpresa('Taco Bell', 'Guatemala', '63201574');
call sp_AgregarEmpresa('Panda', 'Mixco', '46507842');

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarEmpresas()
		Begin
			Select
				E.codigoEmpresa, 
                E.nombreEmpresa, 
                E.direccion, 
                E.telefono
                from Empresas E;
        End$$
Delimiter ;

call sp_ListarEmpresas();

-- ----------------------    Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarEmpresa(in codigo_Empresa int)
		Begin
			Select
				E.codigoEmpresa, 
                E.nombreEmpresa, 
                E.direccion, 
                E.telefono
                from Empresas E
					where E.codigoEmpresa = codigo_Empresa;
        End$$
Delimiter ;

call sp_BuscarEmpresa(2);

-- ---------------------    Editar   ---------------------------

Delimiter $$
	Create procedure sp_EditarEmpresa(in codigo_Empresa int, in nombre_Empresa varchar(150), direccion_ varchar(150),
										in telefono_ varchar(150))
		Begin
			Update Empresas E
				set E.nombreEmpresa = nombre_Empresa, E.direccion = direccion_, E.telefono = telefono_
					where E.codigoEmpresa = codigo_Empresa;
        End$$
Delimiter ;

call sp_EditarEmpresa(1, 'Burger King', 'Baja Verapaz', '57698421');

-- --------------------  Eliminar     ---------------------------

Delimiter $$
	Create procedure sp_EliminarEmpresa(in codigo_Empresa int)
		Begin
			Delete from Empresas 
				where codigoEmpresa = codigo_Empresa;
        End$$
Delimiter ;

-- call sp_EliminarEmpresa(1);

-- -------------------- Entidad Tipo Emplado ---------------------

-- --------------------     Agregar      -------------------------

Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin
			Insert into TipoEmpleado (descripcion)
				values (descripcion);
		End$$
Delimiter ;

call sp_AgregarTipoEmpleado('Supervisa platillos.');
call sp_AgregarTipoEmpleado('Ayuda a supervisar platillos.');
call sp_AgregarTipoEmpleado('Prepara platillos.');
call sp_AgregarTipoEmpleado('Lava los platos.');
call sp_AgregarTipoEmpleado('Toma ordenes y sirve platillos.');

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarTipoEmpleados()
		Begin
			Select
				TE.codigoTipoEmpleado,
                TE.descripcion
                from TipoEmpleado TE;
        End$$
Delimiter ;

call sp_ListarTipoEmpleados();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarTipoEmpleado(in codigo_TipoEmpleado int)
		Begin
			Select
				TE.codigoTipoEmpleado,
                TE.descripcion
                from TipoEmpleado TE
					where TE.codigoTipoEmpleado = codigo_TipoEmpleado;
        End$$
Delimiter ;

call sp_BuscarTipoEmpleado(3);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codigo_TipoEmpleado int, in descripcion_ varchar(50))
		Begin
			Update TipoEmpleado TE
				set TE.descripcion = descripcion_
					where TE.codigoTipoEmpleado = codigo_TipoEmpleado;
        End$$
Delimiter ;

call sp_EditarTipoEmpleado(4, 'Sanitiza los platos.');

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codigo_TipoEmpleado int)
		Begin
			Delete from TipoEmpleado 
				where codigoTipoEmpleado = codigo_TipoEmpleado;
        End$$
Delimiter ;

-- call sp_EliminarTipoEmpleado(1);

-- -------------------- Entidad Emplados ---------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarEmpleado(in numeroEmpleado int, in apellidosEmpleado varchar(150), in nombresEmpleado varchar(150),
											in direccionEmpleado varchar(150), in telefonoContacto varchar(10), in gradoCocinero varchar(50),
												in codigoTipoEmpleado int)
		Begin
			Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
										telefonoContacto, gradoCocinero, codigoTipoEmpleado)
				values(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, 
							gradoCocinero, codigoTipoEmpleado);
        End$$
Delimiter ;

call sp_AgregarEmpleado(1, "Lopez Fernandez", "Marta Alexandra", "15 Avenida A 6-32, Zona 10", "58963201", "Grado Cinco", 1);
call sp_AgregarEmpleado(5, "Perez Gonzalez", "Carlos Estuardo", "4a. Calle B 12-26, Zona 3", "57896421", "Grado Cuatro", 2);
call sp_AgregarEmpleado(20, "Rodriguez Ramirez", "Fernando Joaquín", "10a. Avenida 8-18, Zona 1", "24589674", "Grado Tres", 3);
call sp_AgregarEmpleado(100, "García Posadas", "Diego Carlos", "13a. Calle 22-12, Colonia El Milagro", "78216453", "Grado Dos", 2);
call sp_AgregarEmpleado(200, "Juarez Lopez", "Abner", "7a. Avenida C 15-27, Zona 7", "87642018", "Sin Grado Cocina", 1);
call sp_AgregarEmpleado(300, "Humberto", "Mario", "Zona 4 Calle El Salvador", "89642013", "Sin Grado Cocina", 3);


-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarEmpleados()
		Begin
			Select
				ES.codigoEmpleado, 
                ES.numeroEmpleado, 
                ES.nombresEmpleado, 
                ES.apellidosEmpleado, 
                ES.direccionEmpleado, 
                ES.telefonoContacto, 
                ES.gradoCocinero, 
                ES.codigoTipoEmpleado,
                TE.descripcion
                from Empleados ES
					Inner Join TipoEmpleado TE on ES.codigoTipoEmpleado = TE.codigoTipoEmpleado;
        End$$
Delimiter ;

call sp_ListarEmpleados();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarEmpleado(in codigo_Empleado int)
		Begin
			Select
				ES.codigoEmpleado, 
                ES.numeroEmpleado, 
                ES.nombresEmpleado, 
                ES.apellidosEmpleado, 
                ES.direccionEmpleado, 
                ES.telefonoContacto, 
                ES.gradoCocinero, 
                ES.codigoTipoEmpleado,
                TE.descripcion
                from Empleados ES
					Inner Join TipoEmpleado TE on ES.codigoTipoEmpleado = TE.codigoTipoEmpleado
						where ES.codigoEmpleado = codigo_Empleado;
        End$$
Delimiter ;

call sp_BuscarEmpleado(4);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarEmpleado(in codigo_Empleado int, in numero_Empleado int, in apellidos_Empleado varchar(150), 
											in nombres_Empleado varchar(150),in direccion_Empleado varchar(150), 
												in telefono_Contacto varchar(10), in grado_Cocinero varchar(50),in codigo_TipoEmpleado int)
		Begin
			Update Empleados ES
				set ES.numeroEmpleado = numero_Empleado, ES.apellidosEmpleado = apellidos_Empleado, ES.nombresEmpleado = nombres_Empleado,
						ES.direccionEmpleado = direccion_Empleado, ES.telefonoContacto = telefono_Contacto, ES.gradoCocinero = grado_Cocinero,
							ES.codigoTipoEmpleado = codigo_TipoEmpleado
					where ES.codigoEmpleado = codigo_Empleado;
        End$$
Delimiter ;

call sp_EditarEmpleado(4, 150, "García Posadas", "Diego Carlos", "13a. Calle 22-12, Colonia Milagroso Dios", "78217453", "Grado Uno", 2);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarEmpleado(in codigo_Empleado int)
		Begin
			Delete from Empleados 
				where codigoEmpleado = codigo_Empleado;
        End$$
Delimiter ;

-- call sp_EliminarEmpleado(5);

-- -------------------- Entidad Tipo Plato ---------------------

-- --------------------     Agregar      ----------------------


Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
		Begin
			Insert into TipoPlato(descripcionTipo)
				values(descripcionTipo);
        End$$
Delimiter ;

call sp_AgregarTipoPlato('Plato Fuerte');
call sp_AgregarTipoPlato('Guarnición');
call sp_AgregarTipoPlato('Sopa');
call sp_AgregarTipoPlato('Ensalada');
call sp_AgregarTipoPlato('Postres');

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarTipoPlatos()
		Begin
			Select
				TP.codigoTipoPlato, 
                TP.descripcionTipo
                from TipoPlato TP;
        End$$
Delimiter ;

call sp_ListarTipoPlatos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codigo_TipoPlato int)
		Begin
			Select
				TP.codigoTipoPlato, 
				TP.descripcionTipo
				from TipoPlato TP
					where TP.codigoTipoPlato = codigo_TipoPlato;
        End$$
Delimiter ;

call sp_BuscarTipoPlato(2);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarTipoPlato(in codigo_TipoPlato int, in descripcion_Tipo varchar(100))
		Begin
			Update TipoPlato TP
				set TP.descripcionTipo = descripcion_Tipo
					where TP.codigoTipoPlato = codigo_TipoPlato;
        End$$
Delimiter ;

call sp_EditarTipoPlato(5, 'Postre');

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codigo_TipoPlato int)
		Begin
			Delete from TipoPlato 
				where codigoTipoPlato = codigo_TipoPlato;
        End$$
Delimiter ; 

-- call sp_EliminarTipoPlato(1);

-- -------------------- Entidad Productos ---------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarProducto(in codigoProducto int, in nombreProducto varchar(150), in cantidad int)
		Begin
			Insert into Productos(codigoProducto, nombreProducto, cantidad)
				values(codigoProducto, nombreProducto, cantidad);
        End$$
Delimiter ;

call sp_AgregarProducto(1000, 'Bolsa de Frijol', 505);
call sp_AgregarProducto(1001, 'Red de Pescado', 250);
call sp_AgregarProducto(1002, 'Bolsa de Arroz', 500);
call sp_AgregarProducto(1003, 'Caja de Queso Mozarella', 850);

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarProductos()
		Begin
			Select
				P.codigoProducto,
                P.nombreProducto,
                P.cantidad
                from Productos P;
        End$$
Delimiter ;

call sp_ListarProductos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarProductos(in codigo_Producto int)
		Begin
			Select
				P.codigoProducto,
                P.nombreProducto,
                P.cantidad
                from Productos P
					where P.codigoProducto = codigo_Producto;
        End$$
Delimiter ;

call sp_BuscarProductos(1002);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarProducto(in codigo_Producto int, in nombre_Producto varchar(150), in cantidad_ int)
		Begin
			Update Productos P
				set P.nombreProducto = nombre_Producto, P.cantidad = cantidad_
					where P.codigoProducto = codigo_Producto;
        End$$
Delimiter ;

call sp_EditarProducto(1003, 'Caja de Chocolate', 650);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarProducto(in codigo_Producto int)
		Begin
			Delete from Productos 
				where codigoProducto = codigo_Producto;
        End$$
Delimiter ;

 -- call sp_EliminarProducto(1004);

-- -------------------- Entidad Servicios ---------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(150), in horaServicio time, in lugarServicio varchar(150),
											in telefonoContacto varchar(10), in codigoEmpresa int)
		Begin
			Insert into Servicios (codigoServicio, fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values(codigoServicio, fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
        End$$
Delimiter ;

call sp_AgregarServicio('2019-05-28', 'Chef', '8:00:00', 'Cocina', '86475201', 1);
call sp_AgregarServicio('2015-04-12', 'Cocinero', '9:30:00', 'Cocina', '97643154', 2);
call sp_AgregarServicio('2022-06-12', 'Repartidor', now(), 'Comedir', '97542013', 1);


-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarServicios()
		Begin
			Select
				S.codigoServicio, 
                S.fechaServicio, 
                S.tipoServicio, 
                S.horaServicio, 
                S.lugarServicio, 
                S.telefonoContacto, 
                S.codigoEmpresa
                from Servicios S;
        End$$
Delimiter ;

call sp_ListarServicios();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarServicio(in codigo_Servicio int)
		Begin
			Select
				S.codigoServicio, 
                S.fechaServicio, 
                S.tipoServicio, 
                S.horaServicio, 
                S.lugarServicio, 
                S.telefonoContacto, 
                S.codigoEmpresa
                from Servicios S
					where S.codigoServicio = codigo_Servicio;
        End$$
Delimiter ;

call sp_BuscarServicio(2);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarServicio(in codigo_Servicio int, in fecha_Servicio date, in tipo_Servicio varchar(150), in hora_Servicio time,
											in lugar_Servicio varchar(150), in telefono_Contacto varchar(10), in codigo_Empresa int)
		Begin
			Update Servicios S
				set S.fechaServicio = fecha_Servicio, S.tipoServicio = tipo_Servicio, S.horaServicio = hora_Servicio, S.lugarServicio = lugar_Servicio,
					S.telefonoContacto = telefono_Contacto, S.codigoEmpresa = codigo_Empresa
						where S.codigoServicio = codigo_Servicio;
        End$$
Delimiter ;

call sp_EditarServicio(3, '2022-06-12', 'Repartidor', '12:39:15', 'Comedor', '97557013', 2);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarServicio(in codigo_Servicio int)
		Begin
			Delete from Servicios 
				where codigoServicio = codigo_Servicio;
        End$$
Delimiter ;

-- call sp_EliminarServicio(1);

-- -------------------- Entidad Presupuestos ------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
		Begin
			Insert into Presupuestos(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
        End$$
Delimiter ;

call sp_AgregarPresupuesto(now(), 250000.00, 1);
call sp_AgregarPresupuesto('2022-04-12', 500000.50, 2);
call sp_AgregarPresupuesto('2023-04-11', 750000.00, 3);

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarPresupuestos()
		Begin
			Select
				PR.codigoPresupuesto, 
                PR.fechaSolicitud, 
                PR.cantidadPresupuesto, 
                PR.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono
                from Presupuestos PR
					Inner join Empresas E on PR.codigoEmpresa = E.codigoEmpresa;
        End$$
Delimiter ;

call sp_ListarPresupuestos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarPresupuesto(in codigo_Presupuesto int)
		Begin
			Select
				PR.codigoPresupuesto, 
                PR.fechaSolicitud, 
                PR.cantidadPresupuesto, 
                PR.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono
                from Presupuestos PR
					Inner join Empresas E on PR.codigoEmpresa = E.codigoEmpresa
						where PR.codigoPresupuesto = codigo_Presupuesto;
        End$$
Delimiter ;

call sp_BuscarPresupuesto(2);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarPresupuesto(in codigo_Presupuesto int, in fecha_Solicitud date, in cantidad_Presupuesto decimal(10,2), in codigo_Empresa int)
		Begin
			Update Presupuestos PR
				set PR.fechaSolicitud = fecha_Solicitud, PR.cantidadPresupuesto = cantidad_Presupuesto, PR.codigoEmpresa = codigo_Empresa
					where PR.codigoPresupuesto = codigo_Presupuesto;
        End$$
Delimiter ;

call sp_EditarPresupuesto(2, '2020-07-04', 7520152.00, 2);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarPresupuesto(in codigo_Presupuesto int)
		Begin
			Delete from Presupuestos 
				where codigoPresupuesto = codigo_Presupuesto;
        End$$
Delimiter ;

-- call sp_EliminarPresupuesto(1);

-- -------------------- Entidad Platos ------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarPlato(in cantidad int, in nombrePlato varchar(50), in descripcionPlato varchar(150), in precioPlato decimal (10,2),
										in codigoTipoPlato int)
		Begin
			Insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				values(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
        End$$
Delimiter ;

call sp_AgregarPlato(2, 'Suchi', 'Comida Japonesa que lleva arroz, camaron o pescado, vinagre y azucar todo eso combinado en una alga.', 58.00, 1);
call sp_AgregarPlato(1, 'MC Wrapp', 'Una tortilla de harina con lechuga, tomate, salsas y carne o pollo dentro.', 48.00, 1);
call sp_AgregarPlato(4, 'Sopa de Frijol', 'Sopa con Frijol, cebolla y arroz dentro.', 10.00, 3);

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarPlatos()
		Begin
			Select
				PL.codigoPlato, 
                PL.cantidad, 
                PL.nombrePlato, 
                PL.descripcionPlato, 
                PL.precioPlato, 
                PL.codigoTipoPlato,
                TP.descripcionTipo
                from Platos PL
					Inner join TipoPlato TP on PL.codigoTipoPlato = TP.codigoTipoPlato;
        End$$
Delimiter ;

call sp_ListarPlatos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarPlato(in codigo_Plato int)
		Begin
			Select
				PL.codigoPlato, 
                PL.cantidad, 
                PL.nombrePlato, 
                PL.descripcionPlato, 
                PL.precioPlato, 
                PL.codigoTipoPlato,
                TP.descripcionTipo
                from Platos PL
					Inner join TipoPlato TP on PL.codigoTipoPlato = TP.codigoTipoPlato
						where PL.codigoPlato = codigo_Plato;
        End$$
Delimiter ;

call sp_BuscarPlato(3);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarPlato(in codigo_Plato int, in cantidad_ int, in nombre_Plato varchar(50), in descripcion_Plato varchar(150),
										in precio_Plato decimal(10,2), in codigo_TipoPlato int)
		Begin
			Update Platos PL
				set PL.cantidad = cantidad_, PL.nombrePlato = nombre_Plato, PL.descripcionPlato = descripcion_Plato, PL.precioPlato = precio_Plato,
					PL.codigoTipoPlato = codigo_TipoPlato
                    where PL.codigoPlato = codigo_Plato;
        End$$
Delimiter ;

call sp_EditarPlato(2 ,2, 'Volcan', 'Una masa de chocolate con chocolate deretido dentro y una bola de helado encima.', 30.00, 5);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarPlato(in codigo_Plato int)
		Begin
			Delete from Platos 
				where codigoPlato = codigo_Plato;
        End$$
Delimiter ;

-- call sp_EliminarPlato(1);

-- -------------------- Entidad Productos Has Platos ----------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarProducto_Has_Plato(in Producto_CodigoProducto int, in codigoPlato int, in codigoProducto int)
		Begin
			Insert into Productos_Has_Platos (Producto_CodigoProducto, codigoPlato, codigoProducto)
				values(Producto_CodigoProducto, codigoPlato, codigoProducto);
        End$$
Delimiter ;

call sp_AgregarProducto_Has_Plato(5000, 1, 1001);
call sp_AgregarProducto_Has_Plato(5001, 2, 1003);
call sp_AgregarProducto_Has_Plato(5002, 3, 1002);

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarProductos_Has_Platos()
		Begin
			Select
				PHP.Producto_CodigoProducto, 
				PHP.codigoPlato, 
                PL.nombrePlato,
                PL.precioPlato,
				PHP.codigoProducto,
                PR.nombreProducto
                from Productos_Has_Platos PHP
					Inner Join Platos PL on PHP.codigoPlato = PL.codigoPlato
						Inner Join Productos PR on PHP.codigoProducto = PR.codigoProducto;
        End$$
Delimiter ;

call sp_ListarProductos_Has_Platos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarProducto_Has_Plato(in Producto_Codigo_Producto int)
		Begin
			Select
				PHP.Producto_CodigoProducto, 
				PHP.codigoPlato, 
                PL.nombrePlato,
                PL.precioPlato,
				PHP.codigoProducto,
                PR.nombreProducto
                from Productos_Has_Platos PHP
					Inner Join Platos PL on PHP.codigoPlato = PL.codigoPlato
						Inner Join Productos PR on PHP.codigoProducto = PR.codigoProducto
							where PHP.Producto_CodigoProducto = Producto_Codigo_Producto;
        End$$
Delimiter ;

call sp_BuscarProducto_Has_Plato(5001);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarProducto_Has_Plato(in Producto_Codigo_Producto int, in codigo_Plato int, in codigo_Producto int)
		Begin
			Update Productos_Has_Platos PHP
				set PHP.codigoPlato = codigo_Plato, PHP.codigoProducto = codigo_Producto
					where PHP.Producto_CodigoProducto = Producto_Codigo_Producto;
        End$$
Delimiter ;

call sp_EditarProducto_Has_Plato(5002, 3, 1000);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarProducto_Has_Plato(in Producto_Codigo_Producto int)
		Begin
			Delete from Productos_Has_Platos 
				where Producto_CodigoProducto = Producto_Codigo_Producto;
        End$$
Delimiter ;

-- call sp_EliminarProducto_Has_Plato(5000);

-- -------------------- Entidad Servicios Has Platos ----------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarServicio_Has_Plato(in Servicios_codigoServicio int, in codigoPlato int, in codigoServicio int)
		Begin
			Insert into Servicios_Has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
				values(Servicios_codigoServicio, codigoPlato, codigoServicio);
        End$$
Delimiter ;

call sp_AgregarServicio_Has_Plato(10000, 1, 1);
call sp_AgregarServicio_Has_Plato(10001, 2, 2);
call sp_AgregarServicio_Has_Plato(10002, 3, 2);

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarServicios_Has_Platos()
		Begin
			Select
				SHP.Servicios_codigoServicio, 
                SHP.codigoPlato, 
                PL.nombrePlato,
                PL.cantidad,
                PL.precioPlato,
                SHP.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto
                from Servicios_Has_Platos SHP
					Inner Join Platos PL on SHP.codigoPlato = PL.codigoPlato
						Inner Join Servicios S on SHP.codigoServicio = S.codigoServicio;
        End$$
Delimiter ;

call sp_ListarServicios_Has_Platos();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarServicio_Has_Plato(in Servicios_codigo_Servicio int)
		Begin
			Select
				SHP.Servicios_codigoServicio, 
                SHP.codigoPlato, 
                PL.nombrePlato,
                PL.cantidad,
                PL.precioPlato,
                SHP.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto
                from Servicios_Has_Platos SHP
					Inner Join Platos PL on SHP.codigoPlato = PL.codigoPlato
						Inner Join Servicios S on SHP.codigoServicio = S.codigoServicio
							where SHP.Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

call sp_BuscarServicio_Has_Plato(10002);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarServicio_Has_Plato(in Servicios_codigo_Servicio int, in codigo_Plato int, in codigo_Servicio int)
		Begin
			Update Servicios_Has_Platos SHP
				set SHP.codigoPlato = codigo_Plato, SHP.codigoServicio = codigo_Servicio
					where SHP.Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

call sp_EditarServicio_Has_Plato (10002, 3, 3);

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarServicio_Has_Plato(in Servicios_codigo_Servicio int)
		Begin
			Delete from Servicios_Has_Platos 
				where Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

-- call sp_EliminarServicio_Has_Plato(10000);

-- -------------------- Entidad Servicios Has Empleados -------------------------

-- --------------------     Agregar      ----------------------

Delimiter $$
	Create procedure sp_AgregarServicio_Has_Empleado(in Servicios_codigoServicio int, in codigoServicio int, in codigoEmpleado int,
														in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
		Begin
			Insert into Servicios_Has_Empleados (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End$$
Delimiter ;

call sp_AgregarServicio_Has_Empleado(1000500, 1, 1, '2022-09-23', now(), 'Club Campestre la Montaña');
call sp_AgregarServicio_Has_Empleado(1000501, 2, 3, now(), '18:00:00', 'Antigua Hills');
call sp_AgregarServicio_Has_Empleado(1000502, 3, 5, '2023-01-01', '00:00:00', 'Hotel Tikal');

-- --------------------     Mostrar      ----------------------

Delimiter $$
	Create procedure sp_ListarServicios_Has_Empleados()
		Begin
			Select
				SHE.Servicios_codigoServicio, 
                SHE.codigoServicio, 
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                SHE.codigoEmpleado, 
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.gradoCocinero,
                E.telefonoContacto,
                SHE.fechaEvento, 
                SHE.horaEvento, 
                SHE.lugarEvento
                from Servicios_Has_Empleados SHE
					Inner Join Servicios S on SHE.codigoServicio = S.codigoServicio
						Inner Join Empleados E on SHE.codigoEmpleado = E.codigoEmpleado;
        End$$
Delimiter ;

call sp_ListarServicios_Has_Empleados();

-- --------------------     Buscar       ----------------------

Delimiter $$
	Create procedure sp_BuscarServicio_Has_Empleado(in Servicios_codigo_Servicio int)
		Begin
			Select
				SHE.Servicios_codigoServicio, 
                SHE.codigoServicio, 
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                SHE.codigoEmpleado, 
                E.apellidosEmpleado,
                E.nombresEmpleado,
                E.gradoCocinero,
                E.telefonoContacto,
                SHE.fechaEvento, 
                SHE.horaEvento, 
                SHE.lugarEvento
                from Servicios_Has_Empleados SHE
					Inner Join Servicios S on SHE.codigoServicio = S.codigoServicio
						Inner Join Empleados E on SHE.codigoEmpleado = E.codigoEmpleado
							where SHE.Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

call sp_BuscarServicio_Has_Empleado(1000501);

-- --------------------     Editar       ----------------------

Delimiter $$
	Create procedure sp_EditarServicio_Has_Empleado(in Servicios_codigo_Servicio int, in codigo_Servicio int, in codigo_Empleado int,
														in fecha_Evento date, in hora_Evento time, in lugar_Evento varchar(150))
		Begin
			Update Servicios_Has_Empleados SHE
				set SHE.codigoServicio = codigo_Servicio, SHE.codigoEmpleado = codigo_Empleado, SHE.fechaEvento = fecha_Evento,
					SHE.horaEvento = hora_Evento, SHE.lugarEvento= lugar_Evento
						where SHE.Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

call sp_EditarServicio_Has_Empleado(1000501, 2, 3, '2023-04-12', '20:30:00', 'Antigua Hills');

-- --------------------     Eliminar     ----------------------

Delimiter $$
	Create procedure sp_EliminarServicio_Has_Empleado(in Servicios_codigo_Servicio int)
		Begin
			Delete from Servicios_Has_Empleados 
				where Servicios_codigoServicio = Servicios_codigo_Servicio;
        End$$
Delimiter ;

call sp_EliminarServicio_Has_Empleado(1000500);

Delimiter $$
	Create procedure sp_ReporteGeneral()
		Begin
			Select
				S.codigoServicio,
                S.fechaServicio,
                S.tipoServicio,
                S.horaServicio,
                S.lugarServicio,
                S.telefonoContacto,
                S.codigoEmpresa,
                E.nombreEmpresa,
                E.direccion,
                E.telefono,
                P.codigoPresupuesto,
                P.fechaSolicitud,
                P.cantidadPresupuesto,
                SHP.Servicios_codigoServicio,
                SHP.codigoPlato,
                Pl.cantidad,
                Pl.nombrePlato,
                Pl.descripcionPlato,
                Pl.precioPlato,
                Pl.codigoTipoPlato,
                TP.descripcionTipo,
                PHP.Producto_CodigoProducto,
                PHP.codigoProducto,
                PRO.nombreProducto,
                PRO.cantidad,
                SHE.Servicios_codigoServicio,
                SHE.codigoEmpleado,
                SHE.fechaEvento,
                SHE.horaEvento,
                SHE.lugarEvento,
                EM.numeroEmpleado,
                EM.apellidosEmpleado,
                EM.nombresEmpleado,
                EM.direccionEmpleado,
                EM.telefonoContacto,
                EM.gradoCocinero,
                EM.codigoTipoEmpleado,
                TE.descripcion
					from Servicios S
						Inner Join Empresas E on S.codigoEmpresa = E.codigoEmpresa
                        Inner Join Presupuestos P on E.codigoEmpresa = P.codigoEmpresa
                        Inner Join Servicios_has_Platos SHP on S.codigoServicio = SHP.codigoServicio
                        Inner Join Platos Pl on Pl.codigoPlato = SHP.codigoPlato
                        Inner Join TipoPlato TP on TP.codigoTipoPlato = Pl.codigoTipoPlato
                        Inner Join Productos_has_Platos PHP on PHP.codigoPlato = Pl.codigoPlato
                        Inner Join Productos PRO on PRO.codigoProducto = PHP.codigoProducto
                        Inner Join Servicios_has_Empleados SHE on SHE.codigoServicio = S.codigoServicio
                        Inner Join Empleados EM on EM.codigoEmpleado = SHE.codigoEmpleado
                        Inner Join TipoEmpleado TE on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado;

        End$$
Delimiter ;

call sp_ReporteGeneral();

select * from Platos;
