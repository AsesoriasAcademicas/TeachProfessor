<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$idClase = $_GET['idClase'];

$sql_query = "SELECT estado FROM TUTORIA WHERE id_tutoria = (SELECT id_tutoria FROM CLASE WHERE id_clase=$idClase)";
$mysql_query = mysqli_query($conn, $sql_query);
if ($estado = mysqli_fetch_array($mysql_query)) {
    $result["success"] = "1";
    $result["estado"][] = $estado;
} else {
    $result["error"] = "0";
	$result["estado"]='No registra';
	$result["estado"][] = $estado;
}
echo json_encode($result);
mysqli_close($conn);
?>