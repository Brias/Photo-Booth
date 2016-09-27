<?php
/**
 * This file contains the class DBHelper
 *
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server/dbconfig
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */

/**
 * This Class establishes and closes a connection to a specific SQL database
 *
 * @package    server/dbconfig
 * @author     2016 Matthias Bräuer
 * @copyright  (c)2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */
class DBHelper
{
    protected function connect()
    {
        $db = new mysqli('localhost', 'photobooth', 'FotoBude', 'photobooth');

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
