<?php

/**
 * Created by PhpStorm.
 * User: Matze
 * Date: 14.06.2016
 * Time: 18:09
 */
class DBHelper
{
    protected function connect()
    {
        $db = new mysqli('urwalking.ur.de', 'photobooth', 'FotoBude', 'photobooth');

        if (mysqli_connect_errno()) {
            return null;
        }
     
        return $db;
    }

    protected function close(&$db)
    {
        $db->close();
    }
}