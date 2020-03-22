// Just a simple script to intially get some
// stuff into the local mongodb container

// Create the my_retail database
db = db.getSiblingDB("my_retail");

db.createUser({
  user: "coolUser",
  pwd: "coolPassword",
  roles: [{
    role: "readWrite",
    db: "my_retail"
  }]
});

// Insert data to a products collection
db.products.insert([
  {
    "_id": 13860428,
    "name": "The Big Lebowski (Blu-ray) (Widescreen)",
    "current_price": {
      "value": 13.49,
      "currency_code": "USD"
    }
  },
  {
    "_id": 12345678,
    "name": "Redken Shampoo (1qt)",
    "current_price": {
      "value": 24.99,
      "currency_code": "USD"
    }
  },
  {
    "_id": 11223344,
    "name": "Samsung SmartTV (75inch)",
    "current_price": {
      "value": 2399.99,
      "currency_code": "USD"
    }
  }
]);
