## Application.yml:

#### Have mentioned username and password.

## Booking Service:

#### Code and its logic is correct. I think no need to mention this:

if (user.getId() != booking.getUser().getId()) {
throw new EntityNotFoundException("Booking", bookingId);
}
if (taxiId != booking.getTaxi().getId()) {
throw new EntityNotFoundException("Taxi", taxiId);
}
if (booking.getStatus().equals(Status.CANCELLED)) {
throw new BookingAlreadyCancelledException();
}

#### As he already throws an exception for Taxi, Booking. Even if these of codes are not there. It will run successfully and throws exception.

