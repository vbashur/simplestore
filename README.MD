

### HTTP routes

#### Category API
Create new category
```
POST       /api/catalogue/category
```
Request body example:
````json
{
    "name": "Alcohol"
}
````
Read all available categories
```
GET        /api/catalogue/category
```
Find category by name
```
GET        /api/catalogue/category/name/:name
```
Find category by uuid
```
GET        /api/catalogue/category/:uuid
```
Update category by category uuid, correct category is represented in JSON format must be provided as a payload
```
PATCH     /api/catalogue/category/:uuid
```
Delete category by category uuid
```
DELETE   /api/catalogue/category/:uuid
```
#### Product API
Create new product
```
POST       /api/catalogue/product
```
Request body example:
````json
{
    "name": "Paulaner Helles",
    "price": 1.09,
    "currency" : "EUR",
    "category": [
     {
        "uuid": "52d84330-4bf2-4e2e-a713-44dd2d24c41c",
        "name": "Alcohol"
     }
    ]
}
````

> For Read operations add optional 'currency' argument to get products price in specific currency.

Read all available products.
```
GET        /api/catalogue/product?currency=:currency
```
Find product by name. Add optional 'currency' argument to get products in specific currency
```
GET        /api/catalogue/product/name/:name?currency=:currency
```
Find product by uuid
```
GET        /api/catalogue/product/:uuid?currency=:currency
```
Find products by category name
```
GET        /api/catalogue/product/category/name/:categoryName?currency=:currency
```
Find products by category uuid
```
GET        /api/catalogue/product/category/:categoryUuid?currency=:currency
```
Update product by product uuid, correct product represented in JSON format must be provided as a payload
```
PATCH     /api/catalogue/product/:uuid
```
Delete product by product uuid
```
DELETE   /api/catalogue/product/:uuid
```


_if product or category name contains space ' ' replace it with '%20', for example /api/catalogue/product/name/Paulaner%20Helles, [see more details](https://en.wikipedia.org/wiki/Percent-encoding)_

