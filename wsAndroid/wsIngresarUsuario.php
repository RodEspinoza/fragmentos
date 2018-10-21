<?php

header('Content-Type: application/json');

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json=array();

$body = json_decode(file_get_contents("php://input"), true);

$email = $_POST['email'];
$pass = $_POST['pass'];
$fecha = $_POST['fecha'];

$conexion = mysqli_connect($hostname_localhost, $username_locahost, $password_localhost, $database_localhost);

if(mysqli_connect_errno){
	echo "".mysqli_connect_error();
}

$insert = "INSERT INTO usuario(email, pass, fecha) VALUES ('{$email}','{$pass}','{$fecha}')";

if (mysqli_query($conexion,$insert)){
	$last_id = mysqli_insert_id($conexion);
	$consulta = "SELECT id FROM usuario where id = {$last_id}";
	$resultado = mysqli_query($conexion,$consulta);

	if($registro = mysqli_fetch_array($resultado)){
		$json['id_usuario'] = $registro;
	}
	echo json_encode($json);
} else {
	echo "".mysqli_error($conexion);
}
mysqli_close($conexion);
?>