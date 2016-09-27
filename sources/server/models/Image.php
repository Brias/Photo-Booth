<?php
/**
 * This file contains the class Image
 *
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server/models
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */

include_once "dbconfig/DBHelper.php";
include_once "exceptions/DBConnectionFailedException.php";
include_once "helper/CodeGenerator.php";

/**
 * This Class extends the class DBHelper and is responsible for the communication wth the SQL Database as well as 
 * for saving ang getting the image locally
 *
 * @package    server/models
 * @author     2016 Matthias Bräuer
 * @copyright  (c)2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */
class Image extends DBHelper
{
    private $_image;
    private $_name;
    private $_downloadCode;
    private $_BASE_PATH = "images/";

    public function __construct($image = "", $name = "", $downloadCode = null)
    {
        $this->_downloadCode = $downloadCode !== null ? $downloadCode : CodeGenerator::generateCode();
        $this->_image = $image;
        $this->_name = $name;
    }

    public static function decodeImageData($image)
    {
        $decodedImage = base64_decode($image);

        if (!isset($image) || empty($decodedImage) || $decodedImage === false) {
            throw new InvalidDataTypeException(__CLASS__ . ":" . __FUNCTION__ . ":" . "Invalid Data Type");
        }

        return $decodedImage;
    }

    public function getDownloadCode()
    {
        return $this->_downloadCode;
    }

    private function executeQuery($query)
    {
        htmlspecialchars($query);

        $db = parent::connect();

        $rows = array();

        if ($db) {
            $res = $db->query($query);

            if ($res) {
                while ($row = $res->fetch_object()) {
                    array_push($rows, $row);
                }

                return $rows;
            } else {
                echo "NO Result";
            }

            $db->close();

        } else {
            throw new DBConnectionFailedException(__CLASS__ . ":" . __FUNCTION__ . ":" . "DB Connection failed");
        }
    }

    public function create()
    {
        $db = parent::connect();

        if ($db) {
            $result = $db->query("INSERT INTO photobooth VALUES ('$this->_downloadCode', '$this->_name', DEFAULT)");

            if ($result === TRUE) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new DBConnectionFailedException(__CLASS__ . ":" . __FUNCTION__ . ":" . "DB Connection failed");
        }
    }

    public function saveImage()
    {
        $name = round(microtime(true) * 1000) . ".jpg";

        if (file_put_contents($name = $this->_BASE_PATH . $name, $this->_image)) {
            $this->_name = $name;

            return true;
        }

        return false;
    }

    public function getImageName()
    {
        $query = "SELECT * FROM photobooth WHERE token = '" . $this->_downloadCode . "' LIMIT 1";

        $imageName = $this->executeQuery($query);

        return $imageName[0]->name;
    }

    public function getImage($name = null)
    {
        if (!$name) {
            return null;
        }

        if (file_exists($name)) {
            header('Content-Description: File Transfer');
            header('Content-Disposition: attachment; filename=' . basename($name));
            header('Content-Transfer-Encoding: binary');
            header('Content-Type: application/octet-stream');
            header('Expires: 0');
            header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
            header('Pragma: public');

            ob_clean();
            flush();
            readfile($name);

            exit;
        } else {
            return null;
        }
    }
}
