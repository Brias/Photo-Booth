<?php
/**
 * This file contains the class SendEmail
 *
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server/models
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/tree/master/sources/server/models/SendEmail.php
 */

include_once "exceptions/InvalidDataTypeException.php";
include_once "exceptions/InvalidEmailException.php";

/**
 * This Class sends an email to one ore more persons with an generated "activation code"
 *
 * @package    server/models
 * @author     2016 Matthias Bräuer
 * @copyright  (c)2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0 
 */
class SendEmail
{

    private $_message = array("en" => "Dear ", "de" => "Hallo SIM-Sommerfest-Besucher,\r\n\r\n klicke auf den unten 
    stehenden Link, um den automatischen Download deines Fotos zu starten.\r\n\r\n Startet der Download nicht, oder
     tritt ein Fehler auf, probiere ein anderes Gerät und/oder Browser!");
    private $_subject = 'SIM Sommerfest 2016';
    private $_downloadLink = "http://urwalking.ur.de/photobooth/download.php?activation=";
    private $_to;
    private $_activationCode;
    private $_msgValediction = "Viele Grüße\r\n\r\nDein Fotobox-Team";

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

    public function setActivationCode($activationCode)
    {
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
        $msg = $this->_message["de"] . "\r\n\r\n" . $this->_downloadLink . $this->_activationCode . "\r\n\r\n" . $this->_msgValediction;

        $headers = 'From: SIM-Sommerfest2016@rhslx1.uni-regensburg.de' . "\r\n" .
            'Bcc: ' . $this->_to . "\r\n" .
            'Content-Type: text/plain; charset="UTF-8"' . "\r\n" .
            'X-Mailer: PHP/' . phpversion();

        if (mail("", $this->_subject, $msg, $headers)) {
            return true;
        } else {
            return false;
        }
    }

}
