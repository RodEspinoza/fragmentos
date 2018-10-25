<?php

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";
$json=array();


if(isset($_POST["rut"])){
	$id=$_POST["id"];
  $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
  $stmt = $conexion->prepare("DELETE from producto WHERE id = '{id}'");
	$stmt->execute();
  $nrows = $stmt->affected_rows;

	if(!$nrows){
		echo 'noEliminado';
	}
	else{
		echo 'eliminado';
	}

}

?>
