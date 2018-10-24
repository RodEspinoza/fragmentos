<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json = array();

$body = json_decode(file_get_contents("php://input"), true);

//if(isset($_POST["id"])){

    $id_user = $_POST["id"];

    $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
    $query = "SELECT nombre, last_name, sexo, location FROM persona WHERE id_user = '{$id_user}'";

    $result = mysqli_query($conexion, $query);

    while($registro=mysqli_fetch_array($result)){
		$json[] = $registro;
    }
    
    mysqli_close($conexion);
	echo json_encode($json);
//}
?>