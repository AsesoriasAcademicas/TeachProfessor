<?php

include 'config.php';

$result = array();
$fila = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$sql_query = "SELECT PERSONA.id_persona,nombre,email,telefono,direccion,password,id_estudiante FROM PERSONA INNER JOIN ESTUDIANTE WHERE PERSONA.id_persona = ESTUDIANTE.id_persona";
if ($mysql_query = mysqli_query($conn, $sql_query)) {
    $longitud = mysqli_num_rows($mysql_query);
    if($longitud > 0) {
        for($i = 0; $i < $longitud; $i++){
        	$mysql_fetch_row = mysqli_fetch_row($mysql_query);
            $fila["id_persona"] = $mysql_fetch_row[0];
            $fila["nombre"] = $mysql_fetch_row[1];
            $fila["email"] = $mysql_fetch_row[2];
            $fila["telefono"] = $mysql_fetch_row[3];
            $fila["direccion"] = $mysql_fetch_row[4];
            $fila["password"] = $mysql_fetch_row[5];
            $fila["id_estudiante"] = $mysql_fetch_row[6];
            $result['listaAlumnos'][$i] = $fila;
        }
        echo json_encode($result);
    } else {
        $result['listaAlumnos'][] = $fila;
        echo json_encode($result);
    }
} else {
	$result['listaAlumnos'][] = $fila;
}
echo json_encode($result);
mysqli_close($conn);
?>