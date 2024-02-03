# Taxi Booking System
The Taxi Booking System is a Spring Boot application that provides a platform for users to book taxis, manage their bookings, and taxi operators to add and manage taxis. The system also includes user authentication for securing user data.

## User Operations:

/v1/user/signup (POST): Sign up a new user.

/v1/user/login (POST): Log in an existing user.

/v1/user/balance (PUT): Update user account balance.

## Taxi Operations:

/taxi (POST): Add a new taxi.

## Booking Operations:

/book/searchTaxi (GET): Search for the nearest available taxi.

/book (POST): Book a taxi.

/book/{bookingId} (GET): View booking details.

/book/cancel/{bookingId} (PUT): Cancel a booking.
