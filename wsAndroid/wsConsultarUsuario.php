<?php

$hostname_localhost = "localhost:3306";
$database_localhost = "bd_gestor_pedidos";
$username_locahost = "root";
$password_localhost = "siempretropical";

$json=array();


if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["fecha"])){

	$email = $_POST["email"];
	$pass = $_POST["pass"];
	$fecha = $_POST["fecha"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$insert = "INSERT INTO usuario(email,pass,fecha) VALUES('{$email}','{$pass}','{$fecha}')";
	$resultado_insert = mysqli_query($conexion,$insert);

	$last_id = mysqli_insert_id($conexion);

	if($resultado_insert){

		$consulta = "SELECT * FROM usuario where id={$last_id}";
		$resultado = mysqli_query($conexion,$consulta);

		if($registro = mysqli_fetch_array($resultado)){
			$json['usuario'][] = $registro;
		}
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$result["id"]=0;
		$json['usuario'][] = $result;
		echo json_encode($json);
	}

}


else{

}

?>
