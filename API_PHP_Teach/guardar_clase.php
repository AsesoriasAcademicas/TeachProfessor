<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$materia = $_GET['materia'];
$tema = $_GET['tema'];
$inquietudes = $_GET['inquietudes'];
$estado = $_GET['estado'];
$fecha = $_GET['fecha'];
$hora = $_GET['hora'];
$duracion = $_GET['duracion'];
$precio = $_GET['precio'];
$idEstudiante = $_GET['idEstudiante'];
$idProfesor = $_GET['idProfesor'];

$sql_query_tutoria = "INSERT INTO TUTORIA (materia, tema, inquietudes, estado) VALUES ('$materia', '$tema', '$inquietudes', '$estado')";
$sql_query_clase = "INSERT INTO CLASE (fecha, hora, duracion, id_tutoria, id_estudiante, id_profesor) VALUES ('$fecha', '$hora', '$duracion', (SELECT MAX(id_tutoria) FROM TUTORIA), $idEstudiante, $idProfesor)";
$sql_query_pago = "INSERT INTO PAGO (id_clase, valor_ganacia, valor_fondo) VALUES ((SELECT MAX(id_clase) FROM CLASE), $precio, $duracion*700)";
if (mysqli_query($conn, $sql_query_tutoria) && mysqli_query($conn, $sql_query_clase) && mysqli_query($conn, $sql_query_pago)){
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