<?php

$hostname_localhost = "localhost";
$database_localhost = "bd_usuarios";
$username_locahost = "root";
$password_localhost = "";

$json=array();


if(isset($_POST["nombre"])&&isset($_POST["apellido"])&&isset($_POST["rut"])){

	$nombre=$_POST["nombre"];
	$apellido=$_POST["apellido"];
	$rut=$_POST["rut"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);

	$stmt = $conexion->prepare("UPDATE usuario SET nombre = '{$nombre}',apellido='{$apellido}' WHERE rut = '{$rut}'");
	$stmt->execute();

	//filas afectadas
	$nrows = $stmt->affected_rows;

	if(!$nrows){
		echo 'noActualizado';
	}
	else{
		echo 'actualizado';
	}

}

?>