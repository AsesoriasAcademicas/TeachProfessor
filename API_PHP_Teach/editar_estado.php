<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$idClase = $_GET['idClase'];
$estado = $_GET['estado'];

$sql_query = "UPDATE TUTORIA SET estado='$estado' WHERE id_tutoria = (SELECT id_tutoria FROM CLASE WHERE id_clase=$idClase)";
if (mysqli_query($conn, $sql_query)) {
    $result["success"] = "1";
    $result["message"] = "success";
   
    echo json_encode($result);
} else {
    $result["error"] = "0";
    $result["message"] = "error";
   
    echo json_encode($result);
}
mysqli_close($conn);
?>