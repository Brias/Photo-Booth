<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:58
 */

include "InvalidDataTypeException.php";
include "InvalidEmailException.php";

class SendEmail
{
    private $_message = array("en" => "Dear ", "de" => "Hallo Absolventenfeierbesucher,\r\n anbei ist ein Photo. \r\n\r\nViele Grüße\r\n Dein Photo-Booth-Team\r\n www.google.com");
    private $_subject = 'Absolventenfeier';
    private $_downloadLink = "http://homepages.uni-regensburg.de/~brm08652/photo_booth/download.php?activation=";
    private $_to;

    public function __construct($to, $activationCode)
    {
        if (!is_array($to) && !is_string($to)) {
            throw new InvalidDataTypeException("Email is not in valid Data Type");
        }

        if (($invalidEmails = $this->isEmailAddressValid($to)) !== true) {
            throw new InvalidEmailException("Invalid Email Address/es: " . implode(", ", $invalidEmails));
        }

        $this->_to = is_array($this->_to) ? implode(",", $to) : $to;

        $this->_downloadLink .= $activationCode;
    }

    public function isEmailAddressValid($email)
    {
        $invalids = array();

        if (is_array($email)) {
            foreach ($email as $key => $value) {
                if (!preg_match("/^[^0-9][A-z0-9_]+([.][A-z0-9_]+)*[@][A-z0-9_]+([.][A-z0-9_]+)*[.][A-z]{2,4}$/", $value['email'])) {
                    array_push($invalids, $email[$key]);
                }
            }
        } else {
            if (!preg_match("/^[^0-9][A-z0-9_]+([.][A-z0-9_]+)*[@][A-z0-9_]+([.][A-z0-9_]+)*[.][A-z]{2,4}$/", $email)) {
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
        $msg = $this->_message["de"] . "\r\n" . $this->_downloadLink;

        $headers = 'From: webmaster@example.com' . "\r\n" .
            'BCC: ' . $this->_to . "\r\n" .
            'X-Mailer: PHP/' . phpversion();

        if (mail(null, $this->_subject, $msg, $headers)) {
            return true;
        } else {
            return false;
        }
    }
}