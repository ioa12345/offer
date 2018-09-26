# offer

This is a simple Spring Boot Application involving a controller with 3 main methods:

- CREATE OFFER
- RETRIEVE OFFER
- CANCEL OFFER

To run the application simply right click on Application and select Run.
Alternatively go to Maven Projects-> Plugins-> Double Click spring-boot:run

Assumptions:

Offer must have following fields as mandatory
   -id
   -description
   -validityTime
   -price

Price must have following fields as mandatory
   -currency
   -ammount

The period of time the offer should expire is in days.

Failure to send any mandatory field will result in a validation error

Important fields:

Offer.StartDate##
  -Offer.StartDate is the date when the offer starts being valid
  -It must be in ISO_DATE format, otherwise a validation error will be thrown
  -One cannot create an offer with startDate in the past. However startDate as null is accepted as the application will internally set it to LocalDate.now()

Offer.valididtyTime
  - Offer.valididtyTime is the time for which the offer is valid.
  - It is an Integer respresenting a number of days.
  - startDate+validityTime should be after currentDate. Otherwise the application will set the offer as cancelled and return a customized error message.


Offer.id
  - Offer.id is unique for each offer. If another request will be sent with an id that already exists, the offer will get updated.
  - Assumed that the implementation of a PUT update offer endpoint was not necessary at this stage