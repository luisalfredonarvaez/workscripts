import com.redhat.workscripts.Main;
import com.redhat.workscripts.config.ConfigPropertiesException;
import com.redhat.workscripts.config.ConfigPropertiesReaderFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class TestConfigPropertiesReaderFactory
{
    private final String invalidArgumentForParameter="dddsdssddsds";

    @Test
    public void testMainCommandLineArgumentsFail()
    {
        //One single invalid argument
        String invalidProperty;
        ConfigPropertiesException exception = assertThrows(ConfigPropertiesException.class, () -> {
                String[] args = new String[1];
                args[0] = invalidArgumentForParameter;
                Main.main(args);
        });
        invalidProperty = exception.getInvalidProperty();

        assertEquals(invalidArgumentForParameter , invalidProperty);


        //One invalid argument and two valid ones
        exception = assertThrows(ConfigPropertiesException.class, () -> {
            String [] args = new String[3];
            args[0] = "a=b";
            args[1] = "b=c";
            args[2] = invalidArgumentForParameter;
            Main.main(args);
        });
        invalidProperty = exception.getInvalidProperty();

        assertEquals(invalidArgumentForParameter , invalidProperty);

        exception = assertThrows(ConfigPropertiesException.class, () -> {
            //note that args[2] = null
            String[] args = new String[3];
            args[0] = "a=b";
            args[1] = "b=c";
            Main.main(args);
        });
        invalidProperty = exception.getInvalidProperty();

        assertEquals(null , invalidProperty);
    }


    @Test
    public void testMainCommandLineArgumentsSuccess()
    {
        String[] args = new String[2];
        args[0] = "a=b";
        args[1] = "b=c";
        assertDoesNotThrow(() -> Main.main(args));
    }

    @Test
    public void testFactoryCommandLineArgumentsSuccess()
    {
        String[] args = new String[3];
        args[0] = "a=b";
        args[1] = "b=c";
        args[2] = "s=c=c";
        Properties properties = ConfigPropertiesReaderFactory.createFromCommandLineArguments(args);
        assertEquals("b", properties.getProperty("a"));
        assertEquals("c=c", properties.getProperty("s"));
    }

    /*
    * testFactoryCommandLineArgumentsFail() is not necessary, as the same has been tested in
    * testMainCommandLineArgumentsFail
    * */


    @Test
    public void testFactoryFileSuccess() throws IOException
    {
        File f = File.createTempFile("test", "test");
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write("a=b\n");
        fileWriter.write("b=c\n");
        fileWriter.write("s=c=c\n");
        fileWriter.close();
        Properties properties = ConfigPropertiesReaderFactory.createFromFile(f.getAbsolutePath());
        assertEquals("b", properties.getProperty("a"));
        assertEquals("c", properties.getProperty("b"));
        assertEquals("c=c", properties.getProperty("s"));
    }

// No way to make testFactoryFileFail()
// "asdsddb\n" in the file is interpreted as an empty property, not as an invalid property

//    @Test
//    public void testFactoryFileFail() throws IOException
//    {
//        File f = File.createTempFile("test", "test");
//        FileWriter fileWriter = new FileWriter(f);
//        fileWriter.write("asdsddb\n");
//        fileWriter.write("b=c\n");
//        fileWriter.write("s=c=c\n");
//        fileWriter.close();
//        Properties properties = ConfigPropertiesReaderFactory.createFromFile(f.getAbsolutePath());
//        properties.list(System.out);
//    }
}
