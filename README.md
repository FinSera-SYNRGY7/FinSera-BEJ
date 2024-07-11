
## Informasi Saldo API Spec

Endpoint : GET /v1/amount

Authorization Type Bearer Token : "USER_TOKEN"

Response Body (succes) :

```json
{
    "data": {
        "idUser": 1,
        "name": "Ramadhan",
        "amount" : "10.000.000",
        "appName" : "FinSera",
        "accountNumber" : "1234 567 897 890",
        "income" : "5.000.000",
        "expenses" : "500.000",
        "topUpEwallet" : "600.000" 
    },
    "message": "Berhasil mendapatkan data",
    "status": 200
}
```
Response Body (failed) :

```json
{
    "message": "Gagal mendapatkan data",
    "status": 400
}
```
