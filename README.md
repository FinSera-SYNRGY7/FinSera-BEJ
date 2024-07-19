
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
Response Body (failed) :

```json
{
    "message": "Nomor rekening tidak ditemukan",
    "status": 400
}
```
