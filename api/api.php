<?php

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

class api{

    private $conn,$table_name,$token,$values;
    public $obj_url;

    public function __construct(){
        require_once "./config/database.php";
        require_once "./bin/AES.php";
        require_once "./bin/check.php";

        $database = new database();
        $this->conn = $database->getConnection();
        $this->check = new check();
        $this->ASE = new AES();

        $status_connection = $database->getStatus();

        if ($status_connection === "true"){
            $this->get_Url();

            if ($this->obj_url->token === "Jiojio000608."){
                $this->call_function($this->obj_url->function_name,[]);
            }else{
                echo "{status:false}";
            }
        }else{
            echo "{status:false}";
        }
    }



    //query contient
    public function get_Url(){
        $url = $_SERVER["HTTP_HOST"] . $_SERVER["REQUEST_URI"];
        $url_parse = parse_url($url);
        $url_query = $this->convertUrlArray($url_parse["query"]);
        $list_parse = array();

        foreach ($url_query as $list){
            array_push($list_parse,$this->ASE->decrypt($list));
        }

        $objArray = new ObjArray($list_parse[0], $list_parse[1], $list_parse[2],$list_parse[3]);

        $valuesArray = array();
        $splitValues = explode(',', substr($list_parse[3], 1, -1));
        foreach ($splitValues as $value) {
            list($key, $val) = explode(':', $value);
            $valuesArray[$key] = $val;
        }

        $objArray->values = $valuesArray;

        $this->obj_url = $objArray;

    }

    public function convertUrlArray($query)
    {
        $queryParts = explode('&', $query);
        $params = array();
        foreach ($queryParts as $param) {
            $item = explode('=', $param);
            $params[$item[0]] = $item[1];
        }
        return $params;
    }

    public function call_function($name_function,$options){
        return call_user_func_array(array($this,$name_function),$options);
    }

    // {email:houzeyu7@gmail.com,password:Jiojio000608.}
    public function login(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_partients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_partients->bindParam(':email',$this->obj_url->values["email"]);
        $stmt_doctors->bindParam(':email',$this->obj_url->values["email"]);

        $stmt_partients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_partients->fetch(PDO::FETCH_ASSOC);
        $res_doctors = $stmt_doctors->fetch(PDO::FETCH_ASSOC);
        
        if ($res_doctors === false ){
            if (password_verify($this->obj_url->values["password"],$res_patients["password"])){
                $res_patients["type"] = "patient";
                $res_patients["login"] = "true";
                echo json_encode($res_patients);
            }else{
                $message = [
                    "login" => "false"
                ];
                echo json_encode($message);
            }
        }else{
            if (password_verify($this->obj_url->values["password"],$res_doctors["password"])){
                $res_doctors["type"] = "doctor";
                $res_doctors["login"] = "true";
                $res_doctors["type"] = "docteur";
                echo json_encode($res_doctors);
            }else{
                $message = [
                    "login" => "false"
                ];
                echo json_encode($message);
            }

        }
    }

