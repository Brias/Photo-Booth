<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 15.06.2016
 * Time: 02:14
 */

include_once "InvalidDataTypeException.php";
include_once "Image.php";

$activation = htmlspecialchars($_GET["activation"]);

if (isset($activation) && !empty($activation)){
    try{
        $image = new Image("", "", $activation);
        
        $download = $image->getImage($image->getImageName());
        
        echo $download !== null ? $download : "Unknown Error Occured!";
    } catch (InvalidDataTypeException $e) {
        echo $e->__toString();
    }
} else {
    echo "No ActivationCode provded";
}