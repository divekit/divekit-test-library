package thkoeln.st.springtestlib.specification.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class RowAndColumnTests {

    private GenericTableSpecificationTests genericTableSpecificationTests = null;
    private Map<String, TableTestAspect[]> testCases;


    @BeforeEach
    public void setUp() {
        this.genericTableSpecificationTests = new GenericTableSpecificationTests();
        testCases = new HashMap<>();

        testCases.put( "Aggregates", new TableTestAspect[] {
                new TableTestAspect( "missing-inner", InputMismatchException.class,
                        "Cell content is not matching. Row: \"SpaceShip\"" ),
                new TableTestAspect( "surplus-inner", InputMismatchException.class,
                        "Cell content is not matching. Row: \"Planet\"" ),
                new TableTestAspect( "wrong-root", InputMismatchException.class,
                        "Too many rows." )
        } );
        testCases.put( "Rest", new TableTestAspect[] {
                new TableTestAspect( "wrong-case", InputMismatchException.class,
                        "Cell content is not matching. Row: \"Get a specific space ship by ID\"" ),
                new TableTestAspect( "wrong-verb", InputMismatchException.class,
                        "Cell content is not matching. Row: \"Create a new space ship\"" )
        } );
    }


    @Test
    public void testSyntaxChecksForWellFormedTables() {
        for ( Map.Entry<String, TableTestAspect[]> entry : testCases.entrySet() ) {
            for ( TableTestAspect aspect : entry.getValue() ) {
                String testCaseName = entry.getKey();
                assertDoesNotThrow( () -> {
                    genericTableSpecificationTests.testTableSyntax(
                            getStudentTable( testCaseName, aspect.getAspectName() ),
                            getConfig( testCaseName ),
                            TableType.ROWS_AND_COLUMNS );
                });
            }
        }
    }

    @Test
    public void testSyntaxChecksForMalFormedTables() {
        Throwable exception = assertThrows( InputMismatchException.class, () -> {
            genericTableSpecificationTests.testTableSyntax(
                    getStudentTable( "Aggregates", "invalid-syntax1" ),
                    getConfig( "Aggregates" ),
                    TableType.ROWS_AND_COLUMNS ); });
        assertEquals( "Each table line needs the same number of \"|\" chars", exception.getMessage() );

        exception = assertThrows( InputMismatchException.class, () -> {
                    genericTableSpecificationTests.testTableSyntax(
                            getStudentTable( "Aggregates", "invalid-syntax2" ),
                            getConfig( "Aggregates" ),
                            TableType.ROWS_AND_COLUMNS ); });
                assertEquals( "Each table line needs the same number of \"|\" chars", exception.getMessage() );
    }


    @Test
    public void testOkTables() {
        for ( Map.Entry<String, TableTestAspect[]> entry : testCases.entrySet() ) {
            for ( TableTestAspect aspect : entry.getValue() ) {
                String testCaseName = entry.getKey();
                System.out.println( "Testing " + testCaseName + " with " + aspect.getAspectName() );
                assertDoesNotThrow( () -> {
                    genericTableSpecificationTests.testTableSpecification(
                            getSolution( testCaseName ),
                            getStudentTable( testCaseName, "ok" ),
                            getConfig( testCaseName ),
                            TableType.ROWS_AND_COLUMNS );
                });
            }
        }
    }

    @Test
    public void testFaultyTables() {
        for ( Map.Entry<String, TableTestAspect[]> entry : testCases.entrySet() ) {
            for ( TableTestAspect aspect : entry.getValue() ) {
                String testCaseName = entry.getKey();
                System.out.println( "Testing " + testCaseName + " with " + aspect.getAspectName() );
                Throwable exception = assertThrows( aspect.getExpectedException(), () -> {
                    genericTableSpecificationTests.testTableSpecification(
                            getSolution( testCaseName ),
                            getStudentTable( testCaseName, aspect.getAspectName() ),
                            getConfig( testCaseName ),
                            TableType.ROWS_AND_COLUMNS ); });
                assertEquals( aspect.getExpectedExceptionMessage(), exception.getMessage() );
            }
        }
    }



    private String getConfig( String useCaseName ) {
        return "specification/table/solutions/" + useCaseName + "-config.json";
    }

    private String getSolution( String useCaseName ) {
        return "specification/table/solutions/" + useCaseName + "-solution.md";
    }

    private String getStudentTable( String useCaseName, String error ) {
        return "specification/table/" + useCaseName + "-" + error + ".md";
    }

}
