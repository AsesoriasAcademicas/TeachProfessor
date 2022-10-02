<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$idClase = $_GET['idClase'];
$materia = $_GET['materia'];
$tema = $_GET['tema'];
$inquietudes = $_GET['inquietudes'];
$estado = $_GET['estado'];
$fecha = $_GET['fecha'];
$hora = $_GET['hora'];
$duracion = $_GET['duracion'];
$precio = $_GET['precio'];

$sql_query_materia = "UPDATE TUTORIA SET materia='$materia', tema='$tema', inquietudes='$inquietudes', estado='$estado' WHERE id_tutoria = (SELECT id_tutoria FROM CLASE WHERE id_clase=$idClase)";
$sql_query_clase = "UPDATE CLASE SET fecha='$fecha', hora='$hora', duracion='$duracion', id_tutoria = (SELECT id_tutoria FROM CLASE WHERE id_clase=$idClase) WHERE id_clase=$idClase";
$sql_query_pago = "UPDATE PAGO SET valor_ganacia='$precio', valor_fondo=$duracion*700 WHERE id_clase='$idClase'";
if (mysqli_query($conn, $sql_query_materia) && mysqli_query($conn, $sql_query_clase) && mysqli_query($conn, $sql_query_pago)) {
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