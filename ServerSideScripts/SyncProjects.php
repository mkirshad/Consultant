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
{"LastUpdatedProject":"2018-06-28 04:31:22","LastUpdatedUser":"2018-06-28 07:08:03","projects":[],"user":{"AddressLine1":"","AddressLine2":"","City":"","Country":"","EmailAddress":"kashif.ir@gmail.com","FirstName":"","Id":23,"IsEmailVerified":0,"IsLoggedIn":1,"IsSynched":0,"LastName":"","MiddleName":"","Password":"7de5f8fa3769eef36c41d9659272a91b6cbdd13b903dde639e4b0a360a1e8caed3b752a0119fd2590f076958d21ddb0f16042a5c5768ce0a2c65ee3ae3a362ed","ServerId":23,"ShowUnreadStoriesOnly":0,"SkypeId":"","State":"","SyncDuration":1,"Token":0,"WatsAppNo":""}}
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
	$user = $json_obj['user'];
	if($user['EmailAddress'] == 'kashif.ir@gmail.com'){
		$query = "SELECT *
			FROM AndroidProjects_Users 
			WHERE UpdatedAt > '".$json_obj['LastUpdatedUser']."'";
			$result = $mysqli->query($query) or die('Errant query:  '.$query);
			$userRows = $result->fetch_all(MYSQLI_ASSOC);
	}else{
		$userRows = [];
	}
	
//	if($user['IsSynched'] == 0 || $user['ServerId'] == 0){
				$userEmailAddress = $user['EmailAddress'];
				$query = "SELECT Id, count(*) as rowCount, MAX(IsLocked) IsLocked FROM AndroidProjects_Users WHERE EmailAddress = '".$user['EmailAddress']."'";
				$result = $mysqli->query($query) or die('Errant query:  '.$query);
				$row = $result->fetch_array(MYSQLI_ASSOC);
				if($row['rowCount'] == 0){
					$query = "INSERT INTO AndroidProjects_Users (FirstName, MiddleName, LastName, EmailAddress, SkypeId, WatsAppNo, AddressLine1, AddressLine2, 
																	City, State, Country, UnReadOnly, SyncDuration,UpdatedAt, CreatedAt, IsLocked)
							VALUES('".$user['FirstName']."','".$user['MiddleName']."','".$user['LastName']."','".$user['EmailAddress']."','".$user['SkypeId']."','"
							.$user['WatsAppNo']."','".$user['AddressLine1']."','".$user['AddressLine2']."','".$user['City']."','".$user['State']."','"
							.$user['Country']."','".$user['ShowUnreadStoriesOnly']."','".$user['SyncDuration']."','". $dateTime ."','".$dateTime."'". ",1)";
					
					$mysqli->query($query) or die('Errant query:  '.$query);
					$userArr[$user['Id']]=$mysqli->insert_id;
				}ELSE{
					$isLocked = $row['IsLocked'];
					$userArr[$user['Id']]=$row['Id'];
					$query = "UPDATE AndroidProjects_Users 
					SET FirstName = '".$user['FirstName']."'
					   ,MiddleName = '".$user['MiddleName']."'
					   , LastName = '".$user['LastName']."'
					   , SkypeId = '".$user['SkypeId']."'
					   , WatsAppNo = '".$user['WatsAppNo']."'
					   , AddressLine1 = '".$user['AddressLine1']."'
					   , AddressLine2 = '".$user['AddressLine2']."'
					   , City = '".$user['City']."'
					   , State = '".$user['State']."'
					   , Country = '".$user['Country']."'
					   , UnReadOnly = '".$user['ShowUnreadStoriesOnly']."'
					   , SyncDuration = '".$user['SyncDuration']."'
					   , UpdatedAt = '".$dateTime."'
					   , IsLocked = 1
					   WHERE Id = ". $row['Id'];
					   $mysqli->query($query);
				}
