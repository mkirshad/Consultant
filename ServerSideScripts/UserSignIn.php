<?php
	error_reporting(E_ERROR | E_PARSE);
	$dateTime = date('Y-m-d H:i:s', time());

	$mysqli = new mysqli("localhost", "kashifir_user1", "Fastnu72!","kashifir_db1");
	
	if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
	}

	/* grab the posts from the db */
	
	$p_string = $json_str = file_get_contents('php://input');

	/*
	$p_string = '
	
	{"LastUpdatedProject":"0000-00-00 00:00:00","LastUpdatedUser":"0000-00-00 00:00:00","projects":[],"user":{"EmailAddress":"fauzia.kashif09@gmail.com","IsSynched":0,"Id":1,"ServerId":0,"ShowUnreadStoriesOnly":0,"SyncDuration":1}}
	
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
					if($row['Password'] != $user['Password']){
						$msg = "Invalid Password! If you have forgotton please reset";
					}else{
						if($row['IsEmailVerified'] !=1 ){
							$query = "SELECT `Token` FROM `EmailTokens` WHERE `Id` = (SELECT Max(Id) FROM EmailTokens WHERE Email = '".$user['EmailAddress']."')";
							$result = $mysqli->query($query) or die('Errant query:  '.$query);
							$row2 = $result->fetch_array(MYSQLI_ASSOC);
							if($row2['Token'] != $user['Token']){
								$msg = "Invalid Token! Please check your email for token";
							} else {
								// User Password and Token Matched
								$query = "UPDATE AndroidProjects_Users 
											SET IsEmailVerified = 1
										  WHERE Id = ".$row['Id']; 
								$mysqli->query($query);
								$msg = "Success";
							}
						} else {
							$msg = "Success";
						}
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