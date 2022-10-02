<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$nombre = $_GET['nombre'];
$email = $_GET['email'];
$telefono = $_GET['telefono'];
$direccion = $_GET['direccion'];
$password = $_GET['password'];

$sql_query_persona = "INSERT INTO PERSONA (nombre, email, telefono, direccion, password) VALUES ('$nombre', '$email', '$telefono', '$direccion', '$password')";
$sql_query_estudiante = "INSERT INTO ESTUDIANTE (id_persona) SELECT MAX(id_persona) FROM PERSONA";
if (mysqli_query($conn, $sql_query_persona) && mysqli_query($conn, $sql_query_estudiante)) {
    $result["success"] = "1";
    $result["message"] = "success";
   
    echo json_encode($result);
} else {
    $result["success"] = "0";
    $result["message"] = "error";
   
    echo json_encode($result);
}
mysqli_close($conn);
?>