<?php
/**
 * This file contains the class ImageController
 *
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server/controller
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */

include_once "models/SendEmail.php";
include_once "models/Image.php";
include_once "exceptions/InvalidDataTypeException.php";

/**
 * This Class coordinates saving an given image and sending an email with an access link
 *
 * @package    server/controller
 * @author     2016 Matthias Bräuer
 * @copyright  (c)2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */
class ImageController
{
    private $_image;
    private $_email;

    public function __construct($image, $email)
    {
        if (!($image instanceof Image) || !($email instanceof SendEmail)) {
            throw new InvalidDataTypeException(__CLASS__ . ":" . __FUNCTION__ . ":" . "Invalid Data Type");
        }

        $this->_image = $image;
        $this->_email = $email;
    }

    public function save()
    {
        if ($this->saveImage()) {
            if ($this->sendEmail()) {
                return true;
            }
        }

        return false;
    }

    private function saveImage()
    {
        if ($this->_image->saveImage()) {
            try {
                $this->_image->create();
                return true;
            } catch (DBConnectionFailedException $e) {
                echo $e->__toString();
            }
        }

        return false;
    }

    private function sendEmail()
    {
        $this->_email->setActivationCode($this->_image->getDownloadCode());

        return $this->_email->sendMail();
    }
}