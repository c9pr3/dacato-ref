package cs.ecs.jdaoref;

import co.ecso.jdao.ConnectionPool;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Application.
 *
 * @author Christian Senkowski (cs@2scale.net)
 * @version $Id:$
 * @since 29.08.16
 */
public final class Application {

    public static void main(final String ...params) {
        final ApplicationConfig config = new ApplicationConfig();

        try {
            setupTestDatabase(config);
            final Customers customers = new Customers(config);

            //now execute all
            CompletableFuture.allOf(
                    createAndGetFirstName(customers),
                    createAndCombineFirstNameAndNumber(customers),
                    createAndChainClean(customers),
                    createAndCombineTwo(customers),
                    createAndComposeCombine(customers),
                    createLotsOfNewEntries(customers)
            ).get(10, TimeUnit.SECONDS);

            //let's see how many we got
            final List<Customer> allCustomers = customers.findAll().get(10, TimeUnit.SECONDS);
            if (allCustomers.size() != 64) {
                throw new RuntimeException("Concurrency problem. Throw jdao away.");
            }
            System.out.println("After all we have " + allCustomers.size() + " customers in database");

            System.out.println("Truncating");
            customers.removeAll().get();
            final List<Customer> allCustomers2 = customers.findAll().get(10, TimeUnit.SECONDS);
            System.out.println("Now we have " + allCustomers2.size() + " customers in database");

        } catch (final SQLException | InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private static CompletableFuture<Void> createLotsOfNewEntries(final Customers customers) {
        return CompletableFuture.allOf(
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L),
                customers.add("foo", "bar", 1L)
        );
    }

    //compose and combine
    private static CompletableFuture<CompletableFuture<Object>> createAndComposeCombine(final Customers customers) {
        return customers.add("IAmAFirstName5", "IamALastName5", 5L)
                        .thenApply(newCustomer5 -> newCustomer5.firstName().thenCompose(
                                firstName -> newCustomer5.lastName().thenCombine(
                                        newCustomer5.number(), (lastName, number) -> {
                                            System.out.printf("Found customer %d, firstName %s, number %s, lastName: %s%n",
                                                    newCustomer5.id(), firstName, number, lastName);
                                            return null;
                                        })));
    }

    //combine two
    private static CompletableFuture<PrintStream> createAndCombineTwo(final Customers customers) {
        return customers.add("IAmAFirstName4", "IamALastName4", 4L).thenCompose(newCustomer4 ->
                        newCustomer4.firstName().thenCombine(newCustomer4.lastName(), (firstName, lastName) ->
                                System.out.printf("Found customer %d, firstName %s, lastName: %s%n",
                                        newCustomer4.id(), firstName, lastName)));
    }

    //Chain and "combine" clean
    private static CompletableFuture<CompletableFuture<Void>> createAndChainClean(final Customers customers) {
        return customers.add("IAmAFirstName3", "IamALastName3", 3L)
                        .thenApply(newCustomer3 -> newCustomer3.firstName().thenCompose(
                                firstName -> newCustomer3.lastName().thenCompose(
                                        lastName -> newCustomer3.number().thenAccept(
                                                number -> System.out.printf("Found customer %d, firstName %s, number %s, lastName: %s%n",
                                                        newCustomer3.id(), firstName, number, lastName)))));
    }

    //Chain and get firstname only
    private static CompletableFuture<Void> createAndGetFirstName(final Customers customers) {
        return customers.add("IamAfirstName", "IamALastName", 1L)
                        .thenCompose(Customer::firstName)
                        .thenAccept(firstName -> System.out.printf("found customer  , firstName %s%n", firstName));
    }

    //Create a new customer and THEN combine number and firstname calls
    private static CompletableFuture<Object> createAndCombineFirstNameAndNumber(final Customers customers)
            throws InterruptedException, ExecutionException {
        final Customer newCustomer2 = customers.add("IamAfirstName2", "IamALastName2", 2L)
                .thenCompose(newCustomer1 -> customers.findOne(CompletableFuture.completedFuture(newCustomer1.id()))).get();
        return newCustomer2.firstName().thenCombine(newCustomer2.number(), (firstName, number) -> {
            System.out.printf("Found customer %d, firstName %s, number %d%n", newCustomer2.id(), firstName, number);
            return null;
        });
    }

    //set up database
    private static void setupTestDatabase(final ApplicationConfig config) throws SQLException {
        try (Connection connection = config.getConnectionPool().getConnection()) {
               try (final Statement statement = connection.createStatement()) {
                   statement.execute(filter("" +
                           "CREATE TABLE `customer` (\n" +
                           "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                           "  `customer_first_name` varchar(255) NOT NULL,\n" +
                           "  `customer_last_name` varchar(255) NOT NULL,\n" +
                           "  `customer_number` bigint(20) NOT NULL,\n" +
                           "  PRIMARY KEY (`id`),\n" +
                           ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;" +
                           ""));
               }
        } catch (final Exception ignored) {
            //
        }
    }

    //we now may use mysql syntax
    private static String filter(final String s) {
        return s
                .replaceAll("/\\*.*?\\*/", "")
                .replaceAll("`|´", "")
                .replaceAll("TINYINT", "INTEGER")
                .replaceAll("\\) ENGINE.*?;", ")")
                .replaceAll("VARCHAR|varchar", "CHAR(255)")
                .replaceAll("LONGTEXT|longtext", "CHAR(255)")
                .replaceAll("([a-zA-Z]+)\\([0-9]+\\)", "$1")
                .replaceAll("AUTO_INCREMENT", "generated by default as identity")
                .replaceAll("(ENUM|enum)\\(.*?\\)", "CHAR(255)")
                .replaceAll("((?:NOT )?NULL)[\\s ]DEFAULT[\\s ](.*?)([\\s, ])", "default $2 $1 $3")
                .replaceAll("char ", "char(255) ")
                .replaceAll("CREATE DATABASE (.*?);", "create schema $1 authorization dba;")
                .replaceAll("\\),\\)", "))");
    }
}
