<?php

$hostname_localhost = "localhost";
$database_localhost = "bd_usuarios";
$username_locahost = "root";
$password_localhost = "";

$json=array();


if(isset($_GET["nombre"])&&isset($_GET["apellido"])&&isset($_GET["rut"])){

	$nombre=$_GET["nombre"];
	$apellido=$_GET["apellido"];
	$rut=$_GET["rut"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$insert = "INSERT INTO usuario(nombre,apellido,rut) VALUES('{$nombre}','{$apellido}','{$rut}')";
	$resultado_insert=mysqli_query($conexion,$insert);

	$last_id = mysqli_insert_id($conexion);

	if($resultado_insert){

		$consulta = "SELECT * FROM usuario where id={$last_id}";
		$resultado = mysqli_query($conexion,$consulta);

		if($registro=mysqli_fetch_array($resultado)){
			$json['usuario'][] = $registro;

		}
		mysqli_close($conexion);
		echo json_encode($json);


	}
	else{
		$result["rut"]=0;
		$json['usuario'][] = $result;
		echo json_encode($json);
	}

}


else{

}

?>
