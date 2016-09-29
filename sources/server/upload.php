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
 * @link       https://github.com/Brias/Photo-Booth/tree/master/sources/server/upload.php
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
};