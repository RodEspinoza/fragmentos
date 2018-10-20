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

$insert = "INSERT INTO usuario(email,pass,fecha) VALUES('{$email}','{$pass}','{$fecha}')";

if (mysqli_query($conexion,$insert)){
	$last_id = mysqli_insert_id($conexion);
	//echo $last_id;
	$json['id_usuario'] = $last_id;

} else {
	$json['id_usuario'] = '0';
}

echo json_encode($json);
mysqli_close($conexion);

?>