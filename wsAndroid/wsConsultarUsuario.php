<?php

$hostname_localhost = "localhost:3306";
$database_localhost = "bd_gestor_pedidos";
$username_locahost = "root";
$password_localhost = "siempretropical";

$json=array();


if(isset($_POST["email"])&&isset($_POST["pass"])){

	$email = $_POST["email"];
	$pass = $_POST["pass"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
	$select = "SELECT id FROM usuario WHERE email = {$email} AND pass = {$pass}";
	$resultado_select = mysqli_query($conexion,$insert);

	if ($resultado_select) {

		$json['id_usuario'][] = $resultado_select;
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		echo "Error: " . mysqli_error($conexion);
	}

} else {

}

?>