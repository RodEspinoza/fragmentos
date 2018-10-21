<?php

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json=array();

$body = json_decode(file_get_contents("php://input"), true);

$email = $_POST["email"];
$pass = $_POST["pass"];

$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
$query = "SELECT id FROM usuario WHERE email = '{$email}' AND pass = '{$pass}'";

$result = mysqli_query($conexion, $query);

if ($result){
	$registro=mysqli_fetch_array($result);
	$json['id_usuario'][]= $registro;
	echo json_encode($json);
} else {
	echo "".mysqli_error($conexion);
}
mysqli_close($conexion);
?>