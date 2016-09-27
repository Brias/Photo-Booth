<?php
/**
 * This file contains the class CodeGenerator
 *
 * LICENSE: This source file is subject of the MIT License
 * http://www.opensource.org/licenses/mit-license.html MIT License
 *
 * @package    server/helper
 * @author     Matthias Bräuer
 * @copyright  (c) 2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */

/**
 * This Class generates a random unique String
 *
 * @package    server/helper
 * @author     2016 Matthias Bräuer
 * @copyright  (c)2016 Matthias Bräuer
 * @license    http://www.opensource.org/licenses/mit-license.html MIT License
 * @version    1.0
 * @link       https://github.com/Brias/Photo-Booth/
 */
class CodeGenerator
{
    public static function generateCode()
    {
        return bin2hex(openssl_random_pseudo_bytes(32));
    }
}