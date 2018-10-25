<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json = array();

if(isset($_POST["fecha"])&&isset($_POST["state"])&&isset($_POST["total"])&&isset($_POST["id_person"])&&isset($_POST["id_product"])){

 $fecha = $_POST["fecha"];
 $state = $_POST["state"];
 $total = $_POST["total"];
 $idPerson = $_POST["id_person"];
 $idProduct = $_POST["idProduct"];

 $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);

 if(mysqli_connect_errno){
     echo "".mysqli_connect_error();
    }

    $insert = "INSERT INTO pedido(fecha,estado,total,id_person,id_product) VALUES('{$fecha}', '{$state}', '{$total}', '{$id_person}', '{$id_product}')";

    $resultado_insert = mysqli_query($conexion, $insert);

 	 if(!$resultado_insert){
 	     echo "".mysqli_error($conexion);
 	 }else{
       $last_id = mysqli_insert_id($conexion);
       echo $last_id;
 	   $consulta = "SELECT * FROM pedido WHERE id={$last_id}";
 	   $resultado = mysqli_query($conexion, $consulta);
 	   echo $resultado;
 	   if($registro = mysqli_fetch_array($resultado)){
 	       $json["pedido"] =$registro;
 	   }
 	   echo json_encode($json);

    }
      
 mysqli_close($conexion);
}



 ?>
