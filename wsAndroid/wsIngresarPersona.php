<?php

header('Content-Type: application/json');

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json=array();

$rut = $_POST['rut'];
$name = $_POST['name'];
$last_name = $_POST['last_name'];
$sexo = $_POST['sexo'];
$location = $_POST['location'];
$id_user = $_POST['id_user'];

$conexion = mysqli_connect($hostname_localhost, $username_locahost, $password_localhost, $database_localhost);

if(mysqli_connect_errno){
	echo "".mysqli_connect_error();
}

$insert = "INSERT INTO persona(rut,nombre,last_name,sexo,location,id_user) 
			VALUES('{$rut}','{$name}','{$last_name}','{$sexo}','{$location}','{$id_user}')";

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