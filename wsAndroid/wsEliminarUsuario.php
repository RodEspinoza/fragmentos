<?php

$hostname_localhost = "localhost";
$database_localhost = "bd_usuarios";
$username_locahost = "root";
$password_localhost = "";

$json=array();


if(isset($_POST["rut"])){

	$rut=$_POST["rut"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);

	$stmt = $conexion->prepare("DELETE from usuario WHERE rut = '{$rut}'");
	$stmt->execute();

	//filas afectadas
	$nrows = $stmt->affected_rows;

	if(!$nrows){
		echo 'noEliminado';
	}
	else{
		echo 'eliminado';
	}

}

?>
