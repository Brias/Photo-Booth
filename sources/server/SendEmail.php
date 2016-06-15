<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:58
 */

include_once "InvalidDataTypeException.php";
include_once "InvalidEmailException.php";

class SendEmail
{
    private $_message = array("en" => "Dear ", "de" => "Hallo Absolventenfeierbesucher,\r\n anbei ist ein Photo. \r\n\r\nViele Grüße\r\n Dein Photo-Booth-Team\r\n");
    private $_subject = 'Absolventenfeier';
    private $_downloadLink = "http://homepages.uni-regensburg.de/~brm08652/photo_booth/download.php?activation=";
    private $_to;
    private $_activationCode;

    public function __construct($to, $activationCode = "")
    {
        $to = explode(",", $to);

        if (!is_array($to) && !is_string($to)) {
            throw new InvalidDataTypeException(__CLASS__ . ":" . __FUNCTION__ . ":" . "Email is not in valid Data Type");
        }

        if (($invalidEmails = $this->isEmailAddressValid($to)) !== true || (is_array($to) && count($to) === 0)) {
            throw new InvalidEmailException(__CLASS__ . ":" . __FUNCTION__ . ":" . "Invalid Email Address/es: " . implode(", ", $invalidEmails));
        }

        $this->_to = is_array($to) ? implode(",", $to) : $to;

        $this->_activationCode = $activationCode;
    }

    public function setActivationCode($activationCode){
        $this->_activationCode = $activationCode;
    }

    public function isEmailAddressValid($email)
    {
        $invalids = array();

        if (is_array($email)) {
            foreach ($email as $key => $value) {
                if (!filter_var($value, FILTER_VALIDATE_EMAIL)) {
                    array_push($invalids, $email[$key]);
                }
            }
        } else {
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                array_push($invalids, $email);
            }
        }

        if (count($invalids) > 0) {
            return $invalids;
        } else {
            return true;
        }
    }

    public function sendMail()
    {
        $msg = $this->_message["de"] . "\r\n" . $this->_downloadLink . $this->_activationCode;

        $headers = 'From: webmaster@example.com' . "\r\n" .
            'Bcc: ' . $this->_to . "\r\n" .
            'X-Mailer: PHP/' . phpversion();

        if (mail("", $this->_subject, $msg, $headers)) {
            return true;
        } else {
            return false;
        }
    }
}