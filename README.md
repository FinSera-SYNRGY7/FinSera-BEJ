# FinSera-BEJ

## Get User Profile

Endpoint : GET /v1/profile

Authorization Type Bearer Token : "USER_TOKEN"

Request Body:
```json
{  
    "idUser": 1  
}
```

Response Body (success) :

```json
{  
    "customer": {  
        "id": 1,  
        "name": "John Doe",  
        "nik": "123456789",  
        "address": "123 Main Street",  
        "gender": "Male",  
        "father_name": "Doe Sr.",  
        "mother_name": "Doe Sr.",  
        "phone_number": "123-456-7890",  
        "income": 50000,  
        "username": "johndoe",  
        "mpin": "1234",  
        "status_user": "Active"  
    }  
}
```
Response Body (failed) :
1. User ID Not Found 

```json
{
    "message": "User Not Found",  
    "status": 402  
}
```
