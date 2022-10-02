<?php

include 'config.php';

$result = array();
$fila = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$email = $_GET['email'];
$fecha_inicio = $_GET['fecha_inicio'];
$fecha_fin = $_GET['fecha_fin'];

$sql_query = "SELECT (SUM(PAGO.valor_ganacia)- SUM(PAGO.valor_fondo)) AS GANANCIA, SUM(PAGO.valor_fondo) AS FONDO, SUM(PAGO.valor_ganacia) AS TOTAL FROM CLASE INNER JOIN TUTORIA INNER JOIN PAGO INNER JOIN PERSONA INNER JOIN PROFESOR WHERE TUTORIA.id_tutoria = CLASE.id_tutoria AND CLASE.id_clase = PAGO.id_clase AND PERSONA.id_persona = PROFESOR.id_persona AND CLASE.id_profesor = PROFESOR.id_profesor AND TUTORIA.estado='activo' AND  email = '$email' AND CLASE.fecha BETWEEN '$fecha_inicio' AND '$fecha_fin'";
$mysql_query = mysqli_query($conn, $sql_query);
if ($ganancias = mysqli_fetch_array($mysql_query)) {
    $result["ganancias"][] = $ganancias;
} else {
    $result["ganancia"]=0;
    $result["fondo"]=0;
    $result["total"]=0;
    $result['ganancias'][]=$result;
}
echo json_encode($result);
mysqli_close($conn);
?>