<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 14.06.2016
 * Time: 15:55
 */
class InvalidEmailException extends Exception
{
    public function __construct($message, $code = 0, Exception $previous = null)
    {
        parent::__construct($message, $code, $previous);
    }

    public function __toString()
    {
        return __CLASS__ . ": [{$this->code}]: {$this->message}\n";
    }
}