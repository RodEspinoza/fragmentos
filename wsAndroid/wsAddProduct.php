<?php
$hostname_localhost = "localhost";
$database_localhost = "id3415554_bd_gestor_pedidos";
$username_locahost = "id3415554_root";
$password_localhost = "siempretropical";


$json = array();
$stock = 21;
$name = "nombre";



if(isset($_POST["stock"])&&isset($_POST["name"])){


 $stock = $_POST["stock"];
 $name = $_POST["name"];
 $conexion = mysqli_connect($hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
 if(mysqli_connect_errno){
     echo "".mysqli_connect_error();
    }
    $insert = "INSERT INTO producto(name, stock) VALUES('{$name}', {$stock})";


 	 if(!mysqli_query($conexion,$insert)){
 	     echo "".mysqli_error($conexion);
 	 }else{
 	     $last_id = mysql_insert_id($conexion);
 	     $consulta = "SELECT * FROM product where id {$last_id}";

 	 }


}



 ?>
