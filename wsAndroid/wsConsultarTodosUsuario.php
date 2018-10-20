<?php

$hostname_localhost = "localhost";
$database_localhost = "bd_usuarios";
$username_locahost = "root";
$password_localhost = "";

$json=array();



	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$consulta = "SELECT * FROM usuario";


	$resultado=mysqli_query($conexion,$consulta);

	while($registro=mysqli_fetch_array($resultado)){
		$json['usuario'][] = $registro;
	}


	mysqli_close($conexion);
	echo json_encode($json);

			


?>