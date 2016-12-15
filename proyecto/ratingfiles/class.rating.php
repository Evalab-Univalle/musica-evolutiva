<?php
class Rating {
  // properties
  public $affected_rows = 0;        // number of affected, or returned rows in SQL query
  protected $rater = '';                    // the user who rate, or its IP
  protected $nrrtg = 0;                 // if it is 1, the user can rate only one item in a day, 0 for multiple items
  public $rtgitems = 'rtgitems';        // Table /or file_name to store items that are ranted
  public $rtgusers = 'rtgusers';             // Table /or filename that stores the users who rated in current day
  protected $tdy;                // will store the number of current day
  public $eror = false;          // to store and check for errors

  // constructor
  public function __construct() {
    // sets $nrrtg, $rater, and $tdy properties
    if(defined('NRRTG')) $this->nrrtg = NRRTG;
    $this->rater = $_SERVER['REMOTE_ADDR'];
    $this->tdy = date("Y/m/d");

    // if set to use TXT files, set the path and name of the files
    $this->rtgitems = '../ratingtxt/'.$this->rtgitems.'.txt';
    $this->rtgusers = '../ratingtxt/'.$this->rtgusers.'.txt';
  }

  // returns JSON string with item:['totalrate', 'nrrates', renot] for each element in $items array 
  public function getRating($items, $rate = '') {

    // if $rate not empty, perform to register the rating, $items contains one item to rate
    if(!empty($rate)) {

        $all_rtgstdy = $this->rtgstdyTxt();     // get 2 array: 'all'-rows ranked today, 'day'-items by rater today
        $rtgstdy = array_unique(array_merge($rtgstdy, $all_rtgstdy[$this->tdy]));
        

        // if already rated, add in cookie, returns JSON from which JS alert message and will reload the page
        // else, accesses the method to add the new rating, in mysql or TXT file
        if(in_array($items[0], $rtgstdy)) {
          $rtgstdy[] = $items[0];
          return '{"'.$items[0].'":[0,0,3]}';
        }
        else $this->setRtgTxt($items, $rate, $all_rtgstdy);          // add the new rate, and rater in TXT files

       array_push($rtgstdy, $items[0]);        // adds curent item as rated
     
    }

    // if $nrrtg is 1, and $rtgstdy has item, set $setrated=1 (user already ranked today)
    // else, user can rate multiple items, after Select is checked if already rated the existend $item
    $setrated = ($this->nrrtg === 1 && count($rtgstdy) > 0) ? 1 : 0;

    // get array with items and their ratings from TXT file
    $rtgitems = $this->getRtgTxt($items, $rtgstdy, $setrated);

    return json_encode($rtgitems);
  }
  
  // add /update rating item in TXT file, keep rows from today in $rtgusers, and add new row with $rater
  protected function setRtgTxt($items, $rate, $all_rtgstdy) {
    // get the rows from file with items, if exists
    if(file_exists($this->rtgitems)) {
      $rows = file($this->rtgitems, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
      $nrrows = count($rows);

      // if exist rows registered, get array for each row, with - item^totalrate^nrrates
      // if row with item, update it and stop, else, add the row at the end
      if($nrrows > 0) {
        for($i=0; $i<$nrrows; $i++) {
          $row = explode('^', $rows[$i]);
          if($row[0] == $items[0]) {
            $rows[$i] = $items[0].'^'.($row[1] + $rate).'^'.($row[2] + 1);
            $rowup = 1; break;
          }
        }
      }
    }
    if(!isset($rowup)) $rows[] = $items[0].'^'.$rate.'^1';

    file_put_contents($this->rtgitems, implode(PHP_EOL, $rows));      // save the items in file

    // add row with curent item rated and the rater (today^rater^item), and save all the rows
    $all_rtgstdy['all'][] = $this->tdy.'^'.$this->rater.'^'.$items[0].'^'.$rate;
    file_put_contents($this->rtgusers, implode(PHP_EOL, $all_rtgstdy['all']));

    // add curent rated item to the others today, and save them as string ',' in cookie (till the end of day)
    $all_rtgstdy[$this->tdy][] = $items[0];
  }

  // get from TXT 'totalrate' and 'nrrates' of each element in $items, $rtgstdy stores items rated by the user today
  // returns array with item:['totalrate', 'nrrates', renot] for each element in $items array
  protected function getRtgTxt($items, $rtgstdy, $setrated) {
    $re = array_fill_keys($items, array(0,0,$setrated));    // makes each value of $items as key with an array(0,0,0)

    if(file_exists($this->rtgitems)) {
      $rows = file($this->rtgitems, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
      $nrrows = count($rows);

      // if exist rows registered, get array for each row, with - item^totalrate^nrrates
      // if row with item is in $items, add its data in $re
      if($nrrows > 0) {
        for($i=0; $i<$nrrows; $i++) {
          $row = explode('^', $rows[$i]);
          $rated = in_array($row[0], $rtgstdy) ? $setrated + 1 : $setrated;   // add 1 if the item was ranked by the user today
          if(in_array($row[0], $items)) $re[$row[0]] = array($row[1], $row[2], $rated);
        }
      }
    }

    return $re;
  }

  // returns from TXT file an array with 2 arrays: all rows ranked today, and items ranked by the user today
  protected function rtgstdyTxt() {
    $re['all'] = array();  
    $re[$this->tdy] = array();
    if(file_exists($this->rtgusers)) {
      $rows = file($this->rtgusers, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);
      $nrrows = count($rows);

      // if exist rows registered, get array for each row, with - day^rater^item , compare 'day', and 'rater'
      if($nrrows > 0) {
        for($i=0; $i<$nrrows; $i++) {
          $row = explode('^', $rows[$i]);
          if($row[0] == $this->tdy) {
            $re['all'][] = $rows[$i];
            if($row[1] == $this->rater) $re[$this->tdy][] = $row[2];
          }
        }
      }
    }

    return $re;
  }
}