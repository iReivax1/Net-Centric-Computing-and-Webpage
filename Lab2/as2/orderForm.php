<?php
	if(isset($_POST["nameInput"]) && isset($_POST["AppleAmount"]) && isset($_POST["BananaAmount"])
  && isset($_POST["OrangeAmount"]) && isset($_POST["Total"]) && isset($_POST["radio-btn"])) {
    //variable initialisation, POST method is used to pass from html to php, php use name
  	$name = $_POST["nameInput"];
		$numberRegex = "/\d+/"; //a shorthand character class, which matches all numbers
    $appleAmount = intval($_POST["AppleAmount"]);
		$orangeAmount = intval($_POST["OrangeAmount"]);
		$bananaAmount = intval($_POST["BananaAmount"]);

		$total = $_POST["Total"];

    $paymentMethod = $_POST["radio-btn"];

		$fileName = "order.txt";

		if (!file_exists($fileName)) {
			// This code block expected to run only once
			$contents = "Total number of apples: ".$appleAmount."\r\nTotal number of oranges: ".$orangeAmount."\r\nTotal number of bananas: ".$bananaAmount."\r\n";
			file_put_contents($fileName, $contents); //create file and put the contents into the file
		}
		else {
			$file = fopen($fileName, "r");
			$contents = "";
			for ($i = 0; !feof($file); ++$i) {
				$string = fgets($file);
				preg_match($numberRegex, $string, $matches); //reg_match — Perform a regular expression match
				switch($i) {
					case 0: //.= Concatenation assignment
						$contents .= preg_replace($numberRegex, $matches[0] + $appleAmount, $string); //preg_replace — Perform a regular expression search and replace
						break;
					case 1:
						$contents .= preg_replace($numberRegex, $matches[0] + $orangeAmount, $string);
						break;
					case 2:
						$contents .= preg_replace($numberRegex, $matches[0] + $bananaAmount, $string);
						break;
				}
			}
			fclose($file); //close the file, clear cache
			file_put_contents($fileName, $contents); //the existing file is overwritten
		}
    //print out reciept
    echo
    '<html>
    <style>
      .center {
        margin-left: auto;
        margin-right: auto;
        width: 80%;
      }
    </style>
		<head>
			<title>Order Details</title>
			<link href="css/bootstrap.min.css" rel="stylesheet">
		</head>
		<body>
		<div class="center">
			<p> Name : '.$name.'</p>
			<table class="center, table table-hover">
	        	<tr>
	        		<td>Name of Customer: </td>
	        		<td>'.$name.'</td>
	        	</tr>
	        	<tr>
	        		<td>Number of apple bought: </td>
	        		<td>'.$appleAmount.'</td>
	        	</tr>
	        	<tr>
	        		<td>Number of oranges bought: </td>
	        		<td>'.$orangeAmount.'</td>
	        	</tr>
	        	<tr>
	        		<td>Number of bananas bought: </td>
	        		<td>'.$bananaAmount.'</td>
	        	</tr>
	        	<tr>
	        		<td>Total Price Payed: </td>
	        		<td>'.$total.'</td>
	        	</tr>
	        	<tr>
	        		<td>Payment method: </td>
	        		<td>'.$paymentMethod.'</td>
	        	</tr>
	        </table>
	    </div>
	    </body>
	   </html>';
	}
	else {
		var_dump($_POST); //Dumps information about a variable
		echo "<br/><h2>This is embarrasing, something went wrong, please try again</h2>";
	}
?>
