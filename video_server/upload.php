<?php
    $target_dir = "/home/kyle/uploads/";
    $target_file = $target_dir . basename($_FILES["uploaded_file"]["name"]);
    $tmp_file = $_FILES["uploaded_file"]["tmp_name"];
    $uploadOk = 1;
    $fileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

    // Check if file already exists
    if (file_exists($target_file)) {
        echo "Sorry, file already exists.";
        $uploadOk = 0;
    }
    // Check file size
    if ($_FILES["uploaded_file"]["size"] > 50000000) {
        echo "Sorry, your file is too large.";
        $uploadOk = 0;
    }
    // Allow certain file formats
    if($fileType != "mp4" && $fileType != "m4v") {
        echo "Sorry, only MP4 and M4V files are allowed.";
        $uploadOk = 0;
    }
    // Check if $uploadOk is set to 0 by an error
    if ($uploadOk == 0) {
        echo "Sorry, your file was not uploaded.";
    // if everything is ok, try to upload file
    } else {
        if (move_uploaded_file($tmp_file, $target_file)) {
            echo "The file ". basename($target_file). " has been uploaded.";
        } else {
            echo "Sorry, there was an error uploading your file.";
            echo "Temp file: " . $tmp_file;
            echo "Upload file: " . $target_file;
        }
    }
?>