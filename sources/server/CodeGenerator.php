<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 14.06.2016
 * Time: 18:21
 */
class CodeGenerator
{
    public static function generateCode(){
        return bin2hex(openssl_random_pseudo_bytes(32));
    }
}