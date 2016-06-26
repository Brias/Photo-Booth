<?php
/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:57
 */

include_once "exceptions/InvalidDataTypeException.php";
include_once "exceptions/InvalidEmailException.php";
include_once "models/SendEmail.php";
include_once "models/Image.php";
include_once "controller/ImageController.php";

$image = $_POST['image'];
$email = $_POST['email'];

try {
    $decodedImage = Image::decodeImageData($image);

    $imageController = new ImageController(new Image($decodedImage), new SendEmail($email));

    if ($imageController->save()) {
        echo 200;
    }
} catch (InvalidDataTypeException $e) {
    echo $e->__toString();
} catch (InvalidEmailException $e) {
    echo $e->__toString();
}