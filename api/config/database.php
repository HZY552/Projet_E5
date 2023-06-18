<?php

class database{

    private $host = "localhost";
    private $database_name = "projet_e5";
    private $username = "root";
    private $password = "";

    public $conn,$status;

    public function getConnection(){
        $this->conn = null;

        try {
            $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->database_name,
                $this->username,
                $this->password
            );
            $this->status = "true";
        }catch (PDOException $exception){
            $this->status = "false";
            json_encode("{status:false}");
        }

        return $this->conn;
    }

    public function getStatus(){
        return $this->status;
    }

}