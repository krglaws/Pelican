<?php

$dir = '../uploads';
$videos = scandir($dir);

foreach ($videos as $vid){
	echo $vid;
}