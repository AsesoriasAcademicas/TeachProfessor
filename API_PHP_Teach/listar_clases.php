<?php

include 'config.php';

$result = array();
$fila = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$email = $_GET['email'];

$sql_query = "SELECT id_clase,fecha,hora,duracion,materia,tema,inquietudes,estado FROM CLASE INNER JOIN TUTORIA INNER JOIN PERSONA INNER JOIN ESTUDIANTE WHERE TUTORIA.id_tutoria = CLASE.id_tutoria AND PERSONA.id_persona = ESTUDIANTE.id_persona AND CLASE.id_estudiante = ESTUDIANTE.id_estudiante AND email = '$email'";
if ($mysql_query = mysqli_query($conn, $sql_query)) {
    $longitud = mysqli_num_rows($mysql_query);
    if($longitud > 0) {
        for($i = 0; $i < $longitud; $i++){
        	$mysql_fetch_row = mysqli_fetch_row($mysql_query);
            $fila["id_clase"] = $mysql_fetch_row[0];
            $fila["fecha"] = $mysql_fetch_row[1];
            $fila["hora"] = $mysql_fetch_row[2];
            $fila["duracion"] = $mysql_fetch_row[3];
            $fila["materia"] = $mysql_fetch_row[4];
            $fila["tema"] = $mysql_fetch_row[5];
            $fila["inquietudes"] = $mysql_fetch_row[6];
            $fila["estado"] = $mysql_fetch_row[7];
            $result['listaClases'][$i] = $fila;
        }
        echo json_encode($result);
    } else {
        $result['listaClases'][] = $fila;
        echo json_encode($result);
    }
} else {
	$result['listaClases'][] = $fila;
}
echo json_encode($result);
mysqli_close($conn);
?>