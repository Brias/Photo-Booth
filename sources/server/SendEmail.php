<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 12.06.2016
 * Time: 17:58
 */
class SendEmail
{
    private $_message = array("en"=>"Dear ", "de" => "Hallo Absolventenfeierbesucher,\n anbei ist ein Photo. \n\nViele Grüße\n Dein Photo-Booth-Team");

    public function __construct()
    {

    }

    public function sendMail()
    {
        $to      = 'nobody@example.com';
        $subject = 'the subject';
        $message = '';
        $headers = //'From: webmaster@example.com' . "\r\n" .
            //'Reply-To: webmaster@example.com' . "\r\n" .
            'X-Mailer: PHP/' . phpversion();

        mail($to, $subject, $message, $headers);
    }
}