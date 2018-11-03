<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";


$json = array();
if(isset($_GET["name"])){
  $product_name = $_GET["producto"];
  $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
  $query_get_product = "SELECT * FROM producto where name='{$product_name}'";
  $resultado = mysqli_query($conexion, $query_get_product);


  	if($registro=mysqli_fetch_array($resultado)){
  			$json['producto'][] = $registro;
  	}
  	else
  	{
  		$result["id"]=0;
  		$json['producto'][] = $result;
  		echo json_encode($json);
  	}
    mysqli_close();
    echo json_encode($json);

}

?>
