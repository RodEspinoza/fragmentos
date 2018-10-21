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
	$consulta = "SELECT id FROM persona where id = {$last_id}";
	$resultado = mysqli_query($conexion,$consulta);

	if($registro = mysqli_fetch_array($resultado)){
		$json['id_persona'] = $registro;
	}
	echo json_encode($json);
} else {
	echo "".mysqli_error($conexion);
}

mysqli_close($conexion);

?>