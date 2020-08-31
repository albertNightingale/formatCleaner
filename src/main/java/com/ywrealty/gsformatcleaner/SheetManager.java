/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ywrealty.gsformatcleaner;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ywrealty.gsformatcleaner.bean.ClientInformation;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Albert Liu 
 */
public class SheetManager extends GooglesheetCredentials {

    private Logger logger = Logger.getLogger(SheetManager.class.getName());
    private final Sheets sheetsService; 
    
    public SheetManager(Properties prop) throws IOException, GeneralSecurityException {
        super(prop);
        
        sheetsService = createSheetsService();
    }
    
    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT); 
        
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(Defines.APPLICATION_NAME).build(); 
    }     
    
    /*******
     * Set one cell in the specified location bold. 
     * If the cell is already bold, leave it bold, otherwise, set it bold. 
     * @param value
     * @param startingrowindex
     * @param startcolumnindex
     * @throws IOException 
     */
    public void setBold(String value, int startingrowindex, int startcolumnindex) throws IOException {
        setbold(true, value, startingrowindex, startingrowindex + 1, startcolumnindex, startcolumnindex + 1);         
    }
    
    /********
     * Set one cell in the specified location as not bold.
     * If the cell is already not bold, then leave it not bold, 
     * otherwise, set it to be bold. 
     * @param value
     * @param startingrowindex
     * @param startcolumnindex
     * @throws IOException 
     */
    public void setUnbold(String value, int startingrowindex, int startcolumnindex) throws IOException
    {
        setbold(false, value, startingrowindex, startingrowindex + 1, startcolumnindex, startcolumnindex + 1); 
    }
    
    
    /*******
     * Helper method for set bold. 
     * @param isbold
     * @param val
     * @param sri
     * @param eri
     * @param sci
     * @param eci
     * @throws IOException 
     */
    private void setbold(boolean isbold, String val, int sri, int eri, int sci, int eci) throws IOException
    {
        List<Request> requests = new ArrayList<>();

        requests.add 
            ( new Request()
                .setRepeatCell(new RepeatCellRequest()
                    .setCell(new CellData()
                        .setUserEnteredValue(
                            new ExtendedValue()
                                .setStringValue(val)
                        )
                        .setUserEnteredFormat(
                            new CellFormat().setTextFormat(
                                new TextFormat().setBold(isbold)
                            )
                        )
                    )
                    .setRange(new GridRange()
                        .setSheetId(Integer.parseInt(Defines.TTLFORMATTED_GID))
                        .setStartRowIndex(sci)
                        .setEndRowIndex(eci)
                        .setStartColumnIndex(sri)
                        .setEndColumnIndex(eri)
                    )
                    .setFields("*")
                )
            );
        
        
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        
        BatchUpdateSpreadsheetResponse batchUpdateResponse = sheetsService.spreadsheets().batchUpdate(Defines.SPREADSHEET_ID, batchUpdateRequest)
                .execute();
    }
    
    /***********
     * Set the background color of the index
     * @param blue
     * @param red
     * @param green
     * @param startingrowindex
     * @param startcolumnindex 
     */
    public void setColor(float blue, float red, float green, int startingrowindex, int startcolumnindex)
    {
        List<Request> requests = new ArrayList<Request>();
        
        requests.add 
            ( new Request()
                .setRepeatCell(new RepeatCellRequest()
                    .setCell( new CellData()
                        .setUserEnteredFormat( new CellFormat()
                            .setBackgroundColor( new Color()
                                .setRed(red)
                                .setBlue(blue)
                                .setGreen(green)
                            )
                        )
                    )
                    .setRange(new GridRange()
                        .setSheetId(Integer.parseInt(Defines.TTLFORMATTED_GID))
                        .setStartRowIndex(startcolumnindex)
                        .setEndRowIndex(startcolumnindex + 1)
                        .setStartColumnIndex(startingrowindex)
                        .setEndColumnIndex(startingrowindex + 1)
                    )
                    .setFields("*")
                )
            );
    }
    
    /*******
     * Delete the specified number of rows starting from the beginning of the spreadsheet (row 0). 
     * @param numofrows
     * @throws IOException
     * @throws InterruptedException 
     */
    public void deleterows(int numofrows) throws IOException, InterruptedException
    {
        BatchUpdateSpreadsheetRequest content = new BatchUpdateSpreadsheetRequest();
        Request request = new Request()
                .setDeleteDimension(new DeleteDimensionRequest()
                  .setRange(new DimensionRange()
                    .setSheetId(Integer.parseInt(Defines.TTLFORMATTED_GID))
                    .setDimension("ROWS")
                    .setStartIndex(0)
                    .setEndIndex(numofrows)
                  )
                );

        List<Request> requests = new ArrayList<Request>();
        requests.add(request);
        content.setRequests(requests);

        sheetsService.spreadsheets().batchUpdate(Defines.SPREADSHEET_ID, content).execute();
    }  
    
    /*********
     * Append rows into the Google sheet
     * @param clientsdata
     * @throws IOException
     * @throws InterruptedException 
     */
    public void appendDatas(List<ClientInformation> clientsdata) throws IOException, InterruptedException  {

        String valueInputOption = "RAW"; // How the input data should be interpreted.
        String insertDataOption = "INSERT_ROWS"; // How the input data should be inserted.
        // Sheets sheetsService = createSheetsService();       
        
        List<List<Object>> content = new LinkedList<List<Object>>();

        String[] titles = {
                "Name 1","Name 2","Name 3","Telephone 1","Telephone 2", "Telephone 3", 
                "Email 1","Email 2","Email 3","Mailing Address","Recent Notes",
                "Past Notes","Date of Last Touch","Status"
        };
        
        content.add(Arrays.asList((titles)));
        
        int counter = 0;
        for (ClientInformation data : clientsdata)
        {
            content.add(Arrays.asList(
                   data.getName1(), data.getName2(), data.getName3(), data.getTele1(), data.getTele2(), data.getTele3(),
                   data.getEmail1(), data.getEmail2(), data.getEmail3(), data.getMailinginfo(), data.getRecentnotes(), 
                   data.getPastnotes(), data.getDate(), data.getStatus()
            ));   

            if (counter >= Defines.breakinginterval)
            {
                sleepABit(); 
                counter = 0; 
            }
            else
            {
                counter++;                
            }               
        }
                    
        ValueRange requestBody = new ValueRange().setValues(content);

        Sheets.Spreadsheets.Values.Append request = sheetsService.spreadsheets().values().
                    append(Defines.SPREADSHEET_ID, Defines.TTLFORMATTED_SHEETNAME, requestBody);
        request.setValueInputOption(valueInputOption);
        request.setInsertDataOption(insertDataOption);
        AppendValuesResponse response = request.execute();
     
        for (int startingindex = 0; startingindex < titles.length; startingindex++)
        {
            setBold(titles[startingindex], startingindex, 0); 
        }
    }

    /*******
     * Allows for accessing the full list of data. 
     * @param spreadsheetid
     * @param sheetname
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws InterruptedException 
     */
    public List<List<Object>> accessdata(String spreadsheetid, String sheetname) throws IOException, GeneralSecurityException, InterruptedException
    {
        ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetid, sheetname).execute(); 
        
        List<List<Object>> values = response.getValues();
        
        if (values == null || response.isEmpty())
        {
            logger.log(Level.INFO, "Sheet is Empty");
            return null; 
        }
        else
        {
            return values; 
        }
    }
    
    /******
     * Clears all rows in the Googlesheet
     * @throws IOException
     * @throws InterruptedException 
     */
    public void cleartable() throws IOException, InterruptedException 
    {
        String range = Defines.SPREADSHEET_ID; 

        ClearValuesRequest requestBody = new ClearValuesRequest();

        Sheets.Spreadsheets.Values.Clear request =
            sheetsService.spreadsheets().values().clear(Defines.SPREADSHEET_ID, range, requestBody);
        sleepABit();

        ClearValuesResponse response = request.execute();
    }
        
    private String timetostr(Timestamp t)
    {
        if (t == null)
        {
            return "NULL";
        }
        else
        {
            if (!t.toString().contains("-"))
                System.out.println(t.toString());
            return t.toString().split(" ")[0]; 
        }
    }
    
    // allows the program to pause a bit for the Google server to catch up. 
    private static void sleepABit() throws InterruptedException {    
        Thread.sleep(5000);
    }
        
}
