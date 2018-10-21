<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json = array();
if(isset($_POST["id"])&&isset($_POST["stock"])&&isset($_POST["name"])){
  $id_product = $_POST["id"];
  $stock = $_POST["stock"];
  $name = $_POST["name"];

  $conexion = mysqli_connect(
    $hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
  $stmt = $conexion->prepare(
    "UPDATE product SET name='{$name}', stock ='{$stock}' WHERE id='{$id_product}'");
  $stmt->execute();
  $nrows = $stmt->affected_rows;
  if(!$nrows){
    echo 'noActualizado';
  }
  else{
    echo 'Actualizado';
  }
}
?>
