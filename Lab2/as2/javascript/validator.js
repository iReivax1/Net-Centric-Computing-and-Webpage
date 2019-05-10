

function totalCost(){
  //calculations
  var appleCost = parseInt(document.getElementById("AppleAmount").value)*0.69;
  var orangeCost = parseInt(document.getElementById("OrangeAmount").value)*0.59;
  var bananaCost = parseInt(document.getElementById("BananaAmount").value)*0.39;
  var total = appleCost + orangeCost + bananaCost;
//Money is 2 decimal
  total = total.toFixed(2);
//make sure that calculation is done correctly should not have anything negative or less than 0
  if(total > 0){
    document.getElementById("Total").value = "$"+total; //change value in total's text box to the value
    document.getElementById('submitbutton').disabled = false; //enables the submit button
  }else if(appleCost == 0 && orangeCost == 0 && bananaCost == 0 ){ //user typed all 0.
    alert("Buy at least 1 fruit!");
    document.getElementById('submitbutton').disabled = true; //disables the submit button
  }else{ //all other wrong data input
    document.getElementById("Total").value = NaN;
    document.getElementById('submitbutton').disabled = true; //disables the submit button
  }
}
/////////////input validation functions///////////
//alert function will notify user's error
//Each fruits have their own validation function so that the webpage will not notify the user when other fruits have not been given any input.
function changeApple(input){
  //parseInt returns an integer rounded down. Chooses the first Integer in the text, if there is any spaces
  var inputInt = parseInt(input);
  //Making sure that only numbers is given
   if (isNaN(input)){
     document.getElementById('submitbutton').disabled = true;//disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Amount input is invalid, use integers only!");
   }
   else if (input < 0){ //negative input
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN;
     alert("Wrong amount, more than 1 please");
    document.getElementById('AppleAmount').value = null; //erases the input given by user so that they will be forced to change the input to correct type
   }
   else if(inputInt != input || inputInt == NaN ){
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Integer value only");
     document.getElementById('AppleAmount').value = null; //erases the input given by user so that they will be forced to change the input to correct type
   }
   else{
     document.getElementById('submitbutton').disabled = false; //enables the submit button
     totalCost();
   }
}

function changeOrange(input){
  //parseInt returns an integer rounded down. Chooses the first Integer in the text, if there is any spaces
  var inputInt = parseInt(input);
  //Making sure that only numbers is given
   if (isNaN(input)){
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Amount input is invalid, use integers only!");
   }
   else if (input < 0){ //negative input
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Wrong amount, more than 1 please");
     document.getElementById('OrangeAmount').value = null; //erases the input given by user so that they will be forced to change the input to correct type
   }
   else if(inputInt != input || inputInt == NaN ){ //a decimal number given such that whats given by user is different from the parsed number, or no input given
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Integer value only");
     document.getElementById('OrangeAmount').value = null; //disables the submit button
   }
   else{
     document.getElementById('submitbutton').disabled = false; //enables the submit button
     totalCost();
   }
}

function changeBanana(input){
  //parseInt returns an integer rounded down. Chooses the first Integer in the text, if there is any spaces
  var inputInt = parseInt(input);
  //Making sure that only numbers is given
   if (isNaN(input)){
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Amount input is invalid, use integers only!");
     totalCost();
   }
   else if (input < 0){ //negative input
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Wrong amount, more than 1 please");
     document.getElementById('BananaAmount').value = null; //erases the input given by user so that they will be forced to change the input to correct type
   }
   else if(inputInt != input || inputInt == NaN ){ //a decimal number given such that whats given by user is different from the parsed number, or no input given
     document.getElementById('submitbutton').disabled = true; //disables the submit button
     document.getElementById("Total").value = NaN; //Change the total value
     alert("Integer value only");
     document.getElementById('BananaAmount').value = null; //erases the input given by user so that they will be forced to change the input to correct type
   }
   else{
     document.getElementById('submitbutton').disabled = false; //enables the submit button
     totalCost();
   }
}

function isEmpty() {//checks if all fields are complete
  if (document.getElementById("AppleAmount").value == "" || document.getElementById("OrangeAmount").value == "" ||document.getElementById("BananaAmount").value == "" || document.getElementById("nameInput").value == "" || (document.getElementById("Visa").checked == false && document.getElementById("MasterCard").checked == false && document.getElementById("Discover").checked == false))
      {
        		alert("Please complete all fields.");
        		return false;
      }
  }



// /^[A-Za-z0-9]{3}$/.test('Abc') //✅
//   ///^\d{3}$/.test('123')  //✅
//   /^(\d{2})+$/.test('1234') //✅
//   /^(\d{2})+$/.test('123')  //❌
