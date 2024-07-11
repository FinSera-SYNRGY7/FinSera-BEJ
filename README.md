# Auth API

---

## Login User

### Request :
* Method : `POST`
* Endpoint : /v1/auth/login
* Headers :
    * Content-type : application/json
    * Accept : application/json

### Body :
````json
{
  "username" : "string",
  "mpin" : "string"
}
````

## Response : <span style="color: green;">200 OK</span>
````json
{
  "status" : "success",
  "message" : "Login success",
  "data" : {
    "token" : "string, JWT",
    "userDetails" : {
      "username" : "string",
      "status" : "enum"
    }
  }
}
````

## Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Username or Password is invalid",
  "data" : null
}
````
---

## Re-Login User
### Request :
* Method : `POST`
* Endpoint : /v1/auth/relogin
* Headers :
    * Content-type : application/json
    * Accept : application/json

### Body :
````json
{
  "mpin" : "string"
}
````

## Response : <span style="color: green;">200 OK</span>
````json
{
  "status" : "success",
  "message" : "Login success",
  "data" : {
    "userDetails" : {
      "username" : "string",
      "status" : "enum"
    }
  }
}
````

## Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Pin is invalid",
  "data" : null
}
````

---

## Forgot Password

### Request :
* Method : POST
* Endpoint : /v1/auth/forgot-password
* Headers :
    * Content-type : application/json
    * Accept : application/json

### Body :
````json
{
  "idCustomers" : "long",
  "accountNumber" : "string",
  "mpin" : "string"
}
````

### Response : <span style="color: green;">200 OK</span>
````json
{
  "status" : "success",
  "message" : "Password reset instructions sent to your email"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Invalid username"
}
````
---

## Setup New Password
### Request :
* Method : POST
* Endpoint : /v1/auth/setup-new-password
* Headers :
    * Content-type : application/json
    * Accept : application/json

### Body :
````json
{
  "username" : "string",
  "newPassword" : "string",
  "otpCode" : "string"
}
````

### Response : <span style="color: green;">200 OK</span>
````json
{
  "status" : "success",
  "message" : "Password successfully updated"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Invalid username"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Password does not meet criteria"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "status" : "error",
  "message" : "Invalid OTP"
}
````

