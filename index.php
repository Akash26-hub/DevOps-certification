<html>
<head>
<title>DevOps Sample App</title>

<?php
function infos()
{
   echo "Your test function on button click is working";
}

if(array_key_exists('about',$_POST)){
   infos();
}
?>
</head>

<body>
    <input type="button" name="about" id="about" value="ABOUT" /><br/>
</body>
</html>