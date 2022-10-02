<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$nombre = $_GET['nombre'];
$email = $_GET['email'];
$telefono = $_GET['telefono'];
$direccion = $_GET['direccion'];
$password = $_GET['password'];

$sql_query = "UPDATE PERSONA SET nombre='$nombre', email='$email', telefono='$telefono', direccion='$direccion', password='$password' WHERE email='$email'";
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