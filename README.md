This is a mobile app for android that allows users to scan 2D barcodes using their camera.
After which the user gets redirected to a screen containing a form where they can enter an item description and quantity.
Then the app makes a REST API call to an AWS EC2 instance that is hooked up to an MySQL RDS database to add the item to the table.
Users can also view what is already in the table and update the quantity of and delete items stored in the SQL table.