    // {key:d} Utilisé pour rechercher des médecins Compatible avec la recherche floue
    public function find_doctor() {

        $sql = "SELECT * FROM doctors WHERE first_name LIKE :key1 OR last_name LIKE :key2";
        $stmt = $this->conn->prepare($sql);
        $search_keyword = '%' . $this->obj_url->values["key"] . '%';

        $stmt->bindParam(':key1', $search_keyword);
        $stmt->bindParam(':key2', $search_keyword);

        $stmt->execute();
        $res_all_doctors = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res_all_doctors);
    }

    //{doctor_id:1} Médecin pour rechercher tous les rendez-vous en attente
    public function find_patient_Rdv(){
        $sql = "SELECT * FROM appointments WHERE doctor_availability_id IN 
                                 (SELECT id FROM doctor_availabilities WHERE doctor_id = :id)";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(':id',$this->obj_url->values["doctor_id"]);

        $stmt->execute();

        $res_all_patients = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res_all_patients);
    }

    //{email:houzeyu7@gmail.com}
    public function find_user_By_email(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_patients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patients->bindParam(':email',$this->obj_url->values["email"]);
        $stmt_doctors->bindParam(':email',$this->obj_url->values["email"]);

        $stmt_patients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_patients->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);

        if (count($res_patients) > 0){
            $res_patients["type"] = "patient";
            echo json_encode($res_patients);
        }else{
            $res_doctor["type"] = "doctor";
            echo json_encode($res_doctor);
        }
    }

    public function find_user(){
        $sql_patients = "SELECT * FROM patients";
        $sql_doctors = "SELECT * FROM doctors";

        $stmt_patients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_patients->fetchall(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetchall(PDO::FETCH_ASSOC);

        if (count($res_patients) > 0){
            foreach($res_patients as $res){
                $res["type"] = "patient";
            }
            echo json_encode($res_patients);
        }else{
            foreach($res_doctor as $res){
                $res["type"] = "doctor";
            }
            
            echo json_encode($res_doctor);
        }
    }

    //{id:11}
    public function find_user_By_id(){
        $sql_patients = "SELECT * FROM patients WHERE id = :id";
        $sql_doctors = "SELECT * FROM doctors WHERE id = :id";

        $stmt_patients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patients->bindParam(':id',$this->obj_url->values["id"]);
        $stmt_doctors->bindParam(':id',$this->obj_url->values["id"]);

        $stmt_patients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_patients->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);

        if ($res_patients){
            $res_patients["type"] = "patient";
            echo json_encode($res_patients);
        }else{
            $res_doctor["type"] = "doctor";
            echo json_encode($res_doctor);
        }
    }

    //{email:houzeyu7@gmail.com,type:patient,password:Jiojio000104.}
    public function change_password(){
        $type = $this->obj_url->values["type"];

        if ($type === "patient"){
            $sql = "UPDATE patients SET password = :password WHERE email = :email";
            $stmt = $this->conn->prepare($sql);

            $stmt->bindParam("password",$this->obj_url->values["password"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $stmt->execute();

            echo json_encode("true patient");
        }else{
            $sql = "UPDATE doctors SET password = :password WHERE email = :email";
            $stmt = $this->conn->prepare($sql);

            $stmt->bindParam("password",$this->obj_url->values["password"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);

            $stmt->execute();

            echo json_encode("true doctor");
        }
    }

    //{email:houzeyu7@gmail.com}
    public function check_user_double(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_patient = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patient->bindParam("email",$this->obj_url->values["email"]);
        $stmt_doctors->bindParam("email",$this->obj_url->values["email"]);

        $stmt_patient->execute();
        $stmt_doctors->execute();

        $res_patient = $stmt_patient->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);



        if ($res_patient !== false || $res_doctor !== false){
            if ($res_patient !== false){
                $message = [
                    "type" => "patient",
                    "user_existe" => "true"
                ];
                echo json_encode($message);
            }else{
                $message = [
                    "type" => "doctor",
                    "user_existe" => "true"
                ];
                echo json_encode($message);
            }
        }else{
            $message = [
                "type" => "null",
                "user_existe" => "false"
            ];
            echo json_encode($message);
        }
    }

    //{type:doctor,email:sophiabrown@example.com,phone_number:000122333,office_address:3333333}
    //{type:patient,email:sophiabrown@example.com,first_name:hou,last_name:zeyu,birthdate:1997-05-12,,phone_number:000122333,address:3333333}
    public function update_user_info(){
        if ($this->obj_url->values["type"] === "patient"){
            $sql = "UPDATE patients SET first_name = :first_name,last_name = :last_name,email = :email,phone_number = :phone_number,address = :address WHERE id = :id";
            $stmt = $this->conn->prepare($sql);

            $stmt->bindParam("first_name",$this->obj_url->values["first_name"]);
            $stmt->bindParam("last_name",$this->obj_url->values["last_name"]);
            $stmt->bindParam("email",$this->obj_url->values["email"]);
            $stmt->bindParam("phone_number",$this->obj_url->values["phone_number"]);
            $stmt->bindParam("address",$this->obj_url->values["address"]);
            $stmt->bindParam("id",$this->obj_url->values["id"]);

            $res = $stmt->execute();

            $message = [
                "response" => $res
            ];

            echo json_encode($message);
        }else{
            $sql = "UPDATE doctors SET phone_number = :phone_number,office_address = :office_address WHERE id = :id";
            $stmt = $this->conn->prepare($sql);
            $stmt->bindParam("phone_number",$this->obj_url->values["phone_number"]);
            $stmt->bindParam("office_address",$this->obj_url->values["office_address"]);
            $stmt->bindParam("id",$this->obj_url->values["id"]);

            $res = $stmt->execute();

            $message = [
                "response" => $res
            ];

            echo json_encode($message);
        }
    }

    //{id_patient:11}
    public function find_historique_By_patientId(){
        $sql = "SELECT doctors.first_name,doctors.specialty,doctors.last_name,doctors.office_address,available_from,available_to,specialty.specialty 
                    FROM doctor_availabilities JOIN doctors ON doctors.id = doctor_availabilities.doctor_id 
                        JOIN specialty ON doctors.specialty = specialty.id
                            WHERE doctor_availabilities.available_from < NOW() AND doctor_availabilities.patient_id = :id_patient";

        $stmt = $this->conn->prepare($sql);

        $stmt->bindParam("id_patient",$this->obj_url->values["id_patient"]);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);

    }

    public function inscription() {
        if ($this->obj_url->values["type"] === "patient"){
            $sql_patient = "INSERT INTO patients (first_name, last_name, birthdate, gender, phone_number, email, address, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            $stmt_patient = $this->conn->prepare($sql_patient);

            $first_name = $this->obj_url->values["first_name"];
            $last_name = $this->obj_url->values["last_name"];
            $birthdate = $this->obj_url->values["birthdate"];
            $gender = $this->obj_url->values["genre"];
            $phone_number = $this->obj_url->values["phone_number"];
            $email = $this->obj_url->values["email"];
            $address = $this->obj_url->values["address"];
            $password = $this->obj_url->values["password"];

            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                throw new InvalidArgumentException("Invalid email format");
            }

            $hashed_password = password_hash($password, PASSWORD_DEFAULT);

            $stmt_patient->bindParam(1, $first_name);
            $stmt_patient->bindParam(2, $last_name);
            $stmt_patient->bindParam(3, $birthdate);
            $stmt_patient->bindParam(4, $gender);
            $stmt_patient->bindParam(5, $phone_number);
            $stmt_patient->bindParam(6, $email);
            $stmt_patient->bindParam(7, $address);
            $stmt_patient->bindParam(8, $hashed_password);

            $result = $stmt_patient->execute();

            if ($result) {
                $message = [
                    "res" => "true"
                ];
                echo json_encode($message);
            } else {
                $message = [
                    "res" => "false"
                ];
                echo json_encode($message);
            }
        }else{
            $sql_doc = "INSERT INTO doctors (first_name, last_name, specialty, phone_number, email, office_address, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

            $stmt_doc = $this->conn->prepare($sql_doc);

            $first_name = $this->obj_url->values["first_name"];
            $last_name = $this->obj_url->values["last_name"];
            $specialty= $this->obj_url->values["specialty"];
            $phone_number = $this->obj_url->values["phone_number"];
            $email = $this->obj_url->values["email"];
            $office_address = $this->obj_url->values["office_address"];
            $password = $this->obj_url->values["password"];

            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                throw new InvalidArgumentException("Invalid email format");
            }

            $hashed_password = password_hash($password, PASSWORD_DEFAULT);

            $stmt_doc->bindParam(1, $first_name);
            $stmt_doc->bindParam(2, $last_name);
            $stmt_doc->bindParam(3, $specialty);
            $stmt_doc->bindParam(4, $phone_number);
            $stmt_doc->bindParam(5, $email);
            $stmt_doc->bindParam(6, $office_address);
            $stmt_doc->bindParam(7, $hashed_password);

            $result = $stmt_doc->execute();

            if ($result) {
                $message = [
                    "res" => "true"
                ];
                echo json_encode($message);
            } else {
                $message = [
                    "res" => "false"
                ];
                echo json_encode($message);
            }
        }

    }

    public function find_doctors_By_key(){
        $sql = "SELECT doctors.first_name,doctors.last_name,doctors.email,doctors.phone_number,doctors.office_address,specialty.specialty 
                    FROM doctors JOIN specialty ON specialty.id = doctors.specialty
                        WHERE doctors.first_name LIKE :key OR doctors.last_name LIKE :key OR specialty.specialty LIKE :key";

        $stmt = $this->conn->prepare($sql);
        $key = "%" . $this->obj_url->values["key"] ."%";

        $stmt->bindParam("key",$key);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_rdv_By_doctor_email(){

        $sql = "SELECT doctor_availabilities.id,doctors.first_name,doctors.last_name,specialty.specialty,doctors.office_address,doctors.email,doctor_availabilities.available_from,doctor_availabilities.available_to 
                    FROM doctors JOIN doctor_availabilities ON doctors.id = doctor_availabilities.doctor_id 
                        JOIN specialty ON specialty.id = doctors.specialty
                            WHERE doctors.email = :email AND doctor_availabilities.patient_id IS NULL";

        $stmt = $this->conn->prepare($sql);

        $stmt->bindParam(":email",$this->obj_url->values["doctor_email"]);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_specialty(){
        $sql = "SELECT specialty FROM specialty";

        $stmt = $this->conn->prepare($sql);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_specialty_id(){
        $sql = "SELECT id FROM specialty WHERE specialty = :specialty";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(':specialty',$this->obj_url->values["specialty"]);
        $stmt->execute();

        $res = $stmt->fetch(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function set_specialty(){
        $sql = "UPDATE doctors set specialty = :id WHERE email = :email";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(':email',$this->obj_url->values["email"]);
        $stmt->bindParam(':id',$this->obj_url->values["id"]);

        echo json_encode($stmt->execute());
    }

    //{email:houzeyu7@gmail.com,phone:0695887744}
    public function check_user_phone_email(){
        $sql_patients = "SELECT * FROM patients WHERE email = :email";
        $sql_doctors = "SELECT * FROM doctors WHERE email = :email";

        $stmt_patients = $this->conn->prepare($sql_patients);
        $stmt_doctors = $this->conn->prepare($sql_doctors);

        $stmt_patients->bindParam(':email',$this->obj_url->values["email"]);
        $stmt_doctors->bindParam(':email',$this->obj_url->values["email"]);

        $stmt_patients->execute();
        $stmt_doctors->execute();

        $res_patients = $stmt_patients->fetch(PDO::FETCH_ASSOC);
        $res_doctor = $stmt_doctors->fetch(PDO::FETCH_ASSOC);
        if ($res_patients){
            $sql_check_patient = "SELECT id FROM patients WHERE SUBSTRING(phone_number, -4) = :phone";
            $stmt_check_patients = $this->conn->prepare($sql_check_patient);
            $stmt_check_patients->bindParam("phone",$this->obj_url->values["phone"]);
            $stmt_check_patients->execute();
            $user_id = $stmt_check_patients->fetch(PDO::FETCH_ASSOC);
            if ($user_id){
                $user_id["type"] = "patients";
                echo json_encode($user_id);
            }else{
                $message = [
                    "id" => $user_id
                ];
                echo json_encode($message);
            }

        }else{
            $sql_check_doctor = "SELECT id FROM doctors WHERE SUBSTRING(phone_number, -4) = :phone";
            $stmt_check_doctors = $this->conn->prepare($sql_check_doctor);
            $stmt_check_doctors->bindParam("phone",$this->obj_url->values["phone"]);
            $stmt_check_doctors->execute();
            $user_id = $stmt_check_doctors->fetch(PDO::FETCH_ASSOC);
            if ($user_id){
                $user_id["type"] = "doctors";
                echo json_encode($user_id);
            }else{
                $message = [
                    "id" => $user_id
                ];
                echo json_encode($message);
            }
        }
    }

    public function update_password_By_userId(){
        $type = $this->obj_url->values["type"];

        $sql = "UPDATE " .$type . " SET password = :password WHERE id = :id";

        $stmt = $this->conn->prepare($sql);
        $password_hash = password_hash($this->obj_url->values["password"], PASSWORD_DEFAULT);
        $stmt->bindParam("password",$password_hash);
        $stmt->bindParam("id",$this->obj_url->values["id"]);


        $message = [
            "res" => $stmt->execute()
        ];

        echo json_encode($message);
    }

    // {id:patient_id}
    public function get_message_patient(){

        $sql = "SELECT message.*,doctors.first_name,doctors.last_name FROM message 
                    JOIN doctors ON message.id_doctor = doctors.id 
                        WHERE message.id_patient = :id_patient";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_patient",$this->obj_url->values["id_patient"]);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_doctor(){

        $sql = "SELECT message.*,patients.first_name,patients.last_name FROM message 
                    JOIN patients ON message.id_patient = patients.id 
                        WHERE message.id_doctor = :id_doctor";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_doctor",$this->obj_url->values["id_doctor"]);

        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_patient_by_etat(){
        $sql = "SELECT message.*,doctors.first_name,doctors.last_name FROM message 
                    JOIN doctors ON message.id_doctor = doctors.id 
                        WHERE message.id_patient = :id_patient AND message.etat = :etat";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_patient",$this->obj_url->values["id_patient"]);
        $stmt->bindParam("etat",$this->obj_url->values["etat"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_doctor_by_etat(){
        $sql = "SELECT message.*,patients.first_name,patients.last_name FROM message 
                    JOIN patients ON message.id_patient = patients.id 
                        WHERE message.id_doctor = :id_doctor AND message.etat = :etat";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_doctor",$this->obj_url->values["id_doctor"]);
        $stmt->bindParam("etat",$this->obj_url->values["etat"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_patient_orderBy_time(){
        $sql = "SELECT message.*,doctors.first_name,doctors.last_name FROM message 
                    JOIN doctors ON message.id_doctor = doctors.id 
                        WHERE message.id_patient = :id_patient 
                        ORDER BY message.date_public DESC";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_patient",$this->obj_url->values["id_patient"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_doctor_orderBy_time(){
        $sql = "SELECT message.*,patients.first_name,patients.last_name FROM message 
                    JOIN patients ON message.id_patient = patients.id 
                        WHERE message.id_doctor = :id_doctor 
                        ORDER BY message.date_public DESC";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_doctor",$this->obj_url->values["id_doctor"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_patient_By_sender(){
        $sql = "SELECT message.*,doctors.first_name,doctors.last_name FROM message 
                    JOIN doctors ON message.id_doctor = doctors.id 
                        WHERE message.id_patient = :id_patient AND message.sender = :sender";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_patient",$this->obj_url->values["id_patient"]);
        $stmt->bindParam("sender",$this->obj_url->values["sender"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_doctor_By_sender(){
        $sql = "SELECT message.*,patients.first_name,patients.last_name FROM message 
                    JOIN patients ON message.id_patient = patients.id 
                        WHERE message.id_doctor = :id_doctor AND message.sender = :sender";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id_doctor",$this->obj_url->values["id_doctor"]);
        $stmt->bindParam("sender",$this->obj_url->values["sender"]);
        $stmt->execute();

        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function get_message_details() {
        $sql = "SELECT message.*, doctors.first_name AS doctor_first_name, doctors.last_name AS doctor_last_name, patients.first_name AS patient_first_name, patients.last_name AS patient_last_name 
            FROM message
            JOIN doctors ON message.id_doctor = doctors.id
            JOIN patients ON message.id_patient = patients.id
            WHERE message.id = :id";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id", $this->obj_url->values["id"]);
        $stmt->execute();

        $res = $stmt->fetch(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function set_message_lu(){
        $sql = "UPDATE message SET etat = 1 WHERE id = :id";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam("id", $this->obj_url->values["id"]);
        echo json_encode($stmt->execute());
    }

    public function send_message_patient()
    {
        $sql = "INSERT INTO message (`title`, `contenu`, `id_patient`, `id_doctor`, `date_public`, `etat`, `last_review`, `sender`) 
            SELECT :title, :contenu, :id_patient, doctors.id, NOW(), '0', NOW(), '0' FROM doctors WHERE doctors.email = :mail";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":title", $this->obj_url->values["obj"]);
        $stmt->bindParam(":contenu", $this->obj_url->values["contenu"]);
        $stmt->bindParam(":id_patient", $this->obj_url->values["id_patient"]);
        $stmt->bindParam(":mail", $this->obj_url->values["email"]);
        $message = [
            'message' => $stmt->execute()
        ];
        echo json_encode($message);
    }

    public function send_message_doctor()
    {
        $sql = "INSERT INTO message (`title`, `contenu`, `id_patient`, `id_doctor`, `date_public`, `etat`, `last_review`, `sender`) 
            SELECT :title, :contenu, patients.id, :id_doctor, NOW(), '0', NOW(), '0' FROM patients WHERE patients.email = :mail";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":title", $this->obj_url->values["obj"]);
        $stmt->bindParam(":contenu", $this->obj_url->values["contenu"]);
        $stmt->bindParam(":id_doctor", $this->obj_url->values["id_doctor"]);
        $stmt->bindParam(":mail", $this->obj_url->values["email"]);
        $message = [
            'message' => $stmt->execute()
        ];
        echo json_encode($message);
    }

    public function get_all_rdvByDoctorID(){
        $sql = "SELECT doctor_availabilities.*, patients.first_name, patients.last_name,patients.email
                FROM doctor_availabilities 
                LEFT JOIN patients ON doctor_availabilities.patient_id = patients.id  
                WHERE doctor_availabilities.doctor_id = :id";


        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":id",$this->obj_url->values["id"]);

        $stmt->execute();
        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($res);
    }

    public function get_all_rdvByDoctorID_passe(){
        $sql = "SELECT doctor_availabilities.*, patients.first_name, patients.last_name,patients.email
                FROM doctor_availabilities 
                LEFT JOIN patients ON doctor_availabilities.patient_id = patients.id  
                WHERE doctor_availabilities.doctor_id = :id AND doctor_availabilities.available_from < NOW()";


        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":id",$this->obj_url->values["id"]);

        $stmt->execute();
        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($res);
    }

    public function get_all_rdvByDoctorID_future(){
        $sql = "SELECT doctor_availabilities.*, patients.first_name, patients.last_name,patients.email
                FROM doctor_availabilities 
                LEFT JOIN patients ON doctor_availabilities.patient_id = patients.id  
                WHERE doctor_availabilities.doctor_id = :id AND doctor_availabilities.available_from > NOW()";


        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":id",$this->obj_url->values["id"]);

        $stmt->execute();
        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($res);
    }

    //{id:id_doctor ou id_patient}
    public function get_rdv_Byid(){
        $sql = "SELECT doctor_availabilities.* FROM doctor_availabilities WHERE id = :id;";

        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":id",$this->obj_url->values["id"]);

        $stmt->execute();
        $res = $stmt->fetch(PDO::FETCH_ASSOC);
        if(!empty($res['patient_id'])){
            $sql = "SELECT doctor_availabilities.*, patients.* FROM doctor_availabilities 
                LEFT JOIN patients ON doctor_availabilities.patient_id = patients.id  
                WHERE doctor_availabilities.id = :id";

            $stmt = $this->conn->prepare($sql);
            $stmt->bindParam(":id",$this->obj_url->values["id"]);

            $stmt->execute();
            $res = $stmt->fetch(PDO::FETCH_ASSOC);
            echo json_encode($res);
        }else{
            echo json_encode($res);
        }
    }

    public function delete_rdv_Byid(){
        $sql = "DELETE FROM doctor_availabilities WHERE id = :id";
        $stmt = $this->conn->prepare($sql);
        $stmt->bindParam(":id",$this->obj_url->values["id"]);


        echo json_encode($stmt->execute());
    }

    public function add_rdv(){
        $sql = "INSERT INTO doctor_availabilities (doctor_id, available_from, available_to, patient_id) VALUES (?, ?, ?, null)";

        $stmt = $this->conn->prepare($sql);

        $stmt->bindParam(1, $this->obj_url->values["doctor_id"]);
        $stmt->bindParam(2, $this->obj_url->values["available_from"]);
        $stmt->bindParam(3, $this->obj_url->values["available_to"]);

        echo json_encode($stmt->execute());
    }

    public function get_all_rdvDisponibleBydoctorEmail(){
        $sql = "SELECT doctor_availabilities.* FROM doctors 
                    JOIN doctor_availabilities ON doctors.id = doctor_availabilities.doctor_id 
                    WHERE doctors.email = :email AND doctor_availabilities.patient_id IS NULL";

        $stmt = $this->conn->prepare($sql);

        $stmt->bindParam("email", $this->obj_url->values["email"]);

        $stmt->execute();
        $res = $stmt->fetchAll(PDO::FETCH_ASSOC);
        echo json_encode($res);
    }

    public function get_doctor(){
        $sql = "SELECT doctors.*,specialty.specialty FROM doctors JOIN specialty ON doctors.specialty = specialty.id WHERE doctors.email = :email";

        $stmt = $this->conn->prepare($sql);
        $test = "raffi@gmail.com";
        $stmt->bindParam(":email", $test);

        $stmt->execute();
        $res = $stmt->fetch(PDO::FETCH_ASSOC);

        echo json_encode($res);
    }

    public function update_rdv(){
        $sql = "UPDATE doctor_availabilities SET patient_id = :id_patient WHERE id = :id_rdv";

        $stmt = $this->conn->prepare($sql);

        $stmt->bindParam(":id_patient", $this->obj_url->values['id_patient']);
        $stmt->bindParam(":id_rdv", $this->obj_url->values['id_rdv']);

        echo json_encode($stmt->execute());
    }
}

class ObjArray {
    public $function_name;
    public $tablename;
    public $token;
    public $values;

    public function __construct($function_name,$tablename, $token, $values) {
        $this->function_name = $function_name;
        $this->tablename = $tablename;
        $this->token = $token;
        $this->values = $values;
    }
}

$api = new api();