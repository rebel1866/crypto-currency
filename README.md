-Application uses ExecutorService to manage multi-thread logic. Additionally to main thread,
the second thread is being executed once application has been started.This thread refreshes the state
of database and put there relevant values of currency prices. Also, every time when method "notify" is invoked,
one more thread is started to check changes of prices, and log them, if it's more than 1%.

-All potential exceptions are handled through ErrorHandler class.

-HATEOAS is used for better navigability.

-Mockito is used as testing framework for service layer.

-Code is documented with javadoc.

-All data passed in "notify" method (username and etc.) is validated with the help of Hibernate Validator.

-For database access Spring Data Jpa is integrated in application.

-ApplicationStartedListener is intended to start new thread (for checking currency prices) when application is ready.

-Lombok is used in entities and dto classes.

-Database scheme and screenshot of diagram are attached.

-Layered architecture is used in application.

-Dto object are used for passing data from controller to service and vice versa. Data between service and dao layer
passed through entity classes. Custom dto to entity and entity to dto converters are implemented.




