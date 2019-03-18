

function totalCost(){

  var appleCost = parseInt(document.getElementById("AppleAmount").value)*0.69;
  var orangeCost = parseInt(document.getElementById("OrangeAmount").value)*0.59;
  var bananaCost = parseInt(document.getElementById("BananaAmount").value)*0.39;
  var total = appleCost + orangeCost + bananaCost;
  total = total.toFixed(2);
  document.getElementById("Total").value = "$"+total;

}

function change(input){

   if (isNaN(input)){
     document.getElementById("Total").value = "NaN";
     alert("Amount input is invalid, use integers only!");
   }
   else{
     totalCost();
   }

}


function OneRadio(){
  var radios = document.getElementsByTagName("radio-btn");
  for(i=0; i<radios.length; i++ ) {
      radios[i].onclick = function(e) {
          if(e.ctrlKey) {
              this.checked = false;
          }
      }
  }
}

function radioChecked()
{
    var checkboxs=document.getElementsByTagName("radio-btn");
    var checked=false;
    for(var i=0; i< radios.length;i++)
    {
        if(radios[i].checked)
        {
            checked=true;
            break;
        }
    }
    if(!checked){
      alert("Please select a payment mode")
    }
}
