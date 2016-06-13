<?php
/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:57
 */

$image = $_POST['image'];

$decodedImage = base64_decode($image);

if (!isset($image) || empty($decodedImage) || $decodedImage === false) {
    echo "Uploaded invalid data";
} else {
    $savedSuccessfully = file_put_contents(round(microtime(true) * 1000) . ".jpg", $decodedImage);

    if ($savedSuccessfully === false) {
        echo "Error while saving image. maybe you domt have permissions to write data";
    }

    if ($savedSuccessfully === 0) {
        echo "Error. Image seems to have 0 Bytes";
    } else {
        echo 200;
    }
}