//			}
// if($isLocked == 0){
			foreach($json_obj['projects'] as $key => $proj){
			
						$projParent = 0;
						if($proj['ParentId'] != 0){
							$projParent = $projArr[$proj['ParentId']];
						}
							
						if($proj['ServerId'] == 0){
							$query = "INSERT INTO AndroidProjects_Projects (Story, FilePaths, EstimatedHrs, EstimateCost, DeliveryDate, UpdatedAt,CreatedAt, UserId, ParentId)
									VALUES('".$proj['Story']."','".$proj['FilePaths']."','".$proj['EstimatedHrs']."','".$proj['EstimateCost']."','".$proj['DeliveryDate']."','"
									.$dateTime."','". $dateTime."' ,'" . $userArr[$proj['UserId']] . "','".$projParent. "')";
							$mysqli->query($query) or die('Errant query:  '.$query);
							$projArr[$proj['Id']]=$mysqli->insert_id;
						}elseif($proj['IsSynched'] == 0){
							$projArr[$proj['Id']]=$proj['ServerId'];
							
							$query = "UPDATE AndroidProjects_Projects 
							SET Story = '".$proj['Story']."'
							   ,FilePaths = '".$proj['FilePaths']."'
							   , EstimatedHrs = '".$proj['EstimatedHrs']."'
							   , EstimateCost = '".$proj['EstimateCost']."'
							   , DeliveryDate = '".$proj['DeliveryDate']."'
							   , UpdatedAt = '".$dateTime."'
							   WHERE Id = ". $proj['ServerId'];
							   $mysqli->query($query);
							
						}else{
							$projArr[$proj['Id']]=$proj['ServerId'];
						}

						
						foreach($proj['ChildProjects'] as $key => $proj){
			
						$projParent = 0;
						if($proj['ParentId'] != 0){
							$projParent = $projArr[$proj['ParentId']];
						}
							
						if($proj['ServerId'] == 0){
							$query = "INSERT INTO AndroidProjects_Projects (Story, FilePaths, EstimatedHrs, EstimateCost, DeliveryDate, UpdatedAt,CreatedAt, UserId, ParentId)
									VALUES('".$proj['Story']."','".$proj['FilePaths']."','".$proj['EstimatedHrs']."','".$proj['EstimateCost']."','".$proj['DeliveryDate']."',
									'".$dateTime."','".$dateTime."','" . $userArr[$proj['UserId']] . "','".$projParent. "')";
							$mysqli->query($query) or die('Errant query:  '.$query);
							$projArr[$proj['Id']]=$mysqli->insert_id;
						}elseif($proj['IsSynched'] == 0){
							$projArr[$proj['Id']]=$proj['ServerId'];
							
							$query = "UPDATE AndroidProjects_Projects 
							SET Story = '".$proj['Story']."'
							   ,FilePaths = '".$proj['FilePaths']."'
							   , EstimatedHrs = '".$proj['EstimatedHrs']."'
							   , EstimateCost = '".$proj['EstimateCost']."'
							   , DeliveryDate = '".$proj['DeliveryDate']."'
							   , UpdatedAt = '".$dateTime."'
							   WHERE Id = ". $proj['ServerId'];
							   $mysqli->query($query);
							
						}else{
							$projArr[$proj['Id']]=$proj['ServerId'];
						}
				
				}
		
	}
	
	$queryExc = "";
	if(sizeof($projArr) > 0){
		$queryExc = " AND a.Id NOT IN (".implode(', ', $projArr).")";
	}
		
	$query = "SELECT a.Id AS ServerId, a.Story , a.FilePaths, a.EstimatedHrs, a.EstimateCost, a.DeliveryDate, a.CreatedAt, a.UpdatedAt, a.UserId, a.ParentId
	FROM AndroidProjects_Projects a LEFT JOIN  AndroidProjects_Projects b ON a.ParentId = b.Id
	WHERE (a.UpdatedAt > '".$json_obj['LastUpdatedProject']."' OR b.UpdatedAt > '".$json_obj['LastUpdatedProject']."')
	AND (a.UserId = '".array_values($userArr)[0]."' OR b.UserId = '".array_values($userArr)[0]."' OR '".$user['EmailAddress']."' = 'kashif.ir@gmail.com' )".
	 $queryExc
	 ." ORDER BY a.Id"
	;

	$result = $mysqli->query($query) or die('Errant query:  '.$query);
	$projRows = $result->fetch_all(MYSQLI_ASSOC);
				
				
	
				
	$UsersArr = [];
	foreach($userArr as $key=>$value){
		$UsersArr[]=array('UserId'=>$key, 'ServerUserId'=>$value);
	}
	
	$projsArr = [];
	foreach($projArr as $key=>$value){
		$projsArr[]=array('ProjectId'=>$key, 'ServerProjectId'=>$value);
	}				

	$results['UpdatedAt'] = $dateTime;
	$results['userIds'] = $UsersArr;
	$results['projectIds'] = $projsArr;
	$results['newProjects'] = $projRows;
	$results['newUsers'] = $userRows;
	
	
	$query = "UPDATE AndroidProjects_Users 
					SET IsLocked = 0
					   WHERE Id = ".array_values($userArr)[0]; 
	$mysqli->query($query);
// }
	$response = json_encode(array('results'=>$results));
	header('Content-type: application/json');
	echo $response;

	$query = "UPDATE AndroidProjects_Requests SET Response = '$response' WHERE Id = $logId";
	$mysqli->query($query) or die('Errant query:  '.$query);


	/* disconnect from the db */
	@mysqli_close($link);
?>