<?php
	if(isset($_POST["nameInput"]) && isset($_POST["AppleAmount"]) && isset($_POST["BananaAmount"])
  && isset($_POST["OrangeAmount"]) && isset($_POST["Total"]) && isset($_POST["radio-btn"])) {
    //variable initialisation, POST method
  	$name = $_POST["nameInput"];

    $appleAmount = intval($_POST["AppleAmount"]);
		$bananaAmount = intval($_POST["BananaAmount"]);
		$orangeAmount = intval($_POST["OrangeAmount"]);

		$total = $_POST["Total"];

    $paymentMethod = $_POST["radio-btn"];

		$fileName = "order.txt";
		if(!is_file($fileName)){
    	fopen($fileName, "w+"); //create an empty file
		}

		$file = fopen($fileName, "r") or die("Unable to open file!"); //last error check
		$fileSize = filesize($fileName); //get file size

		if($fileSize == 0) {  //if file is empty
    	$file = fopen($fileName, "r+"); //erases the contents of the file or creates a new file if it doesn't exist. File pointer starts at the beginning of the file
    	fwrite($file, "Total number of apples: $appleAmount\nTotal number of oranges: $orangeAmount\nTotal number of bananas: $bananaAmount");
		}
    else { //file not empty

    	$fileText = fread( $fileName, $fileSize); //read the file, up to size of the file
    	$orders = explode("\n" , $fileText); //create an array for every line
    	$appleCurrent = intval(explode(": " , $orders[0])); //put appleAmount into array as an int, applesCurrent = array[0]
    	$orangeCurrent = intval(explode(": " , $orders[1])); //put orangeAmount into array as an int, applesCurrent = array[0]
    	$bananaCurrent = intval(explode(": " , $orders[2])); //put BananaAmount into array as an int, applesCurrent = array[0]

    	$appleTotal = $appleAmount + $appleCurrent;
    	$orangeTotal = $orangeAmount + $orangeCurrent;
    	$bananaTotal = $bananaAmount + $bananaCurrent;


    	$file = fopen($fileName, "w"); //Open a file for read/write. File pointer starts at the beginning of the file
    	fwrite($file ,"Total number of apples: $appleTotal\nTotal number of oranges: $orangeTotal\n Total number of bananas: $bananaTotal") or die('fwrite failed');
    }

    fclose( $file ); //close file and clear buffer
    //print out reciept
    echo
    '<html>
    <style>
      .center {
        margin-left: auto;
        margin-right: auto;
        width: 800px;
      }
    </style>
		<head>
			<title>Order Details</title>
			<link href="css/bootstrap.min.css" rel="stylesheet">
		</head>
		<body>
		<div class="center">
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
	        		<td>Total Price payed: </td>
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
