
## Informasi Saldo API Spec

Endpoint : GET api/v1/amount

Authorization Type Bearer Token : "USER_TOKEN"

Response Body (succes) :

```json
{
    "code": 200,
    "message": "Nomor Rekening ditemukan",
    "status": true,
    "data": {
        "customerId": 1,
        "username": "John Doe",
        "accountNumber": "1234567890",
        "amount": {
            "amount": 10000.0,
            "currency": "IDR"
        }
    }
}
```
Response Body (failed) :

```json
{
    "message": "Nomor rekening tidak ditemukan",
    "status": 400
}
```

# Auth API

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
  "password" : "string"
}
````

### Response : <span style="color: green;">200 OK</span>
````json
{
  "data" : {
    "token" : "string, JWT",
    "username" : "string",
    "status" : "enum"
  },
  "message" : "Login success"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "data" : null,
  "message" : "Username or Password is invalid"
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

### Param :
````json
{
  "mpin" : "string"
}
````

### Response : <span style="color: green;">200 OK</span>
````json
{
  "data" : {
    "username" : "string",
    "status" : "enum"
  },
  "message" : "Login success"
}
````

### Error Response : <span style="color: red;">400 Bad Request</span>
````json
{
  "data" : null,
  "message" : "Pin is invalid"
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
# FinSera-BEJ

## Transfer Sesama Bank API Spec

### Check Data Rekening API Spec
Endpoint : POST /v1/transaction/check

Authorization Type Bearer Token : "USER_TOKEN"

Request Body:
```json
{
    "accountnum_recipient": "213818317",
    "nominal" : 100000,
    "note" : "Cicilan Motor"
}
```

Response Body (success) :

```json
{
    "data": {
        "transaction_num": 12138173913,
        "accountnum_recipient" : "213818317",
        "nominal" : "Rp.100.000",
        "note" : "Cicilan Motor" 
    },
    "message": "Data Rekening tersedia",
    "status": 200
}
```
Response Body (failed) :
1. Account Number is unavailable

```json
{
    "message": "Nomor Rekening Tidak Ditemukan",
    "status": 402
}
```
## Transfer Sesama Bank API Spec

Endpoint : POST /v1/transaction/create

Authorization Type Bearer Token : "USER_TOKEN"

Request Body:
```json
{
    "id_user": 1,
    "accountnum_recipient": "213818317",
    "nominal" : 100000,
    "note" : "Cicilan Motor",
    "pin" : "2213131",
}
```

Response Body (success) :

```json
{
    "data": {
        "transaction_num": 12138173913,
        "transaction_date": "08/07/2024 13:00",
        "name_sender" : "FinSera",
        "accountnum_sender" : "1234 567 897 890",
        "name_recipient" : "Binar",
        "accountnum_recipient" : "213818317",
        "nominal" : "Rp.100.000",
        "note" : "Cicilan Motor" 
    },
    "message": "Transaksi Berhasil",
    "status": 200
}
```
Response Body (failed) :
1. Account Number is unavailable

```json
{
    "message": "Nomor Rekening Tidak Ditemukan",
    "status": 402
}
```
2. The balance is insufficient

```json
{
    "message": "Saldo Anda Tidak Cukup",
    "status": 402
}
```
3. Incorrect Pin

```json
{
    "message": "Pin Anda Salah",
    "status": 402
}
```

