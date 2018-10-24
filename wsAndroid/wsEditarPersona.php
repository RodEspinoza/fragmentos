<?php
$hostname_localhost = "localhost";
$database_localhost = "u114296893_gesto";
$username_locahost = "u114296893_root";
$password_localhost = "siempretropical";

$json = array();
if(isset($_POST["id"])&&isset($_POST["name"])&&isset($_POST["last_name"])&&isset($_POST["sexo"])&&isset($_POST["location"])){

    $id_user = $_POST["id"];
    $name = $_POST["name"];
    $last_name = $_POST["last_name"];
    $sexo = $_POST["sexo"];
    $location = $_POST["location"];

    $conexion = mysqli_connect(
    $hostname_localhost,$username_locahost,$password_localhost,$database_localhost);
    $stmt = $conexion->prepare(
    "UPDATE persona SET nombre='{$name}', last_name ='{$last_name}', sexo = '{$sexo}', location = '{$location}' WHERE id_user = '{$id_user}'");
    $stmt->execute();
    $nrows = $stmt->affected_rows;
    if(!$nrows){
       echo 'No Actualizado';
    }
    else{
        echo 'Actualizado';
    }
}
?>
