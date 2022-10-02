<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$sql_query = "SELECT MAX(id_persona) AS maxId FROM PERSONA";
$mysql_query = mysqli_query($conn, $sql_query);
if ($max = mysqli_fetch_array($mysql_query)) {
    $result["idMax"] = $max;
    $result["success"] = "1";
    $result["message"] = "success";
   
    echo json_encode($result);
} else {
	$result["idMax"] = 0;
    $result["error"] = "0";
    $result["message"] = "error";
   
    echo json_encode($result);
}
mysqli_close($conn);
?>