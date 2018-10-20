<?php
$hostname_localhost = "localhost:3306";
$database_localhost = "bd_gestor_pedidos";
$username_locahost = "root";
$password_localhost = "";
$json = array();
if(isset($_POST["stock"])&&isset($_POST["name"])){
 $stock = $_POST["stock"];
 $name = $_POST["name"];
 $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
 $insert = "INSERT INTO product(stock, name) VALUES('{$stock}', '{$name}')";
 $resultado_insert = mysqli_query($conexion, $insert);
 $last_id = mysql_insert_id($conexion);
 if($resultado_insert){
   $consulta = "SELECT * FROM product where id={$last_id}";
   $resultado = mysqli_query($conexion, $consulta);
   if($registro = mysqli_fetch_array($resultado)){
     $json['product'][] = $registro;
   }
   mysqli_close($conexion);
   echo json_encode($json);
 }
 else{
$result["id"]=0;
$json["product"][] = $result;
echo json_encode($json);
   }
}

 ?>
