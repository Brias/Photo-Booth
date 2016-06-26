<?php

/**
 * Created by PhpStorm.
 * User: ubuntu
 * Date: 14.06.16
 * Time: 23:43
 */

include_once "models/SendEmail.php";
include_once "models/Image.php";
include_once "exceptions/InvalidDataTypeException.php";

class ImageController
{
    private $_image;
    private $_email;

    public function __construct($image, $email)
    {
        if(!($image instanceof Image) || !($email instanceof SendEmail)){
            throw new InvalidDataTypeException(__CLASS__ . ":" . __FUNCTION__ . ":" . "Invalid Data Type");
        }

        $this->_image = $image;
        $this->_email = $email;
    }

    public function save(){
        if($this->saveImage()) {
            if($this->sendEmail()){
                return true;
            }
        }

        return false;
    }

    private function saveImage(){
        if($this->_image->saveImage()){
            try {
                $this->_image->create();
                return true;
            } catch (DBConnectionFailedException $e) {
                echo $e->__toString();
            }
        }

        return false;
    }

    private function sendEmail(){
        $this->_email->setActivationCode($this->_image->getDownloadCode());

        return $this->_email->sendMail();
    }
}