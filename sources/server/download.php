<?php
/**
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/tree/master/sources/server/download.php
 */

include_once "exceptions/InvalidDataTypeException.php";
include_once "models/Image.php";

$activation = htmlspecialchars($_GET["activation"]);

if (isset($activation) && !empty($activation)) {
    try {
        $image = new Image("", "", $activation);

        $download = $image->getImage($image->getImageName());

        echo $download !== null ? $download : "Unknown Error Occurred!";
    } catch (InvalidDataTypeException $e) {
        echo $e->__toString();
    }
} else {
    echo "No ActivationCode provided";
}