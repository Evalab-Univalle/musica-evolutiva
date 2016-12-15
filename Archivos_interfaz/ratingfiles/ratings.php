<?php
// Star Rating Script - coursesweb.net/php-mysql/

// if NRRTG is 0, the user can rate multiple items in a day, if it is 1, the user can rate only one item in a day
define('NRRTG', 0);

     /* From Here no need to modify */

if(!headers_sent()) header('Content-type: text/html; charset=utf-8');      // header for utf-8

include('class.rating.php');        // Include Rating class
$obRtg = new Rating();

// if data from POST 'elm' and 'rate'
if(isset($_POST['elm']) && isset($_POST['rate'])) {
  // removes tags and external whitespaces from 'elm'
  $_POST['elm'] = array_map('strip_tags', $_POST['elm']);
  $_POST['elm'] = array_map('trim', $_POST['elm']);
  if(!empty($_POST['rate'])) $_POST['rate'] = intval($_POST['rate']);

  echo $obRtg->getRating($_POST['elm'], $_POST['rate']);
}