# FinSera-BEJ

## Transfer Sesama Bank API Spec

Endpoint : POST /v1/create-transaction

Authorization Type Bearer Token : "USER_TOKEN"

Request Body:
```json
{
    "idUser": 1,
    "accountnum_recipient": "213818317",
    "nominal" : 100000,
    "note" : "Cicilan Motor",
    "pin" : 2213131,
}
```

Response Body (success) :

```json
{
    "data": {
        "transaction_num": 12138173913,
        "transaction_Date": "08/07/2024 13:00",
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
