# API_Integration
Practicing how to integrate APIs with AWS Lambda to perform GET and PUT operations against a DynamoDB table.

Idea: Haj/Umrah-themed travel app.
GET API fetches hotels, currently by name. Will change to city name, e.g. Makkah
PUT API inserts a hotel to the database (on AWS DynamoDB) with the params: Name, City, Price, Stars.

To-Do:
1. Learn how to use OAuth to secure the API endpoints
2. Learn to integrate with Salesforce, maybe have a Hotel object where we can store records
3. Try to resolve difficulties in passing a Hotel class, which was passing null and 0 values.
