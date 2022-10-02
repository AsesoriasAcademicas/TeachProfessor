<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$idClase = $_GET['idClase'];

$sql_query = "SELECT CLASE.id_clase,fecha,hora,duracion,materia,tema,inquietudes,estado,CLASE.id_estudiante, nombre,PAGO.valor_ganacia AS precio FROM CLASE INNER JOIN TUTORIA INNER JOIN PAGO INNER JOIN ESTUDIANTE INNER JOIN PERSONA WHERE TUTORIA.id_tutoria = CLASE.id_tutoria AND CLASE.id_clase = PAGO.id_clase AND CLASE.id_clase = $idClase AND CLASE.id_estudiante = ESTUDIANTE.id_estudiante AND ESTUDIANTE.id_persona = PERSONA.id_persona";
$mysql_query = mysqli_query($conn, $sql_query);
if ($class = mysqli_fetch_array($mysql_query)) {
    $result["class"][] = $class;
} else {
	$result["id_clase"]='No registra';
	$result["fecha"]='No registra';
	$result["hora"]='No registra';
	$result["duracion"]='No registra';
	$result["materia"]='No registra';
	$result["tema"]='No registra';
	$result["inquietudes"]='No registra';
	$result["estado"]='No registra';
	$result["precio"]="No registra";
	$result["id_estudiante"]="No registra";
	$result["nombre"]="No registra";
	$result['class'][]=$class;
}
echo json_encode($result);
mysqli_close($conn);
?>