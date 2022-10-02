<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$idEstudiante = $_GET['idEstudiante'];
$idProfesor = $_GET['idProfesor'];
$fecha = $_GET['fecha'];
$hora = $_GET['hora'];

$sql_query = "SELECT * FROM CLASE WHERE fecha = '$fecha' AND hora = '$hora' AND id_estudiante = $idEstudiante AND id_profesor = $idProfesor";
$mysql_query = mysqli_query($conn, $sql_query);
if ($class = mysqli_fetch_array($mysql_query)) {
    $result["class"][] = $class;
} else {
	$result["id_clase"]=0;
	$result["fecha"]='No registra';
	$result["hora"]='No registra';
	$result["duracion"]='No registra';
	$result["id_tutoria"]='No registra';
	$result["id_estudiante"]='No registra';
	$result["id_profesor"]='No registra';
	$result['class'][]=$result;
}
echo json_encode($result);
mysqli_close($conn);
?>