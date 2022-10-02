<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$email = $_GET['email'];

$sql_query = "SELECT nombre,email,telefono,direccion,password FROM PERSONA WHERE email = '$email'";
$mysql_query = mysqli_query($conn, $sql_query);
if ($user = mysqli_fetch_array($mysql_query)) {
    $result["user"][] = $user;
} else {
	$result["nombre"]='No registra';
	$result["email"]='No registra';
	$result["telefono"]='No registra';
	$result["direccion"]='No registra';
	$result["password"]='No registra';
	$result['user'][]=$user;
}
echo json_encode($result);
mysqli_close($conn);
?>