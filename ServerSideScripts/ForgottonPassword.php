<?php
	error_reporting(E_ERROR | E_PARSE);
	$dateTime = date('Y-m-d H:i:s', time());
	$date = date('Y-m-d', time());

	$mysqli = new mysqli("localhost", "kashifir_user1", "Fastnu72!","kashifir_db1");
	
	if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
	}

	/* grab the posts from the db */
	
	$p_string = $json_str = file_get_contents('php://input');

	/*
	$p_string = '
	
	{"AddressLine1":"","AddressLine2":"","City":"","Country":"","EmailAddress":"kashif.ir@gmail.com","FirstName":"","Id":23,"IsEmailVerified":0,"IsLoggedIn":0,"IsSynched":1,"LastName":"","MiddleName":"","Password":"005e408f4905098f877eab7cf97ce9f0bc706994aa759eb4aa4653cf443e45fb9a0fa831986fabc0e6afce2c243f9605acc6fd3fd4e4a8fe0d16734301f1937f","ServerId":23,"ShowUnreadStoriesOnly":0,"SkypeId":"","SyncDuration":30,"Token":0,"UpdatedAt":"2018-07-28 13:10:15","WatsAppNo":""}
	
	';
	*/

	$query = "INSERT INTO AndroidProjects_Requests(Request) VALUES ('$p_string')";
	$mysqli->query($query) or die('Errant query:  '.$query);
	$logId = $mysqli->insert_id;
	$isLocked = 0;
	
	$json_obj = json_decode($p_string, true);
	$results = [];
	$user = $json_obj;
	$msg = "";
				$userEmailAddress = $user['EmailAddress'];
				$query = "SELECT Id, count(*) as rowCount, MAX(IsLocked) IsLocked, Token, Password, IsEmailVerified FROM AndroidProjects_Users WHERE EmailAddress = '".$user['EmailAddress']."'";
				$result = $mysqli->query($query) or die('Errant query:  '.$query);
				$row = $result->fetch_array(MYSQLI_ASSOC);
				if($row['rowCount'] == 0){
					$msg = "Eamail Address does not exist. Please correct it or SignUp.";
				}ELSE{
							$query = "SELECT COUNT(*) AS rowCount, `Token` FROM `EmailTokens` WHERE `Email` = '".$user['EmailAddress']."' AND `Date` = '$date'";
							$result = $mysqli->query($query) or die('Errant query:  '.$query);
							$row2 = $result->fetch_array(MYSQLI_ASSOC);
							if($row2['rowCount'] == 0){
								$rand = rand(10000,99999);
								$query = "INSERT INTO EmailTokens(Email, Date, Token) VALUES( '".$user['EmailAddress']."','$date',$rand )";
								$mysqli->query($query) or die('Errant query:  '.$query);
								
								$msgEmail = "Please use Token: ".$rand." on Login/Password Reset Screen to verify email address on Software Projects Android Application.
								Some one has registered your email address on Software Projects Android App. If its not you, don't need to bother about.";
								$msgEmail = wordwrap($msgEmail,100);
								mail($user['EmailAddress'],"Email Verification Token Software Projects Android App",$msgEmail);

								$msg = "Token is sent in email, please provide the Token";
							}elseif($user['Token'] == 0){
								$msg = "Please provide Token! Token is sent in today's email!";
							}
							elseif($row2['Token'] != $user['Token']){
								$msg = "Invalid Token! Token is sent in today's email";
							} else {
								// User Password and Token Matched
								$query = "UPDATE AndroidProjects_Users SET IsEmailVerified = 1, Password='".$user['Password']."' WHERE Id = ".$row['Id']; 
								$mysqli->query($query);
								$msg = "Success";
							}
				}

	$results['UpdatedAt'] = $dateTime;
	$results['Id'] = $row['Id'];
	$results['msg']=$msg;
	$response = json_encode(array('results'=>$results));

	header('Content-type: application/json');
	echo $response;
	$query = "UPDATE AndroidProjects_Requests SET Response = '$response' WHERE Id = $logId";
	$mysqli->query($query) or die('Errant query:  '.$query);

	/* disconnect from the db */
	@mysqli_close($link);
?>