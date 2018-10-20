<?php

$hostname_localhost = "localhost";
$database_localhost = "bd_usuarios";
$username_locahost = "root";
$password_localhost = "";

$json=array();


if(isset($_GET["rut"])){

	$rut=$_GET["rut"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$consulta = "SELECT * FROM usuario where rut='{$rut}'";


	$resultado=mysqli_query($conexion,$consulta);

	if($registro=mysqli_fetch_array($resultado)){
			$json['usuario'][] = $registro;
	}
	else
	{
		$result["rut"]=0;
		$json['usuario'][] = $result;
		echo json_encode($json);
	}


	mysqli_close($conexion);
	echo json_encode($json);

			
	}

?>