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
        $db = new mysqli('db2', 'phy_kurs_user', 'jker34.ew3', 'Phy_Kurs');

        if (mysqli_connect_errno()) {
            //return $err[0]->$err[1](mysqli_connect_errno(), mysqli_connect_error());
            return null;
        }
     
        return $db;
    }

    protected function close(&$db)
    {
        $db->close();
    }
}