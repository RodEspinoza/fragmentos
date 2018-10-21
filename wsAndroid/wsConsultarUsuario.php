<?php

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json=array();

if(isset($_POST["email"])&&isset($_POST["pass"])){

	$email = $_POST["email"];
	$pass = $_POST["pass"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$select = "SELECT id FROM usuario WHERE email = {$email} AND pass = {$pass}";
	$resultado_select = mysqli_query($conexion,$select);
	
	if($registro = mysqli_fetch_array($resultado_select)){
		$json['id_usuario'][] = $registro;
	} else {
		echo "".mysqli_error($conexion);
	}


} else {

}

?>