<?php

$hostname_localhost = "localhost";
$database_localhost = "id3415554_bd_gestor_pedidos";
$username_locahost = "id3415554_root";
$password_localhost = "siempretropical";
$json=array();


if(isset($_POST["email"])&&isset($_POST["pass"])&&isset($_POST["fecha"])){

	$email = $_POST["email"];
	$pass = $_POST["pass"];
	$fecha = $_POST["fecha"];

	$conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
    if(mysqli_connect_errno){
         echo "".mysqli_connect_error();
        }
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
	    echo "".mysqli_error($conexion);
		$result["id"]=0;
		$json['usuario'][] = $result;
		echo json_encode($json);
	}

}


else{

}

?>
