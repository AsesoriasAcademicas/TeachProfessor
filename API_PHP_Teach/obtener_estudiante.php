<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$email = $_GET['email'];

$sql_query = "SELECT id_estudiante,nombre,email,telefono,direccion,password FROM PERSONA INNER JOIN ESTUDIANTE WHERE PERSONA.id_persona = ESTUDIANTE.id_persona AND email = '$email'";
$mysql_query = mysqli_query($conn, $sql_query);
if ($user = mysqli_fetch_array($mysql_query)) {
    $result["user"][] = $user;
} else {
	$result["id_estudiante"]=0;
	$result["nombre"]='No registra';
	$result["email"]='No registra';
	$result["telefono"]='No registra';
	$result["direccion"]='No registra';
	$result["password"]='No registra';
	$result['user'][]=$result;
}
echo json_encode($result);
mysqli_close($conn);
?>