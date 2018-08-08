<?php
	error_reporting(E_ERROR | E_PARSE);
	$dateTime = date('Y-m-d H:i:s', time());
	$date = date('Y-m-d', time());

	$mysqli = new mysqli("localhost", "kashifir_consultant", "Fastnu72!","kashifir_consultant");
	
	if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
	}


	/* grab the posts from the db */
	
	$p_string = $json_str = file_get_contents('php://input');

	/*
	$p_string = '
	
	{"CreatedAt":"2018-07-27 18:22:58","EmailAddress":"kashif.ir@gmail.com","Id":0,"IsEmailVerified":0,"IsLoggedIn":0,"IsSynched":0,"Password":"7de5f8fa3769eef36c41d9659272a91b6cbdd13b903dde639e4b0a360a1e8caed3b752a0119fd2590f076958d21ddb0f16042a5c5768ce0a2c65ee3ae3a362ed","ServerId":0,"ShowUnreadStoriesOnly":0,"SyncDuration":0,"Token":0,"UpdatedAt":"2018-07-27 18:22:58"}
	
	';
	*/

	$query = "INSERT INTO AndroidProjects_Requests(Request) VALUES ('$p_string')";
	$mysqli->query($query) or die('Errant query:  '.$query);
	$logId = $mysqli->insert_id;
	$isLocked = 0;
	
	$json_obj = json_decode($p_string, true);
	$userArr = [];
	$projArr = [];
	$results = [];
	$user = $json_obj;
	$msg = "";
	
				$userEmailAddress = $user['EmailAddress'];
				$query = "SELECT Id, count(*) as rowCount, MAX(IsLocked) IsLocked FROM AndroidProjects_Users WHERE EmailAddress = '".$user['EmailAddress']."'";
				$result = $mysqli->query($query) or die('Errant query:  '.$query);
				$row = $result->fetch_array(MYSQLI_ASSOC);
				if($row['rowCount'] == 0){
					$rand = rand(10000,99999);
					$query = "INSERT INTO AndroidProjects_Users (EmailAddress, UpdatedAt, CreatedAt, IsLocked, Password, IsEmailVerified, IsLoggedIn, Token)
							VALUES('".$user['EmailAddress']."','". $dateTime ."','".$dateTime."',1,'".$user['Password']."',0,0,".$rand.")";

					$mysqli->query($query) or die('Errant query:  '.$query);
					$userId=$mysqli->insert_id;
					
					// User Registration Alert to Admin
					$msgEmail = "New User Registered Consultant Android App: ".$user['EmailAddress'];
					$msgEmail = wordwrap($msgEmail,100);
					mail("kashif@kashifirshad.com","New User Registered Consultant Android App",$msgEmail);
					
					$msg = "Success";
					
					$query = "INSERT INTO EmailTokens(Email, Date, Token) VALUES( '".$user['EmailAddress']."','$date',$rand )";
					$mysqli->query($query) or die('Errant query:  '.$query);
					
					$msgEmail = "Please use Token: ".$rand." on Login/Password Reset Screen to verify email address on Consultant Android Application. 
					Some one has registered your email address on Software Projects Android App. 
					If its not you, don't need to bother about.";
					$msgEmail = wordwrap($msgEmail,100);
					mail($user['EmailAddress'],"Email Verification Token Consultant Android App",$msgEmail);
					
				}ELSE{
					$msg = "Eamail Address already exists. Reset password if not remember";
				}


	
				

	$results['UpdatedAt'] = $dateTime;
	$results['Id'] = $userId;
	$results['msg']=$msg;
	
	
	$query = "UPDATE AndroidProjects_Users 
					SET IsLocked = 0
					   WHERE Id = ".array_values($userArr)[0]; 
	$mysqli->query($query);

	$response = json_encode(array('results'=>$results));
	header('Content-type: application/json');
	echo $response;

	$query = "UPDATE AndroidProjects_Requests SET Response = '$response' WHERE Id = $logId";
	$mysqli->query($query) or die('Errant query:  '.$query);
	
	/* disconnect from the db */
	@mysqli_close($link);
?>