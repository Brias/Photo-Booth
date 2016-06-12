<?php
/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:57
 */

$name = $_POST['name']; //image name
$image = $_POST['image']; //image in string format

$decodedImage = base64_decode($image);

file_put_contents($name . ".jpg", $decodedImage);

echo 200;