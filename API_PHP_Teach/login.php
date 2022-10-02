<?php

include 'config.php';

$result = array();

$conn = mysqli_connect($hostname_localhots, $username_localhots, $password_localhots, $database_localhots);

$email = $_GET['email'];
$password = $_GET['password'];

$sql_query = "SELECT email,password FROM PERSONA WHERE email = '$email' and password = '$password'";
$mysql_query = mysqli_query($conn, $sql_query);
$mysql_num_rows = mysqli_num_rows($mysql_query);
if ($mysql_num_rows > 0) {
    $result["success"] = "1";
    $result["message"] = "success";
   
    echo json_encode($result);
} else {
    $result["success"] = "0";
    $result["message"] = "error";
   
    echo json_encode($result);
}
mysqli_close($conn);
?>