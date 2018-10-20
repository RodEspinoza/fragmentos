<?php

$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";
$json=array();



	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
  if(mysqli_connect_errno){
      echo "".mysqli_connect_error();
     }
  $consulta = "SELECT * FROM producto";


	$resultado=mysqli_query($conexion,$consulta);

	while($registro=mysqli_fetch_array($resultado)){
		$json[] = $registro;
	}


	mysqli_close($conexion);
	echo json_encode($json);




?>
