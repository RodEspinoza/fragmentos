<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";


$json = array();

if(isset($_POST["stock"])&&isset($_POST["name"])){


 $stock = $_POST["stock"];
 $name = $_POST["name"];

 $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);

 if(mysqli_connect_errno){
     echo "".mysqli_connect_error();
    }

    $insert = "INSERT INTO producto(name, stock) VALUES('{$name}', {$stock})";

    $resultado_insert = mysqli_query($conexion, $insert);

 	 if(!$resultado_insert){
 	     echo "".mysqli_error($conexion);
 	 }else{
       $last_id = mysqli_insert_id($conexion);
       echo $last_id;
 	   $consulta = "SELECT * FROM PRODUCT WHERE id={$last_id}";
 	   $resultado = mysqli_query($conexion, $consulta);
 	   echo $resultado;
 	   if($registro = mysqli_fetch_array($resultado)){
 	       $json["product"] =$registro;
 	   }
 	   echo json_encode($json);

 	 }



 mysqli_close($conexion);
}



 ?>
