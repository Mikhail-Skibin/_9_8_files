import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import com.codeborne.selenide.selector.ByText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilesTest {


    @BeforeAll
    static void beforeAll() {
        // Configuration.browser = "firefox";
        // Configuration.browserSize = "1800x1000";
        Configuration.pageLoadTimeout = 60000;
    }

    @Test
    // @DisplayName("имя файла")
    void uploadFileTest() {
        open("https://the-internet.herokuapp.com/upload");
        File exampleFile = new File("src/test/resources/example.txt");
        // File exampleFile = new File("C:\\Users\\mick_\\IdeaProjects\\_9_8_files\\src\\test\\resources\\example.txt");
        $("[id=file-upload]").uploadFile(exampleFile);
        // $("[id=file-upload]").uploadFromClasspath("example.txt");
        $("[id=file-submit]").click();
        $("[id=uploaded-files]").shouldHave(text("example.txt"));
    }

    @Test
    void downloadTextFileTest() throws IOException {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File download = $("[data-testid=raw-button]").download();
        System.out.println(download.getAbsolutePath());
        // String fileContent = IOUtils.toString(new FileReader(download));
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("This repository"));
    }

    @Test
    void downloadPdfFileTest() throws IOException {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdf = $(new ByText("PDF download")).download();
        PDF parsedPdf = new PDF(pdf);
        assertEquals(179,parsedPdf.numberOfPages);
    }

    @Test
    void downloadXlsFileTest() throws IOException {
        open("https://romashka2008.ru/price");
        // File file = $$("a[href*='prajs']").find(text("Скачать Прайс-лист Excel")).download();
        File file = $(new ByText("Прайс от 10.11")).download();  // не видит кирилицу???
        XLS parsedXls = new XLS(file);
        /*boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(11)
                .getCell(1)
                .getStringCellValue()
                .contains("693010");
        assertTrue(checkPassed);*/
    }

    @Test
    void parseCsvFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("example.csv");
             Reader reader = new InputStreamReader(is)) {
            CSVReader csvReader = new CSVReader(reader);
            List<String[]> strings = csvReader.readAll();
            assertEquals(3, strings.size());
        }
    }

    @Test
    void parseZipFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("example.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }
    }

}
